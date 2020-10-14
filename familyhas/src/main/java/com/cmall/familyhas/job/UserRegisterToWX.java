package com.cmall.familyhas.job;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.listener.UserRegisterToWxJmsListener;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.srnpr.zapweb.rootweb.RootJob;


/** 
* @ClassName: UserRegisterToWX 
* @Description: 惠家友或微公社注册后推送给微信商城注册
* @author 张海生
* @date 2015-4-3 上午11:35:03 
*  
*/
public class UserRegisterToWX extends RootJob{

	private final static UserRegisterToWxJmsListener LISTENSER = new UserRegisterToWxJmsListener();
	
	public void doExecute(JobExecutionContext context) {

		JmsNoticeSupport.INSTANCE.onReveiveQueue(JmsNameEnumer.OnRegisterWX, LISTENSER);

	}
}
