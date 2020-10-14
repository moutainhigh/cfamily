package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/** 
* @ClassName: CouponApproveFuncEdit 
* @Description: 优惠券系统发放审批
* @author 张海生
* @date 2016-1-21 下午2:57:35 
*  
*/
public class CouponApproveFuncEdit extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		String toStatus = mDataMap.get("zw_f_to_status");
		String taskCode = mDataMap.get("zw_f_taskCode");
		MDataMap upMap = new MDataMap();
		upMap.put("distribute_status", toStatus);
		upMap.put("task_code", taskCode);
		try {
			DbUp.upTable("oc_coupon_check").dataUpdate(upMap, "distribute_status", "task_code");//更新生成优惠券任务状态
			DbUp.upTable("oc_coupon_task").dataUpdate(upMap, "distribute_status", "task_code");//更新总任务状态
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
