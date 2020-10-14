package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncAddGarderobeProgram extends FuncAdd {
	
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult result = new MWebResult();
		/* 获取当前登录人 */
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		String onTime = mDataMap.get("zw_f_on_time");
		String zw_f_on_time = onTime;
		if(onTime.length() > 0) {
			if(onTime.split(":")[0].equals("00")) {
				zw_f_on_time = onTime.split(":")[1] +":"+ onTime.split(":")[2];
			}
		}
		DbUp.upTable("fh_program").insert("program_code",WebHelper.upCode("PR"),
				"title",mDataMap.get("zw_f_title"),
				"vedio_img",mDataMap.get("zw_f_vedio_img"),
				"play_time",mDataMap.get("zw_f_play_time"),
				"status","449746740001",
				"count",mDataMap.get("zw_f_count"),
				"on_time",zw_f_on_time,
				"link",mDataMap.get("zw_f_link"),
				"create_user",create_user,
				"create_time",DateUtil.getSysDateTimeString());
		return result;
	}
}
