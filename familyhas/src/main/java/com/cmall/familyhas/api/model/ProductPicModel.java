package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 
 * 
 */
public class ProductPicModel {
	
	@ZapcomApi(value="商品编号")
	private String productCode="";
	
	@ZapcomApi(value="商品主图")
	private String productMainPic="";
	
	@ZapcomApi(value="商品轮播图")
	private String productCarouselPics="";
	
	@ZapcomApi(value="商品详情图")
	private String productDetailPics="";

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductMainPic() {
		return productMainPic;
	}

	public void setProductMainPic(String productMainPic) {
		this.productMainPic = productMainPic;
	}

	public String getProductCarouselPics() {
		return productCarouselPics;
	}

	public void setProductCarouselPics(String productCarouselPics) {
		this.productCarouselPics = productCarouselPics;
	}

	public String getProductDetailPics() {
		return productDetailPics;
	}

	public void setProductDetailPics(String productDetailPics) {
		this.productDetailPics = productDetailPics;
	}
	
	
}
