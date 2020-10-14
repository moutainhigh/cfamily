package com.cmall.familyhas.api;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForGetRelAmtInput;
import com.cmall.familyhas.api.result.APiForGetRelAmtResult;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 查询用户的积分、储值金、暂存款
 */
public class ApiForGetRelAmt extends RootApiForVersion<APiForGetRelAmtResult, ApiForGetRelAmtInput>{

	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
	
	@Override
	public APiForGetRelAmtResult Process(ApiForGetRelAmtInput inputParam, MDataMap mRequestMap) {
		if(!getFlagLogin()) return new APiForGetRelAmtResult();
		
		String custId = plusServiceAccm.getCustId(getOauthInfo().getUserCode());
		GetCustAmtResult amtRef = null;
		if(StringUtils.isNotBlank(custId)) {
			amtRef = plusServiceAccm.getPlusModelCustAmt(custId);
		}
		
		APiForGetRelAmtResult result = new APiForGetRelAmtResult();
		if(amtRef != null) {
			result.setPossAccmAmt(plusServiceAccm.moneyToAccmAmt(amtRef.getPossAccmAmt(),0, BigDecimal.ROUND_FLOOR).intValue());
			result.setPossCrdtAmt(amtRef.getPossCrdtAmt());
			result.setPossPpcAmt(amtRef.getPossPpcAmt());
		}
		
		return result;
	}
	
}
