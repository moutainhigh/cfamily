package com.cmall.familyhas.model;

import java.util.ArrayList;
import java.util.List;

import com.cmall.productcenter.model.PcPropertyinfoForFamily;
import com.srnpr.xmassystem.modelproduct.PlusModelAuthorityLogo;
import com.srnpr.zapcom.baseannotation.ZapcomApi;



/**   
 * 	购物车查询的商品信息对象
*    xiegj
*/
public class GoodsInfoForConfirm  {
	@ZapcomApi(value = "促销种类", remark = "促销种类", demo = "123456")
	private String sales_type = "";
	
   @ZapcomApi(value="活动类型", remark = "(4497472600010001为秒杀)，(4497472600010002为特价)，(4497472600010003为拍卖)，(4497472600010004为扫码购)，(4497472600010005为闪购)，(4497472600010006为内购)，(4497472600010007为TV专场)")
	private String eventType="";
	
	@ZapcomApi(value="商品活动相关显示语",remark="闪购，内购，特价")
	private List<Activity> activitys = new ArrayList<Activity>();
	
	@ZapcomApi(value = "促销活动编号", remark = "促销活动编号", demo = "123456")
	private String sales_code = "";
	
	@ZapcomApi(value = "sku编号", remark = "sku编号",require = 1, demo = "8019123456")
	private String sku_code = "";
	
	@ZapcomApi(value = "商品编号", remark = "商品编号",require = 1, demo = "8016123456")
	private String product_code = "";
	
	@ZapcomApi(value = "当前库存量", remark = "当前库存量", demo = "当前库存量")
	private int now_stock = 0;
	
	@ZapcomApi(value = "促销描述", remark = "促销描述", demo = "促销描述")
	private String sales_info = "";
	
	@ZapcomApi(value = "商品图片链接", remark = "商品图片链接", demo = "http:~~~")
	private String pic_url = "";
	
	@ZapcomApi(value = "商品名称", remark = "商品名称",require = 1, demo = "花露水")
	private String sku_name = "";
	
	@ZapcomApi(value = "商品属性", remark = "商品规格,商品款式",require = 1, demo = "商品规格,商品款式")
	private List<PcPropertyinfoForFamily> sku_property = new ArrayList<PcPropertyinfoForFamily>();
	
	@ZapcomApi(value = "商品价格", remark = "商品价格",require = 1, demo = "")
	private Double sku_price = 0.00;
	
	@ZapcomApi(value = "市场价", remark = "可能为空字符串")
	private String marketPrice = "";
	
	@ZapcomApi(value = "商品数量", remark = "商品数量",require = 1, demo = "123456")
	private int sku_num = 0;
	
	@ZapcomApi(value = "仓储地区", remark = "仓储地区",require = 1, demo = "123456")
	private String area_code = "";
	
	@ZapcomApi(value = "赠品列表", remark = "赠品列表",require = 1, demo = "赠品列表")
	private List<Gift> giftList = new ArrayList<Gift>();

	@ZapcomApi(value="其他相关显示语",remark="赠品")
	private List<String> otherShow = new ArrayList<String>();
	
	@ZapcomApi(value = "是否海外购商品", remark = "1：是，0：否", demo = "1")
	private String flagTheSea = "0";
	
	@ZapcomApi(value="商品标签,已作废：390版本之后不再使用",remark="LB160108100002:生鲜商品;LB160108100003:TV商品;LB160108100004:海外购商品")
    private List<String> labelsList = new ArrayList<String>();

	@ZapcomApi(value="商品标签对应的图片地址",remark="")
    private String labelsPic = "" ;

	@ZapcomApi(value = "商品限购提示语", remark = "不支持配送", demo = "1")
	private String alert = "";
	
	@ZapcomApi(value="权威标志")
	private List<PlusModelAuthorityLogo> authorityLogo = new ArrayList<PlusModelAuthorityLogo>();
	
	@ZapcomApi(value = "是否原价购买标识",remark="0:否；1:是")
	private String flg = "0";
	
	@ZapcomApi(value="商品积分")
	private String integral;
	
	@ZapcomApi(value="是否分销商品标识 0否 1是")
    private int fxFlag = 0;
	
