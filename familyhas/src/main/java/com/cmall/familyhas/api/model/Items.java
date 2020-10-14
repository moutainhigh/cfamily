package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class Items {

	@ZapcomApi(value = "图片地址", demo = "http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/22676/2bf6697a4c1a4536bcec0b0c417a7356.jpg")
	private String img_url = "";

	@ZapcomApi(value = "图片名称", demo = "让子弹一会")
	private String item_name = "";

	@ZapcomApi(value = "链接地址", demo = "www.baidu.com")
	private String link = "";

	@ZapcomApi(value = "分享信息")
	private ShareResult share_info = new ShareResult();

	@ZapcomApi(value = "专题id")
	private String infoCode = "";

	@ZapcomApi(value = "开始时间", demo = "2015-03-04 08:09:07")
	private String upTime = "";

	@ZapcomApi(value = "结束时间", demo = "2015-03-04 08:09:07")
	private String downTime = "";
	
	@ZapcomApi(value = "折扣")
	private String discount = "";
	
	@ZapcomApi(value = "是否分享", remark = "449747110001:否,449747110002:是")
	private String shareFlag = "";

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public ShareResult getShare_info() {
		return share_info;
	}

	public void setShare_info(ShareResult share_info) {
		this.share_info = share_info;
	}

	public String getInfoCode() {
		return infoCode;
	}

	public void setInfoCode(String infoCode) {
		this.infoCode = infoCode;
	}

	public String getDownTime() {
		return downTime;
	}

	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}

	public String getUpTime() {
		return upTime;
	}

	public void setUpTime(String upTime) {
		this.upTime = upTime;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getShareFlag() {
		return shareFlag;
	}

	public void setShareFlag(String shareFlag) {
		this.shareFlag = shareFlag;
	}

}
