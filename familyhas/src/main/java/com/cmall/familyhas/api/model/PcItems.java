package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PcItems extends Items {

	@ZapcomApi(value = "品牌名称")
	private String brandName="";
	@ZapcomApi(value = "品牌编码")
	private String brandCode="";
	@ZapcomApi(value = "品牌图片")
	private String brandPic="";
	@ZapcomApi(value = "品牌描述")
	private String branddesc="";
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
	 * 获取  brandPic
	 */
	public String getBrandPic() {
		return brandPic;
	}
	/**
	 * 设置 
	 * @param brandPic 
	 */
	public void setBrandPic(String brandPic) {
		this.brandPic = brandPic;
	}
	/**
	 * 获取  branddesc
	 */
	public String getBranddesc() {
		return branddesc;
	}
	/**
	 * 设置 
	 * @param branddesc 
	 */
	public void setBranddesc(String branddesc) {
		this.branddesc = branddesc;
	}
	
	
}
