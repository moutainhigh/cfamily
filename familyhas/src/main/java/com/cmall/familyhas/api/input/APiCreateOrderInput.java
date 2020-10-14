package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.model.BillInfo;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.srnpr.xmasorder.model.DistributionInfoModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiCreateOrderInput extends RootInput {

	@ZapcomApi(value = "买家编号", remark = "可为空，默认取当前登录人的编号", demo = "123456")
	private String buyer_code = "";
	

	@ZapcomApi(value = "订单类型", remark = "449715200003试用订单、449715200004闪购订单、449715200005普通订单,449715200007内购订单、449715200010扫码购订单、449715200013拼好货,449715200014一元购,449715200015百度外卖订单,449715200024 积分商城订单", require=1, demo = "449715200003" )
	private String order_type = "";
	
	
	@ZapcomApi(value = "订单来源", remark = "订单来源,可选值:449715190001(正常订单)，449715190002(android订单),449715190003(ios订单),449715190004(网站wap手机订单),微信订单(449715190006),449715190007(扫码购订单),449715190008(客服订单),449715190012(百度外卖订单),449715190053(惠家有通路短信召回订单),449715190054(集团通路短信召回订单)", demo = "449715190001" )
	private String order_souce = "";
	
	@ZapcomApi(value = "订单页面来源", remark = "449748740007	赠送雨滴页下单\n" + 
			"449748740006	逛会场下单\n" + 
			"449748740005	看商品下单\n" + 
			"449748740004	为您推荐\n" + 
			"449748740003	节目表\n" + 
			"449748740002	福利大转盘\n" + 
			"449748740001	积分打卡\\n" + 
			"449748740008   LD短信支付成功页节目表"
			+ "449748740009  LD短信支付成功猜你喜欢", demo = "449715190001" )
	private String orderPageSouce = "";
	
	@ZapcomApi(value = "渠道编号", remark = "惠家有app：449747430001，wap商城：449747430002，微信商城：449747430003,惠家有PC订单:449747430004,百度外卖:449747430005", demo = "123456")
	private String channelId = "449747430001";
	
	@ZapcomApi(value = "小程序公众号渠道来源", remark = "",demo = "")
	private String outChannelId = "";
	
	@ZapcomApi(value = "小程序直播来源房间号", remark = "",demo = "")
	private String liveRoomID = "";
	
	@ZapcomApi(value = "商品列表", remark = "不可为空",require=0, demo = "")
	private List<GoodsInfoForAdd> goods = new ArrayList<GoodsInfoForAdd>();
	
	@ZapcomApi(value = "收货人姓名", remark = "收货人姓名",require=1, demo = "")
	private String buyer_name = "";
	
	@ZapcomApi(value = "收货人地址维护编号", remark = "用户维护的地址的维护编号",demo = "")
	private String buyer_address_id = "";
	
	@ZapcomApi(value = "收货人地址编号", remark = "收货人地址所在地区选择的第三级编号",require=1, demo = "")
	private String buyer_address_code = "";
	
	@ZapcomApi(value = "收货人地址", remark = "收货人地址",require=1, demo = "")
	private String buyer_address = "";
	
	@ZapcomApi(value = "收货人手机号", remark = "手机号", demo = "13333100204", require = 1, verify = {"base=mobile" })
	private String buyer_mobile = "";
	
	
	@ZapcomApi(value = "支付方式", remark = "支付方式:449716200001:在线支付(449746280003:支付宝支付,449746280005:微信支付,449746280009:微公社支付),449716200002:货到付款,449716200005:百度外卖代收货款",require=1, demo = "449716200001:在线支付,449716200002:货到付款")
	private String pay_type = "";
	
	@ZapcomApi(value = "微信支付时客户端IP地址", remark = "微信支付时客户端IP地址", demo = "8.8.8.8")
	private String pay_ip = "";
	
	@ZapcomApi(value = "应付款", remark = "应付款",require=1, demo = "8888.88")
	private double check_pay_money = 0.00;
	
	@ZapcomApi(value = "发票信息", remark = "发票信息",require=1, demo = "")
	private BillInfo billInfo = new BillInfo();
	
	@ZapcomApi(value = "app版本信息", remark = "app版本信息",require=1, demo = "1.0.0")
	private String app_vision = "";
	
	@ZapcomApi(value = "手机型号", remark = "手机型号", demo = "mi3")
	private String model = "";

	@ZapcomApi(value = "设备的唯一标识", remark = "设备的唯一标识", demo = "advertisingIdentifier")
	private String uniqid = "";

	@ZapcomApi(value = "mac地址", remark = "mac地址", demo = "mac")
	private String mac = "";

	@ZapcomApi(value = "手机操作系统", remark = "手机操作系统", demo = "ios")
	private String os = "";

	@ZapcomApi(value = "手机操作系统及版本", remark = "手机操作系统及版本", demo = "os_info")
	private String os_info = "";

	@ZapcomApi(value = "渠道号", remark = "渠道号", demo = "9100701")
	private String from = "";

	@ZapcomApi(value = "屏幕分辨率", remark = "屏幕分辨率", demo = "800x480")
	private String screen = "";

	@ZapcomApi(value = "运营商SIM卡国家码和网络码", remark = "运营商SIM卡国家码和网络码", demo = "46001")
	private String op = "";

	@ZapcomApi(value = "产品名称", remark = "产品名称", demo = "56mv_phone")
	private String product = "";

	@ZapcomApi(value = "网络状态", remark = "网络状态", demo = "wifi")
	private String net_type = "";
	
	@ZapcomApi(value ="订单备注",remark = "订单备注")
	private String remark = "";
	
	@ZapcomApi(value ="优惠券编号（此处为list，为以后扩充留用）", remark = "优惠券编号", demo = "yhq123456")
	private List<String> coupon_codes = new ArrayList<String>();
	
	@ZapcomApi(value = "已使用惠币金额", remark = "已使用惠币金额", demo = "123456.00")
	private double usedhjycoinTotal = 0.00; 
	
	@ZapcomApi(value = "微公社支付金额", remark = "微公社支付金额", demo = "66.66")
	private double wgsUseMoney = 0.00;
	
	@ZapcomApi(value = "储值金使用金额", remark = "储值金使用金额", demo = "123456.00")
	private double czj_money = 0.00; 
	
	@ZapcomApi(value = "暂存款使用金额", remark = "暂存款使用金额", demo = "123456.00")
	private double zck_money = 0.00;

	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	@ZapcomApi(value = "用户编号", remark = "默认为空   传递值为MI开头的那串数字")
	private String isMemberCode="";
	
	@ZapcomApi(value = "一元购订单号" ,remark = "一元购订单所用期号")
	private String yygOrderNo = "";
	
	@ZapcomApi(value="一元购加密认证")
	private String yygMac = "";
	
	@ZapcomApi(value="token加密验证")
	private String encryptToken = "";
	
	private String yygPayMoney = "";
	
	
	@ZapcomApi(value="主播房间号",remark="主播房间号",require=0,demo="46465456")
	private String roomId="";
	
	@ZapcomApi(value="主播ID",remark="主播ID",require=0,demo="46579")
	private String anchorId="";
	
	@ZapcomApi(value="已使用惠豆数量",remark="0")
	private Integer usedBeanTotal = 0;
	
	@ZapcomApi(value="已使用积分数量",remark="0")
	private Integer usedIntegralTotal = 0;
	
	@ZapcomApi(value = "CPS渠道编码", remark = "默认为空")
	private String cpsCode = "";
	
	@ZapcomApi(value="是否原价购买", remark="0：不原价，1：原价")
	private String isOriginal = "0";
	
	@ZapcomApi(value="拼团标示", remark="0：非拼团，1：拼团")
	private String collageFlag = "0";
	
	@ZapcomApi(value="团编码", remark="要参与团的团编码")
	private String collageCode = "";
	
	@ZapcomApi(value="活动编号", remark="兑换码兑换活动")
	private String activityCode = "";
	
	@ZapcomApi(value="兑换码", remark="兑换码兑换活动")
	private String redeemCode = "";
	
	@ZapcomApi(value="版块标识", remark="版块标识:积分签到版块为10,大转盘版块为20")
	private String blockSign = "";
	
	@ZapcomApi(value="订单确认是否来自购物车标识 0否 1是")
	private String ifFromShopCar = "0";
	
	@ZapcomApi(value="特殊促销活动编号",remark="例：投票换购")
	private String eventCode = "";
	
	@ZapcomApi(value="是否橙意卡专题订单", remark="0：不是，1：是")
	private String isCyk = "0";
	
	@ZapcomApi(value="互动活动编号", remark="互动活动编号")
	private String huDongCode = "";
	
	@ZapcomApi(value="下单商品所属分销人信息")
	private List<DistributionInfoModel> productSharedInfos = new ArrayList<DistributionInfoModel>();
	
	@ZapcomApi(value="缤纷扫码编号", remark="")
	private String qrcode = "";
	
	@ZapcomApi(value="果树编号", remark="惠惠农场活动")
	private String treeCode = "";
	
	@ZapcomApi(value = "app直播房间编号", remark = "为了统计直播房间下单量")
	private String appLiveRoomId = "";
	
	
	public String getAppLiveRoomId() {
		return appLiveRoomId;
	}

	public void setAppLiveRoomId(String appLiveRoomId) {
		this.appLiveRoomId = appLiveRoomId;
	}

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
	
	public List<DistributionInfoModel> getProductSharedInfos() {
		return productSharedInfos;
	}

	public void setProductSharedInfos(List<DistributionInfoModel> productSharedInfos) {
		this.productSharedInfos = productSharedInfos;
	}	public String getBlockSign() {
		return blockSign;
	}

	public double getUsedhjycoinTotal() {
		return usedhjycoinTotal;
	}

	public void setUsedhjycoinTotal(double usedhjycoinTotal) {
		this.usedhjycoinTotal = usedhjycoinTotal;
	}

	public void setBlockSign(String blockSign) {
		this.blockSign = blockSign;
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

	/**
	 * @return the isMemberCode
	 */
	public String getIsMemberCode() {
		return isMemberCode;
	}

	/**
	 * @param isMemberCode the isMemberCode to set
	 */
	public void setIsMemberCode(String isMemberCode) {
		this.isMemberCode = isMemberCode;
	}

	public double getCzj_money() {
		return czj_money;
	}

	public void setCzj_money(double czj_money) {
		this.czj_money = czj_money;
	}

	public double getZck_money() {
		return zck_money;
	}

	public void setZck_money(double zck_money) {
		this.zck_money = zck_money;
	} 
	
	public String getBuyer_address_code() {
		return buyer_address_code;
	}

	public void setBuyer_address_code(String buyer_address_code) {
		this.buyer_address_code = buyer_address_code;
	}

	public String getBuyer_code() {
		return buyer_code;
	}

	public void setBuyer_code(String buyer_code) {
		this.buyer_code = buyer_code;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public String getBuyer_address() {
		return buyer_address;
	}

	public void setBuyer_address(String buyer_address) {
		this.buyer_address = buyer_address;
	}

	public String getBuyer_mobile() {
		return buyer_mobile;
	}

	public void setBuyer_mobile(String buyer_mobile) {
		this.buyer_mobile = buyer_mobile;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
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

	public String getApp_vision() {
		return app_vision;
	}

	public void setApp_vision(String app_vision) {
		this.app_vision = app_vision;
	}

	public double getCheck_pay_money() {
		return check_pay_money;
	}

	public void setCheck_pay_money(double check_pay_money) {
		this.check_pay_money = check_pay_money;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getUniqid() {
		return uniqid;
	}

	public void setUniqid(String uniqid) {
		this.uniqid = uniqid;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getOs_info() {
		return os_info;
	}

	public void setOs_info(String os_info) {
		this.os_info = os_info;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getNet_type() {
		return net_type;
	}

	public void setNet_type(String net_type) {
		this.net_type = net_type;
	}

	public String getBuyer_address_id() {
		return buyer_address_id;
	}

	public void setBuyer_address_id(String buyer_address_id) {
		this.buyer_address_id = buyer_address_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<String> getCoupon_codes() {
		return coupon_codes;
	}

	public void setCoupon_codes(List<String> coupon_codes) {
		this.coupon_codes = coupon_codes;
	}

	public String getPay_ip() {
		return pay_ip;
	}

	public void setPay_ip(String pay_ip) {
		this.pay_ip = pay_ip;
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

	public String getYygOrderNo() {
		return yygOrderNo;
	}

	public void setYygOrderNo(String yygOrderNo) {
		this.yygOrderNo = yygOrderNo;
	}

	public String getYygMac() {
		return yygMac;
	}

	public void setYygMac(String yygMac) {
		this.yygMac = yygMac;
	}

	public String getYygPayMoney() {
		return yygPayMoney;
	}

	public void setYygPayMoney(String yygPayMoney) {
		this.yygPayMoney = yygPayMoney;
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

	public Integer getUsedBeanTotal() {
		return usedBeanTotal;
	}

	public void setUsedBeanTotal(Integer usedBeanTotal) {
		this.usedBeanTotal = usedBeanTotal;
	}

	public Integer getUsedIntegralTotal() {
		return usedIntegralTotal;
	}

	public void setUsedIntegralTotal(Integer usedIntegralTotal) {
		this.usedIntegralTotal = usedIntegralTotal;
	}

	public String getCpsCode() {
		return cpsCode;
	}

	public void setCpsCode(String cpsCode) {
		this.cpsCode = cpsCode;
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

	public String getEncryptToken() {
		return encryptToken;
	}

	public void setEncryptToken(String encryptToken) {
		this.encryptToken = encryptToken;
	}

	public String getRedeemCode() {
		return redeemCode;
	}

	public void setRedeemCode(String redeemCode) {
		this.redeemCode = redeemCode;
	}
	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
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

	public String getOrderPageSouce() {
		return orderPageSouce;
	}

	public void setOrderPageSouce(String orderPageSouce) {
		this.orderPageSouce = orderPageSouce;
	}

}
