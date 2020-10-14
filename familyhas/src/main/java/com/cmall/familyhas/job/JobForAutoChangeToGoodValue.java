package com.cmall.familyhas.job;


import org.quartz.JobExecutionContext;

import com.cmall.ordercenter.service.OrderService;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobForAutoChangeToGoodValue extends RootJob{
	
	@Override
	public void doExecute(JobExecutionContext context) {
		OrderService os = new OrderService();
		
		os.autoChangeToGoodValueFor15Days();
	}
	
}
