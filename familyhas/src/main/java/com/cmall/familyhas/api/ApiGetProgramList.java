package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiGetProgramListInput;
import com.cmall.familyhas.api.model.Program;
import com.cmall.familyhas.api.result.ApiGetProgramListResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiGetProgramList extends RootApiForManage<ApiGetProgramListResult, ApiGetProgramListInput>{

	public ApiGetProgramListResult Process(ApiGetProgramListInput inputParam,
			MDataMap mRequestMap) {
		ApiGetProgramListResult result = new ApiGetProgramListResult();
		List<MDataMap> queryAll = DbUp.upTable("fh_program").queryAll("program_code,title,play_time,status,count,on_time,link,vedio_img", 
				"-play_time,-count", "status='449746740002'", null);
		for (int i = 0; i < queryAll.size(); i++) {
			MDataMap mDataMap = queryAll.get(i);
			Program pro = new Program();
//			mDataMap.get("status");
			pro.setCount("第"+mDataMap.get("count")+"集");
			pro.setLink(mDataMap.get("link"));
			pro.setOn_time(mDataMap.get("on_time"));
			String[] tm = mDataMap.get("play_time").split("-");
			pro.setPlay_time(tm[0]+tm[1]+tm[2]);
			pro.setTitle(mDataMap.get("title"));
			pro.setVedio_img(mDataMap.get("vedio_img"));
			result.getProList().add(pro);
			
		}
		return result;
	}

}
