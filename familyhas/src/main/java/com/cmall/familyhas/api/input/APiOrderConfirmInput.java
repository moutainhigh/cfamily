package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiOrderConfirmInput extends RootInput {

	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;

	@ZapcomApi(value = "用户编号")
	private String isMemberCode = "";

	@ZapcomApi(value = "买家编号", remark = "可为空，默认取当前登录人的编号", demo = "123456")
	private String buyer_code = ""; 

	@ZapcomApi(value = "订单类型", remark = "449715200003试用订单、449715200004闪购订单、449715200005普通订单、449715200010扫码购订单,449715200016 直播订单", require = 1, demo = "449715200003")
	private String order_type = "";
	
	@ZapcomApi(value = "订单来源", remark = "订单来源,可选值:449715190001(正常订单)，449715190002(android订单),449715190003(ios订单),449715190004(网站wap手机订单),微信订单(449715190006),449715190007(扫码购订单),449715190008(客服订单),449715190012(百度外卖订单)", demo = "449715190001" )
	private String order_souce = "";

	@ZapcomApi(value = "渠道编号", remark = "惠家有app：449747430001，wap商城：449747430002，微信商城：449747430003，PC：449747430004", demo = "123456")
	private String channelId = "449747430001";
	
	@ZapcomApi(value = "小程序公众号渠道来源", remark = "",demo = "")
	private String outChannelId = "";
	
	@ZapcomApi(value = "小程序直播来源房间号", remark = "",demo = "")
	private String liveRoomID = "";

	@ZapcomApi(value = "商品", require = 0)
	private List<GoodsInfoForAdd> goods = new ArrayList<GoodsInfoForAdd>();

	@ZapcomApi(value = "用户收货地址第三级地区编号", remark = "初次调用确认订单接口取默认地址上的三级编号，如无默认地址，运费为0", demo = "123456")
	private String area_code = "";

	@ZapcomApi(value = "优惠券编号（此处为list，为以后扩充留用）", remark = "优惠券编号", demo = "yhq123456")
	private List<String> coupon_codes = new ArrayList<String>();

	@ZapcomApi(value = "微公社支付金额", remark = "微公社支付金额", demo = "66.66")
	private double wgsUseMoney = 0.00;

	@ZapcomApi(value = "主播房间号", remark = "主播房间号", require = 0, demo = "46465456")
	private String roomId = "";

	@ZapcomApi(value = "主播ID", remark = "主播ID", require = 0, demo = "46579")
	private String anchorId = "";

	@ZapcomApi(value = "已使用惠豆数量", remark = "0")
	private Integer usedBeanTotal = 0;
	
	@ZapcomApi(value="已使用积分数量",remark="0")
	private Integer usedIntegralTotal = 0;
	
	@ZapcomApi(value = "已使用惠币金额", remark = "已使用惠币金额", demo = "123456.00")
	private double usedhjycoinTotal = 0.00; 
	
	@ZapcomApi(value = "已使用储值金金额", remark = "已使用储值金金额", demo = "123456.00")
	private double usedCzjTotal = 0.00; 
	
	@ZapcomApi(value = "已使用暂存款金额", remark = "已使用暂存款金额", demo = "123456.00")
	private double usedZckTotal = 0.00;
	
	@ZapcomApi(value = "支付方式", remark = "支付方式:449716200001:在线支付,449716200002:货到付款")
	private String payType = "";
	
	@ZapcomApi(value="积分标示",remark="0: 非积分商城, 1: 积分商城")
	private String integralFlag = "0";
	@ZapcomApi(value="积分商城活动ID")
	private String integralDetailId = "";
	
	@ZapcomApi(value = "收货人地址维护编号", remark = "用户维护的地址的维护编号",demo = "")
	private String buyer_address_id = "";  //网易考拉版本增加
	
	@ZapcomApi(value="是否原价购买", remark="0：不原价，1：原价")
	private String isOriginal = "0";
	
	@ZapcomApi(value="拼团标示", remark="0：非拼团，1：拼团")
	private String collageFlag = "0";
	
	@ZapcomApi(value="团编码", remark="要参与团的团编码")
	private String collageCode = "";
	

	@ZapcomApi(value="优惠券自动选择", remark="0: 不选择，1: 自动选择最优")
	private String autoSelectCoupon = "0";	
	
	@ZapcomApi(value="活动编号", remark="兑换码兑换活动")
	private String activityCode = "";
	
	@ZapcomApi(value="兑换码", remark="兑换码兑换活动")
	private String redeemCode = "";
	
	@ZapcomApi(value="订单确认是否来自购物车标识 0否 1是")
	private String ifFromShopCar = "0";
	
	@ZapcomApi(value="缤纷扫码编号", remark="")
	private String qrcode = "";
	
	@ZapcomApi(value="果树编号", remark="惠惠农场活动")
	private String treeCode = "";
	
	@ZapcomApi(value="互动活动编号", remark="互动活动编号")
	private String huDongCode = "";
	
	@ZapcomApi(value="特殊促销活动编号", remark="例：4497472600010029换购投票")
	private String eventCode = "";
	
	@ZapcomApi(value="是否橙意卡专题订单", remark="0：不是，1：是")
	private String isCyk = "0";
	
	
	public String getIsCyk() {
		return isCyk;
	}

	public void setIsCyk(String isCyk) {
		this.isCyk = isCyk;
	}

	public String getIfFromShopCar() {
		return ifFromShopCar;
	}

	public void setIfFromShopCar(String ifFromShopCar) {
		this.ifFromShopCar = ifFromShopCar;
	}
	
	public double getUsedhjycoinTotal() {
		return usedhjycoinTotal;
	}

	public void setUsedhjycoinTotal(double usedhjycoinTotal) {
		this.usedhjycoinTotal = usedhjycoinTotal;
	}

	public Integer getUsedBeanTotal() {
		return usedBeanTotal;
	}

	public void setUsedBeanTotal(Integer usedBeanTotal) {
		this.usedBeanTotal = usedBeanTotal;
	}

	public String getBuyer_code() {
		return buyer_code;
	}

	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}

	public String getOrder_souce() {
		return order_souce;
	}

	public void setOrder_souce(String order_souce) {
		this.order_souce = order_souce;
	}

	public List<GoodsInfoForAdd> getGoods() {
		return goods;
	}

	public void setGoods(List<GoodsInfoForAdd> goods) {
		this.goods = goods;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public List<String> getCoupon_codes() {
		return coupon_codes;
	}

	public void setCoupon_codes(List<String> coupon_codes) {
		this.coupon_codes = coupon_codes;
	}

	public double getWgsUseMoney() {
		return wgsUseMoney;
	}

	public void setWgsUseMoney(double wgsUseMoney) {
		this.wgsUseMoney = wgsUseMoney;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	public String getOutChannelId() {
		return outChannelId;
	}

	public void setOutChannelId(String outChannelId) {
		this.outChannelId = outChannelId;
	}

	public String getLiveRoomID() {
		return liveRoomID;
	}

	public void setLiveRoomID(String liveRoomID) {
		this.liveRoomID = liveRoomID;
	}

	/**
	 * @return the isPurchase
	 */
	public Integer getIsPurchase() {
		return isPurchase;
	}

	/**
	 * @param isPurchase
	 *            the isPurchase to set
	 */
	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}

	/**
	 * @return the isMemberCode
	 */
	public String getIsMemberCode() {
		return isMemberCode;
	}

	/**
	 * @param isMemberCode
	 *            the isMemberCode to set
	 */
	public void setIsMemberCode(String isMemberCode) {
		this.isMemberCode = isMemberCode;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getAnchorId() {
		return anchorId;
	}

	public void setAnchorId(String anchorId) {
		this.anchorId = anchorId;
	}

	public Integer getUsedIntegralTotal() {
		return usedIntegralTotal;
	}

	public void setUsedIntegralTotal(Integer usedIntegralTotal) {
		this.usedIntegralTotal = usedIntegralTotal;
	}
	
	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	public String getIntegralFlag() {
		return integralFlag;
	}

	public void setIntegralFlag(String integralFlag) {
		this.integralFlag = integralFlag;
	}

	public String getIntegralDetailId() {
		return integralDetailId;
	}

	public void setIntegralDetailId(String integralDetailId) {
		this.integralDetailId = integralDetailId;
	}
	public double getUsedCzjTotal() {
		return usedCzjTotal;
	}

	public void setUsedCzjTotal(double usedCzjTotal) {
		this.usedCzjTotal = usedCzjTotal;
	}

	public double getUsedZckTotal() {
		return usedZckTotal;
	}

	public void setUsedZckTotal(double usedZckTotal) {
		this.usedZckTotal = usedZckTotal;
	}

	public String getBuyer_address_id() {
		return buyer_address_id;
	}

	public void setBuyer_address_id(String buyer_address_id) {
		this.buyer_address_id = buyer_address_id;
	}

	public String getIsOriginal() {
		return isOriginal;
	}

	public void setIsOriginal(String isOriginal) {
		this.isOriginal = isOriginal;
	}

	public String getCollageFlag() {
		return collageFlag;
	}

	public void setCollageFlag(String collageFlag) {
		this.collageFlag = collageFlag;
	}

	public String getCollageCode() {
		return collageCode;
	}

	public void setCollageCode(String collageCode) {
		this.collageCode = collageCode;
	}

	public String getAutoSelectCoupon() {
		return autoSelectCoupon;
	}

	public void setAutoSelectCoupon(String autoSelectCoupon) {
		this.autoSelectCoupon = autoSelectCoupon;
	}
	
	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getRedeemCode() {
		return redeemCode;
	}

	public void setRedeemCode(String redeemCode) {
		this.redeemCode = redeemCode;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getTreeCode() {
		return treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getHuDongCode() {
		return huDongCode;
	}

	public void setHuDongCode(String huDongCode) {
		this.huDongCode = huDongCode;
	}

}
