package com.cmall.familyhas.api.result;

import com.alibaba.fastjson.annotation.JSONField;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;
/**
 * 
 * @author fq
 *
 */
public class ApiRecommendedFrPageInfoResult extends RootResult{

	@ZapcomApi(value="页面标题")
	private String pageTitle = "";
	
	@ZapcomApi(value="广告图连接")
	private String imgUrl = "";
	
	@ZapcomApi(value="二维码连接" ,remark="二维码图片的连接")
	private String qrcodeLink = "";
	
	@ZapcomApi(value="规则标题")
	private String ruleTitle = "";
	
	@ZapcomApi(value="规则信息")
	private String recommendRuleInfo = "";
	
	@ZapcomApi(value="分享图片")
	private String sharePic = "";
	
	@ZapcomApi(value="分享内容")
	private String shareContent = "";
	
	@ZapcomApi(value="分享标题")
	private String shareTitle = "";
	
	@ZapcomApi(value="分享连接")
	private String shareLink = "";
	
	@ZapcomApi(value="分享优惠信息")
	private String rntNum = "";

	public String getRntNum() {
		return rntNum;
	}

	public void setRntNum(String rntNum) {
		this.rntNum = rntNum;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@JSONField(name="qrcodeLink")
	public String getQRcodeLink() {
		return qrcodeLink;
	}

	public void setQRcodeLink(String qRcodeLink) {
		qrcodeLink = qRcodeLink;
	}

	public String getRuleTitle() {
		return ruleTitle;
	}

	public void setRuleTitle(String ruleTitle) {
		this.ruleTitle = ruleTitle;
	}

	public String getRecommendRuleInfo() {
		return recommendRuleInfo;
	}

	public void setRecommendRuleInfo(String recommendRuleInfo) {
		this.recommendRuleInfo = recommendRuleInfo;
	}

	public String getSharePic() {
		return sharePic;
	}

	public void setSharePic(String sharePic) {
		this.sharePic = sharePic;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareLink() {
		return shareLink;
	}

	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	
	

}
