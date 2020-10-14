package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 根据区域查询支付方式输入参数
 * @author jlin
 *
 */
public class ApiForGetPayTypeInput extends RootInput {

	@ZapcomApi(value = "三级区域编号" ,demo= "220105",require = 1)
	private String district_code = "";

	public String getDistrict_code() {
		return district_code;
	}

	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}
	
}
