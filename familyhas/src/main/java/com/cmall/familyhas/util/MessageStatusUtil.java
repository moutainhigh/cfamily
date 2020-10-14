package com.cmall.familyhas.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;

/**
 * 判断信息的状态
 * @author jlin
 *
 */
public class MessageStatusUtil extends BaseClass {

	/**
	 * 判断信息的状态
	 * @param yes
	 * @param status
	 * @param flag_show_time
	 * @param show_time
	 * @return
	 */
	public static boolean status(String yes,String status,String flag_show_time,String show_time){
		
		if(yes.equals(status)){
			if(yes.equals(flag_show_time)){
				if(StringUtils.isNotBlank(show_time)&&DateUtilA.compareNow2(show_time)<=0){
					return true;
				}
				
			}else{
				return true;
			}
		}
		return false;
	}
	
	/***
	 * 忽略null的状态
	 * @param yes
	 * @param status
	 * @param flag_show_time
	 * @param show_time
	 * @return
	 */
	public static boolean statusBlankState(String yes,String status,String flag_show_time,String show_time){
		
		if(status==null){
			return true;
		}
		
		if(yes.equals(status)){
			if(yes.equals(flag_show_time)){
				if(StringUtils.isNotBlank(show_time)&&DateUtilA.compareNow2(show_time)<=0){
					return true;
				}
				
			}else{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取详情连接
	 * @param yes
	 * @param flag_out_link
	 * @param out_link
	 * @param mess_code
	 * @param mess_title
	 * @return
	 */
	public String getLifeUrl(String yes,String flag_out_link,String out_link,String mess_code,String mess_title){
		String url="";
		if(flag_out_link==null){
			return url;
		}
		if(yes.equals(flag_out_link)){
			if(StringUtils.isNotBlank(out_link)){
				url=out_link;
			}
		}else{
			if(StringUtils.isNotBlank(mess_code)){
				try {
					url=FormatHelper.formatString(bConfig("homepool.lifeHome_details_url"),mess_code,URLEncoder.encode(mess_title, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		
		return url;
	}
	
	/**
	 * 获取详情连接 首页信息类型使用
	 * @param yes
	 * @param flag_out_link
	 * @param out_link
	 * @param category_code
	 * @param category_name
	 * @return
	 */
	public String getMesscUrl(String yes,String flag_out_link,String out_link,String category_code,String category_name){
		String url="";
		if(flag_out_link==null){
			return url;
		}
		if(yes.equals(flag_out_link)){
			if(StringUtils.isNotBlank(out_link)){
				url=out_link;
			}
		}else{
			if(StringUtils.isNotBlank(category_code)){
				url=FormatHelper.formatString(bConfig("homepool.messc_details_url"),category_code);
			}
		}
		
		return url;
	}
	
	
}
