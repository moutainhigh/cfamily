package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForGetPayTypeInput;
import com.cmall.familyhas.api.result.ApiForGetPayTypeResult;
import com.cmall.systemcenter.service.StoreService;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;

/***
 * 根据区域查询支付方式
 * @author jlin
 *
 */
public class ApiForGetPayType extends RootApiForManage<ApiForGetPayTypeResult, ApiForGetPayTypeInput> {

	public ApiForGetPayTypeResult Process(ApiForGetPayTypeInput inputParam,MDataMap mRequestMap) {
		ApiForGetPayTypeResult result=new ApiForGetPayTypeResult();
		
		String district_code=inputParam.getDistrict_code();
		
		StoreService storeService=BeansHelper.upBean("bean_com_cmall_systemcenter_service_StoreService");
		String express=storeService.getExpress(district_code);
		
		String pay_type="";
		if("10".equals(express)){
			pay_type = "449716200002";//货到付款
		}else if("30".equals(express)){
			pay_type = "449716200001";//在线支付
		}else{
			result.setResultCode(916401135);
			result.setResultMessage(bInfo(916401135));
			return result;
		}
		
		result.setPay_type(pay_type);
		return result;
	}

}
