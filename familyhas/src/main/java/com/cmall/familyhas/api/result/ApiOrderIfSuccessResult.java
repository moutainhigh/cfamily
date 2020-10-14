package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.ProductInfoForOrderSuccess;
import com.cmall.familyhas.api.result.ld.ShowLinkForLDMsgPaySuccess;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiOrderIfSuccessResult extends RootResultWeb{
	
	@ZapcomApi(value="小订单相关信息")
	private List<OrderInfoIfSuccess> SplitOrderList = new ArrayList<OrderInfoIfSuccess>();
	@ZapcomApi(value="支付状态",remark="在线支付成功:00,货到付款成功:01, 失败:11")
	private String orderPayStatue = "";
	@ZapcomApi(value="下单时间")
	private String createDate = "";
	@ZapcomApi(value="提示信息",remark="提示: 商品由从同仓库发货, 会分成多个订单配送!")
	private String splitOrderHint = "";
	@ZapcomApi(value="时间提示信息",remark="提示: 订单将在**时间后取消")
	private String dateMessage = "";
	@ZapcomApi(value="提交成功的安全提示")
	private String safetyHint = "";
	@ZapcomApi(value="应付金额")
	private String dueMoney = "";
	@ZapcomApi(value="默认支付方式",remark="449746280003:支付宝支付,449746280005:微信支付,449746280009:微公社支付")
	private String default_Pay_type = "";
	@ZapcomApi(value="支付类型",demo="在线支付：449716200001      支付宝支付:449746280003,  微信支付:449746280005",remark="在线支付：449716200001      支付宝支付:449746280003,  微信支付:449746280005")
	private List<String> paymentTypeAll = new ArrayList<String>();
	@ZapcomApi(value="退换货提示信息")
	private List<String> tips = new ArrayList<String>();
	@ZapcomApi(value="订单支付成功后,送给用户的条件触发优惠券.分为下单满X元送Y元券和用户首单券两种")
	private CouponInfo coupon;
	@ZapcomApi(value="拼团编码")
	private String collageCode = "";
	@ZapcomApi(value="拼团类型",remark="拼团类型：（4497473400050001：普通团，4497473400050002：邀新团）")
	private String collageType = "4497473400050001";
	@ZapcomApi(value="是否橙意卡商品",demo = "0不是，1是",remark="如果是橙意卡商品则购买时需要跳转到特定页面")
	private int plusFlag = 0;
	@ZapcomApi(value="广告信息(旧版)")
	public AdvertisementInfo adverInfo = new AdvertisementInfo();
	@ZapcomApi(value="广告信息(新版)")
	public AdvertInfo advert = new AdvertInfo();
	@ZapcomApi(value="是否参与特殊活动",demo="0不是，1是",remark="暂时仅用于投票换购活动")
	public int specialEventFlag = 0;
	
	@ZapcomApi(value="小程序悬浮图标配置")
	private TopGuide topGuide = new TopGuide();
	
	@ZapcomApi(value="LD短信支付成功，广告下tab页配置内容，目前微信商城专用")
	private List<ShowLinkForLDMsgPaySuccess> showLink = new ArrayList<ShowLinkForLDMsgPaySuccess>();
	
	@ZapcomApi(value="商品信息")
	private List<ProductInfoForOrderSuccess> productList = new ArrayList<ProductInfoForOrderSuccess>();
	
	public List<ShowLinkForLDMsgPaySuccess> getShowLink() {
		return showLink;
	}
	public void setShowLink(List<ShowLinkForLDMsgPaySuccess> showLink) {
		this.showLink = showLink;
	}
	public AdvertInfo getAdvert() {
		return advert;
	}
	public void setAdvert(AdvertInfo advert) {
		this.advert = advert;
	}
	public AdvertisementInfo getAdverInfo() {
		return adverInfo;
	}
	public void setAdverInfo(AdvertisementInfo adverInfo) {
		this.adverInfo = adverInfo;
	}
	public List<String> getPaymentTypeAll() {
		return paymentTypeAll;
	}

	public void setPaymentTypeAll(List<String> paymentTypeAll) {
		this.paymentTypeAll = paymentTypeAll;
	}

	public String getDateMessage() {
		return dateMessage;
	}

	public void setDateMessage(String dateMessage) {
		this.dateMessage = dateMessage;
	}

	public TopGuide getTopGuide() {
		return topGuide;
	}
	public void setTopGuide(TopGuide topGuide) {
		this.topGuide = topGuide;
	}
	public String getDueMoney() {
		return dueMoney;
	}
	
	public void setDueMoney(String dueMoney) {
		this.dueMoney = dueMoney;
	}
	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getOrderPayStatue() {
		return orderPayStatue;
	}

	public void setOrderPayStatue(String orderPayStatue) {
		this.orderPayStatue = orderPayStatue;
	}

	public List<OrderInfoIfSuccess> getSplitOrderList() {
		return SplitOrderList;
	}

	public void setSplitOrderList(List<OrderInfoIfSuccess> splitOrderList) {
		SplitOrderList = splitOrderList;
	}

	public String getSplitOrderHint() {
		return splitOrderHint;
	}

	public void setSplitOrderHint(String splitOrderHint) {
		this.splitOrderHint = splitOrderHint;
	}

	public String getSafetyHint() {
		return safetyHint;
	}

	public void setSafetyHint(String safetyHint) {
		this.safetyHint = safetyHint;
	}

	public String getDefault_Pay_type() {
		return default_Pay_type;
	}

	public void setDefault_Pay_type(String default_Pay_type) {
		this.default_Pay_type = default_Pay_type;
	}

	public List<String> getTips() {
		return tips;
	}

	public void setTips(List<String> tips) {
		this.tips = tips;
	}
	public CouponInfo getCoupon() {
		return coupon;
	}

	public void setCoupon(CouponInfo coupon) {
		this.coupon = coupon;
	}

	public String getCollageCode() {
		return collageCode;
	}

	public void setCollageCode(String collageCode) {
		this.collageCode = collageCode;
	}
	
	public int getPlusFlag() {
		return plusFlag;
	}
	public void setPlusFlag(int plusFlag) {
		this.plusFlag = plusFlag;
	}

	public int getSpecialEventFlag() {
		return specialEventFlag;
	}
	public void setSpecialEventFlag(int specialEventFlag) {
		this.specialEventFlag = specialEventFlag;
	}

	public String getCollageType() {
		return collageType;
	}
	public void setCollageType(String collageType) {
		this.collageType = collageType;
	}

	public List<ProductInfoForOrderSuccess> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductInfoForOrderSuccess> productList) {
		this.productList = productList;
	}

	public static class CouponInfo {
		
		private String trigger;
		private Integer amount;
		private String comment;
		public CouponInfo() {}
		
		public String getTrigger() {
			return trigger;
		}
		public void setTrigger(String trigger) {
			this.trigger = trigger;
		}
		
		public Integer getAmount() {
			return amount;
		}

		public void setAmount(Integer amount) {
			this.amount = amount;
		}

		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
	}
}
