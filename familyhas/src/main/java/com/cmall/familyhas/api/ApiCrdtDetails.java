package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiIntegralDetailsInput;
import com.cmall.familyhas.api.result.ApiCrdtDetailsResult;
import com.cmall.groupcenter.homehas.RsyncGetCustCrdtUseDetail;
import com.cmall.groupcenter.homehas.model.CrdtInfo;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 暂存款明细
 * 
 * @author sunyan
 * 
 */
public class ApiCrdtDetails extends RootApiForToken<ApiCrdtDetailsResult, ApiIntegralDetailsInput> {

	public ApiCrdtDetailsResult Process(ApiIntegralDetailsInput inputParam, MDataMap mRequestMap) {
		ApiCrdtDetailsResult result = new ApiCrdtDetailsResult();
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		RsyncGetCustCrdtUseDetail custDetail = new RsyncGetCustCrdtUseDetail();
		DecimalFormat df = new DecimalFormat("#.##");
		custDetail.upRsyncRequest().setCust_id(plusServiceAccm.getCustId(getUserCode()));
		custDetail.upRsyncRequest().setPage_num(inputParam.getPageNum());
		custDetail.upRsyncRequest().setPage_count(inputParam.getPageCount());
		custDetail.doRsync();

		List<CrdtInfo> list = custDetail.upResponseObject().getResult();
		result.setPageFlag(custDetail.upResponseObject().getPage_flag());
		result.setTotal(custDetail.upResponseObject().getTotal());
		GetCustAmtResult amtRef = plusServiceAccm.getPlusModelCustAmt(plusServiceAccm.getCustId(getUserCode()));
		if (amtRef != null) {
			BigDecimal crdt_amt = amtRef.getPossCrdtAmt();
			if (crdt_amt.intValue() < 1000000) {
				result.setIntegralTotal(df.format(crdt_amt));
			}else {
				result.setIntegralTotal("1000000+");
			}
		}
		
		CrdtInfo crdtInfo;
		for (int i = 0; i < list.size(); i++) {
			crdtInfo = list.get(i);
			String crdtType = crdtInfo.getCrdt_cd();
			String sOrderCode = crdtInfo.getApp_ord_id();
			com.cmall.familyhas.api.model.CrdtInfo info = new com.cmall.familyhas.api.model.CrdtInfo();
			info.setCrdtCount(df.format(new BigDecimal(crdtInfo.getCrdt_amt())));
			info.setCrdtCnfmDate(crdtInfo.getCrdt_cnfm_date());
			info.setAppOrdId(sOrderCode);
			info.setCrdtRelId(crdtInfo.getCrdt_rel_id());
			info.setCrdtDesc(accmCnfmDate(crdtType));
			info.setCrdtType(accmType(crdtType));
			
			
			result.getList().add(info);
		}
		return result;
	}
	
	public String accmType (String type){
		Map<String, String> map = new HashMap<String,String>();
		map.put("10", "1");
		map.put("15", "1");                         
		map.put("20", "1");  
		map.put("30", "1");  
		map.put("40", "1");  
		map.put("45", "1");    
		map.put("60", "0");   
		map.put("70", "0");  
		map.put("80", "0");  
		map.put("90", "0");
		map.put("95", "0");                    
		return map.get(type);
	}
	
	public String accmCnfmDate (String AccmCnfmDate){
		Map<String, String> map = new HashMap<String,String>();
		map.put("10", "订单返还");
		map.put("15", "订单返还");                         
		map.put("20", "订单返还");  
		map.put("30", "订单返还");  
		map.put("40", "订单返还");  
		map.put("45", "订单返还");    
		map.put("60", "订单使用");   
		map.put("70", "订单使用");  
		map.put("80", "订单使用");  
		map.put("90", "订单使用");
		map.put("95", "订单使用");                     
		return map.get(AccmCnfmDate);
	}
}
