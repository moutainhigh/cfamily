package com.cmall.familyhas.api.result;


import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiPersonHbResult extends RootResultWeb {
	
	@ZapcomApi(value = "用户惠币（可提现收益）")
	private String hbTotal = "";
	@ZapcomApi(value = "预估收益")
	private String yghbTotal = "";
	@ZapcomApi(value = "已提现收益")
	private String ytxhbTotal = "";
	@ZapcomApi(value = "用户姓名")
	private String name = "";
	@ZapcomApi(value = "身份证号")
	private String idcard_number = "";
	@ZapcomApi(value = "小程序id")
	private String miniprogram_id = "";
	@ZapcomApi(value = "小程序路径")
	private String miniprogram_path = "";
	@ZapcomApi(value = "0:不能体现,1:能体现")
	private String withdraw = "1";
	@ZapcomApi(value = "不能提现原因")
	private String reason = "";
	
	
	public String getWithdraw() {
		return withdraw;
	}
	public void setWithdraw(String withdraw) {
		this.withdraw = withdraw;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdcard_number() {
		return idcard_number;
	}
	public void setIdcard_number(String idcard_number) {
		this.idcard_number = idcard_number;
	}
	public String getMiniprogram_id() {
		return miniprogram_id;
	}
	public void setMiniprogram_id(String miniprogram_id) {
		this.miniprogram_id = miniprogram_id;
	}
	public String getMiniprogram_path() {
		return miniprogram_path;
	}
	public void setMiniprogram_path(String miniprogram_path) {
		this.miniprogram_path = miniprogram_path;
	}
	public String getHbTotal() {
		return hbTotal;
	}
	public void setHbTotal(String hbTotal) {
		this.hbTotal = hbTotal;
	}
	public String getYghbTotal() {
		return yghbTotal;
	}
	public void setYghbTotal(String yghbTotal) {
		this.yghbTotal = yghbTotal;
	}
	public String getYtxhbTotal() {
		return ytxhbTotal;
	}
	public void setYtxhbTotal(String ytxhbbTotal) {
		this.ytxhbTotal = ytxhbbTotal;
	}
	
	
	
	
	

}
