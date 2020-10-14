package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.model.AddressDetail;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForPurchaseAddressResult extends RootResult {

	@ZapcomApi(value = "地址信息")
	private List<AddressDetail> addressDetailList =  new ArrayList<AddressDetail>();

	public List<AddressDetail> getAddressDetailList() {
		return addressDetailList;
	}

	public void setAddressDetailList(List<AddressDetail> addressDetailList) {
		this.addressDetailList = addressDetailList;
	}
	
	
	
	
}
