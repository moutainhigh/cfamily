package com.cmall.familyhas.api.input;

import com.cmall.familyhas.api.model.PageOption;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiUserInfoQueryFromHomeInput extends RootInput {
	
	@ZapcomApi(value = "翻页选项", remark = "输入起始页码和每页10条", demo = "5,10", require = 1)
	private PageOption paging = new PageOption();

	@ZapcomApi(value = "查询类型", remark = "0：用户积分、1：用户礼金卷、2：用户储值金、3：用户暂存款", demo = "0", require = 1, verify = { "in=0,1,2,3" })
	private String type = "0";

	@ZapcomApi(value = "查询年份", remark = "查询类型为积分、储值金、暂存款时使用，如果是空则查询所有，非空刚查询相应年份数据,如果为 XXXX年以前，请输入 -XXXX", demo = "2014") 
	private String time = "";

	@ZapcomApi(value = "礼金卷状态", remark = "查询类型为礼金卷时使用，0：未用、1：已用、2：失效", demo = "0", verify = { "in=0,1,2" })
	private String status = "0";

	public PageOption getPaging() {
		return paging;
	}

	public void setPaging(PageOption paging) {
		this.paging = paging;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
