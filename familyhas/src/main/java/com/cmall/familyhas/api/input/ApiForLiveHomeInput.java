package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;
/**
 * 生活家输入类
 * 
 * @author guz
 *
 */
public class ApiForLiveHomeInput extends RootInput{

	@ZapcomApi(value = "生活家编号" ,demo= "467703130008000100050001",require = 1,verify={ "in=467703130008000100050001" })
	private String activity = "";

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
}
