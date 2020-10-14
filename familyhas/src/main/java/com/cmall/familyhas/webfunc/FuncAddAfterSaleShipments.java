package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加售后的物流信息
 */
public class FuncAddAfterSaleShipments extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap param = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		if(StringUtils.isBlank(param.get("order_code"))) {
			mResult.setResultCode(99);
			mResult.setResultMessage("售后单号不能为空");
			return mResult;
		}
		
		if(StringUtils.isBlank(param.get("logisticse_code"))) {
			mResult.setResultCode(99);
			mResult.setResultMessage("物流公司不能为空");
			return mResult;
		}
		
		if(StringUtils.isBlank(param.get("waybill"))) {
			mResult.setResultCode(99);
			mResult.setResultMessage("运单号不能为空");
			return mResult;
		}
		
		String now=DateUtil.getSysDateTimeString();
		param.put("remark", "");
		param.put("logisticse_name", (String)DbUp.upTable("sc_logisticscompany").dataGet("company_name", "company_code=:company_code", new MDataMap("company_code",param.get("logisticse_code"))));
		param.put("is_send100_flag", "1"); // 默认不需要订阅轨迹信息
		param.put("creator", UserFactory.INSTANCE.create().getUserCode());
		param.put("create_time", now);
		param.put("update_time", now);
		param.put("update_user", param.get("creator"));
		DbUp.upTable("oc_order_shipments").dataInsert(param);
		//补充运单号之后，将退货单状态改为待商户审核。20190905 NG++  4497477800030001:退货单走此流程
		String returnCode = param.get("order_code");
		MDataMap afterSaleOrder = DbUp.upTable("oc_order_after_sale").one("asale_code",returnCode);
		if(afterSaleOrder != null && "4497477800030001".equals(afterSaleOrder.get("asale_type"))) {//退货退款的订单
			DbUp.upTable("oc_return_goods").dataUpdate(new MDataMap("return_code",returnCode,"status","4497153900050004"), "status", "return_code");
		}
		
		String asale_type = afterSaleOrder.get("asale_type") == null ? "4497477800030001" : afterSaleOrder.get("asale_type").toString();
		String asale_status = "4497477800050005";
		if("4497477800030003".equals(asale_type)) {
			asale_status = "4497477800050013";
		}
		
		DbUp.upTable("oc_order_after_sale").dataUpdate(new MDataMap("asale_code", returnCode,"asale_status",asale_status), "asale_status", "asale_code");
		
		MDataMap loasMap=new MDataMap();
		loasMap.put("asale_code", returnCode);
		loasMap.put("create_user", afterSaleOrder.get("buyer_mobile"));
		loasMap.put("create_time", now);
		loasMap.put("asale_status", asale_status);
		loasMap.put("remark", "[客服填写物流信息]");
		loasMap.put("lac_code", WebHelper.upCode("LAC"));
		DbUp.upTable("lc_order_after_sale").dataInsert(loasMap);
		
		MDataMap lsasMap=new MDataMap();
		lsasMap.put("asale_code", returnCode);
		lsasMap.put("lac_code", loasMap.get("lac_code"));
		lsasMap.put("create_source", "4497477800070001");
		lsasMap.put("create_time", now);
		
		MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100026");
		MDataMap lcMap = DbUp.upTable("sc_logisticscompany").one("company_code",param.get("logisticse_code"));
		lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"),lcMap.get("company_name"),param.get("waybill")));
		lsasMap.put("serial_title", templateMap.get("template_title"));
		lsasMap.put("template_code", templateMap.get("template_code"));
		DbUp.upTable("lc_serial_after_sale").dataInsert(lsasMap);
		
		
		return mResult;
	}
}
