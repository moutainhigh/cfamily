package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncWHAdvert extends RootFunc{

	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
	
		MWebResult mResult = new MWebResult();
		MDataMap upSubMap = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String uid = upSubMap.get("uid");
		if(StringUtils.isBlank(uid)) {//add
            MDataMap paramMap = new MDataMap(); 
			uid = WebHelper.upUuid();
			paramMap.put("uid", uid);
			paramMap.put("advertise_code", WebHelper.upCode("ADVE"));
			paramMap.put("adver_entry_type", upSubMap.get("adver_entry_type").toString());
			paramMap.put("programa_num", upSubMap.get("programa_num").toString());
			paramMap.put("start_time", upSubMap.get("start_time").toString());
			paramMap.put("end_time", upSubMap.get("end_time").toString());
			paramMap.put("channel_id", upSubMap.get("channel_id").toString());
			DbUp.upTable("fh_advert").dataInsert(paramMap);
			
		}else {//edit
			MDataMap one = DbUp.upTable("fh_advert").one("uid",uid);
			String advertise_code = one.get("advertise_code").toString();
			String programa_num = upSubMap.get("programa_num").toString();
			int count = DbUp.upTable("fh_advert_column").count("advertise_code",advertise_code);
			if(Integer.parseInt(programa_num) < count) {
				mResult.setResultCode(0);
				mResult.setResultMessage("设置的广告栏数超过了已存广告数量,不允许修改!");
			}
			MDataMap paramMap = new MDataMap(); 
			paramMap.put("uid", uid);
			paramMap.put("adver_entry_type", upSubMap.get("adver_entry_type").toString());
			paramMap.put("programa_num", programa_num);
			paramMap.put("start_time", upSubMap.get("start_time").toString());
			paramMap.put("end_time", upSubMap.get("end_time").toString());
			paramMap.put("channel_id", upSubMap.get("channel_id").toString());
			DbUp.upTable("fh_advert").dataUpdate(paramMap, "adver_entry_type,programa_num,"
					+ "start_time,end_time,channel_id", "uid");
		}
		
		return mResult;
	}

}
