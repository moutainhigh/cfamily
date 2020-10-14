package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Program;
import com.cmall.familyhas.api.model.ProgramRelProduct;
import com.cmall.familyhas.api.model.ShareProgram;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetProgramInfoResult extends RootResult{

	@ZapcomApi(value="最新节目")
	private Program program = new Program();
	
	@ZapcomApi(value="栏目介绍")
	private String categoryDes = "";
	
	@ZapcomApi(value="分享")
	private ShareProgram shareProgram = new ShareProgram();
	
	@ZapcomApi(value="同款推荐",remark="最新两期商品LIST")
	private List<ProgramRelProduct> newrecommend = new ArrayList<ProgramRelProduct>();
	
	@ZapcomApi(value="所有商品")
	private List<ProgramRelProduct> allProducts = new ArrayList<ProgramRelProduct>();
	
	@ZapcomApi(value="往期精彩")
	private List<Program> oldPrograms = new ArrayList<Program>();

	@ZapcomApi(value="是否有更多节目",remark="1001:有更多节目;1000:没有更多的节目")
	private String isMore = "1000";
	
	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public String getCategoryDes() {
		return categoryDes;
	}

	public void setCategoryDes(String categoryDes) {
		this.categoryDes = categoryDes;
	}

	public ShareProgram getShareProgram() {
		return shareProgram;
	}

	public void setShareProgram(ShareProgram shareProgram) {
		this.shareProgram = shareProgram;
	}

	public List<ProgramRelProduct> getNewrecommend() {
		return newrecommend;
	}

	public void setNewrecommend(List<ProgramRelProduct> newrecommend) {
		this.newrecommend = newrecommend;
	}

	public List<ProgramRelProduct> getAllProducts() {
		return allProducts;
	}

	public void setAllProducts(List<ProgramRelProduct> allProducts) {
		this.allProducts = allProducts;
	}

	public List<Program> getOldPrograms() {
		return oldPrograms;
	}

	public void setOldPrograms(List<Program> oldPrograms) {
		this.oldPrograms = oldPrograms;
	}

	public String getIsMore() {
		return isMore;
	}

	public void setIsMore(String isMore) {
		this.isMore = isMore;
	}
	
	
}
