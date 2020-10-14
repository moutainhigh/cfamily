package com.cmall.familyhas.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.JobExecutionContext;

import com.srnpr.xmassystem.load.LoadWebTemplete;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 定时清理专题页面缓存  (临时方案)
 */
public class JobForTempletePageCache extends RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		Date time = DateUtils.addDays(new Date() , -30);  // 最近30天新增的模版
		List<Map<String, Object>> itemList = DbUp.upTable("fh_data_page").dataSqlList("select page_number from fh_data_page where dal_status = '1001' and (create_time >= :create_time OR update_time >= :create_time)", new MDataMap("create_time", DateFormatUtils.format(time, "yyyy-MM-dd")));
		
		LoadWebTemplete load = new LoadWebTemplete();
		for(Map<String, Object> map : itemList){
			load.deleteInfoByCode(map.get("page_number")+"-449747430001");
			load.deleteInfoByCode(map.get("page_number")+"-449747430003");
			load.deleteInfoByCode(map.get("page_number")+"-449747430023");
		}
	}
}
