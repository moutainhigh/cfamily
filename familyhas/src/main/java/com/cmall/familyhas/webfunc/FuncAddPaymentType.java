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
 * 支付类型管理-新增
 */
public class FuncAddPaymentType extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MWebResult result = new MWebResult();
		
		String visible = mAddMaps.get("visible");
		String payType = mAddMaps.get("pay_type");
		if(StringUtils.isBlank(payType)){
			result.setResultCode(0);
			result.setResultMessage("支付类型编码不能为空");
			return result;
		}
		
		MDataMap payTypeInfo = DbUp.upTable("sc_define").one("parent_code","44974628","define_code",payType);
		if(payTypeInfo == null){
			result.setResultCode(0);
			result.setResultMessage("支付类型编码错误");
			return result;
		}
		
		if(DbUp.upTable("fh_payment_type").count("pay_type",payType) > 0){
			result.setResultCode(0);
			result.setResultMessage("支付类型已存在");
			return result;
		}
		
		MDataMap paymentType = new MDataMap();
		paymentType.put("pay_type", payType);
		paymentType.put("pay_name", payTypeInfo.get("define_name"));
		paymentType.put("visible", StringUtils.trimToEmpty(visible));
		DbUp.upTable("fh_payment_type").dataInsert(paymentType);
		
		String content = "在表《fh_payment_type》 添加一条记录:"+JSON.toJSONString(paymentType);
		TempletePageLog.upLog(content);
		
		return result;
		
	}

}
