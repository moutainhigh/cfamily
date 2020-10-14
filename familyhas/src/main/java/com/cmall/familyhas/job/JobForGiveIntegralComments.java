package com.cmall.familyhas.job;

import java.math.BigDecimal;

import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 取消退货需要重新赋予积分
 */
public class JobForGiveIntegralComments extends RootJobForExec {

	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();

	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();
		String temp[] = sInfo.split(",");
		if (temp.length<5) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("参数不正确");
			return mWebResult;
		}
		String memberCode = temp[0];
		BigDecimal integral = new BigDecimal(temp[1]);
		String custId = temp[2];
		String orderCode = temp[3];
		String skuCode = temp[4];
		
		RootResult rootResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.GI, integral, custId, orderCode, skuCode);

		// 记录积分变更日志
		if (rootResult.getResultCode() == 1) {
			MDataMap changeDataMap = new MDataMap();
			changeDataMap.put("member_code", memberCode);
			changeDataMap.put("cust_id", custId);
			changeDataMap.put("change_type", "449748080010");
			changeDataMap.put("change_money", integral.toString());
			changeDataMap.put("remark", skuCode);
			changeDataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("mc_member_integral_change").dataInsert(changeDataMap);
		}

		mWebResult.inOtherResult(rootResult);
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990021");
		config.setMaxExecNumber(5);
		return config;
	}

}
