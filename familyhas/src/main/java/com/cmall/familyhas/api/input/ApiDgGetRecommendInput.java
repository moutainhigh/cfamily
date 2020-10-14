package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiDgGetRecommendInput extends RootInput {
	@ZapcomApi(value="导航项编码", remark="编码从首页导航栏接口获取")
	private String navCode = "";
	@ZapcomApi(value="推荐类型", remark="推荐类型,个性化推荐:1(需要用户登陆);相关推荐:2;热门推荐:3,看了又看:4,LD短信支付广告推荐位：5")
	private String recommendType = "3";
	@ZapcomApi(value="当前页码", remark="默认展示第一页")
	private int pageIndex = 1;
	@ZapcomApi(value="每页展示商品数", remark="默认展示每页展示10个商品")
	private int pageSize = 10;
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	@ZapcomApi(value="商品编号,仅相关推荐时需要传此参数")
	private String productCode = "";
	@ZapcomApi(value="app唯一标示,仅app端需要传参")
	private String imei = "";
	@ZapcomApi(value="app不需要传参,针对微信商城参数")
	private String cId = "";
	@ZapcomApi(value="区分前端位置",remark="不同位置显示条数不同(maybelove:猜你喜欢，shopcart：购物车，productdetail：商品详情，paysuccess：支付成功，secondKillListVC：秒杀列表，MessageVC：消息列表，home_cf_sale:看了又看)")
	private String operFlag = "";
	
	@ZapcomApi(value="手机号码",remark="当入参recommendType = 5 时，此参数有参考依据，需要传入数据 ")
	private String phone = "";
	
	public String getNavCode() {
		return navCode;
	}
	public void setNavCode(String navCode) {
		this.navCode = navCode;
	}
	
	public String getRecommendType() {
		return recommendType;
	}
	public void setRecommendType(String recommendType) {
		this.recommendType = recommendType;
	}
	
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public Integer getIsPurchase() {
		return isPurchase;
	}
	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	
	public String getOperFlag() {
		return operFlag;
	}
	public void setOperFlag(String operFlag) {
		this.operFlag = operFlag;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
