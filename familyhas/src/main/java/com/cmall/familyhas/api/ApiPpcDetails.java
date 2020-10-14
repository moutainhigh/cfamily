package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiIntegralDetailsInput;
import com.cmall.familyhas.api.result.ApiPpcDetailsResult;
import com.cmall.groupcenter.homehas.RsyncGetCustPpcUseDetail;
import com.cmall.groupcenter.homehas.model.PpcInfo;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;

import javassist.expr.NewArray;

/**
 * 储值金明细
 * 
 * @author sunyan
 * 
 */
public class ApiPpcDetails extends RootApiForToken<ApiPpcDetailsResult, ApiIntegralDetailsInput> {

	public ApiPpcDetailsResult Process(ApiIntegralDetailsInput inputParam, MDataMap mRequestMap) {
		ApiPpcDetailsResult result = new ApiPpcDetailsResult();
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		RsyncGetCustPpcUseDetail custDetail = new RsyncGetCustPpcUseDetail();
		DecimalFormat df = new DecimalFormat("#.##");
		custDetail.upRsyncRequest().setCust_id(plusServiceAccm.getCustId(getUserCode()));
		custDetail.upRsyncRequest().setPage_num(inputParam.getPageNum());
		custDetail.upRsyncRequest().setPage_count(inputParam.getPageCount());
		custDetail.doRsync();

		List<PpcInfo> list = custDetail.upResponseObject().getResult();
		result.setPageFlag(custDetail.upResponseObject().getPage_flag());
		result.setTotal(custDetail.upResponseObject().getTotal());
		GetCustAmtResult amtRef = plusServiceAccm.getPlusModelCustAmt(plusServiceAccm.getCustId(getUserCode()));
		if (amtRef != null) {
			BigDecimal ppc_amt = amtRef.getPossPpcAmt();
			if (ppc_amt.intValue() < 1000000) {
				result.setIntegralTotal(df.format(ppc_amt));
			}else {
				result.setIntegralTotal("1000000+");
			}
		}
		
		PpcInfo ppcInfo;
		for (int i = 0; i < list.size(); i++) {
			ppcInfo = list.get(i);
			String ppcType = ppcInfo.getPpc_rsn_cd();
			String sOrderCode = ppcInfo.getApp_ord_id();
			com.cmall.familyhas.api.model.PpcInfo info = new com.cmall.familyhas.api.model.PpcInfo();
			info.setPpcCount(df.format(new BigDecimal(ppcInfo.getPpc_amt())));
			info.setPpcCnfmDate(ppcInfo.getPpc_cnfm_date());
			info.setAppOrdId(sOrderCode);
			info.setPpcRelId(ppcInfo.getPpc_rel_id());
			info.setPpcDesc(accmCnfmDate(ppcType));
			info.setPpctype(accmType(ppcType));
			
			
			result.getList().add(info);
		}
		return result;
	}
	
	public String accmType (String type){
		Map<String, String> map = new HashMap<String,String>();
		map.put("10", "1");                         
		map.put("20", "1");  
		map.put("40", "1");    
		map.put("60", "0");    
		map.put("80", "0");                      
		return map.get(type);
	}
	
	public String accmCnfmDate (String AccmCnfmDate){
		Map<String, String> map = new HashMap<String,String>();
		map.put("10", "家有卡充值");                         
		map.put("20", "取消订单返还");  
		map.put("40", "储值金增加");    
		map.put("60", "订单使用");    
		map.put("80", "订单使用");                     
		return map.get(AccmCnfmDate);
	}
}
