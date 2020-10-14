package com.cmall.familyhas.webfunc;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncDeleteCommonProblemForKJT extends FuncDelete {

	/**
	 * 删除跨境通常见问题
	 */ 
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = super.funcDo(sOperateUid, mDataMap);
		if (mResult.upFlagTrue()) {
			//删除跨境通商品的缓存
			XmasKv.upFactory(EKvSchema.ProductCommonProblem).del("SI2003");
		}
		return mResult;
	}
}
