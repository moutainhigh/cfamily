package com.cmall.familyhas.job;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.service.CollageService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;
/**
 * 对于拼团时效低于两小时的订单，机器人保底拼单，保证成功
 * @author Angel Joy
 *
 */
public class JobForRobotCollage extends RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		Integer timeliness = 2;
		String expireTime = DateUtil.addDateMinut(DateUtil.getSysDateTimeString(), timeliness);
		String sql = "SELECT * FROM systemcenter.sc_event_collage WHERE collage_status = '449748300001' AND expire_time <= '"+expireTime+"'";
		//即将拼团失败的单子。
		List<Map<String,Object>> collageList = DbUp.upTable("sc_event_collage").dataSqlList(sql, null);
		CollageService cs = new CollageService();
		for(Map<String,Object> map : collageList) {
			if(map == null) {
				continue;
			}
			MDataMap mDataMap = new MDataMap(map);
			String collageCode = mDataMap.get("collage_code");
			String eventCode = mDataMap.get("event_code");
			MDataMap eventInfo = DbUp.upTable("sc_event_info").one("event_code",eventCode);
			if(eventInfo != null&&!eventInfo.isEmpty() ) {
				String collageType = eventInfo.get("collage_type");
				if(collageType.equals("4497473400050002")) {//邀新团不做补团操作
					continue;
				}
			}
			cs.robotComplateCollage(collageCode);
		}
	}
	
	
}
