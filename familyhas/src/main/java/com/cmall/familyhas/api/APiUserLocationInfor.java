package com.cmall.familyhas.api;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.APiUserLocationInforInput;
import com.cmall.familyhas.api.result.APiUserLocationInforResult;
import com.cmall.systemcenter.service.StartPageService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * user location information
 * 
 * @author xiegj
 * 
 */
public class APiUserLocationInfor extends
		RootApiForMember<APiUserLocationInforResult, APiUserLocationInforInput> {

	public APiUserLocationInforResult Process(APiUserLocationInforInput inputParam,
			MDataMap mRequestMap) {
		APiUserLocationInforResult result = new APiUserLocationInforResult();
		MDataMap map = new MDataMap();
		map.put("seller_code", getManageCode());
		if(StringUtils.isNotBlank(inputParam.getSqNum())){
			map.put("sqNum", inputParam.getSqNum());
		}
		if(StringUtils.isNotBlank(inputParam.getCityName())){
			map.put("cityName", inputParam.getCityName());
		}
		if(StringUtils.isNotBlank(inputParam.getLatitude())){
			map.put("longitude", inputParam.getLongitude());
		}
		if (StringUtils.isNotBlank(inputParam.getLatitude())) {
			map.put("latitude", inputParam.getLatitude());
		}
		new StartPageService().saveUserLocationInfor(map);
		
		return result;
	}

}
