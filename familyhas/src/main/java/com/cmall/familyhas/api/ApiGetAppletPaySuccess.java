package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetAppletPaySuccessInput;
import com.cmall.familyhas.api.model.ProductMaybeLove;
import com.cmall.familyhas.api.result.ApiGetAppletPaySuccessResult;
import com.cmall.familyhas.util.ProductCodeCopyLoader;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductLabelService;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 获取小程序支付完成配置商品
 * @author lgx
 *
 */
public class ApiGetAppletPaySuccess extends RootApiForVersion<ApiGetAppletPaySuccessResult, ApiGetAppletPaySuccessInput> {

	private final int pageSize = 10;
	
	@Override
	public ApiGetAppletPaySuccessResult Process(ApiGetAppletPaySuccessInput inputParam, MDataMap mRequestMap) {
		ApiGetAppletPaySuccessResult apiResult = new ApiGetAppletPaySuccessResult();
		
		int pageIndex = inputParam.getPageIndex();
		int iStart = (pageIndex - 1) * pageSize;
		
		String title = bConfig("familyhas.appletPaySuccessTitle");
		int pagination = 0;
		List<ProductMaybeLove> loveList = new ArrayList<ProductMaybeLove>();

		String sql = "SELECT ap.*, " + 
				"	(SELECT sum(ss.stock_num) FROM systemcenter.sc_store_skunum ss WHERE ss.sku_code in( " + 
				"		SELECT s.sku_code FROM productcenter.pc_skuinfo s WHERE s.product_code = ap.product_code " + 
				"	)) num " + 
				" FROM productcenter.pc_applet_pay_success ap " + 
				" LEFT JOIN productcenter.pc_productinfo p ON ap.product_code = p.product_code " + 
				" WHERE p.product_status = '4497153900060002' " + 
				" HAVING num > 0 " + 
				" ORDER BY ap.position ASC, ap.create_time DESC " + 
				" LIMIT "+iStart+","+pageSize;
		List<Map<String, Object>> list = DbUp.upTable("pc_applet_pay_success").dataSqlList(sql, new MDataMap());
		if(list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				ProductMaybeLove productMaybeLove = new ProductMaybeLove();
				
				String productCode = (String) map.get("product_code");
				// 如果是调编的商品则用新的商品编号替换
				String productCodeNew = ProductCodeCopyLoader.queryCode(productCode);
				ProductService pService = new ProductService();
				if(StringUtils.isNotBlank(productCodeNew)) {
					productCode = productCodeNew;
				}
				PlusModelProductInfo plusModelProductInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
				/*// 忽略下架商品
				if(plusModelProductInfo == null || !"4497153900060002".equals(plusModelProductInfo.getProductStatus())){
					continue;
				}
				//忽略库存数小于0的商品
				long allStock = new PlusSupportStock().upAllStockForProduct(productCode);
				if(allStock <= 0) {
					continue;
				}*/
				
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				skuQuery.setCode(productCode);
				skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
				skuQuery.setIsPurchase(1);
				skuQuery.setChannelId("449747430023");
				PlusModelSkuInfo plusModelSkuInfo = new ProductPriceService().getProductMinPriceSkuInfo(skuQuery).get(productCode);
				//如果商品取不到价格则返回null
				if(plusModelSkuInfo == null) {
					PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(productCode));
					// 销售价
					productMaybeLove.setProductPrice(MoneyHelper.format(productInfo.getMinSellPrice()));
				}else {
					productMaybeLove.setProductPrice(MoneyHelper.format(plusModelSkuInfo.getSellPrice()));  
					if(plusModelSkuInfo.getSellPrice().compareTo(plusModelSkuInfo.getSkuPrice()) < 0) {
						productMaybeLove.setMarket_price(MoneyHelper.format(plusModelSkuInfo.getSkuPrice()));
						productMaybeLove.setSkuPrice(plusModelSkuInfo.getSkuPrice());
					}
				}
				
				//获取所属商品字段值：map中存放的为商品分类的列表标签，和详情标签
				String ssc =plusModelProductInfo.getSmallSellerCode();
				String st="";
				if("SI2003".equals(ssc)) {
					st="4497478100050000";
				}else {
					st = WebHelper.getSellerType(ssc);
				}
				Map<String, String> productTypeMap = WebHelper.getAttributeProductType(st);
				
				productMaybeLove.setLabelsList(plusModelProductInfo.getLabelsList());
				productMaybeLove.setLabelsPic(new ProductLabelService().getLabelInfo(plusModelProductInfo.getProductCode()).getListPic());
				productMaybeLove.setFlagTheSea(plusModelProductInfo.getFlagTheSea());		
				productMaybeLove.setMainpic_url(pService.getPicInfoOprBig(Constants.IMG_WIDTH_SP02, plusModelProductInfo.getMainpicUrl()).getPicNewUrl());
				productMaybeLove.setProcuctCode(plusModelProductInfo.getProductCode());
				productMaybeLove.setProductNameString(plusModelProductInfo.getProductName());
				productMaybeLove.setSmallSellerCode(plusModelProductInfo.getSmallSellerCode());
				productMaybeLove.setRecommendId("");
				productMaybeLove.setProClassifyTag(productTypeMap.get("proTypeListPic").toString());
				productMaybeLove.setLabelsInfo(new ProductLabelService().getLabelInfoList(productCode));
				
				loveList.add(productMaybeLove);
			}
		}
		apiResult.setProductMaybeLove(loveList);
		//只有5.4.0之后版本走此逻辑。
		String appVersion = StringUtils.trimToEmpty(getApiClient().get("app_vision"));
		if(appVersion.compareTo("5.4.0")>=0){//当版本号高于或等于5.4.0的时候才会执行以下代码，添加拼团标识
			List<ProductMaybeLove> items = apiResult.getProductMaybeLove();
			for(ProductMaybeLove itemEntity : items){
				String productCode = itemEntity.getProcuctCode();
				//根据商品编号查询商品所参与的活动
				PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
				skuQuery.setCode(productCode);
				skuQuery.setMemberCode(getFlagLogin() ? getOauthInfo().getUserCode() : "");
				skuQuery.setChannelId("449747430023");
				Map<String,PlusModelSkuInfo> map = new ProductPriceService().getProductMinPriceIncloudGroupPrice(skuQuery);
				PlusModelSkuInfo skuInfo = map.get(productCode);
				if("4497472600010024".equals(skuInfo.getEventType())){//拼团单
					itemEntity.setProductType("4497472000050001");//是拼团单
					itemEntity.setGroupBuying("4497472600010024");
					itemEntity.setGroupBuyingPrice(skuInfo.getGroupBuyingPrice());//设置拼团价
					itemEntity.setSkuPrice(skuInfo.getSkuPrice());//拼团前的价格
					String eventCode = skuInfo.getEventCode();
					PlusModelEventInfo eventInfo = new PlusSupportEvent().upEventInfoByCode(eventCode);
					String collagePersonCount = eventInfo.getCollagePersonCount();//拼团人数
					itemEntity.setCollagePersonCount(collagePersonCount);
				}else{
					itemEntity.setProductType("4497472000050002");//不是拼团单
				}
			}
		}
		//562版本对于商品列表标签做版本兼容处理
		if(appVersion.compareTo("5.6.2")<0){
			for(ProductMaybeLove info:apiResult.getProductMaybeLove()){
				Iterator<PlusModelProductLabel> iter = info.getLabelsInfo().iterator();
				while (iter.hasNext()) {
					PlusModelProductLabel plusModelProductLabel = (PlusModelProductLabel) iter.next();
					if(plusModelProductLabel.getLabelPosition().equals("449748430005")){
						iter.remove();
					}
				}
			}
		}
		
		String countSql = "SELECT count(1) total FROM ( " + 
				"	SELECT ap.*, " + 
				"		(SELECT sum(ss.stock_num) FROM systemcenter.sc_store_skunum ss WHERE ss.sku_code in( " + 
				"			SELECT s.sku_code FROM productcenter.pc_skuinfo s WHERE s.product_code = ap.product_code " + 
				"		)) num " + 
				"	FROM productcenter.pc_applet_pay_success ap  " + 
				"	LEFT JOIN productcenter.pc_productinfo p ON ap.product_code = p.product_code " + 
				"	WHERE p.product_status = '4497153900060002' " + 
				"	HAVING num > 0 " + 
				") c";
		Map<String, Object> countMap = DbUp.upTable("pc_applet_pay_success").dataSqlOne(countSql, new MDataMap());
		if(countMap != null) {
			int intValue = MapUtils.getIntValue(countMap, "total");
			pagination = totalPage(intValue, pageSize);
		}
		apiResult.setPagination(pagination);
		
		apiResult.setTitle(title);
		
		return apiResult;
	}
	
	public int totalPage(int totalSize,int pageSize){
		int pageCount = totalSize / pageSize;
		return (totalSize % pageSize) > 0 ? (pageCount + 1) : pageCount;
	}
	
}
