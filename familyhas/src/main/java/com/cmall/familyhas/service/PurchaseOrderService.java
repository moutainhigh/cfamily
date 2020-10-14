package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.model.AddressDetail;
import com.cmall.familyhas.model.OrderDetail;
import com.cmall.familyhas.model.PurchaseOrderInfos;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;



/**
 * 惠家有采购service
 * @author zhangbo
 *
 */
public class PurchaseOrderService extends BaseClass{ 

     
	public String getPurchaseOrderInfos(String orderId) {
		PurchaseOrderInfos infos = new PurchaseOrderInfos();
		if(StringUtils.isBlank(orderId)) {
			List<Map<String, Object>> orderAddressList = DbUp.upTable("oc_purchase_order_address").dataSqlList("select * from  oc_purchase_order_address where if_delete='1' order by zid desc", null);
			if(orderAddressList!=null&&orderAddressList.size()>0) {
				for (Map<String, Object> map : orderAddressList) {
					AddressDetail orderAddress = new AddressDetail();
					orderAddress.setAdress_id(map.get("adress_id").toString());
					orderAddress.setDetail_addtess(map.get("detail_addtess").toString());
					orderAddress.setIdentity_number(map.get("identity_number").toString());
					orderAddress.setPostcode(map.get("postcode").toString());
					orderAddress.setProvince_city_district_code(map.get("province_city_district_code").toString());
					orderAddress.setPurchase_order_id(map.get("purchase_order_id").toString());
					orderAddress.setReceiver(map.get("receiver").toString());
					orderAddress.setSelect_flag("0");
					orderAddress.setPhone(map.get("phone").toString());
					String pcdc = map.get("province_city_district_code").toString();
					String[] split = pcdc.split("_");
					String pcdv = "";
					for (String code : split) {
						MDataMap one = DbUp.upTable("sc_tmp").one("code",code);
						pcdv=pcdv+one.get("name");
					}
					orderAddress.setPcdv(pcdv);
					infos.getAddressDetailList().add(orderAddress);
				}
				
			}	
			return JSON.toJSONString(infos);
		}else {
			Map<String, Object> dataSqlOne = DbUp.upTable("oc_purchase_order").dataSqlOne("select * from oc_purchase_order where purchase_order_id=:purchase_order_id", new MDataMap("purchase_order_id",orderId));
			if(dataSqlOne!=null) {
				infos.getPurchaseBaseInfo().setAdress_id(dataSqlOne.get("adress_id").toString());
				infos.getPurchaseBaseInfo().setBasic_order_skus(dataSqlOne.get("basic_order_skus").toString());
				infos.getPurchaseBaseInfo().setPhone(dataSqlOne.get("phone").toString());
				infos.getPurchaseBaseInfo().setPurchase_money(dataSqlOne.get("purchase_money").toString());
				infos.getPurchaseBaseInfo().setPurchase_num(dataSqlOne.get("purchase_num").toString());
				infos.getPurchaseBaseInfo().setPurchase_order_id(dataSqlOne.get("purchase_order_id").toString());
				infos.getPurchaseBaseInfo().setPurchase_text(dataSqlOne.get("purchase_text").toString());
				
				List<Map<String, Object>> orderDetailList = DbUp.upTable("oc_purchase_order_detail").dataSqlList("select * from  oc_purchase_order_detail where purchase_order_id=:purchase_order_id and if_delete='1' ", new MDataMap("purchase_order_id",orderId));
				if(orderDetailList!=null&&orderDetailList.size()>0) {
				  for (Map<String, Object> map : orderDetailList) {
					  OrderDetail orderDetail = new OrderDetail();
					  orderDetail.setCost_money(map.get("cost_money").toString());
					  orderDetail.setProduct_code(map.get("product_code").toString());
					  orderDetail.setProduct_name(map.get("product_name").toString());
					  orderDetail.setPurchase_order_id(map.get("purchase_order_id").toString());
					  orderDetail.setSku_code(map.get("sku_code").toString());
					  orderDetail.setSku_num(map.get("sku_num").toString());
					  orderDetail.setIf_selected(map.get("if_selected").toString());
					  orderDetail.setSell_money(map.get("sell_money").toString());
					  orderDetail.setProduct_img(map.get("product_img").toString());
					  orderDetail.setProduct_property(map.get("product_property").toString());
					  BigDecimal sm =new BigDecimal(map.get("sell_money").toString());
					  BigDecimal multiply = sm.multiply(new BigDecimal(map.get("sku_num").toString()));
					  BigDecimal roundHalfUp = MoneyHelper.roundHalfUp(multiply);
					  orderDetail.setRowSumMoney(roundHalfUp.toString());
					  infos.getOrderDetailList().add(orderDetail);
				}
				}
				List<Map<String, Object>> orderAddressList = DbUp.upTable("oc_purchase_order_address").dataSqlList("select * from  oc_purchase_order_address where if_delete='1' order by zid desc",null);
				if(orderAddressList!=null&&orderAddressList.size()>0) {
					List<AddressDetail> paramList= new ArrayList<AddressDetail>();
					for (Map<String, Object> map : orderAddressList) {
						AddressDetail orderAddress = new AddressDetail();
						orderAddress.setAdress_id(map.get("adress_id").toString());
						orderAddress.setDetail_addtess(map.get("detail_addtess").toString());
						orderAddress.setIdentity_number(map.get("identity_number").toString());
						orderAddress.setPostcode(map.get("postcode").toString());
						orderAddress.setProvince_city_district_code(map.get("province_city_district_code").toString());
						orderAddress.setPurchase_order_id(map.get("purchase_order_id").toString());
						orderAddress.setReceiver(map.get("receiver").toString());
						if(dataSqlOne.get("adress_id").toString().equals(map.get("adress_id").toString())) {
							orderAddress.setSelect_flag("1");
						}else {
							orderAddress.setSelect_flag("0");
						}
						orderAddress.setPhone(map.get("phone").toString());
						String pcdc = map.get("province_city_district_code").toString();
						String[] split = pcdc.split("_");
						String pcdv = "";
						for (String code : split) {
							MDataMap one = DbUp.upTable("sc_tmp").one("code",code);
							pcdv=pcdv+one.get("name");
						}
						orderAddress.setPcdv(pcdv);
						paramList.add(orderAddress);
						
					}
					//选中的放在第一位
					Collections.sort(paramList, new Comparator<AddressDetail>() {
						@Override
						public int compare(AddressDetail o1, AddressDetail o2) {
							String detail1 = o1.getSelect_flag();
							String detail2 = o2.getSelect_flag();
							return detail2.compareTo(detail1);
						}
					});
					
					infos.setAddressDetailList(paramList);
					
				}	
				return JSON.toJSONString(infos);
			}
		}
	         return "";
	}
	



