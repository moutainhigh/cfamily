package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.model.PageHuDongInfo;
import com.cmall.familyhas.api.model.PageTemplete;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetTempletePageInfoResult extends RootResult {

	@ZapcomApi(value = "页面标题")
	private String pageTitle = "";

	@ZapcomApi(value = "页面类型")
	private String pageType = "";

	@ZapcomApi(value = "页面编号")
	private String pageNum = "";

	@ZapcomApi(value = "模板列表")
	private List<PageTemplete> tempList = new ArrayList<PageTemplete>();

	@ZapcomApi(value = "分享标题")
	private String shareTitle = "";

	@ZapcomApi(value = "分享图片")
	private String shareImg = "";

	@ZapcomApi(value = "分享内容")
	private String shareContent = "";

	@ZapcomApi(value = "分享连接")
	private String shareLink = "";

	@ZapcomApi(value = "是否可分享")
	private boolean isShare = false;

	@ZapcomApi(value = "专题模板所属项目")
	private String project_ad = "449747880001";
	
	@ZapcomApi(value="主播手机号及ID",remark="key：主播手机号；value：主播ID")
	private Map<String, String> userMobile_MemCode = new HashMap<String, String>();
	
	@ZapcomApi(value="是否关联主播直播")
	private boolean relLive = false;
	
	@ZapcomApi(value="页面监测是否有直播的定时开启时间")
	private String live_job_start_time = "";
	
	@ZapcomApi(value="页面监测是否有直播的定时结束时间")
	private String live_job_end_time = "";
	
	@ZapcomApi(value="系统时间")
	private String sysTime = "";
	
	@ZapcomApi(value="浏览专题送积分活动校验码")
	private String jfSign = "";
	
	@ZapcomApi(value="是否参与浏览专题送积分活动")
	private String isZtHuDong = "N";
	
	@ZapcomApi(value="浏览专题送积分活动信息")
	private PageHuDongInfo huDongInfo;

	@ZapcomApi(value="专题页是否存在需要登录的模板")
	private boolean needLogin = false;
	
	public String getJfSign() {
		return jfSign;
	}

	public void setJfSign(String jfSign) {
		this.jfSign = jfSign;
	}

	public String getIsZtHuDong() {
		return isZtHuDong;
	}

	public void setIsZtHuDong(String isZtHuDong) {
		this.isZtHuDong = isZtHuDong;
	}

	public PageHuDongInfo getHuDongInfo() {
		return huDongInfo;
	}

	public void setHuDongInfo(PageHuDongInfo huDongInfo) {
		this.huDongInfo = huDongInfo;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = pageNum;
	}

	public List<PageTemplete> getTempList() {
		return tempList;
	}

	public void setTempList(List<PageTemplete> tempList) {
		this.tempList = tempList;
	}

	/**
	 * 返回: the shareTitle <br>
	 * 
	 * 时间: 2016-7-26 下午12:06:44
	 */
	public String getShareTitle() {
		return shareTitle;
	}

	/**
	 * 参数: shareTitle the shareTitle to set <br>
	 * 
	 * 时间: 2016-7-26 下午12:06:44
	 */
	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	/**
	 * 返回: the shareImg <br>
	 * 
	 * 时间: 2016-7-26 下午12:06:44
	 */
	public String getShareImg() {
		return shareImg;
	}

	/**
	 * 参数: shareImg the shareImg to set <br>
	 * 
	 * 时间: 2016-7-26 下午12:06:44
	 */
	public void setShareImg(String shareImg) {
		this.shareImg = shareImg;
	}

	/**
	 * 返回: the shareContent <br>
	 * 
	 * 时间: 2016-7-26 下午12:06:44
	 */
	public String getShareContent() {
		return shareContent;
	}

	/**
	 * 参数: shareContent the shareContent to set <br>
	 * 
	 * 时间: 2016-7-26 下午12:06:44
	 */
	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	/**
	 * 返回: the shareLink <br>
	 * 
	 * 时间: 2016-7-26 下午12:06:44
	 */
	public String getShareLink() {
		return shareLink;
	}

	/**
	 * 参数: shareLink the shareLink to set <br>
	 * 
	 * 时间: 2016-7-26 下午12:06:44
	 */
	public void setShareLink(String shareLink) {
		this.shareLink = shareLink;
	}

	/**
	 * 返回: the isShare <br>
	 * 
	 * 时间: 2016-7-26 下午12:09:49
	 */
	public boolean isShare() {
		return isShare;
	}

	/**
	 * 参数: isShare the isShare to set <br>
	 * 
	 * 时间: 2016-7-26 下午12:09:49
	 */
	public void setShare(boolean isShare) {
		this.isShare = isShare;
	}

	/**
	 * 返回: the project_ad <br>
	 * 
	 * 时间: 2016-7-29 下午6:27:25
	 */
	public String getProject_ad() {
		return project_ad;
	}

	/**
	 * 参数: project_ad the project_ad to set <br>
	 * 
	 * 时间: 2016-7-29 下午6:27:25
	 */
	public void setProject_ad(String project_ad) {
		this.project_ad = project_ad;
	}

	/**
	 * 返回: the userMobile_MemCode <br>
	 * 
	 * 时间: 2016-8-2 下午1:09:06
	 */
	public Map<String, String> getUserMobile_MemCode() {
		return userMobile_MemCode;
	}

	/**
	 * 参数: userMobile_MemCode the userMobile_MemCode to set <br>
	 * 
	 * 时间: 2016-8-2 下午1:09:06
	 */
	public void setUserMobile_MemCode(Map<String, String> userMobile_MemCode) {
		this.userMobile_MemCode = userMobile_MemCode;
	}

	/**
	 * 返回: the relLive <br>
	 * 
	 * 时间: 2016-8-2 下午1:31:49
	 */
	public boolean isRelLive() {
		return relLive;
	}

	/**
	 * 参数: relLive the relLive to set <br>
	 * 
	 * 时间: 2016-8-2 下午1:31:49
	 */
	public void setRelLive(boolean relLive) {
		this.relLive = relLive;
	}

	/**
	 * 返回: the live_job_start_time <br>
	 * 
	 * 时间: 2016-8-2 下午4:39:42
	 */
	public String getLive_job_start_time() {
		return live_job_start_time;
	}

	/**
	 * 参数: live_job_start_time the live_job_start_time to set <br>
	 * 
	 * 时间: 2016-8-2 下午4:39:42
	 */
	public void setLive_job_start_time(String live_job_start_time) {
		this.live_job_start_time = live_job_start_time;
	}

	/**
	 * 返回: the live_job_end_time <br>
	 * 
	 * 时间: 2016-8-2 下午4:39:42
	 */
	public String getLive_job_end_time() {
		return live_job_end_time;
	}

	/**
	 * 参数: live_job_end_time the live_job_end_time to set <br>
	 * 
	 * 时间: 2016-8-2 下午4:39:42
	 */
	public void setLive_job_end_time(String live_job_end_time) {
		this.live_job_end_time = live_job_end_time;
	}

	/**
	 * 返回: the sysTime <br>
	 * 
	 * 时间: 2016-8-2 下午4:55:10
	 */
	public String getSysTime() {
		return sysTime;
	}

	/**
	 * 参数: sysTime the sysTime to set <br>
	 * 
	 * 时间: 2016-8-2 下午4:55:10
	 */
	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

	public boolean isNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}
	
}
