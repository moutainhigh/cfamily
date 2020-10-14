package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 
 * @author xiegj
 * 短信验证登录输出类
 */
public class ApiForQRCodeResult extends RootResult {

	@ZapcomApi(value = "跳转类型", remark = "1:跳转链接(wap)，2:减免活动商品编号,3:无减免活动商品编号,4:浏览器打开类型,5:拼团,99:无法识别")
	private String gotoType = "";

	@ZapcomApi(value = "跳转内容", remark = "8016123456")
	private String gotoContent = "";

	public String getGotoType() {
		return gotoType;
	}

	public void setGotoType(String gotoType) {
		this.gotoType = gotoType;
	}

	public String getGotoContent() {
		return gotoContent;
	}

	public void setGotoContent(String gotoContent) {
		this.gotoContent = gotoContent;
	}
	
}
