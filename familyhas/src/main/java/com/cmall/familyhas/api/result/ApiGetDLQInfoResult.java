package com.cmall.familyhas.api.result;

import java.util.List;
import java.util.ArrayList;

import com.cmall.familyhas.api.model.DLQpicListModel;
import com.cmall.familyhas.api.model.DLQpicture;
import com.cmall.familyhas.api.model.DLQshare;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 
 * @author fq
 *
 */
public class ApiGetDLQInfoResult  extends RootResult { 

	@ZapcomApi(value="分享信息")
	private DLQshare share_info = new DLQshare();
	
	@ZapcomApi(value="轮播广告")
	private List<DLQpicture> picList = new ArrayList<DLQpicture>();
	
	@ZapcomApi(value="内容列表")
	private List<DLQpicListModel> conntentList = new ArrayList<DLQpicListModel>();
	
	@ZapcomApi(value="电视台编号")
	private String tv_number = "";
	
	@ZapcomApi(value="渠道类型")
	private String p_type = "";
	
	@ZapcomApi(value="全集页面标题")
	private String page_title = "";

	public DLQshare getShare_info() {
		return share_info;
	}

	public void setShare_info(DLQshare share_info) {
		this.share_info = share_info;
	}

	public List<DLQpicture> getPicList() {
		return picList;
	}

	public void setPicList(List<DLQpicture> picList) {
		this.picList = picList;
	}

	public List<DLQpicListModel> getConntentList() {
		return conntentList;
	}

	public void setConntentList(List<DLQpicListModel> conntentList) {
		this.conntentList = conntentList;
	}

	public String getTv_number() {
		return tv_number;
	}

	public void setTv_number(String tv_number) {
		this.tv_number = tv_number;
	}

	public String getP_type() {
		return p_type;
	}

	public void setP_type(String p_type) {
		this.p_type = p_type;
	}

	public String getPage_title() {
		return page_title;
	}

	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}
	
	
}
