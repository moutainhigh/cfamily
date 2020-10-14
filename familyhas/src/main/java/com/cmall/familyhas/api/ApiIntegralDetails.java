package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiIntegralDetailsInput;
import com.cmall.familyhas.api.model.IntegralInfo;
import com.cmall.familyhas.api.result.ApiIntegralDetailsResult;
import com.cmall.groupcenter.homehas.RsyncGetCustAccmUseDetail;
import com.cmall.groupcenter.homehas.model.AccmInfo;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 积分明细
 * 
 * @author wz
 * 
 */
public class ApiIntegralDetails extends RootApiForToken<ApiIntegralDetailsResult, ApiIntegralDetailsInput> {

	public ApiIntegralDetailsResult Process(ApiIntegralDetailsInput inputParam, MDataMap mRequestMap) {
		ApiIntegralDetailsResult result = new ApiIntegralDetailsResult();
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		RsyncGetCustAccmUseDetail custDetail = new RsyncGetCustAccmUseDetail();
		DecimalFormat df = new DecimalFormat("#.#");
		custDetail.upRsyncRequest().setCust_id(plusServiceAccm.getCustId(getUserCode()));
		custDetail.upRsyncRequest().setPage_num(inputParam.getPageNum());
		custDetail.upRsyncRequest().setPage_count(inputParam.getPageCount());
		custDetail.doRsync();

		List<AccmInfo> list = custDetail.upResponseObject().getResult();
		result.setPageFlag(custDetail.upResponseObject().getPage_flag());
		result.setTotal(custDetail.upResponseObject().getTotal());
		IntegralInfo integralInfo = null;
		GetCustAmtResult amtRef = plusServiceAccm.getPlusModelCustAmt(plusServiceAccm.getCustId(getUserCode()));
		if (amtRef != null) {
			BigDecimal accm = plusServiceAccm.moneyToAccmAmt(amtRef.getPossAccmAmt(),1);
			BigDecimal expireAccm = plusServiceAccm.moneyToAccmAmt(amtRef.getExpireAccm(),1);
			if (accm.intValue() < 1000000) {
				result.setIntegralTotal(df.format(plusServiceAccm.moneyToAccmAmt(amtRef.getPossAccmAmt(),1).doubleValue()));
			}else {
				result.setIntegralTotal("1000000+");
			}
			if(expireAccm.intValue() >= 200){
				result.setExpireIntegral(df.format(expireAccm));
			}
		}
		
		AccmInfo accmInfo;
		for (int i = 0; i < list.size(); i++) {
			accmInfo = list.get(i);
			String accmType = accmInfo.getAccm_rsn_cd();
			String sOrderCode = accmInfo.getApp_ord_id();
			integralInfo = new IntegralInfo();
			BigDecimal AccmAmt = new BigDecimal(accmInfo.getAccm_amt());
			integralInfo.setAccmCount(df.format(plusServiceAccm.moneyToAccmAmt(AccmAmt, 1).doubleValue()));
			integralInfo.setAccmCnfmDate(accmInfo.getAccm_cnfm_date());
			integralInfo.setAccmDesc(accmCnfmDate(accmType));
			integralInfo.setAccmtype(accmType(accmType));
			integralInfo.setAccmRelId(accmInfo.getAccm_rel_id());
//			integralInfo.setAccmRelSeq(custDetail.upResponseObject().getResult().get(i).getAccm_rel_seq());
//			integralInfo.setAccmRsnCd(custDetail.upResponseObject().getResult().get(i).getAccm_rsn_cd());
//			integralInfo.setAppChildOrdId(custDetail.upResponseObject().getResult().get(i).getApp_child_ord_id());
			integralInfo.setAppOrdId(sOrderCode);
			
			// APP订单占用积分和取消积分
			if(StringUtils.isNotBlank(accmInfo.getApp_ord_id())){
				if("80".equals(accmType)){
					integralInfo.setAccmDesc("订单使用积分");
				}
				if("40".equals(accmType)){
					integralInfo.setAccmDesc("取消订单返还积分");
				}
				if("60".equals(accmType) && StringUtils.isNotBlank(accmInfo.getAccm_desc()) && accmInfo.getAccm_desc().contains("兑换优惠券")){
					integralInfo.setAccmDesc(accmInfo.getAccm_desc());
				}
			}
			
			if("20".equals(accmType) && accmInfo.getAccm_desc() != null && accmInfo.getAccm_desc().contains("取消退货")){
				integralInfo.setAccmDesc("取消退货返还积分");
			}
			if("15".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("积分共享")){
				integralInfo.setAccmDesc("积分共享赋予");
			}
			if("70".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("积分共享")){
				if(list.get(i).getAccm_desc().contains("之后取消退货")) {
					integralInfo.setAccmDesc("积分共享赋予");
				}else {
					integralInfo.setAccmDesc("积分共享退货扣除");
				}
			}
			if("70".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("退货挽留扣除积分")){
				integralInfo.setAccmDesc("退货挽留扣除积分");
			}
			if("15".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("好评奖励积分")){
				integralInfo.setAccmDesc("评论送积分");
			}
			if("15".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("退货挽留奖励")){
				integralInfo.setAccmDesc("退货挽留送积分");
			}
			if("15".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("签到送积分")){
				integralInfo.setAccmDesc("签到送积分");
			}
			if("15".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("福利转盘奖励积分")){
				integralInfo.setAccmDesc("福利转盘奖励积分");
			}
			if("80".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("大转盘抽奖消费积分")){
				integralInfo.setAccmDesc("大转盘抽奖消费积分");
			}
			if("15".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("浏览专题送积分")){
				integralInfo.setAccmDesc("浏览专题送积分");
			}
			if("40".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("自动赋予:老推新积分赋予")){
				integralInfo.setAccmDesc("老推新积分赋予");
			}
			if("15".equals(accmType) && list.get(i).getAccm_desc() != null && list.get(i).getAccm_desc().contains("惠家有切蛋糕活动奖励积分")){
				integralInfo.setAccmDesc("切蛋糕奖励积分");
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
		map.put("10", "购物送积分");
		map.put("15", "购物送积分");                         
		map.put("20", "取消订单返还积分");  
		map.put("30", "取消订单返还积分");  
		map.put("35", "取消订单返还积分");
		map.put("45", "取消订单返还积分");  
		map.put("40", "系统赠送积分");  
		map.put("90", "系统赠送积分");  
		map.put("60", "订单使用积分");
		map.put("65", "退货积分扣除");  
		map.put("70", "退货积分扣除");
		map.put("75", "订单使用积分");
		map.put("80", "积分过期扣除");
		map.put("95", "积分过期扣除");                     
		return map.get(AccmCnfmDate);
	}
}
