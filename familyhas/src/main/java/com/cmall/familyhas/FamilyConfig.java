package com.cmall.familyhas;

public class FamilyConfig {

	/**
	 *当前app编号 
	 * 
	 */
	public static String manageCode = "SI2003";
	
	public static String partner = "2088511816887140"; // 合作身份者ID，以2088开头由16位纯数字组成的字符串
	//商户私钥
	public static String key="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMCKIVvh6FMB50R1mUBeKR05nFTW/bHTnaAaRfR2eQ0+C31YHuGG61estWB6XzFersHYKb18BdZLb/Ra+s4vQauRGFZiFfsqc7HKrRVCfYDaPs9u5LEDRr2975sAYZiKOffgcUgwcR9Oj0JaqWI91xL8Pnl3gyVtIsLf7iD7rhlXAgMBAAECgYAqyMb+5uU8RMkCQmuKjSHvt5SQmbGIKXD2WcA/wW/GzIm7EbDTBqsXMW6ggLDUhKiqtIEZ9QxLATpgfzMKTB/4QB0A3OJ6Uw9qpKdR8x7XMPRMehZnbw6JQ7ZwbGPjqkMN0U7MlqUM0e+ufekSjUBWmSPH63yJlZGMgl3LQvdE2QJBAOkv17uzcuoJ6pmGEzUjlbWhg0LYNisAa8iOUnUkrqzn/y7mtEx4p9zqTjRAu2yy0/ck2LpNY9GuMlg+NL5vS1sCQQDTYEbtWDE1pFaZPLCMkprLQLhT3eeLQzP7d60mfQW96W3QjprlipvHj4kDssX1/RnQleivNY/JfepFcelOzBa1AkAZ44HkCOw9J5SwLr57K9Q3MhNMnIyHAaj1vzdQYh4yfB9MqbhitRKN6EV+b6FfVAtMaP7W0DjA0sIsIdvhOKH5AkBft5BGuBIIlXN1jqrv7Q9VjOgraigIwxTOAcKR1Dl+Zy8IKxtvaFXkh1XnK9RC8Sr4bnngpWOIPZGRguTAfuClAkEAoMteToYMktB3/Skb+mkL0OZrU6GPdJ2HO9XHnYlXQNdpB3v28DTXJLDYEVAhUVZ5JScuoJWpBW2H4BCoG2mBXw==";
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	public static String seller_email = "weigongshe@52yungo.com"; // //乾和企业级账号
	// 移动支付即时到帐接口名称
	public static String serviceMove = "mobile.securitypay.pay";

	// 签名方式 不需修改
	public static String sign_type = "RSA";
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	//支付宝http链接
	public static String ali_url_http = "https://wappaygw.alipay.com/service/rest.htm?";
	
	public static String jyh = "SI2003";//惠家有的应用编号
	public static String hjy = "SI2009";//家有汇的应用编号
	
	/**订单状态：下单成功-未付款*/
	public static final String ORDER_STATUS_UNPAY = "4497153900010001";
	/**订单状态：下单成功-未发货*/
	public static final String ORDER_STATUS_PAYED = "4497153900010002";
	/**已发货*/
	public static final String ORDER_STATUS_DELIVERED = "4497153900010003";
	/**交易成功*/
	public static final String ORDER_STATUS_TRADE_SUCCESS = "4497153900010005";
	/**交易失败*/
	public static final String ORDER_STATUS_TRADE_FAILURE = "4497153900010006";
	
	/**是否参与清分：1 是 0 否*/
	public static final String ISRECKON_YES = "1";
	/**是否参与清分：1 是 0 否*/
	public static final String ISRECKON_NO = "0";
	/**接口同步标志：1 成功 0 失败*/
	public static final String RSYNC_SUCCESS = "1";
	/**接口同步标志：1 成功 0 失败*/
	public static final String RSYNC_FAILURE = "0";
	/**结算统计日期：本月*/
	public static final String OC_PERIOD_MONTH_CURR = "本月";
	/**结算统计日期：上月*/
	public static final String OC_PERIOD_MONTH_PRE = "上月";
	/**结算统计日：月末*/
	public static final String OC_PERIOD_MONTH_END = "月末";
	
	/**支付方式：支付宝*/
	public static final String OC_PAYTYPE_ALIPAY = "449746280003";
	
	/**支付方式：微信*/
	public static final String OC_PAYTYPE_WECHAT = "449746280005";
	
	/**身份证状态：通关正常*/
	public static final String CUSTOMS_STATUS_NORMAL = "0";
	
	/**身份证状态：通关失败*/
	public static final String CUSTOMS_STATUS_FAILURE = "1";
	
	/**是否海外购标识 1：是*/
	public static final String OVER_SEAS_FLAG_YES = "1";
	
	/**是否海外购标识 0：否*/
	public static final String OVER_SEAS_FLAG_NO = "1";
	
	/**海关通关未通过*/
	public static final String KJT_CUSTOMS_STATUS_NO = "65";
	
	/**猜你喜欢控制开关相关标识*/
	public static final String ZW_DEFINE_MAYBELOVE_ID = "46992326";
	
	/**谷歌推荐是否展示标识*/
	public static final String GOOGLE_REC_DISPLAY = "google_rec_display";
	
	/**百分点推荐是否展示标识*/
	public static final String BFD_DISPLAY = "bfd_display";
	
	/**猜你喜欢是否展示标识*/
	public static final String MAYBELOVE_DISPLAY = "maybelove_display";
	
	
}
