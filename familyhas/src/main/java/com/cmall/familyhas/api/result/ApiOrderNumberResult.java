package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiOrderNumberResult extends RootResultWeb {
	@ZapcomApi(value = "买家编号")
	private List<ApiOrderStateNumberResult> list = new ArrayList<ApiOrderStateNumberResult>();

	@ZapcomApi(value = "可用优惠券数量")
	private int couponCount = 0;

	@ZapcomApi(value = "个人优惠券到期提醒", remark = "X张优惠券即将过期")
	private String couponPrompt = "";

	@ZapcomApi(value = "个人优惠券新券提醒", remark = "0:无新券 1:有新券")
	private String newCoupon = "0";

	@ZapcomApi(value = "微公社余额", remark = "返利余额：20.02元")
	private String microCommuneBalance = "";
	
	@ZapcomApi(value = "橙意会员卡信息")
	private PlusInfo plusInfo = new PlusInfo();
	
	@ZapcomApi(value = "橙意会员卡控制标识", remark = "0:不展示 1:展示")
	private int plusShowFlag = 0;
	
	/**
	 * 2016-12-06 zhy
	 */
	@ZapcomApi(value = "用户中心是否显示微公社", remark = "0 不显示 1显示 ")
	private String microCommuneOpenFlag = "0";
	@ZapcomApi(value = "我的惠豆", remark = "0")
	private String myBeans;
	@ZapcomApi(value = "用户中心是否显示我的惠豆", remark = "0 不显示 1 显示")
	private String beanUsableFlag = "0";
	@ZapcomApi(value = "其他订单数量")
	private String otherOrderNumber = "0";
	@ZapcomApi(value = "积分数量")
	private String integralTotal = "";
	@ZapcomApi(value = "快过期积分数量")
	private String expireIntegral = "";
	@ZapcomApi(value = "储值金数量")
	private String czjTotal = "";
	@ZapcomApi(value = "暂存款数量")
	private String zckTotal = "";
	@ZapcomApi(value = "用户惠币（可提现收益）")
	private String hbTotal = "";
	@ZapcomApi(value = "客户等级")
	private String custLvl = "";
	@ZapcomApi(value = "分销收益")
	private String fxAmount = "";
	
	
	
	public String getHbTotal() {
		return hbTotal;
	}
	public void setHbTotal(String hbTotal) {
		this.hbTotal = hbTotal;
	}
	public String getFxAmount() {
		return fxAmount;
	}
	public void setFxAmount(String fxAmount) {
		this.fxAmount = fxAmount;
	}
	@ZapcomApi(value = "分销等级 4497484600010001:特邀 4497484600010002:粉丝")
	private String levelCode = "";
	
	
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}

	@ZapcomApi(value = "橙意会员卡信息结构")
	public static class PlusInfo {
		@ZapcomApi(value = "是否橙意卡会员", remark = "0 不是、 1 是")
		private int stat = 0;
		@ZapcomApi(value = "橙意卡名称", remark = "橙意卡")
		private String name = "";
		@ZapcomApi(value = "过期时间")
		private String expireDate = "";
		@ZapcomApi(value = "商品编号")
		private String productCode = "";
		@ZapcomApi(value = "SKU编号")
		private String skuCode = "";
		public int getStat() {
			return stat;
		}
		public void setStat(int stat) {
			this.stat = stat;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getExpireDate() {
			return expireDate;
		}
		public void setExpireDate(String expireDate) {
			this.expireDate = expireDate;
		}
		public String getProductCode() {
			return productCode;
		}
		public void setProductCode(String productCode) {
			this.productCode = productCode;
		}
		public String getSkuCode() {
			return skuCode;
		}
		public void setSkuCode(String skuCode) {
			this.skuCode = skuCode;
		}
	}

	public String getExpireIntegral() {
		return expireIntegral;
	}

	public void setExpireIntegral(String expireIntegral) {
		this.expireIntegral = expireIntegral;
	}

	public String getCustLvl() {
		return custLvl;
	}

	public void setCustLvl(String custLvl) {
		this.custLvl = custLvl;
	}

	public String getMicroCommuneOpenFlag() {
		return microCommuneOpenFlag;
	}

	public void setMicroCommuneOpenFlag(String microCommuneOpenFlag) {
		this.microCommuneOpenFlag = microCommuneOpenFlag;
	}

	public String getBeanUsableFlag() {
		return beanUsableFlag;
	}

	public void setBeanUsableFlag(String beanUsableFlag) {
		this.beanUsableFlag = beanUsableFlag;
	}

	public List<ApiOrderStateNumberResult> getList() {
		return list;
	}

	public void setList(List<ApiOrderStateNumberResult> list) {
		this.list = list;
	}

	public int getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}

	public String getMicroCommuneBalance() {
		return microCommuneBalance;
	}

	public void setMicroCommuneBalance(String microCommuneBalance) {
		this.microCommuneBalance = microCommuneBalance;
	}
	
	public String getOtherOrderNumber() {
		return otherOrderNumber;
	}

	public void setOtherOrderNumber(String otherOrderNumber) {
		this.otherOrderNumber = otherOrderNumber;
	}

	public String getCouponPrompt() {
		return couponPrompt;
	}

	public void setCouponPrompt(String couponPrompt) {
		this.couponPrompt = couponPrompt;
	}

	public String getNewCoupon() {
		return newCoupon;
	}

	public void setNewCoupon(String newCoupon) {
		this.newCoupon = newCoupon;
	}

	public String getMyBeans() {
		return myBeans;
	}

	public void setMyBeans(String myBeans) {
		this.myBeans = myBeans;
	}

	public PlusInfo getPlusInfo() {
		return plusInfo;
	}

	public void setPlusInfo(PlusInfo plusInfo) {
		this.plusInfo = plusInfo;
	}

	public int getPlusShowFlag() {
		return plusShowFlag;
	}

	public void setPlusShowFlag(int plusShowFlag) {
		this.plusShowFlag = plusShowFlag;
	}

	public String getIntegralTotal() {
		return integralTotal;
	}

	public void setIntegralTotal(String integralTotal) {
		this.integralTotal = integralTotal;
	}

	public String getCzjTotal() {
		return czjTotal;
	}

	public void setCzjTotal(String czjTotal) {
		this.czjTotal = czjTotal;
	}

	public String getZckTotal() {
		return zckTotal;
	}

	public void setZckTotal(String zckTotal) {
		this.zckTotal = zckTotal;
	}

}
