package com.cmall.familyhas.webfunc;


import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 删除直播间
 */
public class FuncDeleteLiveRoom extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MWebResult result = new MWebResult();
		
		MDataMap insertMap = new MDataMap();
		insertMap.put("uid", mAddMaps.get("uid"));
		insertMap.put("is_delete", "1");
		insertMap.put("update_time", FormatHelper.upDateTime());
		
		DbUp.upTable("lv_live_room").dataUpdate(insertMap, "", "uid");
		
		return result;
	}

}
