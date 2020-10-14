package com.cmall.familyhas.webfunc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加退货售后原因
 * @author zmm
 *
 */

public class FuncAddReturnGoodsReason extends FuncAdd {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String return_reason_code = WebHelper.upCode("RGR");
		String return_reson = mAddMaps.get("return_reson");
		String after_sales_type = mAddMaps.get("after_sales_type");
		String status = mAddMaps.get("status");
		String app_name = mAddMaps.get("app_name");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		String create_time = df.format(calendar.getTime()).toString();
		String create_user=UserFactory.INSTANCE.create().getLoginName();
		MDataMap reasonMap = new MDataMap();
		reasonMap.put("return_reson", return_reson);
		reasonMap.put("after_sales_type", after_sales_type);
		reasonMap.put("status", status);
		reasonMap.put("app_name", app_name);
		if (app_name.equals("SI2003")) {
			reasonMap.put("app_code", "SI2003");
		} else {
			reasonMap.put("app_code", "SI3003");
		}
		reasonMap.put("return_reson_code", return_reason_code);
		reasonMap.put("create_time", create_time);
		reasonMap.put("create_user", create_user);
		DbUp.upTable("oc_return_goods_reason").dataInsert(reasonMap);
		return mResult;

	}
}
