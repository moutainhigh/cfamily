package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 下单是否可用微公社余额
 * @author Lee
 *
 */
public class FuncRefreshCacheBalance extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) { 
		
		MWebResult result = new MWebResult();
		
		String operateValue = mDataMap.get("zw_f_operate_value");
		
		if (StringUtils.isBlank(operateValue)){
			result.setResultCode(916423017);
			result.setResultMessage(bInfo(916423017));
		}
		if (result.upFlagTrue()) {
			if("1".equals(operateValue)){
//				XmasKv.upFactory(EKvSchema.CgroupMoney).set("use", "1");
				XmasKv.upFactory(EKvSchema.CgroupMoney).hset("Config", "use", "1");
			}else{
				XmasKv.upFactory(EKvSchema.CgroupMoney).hdel("Config", "use");
			}
		}
		return result;
		
	}
}
