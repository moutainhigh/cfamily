package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;
import com.cmall.familyhas.api.model.ProductItem;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetSmsNoticeProductsResult extends RootResult {
	@ZapcomApi(value="扫码购落地页显示商品列表")
	private List<ProductItem> productList=new ArrayList<>();
	
	@ZapcomApi(value="惠惠农场入口参数",remark="如果里面地址和logo都为空则不展示",demo="")
	private ShowFormInfo hhFarm = new ShowFormInfo();
	
	public List<ProductItem> getProductList() {
		return productList;
	}
	public void setProductList(List<ProductItem> productList) {
		this.productList = productList;
	}
	public ShowFormInfo getHhFarm() {
		return hhFarm;
	}
	public void setHhFarm(ShowFormInfo hhFarm) {
		this.hhFarm = hhFarm;
	}
}