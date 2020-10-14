package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiGetCouponForOrderConfirmInput extends RootInput {
	
	/*@ZapcomApi(value="订单中的sku编号",remark="",require=1)
	private List<String> skuList = new ArrayList<>();

	public List<String> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<String> skuList) {
		this.skuList = skuList;
	}*/
	
	@ZapcomApi(value = "商品", require = 0)
	private List<GoodsInfoForAdd> goods = new ArrayList<GoodsInfoForAdd>();
	
	@ZapcomApi(value = "实付款", remark = "实付款")
	private double pay_money = 0.00;
	


	public double getPay_money() {
		return pay_money;
	}

	public void setPay_money(double pay_money) {
		this.pay_money = pay_money;
	}


	public List<GoodsInfoForAdd> getGoods() {
		return goods;
	}

	public void setGoods(List<GoodsInfoForAdd> goods) {
		this.goods = goods;
	}
	
	
	
	

	
}
