package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.TodayProduct;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForGetTvFormInfoResult extends RootResultWeb {

	@ZapcomApi(value = "节目列表")
	private List<ApiForGetTvFormInfoResult.TvForm> list = new ArrayList<ApiForGetTvFormInfoResult.TvForm>();
	
	@ZapcomApi(value = "TV直播链接")
	private String videoUrlTV = "";
	
	public List<ApiForGetTvFormInfoResult.TvForm> getList() {
		return list;
	}

	public void setList(List<ApiForGetTvFormInfoResult.TvForm> list) {
		this.list = list;
	}

	public String getVideoUrlTV() {
		return videoUrlTV;
	}

	public void setVideoUrlTV(String videoUrlTV) {
		this.videoUrlTV = videoUrlTV;
	}

	public static class TvForm {
		@ZapcomApi(value="节目名称")
		private String title = "";
		@ZapcomApi(value="节目id")
		private String formId = "";
		@ZapcomApi(value="开始时间")
		private String startDate = "";
		@ZapcomApi(value="结束时间")
		private String endDate = "";
		@ZapcomApi(value="节目商品")
		private List<TodayProduct> productList = new ArrayList<TodayProduct>();
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getFormId() {
			return formId;
		}
		public void setFormId(String formId) {
			this.formId = formId;
		}
		public String getStartDate() {
			return startDate;
		}
		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}
		public String getEndDate() {
			return endDate;
		}
		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}
		public List<TodayProduct> getProductList() {
			return productList;
		}
		public void setProductList(List<TodayProduct> productList) {
			this.productList = productList;
		}
	}
	
}
