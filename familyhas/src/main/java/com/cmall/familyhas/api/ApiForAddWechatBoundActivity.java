package com.cmall.familyhas.api;


import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForAddWechatBoundActivityInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;


public class ApiForAddWechatBoundActivity extends RootApi<RootResult,  ApiForAddWechatBoundActivityInput>{


	@Override
	public RootResult Process(ApiForAddWechatBoundActivityInput inputParam, MDataMap mRequestMap) {
		RootResult rootResult = new RootResult();
		String activity_code = inputParam.getActivity_code();
		if(StringUtils.isBlank(activity_code)) {
			//为空，则删除绑定活动
			DbUp.upTable("oc_coupon_wechat_bound").dataExec("delete from oc_coupon_wechat_bound", null);
		}else {
			MDataMap one = DbUp.upTable("oc_activity").one("activity_code",activity_code);
			if(one!=null) {
				/*if(!"4497471600060002".equals(one.get("provide_type"))) {
					rootResult.setResultMessage("请配置系统发放类型的活动!");
					rootResult.setResultCode(0);
					return rootResult;
				}*/
				String activity_name = one.get("activity_name");
				String userCode = UserFactory.INSTANCE.create().getUserCode();
				DbUp.upTable("oc_coupon_wechat_bound").dataExec("delete from oc_coupon_wechat_bound", null);
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("uid", WebHelper.upUuid());
				mDataMap.put("activity_code", activity_code);
				mDataMap.put("activity_name",activity_name==null?"":activity_name );
				mDataMap.put("operator",userCode );
				DbUp.upTable("oc_coupon_wechat_bound").dataInsert(mDataMap);
				rootResult.setResultMessage("添加成功");
			}else {
				rootResult.setResultMessage("活动编号错误!");
				rootResult.setResultCode(0);
			}
		}
		
		
		return rootResult;
	}



}
