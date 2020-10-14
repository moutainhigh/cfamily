package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Button;
import com.cmall.familyhas.api.model.Reason;
import com.cmall.groupcenter.account.model.ApiHomeOrderTrackingListResult;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiOrderDetailsResult  extends RootResultWeb{
	@ZapcomApi(value="订单编号")
	private String order_code = "";
	@ZapcomApi(value="订单状态" ,remark="4497153900010001:下单成功-未付款,4497153900010002:下单成功-未发货,4497153900010003:已发货,4497153900010004:已收货,4497153900010005	:交易成功,4497153900010006:交易失败")
	private String order_status = "";
	@ZapcomApi(value="订单金额")
	private String order_money = "";
	@ZapcomApi(value="下单时间")
	private String create_time = "";
	@ZapcomApi(value="应付金额")
	private BigDecimal due_money;
	@ZapcomApi(value="商品总金额")
	private BigDecimal productMoney = BigDecimal.ZERO;
	@ZapcomApi(value="储值金")
	private BigDecimal czjAmt = BigDecimal.ZERO;
	@ZapcomApi(value="暂存款")	
	private BigDecimal zckAmt = BigDecimal.ZERO;
	@ZapcomApi(value="首单优惠")
	private String firstFavorable = "";
	@ZapcomApi(value="运费")
	private Double freight = 0.00;
	@ZapcomApi(value="满减")
	private Double fullSubtraction = 0.00;
	@ZapcomApi(value="关税")
	private String tariffMoney = "";
	@ZapcomApi(value="手机下单减少")
	private Double telephoneSubtraction = 0.00;
	@ZapcomApi(value="收货人地址")
	private String consigneeAddress = "";
	@ZapcomApi(value="收货人电话")
	private String consigneeTelephone = "";
	@ZapcomApi(value="支付方式")
	private String pay_type = "";
	@ZapcomApi(value="支付宝移动支付链接",remark="")
	private String alipayUrl = "";
	@ZapcomApi(value="支付宝Sign",remark="签名过的")
	private String alipaySign = "";
	@ZapcomApi(value="默认支付方式",remark="支付方式:(449746280003:支付宝支付,449746280005:微信支付),449746280009:微公社支付")
	private String default_Pay_type="449746280003";
	@ZapcomApi(value="收货人姓名")
	private String consigneeName = "";
	@ZapcomApi(value="失效时间提示")
	private String  failureTimeReminder = "";
	@ZapcomApi(value="是否为闪购订单", remark="0:闪购     1:非闪购")
	private String ifFlashSales = "";	
	@ZapcomApi(value="是否为需要分包的订单",remark="0:是    1:否")
	private String isSeparateOrder = "1";
	@ZapcomApi(value="是否为海外购", remark="1:是    0:否")
	private String flagTheSea = "0";
	@ZapcomApi(value="是否为网易考拉订单", remark="0:否     1:是")
	private String flagKaola = "0";
	@ZapcomApi(value="预计返利金额")
	private BigDecimal cashBackMoney;
	@ZapcomApi(value="是否为新用户", remark="0:老用户     1:新用户")
	private String  newuserFlag = "0";
	@ZapcomApi(value="发票信息")
	private InvoiceInformationResult invoiceInformation = new InvoiceInformationResult();
	@ZapcomApi(value="是否展示支付方式和发票信息", remark="1:是    0:否")
	private String isShowPay = "1";	
	@ZapcomApi(value="是否展示发票信息", remark="1:是    0:否")
	private String isShowInvoice = "1";
	@ZapcomApi(value="余额支付")
	private List<ApiOrderDetailsBalancePayResult> orderDetailsBalancePay = new ArrayList<ApiOrderDetailsBalancePayResult>();
	@ZapcomApi(value="订单商品列表")
	private List<ApiOrderSellerDetailsResult>  orderSellerList = new ArrayList<ApiOrderSellerDetailsResult>();
	@ZapcomApi(value="活动信息")
	private List<ApiOrderActivityDetailsResult> orderActivityDetailsResult = new ArrayList<ApiOrderActivityDetailsResult>();
	@ZapcomApi(value="活动备注")
	private List<ApiOrderActivityRemarkDetailsResult> orderActivityRemarkDetailsResult = new ArrayList<ApiOrderActivityRemarkDetailsResult>();
	@ZapcomApi(value="运单号")
	private String yc_express_num = "";
	@ZapcomApi(value="送货商名称")
	private String yc_delivergoods_user_name = "";
	@ZapcomApi(value="订单跟踪信息")
	private List<ApiHomeOrderTrackingListResult> apiHomeOrderTrackingListResult = new ArrayList<ApiHomeOrderTrackingListResult>();
	@ZapcomApi(value="包裹信息")
	private List<ApiOrderKjtParcelResult> apiOrderKjtParcelResult = new ArrayList<ApiOrderKjtParcelResult>();	
	@ZapcomApi(value="身份证号码")
	private String idNumber = "";
	@ZapcomApi(value="未付款截止时间(格式例:2019-10-10 23:59:59)")
	private String remainTime = "";	
	
	@ZapcomApi(value = "订单支持的功能按钮", require = 1, demo = "xxxxx")
	private List<Button> orderButtonList = new ArrayList<Button>();
	
	@ZapcomApi(value = "售后详情", remark="目前只是提供售后详情入口'1':显示，'0'：不显示", require = 1, demo = "xxxxx")
	private String link = "1";
	
	@ZapcomApi(value = "取消发货原因列表", require = 1)
	private List<Reason> reasonList=new ArrayList<Reason>();
	
	@ZapcomApi(value="惠豆金额",remark="已经将惠豆转成金额")
	private BigDecimal hjyBean = BigDecimal.ZERO;
	
	@ZapcomApi(value="积分金额",remark="已经将积分转成金额")
	private BigDecimal integralMoney = BigDecimal.ZERO;
	
	@ZapcomApi(value="物流信息温馨提示")
	private String logisticsTips = "";		
	
	@ZapcomApi(value="商品总积分")
	private BigDecimal productIntegral = BigDecimal.ZERO;
	
	@ZapcomApi(value="订单类型")
	private String orderType = "";
	
	@ZapcomApi(value="换货标识",remark="有换货标识时给出图片url")
	private String isChangeGoods = "";
	
	@ZapcomApi(value="拼团编码",remark="微信商城、小程序通路兼容参数，如果是拼团单，返回拼团编码，如果非拼团单，返回值为空。其他通路忽略此参数")
	private String collageCode = "";
	
	
	public String getCollageCode() {
		return collageCode;
	}
	public void setCollageCode(String collageCode) {
		this.collageCode = collageCode;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getIsSeparateOrder() {
		return isSeparateOrder;
	}
	public void setIsSeparateOrder(String isSeparateOrder) {
		this.isSeparateOrder = isSeparateOrder;
	}
	public String getFlagTheSea() {
		return flagTheSea;
	}
	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}
	public List<ApiOrderKjtParcelResult> getApiOrderKjtParcelResult() {
		return apiOrderKjtParcelResult;
	}
	public void setApiOrderKjtParcelResult(
			List<ApiOrderKjtParcelResult> apiOrderKjtParcelResult) {
		this.apiOrderKjtParcelResult = apiOrderKjtParcelResult;
	}
	public String getTariffMoney() {
		return tariffMoney;
	}
	public void setTariffMoney(String tariffMoney) {
		this.tariffMoney = tariffMoney;
	}
	public List<ApiOrderDetailsBalancePayResult> getOrderDetailsBalancePay() {
		return orderDetailsBalancePay;
	}
	public void setOrderDetailsBalancePay(
			List<ApiOrderDetailsBalancePayResult> orderDetailsBalancePay) {
		this.orderDetailsBalancePay = orderDetailsBalancePay;
	}
	public String getDefault_Pay_type() {
		return default_Pay_type;
	}
	public void setDefault_Pay_type(String default_Pay_type) {
		this.default_Pay_type = default_Pay_type;
	}
	public String getNewuserFlag() {
		return newuserFlag;
	}
	public void setNewuserFlag(String newuserFlag) {
		this.newuserFlag = newuserFlag;
	}
	public BigDecimal getCashBackMoney() {
		return cashBackMoney;
	}
	public void setCashBackMoney(BigDecimal cashBackMoney) {
		this.cashBackMoney = cashBackMoney;
	}
	public List<ApiOrderActivityRemarkDetailsResult> getOrderActivityRemarkDetailsResult() {
		return orderActivityRemarkDetailsResult;
	}
	public void setOrderActivityRemarkDetailsResult(
			List<ApiOrderActivityRemarkDetailsResult> orderActivityRemarkDetailsResult) {
		this.orderActivityRemarkDetailsResult = orderActivityRemarkDetailsResult;
	}
	public List<ApiOrderActivityDetailsResult> getOrderActivityDetailsResult() {
		return orderActivityDetailsResult;
	}
	public void setOrderActivityDetailsResult(
			List<ApiOrderActivityDetailsResult> orderActivityDetailsResult) {
		this.orderActivityDetailsResult = orderActivityDetailsResult;
	}
	public String getAlipayUrl() {
		return alipayUrl;
	}
	public void setAlipayUrl(String alipayUrl) {
		this.alipayUrl = alipayUrl;
	}
	public String getAlipaySign() {
		return alipaySign;
	}
	public void setAlipaySign(String alipaySign) {
		this.alipaySign = alipaySign;
	}
	public Double getFreight() {
		return freight;
	}
	public Double getFullSubtraction() {
		return fullSubtraction;
	}
	public Double getTelephoneSubtraction() {
		return telephoneSubtraction;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public List<ApiOrderSellerDetailsResult> getOrderSellerList() {
		return orderSellerList;
	}
	public void setOrderSellerList(List<ApiOrderSellerDetailsResult> orderSellerList) {
		this.orderSellerList = orderSellerList;
	}
	public String getOrder_money() {
		return order_money;
	}
	public void setOrder_money(String order_money) {
		this.order_money = order_money;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public BigDecimal getDue_money() {
		return due_money;
	}
	public void setDue_money(BigDecimal due_money) {
		this.due_money = due_money;
	}
	public void setFreight(Double freight) {
		this.freight = freight;
	}
	public void setFullSubtraction(Double fullSubtraction) {
		this.fullSubtraction = fullSubtraction;
	}
	public void setTelephoneSubtraction(Double telephoneSubtraction) {
		this.telephoneSubtraction = telephoneSubtraction;
	}
	public String getFirstFavorable() {
		return firstFavorable;
	}
	public void setFirstFavorable(String firstFavorable) {
		this.firstFavorable = firstFavorable;
	}
	public String getConsigneeAddress() {
		return consigneeAddress;
	}
	public void setConsigneeAddress(String consigneeAddress) {
		this.consigneeAddress = consigneeAddress;
	}
	public String getConsigneeTelephone() {
		return consigneeTelephone;
	}
	public void setConsigneeTelephone(String consigneeTelephone) {
		this.consigneeTelephone = consigneeTelephone;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public InvoiceInformationResult getInvoiceInformation() {
		return invoiceInformation;
	}
	public void setInvoiceInformation(InvoiceInformationResult invoiceInformation) {
		this.invoiceInformation = invoiceInformation;
	}
	public String getConsigneeName() {
		return consigneeName;
	}
	public void setConsigneeName(String consigneeName) {
		this.consigneeName = consigneeName;
	}
	public String getFailureTimeReminder() {
		return failureTimeReminder;
	}
	public void setFailureTimeReminder(String failureTimeReminder) {
		this.failureTimeReminder = failureTimeReminder;
	}
	public String getIfFlashSales() {
		return ifFlashSales;
	}
	public void setIfFlashSales(String ifFlashSales) {
		this.ifFlashSales = ifFlashSales;
	}
	public String getYc_express_num() {
		return yc_express_num;
	}
	public void setYc_express_num(String yc_express_num) {
		this.yc_express_num = yc_express_num;
	}
	public String getYc_delivergoods_user_name() {
		return yc_delivergoods_user_name;
	}
	public void setYc_delivergoods_user_name(String yc_delivergoods_user_name) {
		this.yc_delivergoods_user_name = yc_delivergoods_user_name;
	}
	public List<ApiHomeOrderTrackingListResult> getApiHomeOrderTrackingListResult() {
		return apiHomeOrderTrackingListResult;
	}
	public void setApiHomeOrderTrackingListResult(
			List<ApiHomeOrderTrackingListResult> apiHomeOrderTrackingListResult) {
		this.apiHomeOrderTrackingListResult = apiHomeOrderTrackingListResult;
	}
	public BigDecimal getProductMoney() {
		return productMoney;
	}
	
	public void setProductMoney(BigDecimal productMoney) {
		this.productMoney = productMoney;
	}
	
	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}
	public BigDecimal getCzjAmt() {
		return czjAmt;
	}
	public void setCzjAmt(BigDecimal czjAmt) {
		this.czjAmt = czjAmt;
	}
	public BigDecimal getZckAmt() {
		return zckAmt;
	}
	public void setZckAmt(BigDecimal zckAmt) {
		this.zckAmt = zckAmt;
	}
	public List<Button> getOrderButtonList() {
		return orderButtonList;
	}
	public void setOrderButtonList(List<Button> orderButtonList) {
		this.orderButtonList = orderButtonList;
	}
	public List<Reason> getReasonList() {
		return reasonList;
	}
	public void setReasonList(List<Reason> reasonList) {
		this.reasonList = reasonList;
	}
	public BigDecimal getHjyBean() {
		return hjyBean;
	}
	public void setHjyBean(BigDecimal hjyBean) {
		this.hjyBean = hjyBean;
	}
	public String getLogisticsTips() {
		return logisticsTips;
	}
	public void setLogisticsTips(String logisticsTips) {
		this.logisticsTips = logisticsTips;
	}
	public BigDecimal getIntegralMoney() {
		return integralMoney;
	}
	public void setIntegralMoney(BigDecimal integralMoney) {
		this.integralMoney = integralMoney;
	}
	public BigDecimal getProductIntegral() {
		return productIntegral;
	}
	public void setProductIntegral(BigDecimal productIntegral) {
		this.productIntegral = productIntegral;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getIsShowPay() {
		return isShowPay;
	}
	public void setIsShowPay(String isShowPay) {
		this.isShowPay = isShowPay;
	}
	public String getIsShowInvoice() {
		return isShowInvoice;
	}
	public void setIsShowInvoice(String isShowInvoice) {
		this.isShowInvoice = isShowInvoice;
	}
	public String getFlagKaola() {
		return flagKaola;
	}
	public void setFlagKaola(String flagKaola) {
		this.flagKaola = flagKaola;
	}
	public String getIsChangeGoods() {
		return isChangeGoods;
	}
	public void setIsChangeGoods(String isChangeGoods) {
		this.isChangeGoods = isChangeGoods;
	}
	public String getRemainTime() {
		return remainTime;
	}
	public void setRemainTime(String remainTime) {
		this.remainTime = remainTime;
	}
	
}
