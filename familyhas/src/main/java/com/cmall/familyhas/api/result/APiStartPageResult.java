package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.StartupPageJump;
import com.cmall.groupcenter.model.UpdateEntity;
import com.cmall.productcenter.model.NavigationVersion;
import com.cmall.systemcenter.model.AppNavigation;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;

public class APiStartPageResult extends RootResult {

	@ZapcomApi(value = "启动页图片序号", remark="启动页图片序号" ,demo="XH0001")
	private String picNm = "";
	
	@ZapcomApi(value = "启动页图片地址", remark="启动页图片地址" ,demo="XH0001")
	private String picUrl = "";
	
	@ZapcomApi(value = "启动页图片标识", remark="0：不更新；1：更新；2：删除" ,demo="XH0001")
	private String picType = "";
	
	@ZapcomApi(value = "操作流水号", remark="操作流水号" ,demo="LSH0001")
	private String sqNum = "";
	
	@ZapcomApi(value = "用户类型", remark="用户类型" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";
	
	@ZapcomApi(value = "更新数据List", remark=" updateValue:1：更新；0：不更新" ,demo="updateKey:11,updateValue:22")
	private List<UpdateEntity> list = new ArrayList<UpdateEntity>();

	@ZapcomApi(value = "keyList", remark="key名称:听云key:nbsAppAgentKey ,微信开发平台注册应用的AppID:wechatAppId,微信Secret:wechatAppSecret,开发者在QQ互联申请的APP ID:qqAppId,开发者在QQ互联申请的APP kEY:qqAppKey,百度推送key:baiduPushKeyAndroid,百度推送key:baiduPushKeyIos,APP应用对应的友盟上的key:umKeyAndroid,APP应用对应的友盟上的key:umKeyIos" ,demo="nbsAppAgentKey:4h5u435y4ui,wechatAppId:34fg53y4534")
	private MDataMap keys = new MDataMap();
	
	@ZapcomApi(value = "app里面所用电话号码", remark="400-123456" ,demo="4008678210")
	private String serviceTel = "";
	
	@ZapcomApi(value = "商品详情页的‘电话订购’字样", remark="电话订购" ,demo="电话订购")
	private String telOrdering = "";
	
	@ZapcomApi(value = "跳转实体", remark="跳转实体" ,demo="跳转实体")
	private StartupPageJump pageJump = new StartupPageJump();
	
	@ZapcomApi(value ="导航图片版本号")
	private NavigationVersion navigationVersion = new NavigationVersion();
	
	@ZapcomApi(value ="导航维护集合")
	private List<AppNavigation> appNavigations = new ArrayList<AppNavigation>();;
	
	@ZapcomApi(value="头像后面背景图片的url")
	private String backgroundPic = "";
	
	@ZapcomApi(value="优惠券列表免责声明")
	private String couponDisclaimer;
	
	@ZapcomApi(value="在线客服是否在合作中")
	private String isSupport;
	
	@ZapcomApi(value="客户端消息是否开启：Y 开启  N 未开启")
	private String is_flag="";
	
	@ZapcomApi(value = "webView使用标记：0：旧的 1：新的", remark="webView使用标记：0：旧的 1：新的" ,demo="0")
	private String webViewFlag = "1";
	
	@ZapcomApi(value = "直播互动链接地址")
	private String liveInteractionLink = "";
	

	public String getLiveInteractionLink() {
		return liveInteractionLink;
	}

	public void setLiveInteractionLink(String liveInteractionLink) {
		this.liveInteractionLink = liveInteractionLink;
	}

	public String getWebViewFlag() {
		return webViewFlag;
	}

	public void setWebViewFlag(String webViewFlag) {
		this.webViewFlag = webViewFlag;
	}

	public String getIs_flag() {
		return is_flag;
	}

	public void setIs_flag(String is_flag) {
		this.is_flag = is_flag;
	}

	public List<AppNavigation> getAppNavigations() {
		return appNavigations;
	}

	public void setAppNavigations(List<AppNavigation> appNavigations) {
		this.appNavigations = appNavigations;
	}

	public MDataMap getKeys() {
		return keys;
	}

	public void setKeys(MDataMap keys) {
		this.keys = keys;
	}

	public String getPicNm() {
		return picNm;
	}

	public void setPicNm(String picNm) {
		this.picNm = picNm;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPicType() {
		return picType;
	}

	public void setPicType(String picType) {
		this.picType = picType;
	}

	public String getSqNum() {
		return sqNum;
	}

	public void setSqNum(String sqNum) {
		this.sqNum = sqNum;
	}

	public List<UpdateEntity> getList() {
		return list;
	}

	public void setList(List<UpdateEntity> list) {
		this.list = list;
	}

	public String getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}

	public String getServiceTel() {
		return serviceTel;
	}

	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}

	public String getTelOrdering() {
		return telOrdering;
	}

	public void setTelOrdering(String telOrdering) {
		this.telOrdering = telOrdering;
	}

	public StartupPageJump getPageJump() {
		return pageJump;
	}

	public void setPageJump(StartupPageJump pageJump) {
		this.pageJump = pageJump;
	}

	public NavigationVersion getNavigationVersion() {
		return navigationVersion;
	}

	public void setNavigationVersion(NavigationVersion navigationVersion) {
		this.navigationVersion = navigationVersion;
	}

	public String getBackgroundPic() {
		return backgroundPic;
	}

	public void setBackgroundPic(String backgroundPic) {
		this.backgroundPic = backgroundPic;
	}

	public String getCouponDisclaimer() {
		return couponDisclaimer;
	}

	public void setCouponDisclaimer(String couponDisclaimer) {
		this.couponDisclaimer = couponDisclaimer;
	}

	public String getIsSupport() {
		return isSupport;
	}

	public void setIsSupport(String isSupport) {
		this.isSupport = isSupport;
	}
	
	
}















