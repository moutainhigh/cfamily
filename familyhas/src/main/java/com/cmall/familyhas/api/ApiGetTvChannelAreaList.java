package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.result.ApiGetTvChannelAreaListResult;
import com.cmall.familyhas.api.result.ApiGetTvChannelAreaListResult.AreaChannelInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiGetTvChannelAreaList extends RootApiForManage<ApiGetTvChannelAreaListResult ,RootInput>{

	public ApiGetTvChannelAreaListResult Process(RootInput inputParam,
			MDataMap mRequestMap) {
		ApiGetTvChannelAreaListResult result = new ApiGetTvChannelAreaListResult();
		MDataMap mp = new MDataMap();
		List<Map<String, Object>> list = DbUp.upTable("sc_tv_area").dataSqlList("SELECT t.name,d.channel_name from sc_tmp t inner JOIN (select DISTINCT CONCAT(left(a.code,2),'0000') area_code from sc_tv_area a) a on t.code = a.area_code LEFT JOIN sc_tv_area d ON t.code = d.code", mp);
		if(list.size()>0 ) {
			for(Map<String, Object> map:list){
				AreaChannelInfo info = new AreaChannelInfo();
				info.setAreaName(map.get("name").toString());
				info.setChannelName(map.get("channel_name")==null?"":map.get("channel_name").toString());
				result.getArea_channel().add(info);
			}
		}else{
			result.setResultCode(-1);
			result.setResultMessage("没有相关数据");
		}
		return result;
	}

}
