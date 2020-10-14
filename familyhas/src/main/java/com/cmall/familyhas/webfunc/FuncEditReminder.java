package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改提示语
 * 
 * @author ligj
 * 
 */
public class FuncEditReminder extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		String createTime = DateUtil.getNowTime();
		String user = UserFactory.INSTANCE.create().getLoginName();
		mDataMap.put("zw_f_update_time", createTime);
		mDataMap.put("zw_f_updater", user);
		//不是指定商户类型时把指定商户字段设置为空值
		if (!"4497471600260004".equals(mDataMap.get("zw_f_seller_type"))) {
			mDataMap.put("zw_f_seller_codes", "");
		}
		mResult = super.funcDo(sOperateUid, mDataMap);
		return mResult;
	}
}
