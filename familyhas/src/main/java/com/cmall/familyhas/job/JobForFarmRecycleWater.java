package com.cmall.familyhas.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.quartz.JobExecutionContext;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 惠惠农场定时回收雨滴
 * @remark 
 * @author 任宏斌
 * @date 2020年2月26日
 */
public class JobForFarmRecycleWater extends RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		MDataMap event = DbUp.upTable("sc_hudong_event_info")
				.one("event_type_code", "449748210010", "event_status", "4497472700020002");

		if (null != event) {
			String eventCode = event.get("event_code");

			String sql1 = "SELECT water_code FROM systemcenter.sc_huodong_farm_user_water "
					+ "WHERE event_code=:event_code AND flag = '1' AND create_time < DATE_SUB(NOW(),INTERVAL 1 DAY)";

			List<Map<String, Object>> memberCodes = DbUp.upTable("sc_huodong_farm_user_water")
					.dataSqlList(sql1, new MDataMap("event_code", eventCode));

			if (null != memberCodes && memberCodes.size() > 0) {
				for (Map<String, Object> water : memberCodes) {
					String waterCode = MapUtils.getString(water, "water_code");
					DbUp.upTable("sc_huodong_farm_user_water")
						.dataUpdate(new MDataMap("water_code", waterCode, "flag", "0"),  "flag", "water_code");
				}
			}
		}
	}

}
