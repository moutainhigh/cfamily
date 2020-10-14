package com.cmall.familyhas.webfunc;
import org.apache.commons.collections.MapUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;


public class FuncDeleteMemberAddress extends RootFunc{

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		String uid = MapUtils.getString(mDataMap, "zw_f_uid","");
		DbUp.upTable("nc_address").delete("uid",uid);
		return new MWebResult();
	}
}
