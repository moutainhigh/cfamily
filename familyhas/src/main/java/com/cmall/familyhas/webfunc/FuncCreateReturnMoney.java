package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.cmall.ordercenter.service.money.CreateMoneyService;
import com.cmall.ordercenter.service.money.ReturnMoneyResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 提供给后台人工创建退款单
 */
public class FuncCreateReturnMoney extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		String orderCode = mAddMaps.get("order_code");

		if(StringUtils.isBlank(orderCode)){
			mResult.setResultCode(0);
			mResult.setResultMessage("订单号不能为空");
			return mResult;
		}
		
		String updater = UserFactory.INSTANCE.create().getLoginName();
		
		CreateMoneyService createMoneyService = new CreateMoneyService();
		ReturnMoneyResult res = createMoneyService.creatReturnMoney(orderCode, updater, "人工创建退款单");
		mResult.setResultCode(res.getResultCode());
		mResult.setResultMessage(res.getResultMessage());
		
		if(res.getResultCode() == 1) {
			mResult.setResultMessage("退款单号："+res.getReturnMoneyCode());
		}
		
		return mResult;
	}
}
