package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiHbDetailsInput;
import com.cmall.familyhas.api.model.HbDetailInfo;
import com.cmall.familyhas.api.result.ApiHbDetailsResult;
import com.cmall.groupcenter.homehas.RsyncGetCustHbUseDetail;
import com.cmall.groupcenter.homehas.model.HbInfo;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;


/**
 * 惠币收支明细接口
 */
public class ApiHbDetails extends RootApiForToken<ApiHbDetailsResult, ApiHbDetailsInput> {
	@Override
	public ApiHbDetailsResult Process(ApiHbDetailsInput inputParam, MDataMap mRequestMap) {
		ApiHbDetailsResult result = new ApiHbDetailsResult();
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		RsyncGetCustHbUseDetail custDetail = new RsyncGetCustHbUseDetail();
		DecimalFormat df = new DecimalFormat("#.###");
		custDetail.upRsyncRequest().setCust_id(plusServiceAccm.getCustId(getUserCode()));
		custDetail.upRsyncRequest().setPage_num(inputParam.getPageNum());
		custDetail.upRsyncRequest().setPage_count(inputParam.getPageCount());
		custDetail.doRsync();

		List<HbInfo> list = custDetail.upResponseObject().getResult();
		result.setPageFlag(custDetail.upResponseObject().getPage_flag());
		result.setTotal(custDetail.upResponseObject().getTotal());
		result.setTotalPage(custDetail.upResponseObject().getTotalPage());
		HbDetailInfo integralInfo = null;
		GetCustAmtResult amtRef = plusServiceAccm.getPlusModelCustAmt(plusServiceAccm.getCustId(getUserCode()));
		if (amtRef != null) {
			BigDecimal possHcoinAmt = amtRef.getPossHcoinAmt();
			if (possHcoinAmt.intValue() < 1000000) {
				result.setHbTotal(df.format(possHcoinAmt.doubleValue()));
			}else {
				result.setHbTotal("1000000+");
			}
		}
		
		HbInfo accmInfo;
		for (int i = 0; i < list.size(); i++) {
			accmInfo = list.get(i);
			String accmType = accmInfo.getHb_rsn_cd();
			String sOrderCode = accmInfo.getApp_ord_id();
			integralInfo = new HbDetailInfo();
			BigDecimal AccmAmt = new BigDecimal(0.00);
			if(StringUtils.isNotBlank(accmInfo.getHb_amt())) {
				AccmAmt = new BigDecimal(accmInfo.getHb_amt());
			}
			integralInfo.setHbCount(df.format(AccmAmt.doubleValue()));
			integralInfo.setHbCnfmDate(accmInfo.getHb_cnfm_date());
			integralInfo.setHbDesc(accmCnfmDate(accmType));
//			integralInfo.setHbDesc(accmInfo.getHb_desc());
			integralInfo.setHbtype(accmType(accmType));
			integralInfo.setHbRelId(accmInfo.getHb_rel_id());
//			integralInfo.setAccmRelSeq(custDetail.upResponseObject().getResult().get(i).getAccm_rel_seq());
//			integralInfo.setAccmRsnCd(custDetail.upResponseObject().getResult().get(i).getAccm_rsn_cd());
//			integralInfo.setAppChildOrdId(custDetail.upResponseObject().getResult().get(i).getApp_child_ord_id());
			integralInfo.setAppOrdId(sOrderCode);
			
//			// APP订单占用积分和取消积分
			if(StringUtils.isNotBlank(accmInfo.getApp_ord_id())){
				if("80".equals(accmType)){
					integralInfo.setHbDesc("订单使用惠币");
				}
				if("40".equals(accmType)){
					integralInfo.setHbDesc("取消订单返还惠币");
				}
//				if("60".equals(accmType) && StringUtils.isNotBlank(accmInfo.getHb_desc()) && accmInfo.getHb_desc().contains("兑换优惠券")){
//					integralInfo.setAccmDesc(accmInfo.getHb_desc());
//				}
			}
//			
			if("20".equals(accmType) && accmInfo.getHb_desc() != null && accmInfo.getHb_desc().contains("取消退货")){
				integralInfo.setHbDesc("取消退货返还惠币");
			}
			if("15".equals(accmType) && list.get(i).getHb_desc() != null && list.get(i).getHb_desc().contains("推广人") && list.get(i).getHb_desc().contains("订单")){
				integralInfo.setHbDesc("订单奖励推广人惠币");
			}
			if("15".equals(accmType) && list.get(i).getHb_desc() != null && list.get(i).getHb_desc().contains("推广人") && list.get(i).getHb_desc().contains("买家秀")){
				integralInfo.setHbDesc("买家秀奖励推广人惠币");
			}
			if("15".equals(accmType) && list.get(i).getHb_desc() != null && list.get(i).getHb_desc().contains("取消退货还原推广人惠币")){
				integralInfo.setHbDesc("取消退货还原推广人惠币");
			}
			if("65".equals(accmType) && list.get(i).getHb_desc() != null && list.get(i).getHb_desc().contains("取消订单还原惠币")){
				integralInfo.setHbDesc("取消订单还原惠币");
			}
			if("80".equals(accmType) && list.get(i).getHb_desc() != null && list.get(i).getHb_desc().contains("还原推广人")){
				integralInfo.setHbDesc("取消订单还原推广人惠币");
			}
			if("80".equals(accmType) && list.get(i).getHb_desc() != null && list.get(i).getHb_desc().contains("退货扣除推广人惠币")){
				integralInfo.setHbDesc("退货扣除推广人惠币");
			}
			if("70".equals(accmType) && list.get(i).getHb_desc() != null && list.get(i).getHb_desc().contains("扣减行销")){
				integralInfo.setHbDesc("退货扣除推广人惠币");
			}
			if("80".equals(accmType) && list.get(i).getHb_desc() != null && list.get(i).getHb_desc().contains("惠币提现")){
				integralInfo.setHbDesc("惠币提现");
			}
			if("40".equals(accmType) && list.get(i).getHb_desc() != null && list.get(i).getHb_desc().contains("提现失败")){
				integralInfo.setHbDesc("惠币提现失败还原");
			}
			
			result.getList().add(integralInfo);
		}
		return result;
	}
	
	public String accmType (String type){
		Map<String, String> map = new HashMap<String,String>();
		map.put("10", "1");
		map.put("15", "1");                         
		map.put("20", "1");  
		map.put("30", "1");  
		map.put("35", "1");
		map.put("40", "1");  
		map.put("45", "1");  
		map.put("90", "1");  
		map.put("60", "0");  
		map.put("65", "0");  
		map.put("70", "0");
		map.put("75", "0");  
		map.put("80", "0");  
		map.put("95", "0");                     
		return map.get(type);
	}
	
	public String accmCnfmDate (String AccmCnfmDate){
		Map<String, String> map = new HashMap<String,String>();
		map.put("10", "购物送惠币");
		map.put("15", "购物送惠币");                         
		map.put("20", "取消订单返还惠币");  
		map.put("30", "取消订单返还惠币");  
		map.put("35", "取消订单返还惠币");
		map.put("45", "取消订单返还惠币");  
		map.put("40", "系统赠送惠币");  
		map.put("90", "系统赠送惠币");  
		map.put("60", "订单使用惠币");
		map.put("65", "退货惠币扣除");  
		map.put("70", "退货惠币扣除");
		map.put("75", "订单使用惠币");
		map.put("80", "惠币过期扣除");
		map.put("95", "惠币过期扣除");                     
		return map.get(AccmCnfmDate);
	}

}
