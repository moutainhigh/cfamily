package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ContentColumnFuncDelete extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		/* 系统当前时间 */
		String delete_time = DateUtil.getNowTime();
		/* 获取当前登录人 */
		String delete_user = UserFactory.INSTANCE.create().getLoginName();
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		String table = mPage.getPageTable();
		MDataMap mdata = DbUp.upTable(table).oneWhere(
				"release_flag", null, null, "uid", mDataMap.get("zw_f_uid"));
		
		if (mdata != null && "449746250001".equals(mdata.get("release_flag"))) {// 已发布
			mResult.inErrorMessage(916401218);// 已经发布的栏目不能删除
			return mResult;
		}
		mDataMap.put("zw_f_delete_time", delete_time);
		mDataMap.put("zw_f_delete_user", delete_user);
		mDataMap.put("zw_f_is_delete", "449746250001");// 标记为删除
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}

}
