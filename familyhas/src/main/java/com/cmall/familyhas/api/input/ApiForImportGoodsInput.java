package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;
/**
 * 进口货列表输入类
 * 
 * @author guz
 *
 */
public class ApiForImportGoodsInput extends RootInput{
	@ZapcomApi(value = "进口货编号" ,demo= "467703130008000100090001",require = 1,verify={ "in=467703130008000100090001" })
	private String activity = "";
	
	@ZapcomApi(value = "用户类型", remark="用户类型" ,demo="4497469400050001:内购会员，4497469400050002:注册会员")
	private String buyerType = "4497469400050002";

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getBuyerType() {
		return buyerType;
	}

	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}
	
	
}
