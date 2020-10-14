package com.cmall.familyhas.api.result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.ApiIntegralDetails;
import com.cmall.familyhas.api.model.Button;
import com.cmall.familyhas.api.model.IntegralInfo;
import com.cmall.familyhas.api.model.Reason;
import com.cmall.groupcenter.account.model.ApiHomeOrderTrackingListResult;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiIntegralDetailsResult extends RootResultWeb {
	
	@ZapcomApi(value = "是否最后页Y/N")
	private String pageFlag = "";
	
	@ZapcomApi(value = "总积分")
	private String integralTotal = "";
	
	@ZapcomApi(value = "快过期积分数量")
	private String expireIntegral = "";
	
	@ZapcomApi(value = "总数量")
	private String total = "";
	
	@ZapcomApi(value="积分明细列表",remark="")
	private List<IntegralInfo> list= new ArrayList<IntegralInfo>();

	
	
	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getExpireIntegral() {
		return expireIntegral;
	}

	public void setExpireIntegral(String expireIntegral) {
		this.expireIntegral = expireIntegral;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
	}

	public List<IntegralInfo> getList() {
		return list;
	}

	public void setList(List<IntegralInfo> list) {
		this.list = list;
	}

	public String getIntegralTotal() {
		return integralTotal;
	}

	public void setIntegralTotal(String integralTotal) {
		this.integralTotal = integralTotal;
	}

}
