package com.cmall.familyhas.api.result;


import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**
 * 商品信息
 * @author wz
 *
 */
public class ApiSellerHbListResult{
	@ZapcomApi(value="商品编号")
	private String product_code = "";
	@ZapcomApi(value="sku编号")
	private String sku_code = "";
	@ZapcomApi(value="商品图片链接")
	private String mainpic_url = "";
	@ZapcomApi(value="商品名称")
	private String product_name = "";
	@ZapcomApi(value="商品数量")
	private String product_number = "";
	@ZapcomApi(value="活动标签")
	private String labels = "";
	@ZapcomApi(value="规格/款式")
	private List<ApiSellerStandardAndStyleResult> standardAndStyleList = new ArrayList<ApiSellerStandardAndStyleResult>();
	@ZapcomApi(value="仓储城市")
	private String warehouseCity = "";
	@ZapcomApi(value="商品单价")
	private String sell_price = "";
	@ZapcomApi(value = "商品类型",remark="明确商品类型的列表不返回     0：普通商品  1：限购商品   2：试用商品")
	private String productType = "";
	@ZapcomApi(value="此商品是否为快境通订单", remark="1:是     0:否")
	private String flagTheSea = "0";
	@ZapcomApi(value="是否通过海关",remark="0:正常     1:未通过")
	private String noPassCustom = "";
	@ZapcomApi(value="商品标签",remark="LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品")
    private List<String> labelsList = new ArrayList<String>();
	
	@ZapcomApi(value="商品标签对应的图片地址",remark="")
    private String labelsPic = "" ;
	
	@ZapcomApi(value="积分")
	private String integral = "0";
	
	@ZapcomApi(value="LD拓展字段，订单序号")
	private String orderSeq = "0";
	
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag;
	
	@ZapcomApi(value="商品标签详细信息",remark="")
    private List<PlusModelProductLabel> labelsInfo = new ArrayList<PlusModelProductLabel>();
	
		public List<PlusModelProductLabel> getLabelsInfo() {
		return labelsInfo;
	}
	public void setLabelsInfo(List<PlusModelProductLabel> labelsInfo) {
		this.labelsInfo = labelsInfo;
	}
		public String getOrderSeq() {
		return orderSeq;
	}
	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}
		public String getProClassifyTag() {
		return proClassifyTag;
	}
	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
	}
	@ZapcomApi(value="商品是否同步", remark="1:是     0:否")
	private String isProductSync = "1";
	
	public String getIsProductSync() {
		return isProductSync;
	}
	public void setIsProductSync(String isProductSync) {
		this.isProductSync = isProductSync;
	}
	public String getProductPrompt() {
		return productPrompt;
	}
	public void setProductPrompt(String productPrompt) {
		this.productPrompt = productPrompt;
	}
	@ZapcomApi(value="商品提示消息", remark="该商品已下架")
	private String productPrompt = "";
	
	public String getFlagTheSea() {
		return flagTheSea;
	}
	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getMainpic_url() {
		return mainpic_url;
	}
	public void setMainpic_url(String mainpic_url) {
		this.mainpic_url = mainpic_url;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_number() {
		return product_number;
	}
	public void setProduct_number(String product_number) {
		this.product_number = product_number;
	}
	public String getLabels() {
		return labels;
	}
	public void setLabels(String labels) {
		this.labels = labels;
	}
	public String getWarehouseCity() {
		return warehouseCity;
	}
	public void setWarehouseCity(String warehouseCity) {
		this.warehouseCity = warehouseCity;
	}
	public String getSell_price() {
		return sell_price;
	}
	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}
	public List<ApiSellerStandardAndStyleResult> getStandardAndStyleList() {
		return standardAndStyleList;
	}
	public void setStandardAndStyleList(
			List<ApiSellerStandardAndStyleResult> standardAndStyleList) {
		this.standardAndStyleList = standardAndStyleList;
	}
	public String getNoPassCustom() {
		return noPassCustom;
	}
	public void setNoPassCustom(String noPassCustom) {
		this.noPassCustom = noPassCustom;
	}
	public List<String> getLabelsList() {
		return labelsList;
	}
	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}
	public String getSku_code() {
		return sku_code;
	}
	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}
	public String getLabelsPic() {
		return labelsPic;
	}
	public void setLabelsPic(String labelsPic) {
		this.labelsPic = labelsPic;
	}
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
}
