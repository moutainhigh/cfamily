package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Button;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**
 * 订单商品列表
 * @author wz
 *
 */
public class ApiOrderSellerDetailsResult{
	@ZapcomApi(value="商品编号")
	private String productCode = "";
	
	@ZapcomApi(value="SKU编号")
	private String skutCode = "";
	
	@ZapcomApi(value="促销种类")
	private String promotionType = "";
	@ZapcomApi(value="促销描述")
	private String promotionDescribe = "";
	@ZapcomApi(value="商品图片链接")
	private String mainpicUrl = "";
	@ZapcomApi(value="商品名称")
	private String productName = "";
	@ZapcomApi(value="价格")
	private Double price = 0.00;
	@ZapcomApi(value="价格",remark="非展示用使用，请勿使用")
	private Double deal_price = 0.00;
	@ZapcomApi(value="数量")
	private String number = "";
	@ZapcomApi(value="地区")
	private String region = "";
	@ZapcomApi(value="是否为跨境通(海外购)商品", remark="1:是     0:否")
	private String flagTheSea = "";
	@ZapcomApi(value="规格/款式")
	private List<ApiSellerStandardAndStyleResult> StandardAndStyleList = new ArrayList<ApiSellerStandardAndStyleResult>();
	@ZapcomApi(value="赠品列表")
	private List<ApiOrderDonationDetailsResult> detailsList = new ArrayList<ApiOrderDonationDetailsResult>();
	@ZapcomApi(value="是否通过海关",remark="0:正常     1:未通过")
	private String noPassCustom = "";
	@ZapcomApi(value="商品标签",remark="LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品")
    private List<String> labelsList = new ArrayList<String>();
	
	@ZapcomApi(value="商品标签对应的图片地址",remark="")
    private String labelsPic = "" ;
	
	@ZapcomApi(value="售后单号",demo="xxxx")
	private String afterCode = "";
	
	@ZapcomApi(value="订单序号")
	private String orderSeq = "";
	
	@ZapcomApi(value="积分")
	private String integral = "0";
	
	@ZapcomApi(value="商品是否同步", remark="1:是     0:否")
	private String isProductSync = "1";
	
	@ZapcomApi(value="商品提示消息", remark="该商品已下架")
	private String productPrompt = "";
	
//	@ZapcomApi(value="售后辅助状态名",demo="退货成功")
//	private String afterSaleStatusName = "";
//	
//	@ZapcomApi(value="售后辅助状态编号",demo="4497477800050001")
//	private String afterSaleStatusCode = "";
	
	@ZapcomApi(value = "订单支持的功能按钮", require = 1)
	private List<Button> orderButtonList = new ArrayList<Button>();
//	
//	@ZapcomApi(value="包裹状态")
//	private String localStatus = "";
//	@ZapcomApi(value="包裹商品信息")
//	private List<ApiOrderKjtDetailsResult> apiOrderKjtDetailsList = new ArrayList<ApiOrderKjtDetailsResult>();
	
	@ZapcomApi(value="商户编号", remark="商户编号")
	private String smallSellerCode = "";
	
	public String getProductCode() {
		return productCode;
	}

	public String getFlagTheSea() {
		return flagTheSea;
	}


	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}
	public String getPromotionDescribe() {
		return promotionDescribe;
	}
	public void setPromotionDescribe(String promotionDescribe) {
		this.promotionDescribe = promotionDescribe;
	}
	public String getMainpicUrl() {
		return mainpicUrl;
	}
	public void setMainpicUrl(String mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public List<ApiSellerStandardAndStyleResult> getStandardAndStyleList() {
		return StandardAndStyleList;
	}
	public void setStandardAndStyleList(
			List<ApiSellerStandardAndStyleResult> standardAndStyleList) {
		StandardAndStyleList = standardAndStyleList;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public List<ApiOrderDonationDetailsResult> getDetailsList() {
		return detailsList;
	}
	public void setDetailsList(List<ApiOrderDonationDetailsResult> detailsList) {
		this.detailsList = detailsList;
	}

	public String getNoPassCustom() {
		return noPassCustom;
	}

	public void setNoPassCustom(String noPassCustom) {
		this.noPassCustom = noPassCustom;
	}

	public Double getDeal_price() {
		return deal_price;
	}

	public void setDeal_price(Double deal_price) {
		this.deal_price = deal_price;
	}

	public List<String> getLabelsList() {
		return labelsList;
	}

	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}

	public List<Button> getOrderButtonList() {
		return orderButtonList;
	}

	public void setOrderButtonList(List<Button> orderButtonList) {
		this.orderButtonList = orderButtonList;
	}

//	public String getAfterSaleStatusName() {
//		return afterSaleStatusName;
//	}
//
//	public void setAfterSaleStatusName(String afterSaleStatusName) {
//		this.afterSaleStatusName = afterSaleStatusName;
//	}
//
//	public String getAfterSaleStatusCode() {
//		return afterSaleStatusCode;
//	}
//
//	public void setAfterSaleStatusCode(String afterSaleStatusCode) {
//		this.afterSaleStatusCode = afterSaleStatusCode;
//	}

	public String getAfterCode() {
		return afterCode;
	}

	public void setAfterCode(String afterCode) {
		this.afterCode = afterCode;
	}

	public String getSkutCode() {
		return skutCode;
	}

	public void setSkutCode(String skutCode) {
		this.skutCode = skutCode;
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

	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

}
