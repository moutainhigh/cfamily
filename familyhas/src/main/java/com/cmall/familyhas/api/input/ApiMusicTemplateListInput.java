package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.topapi.RootInput;

public class ApiMusicTemplateListInput extends RootInput {
	
	    	private String page_type;
			private String template_type_id;
	    	private int nextPage;
	    	private int pageSize=6;
	    	
	    	public int getPageSize() {
				return pageSize;
			}
			public void setPageSize(int pageSize) {
				this.pageSize = pageSize;
			}
			public String getPage_type() {
				return page_type;
			}
			public void setPage_type(String page_type) {
				this.page_type = page_type;
			}
			public String getTemplate_type_id() {
				return template_type_id;
			}
			public void setTemplate_type_id(String template_type_id) {
				this.template_type_id = template_type_id;
			}
			public int getNextPage() {
				return nextPage;
			}
			public void setNextPage(int nextPage) {
				this.nextPage = nextPage;
			}

}
