package com.cmall.familyhas.job.hjybean;

import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 惠豆相关的定时处理
 */
public class JobForHjyBeanExecTimer extends RootJobForHjyBeanExec{

	@Override
	public IBaseResult execByInfo(MDataMap execInfo) {
		MWebResult mWebResult = new MWebResult();
		
		String info = execInfo.get("exec_info");
		String type = execInfo.get("exec_type");
		
		if("449747930001".equals(type)){
			return new JobForHjyBeanOrderSuccess().execByInfo(info);
		}else if("449747930002".equals(type)){
			return new JobForHjyBeanOrderCancel().execByInfo(info);
		}else if("449747930003".equals(type)){
			return new JobForHjyBeanReturnMoney().execByInfo(info);
		}else if("449747930004".equals(type)){
			return new JobForHjyBeanReturnGoods().execByInfo(info);
		}
		
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("'449747930001','449747930002','449747930003','449747930004'");
		config.setMaxExecNumber(10);
		return config;
	}

}
