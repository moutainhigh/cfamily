package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiRecodeDistributionInfoInput extends RootInput {
	

	@ZapcomApi(value="分销商品编号",require=1)
	private String distributionProductCode = "";
	
	@ZapcomApi(value="分销人Id",require=1)
	private String distributionMemberId = "";
	
	@ZapcomApi(value="分销商品点击用户Id")
	private String distributionJuniorMemberId = "";

	public String getDistributionProductCode() {
		return distributionProductCode;
	}

	public void setDistributionProductCode(String distributionProductCode) {
		this.distributionProductCode = distributionProductCode;
	}

	public String getDistributionMemberId() {
		return distributionMemberId;
	}

	public void setDistributionMemberId(String distributionMemberId) {
		this.distributionMemberId = distributionMemberId;
	}

	public String getDistributionJuniorMemberId() {
		return distributionJuniorMemberId;
	}

	public void setDistributionJuniorMemberId(String distributionJuniorMemberId) {
		this.distributionJuniorMemberId = distributionJuniorMemberId;
	}
}
