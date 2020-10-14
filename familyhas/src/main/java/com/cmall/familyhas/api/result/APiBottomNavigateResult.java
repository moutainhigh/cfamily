package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.NewNavigation;
import com.cmall.familyhas.api.model.RedPackageObj;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class APiBottomNavigateResult extends RootResult {

	@ZapcomApi(value = "红包雨数据")
	private RedPackageObj redPackage ;	
	
	@ZapcomApi(value = "搜索栏右栏广告")
	private RedPackageObj searchRiAdObj;
	


	public RedPackageObj getSearchRiAdObj() {
		return searchRiAdObj;
	}

	public void setSearchRiAdObj(RedPackageObj searchRiAdObj) {
		this.searchRiAdObj = searchRiAdObj;
	}

	public RedPackageObj getRedPackage() {
		return redPackage;
	}

	public void setRedPackage(RedPackageObj redPackage) {
		this.redPackage = redPackage;
	}

	@ZapcomApi(value = "是否更改导航图片", remark="0：不更改（版本号一致的时候返回）1：代表更改为使用默认导航图片（服务器导航图片没有或者不足一组的时候返回） 2：代表更改为使用最新的导航图片（版本号不一致的时候返回）" ,demo="0")
	private String option;
	
	@ZapcomApi(value = "是否更改广告导航图片", remark="3：不更改（版本号一致的时候返回） 4：代表更改为不显示广告导航 5：代表更改为使用最新的导航图片（版本号不一致的时候返回）" ,demo="4")
	private String adOption;
	
	@ZapcomApi(value = "导航列表List", remark="" ,demo="")
	private List<NewNavigation> navigationList = new ArrayList<NewNavigation>();
		
	@ZapcomApi(value ="最新导航版本",remark="最新导航版本")
	private String navigationVersion = "";

	@ZapcomApi(value ="最新广告导航版本",remark="最新广告导航版本")
	private String adNavigationVersion = "";
	

	public List<NewNavigation> getNavigationList() {
		return navigationList;
	}

	public void setNavigationList(List<NewNavigation> navigationList) {
		this.navigationList = navigationList;
	}

	public String getNavigationVersion() {
		return navigationVersion;
	}

	public void setNavigationVersion(String navigationVersion) {
		this.navigationVersion = navigationVersion;
	}

	public String getAdNavigationVersion() {
		return adNavigationVersion;
	}

	public void setAdNavigationVersion(String adNavigationVersion) {
		this.adNavigationVersion = adNavigationVersion;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getAdOption() {
		return adOption;
	}

	public void setAdOption(String adOption) {
		this.adOption = adOption;
	}

}