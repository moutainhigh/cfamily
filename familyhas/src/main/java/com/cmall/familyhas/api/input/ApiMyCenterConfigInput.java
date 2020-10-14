package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiMyCenterConfigInput extends RootInput {
	
	@ZapcomApi(value="广告入口类型",demo="ADTP001",remark="ADTP001:个人中心 ,ADTP002:支付成功")
	private String adverEntrType = "";
	@ZapcomApi(value="渠道编号",demo="449748610001",remark="449748610001:APP/微信 ,449748610002:小程序,449748610003:LD短信支付,449748610004:无限制")
	private String channel_id = "449748610004";
	
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getAdverEntrType() {
		return adverEntrType;
	}
	public void setAdverEntrType(String adverEntrType) {
		this.adverEntrType = adverEntrType;
	}
}
