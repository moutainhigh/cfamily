package com.cmall.familyhas.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmasorder.model.TagInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductLabel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class GetBrowseHistory {
	@ZapcomApi(value="商品图片")
	private String mainpicUrl="";
	
	@ZapcomApi(value="商品编号")
	private String productCode="";
	
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@ZapcomApi(value="商品名称")
	private String productName = "";
	
	@ZapcomApi(value="商品现价")
	private String sellPrice = "";

	@ZapcomApi(value="商品原价")
	private String marketPrice ="";

//	@ZapcomApi(value="售罄标示",remark="如果结果为true则表示库存为0或不存在")
//	private String sellFlag ="";

	@ZapcomApi(value="下架标示",remark="4497153900060002表示上架,售罄：4497471600050002;下架：除上架与售罄外的其他值")
	private String productStatus = "";
	
	@ZapcomApi(value="浏览时间")
	private String browseTime = "";

	@ZapcomApi(value="是否海外购",remark="0:否，1:是")
    private String flagTheSea = "0" ;
	
	@ZapcomApi(value="拼团标识", remark="拼团编码：4497472600010024")
	private String groupBuying = "";
	@ZapcomApi(value="是否拼团商品", remark="是：4497472000050001，否：4497472000050002")
	private String productType = "4497472000050002";
	
	@ZapcomApi(value="拼团商品原价", remark="如果是拼团商品的话，需要显示的划线价（原实际售价）")
	private BigDecimal skuPrice;
	
	@ZapcomApi(value="拼团商品展示价", remark="如果是拼团商品的话，需要显示的拼团购买价格")
	private BigDecimal groupBuyingPrice;
	
	@ZapcomApi(value="几人团", remark="需要几人参团，字符串类型的数字")
	private String collagePersonCount;
	
	@ZapcomApi(value="商品标签",remark="LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品")
    private List<String> labelsList = new ArrayList<String>();

	@ZapcomApi(value="商品标签对应的图片地址",remark="3.9.2开始用")
    private String labelsPic = "" ;
	
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag="";
	@ZapcomApi(value = "商品活动标签",remark="秒杀、闪购、拼团、特价、会员日、满减、领券、赠品（最多展示三个）")
	private List<String> tagList = new ArrayList<String>();
	
	@ZapcomApi(value="带样式的商品活动标签")
	private List<TagInfo> tagInfoList = new ArrayList<TagInfo>();

	@ZapcomApi(value="商品标签详细信息",remark="")
    private List<PlusModelProductLabel> labelsInfo = new ArrayList<PlusModelProductLabel>();

		public List<PlusModelProductLabel> getLabelsInfo() {
		return labelsInfo;
	}

	public void setLabelsInfo(List<PlusModelProductLabel> labelsInfo) {
		this.labelsInfo = labelsInfo;
	}

		public List<String> getTagList() {
		return tagList;
	}

	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}

		public String getCollagePersonCount() {
		return collagePersonCount;
	}

	public void setCollagePersonCount(String collagePersonCount) {
		this.collagePersonCount = collagePersonCount;
	}

		public String getGroupBuying() {
		return groupBuying;
	}

	public String getProductType() {
			return productType;
		}

		public void setProductType(String productType) {
			this.productType = productType;
		}

		public BigDecimal getSkuPrice() {
			return skuPrice;
		}

		public void setSkuPrice(BigDecimal skuPrice) {
			this.skuPrice = skuPrice;
		}

		public BigDecimal getGroupBuyingPrice() {
			return groupBuyingPrice;
		}

		public void setGroupBuyingPrice(BigDecimal groupBuyingPrice) {
			this.groupBuyingPrice = groupBuyingPrice;
		}

	public void setGroupBuying(String groupBuying) {
		this.groupBuying = groupBuying;
	}

		public String getProClassifyTag() {
		return proClassifyTag;
	}
	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
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

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}


	public String getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}

//	public String getSellFlag() {
//		return sellFlag;
//	}
//
//	public void setSellFlag(String sellFlag) {
//		this.sellFlag = sellFlag;
//	}

	public String getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(String productStatus) {
		this.productStatus = productStatus;
	}

	public String getBrowseTime() {
		return browseTime;
	}

	public void setBrowseTime(String browseTime) {
		this.browseTime = browseTime;
	}

	public String getFlagTheSea() {
		return flagTheSea;
	}

	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
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

	public List<TagInfo> getTagInfoList() {
		return tagInfoList;
	}

	public void setTagInfoList(List<TagInfo> tagInfoList) {
		this.tagInfoList = tagInfoList;
	}

	
}
