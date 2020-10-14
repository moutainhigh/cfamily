package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmasorder.model.NoStockOrFailureGoods;
import com.srnpr.xmasorder.model.TeslaModelDiscount;
import com.cmall.familyhas.model.GoodsInfoForConfirm;
import com.cmall.familyhas.model.OrderSort;
import com.cmall.ordercenter.model.AddressInformation;
import com.cmall.ordercenter.model.CouponInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class APiOrderConfirmResult extends RootResult {

	@ZapcomApi(value = "商品列表", remark = "商品列表")
	private List<GoodsInfoForConfirm> resultGoodsInfo = new ArrayList<GoodsInfoForConfirm>();

	@ZapcomApi(value = "实付款", remark = "实付款")
	private double pay_money = 0.00;

	@ZapcomApi(value = "返现", remark = "返现")
	private double cash_back = 0.00;

	@ZapcomApi(value = "商品总金额", remark = "商品总金额")
	private double cost_money = 0.00;

	@ZapcomApi(value = "首单优惠", remark = "首单优惠")
	private double first_cheap = 0.00;

	@ZapcomApi(value = "运费", remark = "运费")
	private double sent_money = 0.00;

	@ZapcomApi(value = "用户维护的默认地址对象", remark = "用户维护的默认地址对象")
	private AddressInformation information = new AddressInformation();

	@ZapcomApi(value = "满减金额", remark = "满减金额")
	private double sub_money = 0.00;

	@ZapcomApi(value = "手机下单减少金额", remark = "手机下单减少金额")
	private double phone_money = 0.00;

	@ZapcomApi(value = "仓储地区所支持的支付方式", remark = "仓储地区所支持的支付方式  449716200001:在线支付,449716200002:货到付款,449746280003:只是支付宝支付,449746280005:只是微信支付,449746280009:只是微公社支付")
	private String pay_type = "";

	@ZapcomApi(value = "折扣信息List（折扣名称，折扣类型（加减），折扣金额）", remark = "List{首单立减30元，0，,30.00}")
	private List<TeslaModelDiscount> disList = new ArrayList<TeslaModelDiscount>();

	@ZapcomApi(value = "折扣备注信息", remark = "折扣备注信息", require = 1, demo = "商品包含了闪购商品，订单不享受立减30元的优惠")
	private List<String> disRemarks = new ArrayList<String>();

	@ZapcomApi(value = "提示信息", remark = "提示信息", require = 1, demo = "闪购商品不参与下单立减30元活动！")
	private String prompt = "";

	@ZapcomApi(value = "发票开票内容", remark = "发票开票内容", require = 1, demo = "明细，办公用品，数码商品")
	private List<String> bills = new ArrayList<String>();

	@ZapcomApi(value = "发票备注", remark = "发票备注", demo = "1.使用优惠券抵押的金额不可开具发票。2.海外购商品由于国外政策限制，无法提供发票")
	private String billRemark = "";

	@ZapcomApi(value = "订单分组及运费", remark = "订单分组及运费", require = 1, demo = "<<8019123,8019456,8019789>,10>")
	private List<OrderSort> orders = new ArrayList<OrderSort>();

	@ZapcomApi(value = "优惠券（此处为list，为以后扩充留用）", remark = "优惠券", demo = "yhq123456")
	private List<CouponInfo> coupons = new ArrayList<CouponInfo>();

	@ZapcomApi(value = "本用户可使用优惠劵数量", remark = "", demo = "1")
	private int couponAbleNum = 0;

	@ZapcomApi(value = "是否内购订单", remark = "0否，1是", demo = "0")
	private String sourceFlag = "0";

	@ZapcomApi(value = "微公社账户状态", remark = "001：正常，002：冻结，003：实付款金额为0", demo = "001")
	private String wgsStatus = "001";

	@ZapcomApi(value = "微公社余额", remark = "微公社余额", demo = "88.88")
	private double wgsMoney = 0.00;

	@ZapcomApi(value = "微公社最大可支付金额", remark = "微公社最大可支付金额", demo = "88.88")
	private double wgsMaxMoney = 0.00;

	@ZapcomApi(value = "微公社已使用金额", remark = "微公社已使用金额", demo = "88.88")
	private double wgsalUseMoney = 0.00;

	@ZapcomApi(value = "是否需要校验身份证  3.8.8", remark = "1-需要校验身份证  0-不需要校验身份证", demo = "1")
	private String isVerifyIdNumber = "0";

	@ZapcomApi(value = "提示退换货信息列表", remark = "提示退换货信息列表", demo = "")
	private List<String> tips = new ArrayList<String>();

	@ZapcomApi(value = "LD 商品金额", remark = "LD 商品金额", demo = "88.88")
	private double productMoneyForLD = 0.00;

	@ZapcomApi(value = "主播房间号", remark = "主播房间号", require = 0, demo = "46465456")
	private String roomId = "";

	@ZapcomApi(value = "主播ID", remark = "主播ID", require = 0, demo = "46579")
	private String anchorId = "";

	@ZapcomApi(value = "是否显示返现金额", remark = "1显示，其他：不显示")
	private int isShowDisMoney = 0;
	
	@ZapcomApi(value = "是否校验密码", remark = "1:校验,0:不校验")
	private int confirmpassword = 1;

	@ZapcomApi(value = "支持的支付类型", remark = "订单可以选择支付类型 (在线支付449716200001，支付宝449746280003，微信支付449746280005，银联支付449746280014)")
	private List<String> paymentTypeAll = new ArrayList<String>();
	
	@ZapcomApi(value = "已推荐最优方式", remark = "自动勾选优惠券时会返回")
	private String couponRecommend = "";
	
	@ZapcomApi(value = "橙意会员卡商品折扣", remark = "95")
	private String plusDiscount = "";
	
	@ZapcomApi(value = "橙意会员卡协议文档地址")
	private String plusDocUrl = "";
	
	@ZapcomApi(value = "橙意会员卡客服电话")
	private String plusKfTelphone = "";
	
	@ZapcomApi(value = "橙意会员卡购买提示")
	private String plusBuyTips = "";

	@ZapcomApi(value = "专门针对加价购活动失效的提示字段")
	private String jjgTips = "";
	
	@ZapcomApi(value = "厂商收款商品编号", remark = "")
	private List<String> dlrProductList = new ArrayList<String>();
	
	public List<String> getDlrProductList() {
		return dlrProductList;
	}

	public void setDlrProductList(List<String> dlrProductList) {
		this.dlrProductList = dlrProductList;
	}

	public String getJjgTips() {
		return jjgTips;
	}

	public void setJjgTips(String jjgTips) {
		this.jjgTips = jjgTips;
	}

	/**
	 * 2016-12-07 zhy
	 */
	@ZapcomApi(value = "我的惠豆总数", remark = "0")
	private Integer myBeanTotal = 0;
	@ZapcomApi(value = "可用惠豆总数", remark = "0")
	private Integer usableBeanTotal = 0;
	@ZapcomApi(value = "已使用的惠豆总数", remark = "0")
	private Integer usedBeanTotal = 0;
	@ZapcomApi(value = "惠豆使用规则", remark = "")
	private String beanRegulation;
	@ZapcomApi(value = "惠豆使用信息", remark = "")
	private String beanDetail;
	@ZapcomApi(value = "可使用惠豆数组集合", remark = "")
	private List<Integer> usableBeanList = new ArrayList<Integer>();
	@ZapcomApi(value = "惠豆兑换金额", remark = "0.00")
	private BigDecimal beanConversionMoney = BigDecimal.ZERO;

	@ZapcomApi(value = "惠豆是否显示", remark = "0不 显示 1显示")
	private Integer beanShowFlag = 0;
	@ZapcomApi(value = "惠豆是否可用", remark = "0 不可用 1可用")
	private Integer beanUsableFlag = 0;
	@ZapcomApi(value = "惠豆不可用原因", remark = "")
	private String beanDisabledReason = "";
	@ZapcomApi(value = "惠豆不可用原因", remark = "0不 显示 1显示")
	private Integer microCommuneOpenFlag = 0;
	
	// 积分相关
	@ZapcomApi(value="我的积分总数量",remark="0")
	private Integer myIntegralTotal = 0;
	@ZapcomApi(value="已使用积分数量",remark="0")
	private Integer usedIntegralTotal = 0;
	@ZapcomApi(value = "已使用积分兑换金额", remark = "0.00")
	private BigDecimal usedIntegralMoney = BigDecimal.ZERO;
	@ZapcomApi(value = "可用积分总数", remark = "0")
	private Integer usableIntegralTotal = 0;
	@ZapcomApi(value = "可用积分总数兑换金额", remark = "0")
	private BigDecimal usableIntegralTotalMoney = BigDecimal.ZERO;
	@ZapcomApi(value = "积分是否显示", remark = "0不 显示 1显示")
	private Integer integralShowFlag = 0;
	@ZapcomApi(value = "积分使用规则", remark = "")
	private String integralRegulation;
	
	@ZapcomApi(value = "惠币是否可用", remark = "0 不可用 1可用  默认0")
	private Integer hjycoinFlag = 0;
	@ZapcomApi(value = "惠币是否置灰", remark = "0不 置灰 1置灰")
	private Integer hjycoinZh = 0;
	@ZapcomApi(value="惠币总金额",remark="0")
	private BigDecimal hjycoinMoney = BigDecimal.ZERO;
	@ZapcomApi(value = "已使用惠币金额", remark = "0.00")
	private BigDecimal usedHjycoinMoney = BigDecimal.ZERO;
	@ZapcomApi(value = "最大使用惠币金额", remark = "0.00")
	private BigDecimal hjycoinMaxMoney = BigDecimal.ZERO;
	
	//储值金、暂存款
	@ZapcomApi(value = "储值金是否可用", remark = "0 不可用 1可用  默认0")
	private Integer czjUsableFlag = 0;
	
	@ZapcomApi(value = "储值金余额", remark = "储值金余额", demo = "88.88")
	private double czjMoney = 0.00;

	@ZapcomApi(value = "储值金最大可支付金额", remark = "储值金最大可支付金额", demo = "88.88")
	private double czjMaxMoney = 0.00;
	
	@ZapcomApi(value = "暂存款是否可用", remark = "0 不可用 1可用  默认0")
	private Integer zckUsableFlag = 0;
	
	@ZapcomApi(value = "暂存款余额", remark = "暂存款余额", demo = "88.88")
	private double zckMoney = 0.00;
	
	@ZapcomApi(value = "暂存款最大可支付金额", remark = "暂存款最大可支付金额", demo = "88.88")
	private double zckMaxMoney = 0.00;

	@ZapcomApi(value = "在线支付提示", remark = "立减10元")
	private String onlinePayTips = "";
	
	@ZapcomApi(value = "库存", remark = "0")
	private Integer stockNumSum = 0;
	
	@ZapcomApi(value = "购买下限", remark = "0")
	private Integer miniOrder = 0;
	
	@ZapcomApi(value = "购买上限", remark = "0")
	private Integer maxBuyCount = 0;
	
	@ZapcomApi(value = "每用户限购数",remark="")
    private Integer limitBuy = 99;
	
	@ZapcomApi(value = "是否显示限购数，0：不显示，1显示")
	private int showLimitNum=0;
	
	@ZapcomApi(value="是否展示优惠券", remark="是否展示优惠券 0不展示 1展示")
	private Integer couponUsableFlag = 1;
	
	@ZapcomApi(value="无库存商品列表")
	private List<NoStockOrFailureGoods> noStockOrFailureGoods = new ArrayList<NoStockOrFailureGoods>();
	
	@ZapcomApi(value = "积分是否置灰", remark = "0不 置灰 1置灰")
	private Integer integralZh = 0;
	
	@ZapcomApi(value = "储值金是否置灰", remark = "0不 置灰 1置灰")
	private Integer czjZh = 0;
	
	@ZapcomApi(value = "暂存款是否置灰", remark = "0不 置灰 1置灰")
	private Integer zckZh = 0;

	public Integer getIntegralZh() {
		return integralZh;
	}

	public void setIntegralZh(Integer integralZh) {
		this.integralZh = integralZh;
	}

	public Integer getHjycoinFlag() {
		return hjycoinFlag;
	}

	public void setHjycoinFlag(Integer hjycoinFlag) {
		this.hjycoinFlag = hjycoinFlag;
	}

	public Integer getHjycoinZh() {
		return hjycoinZh;
	}

	public void setHjycoinZh(Integer hjycoinZh) {
		this.hjycoinZh = hjycoinZh;
	}

	public BigDecimal getHjycoinMoney() {
		return hjycoinMoney;
	}

	public void setHjycoinMoney(BigDecimal hjycoinMoney) {
		this.hjycoinMoney = hjycoinMoney;
	}

	public BigDecimal getUsedHjycoinMoney() {
		return usedHjycoinMoney;
	}

	public void setUsedHjycoinMoney(BigDecimal usedHjycoinMoney) {
		this.usedHjycoinMoney = usedHjycoinMoney;
	}

	public BigDecimal getHjycoinMaxMoney() {
		return hjycoinMaxMoney;
	}

	public void setHjycoinMaxMoney(BigDecimal hjycoinMaxMoney) {
		this.hjycoinMaxMoney = hjycoinMaxMoney;
	}

	public Integer getCzjZh() {
		return czjZh;
	}

	public void setCzjZh(Integer czjZh) {
		this.czjZh = czjZh;
	}

	public Integer getZckZh() {
		return zckZh;
	}

	public void setZckZh(Integer zckZh) {
		this.zckZh = zckZh;
	}

	public int getConfirmpassword() {
		return confirmpassword;
	}

	public void setConfirmpassword(int confirmpassword) {
		this.confirmpassword = confirmpassword;
	}

	public int getShowLimitNum() {
		return showLimitNum;
	}

	public void setShowLimitNum(int showLimitNum) {
		this.showLimitNum = showLimitNum;
	}

	public String getIntegralRegulation() {
		return integralRegulation;
	}

	public void setIntegralRegulation(String integralRegulation) {
		this.integralRegulation = integralRegulation;
	}

	public Integer getMicroCommuneOpenFlag() {
		return microCommuneOpenFlag;
	}

	public void setMicroCommuneOpenFlag(Integer microCommuneOpenFlag) {
		this.microCommuneOpenFlag = microCommuneOpenFlag;
	}

	public String getBeanDisabledReason() {
		return beanDisabledReason;
	}

	public void setBeanDisabledReason(String beanDisabledReason) {
		this.beanDisabledReason = beanDisabledReason;
	}

	public Integer getBeanShowFlag() {
		return beanShowFlag;
	}

	public void setBeanShowFlag(Integer beanShowFlag) {
		this.beanShowFlag = beanShowFlag;
	}

	public Integer getBeanUsableFlag() {
		return beanUsableFlag;
	}

	public void setBeanUsableFlag(Integer beanUsableFlag) {
		this.beanUsableFlag = beanUsableFlag;
	}

	public BigDecimal getBeanConversionMoney() {
		return beanConversionMoney;
	}

	public void setBeanConversionMoney(BigDecimal beanConversionMoney) {
		this.beanConversionMoney = beanConversionMoney;
	}

	public String getBeanDetail() {
		return beanDetail;
	}

	public void setBeanDetail(String beanDetail) {
		this.beanDetail = beanDetail;
	}

	public List<Integer> getUsableBeanList() {
		return usableBeanList;
	}

	public void setUsableBeanList(List<Integer> usableBeanList) {
		this.usableBeanList = usableBeanList;
	}

	public Integer getUsedBeanTotal() {
		return usedBeanTotal;
	}

	public void setUsedBeanTotal(Integer usedBeanTotal) {
		this.usedBeanTotal = usedBeanTotal;
	}

	public Integer getMyBeanTotal() {
		return myBeanTotal;
	}

	public void setMyBeanTotal(Integer myBeanTotal) {
		this.myBeanTotal = myBeanTotal;
	}

	public Integer getUsableBeanTotal() {
		return usableBeanTotal;
	}

	public void setUsableBeanTotal(Integer usableBeanTotal) {
		this.usableBeanTotal = usableBeanTotal;
	}

	public List<GoodsInfoForConfirm> getResultGoodsInfo() {
		return resultGoodsInfo;
	}

	public String getBeanRegulation() {
		return beanRegulation;
	}

	public void setBeanRegulation(String beanRegulation) {
		this.beanRegulation = beanRegulation;
	}

	public void setResultGoodsInfo(List<GoodsInfoForConfirm> resultGoodsInfo) {
		this.resultGoodsInfo = resultGoodsInfo;
	}

	public double getPay_money() {
		return pay_money;
	}

	public void setPay_money(double pay_money) {
		this.pay_money = pay_money;
	}

	public double getCash_back() {
		return cash_back;
	}

	public void setCash_back(double cash_back) {
		this.cash_back = cash_back;
	}

	public double getCost_money() {
		return cost_money;
	}

	public void setCost_money(double cost_money) {
		this.cost_money = cost_money;
	}

	public double getFirst_cheap() {
		return first_cheap;
	}

	public void setFirst_cheap(double first_cheap) {
		this.first_cheap = first_cheap;
	}

	public double getSent_money() {
		return sent_money;
	}

	public void setSent_money(double sent_money) {
		this.sent_money = sent_money;
	}

	public double getSub_money() {
		return sub_money;
	}

	public void setSub_money(double sub_money) {
		this.sub_money = sub_money;
	}

	public double getPhone_money() {
		return phone_money;
	}

	public void setPhone_money(double phone_money) {
		this.phone_money = phone_money;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public List<TeslaModelDiscount> getDisList() {
		return disList;
	}

	public void setDisList(List<TeslaModelDiscount> disList) {
		this.disList = disList;
	}

	public List<String> getDisRemarks() {
		return disRemarks;
	}

	public void setDisRemarks(List<String> disRemarks) {
		this.disRemarks = disRemarks;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public AddressInformation getInformation() {
		return information;
	}

	public void setInformation(AddressInformation information) {
		this.information = information;
	}

	public List<String> getBills() {
		return bills;
	}

	public void setBills(List<String> bills) {
		this.bills = bills;
	}

	public List<OrderSort> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderSort> orders) {
		this.orders = orders;
	}

	public List<CouponInfo> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<CouponInfo> coupons) {
		this.coupons = coupons;
	}

	public int getCouponAbleNum() {
		return couponAbleNum;
	}

	public void setCouponAbleNum(int couponAbleNum) {
		this.couponAbleNum = couponAbleNum;
	}

	public String getSourceFlag() {
		return sourceFlag;
	}

	public void setSourceFlag(String sourceFlag) {
		this.sourceFlag = sourceFlag;
	}

	public String getBillRemark() {
		return billRemark;
	}

	public void setBillRemark(String billRemark) {
		this.billRemark = billRemark;
	}

	public double getWgsMoney() {
		return wgsMoney;
	}

	public void setWgsMoney(double wgsMoney) {
		this.wgsMoney = wgsMoney;
	}

	public double getWgsMaxMoney() {
		return wgsMaxMoney;
	}

	public void setWgsMaxMoney(double wgsMaxMoney) {
		this.wgsMaxMoney = wgsMaxMoney;
	}

	public double getWgsalUseMoney() {
		return wgsalUseMoney;
	}

	public void setWgsalUseMoney(double wgsalUseMoney) {
		this.wgsalUseMoney = wgsalUseMoney;
	}

	public String getWgsStatus() {
		return wgsStatus;
	}

	public void setWgsStatus(String wgsStatus) {
		this.wgsStatus = wgsStatus;
	}

	public String getIsVerifyIdNumber() {
		return isVerifyIdNumber;
	}

	public void setIsVerifyIdNumber(String isVerifyIdNumber) {
		this.isVerifyIdNumber = isVerifyIdNumber;
	}

	public List<String> getTips() {
		return tips;
	}

	public void setTips(List<String> tips) {
		this.tips = tips;
	}

	public double getProductMoneyForLD() {
		return productMoneyForLD;
	}

	public void setProductMoneyForLD(double productMoneyForLD) {
		this.productMoneyForLD = productMoneyForLD;
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

	public int getIsShowDisMoney() {
		return isShowDisMoney;
	}

	public void setIsShowDisMoney(int isShowDisMoney) {
		this.isShowDisMoney = isShowDisMoney;
	}
	
	public String getCouponRecommend() {
		return couponRecommend;
	}

	public void setCouponRecommend(String couponRecommend) {
		this.couponRecommend = couponRecommend;
	}
	
	public String getPlusDiscount() {
		return plusDiscount;
	}

	public void setPlusDiscount(String plusDiscount) {
		this.plusDiscount = plusDiscount;
	}

	public String getPlusDocUrl() {
		return plusDocUrl;
	}

	public void setPlusDocUrl(String plusDocUrl) {
		this.plusDocUrl = plusDocUrl;
	}
	
	public String getPlusKfTelphone() {
		return plusKfTelphone;
	}

	public void setPlusKfTelphone(String plusKfTelphone) {
		this.plusKfTelphone = plusKfTelphone;
	}

	public String getPlusBuyTips() {
		return plusBuyTips;
	}

	public void setPlusBuyTips(String plusBuyTips) {
		this.plusBuyTips = plusBuyTips;
	}

	public List<String> getPaymentTypeAll() {
		return paymentTypeAll;
	}

	public void setPaymentTypeAll(List<String> paymentTypeAll) {
		this.paymentTypeAll = paymentTypeAll;
	}

	public Integer getMyIntegralTotal() {
		return myIntegralTotal;
	}

	public void setMyIntegralTotal(Integer myIntegralTotal) {
		this.myIntegralTotal = myIntegralTotal;
	}

	public Integer getUsedIntegralTotal() {
		return usedIntegralTotal;
	}

	public void setUsedIntegralTotal(Integer usedIntegralTotal) {
		this.usedIntegralTotal = usedIntegralTotal;
	}

	public BigDecimal getUsedIntegralMoney() {
		return usedIntegralMoney;
	}

	public void setUsedIntegralMoney(BigDecimal usedIntegralMoney) {
		this.usedIntegralMoney = usedIntegralMoney;
	}

	public Integer getUsableIntegralTotal() {
		return usableIntegralTotal;
	}

	public void setUsableIntegralTotal(Integer usableIntegralTotal) {
		this.usableIntegralTotal = usableIntegralTotal;
	}

	public Integer getIntegralShowFlag() {
		return integralShowFlag;
	}

	public void setIntegralShowFlag(Integer integralShowFlag) {
		this.integralShowFlag = integralShowFlag;
	}

	public BigDecimal getUsableIntegralTotalMoney() {
		return usableIntegralTotalMoney;
	}

	public void setUsableIntegralTotalMoney(BigDecimal usableIntegralTotalMoney) {
		this.usableIntegralTotalMoney = usableIntegralTotalMoney;
	}

	public String getOnlinePayTips() {
		return onlinePayTips;
	}

	public void setOnlinePayTips(String onlinePayTips) {
		this.onlinePayTips = onlinePayTips;
	}

	public Integer getCzjUsableFlag() {
		return czjUsableFlag;
	}

	public void setCzjUsableFlag(Integer czjUsableFlag) {
		this.czjUsableFlag = czjUsableFlag;
	}

	public double getCzjMoney() {
		return czjMoney;
	}

	public void setCzjMoney(double czjMoney) {
		this.czjMoney = czjMoney;
	}

	public double getCzjMaxMoney() {
		return czjMaxMoney;
	}

	public void setCzjMaxMoney(double czjMaxMoney) {
		this.czjMaxMoney = czjMaxMoney;
	}

	public Integer getZckUsableFlag() {
		return zckUsableFlag;
	}

	public void setZckUsableFlag(Integer zckUsableFlag) {
		this.zckUsableFlag = zckUsableFlag;
	}

	public double getZckMoney() {
		return zckMoney;
	}

	public void setZckMoney(double zckMoney) {
		this.zckMoney = zckMoney;
	}

	public double getZckMaxMoney() {
		return zckMaxMoney;
	}

	public void setZckMaxMoney(double zckMaxMoney) {
		this.zckMaxMoney = zckMaxMoney;
	}
	
	public Integer getStockNumSum() {
		return stockNumSum;
	}

	public void setStockNumSum(Integer stockNumSum) {
		this.stockNumSum = stockNumSum;
	}

	public Integer getMiniOrder() {
		return miniOrder;
	}

	public void setMiniOrder(Integer miniOrder) {
		this.miniOrder = miniOrder;
	}

	public Integer getMaxBuyCount() {
		return maxBuyCount;
	}

	public void setMaxBuyCount(Integer maxBuyCount) {
		this.maxBuyCount = maxBuyCount;
	}

	public Integer getLimitBuy() {
		return limitBuy;
	}

	public void setLimitBuy(Integer limitBuy) {
		this.limitBuy = limitBuy;
	}

	public Integer getCouponUsableFlag() {
		return couponUsableFlag;
	}

	public void setCouponUsableFlag(Integer couponUsableFlag) {
		this.couponUsableFlag = couponUsableFlag;
	}

	public List<NoStockOrFailureGoods> getNoStockOrFailureGoods() {
		return noStockOrFailureGoods;
	}

	public void setNoStockOrFailureGoods(List<NoStockOrFailureGoods> noStockOrFailureGoods) {
		this.noStockOrFailureGoods = noStockOrFailureGoods;
	}

}
