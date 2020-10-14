package com.cmall.familyhas.webfunc;

import com.cmall.groupcenter.homehas.RsyncGetStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;


public class FuncTestGetGifts extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mWebResult = new MWebResult();
		
		RsyncGetStock rsyncGetStock = new RsyncGetStock();
		rsyncGetStock.upRsyncRequest().setGood_id("131833");
		if(!rsyncGetStock.doRsync()||!rsyncGetStock.responseSucc()){
			mWebResult.setResultCode(918501003);
			mWebResult.setResultMessage(bInfo(918501003));
		}
		
		return mWebResult;
	}

	
}
