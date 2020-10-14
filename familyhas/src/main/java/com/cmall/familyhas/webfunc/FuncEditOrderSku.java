package com.cmall.familyhas.webfunc;


import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.service.OrderEditService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改订单的sku
 */
public class FuncEditOrderSku extends FuncAdd{
	
	static OrderEditService editService = new OrderEditService();

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String orderCode = StringUtils.trimToEmpty(mDataMap.get("orderCode"));
		String skuCode = StringUtils.trimToEmpty(mDataMap.get("skuCode"));
		String newSkuCode = StringUtils.trimToEmpty(mDataMap.get("newSkuCode"));
		
		RootResult rs = editService.checkOrderSkuEditLimit(orderCode, skuCode);
		if(rs.getResultCode() != 1) {
			mResult.setResultCode(0);
			mResult.setResultMessage(rs.getResultMessage());
			return mResult;
		}
		
		if(StringUtils.isBlank(skuCode) || StringUtils.isBlank(newSkuCode)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("SKU编号参数有误");
			return mResult;
		}
		
		// 锁定2分钟
		String lockRes = KvHelper.lockCode("FuncEditOrderSku-"+orderCode, 120000);
		if(StringUtils.isBlank(lockRes)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("订单正在修改中，请稍后再操作");
			return mResult;
		}
		
		try {
			RootResult res = editService.updateOrderSku(orderCode, skuCode, newSkuCode);
			mResult.inOtherResult(res);
		} catch (Exception e) {
			e.printStackTrace();
			mResult.setResultCode(0);
			mResult.setResultMessage("系统异常");
		} finally {
			KvHelper.unlockCode("FuncEditOrderSku-"+orderCode, lockRes);
		}
		
		return mResult;
	}
}
