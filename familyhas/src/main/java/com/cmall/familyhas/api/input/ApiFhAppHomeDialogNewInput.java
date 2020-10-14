package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;
/**
 * @descriptions 
 * 
 * @refactor 
 * @author Yangcl
 * @date 2016-5-5-下午5:38:44
 * @version 1.0.0
 */
public class ApiFhAppHomeDialogNewInput extends RootInput {
	
	// 仅在接口测试页面测试人员进行测试的时候传入，APP客户端无需传入此参数
	@ZapcomApi(value = "系统当前时间",remark="接口测试页面测试人员进行测试的时候传入，APP客户端无需传入此参数")
	private String curentTime = "";
	
	@ZapcomApi(value = "渠道编号",remark="449748660001(app)  449748660002(惠家有小程序)")
	private String channels = "";
	
	
	@ZapcomApi(value = "渠道编号",remark="449748700002(首页)  449748700003(支付结果页)")
	private String showLocation = "";
	
	
	public String getCurentTime() {
		return curentTime;
	}

	public void setCurentTime(String curentTime) {
		this.curentTime = curentTime;
	}

	public String getChannels() {
		return channels;
	}

	public void setChannels(String channels) {
		this.channels = channels;
	}

	public String getShowLocation() {
		return showLocation;
	}

	public void setShowLocation(String showLocation) {
		this.showLocation = showLocation;
	}
	
	
	
	
}
