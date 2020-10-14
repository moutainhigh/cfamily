package com.cmall.familyhas.webfunc;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

public class AuthorityLogoFuncDel extends FuncDelete {

	/**
	 * 删除权威标志
	 */ 
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = super.funcDo(sOperateUid, mDataMap);
		if (mResult.upFlagTrue()) {
			//删除商品权威标志的缓存
			XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).del("SI2003");
			//删除商品权威标志的缓存
			XmasKv.upFactory(EKvSchema.ProductAuthorityLogo).del("SI2003-ProductLog");
		}
		return mResult;
	}
}
