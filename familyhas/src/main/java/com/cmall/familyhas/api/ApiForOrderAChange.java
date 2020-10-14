package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForOrderAChangeInput;
import com.cmall.familyhas.api.result.ApiForOrderAChangeResult;
import com.cmall.familyhas.api.result.ApiForOrderAChangeResult.Detail;
import com.cmall.groupcenter.duohuozhu.support.DuohzAfterSaleSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 后台专用订单详情接口
 * <br>后台不支持细化的接口权限 故暂时不做权限划分
 * @author jlin
 *
 */
public class ApiForOrderAChange extends RootApi<ApiForOrderAChangeResult, ApiForOrderAChangeInput> {

	public ApiForOrderAChangeResult Process(ApiForOrderAChangeInput inputParam,MDataMap mRequestMap) {
		
		ApiForOrderAChangeResult orderAChangeResult = new ApiForOrderAChangeResult();
		
		String order_code=inputParam.getOrder_code();
		
		MDataMap orderInfo=DbUp.upTable("oc_orderinfo").one("order_code",order_code);
		if(orderInfo==null||orderInfo.isEmpty()){
			orderAChangeResult.inErrorMessage(916422111, order_code);
			return orderAChangeResult;
		}
		String order_source = orderInfo.get("order_source").toString();
		if(StringUtils.isBlank(inputParam.getDo_type())){
			if("449715190014".equals(order_source)){
				orderAChangeResult.setResultCode(2);
				orderAChangeResult.setResultMessage("该订单为多彩宝订单,不允许退货!!!");
				return orderAChangeResult;
			}
		}		
		BigDecimal ord_amt = new BigDecimal(orderInfo.get("order_money"));
		orderAChangeResult.setOrder_code(order_code);
		orderAChangeResult.setOrder_money(ord_amt);
		orderAChangeResult.setOrder_status(orderInfo.get("order_status"));
		orderAChangeResult.setBuyer_mobile(DbUp.upTable("mc_login_info").one("member_code",orderInfo.get("buyer_code")).get("login_name"));
		if(inputParam.getDo_type().equals("D")){
			String sSql = "SELECT * from systemcenter.sc_detain_configure WHERE start_amt <= "+ord_amt+" AND end_amt > "+ord_amt;
			Map<String, Object> map = DbUp.upTable("uc_detain_integral").dataSqlOne(sSql, null);
			orderAChangeResult.setIntegral(map.get("integral")==null?0:Integer.parseInt(map.get("integral").toString()));
		}
		
		//售后地址
//		MDataMap addressInfo=DbUp.upTable("oc_address_info").one("small_seller_code",orderInfo.get("small_seller_code"));
		MDataMap odetail = DbUp.upTable("oc_orderdetail").one("order_code",order_code,"gift_flag","1");
		MDataMap addressInfo = new MDataMap();
		MDataMap product = DbUp.upTable("pc_productinfo").one("product_code",odetail.get("product_code"));
		if(product.get("after_sale_address_uid") != null && !"".equals(product.get("after_sale_address_uid"))) {
			addressInfo = DbUp.upTable("oc_address_info").one("uid",product.get("after_sale_address_uid"),"small_seller_code",orderInfo.get("small_seller_code"));
		}else {
			addressInfo = DbUp.upTable("oc_address_info").oneWhere("", "zid desc ", "small_seller_code=:small_seller_code", "small_seller_code",orderInfo.get("small_seller_code"));
		}
		//多货主售后地址特殊处理
		if("4497471600430002".equals(orderInfo.get("delivery_store_type"))) {
			addressInfo = new DuohzAfterSaleSupport().getDuohzAfterSaleAddr(orderInfo.get("order_code"));
		}
		if(addressInfo!=null&&!addressInfo.isEmpty()){
			orderAChangeResult.setContacts(addressInfo.get("after_sale_person"));
			orderAChangeResult.setAddress(addressInfo.get("after_sale_address"));
			orderAChangeResult.setReceiver_area_code(addressInfo.get("after_sale_postcode"));
			orderAChangeResult.setMobile(addressInfo.get("after_sale_mobile"));
			orderAChangeResult.setSmall_seller_code(orderInfo.get("small_seller_code"));
		}
		
		
		List<MDataMap> orderDetails=DbUp.upTable("oc_orderdetail").queryAll("", "", "order_code=:order_code", new MDataMap("order_code",order_code));
		for (MDataMap orderDetail : orderDetails) {
			Detail detail = new Detail();
			detail.setSku_code(orderDetail.get("sku_code"));
			detail.setProduct_code(orderDetail.get("product_code"));
			detail.setSku_name(orderDetail.get("sku_name"));
			detail.setSku_price(new BigDecimal(orderDetail.get("sku_price")));
			detail.setSku_num(Integer.valueOf(orderDetail.get("sku_num")));
			//此处不考虑换货的情况   目前的规则，退货进行中不能换货，换货进行中不能退货//////////////
			
			String sql="SELECT SUM(oac_num) oac_num from oc_order_achange where order_code=:order_code and sku_code=:sku_code and oac_status=:oac_status and oac_type=:oac_type ";
			Map<String, Object> map=DbUp.upTable("oc_order_achange").dataSqlOne(sql, new MDataMap("order_code",order_code,"sku_code",detail.getSku_code(),"oac_status","4497477800040002","oac_type","4497477800030001"));
			if(map==null||map.isEmpty()){
				detail.setReturn_num(0);
			}else{
				if(map.get("oac_num")==null){
					detail.setReturn_num(0);
				}else{
					detail.setReturn_num(Integer.valueOf(String.valueOf(map.get("oac_num"))));
				}
			}
			
			Map<String, Object> map1=DbUp.upTable("oc_order_achange").dataSqlOne(sql, new MDataMap("order_code",order_code,"sku_code",detail.getSku_code(),"oac_status","4497477800040001","oac_type","4497477800030001"));
			if(map1==null||map1.isEmpty()){
				detail.setOccupy_num(0);
			}else{
				if(map1.get("oac_num")==null){
					detail.setOccupy_num(0);
				}else{
					detail.setOccupy_num(Integer.valueOf(String.valueOf(map1.get("oac_num"))));
				}
			}
			
			orderAChangeResult.getDetails().add(detail);
		}
		return orderAChangeResult;
	}
	
//	private MObjMap<String, ReturnGoodsInfo> retNum(String order_code) {
//		MObjMap<String, ReturnGoodsInfo> retNum = new MObjMap<String, ReturnGoodsInfo>();
//		List<Map<String, Object>> rgs = DbUp
//				.upTable("oc_return_goods")
//				.dataSqlList(
//						"SELECT d.sku_code,d.count,g.`status`,d.count from oc_return_goods g RIGHT JOIN  oc_return_goods_detail d on g.return_code=d.return_code where g.`status` in ('4497153900050001','4497153900050003')  and g.order_code=:order_code",
//						new MDataMap("order_code", order_code));
//		for (Map<String, Object> map : rgs) {
//			String sku_code = (String) map.get("sku_code");
//			String status = (String) map.get("status");
//			int oac_num = (Integer) map.get("count");
//
//			ReturnGoodsInfo goodsInfo = retNum.get(sku_code);
//			if (goodsInfo == null) {
//				goodsInfo = new ReturnGoodsInfo();
//				retNum.put(sku_code, goodsInfo);
//			}
//
//			if (StringUtils.equals(status, "4497153900050003")) {// 进行中
//				goodsInfo.setOccupy_num(goodsInfo.getOccupy_num() + oac_num);
//			} else if (StringUtils.equals(status, "4497153900050001")) {// 成功的
//				goodsInfo.setReturn_num(goodsInfo.getReturn_num() + oac_num);
//			}
//		}
//		return retNum;
//	}
}
