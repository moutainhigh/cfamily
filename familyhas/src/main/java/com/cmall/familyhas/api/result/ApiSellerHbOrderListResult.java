package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Button;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**
 * 商品订单信息
 * @author wz
 *
 */
public class ApiSellerHbOrderListResult{
	@ZapcomApi(value="订单状态",remark="下单成功-未付款：4497153900010001，下单成功-未发货：4497153900010002，已发货：4497153900010003，已收货：4497153900010004，交易成功：4497153900010005，交易失败：4497153900010006，等待审核：4497153900010008")
	private String order_status = "";
	@ZapcomApi(value="拼单状态",remark="拼团中：449748300001，拼团成功：449748300002，拼团失败：449748300003")
	private String group_buying_status = "";
	@ZapcomApi(value="订单类型",remark="常规订单：449715200025 ,拼团订单：449715200026")
	private String order_type = "449715200025";
	@ZapcomApi(value="推广赚类型",remark="推广收益：4497471600610001 ,买家秀：4497471600610002")
	private String tgz_type = "";
	@ZapcomApi(value="预估收益")
	private String tgz_money = "";
	@ZapcomApi(value="订单号",remark="")
	private String order_code = "";
	@ZapcomApi(value="拼团单号",remark="拓展5.4.0，拼团订单需要返回拼团编号")
	private String collageCode = "";
	@ZapcomApi(value="订单生成时间",remark="")
	private String create_time = "";
	@ZapcomApi(value="订单里的商品数量",remark="")
	private int orderStatusNumber;
	@ZapcomApi(value="实付总价",remark="")
	private String due_money = "";
	@ZapcomApi(value="支付宝移动支付链接",remark="")
	private String alipayUrl = "";
	@ZapcomApi(value="支付宝Sign",remark="签名过的")
	private String alipaySign = "";
	@ZapcomApi(value="是否为闪购订单", remark="0:闪购     1:非闪购")
	private String ifFlashSales = "";
//	@ZapcomApi(value="是否为快境通订单", remark="0:是     1:否")
//	private String flagTheSea = "";
	@ZapcomApi(value="是否为需要分包的订单",remark="0:是    1:否")
	private String isSeparateOrder = "1";
	@ZapcomApi(value="默认支付方式",remark="支付方式:(449746280003:支付宝支付,449746280005:微信支付,449746280009:微公社支付)")
	private String default_Pay_type="449746280003";
	@ZapcomApi(value="每个订单的商品信息",remark="")
	private List<ApiSellerHbListResult> apiSellerList = new ArrayList<ApiSellerHbListResult>();
	@ZapcomApi(value = "商品是否已评价",remark="已评价：1,无评价：0")
	private Integer is_comment = 0;
	@ZapcomApi(value = "拼团订单的拓展值",remark="如果是订单状态为：449748300001（拼团中），则需要此辅助值显示该团还差几人成团")
	private Integer needCount = 0;
	
	@ZapcomApi(value = "订单支持的功能按钮", require = 1, demo = "xxxxx")
	private List<Button> orderButtonList = new ArrayList<Button>();
	
	@ZapcomApi(value="是否可以删除订单",remark="删除按钮不在其他按钮列表展示，因此加字段进行区别是否可以删除订单,5.0.6+")
	private Integer isDeleteOrder = 0;
	
	@ZapcomApi(value="换货标识",remark="有换货标识时给出图片url")
	private String isChangeGoods = "";
	
	@ZapcomApi(value="LD订单序号",remark="0")
	private String orderSeq = "0";
	
	@ZapcomApi(value="提示词", demo="该商品已经超过售后期限")
	private String cue_words = "";
	
	
	
	
	public String getTgz_type() {
		return tgz_type;
	}
	public void setTgz_type(String tgz_type) {
		this.tgz_type = tgz_type;
	}
	public String getTgz_money() {
		return tgz_money;
	}
	public void setTgz_money(String tgz_money) {
		this.tgz_money = tgz_money;
	}
	public String getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}
	public String getCollageCode() {
		return collageCode;
	}
	public void setCollageCode(String collageCode) {
		this.collageCode = collageCode;
	}
	public String getGroup_buying_status() {
		return group_buying_status;
	}
	public void setGroup_buying_status(String group_buying_status) {
		this.group_buying_status = group_buying_status;
	}
	public String getOrder_type() {
		return order_type;
	}
	public void setOrder_type(String order_type) {
		this.order_type = order_type;
	}
	public Integer getNeedCount() {
		return needCount;
	}
	public void setNeedCount(Integer needCount) {
		this.needCount = needCount;
	}
	public String getIsSeparateOrder() {
		return isSeparateOrder;
	}
	public void setIsSeparateOrder(String isSeparateOrder) {
		this.isSeparateOrder = isSeparateOrder;
	}
	public String getIfFlashSales() {
		return ifFlashSales;
	}
	public void setIfFlashSales(String ifFlashSales) {
		this.ifFlashSales = ifFlashSales;
	}
	public String getAlipaySign() {
		return alipaySign;
	}
	public void setAlipaySign(String alipaySign) {
		this.alipaySign = alipaySign;
	}
	public String getAlipayUrl() {
		return alipayUrl;
	}
	
	public Integer getIs_comment() {
		return is_comment;
	}
	public void setIs_comment(Integer is_comment) {
		this.is_comment = is_comment;
	}
	public void setAlipayUrl(String alipayUrl) {
		this.alipayUrl = alipayUrl;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getOrder_code() {
		return order_code;
	}
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getOrderStatusNumber() {
		return orderStatusNumber;
	}
	public void setOrderStatusNumber(int orderStatusNumber) {
		this.orderStatusNumber = orderStatusNumber;
	}
	public String getDue_money() {
		return due_money;
	}
	public void setDue_money(String due_money) {
		this.due_money = due_money;
	}
	
	public List<ApiSellerHbListResult> getApiSellerList() {
		return apiSellerList;
	}
	public void setApiSellerList(List<ApiSellerHbListResult> apiSellerList) {
		this.apiSellerList = apiSellerList;
	}
	public String getDefault_Pay_type() {
		return default_Pay_type;
	}
	public void setDefault_Pay_type(String default_Pay_type) {
		this.default_Pay_type = default_Pay_type;
	}
	public List<Button> getOrderButtonList() {
		return orderButtonList;
	}
	public void setOrderButtonList(List<Button> orderButtonList) {
		this.orderButtonList = orderButtonList;
	}
	public Integer getIsDeleteOrder() {
		return isDeleteOrder;
	}
	public void setIsDeleteOrder(Integer isDeleteOrder) {
		this.isDeleteOrder = isDeleteOrder;
	}
	public String getIsChangeGoods() {
		return isChangeGoods;
	}
	public void setIsChangeGoods(String isChangeGoods) {
		this.isChangeGoods = isChangeGoods;
	}
	public String getCue_words() {
		return cue_words;
	}
	public void setCue_words(String cue_words) {
		this.cue_words = cue_words;
	}
}
