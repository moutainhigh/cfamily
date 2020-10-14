package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class HotBrandInfo {
	@ZapcomApi(value = "品牌名称")
	private String brandName;
	@ZapcomApi(value = "品牌编号")
	private String brandCode;
	@ZapcomApi(value = "品牌图片")
	private String pic;
	@ZapcomApi(value = "品牌链接")
	private String link;
	/**
	 * 获取  brandName
	 */
	public String getBrandName() {
		return brandName;
	}
	/**
	 * 设置 
	 * @param brandName 
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	/**
	 * 获取  brandCode
	 */
	public String getBrandCode() {
		return brandCode;
	}
	/**
	 * 设置 
	 * @param brandCode 
	 */
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	/**
	 * 获取  pic
	 */
	public String getPic() {
		return pic;
	}
	/**
	 * 设置 
	 * @param pic 
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	/**
	 * 获取  link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * 设置 
	 * @param link 
	 */
	public void setLink(String link) {
		this.link = link;
	}
	
	
}
