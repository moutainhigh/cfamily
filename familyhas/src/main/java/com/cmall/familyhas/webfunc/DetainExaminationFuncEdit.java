package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;


/** 
* @ClassName: DetainExaminationFuncEdit 
* @Description: 审核退货挽留赠予积分
* @author sunyan
* @date 2019-08-22 
*  
*/
public class DetainExaminationFuncEdit extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		/*系统当前时间*/
		String toStatus = mDataMap.get("zw_f_to_status");
		String auditmind = mDataMap.get("zw_f_auditmind");
		String uid = mDataMap.get("zw_f_uid");
		MDataMap upMap = new MDataMap();
		upMap.put("give_status", toStatus);
		upMap.put("auditmind", auditmind);
		upMap.put("uid", uid);
		try {
			DbUp.upTable("uc_detain_integral").dataUpdate(upMap, "give_status,auditmind", "uid");
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
