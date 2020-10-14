package com.cmall.familyhas.api.input;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 可用优惠劵查询接口输入参数
 * @author liqt
 *
 */
public class ApiGetAvailableCouponInput extends RootInput{
	
	@ZapcomApi(value="应付金额",remark="应付金额大于限制金额时才可以使用优惠劵",require=1)
	private BigDecimal shouldPay=new BigDecimal(0.00);

	@ZapcomApi(value="商品列表",remark="仅用于扩展")
	private List<GoodsInfoForAdd> goods = new ArrayList<GoodsInfoForAdd>();
	
	@ZapcomApi(value="商品列表",remark="仅用于扩展(兼容android3.7.4参数)")
	private List<GoodsInfoForAdd> skuCodeEntitylist = new ArrayList<GoodsInfoForAdd>();
	
	@ZapcomApi(value = "渠道编号", remark = "惠家有app：449747430001，wap商城：449747430002，微信商城：449747430003，PC：449747430004", demo = "123456")
	private String channelId = "449747430001";
	
	@ZapcomApi(value = "支付类型", remark = "在线支付：449716200001，货到付款：449716200002", demo = "449716200001")
	private String paymentType = "";
	
	@ZapcomApi(value = "是否显示内购", remark = "默认值为0，显示内购活动传递1")
	private Integer isPurchase = 0;
	
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getIsPurchase() {
		return isPurchase;
	}

	public void setIsPurchase(Integer isPurchase) {
		this.isPurchase = isPurchase;
	}
	
	public BigDecimal getShouldPay() {
		return shouldPay;
	}

	public void setShouldPay(BigDecimal shouldPay) {
		this.shouldPay = shouldPay;
	}

	public List<GoodsInfoForAdd> getGoods() {
		return goods;
	}

	public void setGoods(List<GoodsInfoForAdd> goods) {
		this.goods = goods;
	}

	public List<GoodsInfoForAdd> getSkuCodeEntitylist() {
		return skuCodeEntitylist;
	}

	public void setSkuCodeEntitylist(List<GoodsInfoForAdd> skuCodeEntitylist) {
		this.skuCodeEntitylist = skuCodeEntitylist;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
}
