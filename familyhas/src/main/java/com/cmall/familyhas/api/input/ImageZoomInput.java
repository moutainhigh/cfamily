package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.baseface.IBaseInput;

/**
 * 输入参数
 * 
 * @author srnpr
 * 
 */
public class ImageZoomInput implements IBaseInput {

	/**
	 * 图片路径
	 */
	@ZapcomApi(value = "图片路径", remark = "仅用于扩展，无需传入", require = 0)
	private String ImageUrl;

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

}
