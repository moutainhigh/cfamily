package com.cmall.familyhas.webfunc;

import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 发布消息
 * @author lee
 *
 */
public class FuncIssueForMessage extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		// 定义插入数据库
		MDataMap mInsertMap = new MDataMap();

		recheckMapField(mResult, mPage, mAddMaps);

		if (mResult.upFlagTrue()) {

			//判断登陆用户是否为空
			String loginname=UserFactory.INSTANCE.create().getLoginName();
			if(loginname==null||"".equals(loginname)){
				mResult.inErrorMessage(941901073);
				return mResult;
			}
			
			mInsertMap.put("uid", mAddMaps.get("uid"));
			String now=DateUtil.getSysDateTimeString();
			mInsertMap.put("update_user", loginname);   // 更新的用户
			mInsertMap.put("update_time", now);   // 更新时间
			mInsertMap.put("status", "449746250001");   // 更新时间
		}
		
		
		if (mResult.upFlagTrue()) {
			
			DbUp.upTable(mPage.getPageTable()).dataUpdate(mInsertMap, "", "uid");
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;

	}
	
}
