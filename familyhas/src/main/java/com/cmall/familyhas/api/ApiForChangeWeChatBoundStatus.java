package com.cmall.familyhas.api;


import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForChangeWeChatBoundStatusInput;
import com.cmall.groupcenter.homehas.HomehasSupport;
import com.cmall.membercenter.helper.VerifyCodeUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webmodel.MWebResult;


public class ApiForChangeWeChatBoundStatus extends RootApi<RootResult,  ApiForChangeWeChatBoundStatusInput>{


	@Override
	public RootResult Process(ApiForChangeWeChatBoundStatusInput inputParam, MDataMap mRequestMap) {
		RootResult rootResult = new RootResult();
		String custId = inputParam.getCustId();
		String toStatus = inputParam.getToStatus();
		if("0".equals(toStatus)) {
			//解绑
			MDataMap one = DbUp.upTable("mc_member_wechat_bound").one("cust_id",custId);
			if(StringUtils.isNotBlank(one.get("phone_num"))) {
				//调用解绑接口
				HomehasSupport homehasSupport = new HomehasSupport();
				MWebResult result = homehasSupport.unBindWX(one.get("phone_num"));
				if(result.getResultCode()==1) {
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("is_bound", toStatus);
					mDataMap.put("cust_id", custId);
					mDataMap.put("nick_name_wechat", "");
					mDataMap.put("registe_time", "");
					DbUp.upTable("mc_member_wechat_bound").dataUpdate(mDataMap, "is_bound,nick_name_wechat,registe_time", "cust_id");
				}else {
					rootResult.setResultCode(0);
					rootResult.setResultMessage("解绑失败！");
				}
			}else {
				rootResult.setResultCode(0);
				rootResult.setResultMessage("无绑定手机号,解绑失败！");
			}
			
		}else {
			//绑定链接返回  oc_coupon_wechat_validate
			MDataMap one = DbUp.upTable("mc_member_wechat_bound").one("cust_id",custId);
			MDataMap mDataMap = new MDataMap();
			String upUuid = WebHelper.upUuid();
			mDataMap.put("uid", upUuid);
			mDataMap.put("member_code",one.get("member_code"));
			String validate_code = VerifyCodeUtils.generateVerifyCode(6, "0123456789");
			mDataMap.put("validate_code",validate_code);
			DbUp.upTable("oc_coupon_wechat_validate").dataExec("delete from oc_coupon_wechat_validate where member_code=:member_code", mDataMap);
			DbUp.upTable("oc_coupon_wechat_validate").dataInsert(mDataMap);
			
			String  shareUrl = bConfig("cfamily.hjyWeiXinHttp")+"bindphone/?id="+upUuid;
			rootResult.setResultMessage("优惠券领取地址: "+shareUrl);

		}
		
		return rootResult;
	}



}
