package com.cmall.familyhas.mtmanager.inputresult;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * mt管家产品同步信息输入参数
 * @author pang_jhui
 *
 */
public class SyncProductInfoMTInput extends RootInput {
	

	
	@ZapcomApi(value="查询开始日期",remark = "日期格式：yyyy-MM-dd",require = 1,verify = "base=datetime")
	private String startDate = "";
	
	@ZapcomApi(value="查询结束日期" ,remark = "日期格式：yyyy-MM-dd",require = 1,verify = "base=datetime")
	private String endDate = "";

	/**
	 * 获取查询开始日期
	 * @return
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * 获取查询结束日期
	 * @return
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * 设置查询开始日期
	 * @param startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * 设置查询结束日期
	 * @param endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
	

}