	public List<OrderDetail> getpurchaseOrderProducts(String parchaseId){
		List<OrderDetail> result  = new ArrayList<OrderDetail>();
		if(StringUtils.isNotBlank(parchaseId)) {
			List<Map<String, Object>> orderDetailList = DbUp.upTable("oc_purchase_order_detail").dataSqlList("select * from  oc_purchase_order_detail where purchase_order_id=:purchase_order_id and if_delete='1' and if_selected='1' order by zid desc  ", new MDataMap("purchase_order_id",parchaseId));
		   if(orderDetailList!=null&&orderDetailList.size()>0) {
			   for (Map<String, Object> map : orderDetailList) {
                   BigDecimal cost_money = new BigDecimal(map.get("cost_money").toString());
                   BigDecimal sell_money = new BigDecimal(map.get("sell_money").toString());
                   BigDecimal sku_num = new BigDecimal(map.get("sku_num").toString());
                   BigDecimal sumMoney = sell_money.multiply(sku_num);
                   cost_money=MoneyHelper.roundHalfUp(cost_money);
                   sell_money=MoneyHelper.roundHalfUp(sell_money);
                   sumMoney=MoneyHelper.roundHalfUp(sumMoney);
				   OrderDetail orderDetail = new OrderDetail();
				   orderDetail.setCost_money(cost_money.toString());
				   orderDetail.setProduct_code(map.get("product_code").toString());
				   orderDetail.setProduct_img(map.get("product_img").toString());
				   orderDetail.setProduct_name(map.get("product_name").toString());			   
				   orderDetail.setRowSumMoney(sumMoney.toString());
				   orderDetail.setSell_money(sell_money.toString());
				   orderDetail.setSku_code(map.get("sku_code").toString());
				   orderDetail.setSku_num(map.get("sku_num").toString());
				   orderDetail.setOrder_id(map.get("order_id").toString());
				   orderDetail.setProduct_property(map.get("product_property").toString());
				   result.add(orderDetail);
			}		
		   }
		}		
		return result;
	}
	
	public AddressDetail getpurchaseOrderAddressInfo(String parchaseId){
		AddressDetail orderAddress  = new AddressDetail();
		if(StringUtils.isNotBlank(parchaseId)) {
			Map<String, Object> dataSqlOne = DbUp.upTable("oc_purchase_order").dataSqlOne("select * from oc_purchase_order where purchase_order_id=:purchase_order_id", new MDataMap("purchase_order_id",parchaseId));
			String adress_id = dataSqlOne.get("adress_id").toString();
			Map<String, Object> map = DbUp.upTable("oc_purchase_order_address").dataSqlOne("select * from  oc_purchase_order_address where  adress_id=:adress_id ", new MDataMap("adress_id",adress_id));
            if(map!=null) {
            	orderAddress.setAdress_id(map.get("adress_id").toString());
    		 	String pcdc = map.get("province_city_district_code").toString();
    			String[] split = pcdc.split("_");
    			String pcdv = "";
    			for (String code : split) {
    				MDataMap one = DbUp.upTable("sc_tmp").one("code",code);
    				pcdv=pcdv+one.get("name");
    			}
    			orderAddress.setDetail_addtess(pcdv+map.get("detail_addtess").toString());
    			orderAddress.setIdentity_number(map.get("identity_number").toString());
    			orderAddress.setPostcode(map.get("postcode").toString());
    			orderAddress.setProvince_city_district_code(pcdc);
    			orderAddress.setPurchase_order_id(map.get("purchase_order_id").toString());
    			orderAddress.setReceiver(map.get("receiver").toString());
    			orderAddress.setSelect_flag(map.get("select_flag").toString());
    			orderAddress.setPhone(map.get("phone").toString());	
            }

		}		
		return orderAddress;
	}
	
}
