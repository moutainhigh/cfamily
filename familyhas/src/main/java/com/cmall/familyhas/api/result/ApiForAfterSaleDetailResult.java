package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Button;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForAfterSaleDetailResult extends RootResult {

	@ZapcomApi(value = "售后单号", require = 1, demo = "RGR160303100002")
	private String afterSaleCode;
	
	@ZapcomApi(value = "售后原因", require = 1, demo = "其他")
	private String afterSaleReason;
	
	@ZapcomApi(value = "是否收到货", require = 1, demo = "4497476900040001:是,4497476900040002:否")
	private String ifGetGoods;

	@ZapcomApi(value = "退款金额", require = 1, demo = "100")
	private BigDecimal returnMoney = BigDecimal.ZERO;
	
	@ZapcomApi(value = "退还积分", require = 1, demo = "100")
	private BigDecimal returnJF = BigDecimal.ZERO;
	
	@ZapcomApi(value = "退还惠币", require = 1, demo = "1.00")
	private BigDecimal returnHjycoin = BigDecimal.ZERO;
	
	@ZapcomApi(value = "退还储值金", require = 1, demo = "100")
	private BigDecimal returnCZJ = BigDecimal.ZERO;
	
	@ZapcomApi(value = "退还暂存款", require = 1, demo = "100")
	private BigDecimal returnZCK = BigDecimal.ZERO;
	
	@ZapcomApi(value = "订单编号", require = 1, demo = "DD123456789")
	private String orderCode;
	
	@ZapcomApi(value = "申请时间", require = 1, demo = "2019-02-12 10:45:22")
	private String supplyTime;

	@ZapcomApi(value = "页面按钮", require = 1, remark="4497477800080014:客服电话,4497477800050011:取消申请,4497477800080001:填写退换货物流信息,申请售后：4497477800080008", demo = "")
	private List<Button> buttons = new ArrayList<Button>();
	
	@ZapcomApi(value = "审核详情", require = 1, demo = "客服通过了您的申请")
	private String checkInfo;
	
	@ZapcomApi(value = "售后单状态", require = 1,remark="", demo = "退款中")
	private String checkStatus;
	
	@ZapcomApi(value = "售后商品名称", require = 1, demo = "联想笔记本电脑")
	private String goodsName;
	
	@ZapcomApi(value = "维护物流信息最后期限", require = 1,remark="yyyy-MM-dd HH:mm:ss", demo = "2019-10-01 12:53:50")
	private String deadLine;
	
	@ZapcomApi(value = "规格/款式 ", require = 1, demo = "xxxxx")
	private List<ApiSellerStandardAndStyleResult> standardAndStyleList = new ArrayList<ApiSellerStandardAndStyleResult>();
	
	@ZapcomApi(value = "商品单价", require = 1, demo = "100")
	private BigDecimal goodsPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value = "商品数量", require = 1, demo = "2")
	private int goodsCount;
	
	@ZapcomApi(value = "售后商品图片", require = 1, demo = "")
	private String goodsPic;
	
	@ZapcomApi(value = "售后商品编号", require = 1, demo = "")
	private String skuCode;
	
	@ZapcomApi(value = "售后类型", require = 1, remark="售后类型：（退货退款）（换货）（ 仅退款）（ 取消发货）（ 拒收）", demo = "退货退款")
	private String afterSaleType;
	
	@ZapcomApi(value = "LD拓展字段，订单序号", require = 1, demo = "")
	private String orderSeq;
	
	@ZapcomApi(value = "拓展字段，换货标识", require = 1, demo = "")
	private String isChangeGoods = "";
	

	public BigDecimal getReturnHjycoin() {
		return returnHjycoin;
	}

	public void setReturnHjycoin(BigDecimal returnHjycoin) {
		this.returnHjycoin = returnHjycoin;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getAfterSaleType() {
		return afterSaleType;
	}

	public void setAfterSaleType(String afterSaleType) {
		this.afterSaleType = afterSaleType;
	}

	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getIsChangeGoods() {
		return isChangeGoods;
	}

	public void setIsChangeGoods(String isChangeGoods) {
		this.isChangeGoods = isChangeGoods;
	}

	public String getAfterSaleCode() {
		return afterSaleCode;
	}

	public void setAfterSaleCode(String afterSaleCode) {
		this.afterSaleCode = afterSaleCode;
	}

	public String getAfterSaleReason() {
		return afterSaleReason;
	}

	public void setAfterSaleReason(String afterSaleReason) {
		this.afterSaleReason = afterSaleReason;
	}

	public String getIfGetGoods() {
		return ifGetGoods;
	}

	public void setIfGetGoods(String ifGetGoods) {
		this.ifGetGoods = ifGetGoods;
	}

	public BigDecimal getReturnMoney() {
		return returnMoney;
	}

	public void setReturnMoney(BigDecimal returnMoney) {
		this.returnMoney = returnMoney;
	}

	public BigDecimal getReturnJF() {
		return returnJF;
	}

	public void setReturnJF(BigDecimal returnJF) {
		this.returnJF = returnJF;
	}

	public BigDecimal getReturnCZJ() {
		return returnCZJ;
	}

	public void setReturnCZJ(BigDecimal returnCZJ) {
		this.returnCZJ = returnCZJ;
	}

	public BigDecimal getReturnZCK() {
		return returnZCK;
	}

	public void setReturnZCK(BigDecimal returnZCK) {
		this.returnZCK = returnZCK;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getSupplyTime() {
		return supplyTime;
	}

	public void setSupplyTime(String supplyTime) {
		this.supplyTime = supplyTime;
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public void setButtons(List<Button> buttons) {
		this.buttons = buttons;
	}

	public String getCheckInfo() {
		return checkInfo;
	}

	public void setCheckInfo(String checkInfo) {
		this.checkInfo = checkInfo;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public List<ApiSellerStandardAndStyleResult> getStandardAndStyleList() {
		return standardAndStyleList;
	}

	public void setStandardAndStyleList(List<ApiSellerStandardAndStyleResult> standardAndStyleList) {
		this.standardAndStyleList = standardAndStyleList;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	public String getGoodsPic() {
		return goodsPic;
	}

	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}
	
	
}
