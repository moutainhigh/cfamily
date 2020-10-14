package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

public class CdkeyFuncAdd extends FuncAdd {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		/* 系统当前时间 */
		String create_time = com.cmall.familyhas.util.DateUtil.getNowTime();
		/* 获取当前登录人 */
		String create_user = UserFactory.INSTANCE.create().getLoginName();

		mAddMaps.put("create_time", create_time);
		mAddMaps.put("create_user", create_user);
		String manageCode = UserFactory.INSTANCE.create().getManageCode();
		mAddMaps.put("manage_code", manageCode);
		mAddMaps.put("task_status", "3");//待审批
		try{
			String multiAccount = mAddMaps.get("multi_account");
			if("449746250001".equals(multiAccount)){//是多账户使用
				MDataMap mdCount = DbUp
						.upTable("oc_cdkey_provide")
						.oneWhere(
								"cdkey",
								"",
								"cdkey=:cdkey and manage_code=:manageCode and (task_status<>:task_status)",
								"cdkey", mAddMaps.get("cdkey"), "manageCode",
								manageCode, "task_status", "4");//优惠码不能重复,驳回的不计入在内
				if (mdCount != null) {
					mResult.inErrorMessage(916401236);// 优惠码不能重复
					return mResult;
				}
			}
			String uid = DbUp.upTable("oc_cdkey_provide").dataInsert(mAddMaps);//插入到定时任务待扫描表里
			if(StringUtils.isEmpty(uid)){
				mResult.inErrorMessage(959701033);
				return mResult;
			}
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}

}
