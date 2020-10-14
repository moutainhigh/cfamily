package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 删除支付禁用商户
 */
public class FuncDeletePaymentTypeSeller extends FuncDelete {
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String uid = mAddMaps.get("uid");
		
		if(StringUtils.isNotBlank(uid)){
			MDataMap paymentTypeInfo = DbUp.upTable("fh_payment_type_seller").one("uid",uid);
			if(paymentTypeInfo != null){
				DbUp.upTable("fh_payment_type_seller").delete("uid",uid);
				String content = "在表《fh_payment_type_seller》 删除一条记录:"+JSON.toJSONString(paymentTypeInfo);
				TempletePageLog.upLog(content);
			}
		}
		
		return mResult;
	}
}
