package com.cmall.familyhas.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.quartz.JobExecutionContext;

import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.RandomUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 惠惠农场定时送雨滴
 * @remark 
 * @author 任宏斌
 * @date 2020年2月26日
 */
public class JobForFarmSendWater extends RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		MDataMap event = DbUp.upTable("sc_hudong_event_info")
				.one("event_type_code", "449748210010", "event_status", "4497472700020002");

		if (null != event) {
			String eventCode = event.get("event_code");

			String sql1 = "SELECT DISTINCT(member_code) FROM systemcenter.sc_huodong_farm_user_tree "
					+ "WHERE event_code=:event_code AND tree_stage in ('449748450001','449748450002','449748450003','449748450004') ";

			List<Map<String, Object>> memberCodes = DbUp.upTable("sc_huodong_farm_user_tree")
					.dataSqlList(sql1, new MDataMap("event_code", eventCode));

			if (null != memberCodes && memberCodes.size() > 0) {
				// 从配置表读取随机赠送雨滴数区间
				MDataMap config = DbUp.upTable("sc_huodong_farm_config").one("type", "449748480002");
				int beginNum = (int) Double.parseDouble(config.get("begin_num"));
				int endNum = (int) Double.parseDouble(config.get("end_num"));

				for (Map<String, Object> map : memberCodes) {
					String memberCode = MapUtils.getString(map, "member_code");
					String waterNum = RandomUtil.getRandomBetween(beginNum, endNum)+"";
					DbUp.upTable("sc_huodong_farm_user_water").insert(
							"uid", WebHelper.upUuid(), 
							"event_code", eventCode, 
							"member_code", memberCode, 
							"water_code", WebHelper.upCode("FW"), 
							"water_num", waterNum, 
							"original_water_num", waterNum, 
							"create_time", DateUtil.getSysDateTimeString(), 
							"water_type", "449748530001", 
							"flag", "1");
				}
			}
		}
	}

}
