package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class HbTxDetailInfo {
	@ZapcomApi(value = "惠币数量")
	private String apply_money = "";
	@ZapcomApi(value = "状态" , remark = "待审核:4497471600620001  已提现:4497471600620002  已取消:4497471600620003")
	private String status = "";
	@ZapcomApi(value = "时间")
	private String create_time = "";
	
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getApply_money() {
		return apply_money;
	}
	public void setApply_money(String apply_money) {
		this.apply_money = apply_money;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
