package com.cmall.familyhas.mtmanager.api;

import com.cmall.familyhas.mtmanager.inputresult.SyncPayInfoMTInput;
import com.cmall.familyhas.mtmanager.inputresult.SyncPayInfoMTResult;
import com.cmall.familyhas.mtmanager.service.SyncPayInfoMTService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * MT支付信息同步
 * @author pang_jhui
 *
 */
public class ApiSyncPayInfoMT extends RootApiForManage<SyncPayInfoMTResult, SyncPayInfoMTInput> {

	public SyncPayInfoMTResult Process(SyncPayInfoMTInput inputParam, MDataMap mRequestMap) {
		
		SyncPayInfoMTResult payInfoMTResult = null;
		
		try {
			payInfoMTResult = new SyncPayInfoMTService().doProcess(inputParam);
		} catch (Exception e) {
			bLogError(0, e.getMessage());
		}
		
		return payInfoMTResult;
		
	}

}
