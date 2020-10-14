package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 我的积分
 * @author dyc
 * date: 2014-11-20
 * @version1.0
 */
public class MyPoints {

	@ZapcomApi(value = "积分创建时间", remark = "积分创建时间", demo = "2012-06-17")
	private String etr_date="";
	
	@ZapcomApi(value = "获得积分", remark = "获得积分", demo = "3")
	private String get_point="";
	
	@ZapcomApi(value = "消耗积分", remark = "消耗积分", demo = "5")
	private String consume_point="";
	
	@ZapcomApi(value = "详细说明", remark = "详细说明")
	private String remark="";

	public String getEtr_date() {
		return etr_date;
	}

	public void setEtr_date(String etr_date) {
		this.etr_date = etr_date;
	}

	public String getGet_point() {
		return get_point;
	}

	public void setGet_point(String get_point) {
		this.get_point = get_point;
	}

	public String getConsume_point() {
		return consume_point;
	}

	public void setConsume_point(String consume_point) {
		this.consume_point = consume_point;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