	@ZapcomApi(value="推广赚推广人编号",remark = "")
	private String tgzUserCode = "";
	
	@ZapcomApi(value="推广赚买家秀编号",remark = "")
	private String tgzShowCode = "";
	
	@ZapcomApi(value="添加商品分类(LD商品,普通商品,跨境商品,跨境直邮,平台入驻,缤纷商品)标签字段")
	private String proClassifyTag;
	
	@ZapcomApi(value="是否是加价购商品标识 0否 1是")
	private String ifJJGFlag = "0";
	
	@ZapcomApi(value="加价购商品标识图片地址")
	private String jjgFlagPic = "";

	
    public String getJjgFlagPic() {
		return jjgFlagPic;
	}
	public void setJjgFlagPic(String jjgFlagPic) {
		this.jjgFlagPic = jjgFlagPic;
	}
	public String getIfJJGFlag() {
		return ifJJGFlag;
	}
	public void setIfJJGFlag(String ifJJGFlag) {
		this.ifJJGFlag = ifJJGFlag;
	}
		public String getProClassifyTag() {
		return proClassifyTag;
	}
	public void setProClassifyTag(String proClassifyTag) {
		this.proClassifyTag = proClassifyTag;
	}
	
	public String getTgzUserCode() {
		return tgzUserCode;
	}
	public void setTgzUserCode(String tgzUserCode) {
		this.tgzUserCode = tgzUserCode;
	}
	public String getTgzShowCode() {
		return tgzShowCode;
	}
	public void setTgzShowCode(String tgzShowCode) {
		this.tgzShowCode = tgzShowCode;
	}
	public String getFlagTheSea() {
		return flagTheSea;
	}

	public void setFlagTheSea(String flagTheSea) {
		this.flagTheSea = flagTheSea;
	}

	public String getSales_type() {
		return sales_type;
	}

	public void setSales_type(String sales_type) {
		this.sales_type = sales_type;
	}

	public String getSku_code() {
		return sku_code;
	}

	public void setSku_code(String sku_code) {
		this.sku_code = sku_code;
	}

	public String getSales_info() {
		return sales_info;
	}

	public void setSales_info(String sales_info) {
		this.sales_info = sales_info;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public String getSku_name() {
		return sku_name;
	}

	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	public List<PcPropertyinfoForFamily> getSku_property() {
		return sku_property;
	}

	public void setSku_property(List<PcPropertyinfoForFamily> sku_property) {
		this.sku_property = sku_property;
	}

	public Double getSku_price() {
		return sku_price;
	}

	public void setSku_price(Double sku_price) {
		this.sku_price = sku_price;
	}
	
	public String getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	public int getSku_num() {
		return sku_num;
	}

	public void setSku_num(int sku_num) {
		this.sku_num = sku_num;
	}
	
	public int getFxFlag() {
		return fxFlag;
	}
	
	public void setFxFlag(int fxFlag) {
		this.fxFlag = fxFlag;
	}
	
	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public List<Gift> getGiftList() {
		return giftList;
	}

	public void setGiftList(List<Gift> giftList) {
		this.giftList = giftList;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	public int getNow_stock() {
		return now_stock;
	}

	public void setNow_stock(int now_stock) {
		this.now_stock = now_stock;
	}

	public String getSales_code() {
		return sales_code;
	}

	public void setSales_code(String sales_code) {
		this.sales_code = sales_code;
	}

	public List<Activity> getActivitys() {
		return activitys;
	}

	public void setActivitys(List<Activity> activitys) {
		this.activitys = activitys;
	}

	public List<String> getOtherShow() {
		return otherShow;
	}

	public void setOtherShow(List<String> otherShow) {
		this.otherShow = otherShow;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
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

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public List<PlusModelAuthorityLogo> getAuthorityLogo() {
		return authorityLogo;
	}

	public void setAuthorityLogo(List<PlusModelAuthorityLogo> authorityLogo) {
		this.authorityLogo = authorityLogo;
	}

	public String getFlg() {
		return flg;
	}

	public void setFlg(String flg) {
		this.flg = flg;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}
}

