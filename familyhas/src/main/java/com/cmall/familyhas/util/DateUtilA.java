package com.cmall.familyhas.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 日期工具类
 * @author jlin
 *
 */
public class DateUtilA extends com.cmall.systemcenter.common.DateUtil {

	/**
	 * 获取某年某月的最后一天
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getlastDay(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 判断某些变态调用
		if (month > 11)month = 11;
		if (month < 0)month = 0;

		cal.set(Calendar.YEAR, month == 11 ? (year + 1) : year);
		cal.set(Calendar.MONTH, month == 11 ? 0 : (month));
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
	
	public static String createTime(String year){
		if(!"".equals(year)){
			return year+"-01-01";
		}
		return "";
	}
	
	/**
	 * 比较时间大小
	 * 时间格式：2014-12-02 20:14:10
	 * <br>大于当前时间返回正数，等于 0，小于 负数
	 * @param dateTime
	 * @return
	 */
	public int compareNow(String dateTime){
		return compareNow2(dateTime);
	}
	
	/**
	 * 比较时间大小
	 * 时间格式：2014-12-02 20:14:10
	 * <br>大于当前时间返回正数，等于 0，小于 负数
	 * @param dateTime
	 * @return
	 */
	public static int compareNow2(String dateTime){
		try {
			Date date=sdfDateTime.parse(dateTime);
			return date.compareTo(new Date());
		} catch (ParseException e) {
			return 0;
		}
	}
	
	/**
	 * 比较两个时间
	 * 时间格式：2014-12-02 20:14:10
	 * <br>大于结束时间返回正数，等于 0，小于 负数
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public synchronized static int compare(String start_time,String end_time){
		try {
			
			if(StringUtils.isBlank(start_time)){
				return -1;
			}
			
			if(StringUtils.isBlank(end_time)){
				return 1;
			}
			Date date1=sdfDateTime.parse(start_time);
			Date date2=sdfDateTime.parse(end_time);
			return date1.compareTo(date2);
		} catch (ParseException e) {
			return 0;
		}
	}
	
	/**
	 * 比较两个日期时间
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	 public static int compareDateNew(String beginTime, String endTime) {
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        try {
	            Date dt1 = df.parse(beginTime.trim());
	            Date dt2 = df.parse(endTime.trim());
	            if (dt1.getTime() > dt2.getTime()) {
	                return 1;     //beginTime时间大于endTime时间
	            } else if (dt1.getTime() < dt2.getTime()) {
	                return -1;     //beginTime时间小于endTime时间
	            } else {
	                return 0;      //beginTime时间等于endTime时间
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return 0;
	    }
	
	/**
	 * 通过LONG型的时间数据得到月份。
	 * @param timeIsLong
	 * @return
	 */
	public synchronized static int getMonth(Long timeIsLong) {
		Calendar calendar = Calendar.getInstance();
		if (timeIsLong != null) {
			calendar.setTimeInMillis(timeIsLong);
		}
		int month = calendar.get(Calendar.MONTH);
		return month+1;
	}
	
	/**
	 * 以YYYY/MM/DD HH24:MI:SS格式返回系统日期时间
	 * 
	 * @return 系统日期时间
	 * @since 1.0
	 * @history
	 */
	public synchronized static String getSysDateTimeString() {
		String sysFormat = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sysDateTime = new SimpleDateFormat(sysFormat);
		return toString(new java.util.Date(System.currentTimeMillis()),sysDateTime);
	}
	public static void main(String[] args) {
//		System.out.println("2014".equals(DateUtil.getYear(null)+""));
		//System.out.println(DateUtil.toString(getlastDay(2015, 2), DateUtil.DATE_FORMAT_DATEONLY));
	}
}
