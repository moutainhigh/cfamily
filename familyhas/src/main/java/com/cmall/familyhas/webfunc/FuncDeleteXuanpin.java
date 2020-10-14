package com.cmall.familyhas.webfunc;


import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 删除自动选品规则
 */
public class FuncDeleteXuanpin extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MWebResult result = new MWebResult();
		
		MDataMap insertMap = new MDataMap();
		insertMap.put("uid", mAddMaps.get("uid"));
		insertMap.put("delete_flag", "1");
		insertMap.put("update_time", FormatHelper.upDateTime());
		
		DbUp.upTable("pc_product_xuanpin").dataUpdate(insertMap, "", "uid");
		
		return result;
	}

}
