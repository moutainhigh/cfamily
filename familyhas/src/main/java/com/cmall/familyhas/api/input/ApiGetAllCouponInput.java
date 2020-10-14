package com.cmall.familyhas.api.input;


import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/**
 * 全部优惠劵查询接口输入参数
 * @author liqt
 *
 */
public class ApiGetAllCouponInput extends RootInput{
	@ZapcomApi(value = "第几页",remark = "输入页码，,从1开始为第一页" ,demo= "5",require = 1)
	private int pageNum=1;
	
	@ZapcomApi(value="用户点击的页面编号",remark="0代表未使用优惠券，1代表历史优惠券，2代表已使用优惠券，3代表已过期优惠券",demo="0",require = 1)
	private int  couponLocation=0;

	@ZapcomApi(value = "渠道编号", remark = "惠家有app：449747430001，wap商城：449747430002，微信商城：449747430003", demo = "123456")
	private String channelId = "449747430001";
	
	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	
	public int getCouponLocation() {
		return couponLocation;
	}

	public void setCouponLocation(int couponLocation) {
		this.couponLocation = couponLocation;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	
}
