package com.cmall.familyhas.job;

import java.text.ParseException;

import org.quartz.JobExecutionContext;

import com.cmall.groupcenter.behavior.api.ApiBfdUnderProduct;
import com.cmall.groupcenter.service.RsyncOrderBeanFactory;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 定时任务：同步百分点下架商品编号
 * @author jiangdf
 *
 */
public class RsyncProductStratusForBaiFenDian extends RootJob {

	public void doExecute(JobExecutionContext context) {
		
		try {
		     new ApiBfdUnderProduct().doProcess();
		} catch (Exception e) {
			bLogError(0, e.getMessage());
		}
		
	}

}
