package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiMyCenterBtnListResult extends RootResult {
	@ZapcomApi(value="个人中心配置信息", remark="移动端按钮信息")
	public List<ApiMyCenterBtnResult> btnList = new ArrayList<ApiMyCenterBtnResult>();
	@ZapcomApi(value="广告信息(旧版)")
	public AdvertisementInfo adverInfo = new AdvertisementInfo();
	@ZapcomApi(value="广告信息(新版)")
	public AdvertInfo advert = new AdvertInfo();
	
	
	
	public AdvertInfo getAdvert() {
		return advert;
	}
	public void setAdvert(AdvertInfo advert) {
		this.advert = advert;
	}
	public AdvertisementInfo getAdverInfo() {
		return adverInfo;
	}
	public void setAdverInfo(AdvertisementInfo adverInfo) {
		this.adverInfo = adverInfo;
	}
	public List<ApiMyCenterBtnResult> getBtnList() {
		return btnList;
	}
	public void setBtnList(List<ApiMyCenterBtnResult> btnList) {
		this.btnList = btnList;
	}
}
