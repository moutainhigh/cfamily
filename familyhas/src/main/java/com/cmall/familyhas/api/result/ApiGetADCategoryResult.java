package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetADCategoryResult  extends RootResult{
	
	
	@ZapcomApi(value = "栏目类别编号", demo = "467703130008000100060001")
	public String category_code;
	@ZapcomApi(value = "展示方式",remark="商品详情页(449747030002)或者专题页(449747030001)")
	public String link_address;
	@ZapcomApi(value = "商品编号")
	public String product_code;
	@ZapcomApi(value = "商品名称")
	public String product_name;
	@ZapcomApi(value = "专题页的连接地址")
	public String link_url;
	@ZapcomApi(value = "商品连接标识",remark="例:goods_num:8016408536")
	public String product_link;
	@ZapcomApi(value = "栏目图片")
	public String line_head;
	
	@ZapcomApi(value="是否海外购",remark="0:否，1:是")
    private String flagTheSea = "0" ;
	
	public String getCategory_code() {
		return category_code;
	}
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}
	public String getLink_address() {
		return link_address;
	}
	public void setLink_address(String link_address) {
		this.link_address = link_address;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getLink_url() {
		return link_url;
	}
	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}
	public String getProduct_link() {
		return product_link;
	}
	public void setProduct_link(String product_link) {
		this.product_link = product_link;
	}
	public String getLine_head() {
		return line_head;
	}
	public void setLine_head(String line_head) {
		this.line_head = line_head;
	}
	public String getFlagTheSea() {
		return flagTheSea;
	}
	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}
	
}
