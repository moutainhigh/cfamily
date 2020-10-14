package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForNoticeResult extends RootResult {

	@ZapcomApi(value = "通知列表", require = 1)
	private List<Notice> noticeList = new ArrayList<Notice>();
	
	
	public List<Notice> getNoticeList() {
		return noticeList;
	}

	public void setNoticeList(List<Notice> noticeList) {
		this.noticeList = noticeList;
	}
	
	public static class Notice {

		@ZapcomApi(value = "显示位置", remark = "4497477800010001-申请售后页", require = 1, demo = "4497477800010001")
		private String notice_show_place = "";

		@ZapcomApi(value = "通知内容", require = 1)
		private String notice_content = "";

		public String getNotice_show_place() {
			return notice_show_place;
		}

		public void setNotice_show_place(String notice_show_place) {
			this.notice_show_place = notice_show_place;
		}

		public String getNotice_content() {
			return notice_content;
		}

		public void setNotice_content(String notice_content) {
			this.notice_content = notice_content;
		}

		public Notice(String notice_show_place, String notice_content) {
			super();
			this.notice_show_place = notice_show_place;
			this.notice_content = notice_content;
		}

		public Notice() {
			super();
		}

	}

}
