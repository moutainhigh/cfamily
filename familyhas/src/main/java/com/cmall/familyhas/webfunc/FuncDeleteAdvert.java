package com.cmall.familyhas.webfunc;



import org.apache.commons.collections.MapUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncDeleteAdvert extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MDataMap mDelMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String uid = MapUtils.getString(mDelMaps, "uid");
		MDataMap one = DbUp.upTable("fh_advert").one("uid",uid);
		if(one != null) {
			String advertise_code = MapUtils.getString(one, "advertise_code");
			DbUp.upTable("fh_advert").delete("uid",uid);
			DbUp.upTable("fh_advert_column").delete("advertise_code",advertise_code);
		}
		
		return new MWebResult();
	}
	
	
}
