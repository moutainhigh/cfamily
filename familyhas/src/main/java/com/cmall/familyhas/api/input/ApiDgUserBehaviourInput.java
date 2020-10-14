package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.DgUserBehaviourModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiDgUserBehaviourInput extends RootInput {
	@ZapcomApi(value="达观用户行为集合")
	private List<DgUserBehaviourModel> modelList = new ArrayList<DgUserBehaviourModel>();
	@ZapcomApi(value="app唯一标示,仅app端需要传参")
	private String imei = "";
	@ZapcomApi(value="app不需要传参,针对微信商城参数")
	private String cId = "";
	
	public List<DgUserBehaviourModel> getModelList() {
		return modelList;
	}
	public void setModelList(List<DgUserBehaviourModel> modelList) {
		this.modelList = modelList;
	}
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
}
