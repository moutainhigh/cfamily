package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 刷新活动缓存
 */
public class FuncRefreshEventInfo extends FuncEdit {
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String zw_f_event_code = mDataMap.get("zw_f_event_code");
		if(StringUtils.isBlank(zw_f_event_code)){
			mResult.setResultCode(1);
			mResult.setResultMessage("活动编号不能为空");
			return mResult;
		}
		
		zw_f_event_code = zw_f_event_code.replaceAll("\\s+", "").replaceAll("，", ",");
		String[] codes = zw_f_event_code.split(",");
		
		for(String code : codes){
			if(StringUtils.isNotBlank(code) && code.startsWith("CX")){
				PlusHelperNotice.onChangeEvent(code);
			}
		}
		
		return mResult;
	}
	
}
