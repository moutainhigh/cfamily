package com.cmall.familyhas.webfunc;


import com.cmall.groupcenter.jd.JdAfterSaleSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 同步京东服务单状态
 */
public class FuncRsyncJdAfterSale extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String afterSaleCode = mDataMap.get("afterSaleCode");
		
		RootResult res = new JdAfterSaleSupport().execServiceDetailInfoQuery(afterSaleCode);
		
		mResult.inOtherResult(res);
		return mResult;
	}

}
