package com.cmall.familyhas.job;

import org.quartz.JobExecutionContext;

import com.cmall.groupcenter.third.model.GroupAccountInfoInput;
import com.cmall.groupcenter.third.model.GroupAccountInfoResult;
import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapweb.rootweb.RootJob;
import com.srnpr.zapweb.websupport.ApiCallSupport;

/**
 * 检查微公社的余额接口是否可用，并设置系统标识<br>
 * 类: JobForGroupMoneyCheck <br>
 * 作者: 赵俊岭 zhaojunling@huijiayou.cn<br>
 * 时间: 2016年8月23日 下午12:16:22
 */
public class JobForGroupMoneyCheck extends RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		GroupAccountInfoInput accountInfoInput = new GroupAccountInfoInput();
		accountInfoInput.setMemberCode(bConfig("familyhas.wgx_check_membercode"));
		accountInfoInput.setReckonOrderCode("");
		ApiCallSupport<GroupAccountInfoInput, GroupAccountInfoResult> apiCallSupport = new ApiCallSupport<GroupAccountInfoInput, GroupAccountInfoResult>();
		
		GroupAccountInfoResult accountInfoResult = new GroupAccountInfoResult();
		
		try {
			accountInfoResult = apiCallSupport.doCallApi(
					bConfig("xmassystem.group_pay_url"),
					bConfig("xmassystem.group_pay_accountInfo_face"),
					bConfig("xmassystem.group_pay_key"),
					bConfig("xmassystem.group_pay_pass"), accountInfoInput,
					accountInfoResult);
			// 启用的标识在后台手动开启
			//XmasKv.upFactory(EKvSchema.CgroupMoney).set("use", "1");
		} catch (Exception e) {
//			XmasKv.upFactory(EKvSchema.CgroupMoney).set("use", "0");
			//modify by zht
			XmasKv.upFactory(EKvSchema.CgroupMoney).hset("Config", "use", "0");
		}
		
	}

}
