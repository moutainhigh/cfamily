package com.cmall.familyhas.api;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForFillShipmentsTipsInput;
import com.cmall.familyhas.api.result.ApiForFillShipmentsTipsResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 售后物流信息提示
 * 
 * 
 * @author jlin
 *
 */
public class ApiForFillShipmentsTips extends RootApiForToken<ApiForFillShipmentsTipsResult, ApiForFillShipmentsTipsInput> {
	
	@Override
	public ApiForFillShipmentsTipsResult Process(ApiForFillShipmentsTipsInput inputParam, MDataMap mRequestMap) {

		ApiForFillShipmentsTipsResult result = new ApiForFillShipmentsTipsResult();

		String afterCode = inputParam.getAfterCode();
		String buyer_code = getUserCode();
		
		MDataMap asMap = DbUp.upTable("oc_order_after_sale").one("asale_code", afterCode, "buyer_code", buyer_code);
		if (asMap == null || asMap.isEmpty()) {
			result.setResultCode(916422153);
			result.setResultMessage(bInfo(916422153));
			return result;
		}
		MDataMap returnMap = DbUp.upTable("oc_order_after_sale_dtail").one("asale_code",afterCode);
		if(returnMap == null || returnMap.isEmpty()) {
			result.setResultCode(-1);
			result.setResultMessage("售后单不存在，请联系客服");
			return result;
		}
		String smallSellerCode = asMap.get("small_seller_code");
		String productCode = returnMap.get("product_code");
		MDataMap productInfo = DbUp.upTable("pc_productinfo").one("product_code",productCode,"small_seller_code",smallSellerCode);
		if(productInfo == null || productInfo.isEmpty()) {
			result.setResultCode(-1);
			result.setResultMessage("商品不存在，请联系客服");
			return result;
		}
		boolean flag = true;
		if(StringUtils.isEmpty(productInfo.get("after_sale_address_uid"))) {
			flag = false;
		}
		if(flag) {
			MDataMap addressInfo = DbUp.upTable("oc_address_info").one("uid",productInfo.get("after_sale_address_uid"));
			if(addressInfo != null && !addressInfo.isEmpty()) {
				result.setTips(FormatHelper.formatString(bConfig("familyhas.fill_shipments_tips"), addressInfo.get("after_sale_address"),addressInfo.get("after_sale_person"),addressInfo.get("after_sale_mobile"),addressInfo.get("after_sale_postcode")));
			}else {
				flag = false;
			}
		}
		if(!flag) {
			if("4497477800030001".equals(asMap.get("asale_type"))){
				MDataMap orgMap=DbUp.upTable("oc_return_goods").one("return_code",afterCode);
				result.setTips(FormatHelper.formatString(bConfig("familyhas.fill_shipments_tips"), orgMap.get("address"),orgMap.get("contacts"),orgMap.get("mobile"),orgMap.get("receiver_area_code")));
				
			}else{
				
				MDataMap orgMap=DbUp.upTable("oc_exchange_goods").one("exchange_no",afterCode);
				result.setTips(FormatHelper.formatString(bConfig("familyhas.fill_shipments_tips"), orgMap.get("after_sale_address"),orgMap.get("after_sale_person"),orgMap.get("after_sale_mobile"),orgMap.get("after_sale_postcode")));
				
			}
		}
		
//		result.setTips("请以客服提供的回寄地址邮寄!");
		
		return result;
	}

}
