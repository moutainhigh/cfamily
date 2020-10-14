package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	活动
*    xiegj
*/
public class Activity  {
	@ZapcomApi(value = "活动名称", remark = "活动名称", demo = "闪购")
	private String activity_name = "";
	
	@ZapcomApi(value = "活动描述", remark = "活动描述", demo = "打八折")
	private String activity_info = "";

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		this.activity_name = activity_name;
	}

	public String getActivity_info() {
		return activity_info;
	}

	public void setActivity_info(String activity_info) {
		this.activity_info = activity_info;
	}

}

