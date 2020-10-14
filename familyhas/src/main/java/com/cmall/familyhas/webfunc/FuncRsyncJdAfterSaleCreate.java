package com.cmall.familyhas.webfunc;


import com.cmall.groupcenter.jd.JdAfterSaleSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 创建京东服务单
 */
public class FuncRsyncJdAfterSaleCreate extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String afterSaleCode = mDataMap.get("afterSaleCode");
		
		// 先再查询一下避免重复创建
		new JdAfterSaleSupport().execServiceDetailInfoQuery(afterSaleCode);
		// 创建京东服务单
		RootResult res = new JdAfterSaleSupport().execAfsApplyCreate(afterSaleCode);
		
		if(res.getResultCode() == 1) {
			// 创建成功再更新服务单信息
			new JdAfterSaleSupport().execServiceDetailInfoQuery(afterSaleCode);
		}
		
		mResult.inOtherResult(res);
		return mResult;
	}

}
