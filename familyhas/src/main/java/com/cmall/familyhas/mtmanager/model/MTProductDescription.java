package com.cmall.familyhas.mtmanager.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class MTProductDescription {
	
	@ZapcomApi(value="商品描述信息")
    private String descriptionInfo  = ""  ;
    
	@ZapcomApi(value="商品描述图片")
    private String descriptionPic  = ""  ;

	/**
	 * 获取描述信息
	 * @return
	 */
	public String getDescriptionInfo() {
		return descriptionInfo;
	}

	/**
	 * 设置描述信息
	 * @param descriptionInfo
	 */
	public void setDescriptionInfo(String descriptionInfo) {
		this.descriptionInfo = descriptionInfo;
	}

	/**
	 * 获取描述图片
	 * @return
	 */
	public String getDescriptionPic() {
		return descriptionPic;
	}

	/**
	 * 设置描述图片
	 * @param descriptionPic
	 */
	public void setDescriptionPic(String descriptionPic) {
		this.descriptionPic = descriptionPic;
	}

}
