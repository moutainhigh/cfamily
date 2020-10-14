package com.cmall.familyhas.api.result;

import java.util.LinkedList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 家有汇首页底部的友情连接
 * @author jlin
 *
 */
public class ApiForFriendLinksResult extends RootResultWeb {
	
	@ZapcomApi(value = "三级信息")
	private List<Mess2> messList=new LinkedList<Mess2>();
	
	public static class Mess2 {
		
		@ZapcomApi(value = "信息标题")
		private String category_note;
		
		@ZapcomApi(value = "详情URL")
		private String detail_url="";


		public String getCategory_note() {
			return category_note;
		}

		public void setCategory_note(String category_note) {
			this.category_note = category_note;
		}

		public String getDetail_url() {
			return detail_url;
		}

		public void setDetail_url(String detail_url) {
			this.detail_url = detail_url;
		}
	}
	

	public List<Mess2> getMessList() {
		return messList;
	}

	public void setMessList(List<Mess2> messList) {
		this.messList = messList;
	}
	
}
