package com.cmall.familyhas.mtmanager.api;

import com.cmall.familyhas.mtmanager.inputresult.SyncProductInfoMTInput;
import com.cmall.familyhas.mtmanager.inputresult.SyncProductInfoMTResult;
import com.cmall.familyhas.mtmanager.service.SyncProductInfoMTService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * MT管家同步商品信息
 * @author pang_jhui
 *
 */
public class ApiSyncProductInfoMT extends RootApiForManage<SyncProductInfoMTResult, SyncProductInfoMTInput> {

	public SyncProductInfoMTResult Process(SyncProductInfoMTInput inputParam, MDataMap mRequestMap) {
		
		return new SyncProductInfoMTService().doProcess(inputParam, mRequestMap);
	}


}
