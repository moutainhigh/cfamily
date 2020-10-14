package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.FarmSign;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForFarmSignResult extends RootResult {

	@ZapcomApi(value = "签到提示语")
	private String signPrompt = "";
	
	@ZapcomApi(value = "今日是否可签",remark = "0否 ; 1是")
	private String signFlag = "0";
	
	@ZapcomApi(value = "签到信息")
	private List<FarmSign> farmSignList = new ArrayList<FarmSign>();
	
	@ZapcomApi(value = "已签到几天")
	private int signNum = 0;

	public List<FarmSign> getFarmSignList() {
		return farmSignList;
	}

	public void setFarmSignList(List<FarmSign> farmSignList) {
		this.farmSignList = farmSignList;
	}

	public int getSignNum() {
		return signNum;
	}

	public void setSignNum(int signNum) {
		this.signNum = signNum;
	}

	public String getSignPrompt() {
		return signPrompt;
	}

	public void setSignPrompt(String signPrompt) {
		this.signPrompt = signPrompt;
	}

	public String getSignFlag() {
		return signFlag;
	}

	public void setSignFlag(String signFlag) {
		this.signFlag = signFlag;
	}


}
