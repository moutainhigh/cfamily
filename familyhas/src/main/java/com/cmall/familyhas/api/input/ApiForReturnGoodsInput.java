package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiForReturnGoodsInput extends RootInput {

	@ZapcomApi(value="订单号",remark="以DD开头的订单号",require=1,demo="DD32246104")
	private String orderCode ="";
	
	@ZapcomApi(value="sku编码",require=1,demo="8019575972")
	private String skuCode ="";
	
	@ZapcomApi(value="售后类型",require=1,demo="4497477800030003",verify={ "in=4497477800030001,4497477800030003" },remark = "4497477800030001-退货退款  4497477800030002-仅退款  4497477800030003-换货")
	private String reimburseType="";
	
	@ZapcomApi(value="售后原因",require=1,demo="",remark="若为拒收，请输入固定值 rejection")
	private String reimburseReason="";
	
	@ZapcomApi(value="退款金额",require=1,demo="0")
	private String reimburseMoney="";
	
	@ZapcomApi(value="是否收到货",remark="4497476900040001收到   4497476900040002 未收到",require=1,verify={"in=4497476900040001,4497476900040002"},demo="1")
	private String isGetProduce="";
	
	@ZapcomApi(value="退换货数量",remark="退换货数量应小于等于购买数量",require=1,demo="3")
	private int produceNum=0;
	
	@ZapcomApi(value="备注",require=0,demo="买到假货了")
	private String reimburseTips="";
	
	@ZapcomApi(value="退换货图片",remark="",require=0,demo="")
	private List<String> certificatePic=new ArrayList<String>();
	
	@ZapcomApi(value="订单序号",remark="LD订单退货需要提供订单序号",require=0,demo="1")
	private String orderSeq = "0";

	
	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getReimburseType() {
		return reimburseType;
	}

	public void setReimburseType(String reimburseType) {
		this.reimburseType = reimburseType;
	}

	public String getReimburseReason() {
		return reimburseReason;
	}

	public void setReimburseReason(String reimburseReason) {
		this.reimburseReason = reimburseReason;
	}

	public String getReimburseMoney() {
		return reimburseMoney;
	}

	public void setReimburseMoney(String reimburseMoney) {
		this.reimburseMoney = reimburseMoney;
	}

	public String getIsGetProduce() {
		return isGetProduce;
	}

	public void setIsGetProduce(String isGetProduce) {
		this.isGetProduce = isGetProduce;
	}

	public int getProduceNum() {
		return produceNum;
	}

	public void setProduceNum(int produceNum) {
		this.produceNum = produceNum;
	}

	public String getReimburseTips() {
		return reimburseTips;
	}

	public void setReimburseTips(String reimburseTips) {
		this.reimburseTips = reimburseTips;
	}

	public List<String> getCertificatePic() {
		return certificatePic;
	}

	public void setCertificatePic(List<String> certificatePic) {
		this.certificatePic = certificatePic;
	}
	
}
