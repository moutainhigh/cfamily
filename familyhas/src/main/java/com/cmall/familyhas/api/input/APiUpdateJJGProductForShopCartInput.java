package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.xmasorder.model.ShoppingCartGoodsInfoForAdd;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/****
 * 购物车结构体
 * @author 
 *
 */
public class APiUpdateJJGProductForShopCartInput extends RootInput {

	@ZapcomApi(value = "渠道编号", remark = "惠家有app：449747430001，wap商城：449747430002，微信商城：449747430003,PC：449747430004", demo = "123456")
	private String channelId = "449747430001";
	
	@ZapcomApi(value = "加价购活动编号")
	private String jjgEventCode = "";
	
	@ZapcomApi(value = "要保存的该加价购活动下的skuCode，如果是多个则用,拼接",demo="12345,87650")
	private String skuCodes = "";

	@ZapcomApi(value="换购总金额" , remark = "前端传所有的选择商品的总价格，后台做校验使用")
	private String sumRepurchaseMoney = "0";
	
	public String getSumRepurchaseMoney() {
		return sumRepurchaseMoney;
	}

	public void setSumRepurchaseMoney(String sumRepurchaseMoney) {
		this.sumRepurchaseMoney = sumRepurchaseMoney;
	}
	
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getJjgEventCode() {
		return jjgEventCode;
	}

	public void setJjgEventCode(String jjgEventCode) {
		this.jjgEventCode = jjgEventCode;
	}

	public String getSkuCodes() {
		return skuCodes;
	}

	public void setSkuCodes(String skuCodes) {
		this.skuCodes = skuCodes;
	}
	
	
	
}
