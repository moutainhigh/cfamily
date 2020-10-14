package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 栏目内容
 * @author ligj
 *
 */
public class FuncDelColumnContent extends FuncEdit{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		String nowTime = com.cmall.familyhas.util.DateUtil.getNowTime();
		if (mResult.upFlagTrue()) {
			/*获取当前登录人*/
			String delete_user = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_delete_time", nowTime);
			mDataMap.put("zw_f_delete_user", delete_user);
			mDataMap.put("zw_f_is_delete", "449746250001");
		}
		try{
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
