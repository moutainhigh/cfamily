package com.cmall.familyhas.api.input;


import com.cmall.familyhas.api.model.PageOption;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;
/**
 * 商品列表输入类
 * @author houwen
 * date 2014-8-28
 * @version 1.0
 */
public class ApiGetSkuForSellerCategoryInput extends RootInput {
	
	@ZapcomApi(value = "翻页选项",remark = "输入起始页码和每页10条" ,demo= "5,10",require = 1)
	private PageOption paging = new PageOption();
	
	@ZapcomApi(value = "按分类",remark = "按分类可选" ,demo= "到商品分类接口中查询分类编号")
	private String category="";
	
	@ZapcomApi(value = "排序规则",remark = "默认=449746820001   销量=449746820002" ,demo= "449746820001")
	private String sort="";

	public PageOption getPaging() {
		return paging;
	}

	public void setPaging(PageOption paging) {
		this.paging = paging;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	
	
}
