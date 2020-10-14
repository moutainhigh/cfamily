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
 * 切蛋糕送积分失败时定时重新赋予积分
 * @author lgx
 *
 */
public class JobForGiveIntegralCutCake extends RootJobForExec {

	@Override
	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();
		String temp[] = sInfo.split(",");
		if (temp.length<2) {
			mWebResult.setResultCode(0);
			mWebResult.setResultMessage("参数不正确");
			return mWebResult;
		}
		
		String memberCode = temp[0];
		BigDecimal integral = new BigDecimal(temp[1]);
		String eventCode = temp[2];
		String custId = "";
		if(temp.length >= 4) {			
			custId = temp[3];
		}
		
		PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
		if(null == custId || "".equals(custId)) {
			custId = plusServiceAccm.getCustId(memberCode);
		}
		RootResult rootResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.CC, integral, custId, "", eventCode);

		// 记录积分变更日志
		if (rootResult.getResultCode() == 1) {
			MDataMap changeDataMap = new MDataMap();
			changeDataMap.put("member_code", memberCode);
			changeDataMap.put("cust_id", custId);
			changeDataMap.put("change_type", "449748080016");
			changeDataMap.put("change_money", integral.toString());
			changeDataMap.put("remark", "切蛋糕定时送积分");
			changeDataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("mc_member_integral_change").dataInsert(changeDataMap);
		}

		mWebResult.inOtherResult(rootResult);
		return mWebResult;
		
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990032");
		config.setMaxExecNumber(10);
		return config;
	}

}
