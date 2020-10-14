package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiCollageTeamInfoResult extends RootResult {
	
	@ZapcomApi(value="商品编码")
	private String productCode = "";
	@ZapcomApi(value="商品名称")
	private String productName = "";
	@ZapcomApi(value="活动库存")
	private long salesNum = 0;
	@ZapcomApi(value="市场价")
	private String marketPrice = "";
	@ZapcomApi(value="活动价",remark="拼团详情展示价格为当前用户购买商品价格")
	private String activityPrice = "";
	@ZapcomApi(value="分享价",remark="邀请人参与时，应是该商品参与拼团的最低SKU价格")
	private String picUrl = "";
	@ZapcomApi(value="商品图片",remark="优先显示广告图，无广告图，显示商品原图")
	private String productImg = "";
	@ZapcomApi(value="结束时间")
	private String endTime;
	@ZapcomApi(value="需要多少人成团",remark="返回为具体数字")
	private String collagePerson;
	@ZapcomApi(value="拼团人信息",remark="第一位为团长，已参与拼团人数通过集合个数来取")
	private List<ApiCollagePersonInfo> personInfo = new ArrayList<ApiCollagePersonInfo>();
	@ZapcomApi(value="拼团状态", remark="449748300001: 拼团中，449748300002：拼团成功，449748300003： 拼团失败")
	private String collageStatus = "";
	@ZapcomApi(value="是否重新开团",remark="0:未重新开团，1：参团的人已满，重新开团")
	private String reCollage = "0";
	@ZapcomApi(value="广告语")
	private String description;
	@ZapcomApi(value="拼团类型",remark="拼团类型：（4497473400050001：普通团，4497473400050002：邀新团）")
	private String collageType = "4497473400050001";
	@ZapcomApi(value="校验当前用户是否满足拼团条件",remark="1：满足，0：不满足")
	private String supportCollage = "1";
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public long getSalesNum() {
		return salesNum;
	}
	public void setSalesNum(long salesNum) {
		this.salesNum = salesNum;
	}
	
	public String getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	
	public String getActivityPrice() {
		return activityPrice;
	}
	public void setActivityPrice(String activityPrice) {
		this.activityPrice = activityPrice;
	}
	
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getCollagePerson() {
		return collagePerson;
	}
	public void setCollagePerson(String collagePerson) {
		this.collagePerson = collagePerson;
	}
	
	public List<ApiCollagePersonInfo> getPersonInfo() {
		return personInfo;
	}
	public void setPersonInfo(List<ApiCollagePersonInfo> personInfo) {
		this.personInfo = personInfo;
	}
	
	public String getCollageStatus() {
		return collageStatus;
	}
	public void setCollageStatus(String collageStatus) {
		this.collageStatus = collageStatus;
	}
	
	public String getReCollage() {
		return reCollage;
	}
	public void setReCollage(String reCollage) {
		this.reCollage = reCollage;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCollageType() {
		return collageType;
	}
	public void setCollageType(String collageType) {
		this.collageType = collageType;
	}
	public String getSupportCollage() {
		return supportCollage;
	}
	public void setSupportCollage(String supportCollage) {
		this.supportCollage = supportCollage;
	}
	
}
