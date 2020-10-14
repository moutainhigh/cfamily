package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/** 
* @ClassName: ApiForBrandPreferenceContentInput 
* @Description: 品牌专题内容接口
* @author 张海生
* @date 2015-5-11 下午5:45:50 
*  
*/
public class ApiForBrandPreferenceContentInput extends RootInput{
	
	@ZapcomApi(value = "专题id" ,require = 1)
	private String infoCode = "";
	
	@ZapcomApi(value = "用户类型", remark="用户类型" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	@ZapcomApi(value = "屏幕宽度", remark="屏幕宽度" )
	private int picWidth = 0;

	public String getInfoCode() {
		return infoCode;
	}

	public void setInfoCode(String infoCode) {
		this.infoCode = infoCode;
	}

	public String getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}

	public int getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(int picWidth) {
		this.picWidth = picWidth;
	}

}
