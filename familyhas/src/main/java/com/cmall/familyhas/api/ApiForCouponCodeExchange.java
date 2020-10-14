package com.cmall.familyhas.api;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForCouponCodeExchangeInput;
import com.cmall.ordercenter.service.CouponsService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 优惠码兑换
 * @author 张海生
 *
 */
public class ApiForCouponCodeExchange 
			extends RootApiForToken<RootResultWeb, ApiForCouponCodeExchangeInput>{
	public RootResultWeb Process(ApiForCouponCodeExchangeInput input,MDataMap mRequestMap){
		RootResultWeb result=new RootResultWeb();
		
		String key = "ApiForCouponCodeExchange-"+getUserCode();
		String code = KvHelper.lockCodes(60,key);
		if(StringUtils.isBlank(code)) {
			result.setResultCode(0);
			result.setResultMessage("正在领取中，请勿重复点击");
			return result;
		}
		
		try {
			CouponsService couponsService = new CouponsService();
			result = couponsService.couponCodeExchange(input.getCouponCode(), getUserCode(), getManageCode());//兑换优惠券
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			KvHelper.unLockCodes(code, key);
		}
		return result;
	}
	
}
