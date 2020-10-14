package com.cmall.familyhas.api;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ImagePropertyInput;
import com.cmall.familyhas.api.result.ImagePropertyResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapweb.websupport.ImageMagicSupport;

/**
 * @description: 获取一张图片的长宽和存储大小属性信息 
 *
 * @author Yangcl
 * @date 2017年4月25日 下午4:09:31 
 * @version 1.0.0
 */
public class ApiForImageProperty extends RootApi<ImagePropertyResult, ImagePropertyInput>{

	public ImagePropertyResult Process(ImagePropertyInput in, MDataMap mRequestMap) {
		ImagePropertyResult result = new ImagePropertyResult();
		String url = in.getImageUrl();
		if(StringUtils.isBlank(url)){
			return result;
		}
		
		ImageMagicSupport su = new ImageMagicSupport();
		Map<String , String> map = su.getImageSizeByUrl(url); 
		result.setWidth(Integer.valueOf(map.get("width")));
		result.setHeight(Integer.valueOf(map.get("height")));  
		result.setSize(map.get("size"));
		
		return result;
	}
	
	
}










