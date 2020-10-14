package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.PpcInfo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiPpcDetailsResult extends RootResultWeb {
	
	@ZapcomApi(value = "是否最后页Y/N")
	private String pageFlag = "";
	
	@ZapcomApi(value = "总储值金")
	private String integralTotal = "";
	
	@ZapcomApi(value="储值金明细列表",remark="")
	private List<PpcInfo> list= new ArrayList<PpcInfo>();
	
	@ZapcomApi(value = "总数量")
	private String total = "";
	
	

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

	public List<PpcInfo> getList() {
		return list;
	}

	public void setList(List<PpcInfo> list) {
		this.list = list;
	}

	public String getIntegralTotal() {
		return integralTotal;
	}

	public void setIntegralTotal(String integralTotal) {
		this.integralTotal = integralTotal;
	}

}
