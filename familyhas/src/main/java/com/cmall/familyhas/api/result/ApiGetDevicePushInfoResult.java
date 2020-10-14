package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.model.DevicePushModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetDevicePushInfoResult extends RootResult{

	@ZapcomApi(value="设备信息列表")
	List<DevicePushModel> deviceList = new ArrayList<DevicePushModel>();

	public List<DevicePushModel> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<DevicePushModel> deviceList) {
		this.deviceList = deviceList;
	} 
	
	
}
