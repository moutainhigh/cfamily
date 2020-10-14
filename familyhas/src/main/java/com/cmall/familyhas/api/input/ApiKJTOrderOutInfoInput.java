package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 提供给跨境通（KJT）,判断订单是否出关成功接口参数输入
 * @author pangjh
 *
 */
public class ApiKJTOrderOutInfoInput extends RootInput {
	
	@ZapcomApi(value="商家订单编号")
	private String MerchantOrderID = "";
	
	@ZapcomApi(value="出关状态" , remark="-1表示出关失败，1表示出关成功")
	private String Status = "";
	
	@ZapcomApi(value="订单物流运输公司比那好")
	private String ShipTypeID = "";
	
	@ZapcomApi(value="订单物流编号")
	private String TrackingNumber = "";
	
	@ZapcomApi(value="出库时间")
	private String CommitTime = "";
	
	@ZapcomApi(value="出关失败的原因")
	private String Message = "";

	/**
	 * 获取商家订单编号
	 * @return 商家订单编号
	 */
	public String getMerchantOrderID() {
		return MerchantOrderID;
	}

	/**
	 * 设置商家订单编号
	 * @param merchantOrderID
	 * 		商店订单编号
	 */
	public void setMerchantOrderID(String merchantOrderID) {
		MerchantOrderID = merchantOrderID;
	}

	/**
	 * 获取出关状态
	 * @return Status 出关状态
	 */
	public String getStatus() {
		return Status;
	}

	/**
	 * 设置出关状态
	 * @param status
	 * 		出关状态
	 */
	public void setStatus(String status) {
		Status = status;
	}

	/**
	 * 获取订单物流运输公司编号
	 * @return ShipTypeID
	 * 		订单物流运输公司编号
	 */
	public String getShipTypeID() {
		return ShipTypeID;
	}
	
	/**
	 * 设置订单物流运输公司编号
	 * @param shipTypeID
	 * 		订单物流运输公司编号
	 */
	public void setShipTypeID(String shipTypeID) {
		ShipTypeID = shipTypeID;
	}

	/**
	 * 获取订单物流编号
	 * @return TrackingNumber
	 * 		订单物流编号
	 */
	public String getTrackingNumber() {
		return TrackingNumber;
	}

	/**
	 * 设置订单物流编号
	 * @param trackingNumber
	 * 		订单物流编号
	 */
	public void setTrackingNumber(String trackingNumber) {
		TrackingNumber = trackingNumber;
	}

	/**
	 * 获取出库时间
	 * @return CommitTime
	 * 		出库时间
	 */
	public String getCommitTime() {
		return CommitTime;
	}

	/**
	 * 设置出库时间
	 * @param commitTime
	 * 		出库时间
	 */
	public void setCommitTime(String commitTime) {
		CommitTime = commitTime;
	}

	/**
	 * 获取订单出关失败原因 
	 * @return Message
	 */
	public String getMessage() {
		return Message;
	}

	/**
	 * 设置订单出关失败原因
	 * @param message
	 * 		订单失败原因
	 */
	public void setMessage(String message) {
		Message = message;
	}
	

}
