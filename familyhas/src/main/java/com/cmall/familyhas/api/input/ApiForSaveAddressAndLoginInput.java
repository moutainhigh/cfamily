package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/** 
* @ClassName: ApiForSaveAddressAndLoginInput 
* @Description: 保存收货地址并登录输入实体
* @author 张海生
* @date 2015-8-10 上午10:34:29 
*  
*/
public class ApiForSaveAddressAndLoginInput extends RootInput{
	
	@ZapcomApi(value = "收货人", demo = "张三", require = 1, remark = "收货人")
	private String receiver = "";
	
	@ZapcomApi(value = "推荐人手机号", require = 0, remark = "第三方注册选填推荐人手机号", demo = "13388888888")
	private String referrerMobile = "";
	
	@ZapcomApi(value = "手机号", demo = "13011111111", require = 1, remark = "手机号")
	private String mobile = "";
	
	@ZapcomApi(value = "验证码", demo = "1234", require = 1, remark = "验证码")
	private String verifyCode = "";
	
	@ZapcomApi(value="地区编码",require=1)
	private String areaCode="";
	
	@ZapcomApi(value="标记",remark="1:默认, 0不默认",verify={ "in=0,1" })
	private String flagDefault="1";
	
	@ZapcomApi(value = "详细地址", demo = "dsfs", require = 1, remark = "详细地址")
	private String detailAddress = "";

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getFlagDefault() {
		return flagDefault;
	}

	public void setFlagDefault(String flagDefault) {
		this.flagDefault = flagDefault;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getReferrerMobile() {
		return referrerMobile;
	}

	public void setReferrerMobile(String referrerMobile) {
		this.referrerMobile = referrerMobile;
	}
}
