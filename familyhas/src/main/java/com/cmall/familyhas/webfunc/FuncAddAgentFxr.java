package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加优惠券活动
 * @author ligj
 *
 */
public class FuncAddAgentFxr extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		
		try{
			/*获取当前登录人*/
			String create_user = UserFactory.INSTANCE.create().getLoginName();
			/*系统当前时间*/
			String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
			mDataMap.put("zw_f_create_time", createTime);
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
