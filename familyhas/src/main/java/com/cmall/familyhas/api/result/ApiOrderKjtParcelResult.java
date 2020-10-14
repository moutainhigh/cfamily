package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiOrderKjtParcelResult {
	@ZapcomApi(value="包裹状态" ,remark="4497153900010001:下单成功-未付款,4497153900010002:下单成功-未发货,4497153900010003:已发货,4497153900010004:已收货,4497153900010005	:交易成功,4497153900010006:交易失败")
	private String localStatus = "";
	@ZapcomApi(value="包裹商品信息")
	private List<ApiOrderKjtDetailsResult> apiOrderKjtDetailsList = new ArrayList<ApiOrderKjtDetailsResult>();
	
	public String getLocalStatus() {
		return localStatus;
	}

	public void setLocalStatus(String localStatus) {
		this.localStatus = localStatus;
	}

	public List<ApiOrderKjtDetailsResult> getApiOrderKjtDetailsList() {
		return apiOrderKjtDetailsList;
	}

	public void setApiOrderKjtDetailsList(
			List<ApiOrderKjtDetailsResult> apiOrderKjtDetailsList) {
		this.apiOrderKjtDetailsList = apiOrderKjtDetailsList;
	}

	
	
}
