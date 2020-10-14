package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 首页版式栏目 
 * @author zhaoxq
 *
 */
public class HomeColumnPc extends HomeColumn{

	@ZapcomApi(value = "楼层模板广告1List",remark="中间轮播广告位内容")
	private List<HomeColumnContent> ad1List = new ArrayList<HomeColumnContent>();
	

	@ZapcomApi(value = "楼层模板广告2图片地址",remark="左侧广告位")
	private String ad2picture = "";
	
	@ZapcomApi(value = "楼层模板广告2链接类型")
	private String ad2linktype = "";
	
	@ZapcomApi(value = "楼层模板广告2链接值")
	private String ad2linkvalue = "";
	
	@ZapcomApi(value = "左侧底部品牌位内容List")
	private List<HomeColumnContent> logoList = new ArrayList<HomeColumnContent>();
	
	@ZapcomApi(value = "底部广告位内容List")
	private List<HomeColumnContent> ad3List = new ArrayList<HomeColumnContent>();
	
	@ZapcomApi(value = "右侧热销List")
	private List<HomeColumnContent>  hotPointList = new ArrayList<HomeColumnContent>();

	public List<HomeColumnContent> getAd1List() {
		return ad1List;
	}

	public void setAd1List(List<HomeColumnContent> ad1List) {
		this.ad1List = ad1List;
	}

	public String getAd2picture() {
		return ad2picture;
	}

	public void setAd2picture(String ad2picture) {
		this.ad2picture = ad2picture;
	}

	public String getAd2linktype() {
		return ad2linktype;
	}

	public void setAd2linktype(String ad2linktype) {
		this.ad2linktype = ad2linktype;
	}

	public String getAd2linkvalue() {
		return ad2linkvalue;
	}

	public void setAd2linkvalue(String ad2linkvalue) {
		this.ad2linkvalue = ad2linkvalue;
	}

	public List<HomeColumnContent> getLogoList() {
		return logoList;
	}

	public void setLogoList(List<HomeColumnContent> logoList) {
		this.logoList = logoList;
	}

	public List<HomeColumnContent> getAd3List() {
		return ad3List;
	}

	public void setAd3List(List<HomeColumnContent> ad3List) {
		this.ad3List = ad3List;
	}

	public List<HomeColumnContent> getHotPointList() {
		return hotPointList;
	}

	public void setHotPointList(List<HomeColumnContent> hotPointList) {
		this.hotPointList = hotPointList;
	}
}
