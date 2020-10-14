package com.cmall.familyhas.mtmanager.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * MT管家图片信息
 * @author pang_jhui
 *
 */
public class MTProductPic {
	
	@ZapcomApi(value="图片路径") 
    private String picUrl;

	/**
	 * 获取图片路径
	 * @return
	 */
	public String getPicUrl() {
		return picUrl;
	}

	/**
	 * 设置图片路径
	 * @param picUrl
	 */
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}
