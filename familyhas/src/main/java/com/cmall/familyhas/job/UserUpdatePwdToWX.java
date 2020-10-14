package com.cmall.familyhas.job;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.listener.UserUpdatePwdToWxJmsListener;
import com.cmall.systemcenter.enumer.JmsNameEnumer;
import com.cmall.systemcenter.jms.JmsNoticeSupport;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * @ClassName: UserUpdatePwdToWX
 * @Description: 惠家友或微公社修改密码推送给微信商城
 * @author 张海生
 * @date 2015-4-3 下午3:00:21
 * 
 */
public class UserUpdatePwdToWX extends RootJob {

	private final static UserUpdatePwdToWxJmsListener LISTENSER = new UserUpdatePwdToWxJmsListener();

	public void doExecute(JobExecutionContext context) {

		JmsNoticeSupport.INSTANCE.onReveiveQueue(JmsNameEnumer.OnChangePwdWX,
				LISTENSER);

	}
}
