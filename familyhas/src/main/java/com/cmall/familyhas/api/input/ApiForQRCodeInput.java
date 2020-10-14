package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;
/**
 * 
 * @author xiegj
 *	短信验证码登录输入类
 */
public class ApiForQRCodeInput extends RootInput {
	
	@ZapcomApi(value = "二维码链接", remark = "二维码链接", demo = "8016123456", require = 1)
	private String qrStr = "";

	public String getQrStr() {
		return qrStr;
	}

	public void setQrStr(String qrStr) {
		this.qrStr = qrStr;
	}

}
