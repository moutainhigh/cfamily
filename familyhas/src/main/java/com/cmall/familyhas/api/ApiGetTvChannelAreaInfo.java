package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetTvChannelAreaInfoInput;
import com.cmall.familyhas.api.result.ApiGetTvChannelAreaInfoResult;
import com.cmall.familyhas.api.result.ApiGetTvChannelAreaListResult.AreaChannelInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiGetTvChannelAreaInfo extends RootApiForManage<ApiGetTvChannelAreaInfoResult ,ApiGetTvChannelAreaInfoInput>{

	public ApiGetTvChannelAreaInfoResult Process(ApiGetTvChannelAreaInfoInput inputParam,
			MDataMap mRequestMap) {
		ApiGetTvChannelAreaInfoResult result = new ApiGetTvChannelAreaInfoResult();
		MDataMap mp = new MDataMap();
		String code = inputParam.getAreaCode();
		List<Map<String, Object>> list = DbUp.upTable("sc_tv_area").dataSqlList("SELECT a.name,a.channel_name,t.code_lvl from sc_tv_area a right JOIN sc_tmp t on a.code = t.`code` where t.code = "+code, null);
		if(list.size()>0 ) {
			if(StringUtils.isNotBlank(list.get(0).get("name")==null?"":list.get(0).get("name").toString())){
				result.setAreaName(list.get(0).get("name").toString());
				result.setChannelName(list.get(0).get("channel_name").toString());
			}else{
				int lvl = Integer.parseInt(list.get(0).get("code_lvl").toString());
				while (lvl>1) {
					List<Map<String, Object>> tempList = DbUp.upTable("sc_tv_area").dataSqlList("SELECT a.name,a.channel_name,t.code_lvl,t.`code` from sc_tv_area a right JOIN sc_tmp t on a.code = t.`code` where t.code = (SELECT tt.p_code from sc_tmp tt where tt.`code` = "+code+")", null);
					if(tempList.size()>0){
						if(StringUtils.isNotBlank(tempList.get(0).get("name")==null?"":tempList.get(0).get("name").toString())){
							result.setAreaName(tempList.get(0).get("name").toString());
							result.setChannelName(tempList.get(0).get("channel_name").toString());
							break;
						}else{
							code = tempList.get(0).get("code").toString();
							lvl = Integer.parseInt(tempList.get(0).get("code_lvl").toString());
							continue;
						}
					}else{
						result.setResultCode(-1);
						result.setResultMessage("没有相关数据");
						return result;
					}
				}
				if(StringUtils.isBlank(result.getAreaName())){
					List<Map<String, Object>> list1 = DbUp.upTable("sc_tv_area").dataSqlList("SELECT a.name,a.channel_name,t.code_lvl from sc_tv_area a right JOIN sc_tmp t on a.code = t.`code` where t.code = 520000", null);
					if(list1.size()>0&&StringUtils.isNotBlank(list1.get(0).get("name")==null?"":list1.get(0).get("name").toString())){
						result.setAreaName(list1.get(0).get("name").toString());
						result.setChannelName(list1.get(0).get("channel_name").toString());
					}
				}
			}
			
		}else{
			result.setResultCode(-1);
			result.setResultMessage("没有相关数据");
		}
		
		List<Map<String, Object>> list1 = DbUp.upTable("sc_tv_area").dataSqlList("SELECT t.name,d.channel_name from sc_tmp t inner JOIN (select DISTINCT CONCAT(left(a.code,2),'0000') area_code from sc_tv_area a) a on t.code = a.area_code LEFT JOIN sc_tv_area d ON t.code = d.code", mp);
		if(list.size()>0 ) {
			for(Map<String, Object> map:list1){
				AreaChannelInfo info = new AreaChannelInfo();
				info.setAreaName(map.get("name").toString());
				info.setChannelName(map.get("channel_name")==null?"":map.get("channel_name").toString());
				result.getArea_channel().add(info);
			}
		}
		
		return result;
	}

}
