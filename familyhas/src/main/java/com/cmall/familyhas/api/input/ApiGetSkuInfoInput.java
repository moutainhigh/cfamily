package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetSkuInfoInput extends RootInput {

	@ZapcomApi(value="商品编号",require=1)
	private String productCode="";
	
	@ZapcomApi(value="图片宽度",demo="600")
	private Integer picWidth = new Integer(0);

	@ZapcomApi(value = "用户类型", remark="用户类型" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";

	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	@ZapcomApi(value = "渠道编号", remark = "惠家有app：449747430001，wap商城：449747430002，微信商城：449747430003，惠家有PC订单:449747430004")
	private String channelId = "449747430001";
	
	@ZapcomApi(value="积分活动ID",remark="商品参与积分活动标示")
	private String integralDetailId = "";
	
	@ZapcomApi(value="app版本",remark="app版本")
	private String app_version = "";
	
	@ZapcomApi(value = "是否显示分销券后加", remark = "默认值为0，显示传递1")
	private String fxFlag = "0";
	
	@ZapcomApi(value = "直播房间编号", remark = "为了统计直播房间商品点击次数")
	private String liveRoomId = "";
	
	@ZapcomApi(value = "小程序推广赚分享参数唯一编号", remark = "推广赚,用于区分分销")
	private String shortCode = "";
	
	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	
	public String getLiveRoomId() {
		return liveRoomId;
	}

	public void setLiveRoomId(String liveRoomId) {
		this.liveRoomId = liveRoomId;
	}

	public String getFxFlag() {
		return fxFlag;
	}

	public void setFxFlag(String fxFlag) {
		this.fxFlag = fxFlag;
	}

	public String getApp_version() {
		return app_version;
	}

	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(Integer picWidth) {
		this.picWidth = picWidth;
	}

	public String getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}

	/**
	 * @return the isPurchase
	 */
	public Integer getIsPurchase() {
		return isPurchase;
	}

	/**
	 * @param isPurchase the isPurchase to set
	 */
	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getIntegralDetailId() {
		return integralDetailId;
	}

	public void setIntegralDetailId(String integralDetailId) {
		this.integralDetailId = integralDetailId;
	}
}
