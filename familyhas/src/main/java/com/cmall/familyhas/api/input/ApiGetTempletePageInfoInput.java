package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetTempletePageInfoInput extends RootInput{
	
	@ZapcomApi(value="页面编号")
	private String pageNum = "" ;
	
	@ZapcomApi(value="设备宽度",remark="用于压缩图片")
	private Integer width = 0;
	
	@ZapcomApi(value="来源编号",remark="APP,WEB")
	private String sourceCode = "WEB";
	
	@ZapcomApi(value="客户端手机号",remark="用于查看用户是否享有折扣")
	private String mobile = "";
	
	@ZapcomApi(value="渠道编号",remark="449747430001")
	private String channelId = "449747430001";

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

}
