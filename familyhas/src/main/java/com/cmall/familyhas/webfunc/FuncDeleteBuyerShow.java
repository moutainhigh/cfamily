package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 删除买家秀
 * @author lgx
 *
 */
public class FuncDeleteBuyerShow extends FuncDelete {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MDataMap buyer_show = DbUp.upTable("nc_buyer_show_info").one("uid",mMaps.get("uid"));
		if(buyer_show==null) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("查不到该条数据,删除失败");
			return mResult;
		}
		
		MDataMap delDataMap = new MDataMap();
		delDataMap.put("uid", mMaps.get("uid"));
		delDataMap.put("is_delete", "1");
		DbUp.upTable("nc_buyer_show_info").dataUpdate(delDataMap, "is_delete", "uid");
		
		return mResult;
	}

}
