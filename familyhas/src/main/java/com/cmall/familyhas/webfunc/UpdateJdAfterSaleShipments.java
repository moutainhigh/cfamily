package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.groupcenter.jd.JdAfterSaleSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 上传客户退货的物流信息到京东系统
 */
public class UpdateJdAfterSaleShipments extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String afterSaleCode = mDataMap.get("afterSaleCode");
		MDataMap jdAfterSale = DbUp.upTable("oc_order_jd_after_sale").one("asale_code", afterSaleCode);
		if(jdAfterSale == null || StringUtils.isBlank(jdAfterSale.get("afs_service_id"))) {
			mResult.setResultCode(99);
			mResult.setResultMessage("未查询到京东服务单编号");
			return mResult;
		}
		
		RootResult res = new JdAfterSaleSupport().execSendSkuUpdate(afterSaleCode);
		mResult.inOtherResult(res);
		return mResult;
	}

}
