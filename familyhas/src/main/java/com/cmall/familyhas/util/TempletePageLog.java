package com.cmall.familyhas.util;

import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 2015-12-23 
 * @author fq
 *
 */
public class TempletePageLog {

	/**
	 * 
	 * @param content 操作内容
	 * @return 
	 */
	public static MWebResult upLog(String content){
		
		MWebResult mResult = new MWebResult();
		//记录日志
		String sysTime = DateUtil.getSysDateTimeString();
		//判断登陆用户是否为空
		String loginname=UserFactory.INSTANCE.create().getLoginName();
		if(loginname==null||"".equals(loginname)){
			mResult.inErrorMessage(941901073);
			return mResult;
		}
		String oper_content = "操作用户:【"+ loginname+ "】 时间:【" +sysTime +"】 操作内容:【"+content+"】";
		DbUp.upTable("fh_data_log").insert("oper_time",sysTime,"oper_content",oper_content);
		
		return mResult;
	}
	
}
