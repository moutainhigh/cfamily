package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ProductPic{
	@ZapcomApi(value="LD商品编号")
	private String productCode="";

	@ZapcomApi(value="商品上下架状态")
    private String productStatus = "";
    
	@ZapcomApi(value="商品列表图")
    private String mainpicUrl = "";
	
	@ZapcomApi(value="轮播图")
    private List<String> pcPicList = new ArrayList<String>();

	@ZapcomApi(value="描述图片")
    private List<String> discriptPicList = new ArrayList<String>();

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getMainpicUrl() {
		return mainpicUrl;
	}

	public void setMainpicUrl(String mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}

	public List<String> getPcPicList() {
		return pcPicList;
	}

	public void setPcPicList(List<String> pcPicList) {
		this.pcPicList = pcPicList;
	}

	public List<String> getDiscriptPicList() {
		return discriptPicList;
	}

	public void setDiscriptPicList(List<String> discriptPicList) {
		this.discriptPicList = discriptPicList;
	}
}
