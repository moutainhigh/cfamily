package com.cmall.familyhas.job;

import java.text.ParseException;

import org.quartz.JobExecutionContext;

import com.cmall.groupcenter.service.RsyncReturnOrderBeanFactory;
import com.srnpr.zapweb.rootweb.RootJob;
/**
 *  定时任务：与微公社同步退货单信息
 * @author zmm
 *
 */
public class RsyncReturnOrderServiceGroup extends RootJob{

	public void doExecute(JobExecutionContext context) {

		try {
			RsyncReturnOrderBeanFactory.getInstance().getRsyncReturnOrderService().doRsyncHAS();
		} catch (ParseException e) {
			bLogError(0, e.getMessage());
		}

	}
}
