package com.cmall.familyhas.api.model;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class PageTemplete {
	
	@ZapcomApi(value="多栏数量")
	private String columnNum = "";
	@ZapcomApi(value="标题颜色")
	private String templateTitleColor = "";
	@ZapcomApi(value="标题选中色")
	private String templateTitleColorSelected = "";
	@ZapcomApi(value="模板选中色")
	private String templateBackcolorSelected = "";
	@ZapcomApi(value="模板类型")
	private String templeteType = "";
	@ZapcomApi(value="模板背景色")
	private String templeteBackColor = "";
	@ZapcomApi(value="商品购买方式图片")
	private String pcBuyTypeImg = "";
	@ZapcomApi(value="商品文本背景类型")
	private String pcTxtBackType = "";
	@ZapcomApi(value ="商品文本背景值")
	private String pcTxtBackVal = "";
	@ZapcomApi(value="商品文本背景图")
	private String pcTxtBackPic = "";
	@ZapcomApi(value="模板编号")
	private String templeteNum = "";
	@ZapcomApi(value="是否展示最低折扣")
	private String isShowDownDiscount="";
	@ZapcomApi(value="商品文本底色模板",remark="模板a/b")
	private String pcTxtColorType = "";
	@ZapcomApi(value="内容列表")
	private List<TempleteProduct> pcList = new ArrayList<TempleteProduct>();
	@ZapcomApi(value="标题名称")
	private String template_title_name = "";
	@ZapcomApi(value="模板图")
	private String templetePic = "";
	@ZapcomApi(value="视频直播链接",remark="只有视频模板会有视频连接")
	private String tvUrl = "";
	@ZapcomApi(value="售价价格颜色")
	private String sell_price_color = "";
	@ZapcomApi(value="兑换码")
	private String exchangeCode = "";
	@ZapcomApi(value="兑换码结束时间")
	private String exchangeEndTime = "";
	@ZapcomApi(value="兑换码数量")
	private String exchangeCodeCount = "";
	@ZapcomApi(value="活动编号")
	private String activityCode = "";
	@ZapcomApi(value="分隔条",remark="449748410001:不显示；449748410002:显示")
	private String splitBar = "";
	
	public String getActivityCode() {
		return activityCode;
	}
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}
	public String getExchangeCode() {
		return exchangeCode;
	}
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	public String getExchangeEndTime() {
		return exchangeEndTime;
	}
	public void setExchangeEndTime(String exchangeEndTime) {
		this.exchangeEndTime = exchangeEndTime;
	}
	public String getExchangeCodeCount() {
		return exchangeCodeCount;
	}
	public void setExchangeCodeCount(String exchangeCodeCount) {
		this.exchangeCodeCount = exchangeCodeCount;
	}
	public String getColumnNum() {
		return columnNum;
	}
	public void setColumnNum(String columnNum) {
		this.columnNum = columnNum;
	}
	public String getTemplateTitleColor() {
		return templateTitleColor;
	}
	public void setTemplateTitleColor(String templateTitleColor) {
		this.templateTitleColor = templateTitleColor;
	}
	public String getTemplateTitleColorSelected() {
		return templateTitleColorSelected;
	}
	public void setTemplateTitleColorSelected(String templateTitleColorSelected) {
		this.templateTitleColorSelected = templateTitleColorSelected;
	}
	public String getTemplateBackcolorSelected() {
		return templateBackcolorSelected;
	}
	public void setTemplateBackcolorSelected(String templateBackcolorSelected) {
		this.templateBackcolorSelected = templateBackcolorSelected;

	}
	public String getTempleteType() {
		return templeteType;
	}
	public void setTempleteType(String templeteType) {
		this.templeteType = templeteType;
	}
	public String getTempleteBackColor() {
		return templeteBackColor;
	}
	public void setTempleteBackColor(String templeteBackColor) {
		this.templeteBackColor = templeteBackColor;
	}
	public String getPcBuyTypeImg() {
		return pcBuyTypeImg;
	}
	public void setPcBuyTypeImg(String pcBuyTypeImg) {
		this.pcBuyTypeImg = pcBuyTypeImg;
	}
	public String getPcTxtBackType() {
		return pcTxtBackType;
	}
	public void setPcTxtBackType(String pcTxtBackType) {
		this.pcTxtBackType = pcTxtBackType;
	}
	public String getPcTxtBackVal() {
		return pcTxtBackVal;
	}
	public void setPcTxtBackVal(String pcTxtBackVal) {
		this.pcTxtBackVal = pcTxtBackVal;
	}
	public String getPcTxtBackPic() {
		return pcTxtBackPic;
	}
	public void setPcTxtBackPic(String pcTxtBackPic) {
		this.pcTxtBackPic = pcTxtBackPic;
	}
	public String getTempleteNum() {
		return templeteNum;
	}
	public void setTempleteNum(String templeteNum) {
		this.templeteNum = templeteNum;
	}
	public String getIsShowDownDiscount() {
		return isShowDownDiscount;
	}
	public void setIsShowDownDiscount(String isShowDownDiscount) {
		this.isShowDownDiscount = isShowDownDiscount;
	}
	public String getPcTxtColorType() {
		return pcTxtColorType;
	}
	public void setPcTxtColorType(String pcTxtColorType) {
		this.pcTxtColorType = pcTxtColorType;
	}
	public List<TempleteProduct> getPcList() {
		return pcList;
	}
	public void setPcList(List<TempleteProduct> pcList) {
		this.pcList = pcList;
	}
	public String getTemplate_title_name() {
		return template_title_name;
	}
	public void setTemplate_title_name(String template_title_name) {
		this.template_title_name = template_title_name;
	}
	public String getTempletePic() {
		return templetePic;
	}
	public void setTempletePic(String templetePic) {
		this.templetePic = templetePic;
	}
	public String getTvUrl() {
		return tvUrl;
	}
	public void setTvUrl(String tvUrl) {
		this.tvUrl = tvUrl;
	}
	public String getSell_price_color() {
		return sell_price_color;
	}
	public void setSell_price_color(String sell_price_color) {
		this.sell_price_color = sell_price_color;
	}
	public String getSplitBar() {
		return splitBar;
	}
	public void setSplitBar(String splitBar) {
		this.splitBar = splitBar;
	}
	
}
