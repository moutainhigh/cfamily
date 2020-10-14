package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改"我的"图片
 * 
 * @author 李国杰
 * 
 */
public class FuncEditMyPic extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		String createTime = DateUtil.getNowTime();
		String user = UserFactory.INSTANCE.create().getLoginName();
		mDataMap.put("zw_f_update_time", createTime);
		mDataMap.put("zw_f_update_user", user);
		mResult = super.funcDo(sOperateUid, mDataMap);
		return mResult;
	}
}
