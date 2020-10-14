package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @descriptions 3.9.6版本 App首页弹窗 - 广告弹窗 - 数据删除功能
 * 							  原有物理删除的方法不在此处使用。此处实际为更新。
 * 
 * @refactor 覆盖默认的删除方法并重写
 * @author Yangcl
 * @date 2016-5-4-下午5:44:52
 * @version 1.0.0
 */
public class DeleteFhAppHomeDialog extends FuncEdit {

	/**
	 * @descriptions 此处不进行物理删除，而是更新delete_flag字段为0，以此
	 * 							  进行标示此条记录被删除。
	 *  
	 * @param sOperateUid
	 * @param mDataMap
	 * @return mResult
	 * 
	 * @refactor 
	 * @author Yangcl
	 * @date 2016-5-4-下午4:47:11
	 * @version 1.0.0.1
	 */
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap){
		MWebResult mResult = new MWebResult();
		try {
			String update_time = com.cmall.familyhas.util.DateUtil.getNowTime();
			String update_user_code = UserFactory.INSTANCE.create().getUserCode();
			String update_user = UserFactory.INSTANCE.create().getLoginName();
			if (mResult.upFlagTrue()) {
				mDataMap.put("zw_f_delete_flag", "0");
				mDataMap.put("zw_f_update_user_code", update_user_code);
				mDataMap.put("zw_f_update_user", update_user);
				mDataMap.put("zw_f_update_time", update_time);
			}
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e){
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		
		
		return mResult;
	}

}
