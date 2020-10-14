package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 意见反馈接口输入参数
 * @author liqt
 */
public class ApiForSuggestionFeedbackInput extends RootInput{
	
	@ZapcomApi(value="流水号",remark="流水号")
	private String serialNumber="";
	
	@ZapcomApi(value="意见反馈",remark="意见反馈",require=1,verify={"minlength=1"})
	private String suggestionFeedback="";

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSuggestionFeedback() {
		return suggestionFeedback;
	}

	public void setSuggestionFeedback(String suggestionFeedback) {
		this.suggestionFeedback = suggestionFeedback;
	}
}
