package com.cmall.familyhas.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.HongbaoyuSearchInput;
import com.cmall.familyhas.api.result.HongbaoyuSearchResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiForHongbaoyuSearch extends RootApi<HongbaoyuSearchResult, HongbaoyuSearchInput> {

	@Override
	public HongbaoyuSearchResult Process(HongbaoyuSearchInput inputParam, MDataMap mRequestMap) {
		HongbaoyuSearchResult result = new HongbaoyuSearchResult();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String nowTime = DateUtil.getNowTime();
		String sql1 = "select b.* from sc_hudong_event_hongbao_info a left join sc_hudong_event_info b on a.event_code = b.event_code"
				+ " where b.event_type_code = '449748210001' and b.event_status = '4497472700020002' and b.begin_time < '"+nowTime+"' and b.end_time > '"+ nowTime + "'";
		List<Map<String, Object>> dataSqlList1 = DbUp.upTable("sc_hudong_event_hongbao_info").dataSqlList(sql1, null);
		if (null != dataSqlList1 && dataSqlList1.size() > 0) {
			Map<String, Object> map = dataSqlList1.get(0);
			String endTime = map.get("end_time").toString();
			String activityJiedian = map.get("activity_jiedian").toString();
			String jiedianAging = map.get("jiedian_aging").toString();
			String timeCategory = map.get("time_category").toString();
			//先判断当前时间是否在今天某个节点活动中
			String[] split = activityJiedian.split(",");
			
			long actLastTime = 0;
			if("449747280001".equals(timeCategory)) {
				actLastTime = Integer.parseInt(jiedianAging) *  3600;
			}else if("449747280002".equals(timeCategory)) {
				actLastTime = Integer.parseInt(jiedianAging) *  60;
			}else {
				actLastTime = Integer.parseInt(jiedianAging);
			}
			TimeModel  bingo = null;
			TimeModel  nextAct = new TimeModel();
			TimeModel  daojishi = null;
			TimeModel  yijieshu = null;
			nextAct.setToStartTime(2*24*3600);
			for(String jiedianStr: split) {
				TimeModel timeToSeconds = timeToSeconds(nowTime,jiedianStr,actLastTime);
				if(timeToSeconds.isBingo()) {
					bingo = timeToSeconds;
				}
				if(timeToSeconds.getToStartTime()!=0&&timeToSeconds.getToStartTime()<nextAct.getToStartTime()) {
					nextAct = timeToSeconds;
				}
				if(timeToSeconds.getActState()==3) {
					daojishi = timeToSeconds;
				}
				if(timeToSeconds.getActState()==2) {
					yijieshu = timeToSeconds;
				}
			}
			
			if(bingo!=null) {
				result.setActState(1);
				result.setEndSeconds(bingo.getToEndTime());
			}else {
				if(daojishi!=null) {
					result.setActState(daojishi.getActState());
				}else if(yijieshu!=null) {
					result.setActState(yijieshu.getActState());
				}
			}
			
			if(nextAct.getToStartTime()!=2*24*3600) {
				result.setNextActSeconds(nextAct.getToStartTime());
				String nextTime = "";
				String jiedianTime = nextAct.getJiedianTime();
				if(nextAct.isToday) {
					String substring = nowTime.substring(0,11);
					nextTime = substring+jiedianTime+":00";
				}else {
					Date date = null;
					try {
						date = sdf.parse(nowTime);
					} catch (ParseException e) {
						e.printStackTrace();
					}//取时间
			        Calendar calendar = new GregorianCalendar();
			        calendar.setTime(date);
			        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
			        date=calendar.getTime(); //这个时间就是日期往后推一天的结果   
			        String dateTime = sdf.format(date);
			        String substring = dateTime.substring(0, 11);
			        nextTime = substring+jiedianTime+":00";
				}
				
				if(endTime.compareTo(nextTime)>=0) {
					result.setNextActTime(nextTime);
				}
			}
			
		}else {
			String sql2 = "select b.* from sc_hudong_event_hongbao_info a left join sc_hudong_event_info b on a.event_code = b.event_code"
					+ " where b.event_type_code = '449748210001' and b.event_status = '4497472700020002' and b.begin_time > '"+nowTime+"'";
			List<Map<String, Object>> dataSqlList2 = DbUp.upTable("sc_hudong_event_hongbao_info").dataSqlList(sql2, null);
			Map<String, Object> checkMap = new HashMap<>();
			checkMap.put("begin_time", "2050-08-14 13:25:00");
			for(Map<String, Object> map : dataSqlList2) {
				if(map.get("begin_time").toString().compareTo(
						checkMap.get("begin_time").toString())<0
					) {
					checkMap = map;
				}
			}
			
			if(!"2050-08-14 13:25:00".equals(checkMap.get("begin_time").toString())) {
				String begin_time = checkMap.get("begin_time").toString();
				String begin_shifenmiao =  begin_time.substring(11);
				String activityJiedian = checkMap.get("activity_jiedian").toString();
				String littleTime = "23:59";
				String littleTimeBeiyong = "23:59";
				for(String str : activityJiedian.split(",")) {
					if(str.compareTo(littleTimeBeiyong)<0) {
						littleTimeBeiyong = str;
					}
					if(str.compareTo(littleTime)<0&&str.compareTo(begin_shifenmiao)>=0) {
						littleTime =  str;
					}
				};
				String substring   =   "";
				if(!"23:59".equals(littleTime)) {
					substring = begin_time.substring(0, 11) + littleTime + ":00";
				}else {
					Date date = null;
					try {
						date = sdf.parse(begin_time);
					} catch (ParseException e) {
						e.printStackTrace();
					}//取时间
			        Calendar calendar = new GregorianCalendar();
			        calendar.setTime(date);
			        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
			        date=calendar.getTime(); //这个时间就是日期往后推一天的结果   
			        String dateTime = sdf.format(date);
			        substring = dateTime.substring(0, 11) + littleTimeBeiyong + ":00";
				}
				result.setNextActTime(substring);
				
				try {
					long time1 = sdf.parse(substring).getTime();
					long time2 = sdf.parse(nowTime).getTime();
					if((time1 - time2) <= (1*3600*1000)) {
						result.setActState(3);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else {
				result.setResultCode(0);
				result.setResultMessage("没有活动，请联系客服");
			}
			
		}
		return result;
	}

	private  TimeModel timeToSeconds(String currStr,String jiedianTime,long lastTime) {
		TimeModel timeModel = new TimeModel();
		SimpleDateFormat sdfOne = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
		long currTime = 0;
		long actTime = 0;
		//算出当前时间距离今天 0点的秒数
		try {
			currTime = (sdf2.parse(currStr).getTime() - (sdfOne.parse(sdfOne.format(sdf2.parse(currStr))).getTime()))/1000;
			//此活动节点距离今天0点的秒数
			actTime = (sdf3.parse(jiedianTime).getTime() - (sdf3.parse("00:00").getTime()))/1000;
			//如果当前时间在此档范围内，命中
			if(currTime>=actTime&&currTime<(actTime+lastTime)) {
				timeModel.setBingo(true);
				timeModel.setToEndTime(actTime+lastTime - currTime);
				timeModel.setActState(1);
				timeModel.setJiedianTime(jiedianTime);
			}else {
				//如果当前时间大于此活动结束时间，计算到第二天此时间的秒数
				if(currTime > actTime+lastTime) {
					
					
					long toStartTime = actTime - currTime + (1*24*3600);
					
					if(toStartTime<=1*3600) {
						timeModel.setActState(3);
					}else if(currTime - (actTime+lastTime)<= 5*60) {
						timeModel.setActState(2);
					}
					
					timeModel.setToStartTime(toStartTime);
					timeModel.setToday(false);
					timeModel.setJiedianTime(jiedianTime);
					
				}else {
					//此活动开始时间大于当前时间
					timeModel.setToStartTime(actTime - currTime);
					timeModel.setJiedianTime(jiedianTime);
					if(actTime - currTime <=  1*3600) {
						timeModel.setActState(3);
					}
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return timeModel;
	}
	
	class TimeModel{
		private boolean bingo = false;
		private boolean isToday = true;
		private String jiedianTime = "";
		private long toEndTime = 0;
		private long toStartTime = 0;
		private Integer actState =  0;
		public Integer getActState() {
			return actState;
		}
		public void setActState(Integer state) {
			this.actState = state;
		}
		public boolean isToday() {
			return isToday;
		}
		public void setToday(boolean isToday) {
			this.isToday = isToday;
		}
		public String getJiedianTime() {
			return jiedianTime;
		}
		public void setJiedianTime(String jiedianTime) {
			this.jiedianTime = jiedianTime;
		}
		public boolean isBingo() {
			return bingo;
		}
		public void setBingo(boolean bingo) {
			this.bingo = bingo;
		}
		public long getToEndTime() {
			return toEndTime;
		}
		public void setToEndTime(long toEndTime) {
			this.toEndTime = toEndTime;
		}
		public long getToStartTime() {
			return toStartTime;
		}
		public void setToStartTime(long toStartTime) {
			this.toStartTime = toStartTime;
		}
		
	}
	
}
