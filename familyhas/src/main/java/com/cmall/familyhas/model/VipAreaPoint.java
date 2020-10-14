package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class VipAreaPoint {
	@ZapcomApi(value="信息唯一标示")
	private String infoId;
	@ZapcomApi(value="商品编号")
	private String productCode;
	@ZapcomApi(value="标题")
	private String title;
	@ZapcomApi(value="图片")
	private String productImg;
	@ZapcomApi(value="积分")
	private String jfCost;
	@ZapcomApi(value="金额")
	private String charge;
	@ZapcomApi(value="库存数量")
	private int allowCount;
	@ZapcomApi(value="是否抢光")
	private boolean isLoot;
	@ZapcomApi(value="是否下架")
	private boolean isDown;
	
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getProductImg() {
		return productImg;
	}
	public void setProductImg(String productImg) {
		this.productImg = productImg;
	}
	
	public String getJfCost() {
		return jfCost;
	}
	public void setJfCost(String jfCost) {
		this.jfCost = jfCost;
	}
	
	public String getCharge() {
		return charge;
	}
	public void setCharge(String charge) {
		this.charge = charge;
	}
	
	public int getAllowCount() {
		return allowCount;
	}
	public void setAllowCount(int allowCount) {
		this.allowCount = allowCount;
	}
	
	public boolean isLoot() {
		return isLoot;
	}
	public void setLoot(boolean isLoot) {
		this.isLoot = isLoot;
	}
	
	public boolean isDown() {
		return isDown;
	}
	public void setDown(boolean isDown) {
		this.isDown = isDown;
	}
}
