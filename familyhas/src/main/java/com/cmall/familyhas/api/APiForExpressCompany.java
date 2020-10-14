package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.APiForExpressCompanyInput;
import com.cmall.familyhas.api.model.LogisticseInfo;
import com.cmall.familyhas.api.result.APiForExpressCompanyResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 获取快递公司列表
 * @author zmm
 *
 */
public class APiForExpressCompany extends RootApiForManage<APiForExpressCompanyResult,APiForExpressCompanyInput>{

	public APiForExpressCompanyResult Process(APiForExpressCompanyInput inputParam, MDataMap mRequestMap) {
		APiForExpressCompanyResult result=new APiForExpressCompanyResult();
//		String afterCode=inputParam.getAfterCodes().toString();
//		List<MDataMap> list=DbUp.upTable("oc_order_shipments").queryByWhere("order_code",afterCode);
		List<MDataMap> list=DbUp.upTable("sc_logisticscompany").queryByWhere();
		if(list!=null){
			for (MDataMap mDataMap : list) {
				String company_name = mDataMap.get("company_name");
				String company_code= mDataMap.get("company_code");
				LogisticseInfo logisticseInfo = new LogisticseInfo(company_name, company_code);
				result.getCompanyList().add(logisticseInfo);
			}
		}
		return result;
	}

}
