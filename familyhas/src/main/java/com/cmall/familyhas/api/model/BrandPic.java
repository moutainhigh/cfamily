package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/** 
* @ClassName: BrandPic 
* @Description: 品牌特惠专题广告图
* @author 张海生
* @date 2015-5-11 下午5:56:13 
*  
*/
public class BrandPic {

	@ZapcomApi(value = "广告图", demo = "http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/22676/2bf6697a4c1a4536bcec0b0c417a7356.jpg")
	private String brandPic = "";

	@ZapcomApi(value = "位置", remark = "1:头部,2:尾部")
	private String brandLocation = "";

	@ZapcomApi(value = "链接类型", remark = "4497471600020001:URL,4497471600020002:关键词搜索,4497471600020003:分类搜索,4497471600020004:商品详情")
	private String linkType = "";

	@ZapcomApi(value = "链接值")
	private String linkValue = "";

	public String getBrandPic() {
		return brandPic;
	}

	public void setBrandPic(String brandPic) {
		this.brandPic = brandPic;
	}

	public String getBrandLocation() {
		return brandLocation;
	}

	public void setBrandLocation(String brandLocation) {
		this.brandLocation = brandLocation;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLinkValue() {
		return linkValue;
	}

	public void setLinkValue(String linkValue) {
		this.linkValue = linkValue;
	}

}
