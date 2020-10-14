package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ScrollMessage {

	@ZapcomApi(value = "商品编号")
	private String productCode = "";
	@ZapcomApi(value = "商品图片地址")
	private String pic = "";
	@ZapcomApi(value = "地址")
	private String address = "";
	@ZapcomApi(value="昵称")
	private String nickName = "";
	@ZapcomApi(value = "具体消息", demo="参团成功；发起拼团；下单成功；")
	private String message = "";
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
