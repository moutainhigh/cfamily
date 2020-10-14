package com.cmall.familyhas.webfunc;


import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加无库存取消订单数据
 */
public class AddOrderCancelNoStore extends RootFunc{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		String orderCode = StringUtils.trimToEmpty(mAddMaps.get("order_code"));
		if(StringUtils.isBlank(orderCode)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("订单号不能为空");
			return mResult;
		}
		
		if(DbUp.upTable("oc_orderinfo").count("order_code", orderCode, "small_seller_code", "SI2003") > 0) {
			mResult.setResultCode(0);
			mResult.setResultMessage("不支持添加LD订单");
			return mResult;
		}
		
		if(DbUp.upTable("lc_orderstatus").count("code", orderCode, "now_status", "4497153900010002") == 0) {
			mResult.setResultCode(0);
			mResult.setResultMessage("未支付订单");
			return mResult;
		}
		
		if(DbUp.upTable("oc_order_cancel_nostore").count("order_code", orderCode) > 0) {
			mResult.setResultCode(0);
			mResult.setResultMessage("数据已存在");
			return mResult;
		}
		
		MDataMap stautsMap = DbUp.upTable("lc_orderstatus").one("code", orderCode, "now_status", "4497153900010006");
		
		if(stautsMap == null) {
			mResult.setResultCode(0);
			mResult.setResultMessage("未查询到订单取消时间");
			return mResult;
		}
		
		MDataMap mInsertMaps = new MDataMap();
		mInsertMaps.put("order_code", orderCode);
		mInsertMaps.put("cancel_time", stautsMap.get("create_time"));
		mInsertMaps.put("creator", UserFactory.INSTANCE.create().getLoginName());
		mInsertMaps.put("create_time", FormatHelper.upDateTime());
	    DbUp.upTable("oc_order_cancel_nostore").dataInsert(mInsertMaps);
	    
		return mResult;
	}
	
}
