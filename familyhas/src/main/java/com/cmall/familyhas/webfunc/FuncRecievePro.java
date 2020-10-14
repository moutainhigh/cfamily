package com.cmall.familyhas.webfunc;

import com.cmall.ordercenter.service.OrderService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 确认收货
 * @author jlin
 *
 */
public class FuncRecievePro extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		
		String order_code = mDataMap.get("zw_f_uid");
		
		OrderService orderService = new OrderService();
		RootResult result = orderService.changForRecieveByUser(order_code,UserFactory.INSTANCE.create().getUserCode());
		mResult.setResultCode(result.getResultCode());
		mResult.setResultMessage(result.getResultMessage());
		
		return mResult;
	}
}
