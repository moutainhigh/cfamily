package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;


/** 
* @ClassName: CdkeyexaminationFuncEdit 
* @Description: 审核待生成优惠码
* @author 张海生
* @date 2015-11-5 上午11:06:27 
*  
*/
public class CdkeyexaminationFuncEdit extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		/*系统当前时间*/
		String toStatus = mDataMap.get("zw_f_to_status");
		String activityCode = mDataMap.get("zw_f_activityCode");
		String creteTime = mDataMap.get("zw_f_createTime");
		MDataMap upMap = new MDataMap();
		upMap.put("task_status", toStatus);
		upMap.put("activity_code", activityCode);
		upMap.put("create_time", creteTime);
		try {
			DbUp.upTable("oc_cdkey_provide").dataUpdate(upMap, "task_status", "activity_code,create_time");//更新生成优惠码任务状态
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
