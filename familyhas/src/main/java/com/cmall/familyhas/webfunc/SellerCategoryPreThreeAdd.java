package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class SellerCategoryPreThreeAdd extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String categoryCode = mDataMap.get("zw_f_category_code");
		String threeCode = mDataMap.get("zw_f_threeCode");
		if(categoryCode != null && !"".equals(categoryCode) && threeCode != null && !"".equals(threeCode)) {
			List<MDataMap> threeList = DbUp.upTable("uc_sellercategory").queryByWhere("uid", threeCode);
			if(threeList.size() > 0) {
				MDataMap insertMap = threeList.get(0);
				insertMap.remove("zid");
				insertMap.remove("uid");
				insertMap.put("parent_code", categoryCode);
				
				//修改sort，影响其他地方排序
				MDataMap sortParamsMap = new MDataMap();
				sortParamsMap.put("seller_code", "SI2003");
				sortParamsMap.put("parent_code", categoryCode);
				Map<String, Object> sortMap = DbUp.upTable("uc_sellercategory_pre").dataSqlOne("select max(sort) from uc_sellercategory_pre where parent_code=:parent_code and seller_code=:seller_code", sortParamsMap);
				if(!sortMap.isEmpty() && sortMap.get("max(sort)") != null && !"".equals(sortMap.get("max(sort)"))){
					String four = String.valueOf(Integer.valueOf(sortMap.get("max(sort)").toString().substring(sortMap.get("max(sort)").toString().length()-4, sortMap.get("max(sort)").toString().length()))+1);
					for ( int i = 0, j = (4 - four.length()); i < j; i++) {
						four = "0" + four;
					}
					String so = sortMap.get("max(sort)").toString().substring(0, sortMap.get("max(sort)").toString().length()-4)+four;
					insertMap.put("sort", so);
				}else{
					Map<String, Object> caMap = DbUp.upTable("uc_sellercategory_pre").dataSqlOne("select sort from uc_sellercategory_pre where category_code=:parent_code and seller_code=:seller_code", sortParamsMap);
					insertMap.put("sort", caMap.get("sort") + "0001");
				}
				
				DbUp.upTable("uc_sellercategory_pre").dataInsert(insertMap);
			}
		}else {
			mResult.setResultCode(0);
			mResult.setResultMessage(bInfo(916421176));
		}
		return mResult;
	}
}
