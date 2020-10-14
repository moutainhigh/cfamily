package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForPlusProductsInput;
import com.cmall.familyhas.api.result.ApiForPlusProductsResult;
import com.cmall.familyhas.api.result.PlusSaleProduct;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfoPlus;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.PlusServiceEventPlus;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webmodel.MOauthInfo;

/** 
 * 橙意卡商品列表
* @author Angel Joy
* @Time 2020年5月11日 下午1:08:24 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForPlusProducts extends RootApiForVersion<ApiForPlusProductsResult, ApiForPlusProductsInput> {

	@Override
	public ApiForPlusProductsResult Process(ApiForPlusProductsInput inputParam, MDataMap mRequestMap) {
		Integer nowPage = inputParam.getCurrentPage();
		Integer pageNum = inputParam.getPageNum();
		Integer start = (nowPage -1)*pageNum;
		//Integer end = nowPage*pageNum;
		Integer total = 0;
		ApiForPlusProductsResult result = new ApiForPlusProductsResult();
		String sql = "SELECT * FROM systemcenter.sc_event_info WHERE event_type_code = '4497472600010026' AND event_status = '4497472700020002' AND begin_time <= sysdate() AND end_time > sysdate() limit 1";//查询橙意卡专享活动
		Map<String,Object> eventInfo = DbUp.upTable("sc_event_info").dataSqlOne(sql, null);
		if(eventInfo == null) {
			result.setResultCode(2);
			result.setResultMessage("当前暂无活动");
			return result;
		}
		MDataMap mDataMap = new MDataMap(eventInfo);
		String event_code = mDataMap.get("event_code");
		if(StringUtils.isEmpty(event_code)) {
			result.setResultCode(2);
			result.setResultMessage("活动异常");
			return result;
		}
		MDataMap plusEventProducts = DbUp.upTable("sc_event_plus").one("event_code",event_code);
		if(plusEventProducts == null || plusEventProducts.isEmpty()) {
			result.setResultCode(2);
			result.setResultMessage("暂无活动商品。");
			return result;
		}
		String product_limit_type = plusEventProducts.get("product_limit");
		String productCodes = this.checkStringReg(plusEventProducts.get("product_codes"));
		if("4497476400020002".equals(product_limit_type)&&StringUtils.isEmpty(productCodes)) {
			result.setResultCode(2);
			result.setResultMessage("活动商品设置错误");
			return result;
		}
		String [] productCodeArr = productCodes.split(",");
		String memberCode = "";
		if(getFlagLogin()) {
			MOauthInfo oauthInfo = getOauthInfo(); 
			memberCode = oauthInfo.getUserCode();
		}
		String channelId = getChannelId();
		if(StringUtils.isEmpty(channelId)) {
			channelId = "449747430001";//惠家有APP
		}
		if("4497476400020002".equals(product_limit_type)) {//仅包含
			String inWhere = "";
			for(int i = 0;i<productCodeArr.length;i++) {
				if(StringUtils.isEmpty(productCodeArr[i])||",".equals(productCodeArr[i])) {
					continue;
				}
				inWhere += "\'"+productCodeArr[i]+"\',";
			}
			String sql2  = "";
			String countSql = "";
			if(inWhere.length()>1) {
				String where = inWhere.subSequence(0, inWhere.length()-1).toString();
				sql2 = "SELECT * FROM " + 
						"(SELECT ps.product_code,ps.sku_code ,SUM(sss.stock_num) stock_num,pp.product_status " + 
						"FROM productcenter.pc_productinfo pp  " + 
						"LEFT JOIN productcenter.pc_skuinfo ps ON pp.product_code = ps.product_code " + 
						"LEFT JOIN systemcenter.sc_store_skunum sss ON sss.sku_code = ps.sku_code  " + 
						"WHERE pp.small_seller_code = 'SI2003' AND pp.product_status = '4497153900060002' AND pp.product_code in ("+where+") GROUP BY pp.product_code) t where t.stock_num >0 limit "+start+","+pageNum;
				countSql = "SELECT COUNT(1) num FROM " + 
						"(SELECT ps.product_code,ps.sku_code ,SUM(sss.stock_num) stock_num,pp.product_status " + 
						"FROM productcenter.pc_productinfo pp  " + 
						"LEFT JOIN productcenter.pc_skuinfo ps ON pp.product_code = ps.product_code " + 
						"LEFT JOIN systemcenter.sc_store_skunum sss ON sss.sku_code = ps.sku_code  " + 
						"WHERE pp.small_seller_code = 'SI2003' AND pp.product_status = '4497153900060002' AND pp.product_code in ("+where+") GROUP BY pp.product_code) t where t.stock_num >0 ";
				List<Map<String,Object>> productMaps = DbUp.upTable("pc_productinfo").dataSqlList(sql2, null);
				for(Map<String,Object> pro : productMaps) {
					if(pro == null) {
						continue;
					}
					PlusSaleProduct product = this.getProduct(pro.get("product_code").toString(), memberCode,channelId);
					if(product == null) {
						continue;
					}
					result.getProducts().add(product);
				}
				Map<String,Object> countMap = DbUp.upTable("pc_productinfo").dataSqlOne(countSql, null);
				total = countMap.get("num")!=null?Integer.parseInt(countMap.get("num").toString()):0;
			}else {
				result.setResultCode(0);
				result.setResultMessage("活动失效！");
				return result;
			}
		}else if("4497476400020003".equals(product_limit_type)){
			String inWhere = "";
			for(int i = 0;i<productCodeArr.length;i++) {
				if(StringUtils.isEmpty(productCodeArr[i])||",".equals(productCodeArr[i])) {
					continue;
				}
				inWhere += "\'"+productCodeArr[i]+"\',";
			}
			String sql2  = "";
			String countSql = "";
			if(inWhere.length()>1) {
				String where = inWhere.subSequence(0, inWhere.length()-1).toString();
				sql2 = "SELECT * FROM " + 
						"(SELECT ps.product_code,ps.sku_code ,SUM(sss.stock_num) stock_num,pp.product_status " + 
						"FROM productcenter.pc_productinfo pp  " + 
						"LEFT JOIN productcenter.pc_skuinfo ps ON pp.product_code = ps.product_code " + 
						"LEFT JOIN systemcenter.sc_store_skunum sss ON sss.sku_code = ps.sku_code  " + 
						"WHERE pp.small_seller_code = 'SI2003' AND pp.product_status = '4497153900060002' AND pp.product_code not in ("+where+") GROUP BY pp.product_code) t where t.stock_num >0 limit "+start+","+pageNum;
				countSql = "SELECT COUNT(1) num FROM " + 
						"(SELECT ps.product_code,ps.sku_code ,SUM(sss.stock_num) stock_num,pp.product_status " + 
						"FROM productcenter.pc_productinfo pp  " + 
						"LEFT JOIN productcenter.pc_skuinfo ps ON pp.product_code = ps.product_code " + 
						"LEFT JOIN systemcenter.sc_store_skunum sss ON sss.sku_code = ps.sku_code  " + 
						"WHERE pp.small_seller_code = 'SI2003' AND pp.product_status = '4497153900060002' AND pp.product_code not in ("+where+") GROUP BY pp.product_code) t where t.stock_num >0 ";
			}else {
				sql2 = "SELECT * FROM " + 
						"(SELECT ps.product_code,ps.sku_code ,SUM(sss.stock_num) stock_num,pp.product_status " + 
						"FROM productcenter.pc_productinfo pp  " + 
						"LEFT JOIN productcenter.pc_skuinfo ps ON pp.product_code = ps.product_code " + 
						"LEFT JOIN systemcenter.sc_store_skunum sss ON sss.sku_code = ps.sku_code  " + 
						"WHERE pp.small_seller_code = 'SI2003' AND pp.product_status = '4497153900060002' GROUP BY pp.product_code) t where t.stock_num >0 limit "+start+","+pageNum;
				countSql = "SELECT COUNT(1) num FROM " + 
						"(SELECT ps.product_code,ps.sku_code ,SUM(sss.stock_num) stock_num,pp.product_status " + 
						"FROM productcenter.pc_productinfo pp  " + 
						"LEFT JOIN productcenter.pc_skuinfo ps ON pp.product_code = ps.product_code " + 
						"LEFT JOIN systemcenter.sc_store_skunum sss ON sss.sku_code = ps.sku_code  " + 
						"WHERE pp.small_seller_code = 'SI2003' AND pp.product_status = '4497153900060002' GROUP BY pp.product_code) t where t.stock_num >0 ";
			}
			List<Map<String,Object>> productMaps = DbUp.upTable("pc_productinfo").dataSqlList(sql2, null);
			for(Map<String,Object> pro : productMaps) {
				if(pro == null) {
					continue;
				}
				PlusSaleProduct product = this.getProduct(pro.get("product_code").toString(), memberCode,channelId);
				if(product == null) {
					continue;
				}
				result.getProducts().add(product);
			}
			Map<String,Object> countMap = DbUp.upTable("pc_productinfo").dataSqlOne(countSql, null);
			total = countMap.get("num")!=null?Integer.parseInt(countMap.get("num").toString()):0;
		}else {
			String sql2 = "SELECT * FROM " + 
					"(SELECT ps.product_code,ps.sku_code ,SUM(sss.stock_num) stock_num,pp.product_status " + 
					"FROM productcenter.pc_productinfo pp  " + 
					"LEFT JOIN productcenter.pc_skuinfo ps ON pp.product_code = ps.product_code " + 
					"LEFT JOIN systemcenter.sc_store_skunum sss ON sss.sku_code = ps.sku_code  " + 
					"WHERE pp.small_seller_code = 'SI2003' AND pp.product_status = '4497153900060002' GROUP BY pp.product_code) t where t.stock_num >0 limit "+start+","+pageNum;
			String countSql = "SELECT COUNT(1) num FROM " + 
					"(SELECT ps.product_code,ps.sku_code ,SUM(sss.stock_num) stock_num,pp.product_status " + 
					"FROM productcenter.pc_productinfo pp  " + 
					"LEFT JOIN productcenter.pc_skuinfo ps ON pp.product_code = ps.product_code " + 
					"LEFT JOIN systemcenter.sc_store_skunum sss ON sss.sku_code = ps.sku_code  " + 
					"WHERE pp.small_seller_code = 'SI2003' AND pp.product_status = '4497153900060002' GROUP BY pp.product_code) t where t.stock_num >0";
			List<Map<String,Object>> productMaps = DbUp.upTable("pc_productinfo").dataSqlList(sql2, null);
			Map<String,Object> countMap = DbUp.upTable("pc_productinfo").dataSqlOne(countSql, null);
			total = countMap.get("num")!=null?Integer.parseInt(countMap.get("num").toString()):0;
			for(Map<String,Object> pro : productMaps) {
				if(pro == null) {
					continue;
				}
				PlusSaleProduct product = this.getProduct(pro.get("product_code").toString(), memberCode,channelId);
				if(product == null) {
					continue;
				}
				result.getProducts().add(product);
			}
		}
		if(total > 200) {//限制数据展示，最多两百条
			total = 200;
		}
		if(total % pageNum == 0) {
			result.setTotalPage(total/pageNum);
		}else {
			result.setTotalPage(total/pageNum+1);
		}
		result.setCurentPage(inputParam.getCurrentPage());
		return result;
	
	}

	private PlusSaleProduct getProduct(String productCode, String memberCode, String channelId) {
		ProductService pService = new ProductService();//实例化ProductService
		PlusSupportProduct plusSupportProduct = new PlusSupportProduct();
		PlusSaleProduct product = new PlusSaleProduct();
		product.setProductCode(productCode);
		PlusModelProductInfo productInfo = plusSupportProduct.upProductInfo(productCode);
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		skuQuery.setCode(productCode);
		skuQuery.setChannelId(channelId);
		Map<String,BigDecimal> priceMap = new ProductPriceService().getProductMinPrice(skuQuery);
		if(priceMap != null) {
			product.setOrgSellPrice(priceMap.get(productCode));
		}
		PlusServiceEventPlus psep = new PlusServiceEventPlus();
		PlusModelEventInfoPlus plusEvent = psep.getEventInfoPlusUseCache();
		if(plusEvent == null) {//当前无橙意卡活动
			product.setPlusVipPrice(product.getOrgSellPrice());
		}else {
			BigDecimal plusMoney = product.getOrgSellPrice().multiply(plusEvent.getPrice()).setScale(0, BigDecimal.ROUND_HALF_UP);
			product.setPlusVipPrice(plusMoney);
		}
		product.setProductName(productInfo.getProductName());
		product.setProductUrl(productInfo.getMainpicUrl());
		Map<String,String> productTypeMap = WebHelper.getAttributeProductType("4497478100050000");
		if(productTypeMap != null && productTypeMap.get("proTypeInfoPic") != null) {
			product.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
		}
		
		// 新增TagList商品活动标签
		List<TagInfo> tagInfoList = pService.getProductTagInfoList(productCode, memberCode, channelId);
		product.setTagInfoList(tagInfoList);
		
		//排除橙意卡商品。
		if(bConfig("familyhas.plus_product_code").equals(productCode)) {
			return null;
		}
		
		return product;
	}
	
	private String checkStringReg(String a) {
		if(a.startsWith(",")) {
			a = a.substring(1, a.length());
		}
		if(a.endsWith(",")) {
			a = a.substring(0, a.length()-1);
		}
		return a;
	}
	
}
