package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForFillShipmentsInput extends RootInput {

	@ZapcomApi(value = "售后单号", require = 1, demo = "RTG140123100003")
	private String afterCode = "";

	@ZapcomApi(value = "退换货信息", require = 0, demo = "xxx")
	private String tips = "";

	@ZapcomApi(value = "快递公司编号", require = 1, demo = "xxx")
	private String logisticsCompanyName = "";

	@ZapcomApi(value = "快递单号", require = 1, demo = "xxx")
	private String logisticsCode = "";

	@ZapcomApi(value = "快递单图片", require = 0, demo = "xxx")
	private List<String> logisticsPic = new ArrayList<String>();

	public String getAfterCode() {
		return afterCode;
	}

	public void setAfterCode(String afterCode) {
		this.afterCode = afterCode;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public String getLogisticsCompanyName() {
		return logisticsCompanyName;
	}

	public void setLogisticsCompanyName(String logisticsCompanyName) {
		this.logisticsCompanyName = logisticsCompanyName;
	}

	public String getLogisticsCode() {
		return logisticsCode;
	}

	public void setLogisticsCode(String logisticsCode) {
		this.logisticsCode = logisticsCode;
	}

	public List<String> getLogisticsPic() {
		return logisticsPic;
	}

	public void setLogisticsPic(List<String> logisticsPic) {
		this.logisticsPic = logisticsPic;
	}
}
