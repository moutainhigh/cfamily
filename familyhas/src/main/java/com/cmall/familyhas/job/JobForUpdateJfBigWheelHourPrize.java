package com.cmall.familyhas.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 定时每天更新积分大转盘每小时奖品数量	
 * @author lgx
 *
 */
public class JobForUpdateJfBigWheelHourPrize extends RootJob {

	// 积分转盘抽奖限定开始时间(8)
	private final String jf_bigWheel_begin_time = bConfig("familyhas.jf_bigWheel_begin_time");
	// 积分转盘抽奖限定结束时间(22)
	private final String jf_bigWheel_end_time = bConfig("familyhas.jf_bigWheel_end_time");
	// 积分大转盘活动结束日期(2019-11-10)
	private final String jf_bigWheel_end_date = bConfig("familyhas.jf_bigWheel_end_date");
	// 积分大转盘白天发放奖品比例(0.8)
	private final String jf_bigWheel_day_rate = bConfig("familyhas.jf_bigWheel_day_rate");
	// 积分大转盘晚上发放奖品比例(0.2)
	//private final String jf_bigWheel_night_rate = bConfig("familyhas.jf_bigWheel_night_rate");
	
	/*public static void main(String[] args) {
		int a = 33;
		float b = Float.parseFloat("0.8");
		int c = (int) Math.floor(a*b);
		System.out.println(c);
		try {
			String dateTime = "2019-09-23";
			String nowTime = FormatHelper.upDateTime().substring(0, 10);
			Calendar calendar = Calendar.getInstance();
			
			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateTime));
			long end = calendar.getTimeInMillis();
			
			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(nowTime));
			long now = calendar.getTimeInMillis();
			
			long a = end - now;
			long b = a / 1000 / 60 / 60 / 24;
			System.out.println(b);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	@Override
	public void doExecute(JobExecutionContext context) {
		// 每天定时循环,插入24个小时每个小时的奖品(惠)数量
		
		String nowTime = FormatHelper.upDateTime();
		String eventCode = "";
		// 开始时间
		int beginHour = Integer.parseInt(jf_bigWheel_begin_time);
		// 结束时间
		int endHour = Integer.parseInt(jf_bigWheel_end_time);
		// 白天发放奖品比例(0.8)
		float dayRate = Float.parseFloat(jf_bigWheel_day_rate);
		// 晚上发放奖品比例(0.2)
		//float nightRate = Float.parseFloat(jf_bigWheel_night_rate);
		// 计算当前日期到结束日期的天数
		int dayNum = 0;
		String endDate = jf_bigWheel_end_date;
		String nowDate = nowTime.substring(0, 10);
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
			long endm = calendar.getTimeInMillis();
			calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(nowDate));
			long nowm = calendar.getTimeInMillis();
			// 天数
			dayNum = (int) ((endm - nowm) / 1000 / 60 / 60 / 24);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(dayNum > 0) { // 如果剩余天数大于0才执行定时
			String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210006' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
			Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
			
			if(eventInfoMap != null) { // 有发布的积分大转盘活动时才执行定时
				eventCode = (String) eventInfoMap.get("event_code");
				// 查询剩余总个数
				String sqlHui = "SELECT * FROM sc_huodong_event_dzpjf_rule WHERE event_code = '" + eventCode +"' and jp_title = '惠'";
				Map<String, Object> huiMap = DbUp.upTable("sc_huodong_event_dzpjf_rule").dataSqlOne(sqlHui, new MDataMap());
				// "惠"字总个数
				int totalJpnum = (int) huiMap.get("jp_num");
				// 白天每小时可抽中奖品个数
				int dayHourJpnum = (int) Math.floor(totalJpnum / dayNum * dayRate / (endHour - beginHour));
				// 晚上每小时可抽中奖品个数
				int nightHourJpnum = (int) Math.floor((totalJpnum / dayNum - (dayHourJpnum * (endHour - beginHour))) / (24 - (endHour - beginHour)));
				// 插入的数据是第二天的数据
				Date date=new Date();//取时间
				Calendar calendar1 = new GregorianCalendar();
				calendar1.setTime(date);
				calendar1.add(calendar1.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
				date=calendar1.getTime(); //这个时间就是日期往后推一天的结果 
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String tomorrowDate = formatter.format(date);
				
				// 循环插入24小时数据
				for (int i = 0; i < 24; i++) {
					MDataMap mDataMap = new MDataMap();					
					mDataMap.put("uid", WebHelper.upUuid());
					mDataMap.put("event_code", eventCode);
					if(i < 10) {
						mDataMap.put("begin_time", tomorrowDate+" 0"+i+":00:00");
						mDataMap.put("end_time", tomorrowDate+" 0"+i+":59:59");						
					}else {
						mDataMap.put("begin_time", tomorrowDate+" "+i+":00:00");
						mDataMap.put("end_time", tomorrowDate+" "+i+":59:59");						
					}
					if(i >= beginHour && i < endHour) { // 白天
						mDataMap.put("time_xd", "Y");
						mDataMap.put("hjp_num", dayHourJpnum+"");
					}else { // 晚上
						mDataMap.put("time_xd", "N");
						mDataMap.put("hjp_num", nightHourJpnum+"");
					}
					DbUp.upTable("lc_huodong_event_zjxd").dataInsert(mDataMap);
				}
			}
		}

	}

}
