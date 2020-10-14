package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForAfterSaleInfoResult extends RootResult {

	@ZapcomApi(value = "售后单号", require = 1, demo = "RGR160303100002")
	private List<AfterSale> AfterSaleList = new ArrayList<AfterSale>();

	public static class AfterSale {
		
		@ZapcomApi(value = "背景颜色 ", require = 1, demo = "1", remark = "1 红色 2 黄色 3 白色")
		private String bgColor = "";

		@ZapcomApi(value = "身份标识 ", require = 1, demo = "4497477800070001", remark = "4497477800070001 客服   4497477800070002 用户")
		private String identity = "";

		@ZapcomApi(value = "时间", require = 1, demo = "2014-02-19 11:09:19", remark = "创建时间")
		private String time = "";

		@ZapcomApi(value = "标题", require = 1, demo = "客服同意申请")
		private String title = "";
		
		@ZapcomApi(value = "内容", require = 1, demo = "退货失败！拒绝说明：xxxxxxxxxx")
		private String content = "";

		@ZapcomApi(value = "凭证图片标题", require = 1, demo = "凭证")
		private String picTitle = "";

		@ZapcomApi(value = "凭证图片Url", require = 0, demo = "[]")
		private List<String> picUrl = new ArrayList<String>();

		@ZapcomApi(value = "状态类型 ", require = 1, demo = "002", remark = "待完善物流信息：002   其他 001")
		private String statusType = "001";

		public String getBgColor() {
			return bgColor;
		}

		public void setBgColor(String bgColor) {
			this.bgColor = bgColor;
		}

		public String getIdentity() {
			return identity;
		}

		public void setIdentity(String identity) {
			this.identity = identity;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getPicTitle() {
			return picTitle;
		}

		public void setPicTitle(String picTitle) {
			this.picTitle = picTitle;
		}

		public String getStatusType() {
			return statusType;
		}

		public void setStatusType(String statusType) {
			this.statusType = statusType;
		}

		public List<String> getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(List<String> picUrl) {
			this.picUrl = picUrl;
		}

		
	}

	public List<AfterSale> getAfterSaleList() {
		return AfterSaleList;
	}

	public void setAfterSaleList(List<AfterSale> afterSaleList) {
		AfterSaleList = afterSaleList;
	}

}
