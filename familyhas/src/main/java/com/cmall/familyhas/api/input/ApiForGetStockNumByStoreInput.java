package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/***
 * 查看仓库库存状态 输入参数
 * @author jlin
 *
 */
public class ApiForGetStockNumByStoreInput extends RootInput {

	@ZapcomApi(value = "商品编号", remark = "4654546546",require=1)
	private String product_code="";
	@ZapcomApi(value = "会员编号", remark = "4654546546",require=0)
	private String member_code="";
	@ZapcomApi(value = "SKU属性", demo="color_id=0&style_id=0",require=1)
	private String sku_code="";
	@ZapcomApi(value = "区域编号", remark = "4654546546",require=1)
	private String district_code="";
	@ZapcomApi(value = "商品数量", remark = "4654546546",require=0)
	private int product_num=0;
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getMember_code() {
		return member_code;
	}
	public void setMember_code(String member_code) {
		this.member_code = member_code;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	public String getDistrict_code() {
		return district_code;
	}
	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}
	public int getProduct_num() {
		return product_num;
	}
	public void setProduct_num(int product_num) {
		this.product_num = product_num;
	}
	
	 
}
