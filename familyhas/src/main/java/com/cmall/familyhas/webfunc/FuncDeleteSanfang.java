package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 忽略三方异常订单数据
 */
public class FuncDeleteSanfang extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String userCode = UserFactory.INSTANCE.create().getUserCode();
		
		mDataMap.put("uid", mDataMap.get("zw_f_uid"));
		mDataMap.put("delete_flag", "1");
		mDataMap.put("update_time", FormatHelper.upDateTime());
		mDataMap.put("updator", userCode);
		DbUp.upTable("oc_order_sanfang_exception").dataUpdate(mDataMap, "delete_flag,update_time,updator", "uid");
		
		return mResult;
	}
	
}
