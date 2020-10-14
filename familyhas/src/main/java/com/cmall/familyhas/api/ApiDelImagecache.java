package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ImageZoomInput;
import com.srnpr.xmassystem.AppimageZoom;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 删除图片缓存
 * @author liqt
 *
 */
public class ApiDelImagecache extends RootApi<RootResultWeb, ImageZoomInput>{
	public RootResultWeb Process(ImageZoomInput input, MDataMap mDataMap){
		String Picturepath = input.getImageUrl();
		String[] Eliminate = Picturepath.split(",");
		
		RootResultWeb result = new RootResultWeb();
		AppimageZoom imagezoom = new AppimageZoom();
		int numb = imagezoom.ImageZoom(Eliminate);
		result.setResultMessage("成功删除了"+numb+"条数据!");
		return result;
	}
}

