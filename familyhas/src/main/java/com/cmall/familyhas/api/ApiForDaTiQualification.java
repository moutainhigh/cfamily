package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.DaTiQualificationInput;
import com.cmall.familyhas.api.result.DaTiQualificationResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiForDaTiQualification extends RootApiForToken<DaTiQualificationResult, DaTiQualificationInput> {

	@Override
	public DaTiQualificationResult Process(DaTiQualificationInput inputParam, MDataMap mRequestMap) {
		DaTiQualificationResult result = new DaTiQualificationResult();
		
		// 获取用户编号
		String memberCode = getUserCode();
		//获取当前活动
		String eventCode = inputParam.getEventCode();
		
		String sql2 = "select * from sc_hudong_event_dati_log where member_code ='"+memberCode+"' and event_code = '"+eventCode+"'";
		List<Map<String, Object>> dataSqlList2 = DbUp.upTable("sc_hudong_event_dati_log").dataSqlList(sql2, null);
		if (null != dataSqlList2 && dataSqlList2.size() > 0) {//用户已答过此题
			result.setResultCode(0);
		}else {
			result.setHasQualification("1");
			//插入用户题目日志表
			MDataMap log =new MDataMap();
			log.put("event_code", eventCode);
			log.put("member_code", memberCode);
			DbUp.upTable("sc_hudong_event_dati_log").dataInsert(log);
		}
		return result;
	}


}
