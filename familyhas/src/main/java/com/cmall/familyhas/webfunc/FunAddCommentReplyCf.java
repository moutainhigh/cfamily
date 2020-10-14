package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FunAddCommentReplyCf extends  FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		if (mResult.upFlagTrue()) {
			/*获取当前登录人*/
			MUserInfo create_user = UserFactory.INSTANCE.create();
			mDataMap.put("zw_f_manage_code", create_user.getManageCode());
			try{
				mResult = super.funcDo(sOperateUid, mDataMap);
		   }catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		   }
		}
		return mResult;
	}

}
