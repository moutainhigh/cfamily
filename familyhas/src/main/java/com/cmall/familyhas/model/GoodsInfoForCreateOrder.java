package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	购物车添加的商品信息对象
*    xiegj
*/
public class GoodsInfoForCreateOrder  {
	@ZapcomApi(value = "商品编号", remark = "商品编号",require = 1, demo = "8019123456")
	private String sku_code = "";
	
	@ZapcomApi(value = "商品数量", remark = "商品数量",require = 1, demo = "123456")
	private int sku_num = 0;
	
	@ZapcomApi(value = "地区编号", remark = "地区编号",require = 1, demo = "123456")
	private String area_code = "";
	
	@ZapcomApi(value = "商品规格", remark = "商品规格",require = 1, demo = "01&白色")
	private String sku_size = "";
	
	@ZapcomApi(value = "商品款式", remark = "商品款式",require = 1, demo = "01&蝴蝶款")
	private String sku_style = "";

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public int getSku_num() {
		return sku_num;
	}

	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getSku_size() {
		return sku_size;
	}

	public void setSku_size(String sku_size) {
		this.sku_size = sku_size;
	}

	public String getSku_style() {
		return sku_style;
	}

	public void setSku_style(String sku_style) {
		this.sku_style = sku_style;
	}
	
}

