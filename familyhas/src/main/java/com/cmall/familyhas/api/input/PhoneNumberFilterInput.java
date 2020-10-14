package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 推送设备信息输入参数
 * @author fq
 *
 */
public class PhoneNumberFilterInput extends RootInput{

	@ZapcomApi(value="一批需处理的手机号",require=1)
	private String nums = "";
	
	@ZapcomApi(value="对比的版本",require=1)
	private String compareVersion = "";

	public String getNums() {
		return nums;
	}

	public void setNums(String nums) {
		this.nums = nums;
	}

	public String getCompareVersion() {
		return compareVersion;
	}

	public void setCompareVersion(String compareVersion) {
		this.compareVersion = compareVersion;
	}

	
}
