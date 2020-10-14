package com.cmall.familyhas.webfunc;

import java.util.List;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

public class HotBrandAddOrEdit extends FuncAdd {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap param = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		param.put("create_time", DateUtil.getNowTime());
		if(param.containsKey("uid")){
			List<MDataMap> list = DbUp.upTable("pc_hot_brand").queryAll("", "", "sort=:sort and uid!=:uid", param);
			if(list.size()>0){
				mResult.inErrorMessage(916421181,param.get("sort"));
				return mResult;
			}
			DbUp.upTable("pc_hot_brand").update(param);
		}else{
			int num = DbUp.upTable("pc_hot_brand").count("sort",param.get("sort"));
			if(num>0){
				mResult.inErrorMessage(916421181,param.get("sort"));
				return mResult;
			}
			DbUp.upTable("pc_hot_brand").dataInsert(param);
		}
		
		return mResult;
	}
}
