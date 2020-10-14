package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 支付类型管理-修改
 */
public class FuncEditPaymentType extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MWebResult result = new MWebResult();
		
		String zid = mAddMaps.get("zid");
		String visible = mAddMaps.get("visible");
		
		MDataMap paymentType = DbUp.upTable("fh_payment_type").one("zid",zid);
		paymentType.put("visible", StringUtils.trimToEmpty(visible));
		DbUp.upTable("fh_payment_type").update(paymentType);
		
		String content = "在表《fh_payment_type》 更新一条记录:"+JSON.toJSONString(paymentType);
		TempletePageLog.upLog(content);
		
		return result;
		
	}

}
