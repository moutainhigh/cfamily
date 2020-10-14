package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class DgUserBehaviourModel {
	@ZapcomApi(value="商品编码", require=0, remark="除去搜索行为外,此字段必填")
	private String productCode = "";
	@ZapcomApi(value="行为数量")
	private int actionNum = 1;
	@ZapcomApi(value="用户行为", require=1, remark="用户行为;view:点击进入物品详情页;play:播放视频;rec_click:点击达观推荐结果;rec_show:用户浏览过达观推荐结果;collect:收藏(针对商品);"
			+ "comment:评论;share:分享;cart:加入购物车;buy:购买;search:搜索(输入框搜索);")
	private String userAction = "";
	@ZapcomApi(value="如果此条行为是由达观推荐带来的，则此字段对应调达观推荐接口返回itemid所带的request_id")
	private String dgRequestId = "";
	@ZapcomApi(value="针对搜索时,搜索的内容")
	private String searchTxt = "";
	@ZapcomApi(value="浏览商品详情预览时长")
	private String  playTime = "0";

	public String getPlayTime() {
		return playTime;
	}
	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	public int getActionNum() {
		return actionNum;
	}
	public void setActionNum(int actionNum) {
		this.actionNum = actionNum;
	}
	
	public String getUserAction() {
		return userAction;
	}
	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}
	
	public String getDgRequestId() {
		return dgRequestId;
	}
	public void setDgRequestId(String dgRequestId) {
		this.dgRequestId = dgRequestId;
	}
	
	public String getSearchTxt() {
		return searchTxt;
	}
	public void setSearchTxt(String searchTxt) {
		this.searchTxt = searchTxt;
	}
}
