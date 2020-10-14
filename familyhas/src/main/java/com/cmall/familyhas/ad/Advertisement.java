package com.cmall.familyhas.ad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cmall.ordercenter.model.PayResult;
import com.cmall.ordercenter.service.AdvertisementService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.helper.WebSessionHelper;

/**
 * 广告
 * @author wz
 *
 */
public class Advertisement {
	/**
	 * 接收安沃广告参数
	 */
	public void inputAnWoReceive(){
		//System.out.println("============jinru==================");
		HttpServletRequest request = WebSessionHelper.create().upHttpRequest();
		
		MDataMap insertDatamap = new MDataMap();
		Map mMaps = request.getParameterMap();
		StringBuffer str = new StringBuffer();
		String key = null;
		String value = null;
		String endStr = null;
		List<String> list = new ArrayList<String>();
		
		for (Object oKey : mMaps.keySet()) {
			key = oKey.toString();
			value = request.getParameter(oKey.toString());
			
			insertDatamap.put(key, value);
			str.append(key+ "=" + value + "&");
		}
		if(str!=null && !"".equals(str)){
			endStr = str.substring(0, str.toString().length() - 1);
			insertDatamap.put("param_value", endStr);
			
			AdvertisementService advertisementService = new AdvertisementService();
			//插入安沃广告信息
			PayResult PayResult = advertisementService.anwoInsert(insertDatamap);
		}
		
	}
	
}
