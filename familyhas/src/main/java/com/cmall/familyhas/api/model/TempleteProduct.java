package com.cmall.familyhas.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class TempleteProduct {

	@ZapcomApi(value = "商品图")
	private String pcImg = "";
	@ZapcomApi(value = "商品编号")
	private String productCode = "";
	@ZapcomApi(value = "sku编号")
	private String pcNum = "";
	@ZapcomApi(value = "商品位置")
	private String pcPosition = "";
	@ZapcomApi(value = "是否展示折扣", remark = "449746250001 :是   ; 449746250002: 否")
	private String isShowDiscount = "";
	@ZapcomApi(value = "商品名称")
	private String pcName = "";
	@ZapcomApi(value = "商品描述")
	private String pcDesc = "";
	@ZapcomApi(value = "打开方式选择")
	private String openType = "";
	@ZapcomApi(value = "打开方式对应值")
	private String openTypeVal = "";
	@ZapcomApi(value = "栏目图片")
	private String categoryImg = "";
	@ZapcomApi(value = "商品价格")
	private String pcPrice = "0";
	@ZapcomApi(value = "商品市场价")
	private String marketPrice = "0";
	@ZapcomApi(value = "商品折扣")
	private String pcDiscount = "";
	@ZapcomApi(value = "商品库存")
	private String salesNum = "0";
	@ZapcomApi(value = "是否下架")
	private String pcStatus = "";
	@ZapcomApi(value = "优惠码", remark = "base64加密")
	private String coupon = "";
	@ZapcomApi(value = "活动编号", remark = "优惠码对应的活动编号")
	private String activity_code = "";
	@ZapcomApi(value = "允许领取次数")
	private String accountUseTime = "1";
	@ZapcomApi(value = "连接")
	private String url = "";
	@ZapcomApi(value = "宽度")
	private String width = "";
	@ZapcomApi(value = "描述")
	private String template_desc = "";
	@ZapcomApi(value = "图")
	private String img = "";
	@ZapcomApi(value = "城市名称")
	private String city_name = "";
	@ZapcomApi(value = "标题")
	private String title = "";
	@ZapcomApi(value = "标题颜色")
	private String title_color = "";
	@ZapcomApi(value = "标题选中颜色")
	private String title_checked_color = "";
	@ZapcomApi(value = "描述文字", remark = "两栏多行（推荐）[449747500014]、右两栏[449747500015]、左两栏[449747500016] 模板使用")
	private String des_content = "";
	@ZapcomApi(value = "描述颜色")
	private String des_content_color = "";
	@ZapcomApi(value = "优惠描述")
	private String preferential_desc = "";
	@ZapcomApi(value = "关联模板编号", remark = "页面定位模板[449747500018]使用")
	private String rel_template_number = "";
	@ZapcomApi(value = "商品标签图片")
	private List<String> productLabelPicList = new ArrayList<String>();
	@ZapcomApi(value = "商品活动标签",remark="秒杀、闪购、拼团、特价、会员日、满减、领券、赠品（最多展示三个）")
	private List<String> tagList = new ArrayList<String>();
	
	@ZapcomApi(value="关联模板信息")
	private List<PageTemplete> rel_templete = null;
	@ZapcomApi(value = "商户编码")
	private String smallSellerCode = "";
	/*商品成本 判断内购价格时使用 (此字段值不返回给前端)*/
	private transient BigDecimal costPrice = BigDecimal.ZERO;
	@ZapcomApi(value = "商品原价")
	private BigDecimal skuPrice = BigDecimal.ZERO;
	
	@ZapcomApi(value="积分兑换模版的优惠券信息")
	private TemplateCouponType couponType = null;
	
	@ZapcomApi(value = "按钮类型", demo = "1马上抢购，2去拼团")
	private String buttonType = "1";
	
	@ZapcomApi(value = "几人团", demo = "2",remark="2")
	private String manyCollage= "2";
	
	@ZapcomApi(value = "已售多少件", demo = "2000",remark="2000")
	private Integer sellNum;
	
	@ZapcomApi(value = "省XX钱", demo = "20.1",remark="20.1")
	private Integer saveValue;
	
	@ZapcomApi(value = "拼团类型", demo = "普通团：4497473400050001，邀新团：4497473400050002",remark="普通团：4497473400050001，邀新团：4497473400050002")
	private String collageType;
	
	@ZapcomApi(value="活动商品进行的进度",remark="目前参与进度显示的商品活动有 拼团列表，秒杀列表，闪购列表")
	private String rateOfProgress="";
	

	public String getRateOfProgress() {
		return rateOfProgress;
	}

	public void setRateOfProgress(String rateOfProgress) {
		this.rateOfProgress = rateOfProgress;
	}
	
	/*
	 * 扩展：添加倒计时模板属性,下面这个属性为倒计时模板特有
	 */
	@ZapcomApi(value = "倒计时模板目标时间")
	private String targetTime = "";
	
	@ZapcomApi(value = "自营标签（目前只是一栏多行使用）")
	private String proClassifyTag = "";
	
	@ZapcomApi(value="商品标签详细信息",remark="")
    private List<PlusModelProductLabel> labelsInfo = new ArrayList<PlusModelProductLabel>();
	
	
	public List<PlusModelProductLabel> getLabelsInfo() {
		return labelsInfo;
	}

	public void setLabelsInfo(List<PlusModelProductLabel> labelsInfo) {
		this.labelsInfo = labelsInfo;
	}

	public String getProClassifyTag() {
		return proClassifyTag;
	}

	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
	}

	public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

	public String getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(String targetTime) {
		this.targetTime = targetTime;
	}

	public String getPcImg() {
		return pcImg;
	}

	public void setPcImg(String pcImg) {
		this.pcImg = pcImg;
	}

	public String getPcNum() {
		return pcNum;
	}

	public void setPcNum(String pcNum) {
		this.pcNum = pcNum;
	}

	public String getPcPosition() {
		return pcPosition;
	}

	public void setPcPosition(String pcPosition) {
		this.pcPosition = pcPosition;
	}

	public String getIsShowDiscount() {
		return isShowDiscount;
	}

	public void setIsShowDiscount(String isShowDiscount) {
		this.isShowDiscount = isShowDiscount;
	}

	public String getPcName() {
		return pcName;
	}

	public void setPcName(String pcName) {
		this.pcName = pcName;
	}

	public String getPcDesc() {
		return pcDesc;
	}

	public void setPcDesc(String pcDesc) {
		this.pcDesc = pcDesc;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public String getOpenTypeVal() {
		return openTypeVal;
	}

	public void setOpenTypeVal(String openTypeVal) {
		this.openTypeVal = openTypeVal;
	}

	public String getCategoryImg() {
		return categoryImg;
	}

	public void setCategoryImg(String categoryImg) {
		this.categoryImg = categoryImg;
	}

	public String getPcPrice() {
		return pcPrice;
	}

	public void setPcPrice(String pcPrice) {
		this.pcPrice = pcPrice;
	}

	public String getPcDiscount() {
		return pcDiscount;
	}

	public void setPcDiscount(String pcDiscount) {
		this.pcDiscount = pcDiscount;
	}

	public String getSalesNum() {
		return salesNum;
	}

	public void setSalesNum(String salesNum) {
		this.salesNum = salesNum;
	}

	public String getPcStatus() {
		return pcStatus;
	}

	public void setPcStatus(String pcStatus) {
		this.pcStatus = pcStatus;
	}

	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getActivity_code() {
		return activity_code;
	}

	public void setActivity_code(String activity_code) {
		this.activity_code = activity_code;
	}

	public String getAccountUseTime() {
		return accountUseTime;
	}

	public void setAccountUseTime(String accountUseTime) {
		this.accountUseTime = accountUseTime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getTemplate_desc() {
		return template_desc;
	}

	public void setTemplate_desc(String template_desc) {
		this.template_desc = template_desc;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle_color() {
		return title_color;
	}

	public void setTitle_color(String title_color) {
		this.title_color = title_color;
	}

	public String getTitle_checked_color() {
		return title_checked_color;
	}

	public void setTitle_checked_color(String title_checked_color) {
		this.title_checked_color = title_checked_color;
	}

	public String getDes_content() {
		return des_content;
	}

	public void setDes_content(String des_content) {
		this.des_content = des_content;
	}

	public String getDes_content_color() {
		return des_content_color;
	}

	public void setDes_content_color(String des_content_color) {
		this.des_content_color = des_content_color;
	}

	public String getPreferential_desc() {
		return preferential_desc;
	}

	public void setPreferential_desc(String preferential_desc) {
		this.preferential_desc = preferential_desc;
	}

	public String getRel_template_number() {
		return rel_template_number;
	}

	public void setRel_template_number(String rel_template_number) {
		this.rel_template_number = rel_template_number;
	}

	public List<PageTemplete> getRel_templete() {
		return rel_templete;
	}

	public void setRel_templete(List<PageTemplete> rel_templete) {
		this.rel_templete = rel_templete;
	}

	public List<String> getProductLabelPicList() {
		return productLabelPicList;
	}

	public void setProductLabelPicList(List<String> productLabelPicList) {
		this.productLabelPicList = productLabelPicList;
	}

	public String getSmallSellerCode() {
		return smallSellerCode;
	}

	public void setSmallSellerCode(String smallSellerCode) {
		this.smallSellerCode = smallSellerCode;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(BigDecimal skuPrice) {
		this.skuPrice = skuPrice;
	}

	public TemplateCouponType getCouponType() {
		return couponType;
	}

	public void setCouponType(TemplateCouponType couponType) {
		this.couponType = couponType;
	}

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	public String getManyCollage() {
		return manyCollage;
	}

	public void setManyCollage(String manyCollage) {
		this.manyCollage = manyCollage;
	}

	public Integer getSellNum() {
		return sellNum;
	}

	public void setSellNum(Integer sellNum) {
		this.sellNum = sellNum;
	}

	public Integer getSaveValue() {
		return saveValue;
	}

	public void setSaveValue(Integer saveValue) {
		this.saveValue = saveValue;
	}

	public String getCollageType() {
		return collageType;
	}

	public void setCollageType(String collageType) {
		this.collageType = collageType;
	}

}
