package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ExpressDetailsResult {
	
	@ZapcomApi(value="订单号",remark="物流单号",demo="DD1234567898")
	private String  order_code = "";
	
	@ZapcomApi(value="物流公司编号",remark="物流公司编号",demo="SF")
	private String  logisticse_code = "";
	
	@ZapcomApi(value="运单号",remark="快递单号",demo="SF123456789")
	private String  waybill = "";
	
	@ZapcomApi(value="快递流转信息",remark="快递流转信息",demo="北京站已揽收")
	private String  context = "";
	
	
	@ZapcomApi(value="快递流转时间",remark="快递流转时间",demo="2019-09-10 10:10:20")
	private String  time = "";
	
	
	@ZapcomApi(value="状态",remark="状态",demo="在途")
	private String  status = "";
	
	@ZapcomApi(value="城市编码",remark="城市编码",demo="0311")
	private String  areaCode = "";
	
	@ZapcomApi(value="城市名称",remark="城市名称",demo="石家庄")
	private String  areaName = "";

	public String getOrder_code() {
		return order_code;
	}

	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	public String getLogisticse_code() {
		return logisticse_code;
	}

	public void setLogisticse_code(String logisticse_code) {
		this.logisticse_code = logisticse_code;
	}

	public String getWaybill() {
		return waybill;
	}

	public void setWaybill(String waybill) {
		this.waybill = waybill;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	
}
