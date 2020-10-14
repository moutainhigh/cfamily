package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 我的暂存款
 * @author dyc
 * date: 2014-11-20
 * @version1.0
 */

public class MyTemporaryStore {

	@ZapcomApi(value = "收入", remark = "收入", demo = "201")
	private String storeGold_income = "0";
	
	@ZapcomApi(value = "支出	", remark = "支出", demo = "201")
	private String storeGold_expend = "0";
	
	@ZapcomApi(value = "创建时间	", remark = "创建时间")
	private String storeGold_etr_date = "";
	
	@ZapcomApi(value = "详细说明	", remark = "详细说明")
	private String remark = "";

	public String getStoreGold_income() {
		return storeGold_income;
	}

	public void setStoreGold_income(String storeGold_income) {
		this.storeGold_income = storeGold_income;
	}

	public String getStoreGold_expend() {
		return storeGold_expend;
	}

	public void setStoreGold_expend(String storeGold_expend) {
		this.storeGold_expend = storeGold_expend;
	}

	public String getStoreGold_etr_date() {
		return storeGold_etr_date;
	}

	public void setStoreGold_etr_date(String storeGold_etr_date) {
		this.storeGold_etr_date = storeGold_etr_date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
