package com.cmall.familyhas.api.input;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

/** 
* @Author fufu
* @Time 2020-8-4 17:18:58 
* @Version 1.0
* <p>Description:</p>
*/
public class ApiForSubscribeInput extends RootInput {
	
	@ZapcomApi(value="商品编号",remark="订阅通知的商品编号",require=0)
	private String productCode = "";
	
	@ZapcomApi(value="用户微信标识openId",remark="用户标识，调用推送消息网关用",require=1)
	private String openId = "";
	
	@ZapcomApi(value="请求来源",remark="来源：(xcx:小程序)，(wxshop:微信商城)，(ios:苹果APP) ,(android:安卓APP)", require=0,demo="xcx")
	private String source = "";
	
	@ZapcomApi(value="预约类型",remark="1：预约，0：取消预约", require=1)
	private int  type = 1;
	
	@ZapcomApi(value="订阅类型",remark="(449748680003	购物车降价提醒 )，(449748680002	收藏降价提醒 ),( 449748680001	积分/惠币打卡提醒)", require=1)
	private String subscribeType = "";
	
	@ZapcomApi(value="设置推送时间",remark="活动开始前X分钟前通知。X为入参数字，非必填项，如果为空，则默认距活动开始2分钟前推送",require=0)
	private int time = 2;

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getSubscribeType() {
		return subscribeType;
	}

	public void setSubscribeType(String subscribeType) {
		this.subscribeType = subscribeType;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

}
