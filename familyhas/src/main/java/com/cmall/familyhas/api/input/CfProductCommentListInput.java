package com.cmall.familyhas.api.input;

import com.cmall.groupcenter.model.PageOption;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class CfProductCommentListInput extends RootInput{

	@ZapcomApi(value="商品编号",remark="商品编号",demo="8019404046",require=1)
	private String productCode = "";
	

	@ZapcomApi(value="屏幕宽度",remark="屏幕宽度",demo="500")
	private Integer screenWidth = 0 ;
	
	@ZapcomApi(value="评分类型",remark="评分类型",demo="全部,好评,中评,差评",require=1)
	private String gradeType = "";
	
	@ZapcomApi(value = "翻页选项",remark = "输入起始页码和每页10条" ,demo= "5,10",require = 1)
	private PageOption paging = new PageOption();
	

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public Integer getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(Integer screenWidth) {
		this.screenWidth = screenWidth;
	}

	public String getGradeType() {
		return gradeType;
	}

	public void setGradeType(String gradeType) {
		this.gradeType = gradeType;
	}

	public PageOption getPaging() {
		return paging;
	}

	public void setPaging(PageOption paging) {
		this.paging = paging;
	}
	
	
	
	
}
