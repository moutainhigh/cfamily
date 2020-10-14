package com.cmall.familyhas.webfunc;



import com.cmall.productcenter.support.AutoSelectProductSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 预查询商品数量
 */
public class FuncXuanpinPreProductCount extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult result = new MWebResult();
		
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		result.setResultMessage("" + new AutoSelectProductSupport().getTotalProductNum(mAddMaps));
		
		return result;
	}

}
