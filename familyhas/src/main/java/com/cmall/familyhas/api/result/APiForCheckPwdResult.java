package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class APiForCheckPwdResult extends RootResultWeb {
	
	@ZapcomApi(value = "解锁剩余时间(以分钟计算)")
	private int remainMinute;

	public int getRemainMinute() {
		return remainMinute;
	}

	public void setRemainMinute(int remainMinute) {
		this.remainMinute = remainMinute;
	}
}
