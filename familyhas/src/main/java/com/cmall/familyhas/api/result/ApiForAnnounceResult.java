package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.groupcenter.model.PageResults;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 家有惠首页公告
 * @author jlin
 *
 */
public class ApiForAnnounceResult extends RootResultWeb {
	
	@ZapcomApi(value = "公告列表",remark="家有惠首页展示公告列表")
	private List<Announce> announceList = new ArrayList<ApiForAnnounceResult.Announce>();

	@ZapcomApi(value = "翻页结果")
	private PageResults paged = new PageResults();
	
	public List<Announce> getAnnounceList() {
		return announceList;
	}



	public void setAnnounceList(List<Announce> announceList) {
		this.announceList = announceList;
	}


	public PageResults getPaged() {
		return paged;
	}



	public void setPaged(PageResults paged) {
		this.paged = paged;
	}



	public static class Announce {
		@ZapcomApi(value = "公告id",demo="123456465")
		private String id;
		@ZapcomApi(value = "公告标题",demo="热销")
		private String title = "";
		@ZapcomApi(value = "公告内容",remark="快来看伏玟晓主持的家")
		private String content = "";
		@ZapcomApi(value = "更新时间",remark="更新时间")
		private String update_time = "";
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
		public String getUpdate_time() {
			return update_time;
		}
		public void setUpdate_time(String update_time) {
			this.update_time = update_time;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		
	}
	
	
}
