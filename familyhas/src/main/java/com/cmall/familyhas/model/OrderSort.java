package com.cmall.familyhas.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.baseclass.BaseClass;

/**   
 * 	订单分组及运费
*    xiegj
*/
public class OrderSort  extends BaseClass{
	
	@ZapcomApi(value="商品编号",remark="商品编号")
	private List<String> skuCodes= new ArrayList<String>();

	@ZapcomApi(value="运费",remark="运费")
	private double tranMoney = 0.00;

	@ZapcomApi(value="加价购订单标识")
	private String jjgFlag = "0";

	public String getJjgFlag() {
		return jjgFlag;
	}

	public void setJjgFlag(String jjgFlag) {
		this.jjgFlag = jjgFlag;
	}


	public List<String> getSkuCodes() {
		return skuCodes;
	}

	public void setSkuCodes(List<String> skuCodes) {
		this.skuCodes = skuCodes;
	}

	public double getTranMoney() {
		return tranMoney;
	}

	public void setTranMoney(double tranMoney) {
		this.tranMoney = tranMoney;
	}
	
}

