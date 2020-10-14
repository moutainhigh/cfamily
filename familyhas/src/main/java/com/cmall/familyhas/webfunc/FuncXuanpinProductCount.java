package com.cmall.familyhas.webfunc;

import com.cmall.productcenter.support.AutoSelectProductSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 预先查询商品数量，用于选品规则未保存前预查大概的商品数
 */
public class FuncXuanpinProductCount extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		
		result.setResultMessage("" + new AutoSelectProductSupport().getTotalProductNum(mDataMap.get("xpCode")));
		
		return result;
	}

}
