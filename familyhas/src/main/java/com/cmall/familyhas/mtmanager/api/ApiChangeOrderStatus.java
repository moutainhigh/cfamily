package com.cmall.familyhas.mtmanager.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.mtmanager.inputresult.ApiChangeOrderStatusInput;
import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;

public class ApiChangeOrderStatus extends
	RootApiForMember<RootResult, ApiChangeOrderStatusInput> {
	private String string;

	public RootResult Process(ApiChangeOrderStatusInput inputParam,MDataMap mRequestMap) {
		RootResult result = new RootResult();
		List<String> orderCodes = inputParam.getOrderCode();
		String orderStatus = inputParam.getOrderStatus();
		if (null !=  orderCodes && !orderCodes.isEmpty()) {
			List<MDataMap> orderstatusmap = DbUp.upTable("oc_orderinfo").queryAll("zid,uid,order_status,order_code", "", "order_code in ('"+StringUtils.join(orderCodes,"','")+"')", null);
			if (null != orderstatusmap) {
				for (MDataMap orderMap : orderstatusmap) {
					if (!orderStatus.equals(orderMap.get("order_status"))) {
						MDataMap updateData = new MDataMap();
						updateData.put("zid", orderMap.get("zid"));
						updateData.put("uid", orderMap.get("uid"));
						updateData.put("order_code", orderMap.get("order_code"));
						updateData.put("order_status", orderStatus);
						if (DbUp.upTable("oc_orderinfo").update(updateData) > 0) {
							//同步统计订单绑定的分销人的金额统计
							MDataMap one = DbUp.upTable("oc_orderdetail").one("order_code",orderMap.get("order_code"));
							if("4497153900010005".equals(orderStatus)&&one!=null) {//交易成功
								String distribution_member_id= one.get("distribution_member_id");
								BigDecimal sku_price = BigDecimal.valueOf(Double.parseDouble(one.get("sku_price").toString()));
								Map<String, Object> dataSqlOne = DbUp.upTable("oc_distribution_info").dataSqlOne("select * from oc_distribution_info where distribution_member_id=:distribution_member_id", new MDataMap("distribution_member_id",distribution_member_id));
								if(dataSqlOne!=null) {
									Integer  order_success_num = Integer.parseInt(dataSqlOne.get("order_success_num").toString());
									BigDecimal order_success_value = BigDecimal.valueOf(Double.parseDouble(dataSqlOne.get("order_success_value").toString()));
									order_success_num = order_success_num+1;
									order_success_value=order_success_value.add(sku_price);
									DbUp.upTable("oc_distribution_info").dataUpdate(new MDataMap("order_success_num",order_success_num.toString(),"order_success_value",order_success_value.toString(),"distribution_member_id",distribution_member_id), "order_success_num,order_success_value", "distribution_member_id");
								}
							}
							else if("4497153900010006".equals(orderStatus)) {//交易失败
								String distribution_member_id= one.get("distribution_member_id");
								BigDecimal sku_price = BigDecimal.valueOf(Double.parseDouble(one.get("sku_price").toString()));
								Map<String, Object> dataSqlOne = DbUp.upTable("oc_distribution_info").dataSqlOne("select * from oc_distribution_info where distribution_member_id=:distribution_member_id", new MDataMap("distribution_member_id",distribution_member_id));
								if(dataSqlOne!=null) {
									Integer  order_fail_num = Integer.parseInt(dataSqlOne.get("order_fail_num").toString());
									BigDecimal order_fail_value = BigDecimal.valueOf(Double.parseDouble(dataSqlOne.get("order_fail_value").toString()));
									order_fail_num = order_fail_num+1;
									order_fail_value=order_fail_value.add(sku_price);
									DbUp.upTable("oc_distribution_info").dataUpdate(new MDataMap("order_fail_num",order_fail_num.toString(),"order_fail_value",order_fail_value.toString(),"distribution_member_id",distribution_member_id), "order_fail_num,order_fail_value", "distribution_member_id");
								}
							}
							DbUp.upTable("lc_orderstatus").insert("code",orderMap.get("order_code"),"info","同步订单接口更改订单状态",
									"create_time",DateUtil.getSysDateTimeString(),"create_user",(getFlagLogin()?getOauthInfo().getUserCode() : ""),"old_status",orderMap.get("order_status"),
									"now_status",orderStatus);
						}
						
					}
				}
			}
		}
		
		return result;
	}

}
