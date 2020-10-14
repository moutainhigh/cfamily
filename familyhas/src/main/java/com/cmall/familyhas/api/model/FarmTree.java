package com.cmall.familyhas.api.model;

import java.math.BigDecimal;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class FarmTree {

	@ZapcomApi(value = "果树唯一编号")
	private String treeCode = "";
	
	@ZapcomApi(value = "果树类型",remark = "柠檬、核桃、红枣、大米")
	private String treeType = "";
	
	@ZapcomApi(value = "果树所处阶段",remark = "幼苗、大树、开花、结果、成熟")
	private String treeStage = "";
	
	@ZapcomApi(value = "果树进度提示语",remark = "再浇*%的雨露***")
	private String treePrompt = "";
	
	@ZapcomApi(value = "剩余贡献度")
	private BigDecimal surplusContribute = new BigDecimal(0);

	public String getTreePrompt() {
		return treePrompt;
	}

	public void setTreePrompt(String treePrompt) {
		this.treePrompt = treePrompt;
	}

	public String getTreeCode() {
		return treeCode;
	}

	public void setTreeCode(String treeCode) {
		this.treeCode = treeCode;
	}

	public String getTreeType() {
		return treeType;
	}

	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}

	public String getTreeStage() {
		return treeStage;
	}

	public void setTreeStage(String treeStage) {
		this.treeStage = treeStage;
	}

	public BigDecimal getSurplusContribute() {
		return surplusContribute;
	}

	public void setSurplusContribute(BigDecimal surplusContribute) {
		this.surplusContribute = surplusContribute;
	}
	
	
}
