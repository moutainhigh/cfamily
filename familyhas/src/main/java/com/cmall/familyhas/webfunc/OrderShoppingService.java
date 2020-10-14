package com.cmall.familyhas.webfunc;



import java.util.List;

import com.cmall.groupcenter.homehas.RsyncAlipayMoveInformation;
import com.cmall.groupcenter.job.AlipayMoveInformation;
import com.cmall.groupcenter.service.AlipayMoveInformationService;
import com.cmall.ordercenter.model.Order;
import com.cmall.ordercenter.model.OrderDetail;
import com.cmall.ordercenter.service.OrderService;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class OrderShoppingService extends BaseClass {
	/**
	 *根据订单号删除本订单对应的购物车中的商品 
	 */
	public boolean deleteSkuToShopCart(String orderCode){
		boolean flag = true;
		try {
			AlipayMoveInformationService alipayMoveInformationService = new AlipayMoveInformationService();
			alipayMoveInformationService.synchronizationAlipayMove(orderCode);   //同步支付宝数据
			
			MDataMap updateMap = new MDataMap();
			updateMap.put("order_code", orderCode);
			updateMap.put("order_status", "4497153900010002");
			updateMap.put("delete_flag", "0");
			DbUp.upTable("oc_orderinfo").dataUpdate(updateMap, "order_status,delete_flag", "order_code");
			
			OrderService service = new OrderService();
			Order order = service.getOrder(orderCode);
			List<OrderDetail> productList = order.getProductList();
			String buyerCode = order.getBuyerCode();
			if(productList!=null&&!productList.isEmpty()){
				for (int i = 0; i < productList.size(); i++) {
					OrderDetail de = productList.get(i);
					String skuCode = de.getSkuCode();
					DbUp.upTable("oc_shopCart").delete("buyer_code",buyerCode,"sku_code",skuCode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
}
