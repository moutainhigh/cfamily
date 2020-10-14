package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.result.ApiForRegularToNewProductsResult;
import com.cmall.familyhas.api.result.RegularToNewProduct;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.support.PlusSupportProduct;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webmodel.MOauthInfo;

public class ApiForRegularToNewProducts extends RootApiForVersion<ApiForRegularToNewProductsResult, RootInput> {

	@Override
	public ApiForRegularToNewProductsResult Process(RootInput inputParam, MDataMap mRequestMap) {
		ApiForRegularToNewProductsResult result = new ApiForRegularToNewProductsResult();
		String sql = "SELECT * FROM systemcenter.sc_event_info WHERE event_type_code = '4497472600010028' AND event_status = '4497472700020002' AND begin_time <= sysdate() AND end_time > sysdate() limit 1";
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
		MDataMap regularToNewInfo = DbUp.upTable("sc_event_regular_to_new").one("event_code",event_code);
		if(regularToNewInfo == null || regularToNewInfo.isEmpty()) {
			result.setResultCode(2);
			result.setResultMessage("暂无活动商品。");
			return result;
		}
		String product_limit_type = regularToNewInfo.get("product_limit_type");
		String productCodes = regularToNewInfo.get("product_codes");
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
		MDataMap apiClient = getApiClient();
		String channelId = apiClient.get("channelId");
		if(StringUtils.isEmpty(channelId)) {
			channelId = "449747430001";//惠家有APP
		}
		if("4497476400020002".equals(product_limit_type)) {//仅包含
			for(int i = 0;i<productCodeArr.length;i++) {
				String productCode = productCodeArr[i];
				if(StringUtils.isEmpty(productCode)) {
					continue;
				}
				RegularToNewProduct product = this.getProduct(productCode, memberCode,channelId);
				if(product == null) {
					continue;
				}
				result.getItems().add(product);
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
			if(inWhere.length()>1) {
				String where = inWhere.subSequence(0, inWhere.length()-1).toString();
				sql2 = "SELECT product_code FROM productcenter.pc_productinfo WHERE small_seller_code = 'SI2003' AND product_status = '4497153900060002' AND product_code not in ("+where+") order by create_time DESC limit 100";//取前100条数据
			}else {
				sql2  = "SELECT product_code FROM productcenter.pc_productinfo WHERE  small_seller_code = 'SI2003' AND product_status = '4497153900060002' order by create_time DESC limit 100";//取前100条数据
			}
			List<Map<String,Object>> productMaps = DbUp.upTable("pc_productinfo").dataSqlList(sql2, null);
			for(Map<String,Object> pro : productMaps) {
				if(pro == null) {
					continue;
				}
				RegularToNewProduct product = this.getProduct(pro.get("product_code").toString(), memberCode,channelId);
				if(product == null) {
					continue;
				}
				result.getItems().add(product);
			}
		}else {
			String sql2  = "SELECT product_code FROM productcenter.pc_productinfo WHERE  small_seller_code = 'SI2003' AND product_status = '4497153900060002' order by create_time DESC limit 100";//取前100条数据
			List<Map<String,Object>> productMaps = DbUp.upTable("pc_productinfo").dataSqlList(sql2, null);
			for(Map<String,Object> pro : productMaps) {
				if(pro == null) {
					continue;
				}
				RegularToNewProduct product = this.getProduct(pro.get("product_code").toString(), memberCode,channelId);
				if(product == null) {
					continue;
				}
				result.getItems().add(product);
			}
		}
		result.setEventCode(event_code);
		result.setEventDesc(regularToNewInfo.get("event_desc"));
		return result;
	}
	
	private RegularToNewProduct getProduct(String productCode,String memberCode,String channelId) {
		PlusSupportProduct plusSupportProduct = new PlusSupportProduct();
		RegularToNewProduct product = new RegularToNewProduct();
		product.setProductCode(productCode);
		PlusModelProductInfo productInfo = plusSupportProduct.upProductInfo(productCode);
		product.setSellPrice(plusSupportProduct.upPriceByProductCode(productCode, memberCode));
		product.setProductName(productInfo.getProductName());
		product.setProductUrl(productInfo.getMainpicUrl());
		Map<String,String> productTypeMap = WebHelper.getAttributeProductType("4497478100050000");
		if(productTypeMap != null && productTypeMap.get("proTypeInfoPic") != null) {
			product.setProClassifyTag(productTypeMap.get("proTypeInfoPic").toString());
		}
		ProductService ps = new ProductService();
		List<String> tagList = ps.getTagListByProductCode(productCode, memberCode, channelId);
		ps.addTagInfo(product.getTagInfoList(), TagInfo.Style.Normal, tagList.toArray(new String[0]));
		PlusSupportStock pss = new PlusSupportStock();
		if(pss.upAllStockForProduct(productCode) <= 0) {//库存为0
			return null;
		}
		if(!"4497153900060002".equals(productInfo.getProductStatus())) {
			return null;
		}
		//排除橙意卡商品。
		if(bConfig("familyhas.plus_product_code").equals(productCode)) {
			return null;
		}
		
		return product;
	}

}
