package com.cmall.familyhas.webfunc;



import java.util.List;

import com.cmall.groupcenter.homehas.RsyncAlipayMoveInformation;
import com.cmall.groupcenter.job.AlipayMoveInformation;
import com.cmall.groupcenter.service.AlipayMoveInformationService;
import com.cmall.ordercenter.model.Order;
import com.cmall.ordercenter.model.OrderDetail;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.ordercenter.service.OrderShoppingService;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class FamilyOrderShoppingService extends BaseClass {
	/**
	 *根据订单号删除本订单对应的购物车中的商品   并且同步支付数据
	 */
	public boolean deletefamilySkuToShopCart(String orderCode){

			AlipayMoveInformationService alipayMoveInformationService = new AlipayMoveInformationService();
			alipayMoveInformationService.synchronizationAlipayMove(orderCode);   //同步支付宝数据
			OrderShoppingService orderShoppingService = new OrderShoppingService();
			boolean trueAndFalse = orderShoppingService.deleteSkuToShopCart(orderCode);
			return trueAndFalse;

	}
	/**
	 *根据订单号删除本订单对应的购物车中的商品 
	 */
	public boolean deletefamilySkuToShopCartNotSynchronization(String orderCode){

		OrderShoppingService orderShoppingService = new OrderShoppingService();
		boolean trueAndFalse = orderShoppingService.deleteSkuToShopCart(orderCode);
		return trueAndFalse;

}
	
}
