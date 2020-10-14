package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiHomeNavTabInput extends RootInput {

	@ZapcomApi(value = "展示平台", remark="默认为app首页" ,demo="4497471600100001:APP首页，4497471600100002:Wap商城首页，4497471600100003:Web商城首页")
	private String viewType = "4497471600100001";
	@ZapcomApi(value = "公众号渠道编码", remark="公众号渠道编码" )
	private String outChannelId = "";
	
	@ZapcomApi(value = "预览的导航项编码", remark="预览专用" )
	private String yuLan_navCode = "";
	
	public String getYuLan_navCode() {
		return yuLan_navCode;
	}

	public void setYuLan_navCode(String yuLan_navCode) {
		this.yuLan_navCode = yuLan_navCode;
	}

	public String getOutChannelId() {
		return outChannelId;
	}

	public void setOutChannelId(String outChannelId) {
		this.outChannelId = outChannelId;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	
}
