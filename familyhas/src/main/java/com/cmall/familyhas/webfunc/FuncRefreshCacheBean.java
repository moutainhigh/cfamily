package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 
 * 类: FuncRefreshCacheBean <br>
 * 描述: 惠豆是否可用设置 <br>
 * 作者: zhy<br>
 * 时间: 2016年12月15日 下午6:35:21
 */
public class FuncRefreshCacheBean extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) { 
		
		MWebResult result = new MWebResult();
		
		String operateValue = mDataMap.get("zw_f_operate_value");
		
		if (StringUtils.isBlank(operateValue)){
			result.setResultCode(916423017);
			result.setResultMessage(bInfo(916423017));
		}
		if (result.upFlagTrue()) {
			if("1".equals(operateValue)){
				XmasKv.upFactory(EKvSchema.HomehasBeanConfig).set("switch", "1");
			}else{
				XmasKv.upFactory(EKvSchema.HomehasBeanConfig).del("switch");
			}
		}
		return result;
		
	}
}
