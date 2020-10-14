package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncShowRuleName extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		String string = mDataMap.get("rule_name");
		String sql = "select * from pc_product_xuanpin where xp_code = '%s'";
		List<Map<String,Object>> dataSqlList = DbUp.upTable("pc_product_xuanpin").dataSqlList(String.format(sql, string), new MDataMap());
		String result = "? ? ?";
		if(dataSqlList.size() > 0) {
			result = MapUtils.getString(dataSqlList.get(0),"name");
		}
		MWebResult mWebResult = new MWebResult();
		mWebResult.setResultObject(result);
		return mWebResult;
	}

}
