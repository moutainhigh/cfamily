package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 跨境通快递详情
 * @author pangjh
 *
 */
public class KJTExpressDetail {
	
	@ZapcomApi(value="运单编号")
	private String waybill;
	
	@ZapcomApi(value="快递公司代码")
	private String logisticse_code;
	
	@ZapcomApi(value="运单轨迹")
	private String context;
	
	@ZapcomApi(value="更新时间")
	private String time;

	/**
	 * 获取运单编号
	 * @return
	 */
	public String getWaybill() {
		return waybill;
	}

	/**
	 * 设置运单编号
	 * @param waybill
	 */
	public void setWaybill(String waybill) {
		this.waybill = waybill;
	}

	/**
	 * 获取快递公司代码
	 * @return
	 */
	public String getLogisticse_code() {
		return logisticse_code;
	}

	/**
	 * 设置快递公司代码
	 * @param logisticse_code
	 */
	public void setLogisticse_code(String logisticse_code) {
		this.logisticse_code = logisticse_code;
	}

	/**
	 * 获取运单轨迹
	 * @return
	 */
	public String getContext() {
		return context;
	}

	/**
	 * 设置运单轨迹
	 * @param context
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * 获取轨迹时间
	 * @return
	 */
	public String getTime() {
		return time;
	}

	/**
	 * 设置轨迹时间
	 * @param time
	 */
	public void setTime(String time) {
		this.time = time;
	}
	

}
