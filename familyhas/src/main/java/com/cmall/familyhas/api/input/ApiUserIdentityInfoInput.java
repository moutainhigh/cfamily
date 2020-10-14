package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 用户身份信息输入参数
 * @author pang_jhui
 *
 */
public class ApiUserIdentityInfoInput extends RootInput {
	
	@ZapcomApi(value = "身份证号码",require=1)
	private String idNumber = "";
	
	@ZapcomApi(value = "操作标志", remark = "CHECK:校验信息 ;UPDATE:更新信息",require=1)
	private String operFlag = "";

	/**
	 * 获取身份证号码
	 * @return
	 */
	public String getIdNumber() {
		return idNumber;
	}

	/**
	 * 设置身份证号码
	 * @param idNumber
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	/**
	 * 获取操作标识
	 * @return
	 */
	public String getOperFlag() {
		return operFlag;
	}

	/**
	 * 设置操作标识
	 * @param operFlag
	 */
	public void setOperFlag(String operFlag) {
		this.operFlag = operFlag;
	}

}
