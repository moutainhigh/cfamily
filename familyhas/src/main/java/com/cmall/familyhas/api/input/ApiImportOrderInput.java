package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiImportOrderInput extends RootInput {

	@ZapcomApi(value = "1.0.0", remark = "上传excel文件名(先传到文件服务器)", require = 0)
	private String upload_show = "";

	@ZapcomApi(value = "1.0.0", remark = "订单来源", require = 0)
	private String orderSource = "";

	@ZapcomApi(value = "1.0.0", remark = "订单类型", require = 0)
	private String orderType = "";
	@ZapcomApi(value = "1.0.0", remark = "订单渠道", require = 0)
	private String orderChannel = "";
	@ZapcomApi(value = "1.0.0", remark = "支付方式", require = 0)
	private String payType = "";

	public String getUpload_show() {
		return upload_show;
	}

	public void setUpload_show(String upload_show) {
		this.upload_show = upload_show;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderChannel() {
		return orderChannel;
	}

	public void setOrderChannel(String orderChannel) {
		this.orderChannel = orderChannel;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}
