package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.ProductMaybeLove;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;


/**
 * 可能喜欢的商品输出类
 * @author liqt
 *
 */
public class ApiForMaybeLoveResult extends RootResultWeb {
	
	@ZapcomApi(value="可能喜欢的商品属性集合")
	private List<ProductMaybeLove> productMaybeLove=new ArrayList<ProductMaybeLove>();
	
	@ZapcomApi(value="分页总页数")
	private int pagination=0;


	public int getPagination() {
		return pagination;
	}

	public void setPagination(int pagination) {
		this.pagination = pagination;
	}

	public List<ProductMaybeLove> getProductMaybeLove() {
		return productMaybeLove;
	}

	public void setProductMaybeLove(List<ProductMaybeLove> productMaybeLove) {
		this.productMaybeLove = productMaybeLove;
	}
}

//class ProductMaybeLove{
//	@ZapcomApi(value="商品编号")
//	private String procuctCode="";
//	
//	@ZapcomApi(value="商品名称")
//	private String productNameString="";
//	
//	@ZapcomApi(value="商品价格")
//	private String productPrice="";
//	
//	@ZapcomApi(value="商品图片")
//	private String  mainpic_url="";
//	
//
//	public String getProcuctCode() {
//		return procuctCode;
//	}
//
//	public String getProductPrice() {
//		return productPrice;
//	}
//
//	public void setProductPrice(String productPrice) {
//		this.productPrice = productPrice;
//	}
//
//	public String getMainpic_url() {
//		return mainpic_url;
//	}
//
//	public void setMainpic_url(String mainpic_url) {
//		this.mainpic_url = mainpic_url;
//	}
//
//	public void setProcuctCode(String procuctCode) {
//		this.procuctCode = procuctCode;
//	}
//
//	public String getProductNameString() {
//		return productNameString;
//	}
//
//	public void setProductNameString(String productNameString) {
//		this.productNameString = productNameString;
//	}

	
//}
