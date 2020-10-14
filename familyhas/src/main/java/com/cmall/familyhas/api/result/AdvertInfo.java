package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class AdvertInfo {

	@ZapcomApi(value="广告入口类型",require=1,demo="ADTP001",remark="ADTP001:个人中心 ,ADTP002:支付成功")
	private String adverEntrType = "";
	@ZapcomApi(value="栏目数",remark="数字1-4")
	private String programa_num = "";
	@ZapcomApi(value="具体栏目详细信息")
	List<AdvertiseColumnmentInfo> advertiseColumnmentInfos = new ArrayList<AdvertiseColumnmentInfo>();
	public String getAdverEntrType() {
		return adverEntrType;
	}
	public void setAdverEntrType(String adverEntrType) {
		this.adverEntrType = adverEntrType;
	}

	public String getPrograma_num() {
		return programa_num;
	}
	public void setPrograma_num(String programa_num) {
		this.programa_num = programa_num;
	}
	public List<AdvertiseColumnmentInfo> getAdvertiseColumnmentInfos() {
		return advertiseColumnmentInfos;
	}
	public void setAdvertiseColumnmentInfos(List<AdvertiseColumnmentInfo> advertiseColumnmentInfos) {
		this.advertiseColumnmentInfos = advertiseColumnmentInfos;
	}
	
	

}