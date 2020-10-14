package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 *  对于试用商品申请试用状态进行修改
 * @author houwen
 *
 */
public class TryOutApplyUnStatus extends RootFunc {

	
	private static String TABLE_TPL="nc_freetryout_apply"; //免费试用商品申请表
	
	
	/**
	 * 
	 *  (non-Javadoc)
	 * @see com.srnpr.zapweb.webface.IWebFunc#funcDo(java.lang.String, com.srnpr.zapcom.basemodel.MDataMap)
	 */
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		mResult.setResultCode(1);
		String tplUid = mDataMap.get("zw_f_uid");
		MDataMap dataMap = new MDataMap();
		MDataMap mDataMap2 = new MDataMap();
		dataMap.put("uid", tplUid);
		mDataMap2.put("uid", tplUid);
		//申请状态 ：未申请：449746890001；已申请：449746890002；申请通过：449746890003；449746890004：已结束
		String isDisable = mDataMap.get("zw_f_isDisable");
		if("449746890002".equals(isDisable)){  //如果状态为已申请，状态改为已结束
			
			dataMap.put("status", "449746890004");
			DbUp.upTable(TABLE_TPL).dataUpdate(dataMap, "status", "uid");
		
	   }else{
			mResult.setResultCode(934205104);
			mResult.setResultMessage("数据已审核过，不可再修改！");
		}
		
		return mResult;
	}
	
}
