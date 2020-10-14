package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.CrdtInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiCrdtDetailsResult extends RootResultWeb {
	
	@ZapcomApi(value = "是否最后页Y/N")
	private String pageFlag = "";
	
	@ZapcomApi(value = "总暂存款")
	private String integralTotal = "";
	
	@ZapcomApi(value="储值金明细列表",remark="")
	private List<CrdtInfo> list= new ArrayList<CrdtInfo>();
	
	@ZapcomApi(value = "总数量")
	private String total = "0";
	
	

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}

	public List<CrdtInfo> getList() {
		return list;
	}

	public void setList(List<CrdtInfo> list) {
		this.list = list;
	}

	public String getIntegralTotal() {
		return integralTotal;
	}

	public void setIntegralTotal(String integralTotal) {
		this.integralTotal = integralTotal;
	}

}
