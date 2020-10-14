package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.model.DkInfo;
import com.cmall.familyhas.api.result.ApiGetDkInfoResult;
import com.cmall.familyhas.service.PageActiveService;
import com.cmall.productcenter.common.DateUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;


/**
 * 获取打卡奖励信息
 * 
 * @author sunyan
 * 
 */
public class ApiGetDkInfo extends RootApiForToken<ApiGetDkInfoResult, RootInput> {

	public ApiGetDkInfoResult Process(RootInput inputParam, MDataMap mRequestMap) {
		ApiGetDkInfoResult result = new ApiGetDkInfoResult();
		String sql = "SELECT a.event_code from sc_hudong_event_info a where a.event_type_code = 449748210005 AND a.event_status = 4497472700020002 AND NOW() BETWEEN a.begin_time AND a.end_time";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("sc_hudong_event_info").dataSqlList(sql, null);
		if(dataSqlList.size()>0){
			String event_code = dataSqlList.get(0).get("event_code").toString();
			result.setEventCode(event_code);
			String sSql = "SELECT * from sc_huodong_event_dakajf_rule b where b.event_code = '"+event_code+"' ORDER BY b.jf_day ASC";
			List<Map<String, Object>> dataList = DbUp.upTable("sc_huodong_event_dakajf_rule").dataSqlList(sSql, null);
			if(dataList.size()>0){
				String tSql = "SELECT * from lc_huodong_event_dakajf_user WHERE jf_dk_time = (SELECT MAX(a.jf_dk_time) from lc_huodong_event_dakajf_user a where a.member_code = '"+getUserCode()+"' AND a.event_code = '"+event_code+"') AND member_code = '"+getUserCode()+"' AND event_code = '"+event_code+"'";
				List<Map<String, Object>> tempList = DbUp.upTable("lc_huodong_event_dakajf_user").dataSqlList(tSql, null);
				String can_dk = "Y";
				int dk_day = 0;
				if(tempList.size()>0){
					String dk_time = tempList.get(0).get("jf_dk_time").toString();
					long days = DateUtil.daysBetween(DateUtil.toSqlTimestamp(dk_time, "yyyy-MM-dd"), DateUtil.toSqlTimestamp(FormatHelper.upDateTime("yyyy-MM-dd"), "yyyy-MM-dd"));
					if(days < 1){
						can_dk = "N";
						dk_day = Integer.parseInt(tempList.get(0).get("jf_day").toString());
					}else if (days>=1&&days<2) {
						dk_day = Integer.parseInt(tempList.get(0).get("jf_day").toString());
						if(dk_day == 7){
							dk_day = 0;
						}
					}					
				}
				result.setCanDk(can_dk);
				for(Map<String, Object> map:dataList){
					DkInfo info = new DkInfo();
					info.setJfLogo(map.get("jf_logo")==null?"":map.get("jf_logo").toString());
					if(map.get("jf_type").toString().equals("01")){
						info.setJfNum(map.get("jf_min_num").toString());
					}else if (map.get("jf_type").toString().equals("02")) {
						int min = Integer.parseInt(map.get("jf_min_num").toString());
						int max = Integer.parseInt(map.get("jf_max_num").toString());
						info.setJfNum(min+"~"+max);
					}
					int jf_day = Integer.parseInt(map.get("jf_day").toString());
					info.setJfDay(jf_day);
					if(jf_day>dk_day){
						info.setDkYn("N");
					}else{
						info.setDkYn("Y");
					}
					if(can_dk.equals("N")&&dk_day == 7&&jf_day == 7){
						info.setJfNum(tempList.get(0).get("jf_num").toString());
					}
					result.addInfo(info);
				}
			}else{
				result.setResultCode(0);
				result.setResultMessage("没有设置签到奖励数据");
				return result;
			}
		}else{
			result.setResultCode(0);
			result.setResultMessage("没有绑定生效的签到活动");
			return result;
		}
		
		new PageActiveService().active(getUserCode(), getChannelId(), "4497471600630001");
		
		return result;
	}
	
}
