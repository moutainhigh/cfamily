package com.cmall.familyhas.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.helper.MoneyHelper;

/**
 * 首页版式栏目内容包含商品
 * @author ligj
 *
 */
public class HomeColumnContentProductInfo {

	@ZapcomApi(value = "商品编号")
	private String productCode = "";
	
	@ZapcomApi(value = "商品名称")
	private String productName = "";
	
	@ZapcomApi(value = "图片")
	private String mainpicUrl = "";
	
	@ZapcomApi(value = "销售价")
	private String sellPrice = "";
	
	@ZapcomApi(value = "市场价")
	private String markPrice = "";
	
	@ZapcomApi(value = "折扣")
	private String discount = "";
	
	//一栏多行通用大图模板使用
	@ZapcomApi(value = "活动节省")
	private String saveValue = "";
	//一栏多行通用大图模板使用
	@ZapcomApi(value = "活动编号",remark="当活动编号为4497472600010018（会员日折扣）/4497472600010030（打折促销）时，取discount字段显示；当为4497472600010024拼团活动时，取saveValue做拼团标签显示；其他值时按其他活动取saveValue显示")
	private String eventType = "";
	
	@ZapcomApi(value="是否海外购",remark="0:否，1:是")
    private String flagTheSea = "0" ;
	
	@ZapcomApi(value="商品标签",remark="3.9.2已作废")
    private List<String> labelsList = new ArrayList<String>();
		
	@ZapcomApi(value="商品标签对应的图片地址",remark="")
    private String labelsPic = "" ;
	
	@ZapcomApi(value="广告图",remark="")
    private String adPicUrl = "" ;
	
	@ZapcomApi(value="直播商品促销语",remark="")
    private String tvTips = "" ;
	
	@ZapcomApi(value = "广告语",remark="栏目类型为视频直播模板时此字段有效:4497471600010020")
	private String videoAd = "";
	
	@ZapcomApi(value = "是否活动价",remark="栏目类型为视频直播模板时此字段有效:4497471600010020")
	private String isActivity = "";
	
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag="";
	
	@ZapcomApi(value="标签集合",remark="秒杀、闪购、拼团、特价、会员日、满减、赠品（最多展示三个）")
	private List<String> tagList = new ArrayList<String>();
	

	@ZapcomApi(value="带样式的商品活动标签")
	private List<TagInfo> tagInfoList = new ArrayList<TagInfo>();

	@ZapcomApi(value="558商品评价封面评价内容")
	private String evaluationContent = "";
	
	// 内部使用暂不传给前端
	@JSONField(serialize = false)
	@ZapcomApi(value = "商品状态",remark="4497153900060002:上架")
	private String productStatus = "";
	
	@ZapcomApi(value="商品标签详细信息",remark="")
    private List<PlusModelProductLabel> labelsInfo = new ArrayList<PlusModelProductLabel>();
    
    @ZapcomApi(value = "sku关联商品下的所有sku实际库存")
	private int allSkuRealStock = 0;

	@ZapcomApi(value = "分销券优惠面额")
	private String couponValue = "";
	
	@ZapcomApi(value = "商品销量 （虚拟销量+真实销量）")
	private int salesNums = 0;
	
	@ZapcomApi(value = "好评率")
	private String highPraiseRate = "";
	
	public String getHighPraiseRate() {
		return highPraiseRate;
	}
	public void setHighPraiseRate(String highPraiseRate) {
		this.highPraiseRate = highPraiseRate;
	}
	public String getSaveValue() {
		return saveValue;
	}
	public void setSaveValue(String saveValue) {
		this.saveValue = saveValue;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public int getSalesNums() {
		return salesNums;
	}
	public void setSalesNums(int salesNums) {
		this.salesNums = salesNums;
	}
	public String getCouponValue() {	
		if (StringUtils.isNotBlank(couponValue)) {
			couponValue =MoneyHelper.format(new BigDecimal(couponValue));  // 兼容小数 - Yangcl
		}
		return couponValue;
	}
	public void setCouponValue(String couponValue) {
		this.couponValue = couponValue;
	}
	
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
	public String getEvaluationContent() {
		return evaluationContent;
	}
	public void setEvaluationContent(String evaluationContent) {
		this.evaluationContent = evaluationContent;
	}
	
	public String getIsActivity() {
		return isActivity;
	}
	public void setIsActivity(String isActivity) {
		this.isActivity = isActivity;
	}
	public String getVideoAd() {
		return videoAd;
	}
	public void setVideoAd(String videoAd) {
		this.videoAd = videoAd;
	}
	public List<String> getLabelsList() {
		return labelsList;
	}
	public void setLabelsList(List<String> labelsList) {
		this.labelsList = labelsList;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getSellPrice() {
		if (StringUtils.isNotBlank(sellPrice)) {
//			sellPrice = (""+new BigDecimal(sellPrice).setScale(0, BigDecimal.ROUND_DOWN));
			sellPrice =MoneyHelper.format(new BigDecimal(sellPrice));  // 兼容小数 - Yangcl
		}
		return sellPrice;
	}
	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getMainpicUrl() {
		return mainpicUrl;
	}
	public void setMainpicUrl(String mainpicUrl) {
		this.mainpicUrl = mainpicUrl;
	}
	public String getMarkPrice() {
		if (StringUtils.isNotBlank(markPrice)) {
//			markPrice = (""+new BigDecimal(markPrice).setScale(0, BigDecimal.ROUND_DOWN));
			markPrice = MoneyHelper.format(new BigDecimal(markPrice)); // 兼容小数 - Yangcl
		}
		return markPrice;
	}
	public void setMarkPrice(String markPrice) {
		this.markPrice = markPrice;
	}
	public String getFlagTheSea() {
		return flagTheSea;
	}
	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}
	public String getAdPicUrl() {
		return adPicUrl;
	}
	public void setAdPicUrl(String adPicUrl) {
		this.adPicUrl = adPicUrl;
	}
	public String getLabelsPic() {
		return labelsPic;
	}
	public void setLabelsPic(String labelsPic) {
		this.labelsPic = labelsPic;
	}
	public String getTvTips() {
		return tvTips;
	}
	public void setTvTips(String tvTips) {
		this.tvTips = tvTips;
	}
	public List<String> getTagList() {
		return tagList;
	}
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
	public String getProductStatus() {
		return productStatus;
	}
	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public int getAllSkuRealStock() {
		return allSkuRealStock;
	}

	public void setAllSkuRealStock(int allSkuRealStock) {
		this.allSkuRealStock = allSkuRealStock;
	}

	public List<TagInfo> getTagInfoList() {
		return tagInfoList;
	}
	public void setTagInfoList(List<TagInfo> tagInfoList) {
		this.tagInfoList = tagInfoList;
	}
}
