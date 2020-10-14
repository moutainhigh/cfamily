package com.cmall.familyhas.model;

import java.util.ArrayList;
import java.util.List;
import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	采购信息
*   zhangbo
*/
public class PurchaseOrderInfos  {
	
	@ZapcomApi(value = "订单基本信息")
	PurchaseBaseInfo purchaseBaseInfo =new PurchaseBaseInfo();
	
	@ZapcomApi(value = "详细订单信息")
	private List<OrderDetail> orderDetailList =  new ArrayList<OrderDetail>();
	
	@ZapcomApi(value = "地址信息")
	private List<AddressDetail> addressDetailList =  new ArrayList<AddressDetail>();
	

	public PurchaseBaseInfo getPurchaseBaseInfo() {
		return purchaseBaseInfo;
	}

	public void setPurchaseBaseInfo(PurchaseBaseInfo purchaseBaseInfo) {
		this.purchaseBaseInfo = purchaseBaseInfo;
	}

	public List<AddressDetail> getAddressDetailList() {
		return addressDetailList;
	}

	public void setAddressDetailList(List<AddressDetail> addressDetailList) {
		this.addressDetailList = addressDetailList;
	}


	public List<OrderDetail> getOrderDetailList() {
		return orderDetailList;
	}

	public void setOrderDetailList(List<OrderDetail> orderDetailList) {
		this.orderDetailList = orderDetailList;
	}
	
	

}

