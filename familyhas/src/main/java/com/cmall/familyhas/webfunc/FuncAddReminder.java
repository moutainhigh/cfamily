package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加提示语
 * @author ligj
 *
 */
public class FuncAddReminder extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		String createTime = DateUtil.getNowTime();
		mDataMap.put("zw_f_status", "4497469400030001");	//未发布
		mDataMap.put("zw_f_flag_del", "1");					//未被删除
		mDataMap.put("zw_f_updater", create_user);
		mDataMap.put("zw_f_update_time", createTime);
		//不是指定商户类型时把指定商户字段设置为空值
		if (!"4497471600260004".equals(mDataMap.get("zw_f_seller_type"))) {
			mDataMap.put("zw_f_seller_codes", "");
		}
		mResult = super.funcDo(sOperateUid, mDataMap);
		return mResult;
	}
}
