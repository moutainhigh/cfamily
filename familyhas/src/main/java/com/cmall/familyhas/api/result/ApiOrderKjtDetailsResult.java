package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiOrderKjtDetailsResult {
	@ZapcomApi(value="商品编号")
	private String productCodeKJT = "";
	@ZapcomApi(value="促销种类")
	private String promotionTypeKJT = "";
	@ZapcomApi(value="促销描述")
	private String promotionDescribeKJT = "";
	@ZapcomApi(value="商品图片链接")
	private String mainpicUrlKJT = "";
	@ZapcomApi(value="商品名称")
	private String productNameKJT = "";
	@ZapcomApi(value="价格")
	private String priceKJT = "";
	@ZapcomApi(value="数量")
	private String numberKJT = "";
//	@ZapcomApi(value="地区")
//	private String regionKJT = "";
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
	public String getPriceKJT() {
		return priceKJT;
	}
	public void setPriceKJT(String priceKJT) {
		this.priceKJT = priceKJT;
	}
	public String getProductCodeKJT() {
		return productCodeKJT;
	}
	public void setProductCodeKJT(String productCodeKJT) {
		this.productCodeKJT = productCodeKJT;
	}
	public String getPromotionTypeKJT() {
		return promotionTypeKJT;
	}
	public void setPromotionTypeKJT(String promotionTypeKJT) {
		this.promotionTypeKJT = promotionTypeKJT;
	}
	public String getPromotionDescribeKJT() {
		return promotionDescribeKJT;
	}
	public void setPromotionDescribeKJT(String promotionDescribeKJT) {
		this.promotionDescribeKJT = promotionDescribeKJT;
	}
	public String getMainpicUrlKJT() {
		return mainpicUrlKJT;
	}
	public void setMainpicUrlKJT(String mainpicUrlKJT) {
		this.mainpicUrlKJT = mainpicUrlKJT;
	}
	public String getProductNameKJT() {
		return productNameKJT;
	}
	public void setProductNameKJT(String productNameKJT) {
		this.productNameKJT = productNameKJT;
	}
	public String getNumberKJT() {
		return numberKJT;
	}
	public void setNumberKJT(String numberKJT) {
		this.numberKJT = numberKJT;
	}
	public List<ApiSellerStandardAndStyleResult> getStandardAndStyleList() {
		return StandardAndStyleList;
	}
	public void setStandardAndStyleList(
			List<ApiSellerStandardAndStyleResult> standardAndStyleList) {
		StandardAndStyleList = standardAndStyleList;
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
	public List<String> getLabelsList() {
		return labelsList;
	}
	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}
	public String getLabelsPic() {
		return labelsPic;
	}
	public void setLabelsPic(String labelsPic) {
		this.labelsPic = labelsPic;
	}

}
