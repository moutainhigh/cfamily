package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.HomeColumn;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiHomeColumnWeAppResult extends RootResult {

	@ZapcomApi(value = "服务器时间")
	private String sysTime = DateUtil.getSysDateTimeString();
	
	@ZapcomApi(value = "轮播广告模板")
	private HomeColumn banner;
	
	@ZapcomApi(value = "小程序闪购模板")
	private HomeColumn fastBuy;
	
	@ZapcomApi(value = "小程序闪购横滑模板")
	private HomeColumn fastBuyHh;
	
	@ZapcomApi(value = "TV直播横滑")
	private HomeColumn TVLiveH;
	
	@ZapcomApi(value = "TV直播")
	private HomeColumn TVLive;
	
	@ZapcomApi(value = "视频模板数组")
	private List<HomeColumn> videoList;
	
	@ZapcomApi(value = "一栏广告模板数组")
	private List<HomeColumn> oneAdverList;
	
	@ZapcomApi(value = "商品推荐模板数组")
	private List<HomeColumn> recommdList;
	
	@ZapcomApi(value = "三栏两行模板数组")
	private List<HomeColumn> threeTwoList;
	
	@ZapcomApi(value = "两栏两行模板数组")
	private List<HomeColumn> twoTwoList;
	
	@ZapcomApi(value = "秒杀模板")
	private HomeColumn secondBuy;
	
	@ZapcomApi(value = "拼团包邮")
	private HomeColumn groupBuyShip;
	
	@ZapcomApi(value = "一栏多行模版")
	private List<HomeColumn> oneColMoreRowList = new ArrayList<HomeColumn>();
	
	@ZapcomApi(value = "商品评价")
	private HomeColumn prodEvaluation;

	@ZapcomApi(value = "分销模板")
	private HomeColumn fenxiao;
	
	@ZapcomApi(value = "买家秀列表")
	private HomeColumn buyerShowList;
	
	@ZapcomApi(value = "买家秀入口样式1")
	private HomeColumn buyerShowEnter1;
	
	@ZapcomApi(value = "买家秀入口样式2")
	private HomeColumn buyerShowEnter2;
	
	public HomeColumn getBuyerShowEnter1() {
		return buyerShowEnter1;
	}
	public void setBuyerShowEnter1(HomeColumn buyerShowEnter1) {
		this.buyerShowEnter1 = buyerShowEnter1;
	}
	public HomeColumn getBuyerShowEnter2() {
		return buyerShowEnter2;
	}
	public void setBuyerShowEnter2(HomeColumn buyerShowEnter2) {
		this.buyerShowEnter2 = buyerShowEnter2;
	}
	public HomeColumn getBuyerShowList() {
		return buyerShowList;
	}
	public void setBuyerShowList(HomeColumn buyerShowList) {
		this.buyerShowList = buyerShowList;
	}
	public HomeColumn getFenxiao() {
		return fenxiao;
	}
	public void setFenxiao(HomeColumn fenxiao) {
		this.fenxiao = fenxiao;
	}
	
	public HomeColumn getProdEvaluation() {
		return prodEvaluation;
	}
	public void setProdEvaluation(HomeColumn prodEvaluation) {
		this.prodEvaluation = prodEvaluation;
	}
	public HomeColumn getFastBuyHh() {
		return fastBuyHh;
	}
	public void setFastBuyHh(HomeColumn fastBuyHh) {
		this.fastBuyHh = fastBuyHh;
	}
	public List<HomeColumn> getOneAdverList() {
		return oneAdverList;
	}
	public void setOneAdverList(List<HomeColumn> oneAdverList) {
		this.oneAdverList = oneAdverList;
	}
	public List<HomeColumn> getRecommdList() {
		return recommdList;
	}
	public void setRecommdList(List<HomeColumn> recommdList) {
		this.recommdList = recommdList;
	}
	public List<HomeColumn> getThreeTwoList() {
		return threeTwoList;
	}
	public void setThreeTwoList(List<HomeColumn> threeTwoList) {
		this.threeTwoList = threeTwoList;
	}
	public List<HomeColumn> getTwoTwoList() {
		return twoTwoList;
	}
	public void setTwoTwoList(List<HomeColumn> twoTwoList) {
		this.twoTwoList = twoTwoList;
	}
	public HomeColumn getSecondBuy() {
		return secondBuy;
	}
	public void setSecondBuy(HomeColumn secondBuy) {
		this.secondBuy = secondBuy;
	}
	public HomeColumn getGroupBuyShip() {
		return groupBuyShip;
	}
	public void setGroupBuyShip(HomeColumn groupBuyShip) {
		this.groupBuyShip = groupBuyShip;
	}
	
	public String getSysTime() {
		return sysTime;
	}
	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	public HomeColumn getBanner() {
		return banner;
	}
	public void setBanner(HomeColumn banner) {
		this.banner = banner;
	}

	public HomeColumn getFastBuy() {
		return fastBuy;
	}
	public void setFastBuy(HomeColumn fastBuy) {
		this.fastBuy = fastBuy;
	}

	public HomeColumn getTVLiveH() {
		return TVLiveH;
	}
	public void setTVLiveH(HomeColumn tVLiveH) {
		TVLiveH = tVLiveH;
	}

	public HomeColumn getTVLive() {
		return TVLive;
	}
	public void setTVLive(HomeColumn tVLive) {
		TVLive = tVLive;
	}

	public List<HomeColumn> getVideoList() {
		return videoList;
	}
	public void setVideoList(List<HomeColumn> videoList) {
		this.videoList = videoList;
	}
	public List<HomeColumn> getOneColMoreRowList() {
		return oneColMoreRowList;
	}
	public void setOneColMoreRowList(List<HomeColumn> oneColMoreRowList) {
		this.oneColMoreRowList = oneColMoreRowList;
	}
	
}