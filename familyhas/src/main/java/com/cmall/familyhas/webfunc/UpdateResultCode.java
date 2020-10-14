package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 客服服务已处理功能
 * @author zhouguohui
 *
 */
public class UpdateResultCode extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		if(mResult.upFlagTrue()){
			if(mAddMaps.get("record_code") == null || mAddMaps.get("record_code").equals("")){
				mResult.setResultMessage("处理失败");
			}else{
				mAddMaps.put("result_code", "449747410002");
				mAddMaps.put("father_code", mAddMaps.get("record_code"));
				DbUp.upTable("mc_record_person").dataUpdate(mAddMaps, "result_code", "record_code");
				DbUp.upTable("mc_record_person").dataUpdate(mAddMaps, "result_code", "father_code");
			}
			
		}
		return mResult;
	}

}
