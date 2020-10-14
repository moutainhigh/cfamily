package com.cmall.familyhas.mtmanager.inputresult;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.model.BillInfo;
import com.cmall.familyhas.mtmanager.model.MTGood;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class APiCreateOrderDistributorInput extends RootInput {

	
	@ZapcomApi(value = "商品列表", remark = "不可为空",require=1, demo = "")
	private List<MTGood> goods = new ArrayList<MTGood>();
	
	@ZapcomApi(value = "收货人姓名", remark = "收货人姓名",require=1, demo = "")
	private String buyer_name = "";

	@ZapcomApi(value = "收货人手机号", remark = "手机号", demo = "13333100204", require = 1, verify = {"base=mobile" })
	private String buyer_mobile = "";
	
	@ZapcomApi(value = "收货人地址编号", remark = "收货人地址所在地区选择的第三级编号",require=1, demo = "")
	private String buyer_address_code = "";
	
	@ZapcomApi(value = "收货人详细地址", remark = "省市区三级不带",require=1, verify = "maxlength=19" )
	private String buyer_address = "";
	
	@ZapcomApi(value = "应付款", remark = "应付款",require=1, demo = "8888.88")
	private double pay_money = 0.00;
	
	@ZapcomApi(value = "发票信息", remark = "发票信息",require=1, demo = "")
	private BillInfo billInfo = new BillInfo();
	
	@ZapcomApi(value ="订单备注",remark = "订单备注")
	private String remark = "";

	@ZapcomApi(value = "运费", remark = "运费",require=1, demo = "8888.88")
	private double freight  = 0.00;
	
	public List<MTGood> getGoods() {
		return goods;
	}

	public void setGoods(List<MTGood> goods) {
		this.goods = goods;
	}

	public String getBuyer_name() {
		return buyer_name;
	}

	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}

	public String getBuyer_mobile() {
		return buyer_mobile;
	}

	public void setBuyer_mobile(String buyer_mobile) {
		this.buyer_mobile = buyer_mobile;
	}

	public String getBuyer_address_code() {
		return buyer_address_code;
	}

	public void setBuyer_address_code(String buyer_address_code) {
		this.buyer_address_code = buyer_address_code;
	}

	public String getBuyer_address() {
		return buyer_address;
	}

	public void setBuyer_address(String buyer_address) {
		this.buyer_address = buyer_address;
	}


	public double getPay_money() {
		return pay_money;
	}

	public void setPay_money(double pay_money) {
		this.pay_money = pay_money;
	}

	public BillInfo getBillInfo() {
		return billInfo;
	}

	public void setBillInfo(BillInfo billInfo) {
		this.billInfo = billInfo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getFreight() {
		return freight;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}
	
}
