package com.cmall.familyhas.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.cmall.familyhas.api.input.ld.ApiForReturnLdInput;
import com.cmall.familyhas.api.result.ld.ApiForReturnLdResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 用户申请退货接口
 * @author AngelJoy
 *
 */
public class ApiForApplyForReturnLd extends RootApiForToken<ApiForReturnLdResult,ApiForReturnLdInput>{

	
	@Override
	public ApiForReturnLdResult Process(ApiForReturnLdInput inputParam,
			MDataMap mRequestMap) {
		ApiForReturnLdResult result = new ApiForReturnLdResult();
		String orderCode = inputParam.getOrderCode();
		String skuCode = inputParam.getSkuCode();
		MDataMap order = DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
		if(order == null){
			result.setResultCode(10000);
			result.setResultMessage("订单不存在或其他异常");
			return result;
		}
		String orderStatus = order.get("order_status")!=null?order.get("order_status").toString():"";
		if(!"4497153900010005".equals(orderStatus)){//交易成功的单子才可以
			result.setResultCode(10000);
			result.setResultMessage("订单状态不为【交易成功】");
			return result;
		}
		//根据该订单时间判断是否超过退货时间，目前写死是15天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String complateDate = order.get("update_time")!=null?order.get("update_time").toString():"2018-01-01 00:00:00";
		Date now = new Date();
		try {
			Date beforeDate = sdf.parse(complateDate);
			if((now.getTime()-beforeDate.getTime())>(15*24*60*60*1000)){//超过15天的订单
				result.setResultCode(10000);
				result.setResultMessage("订单已超过允许申请退换货时间");
				return result;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		Integer count = inputParam.getCount();
//		String sSql = "SELECT SUM(sku_num) count FROM ordercenter.oc_orderdetail where order_code = '"+orderCode+"' and sku_code = '"+skuCode+"'";
//		Map<String,Object> map = DbUp.upTable("oc_orderdetail").dataSqlOne(sSql, null);
//		Integer total = Integer.parseInt(map.get("count")!=null?map.get("count").toString():"0");
//		//4497477800050007 = 退货失败,不等于这个状态的有多少个
//		sSql = "SELECT COUNT(1) count FROM ordercenter.oc_after_sale_ld where order_code = '"+orderCode+"' and sku_code = '"+skuCode+"' and after_sale_status != '4497477800050007'";
//		Map<String,Object> map2 = DbUp.upTable("oc_after_sale_ld").dataSqlOne(sSql, null);
//		Integer underWay = Integer.parseInt(map2.get("count")!=null?map2.get("count").toString():"0");
		Integer allowApplyCount = 999;//LD接口提供可允许申请个数。
		if(count>allowApplyCount){
			result.setResultCode(10000);
			result.setResultMessage("退货个数超过允许退货数");
			return result;
		}
		String memberCode = this.getUserCode();
		MDataMap map3 = DbUp.upTable("oc_orderdetail").one("order_code",orderCode,"sku_code",skuCode);
		for(int i = 0;i<count;i++){
			String afterSaleCodeApp = "RE"+System.currentTimeMillis()+"LD";
			MDataMap afterSale = new MDataMap();
			afterSale.put("member_code", memberCode);
			afterSale.put("product_code", map3.get("product_code"));
			afterSale.put("sku_code", skuCode);
			afterSale.put("order_code", orderCode);
			afterSale.put("after_sale_code_app", afterSaleCodeApp);//TODO
			afterSale.put("after_sale_status", "4497477800050003");//4497477800050003 = 待审核状态
			afterSale.put("create_time",sdf.format(now));
			DbUp.upTable("oc_after_sale_ld").dataInsert(afterSale);
			//记录日志
			MDataMap log = new MDataMap();
			log.put("after_sale_code_app", afterSaleCodeApp);
			log.put("after_sale_status", "4497477800050003");
			log.put("create_time", sdf.format(now));
			log.put("remark", "用户申请退货");
			log.put("operator", "system");
			DbUp.upTable("lc_after_sale_ld_return").dataInsert(log);
		}
		result.setResultCode(1);
		result.setResultMessage("申请成功，请耐心等待客服审核");
		return result;
	}
	
}
