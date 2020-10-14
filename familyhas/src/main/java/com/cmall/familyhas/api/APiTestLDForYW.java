package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.APiTestLDForYWInput;
import com.cmall.familyhas.api.result.APiTestLDForYWResult;
import com.cmall.groupcenter.homehas.RsyncGetStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;

/**
 * 为运维提供的测试LD接口响应的接口
 * 
 * @author xiegj
 *
 */
public class APiTestLDForYW extends RootApi<APiTestLDForYWResult, APiTestLDForYWInput> {

	public APiTestLDForYWResult Process(APiTestLDForYWInput inputParam,
			MDataMap mRequestMap) {
		APiTestLDForYWResult mWebResult = new APiTestLDForYWResult();
		
		RsyncGetStock rsyncGetStock = new RsyncGetStock();
		rsyncGetStock.upRsyncRequest().setGood_id(inputParam.getSkuCode());
		if(!rsyncGetStock.doRsync()||!rsyncGetStock.responseSucc()){
			mWebResult.setResultCode(918501003);
			mWebResult.setResultMessage(bInfo(918501003));
		}
		
		return mWebResult;
	}

}
