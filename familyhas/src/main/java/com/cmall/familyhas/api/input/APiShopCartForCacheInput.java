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
public class APiShopCartForCacheInput extends RootInput {

	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	@ZapcomApi(value = "用户编号")
	private String isMemberCode = "";
	
	@ZapcomApi(value = "渠道编号", remark = "惠家有app：449747430001，wap商城：449747430002，微信商城：449747430003,PC：449747430004", demo = "123456")
	private String channelId = "449747430001";
	
	@ZapcomApi(value = "商品列表")
	private List<ShoppingCartGoodsInfoForAdd> goodsList = new ArrayList<ShoppingCartGoodsInfoForAdd>();

	public List<ShoppingCartGoodsInfoForAdd> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<ShoppingCartGoodsInfoForAdd> goodsList) {
		this.goodsList = goodsList;
	}

	
	
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	/**
	 * @return the isPurchase
	 */
	public Integer getIsPurchase() {
		return isPurchase;
	}

	/**
	 * @param isPurchase the isPurchase to set
	 */
	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}

	/**
	 * @return the isMemberCode
	 */
	public String getIsMemberCode() {
		return isMemberCode;
	}

	/**
	 * @param isMemberCode the isMemberCode to set
	 */
	public void setIsMemberCode(String isMemberCode) {
		this.isMemberCode = isMemberCode;
	}
	
	
	
}
