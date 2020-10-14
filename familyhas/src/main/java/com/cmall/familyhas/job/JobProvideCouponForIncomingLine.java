package com.cmall.familyhas.job;

import org.quartz.JobExecutionContext;
import com.srnpr.xmassystem.invoke.ref.GetCustInComingLineInfo;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 
 *<p>Description:进线损耗发放优惠券定时 <／p> 
 * @author zb
 * @date 2020年9月15日
 *
 */
public class JobProvideCouponForIncomingLine extends RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		// 调用app接口获取进线信息	
		GetCustInComingLineInfo custInComingLineInfo = (GetCustInComingLineInfo)BeansHelper.upBean(GetCustInComingLineInfo.NAME);		
		custInComingLineInfo.sendCouponMessageForInComingLineUser();
			
	}
}
