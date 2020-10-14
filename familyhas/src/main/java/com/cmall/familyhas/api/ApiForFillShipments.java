package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiForFillShipmentsInput;
import com.cmall.familyhas.api.result.ApiForFillShipmentsResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.Constants;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webwx.WxGateSupport;

/**
 * 提交退换货物流信息
 * 
 * @author jlin
 *
 */
public class ApiForFillShipments extends RootApiForToken<ApiForFillShipmentsResult, ApiForFillShipmentsInput> {

	@Override
	public ApiForFillShipmentsResult Process(ApiForFillShipmentsInput inputParam, MDataMap mRequestMap) {

		ApiForFillShipmentsResult result = new ApiForFillShipmentsResult();

		String afterCode = inputParam.getAfterCode();
		String buyer_code = getUserCode();
		String tips = inputParam.getTips();
		String logisticsCompanyName = inputParam.getLogisticsCompanyName();
		String logisticsCode = inputParam.getLogisticsCode();
		List<String> logisticsPic = inputParam.getLogisticsPic();
		
		MDataMap asMap = DbUp.upTable("oc_order_after_sale").one("asale_code", afterCode, "buyer_code", buyer_code);
		if (asMap == null || asMap.isEmpty()) {
			result.setResultCode(916422153);
			result.setResultMessage(bInfo(916422153));
			return result;
		}
		
		MDataMap lcMap = DbUp.upTable("sc_logisticscompany").one("company_code",logisticsCompanyName);
		if (lcMap == null || lcMap.isEmpty()) {
			result.setResultCode(916422154);
			result.setResultMessage(bInfo(916422154));
			return result;
		}
		
		String shipments_code=WebHelper.upCode("SCC");
		
		MDataMap shipmentsMap = new MDataMap();
		shipmentsMap.put("order_code", afterCode);
		shipmentsMap.put("logisticse_code", logisticsCompanyName);
		shipmentsMap.put("logisticse_name", lcMap.get("company_name"));
		shipmentsMap.put("waybill", logisticsCode);
		shipmentsMap.put("creator", buyer_code);
		shipmentsMap.put("create_time", DateUtil.getSysDateTimeString());
		shipmentsMap.put("remark", tips);
		shipmentsMap.put("is_send100_flag", "0");
		shipmentsMap.put("send_count", "0");
		shipmentsMap.put("update_time", shipmentsMap.get("create_time"));
		shipmentsMap.put("update_user", buyer_code);
		shipmentsMap.put("shipments_code", shipments_code);
		DbUp.upTable("oc_order_shipments").dataInsert(shipmentsMap);
		
		MDataMap extMap = new MDataMap();
		extMap.put("order_code", afterCode);
		extMap.put("shipments_code", shipments_code);
		extMap.put("waybill", logisticsCode);
		
		if(logisticsPic!=null&&!logisticsPic.isEmpty()){
			for (int j = 0; j < logisticsPic.size(); j++) {
				if(j>2){
					break;
				}
				extMap.put("waybill_pic"+(j+1), logisticsPic.get(j));
			}
		}
		
		extMap.put("waybill_remark", tips);
		extMap.put("create_time", shipmentsMap.get("create_time"));
		extMap.put("create_user", "用户");
		
		DbUp.upTable("oc_order_shipments_ext").dataInsert(extMap);
		String asale_type = asMap.get("asale_type") == null ? "4497477800030001" : asMap.get("asale_type").toString();
		String asale_status = "4497477800050005";
		if("4497477800030003".equals(asale_type)) {
			asale_status = "4497477800050013";
		}
		
		DbUp.upTable("oc_order_after_sale").dataUpdate(new MDataMap("asale_code", afterCode,"asale_status",asale_status), "asale_status", "asale_code");
		String now=DateUtil.getSysDateTimeString();
		
		MDataMap loasMap=new MDataMap();
		loasMap.put("asale_code", afterCode);
		loasMap.put("create_user", asMap.get("buyer_mobile"));
		loasMap.put("create_time", now);
		loasMap.put("asale_status", asale_status);
		loasMap.put("remark", "[用户填写物流信息]"+tips);
		loasMap.put("lac_code", WebHelper.upCode("LAC"));
		DbUp.upTable("lc_order_after_sale").dataInsert(loasMap);
		
		MDataMap lsasMap=new MDataMap();
		lsasMap.put("asale_code", afterCode);
		lsasMap.put("lac_code", loasMap.get("lac_code"));
		lsasMap.put("create_source", "4497477800070002");
		lsasMap.put("create_time", now);
		
		MDataMap templateMap=DbUp.upTable("oc_order_after_sale_template").one("template_code","OST160312100020");
		lsasMap.put("serial_msg", FormatHelper.formatString(templateMap.get("template_context"),lcMap.get("company_name"),logisticsCode));
		lsasMap.put("serial_title", templateMap.get("template_title"));
		lsasMap.put("template_code", templateMap.get("template_code"));
		DbUp.upTable("lc_serial_after_sale").dataInsert(lsasMap);
		
		//完善退换货物流信息需要变更售后单状态，新增功能20190905 NG++
		if(asMap != null && "4497477800030001".equals(asMap.get("asale_type"))) {//退货退款的订单
			DbUp.upTable("oc_return_goods").dataUpdate(new MDataMap("return_code",afterCode,"status","4497153900050004"), "status", "return_code");
		}
		
		// 京东订单完善物流信息时发送消息给客服
		if(Constants.SMALL_SELLER_CODE_JD.equals(asMap.get("small_seller_code"))) {
			String receives = bConfig("groupcenter.jd_notice_receives_aftersale");
			List<String> list = new WxGateSupport().queryOpenId(receives);
			for(String v : list) {
				new WxGateSupport().sendWarnCountMsg("京东服务单", "完善物流信息", v, "["+afterCode+"]客户提交了物流信息，请确认运费金额后提交物流信息到京东");
			}
		}
		
		return result;
	}

}
