package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiHbDetailsInput;
import com.cmall.familyhas.api.input.ApiHbWithdrawInput;
import com.cmall.familyhas.api.model.HbDetailInfo;
import com.cmall.familyhas.api.model.IntegralInfo;
import com.cmall.familyhas.api.result.ApiHbDetailsResult;
import com.cmall.familyhas.api.result.ApiHbWithdrawResult;
import com.cmall.groupcenter.homehas.RsyncGetCustAccmUseDetail;
import com.cmall.groupcenter.homehas.RsyncGetCustHbUseDetail;
import com.cmall.groupcenter.homehas.model.AccmInfo;
import com.cmall.groupcenter.homehas.model.HbInfo;
import com.cmall.systemcenter.enumer.EVerifyCodeTypeEnumer;
import com.cmall.systemcenter.support.VerifySupport;
import com.srnpr.xmassystem.invoke.ref.CustRelAmtRef;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput.ChildOrder;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webmodel.MWebResult;


/**
 * 惠币提现接口
 */
public class ApiHbWithdraw extends RootApiForToken<ApiHbWithdrawResult, ApiHbWithdrawInput> {
	@Override
	public ApiHbWithdrawResult Process(ApiHbWithdrawInput inputParam, MDataMap mRequestMap) {
		ApiHbWithdrawResult result = new ApiHbWithdrawResult();
		VerifySupport support = new VerifySupport();
		if(support.checkVerifyCodeByType(EVerifyCodeTypeEnumer.huiCoinsWithdraw, getOauthInfo().getLoginName(), inputParam.getVerification_info()).upFlagTrue()) {
			String sLockKey=KvHelper.lockCodes(10, getUserCode());
			if(StringUtils.isBlank(sLockKey)){
				result.inErrorMessage(963902124);
				return result;
			}
			double parseDouble = Double.parseDouble(inputParam.getApply_money());
			if(parseDouble > 500.0) {
				result.inErrorMessage(916427001);
				return result;
			}
			if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) < 25) {
				result.inErrorMessage(916427006);
				return result;
			}
			PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(getUserCode()));
			String custId = levelInfo.getCustId();
			PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
			
			/**
			 * 查询积分数量、储值金、暂存款
			 */
			if (StringUtils.isNotEmpty(custId)) {
				GetCustAmtResult amtRef = plusServiceAccm.getPlusModelCustAmt(custId);
				if (amtRef != null) {
					//添加惠币
					BigDecimal possHcoinAmt = amtRef.getPossHcoinAmt();
					if(possHcoinAmt.doubleValue() < 100.0) {
						result.inErrorMessage(916427005);
						return result;
					}
					if(parseDouble%100 != 0) {
						result.inErrorMessage(916427007);
						return result;
					}
					if(parseDouble > possHcoinAmt.doubleValue()){
						result.inErrorMessage(916427002);
						return result;
					}
				}else {
					result.inErrorMessage(916427002);
					return result;
				}
			}
			MDataMap insertmap = new MDataMap();
			insertmap.put("member_code", getUserCode());
			
			List<Map<String,Object>> dataSqlList = DbUp.upTable("fh_tgz_withdraw_info").dataSqlList("select * from fh_tgz_withdraw_info WHERE create_time > date_format(now(),'%Y-%m-%d') and `status` != '4497471600620003'and member_code =:member_code", insertmap);
			List<Map<String,Object>> dataSqlList1 = DbUp.upTable("fh_tgz_withdraw_info").dataSqlList("select * from fh_tgz_withdraw_info WHERE `status` = '4497471600620001'and member_code =:member_code", insertmap);
			
			if (dataSqlList.size() > 0) {
				result.inErrorMessage(916427009);
				return result;
			}
			
			if(dataSqlList1.size() > 0) {
				result.inErrorMessage(916427003);
				return result;
			}
			
			UpdateCustAmtInput paramInput = new UpdateCustAmtInput();
		    paramInput.setCurdFlag(UpdateCustAmtInput.CurdFlag.TXHB);
		    paramInput.setCustId(custId);
		    ChildOrder childOrder = new ChildOrder();
		    childOrder.setChildHcoinAmt(new BigDecimal(inputParam.getApply_money()));//transfer_amount
		    paramInput.getOrderList().add(childOrder);
		    CustRelAmtRef custRelAmtRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
		    RootResult updateCustAmt = custRelAmtRef.updateCustAmt(paramInput);
		    if(updateCustAmt.getResultCode() != 1) {
		    	System.out.println("扣除惠币接口调用报错");
		    	System.out.println(updateCustAmt.getResultMessage());
		    	result.inErrorMessage(916427008);
				return result;
		    }
			
			String upCode = WebHelper.upCode("HB");
			JobExecHelper.createExecInfo("449746990034", upCode, null);
			
			
			String upDateTime = FormatHelper.upDateTime();
			insertmap.put("uid", UUID.randomUUID().toString().replaceAll("-", "").trim());
			insertmap.put("apply_code", upCode);
			insertmap.put("apply_money", inputParam.getApply_money());
			insertmap.put("name", inputParam.getName());
			insertmap.put("idcard_number", inputParam.getIdcard_number());
			insertmap.put("openid", inputParam.getOpenid());
			insertmap.put("status", "4497471600620001");
			insertmap.put("create_time", upDateTime);
			DbUp.upTable("fh_tgz_withdraw_info").dataInsert(insertmap);
			insertmap = new MDataMap();
			insertmap.put("uid", UUID.randomUUID().toString().replaceAll("-", "").trim());
			insertmap.put("code", upCode);
			insertmap.put("now_status", "4497471600620001");
			insertmap.put("remark", "申请提现");
			insertmap.put("create_user", inputParam.getName());
			insertmap.put("create_time", upDateTime);
			DbUp.upTable("lc_tgz_withdraw_status_log").dataInsert(insertmap);
		}else {
			result.inErrorMessage(916427004);
			return result;
		}
		return result;
	}

}
