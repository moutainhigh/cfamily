package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 推荐商品信息输入结果
 * @author pang_jhui
 *
 */
public class ApiRecProductInfoInput extends RootInput {
	
	@ZapcomApi(value="渠道编号",remark="推广渠道")
	private String channelNO = "";
	
	@ZapcomApi(value="流水号",remark="设备流水号")
	private String sequenceNO = "";
	
	@ZapcomApi(value="用户唯一标识",remark="百分点系统提供")
	private String uid = "";
	
	@ZapcomApi(value="操作标识标识",remark="商品详情：productdetail、订单详情：orderdetail、猜你喜欢：maybelove、支付成功：paysuccess、购物车页面：shopcart")
	private String operFlag = "";
	
	@ZapcomApi(value = "用户类型", remark="用户类型" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	@ZapcomApi(value="每页展示的商品数",remark="默认每页展示6个")
	private int pageSize = 6;
	
	@ZapcomApi(value="当前页码",remark="默认展示第一页")
	private int pageIndex = 1;
	
	@ZapcomApi(value="当前浏览商品",remark="提高推荐精准性,4月25日要求添加")
	private String iid = "";
	
	@ZapcomApi(value = "导航项编码", remark="编码从首页导航栏接口获取")
	private String navCode = "";
	
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	@ZapcomApi(value = "app版本信息", remark = "app版本信息", demo = "1.0.0")
	private String app_vision = "";
	
	public Integer getIsPurchase() {
		return isPurchase;
	}

	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}

	public String getChannelNO() {
		return channelNO;
	}

	public void setChannelNO(String channelNO) {
		this.channelNO = channelNO;
	}

	public String getSequenceNO() {
		return sequenceNO;
	}

	public void setSequenceNO(String sequenceNO) {
		this.sequenceNO = sequenceNO;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getOperFlag() {
		return operFlag;
	}

	public void setOperFlag(String operFlag) {
		this.operFlag = operFlag;
	}

	public String getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	public String getNavCode() {
		return navCode;
	}

	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}

	public String getApp_vision() {
		return app_vision;
	}

	public void setApp_vision(String app_vision) {
		this.app_vision = app_vision;
	}

}
