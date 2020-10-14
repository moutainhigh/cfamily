package com.cmall.familyhas.job;

import java.text.ParseException;

import org.quartz.JobExecutionContext;

import com.cmall.groupcenter.service.RsyncOrderBeanFactory;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 定时任务：与微公社同步订单获取返利
 * @author pangjh
 *
 */
public class RsyncSellerOrderForGroup extends RootJob {

	public void doExecute(JobExecutionContext context) {
		
		try {
			RsyncOrderBeanFactory.getInstance().getRsyncSellerOrderService().doRsyncHAS();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			bLogError(0, e.getMessage());
		}
		
	}

}
