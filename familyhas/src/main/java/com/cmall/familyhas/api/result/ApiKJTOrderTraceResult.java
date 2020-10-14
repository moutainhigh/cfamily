package com.cmall.familyhas.api.result;

import java.util.List;

import com.cmall.familyhas.api.model.KJTOrderTraceInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 跨境通订单轨迹响应结果
 * @author pangjh
 *
 */
public class ApiKJTOrderTraceResult extends RootResultWeb {
	
	@ZapcomApi(value="运单信息集合")
	private List<KJTOrderTraceInfo> orderTraceInfos;
	
	@ZapcomApi(value="物流信息温馨提示")
	private String logisticsTips="";

	/**
	 * 获取运单详情信息
	 * @return
	 */
	public List<KJTOrderTraceInfo> getOrderTraceInfos() {
		return orderTraceInfos;
	}

	/**
	 * 设置运单详情信息
	 * @param orderTraceInfos
	 */
	public void setOrderTraceInfos(List<KJTOrderTraceInfo> orderTraceInfos) {
		this.orderTraceInfos = orderTraceInfos;
	}

	public String getLogisticsTips() {
		return logisticsTips;
	}

	public void setLogisticsTips(String logisticsTips) {
		this.logisticsTips = logisticsTips;
	}
	
	
	
}
