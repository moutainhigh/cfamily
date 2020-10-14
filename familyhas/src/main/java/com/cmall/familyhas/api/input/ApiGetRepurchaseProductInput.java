package com.cmall.familyhas.api.input;

import com.srnpr.xmasorder.model.RepurchaseEvent;
import com.srnpr.xmasorder.model.ShoppingCartGoodsInfoForAdd;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.result.PriceChooseObj;

/**
 * 搜索输入参数
 * @author zhangbo
 *
 */
public class ApiGetRepurchaseProductInput extends RootInput{
	
	@ZapcomApi(value = "活动编号")
	private String eventCode = "";
	
	@ZapcomApi(value = "已经选择的商品编号",remark="如果多个商品则用,隔开")
	private String selectedSkuCodes = "";
	
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	@ZapcomApi(value = "用户编号")
	private String isMemberCode = "";
	
	@ZapcomApi(value = "渠道编号", remark = "惠家有app：449747430001，wap商城：449747430002，微信商城：449747430003,PC：449747430004", demo = "123456")
	private String channelId = "449747430001";
	

	public Integer getIsPurchase() {
		return isPurchase;
	}

	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}

	public String getIsMemberCode() {
		return isMemberCode;
	}

	public void setIsMemberCode(String isMemberCode) {
		this.isMemberCode = isMemberCode;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@ZapcomApi(value="页数")
	private int page = 1;
	
	@ZapcomApi(value="每页条数")
	private int pageSize = 20;
	
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSelectedSkuCodes() {
		return selectedSkuCodes;
	}

	public void setSelectedSkuCodes(String selectedSkuCodes) {
		this.selectedSkuCodes = selectedSkuCodes;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	
	
}
	
