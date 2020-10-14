package com.cmall.familyhas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.MD5Util;

import net.minidev.json.JSONObject;

public class TestChannel {
	
//	public final static  String url = "http://127.0.0.1:8080/cfamily/jsonapi/";
	public final static  String url = "http://test-hjy.huijiayou.cn/cfamily/jsonapi/";
	public final static  String api_key = "testchannel";
	public final static  String secret = "testchannelapi";

	public static void main(String[] args) {
		
//		getProductList();
//		getProductInfo();
//		getSkuStatus();
//		getSkuStock();
//		getAreaCode();
//		submitAsaleOrder();
//		cancelAsaleOrder();
//		submitLogisticCode();
//		getAsaleOrderStatus();
//		getChannelBalance();
		
//		queryFeigth();
//		preOrder();
		createOrder();
//		cancelOrder();
//		orderStatus();
//		orderTrack();
//		sureOrder();
		
	}
	
	/**
	 * 确认收货
	 */
	public static void sureOrder() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("hjyOrderCode", "DD379021104");
		params.put("time", "");
		getInterfaceResult("com_cmall_channel_api_ApiForOrderSure",JSONObject.toJSONString(params),"确认收货：");
	}
	
	/**
	 * 查询物流
	 */
	public static void orderTrack() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("hjyOrderCode", "DD379021104");
		getInterfaceResult("com_cmall_channel_api_ApiForOrderTrack",JSONObject.toJSONString(params),"物流轨迹：");
	}
	
	/**
	 * 查询订单状态
	 */
	public static void orderStatus() {
		Map<String, Object> params = new HashMap<String, Object>();
		List<String> orderList = new ArrayList<String>();
		orderList.add("DD379021104");
		orderList.add("DD378884104");
		params.put("hjyOrderCode", orderList);
		getInterfaceResult("com_cmall_channel_api_ApiForOrderStatus",JSONObject.toJSONString(params),"订单状态：");
	}
	
	/**
	 * 取消订单
	 */
	public static void cancelOrder() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("hjyOrderCode", "DD378817104");
		getInterfaceResult("com_cmall_channel_api_ApiForCancelOrder",JSONObject.toJSONString(params),"取消订单：");
	}
	
	/**
	 * 创建订单
	 */
	public static void createOrder() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderCode", "9527");
		List<Map<String, Object>> skus = new ArrayList<Map<String, Object>>();
		Map<String, Object> sku2 = new HashMap<String, Object>();
		sku2.put("skuCode", "8019630917");
		sku2.put("skuNum", 2);
		sku2.put("costPrice", 2);
		skus.add(sku2);
		Map<String, Object> sku3 = new HashMap<String, Object>();
		sku3.put("skuCode", "8019632594");
		sku3.put("skuNum", 3);
		sku3.put("costPrice", 3);
		skus.add(sku3);
		params.put("productList", skus);
		params.put("remark", "测试订单");
		params.put("areaCode", "110105034");
		params.put("address", "双流北街38号");
		params.put("person", "王麻子");
		params.put("phone", "18888888888");
		params.put("buyerPhone", "17312349876");
		params.put("createTime", "2019-12-12 15:06:33");
		params.put("transportMoney", 0);
		getInterfaceResult("com_cmall_channel_api_ApiForCreateOrder",JSONObject.toJSONString(params),"创建订单：");
	}
	
	/**
	 * 预下单
	 */
	public static void preOrder() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("areaCode", "110105034");
		List<Map<String, Object>> skus = new ArrayList<Map<String, Object>>();
		Map<String, Object> sku1 = new HashMap<String, Object>();
		sku1.put("skuCode", "8019646641");
		sku1.put("skuNum", 1);
		sku1.put("costPrice", 1);
		skus.add(sku1);
		Map<String, Object> sku2 = new HashMap<String, Object>();
		sku2.put("skuCode", "8019630917");
		sku2.put("skuNum", 2);
		sku1.put("costPrice", 2);
		skus.add(sku2);
		Map<String, Object> sku3 = new HashMap<String, Object>();
		sku3.put("skuCode", "8019632594");
		sku3.put("skuNum", 3);
		sku1.put("costPrice", 3);
		skus.add(sku3);
		params.put("sku", skus);
		params.put("phone", "");
		getInterfaceResult("com_cmall_channel_api_ApiForPreOrder",JSONObject.toJSONString(params),"获取运费列表：");
	}
	
	/**
	 * 查询运费
	 */
	public static void queryFeigth() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("areaCode", "110105034");
		List<Map<String, Object>> skus = new ArrayList<Map<String, Object>>();
		Map<String, Object> sku1 = new HashMap<String, Object>();
		sku1.put("skuCode", "8019646641");
		sku1.put("skuNum", 1);
		skus.add(sku1);
		Map<String, Object> sku2 = new HashMap<String, Object>();
		sku2.put("skuCode", "8019630917");
		sku2.put("skuNum", 2);
		skus.add(sku2);
		Map<String, Object> sku3 = new HashMap<String, Object>();
		sku3.put("skuCode", "8019632594");
		sku3.put("skuNum", 3);
		skus.add(sku3);
		params.put("skus", skus);
		getInterfaceResult("com_cmall_channel_api_ApiForQueryFreight",JSONObject.toJSONString(params),"获取运费列表：");
	}
	
	/**
	 * 获取商品列表
	 */
	public static void getProductList() {
		getInterfaceResult("com_cmall_channel_api_ApiForAllProduct","","获取商品列表：");
	}
	
	/**
	 * 库存查询
	 */
	public static void getSkuStock() {
		List<String> productCode = new ArrayList<String>();
		productCode.add("8016827347");
		productCode.add("8016410985");
		Map<String,Object> stockParams = new HashMap<String,Object>();
		stockParams.put("productCode", productCode);
		getInterfaceResult("com_cmall_channel_api_ApiForProductStock",JSONObject.toJSONString(stockParams),"库存查询：");
	}
	
	/**
	 * 获取商品详情
	 */
	public static void getProductInfo() {
		List<String> productCode = new ArrayList<String>();
		productCode.add("8016827347");
		productCode.add("8016410985");
		Map<String,Object> productInfoParams = new HashMap<String,Object>();
		productInfoParams.put("productCodes", productCode);
		getInterfaceResult("com_cmall_channel_api_ApiForProductInfo",JSONObject.toJSONString(productInfoParams),"获取商品详情：");
						
	}
	
	/**
	 * 获取SKU状态
	 */
	public static void getSkuStatus() {
		List<String> skuCode = new ArrayList<String>();
		skuCode.add("8019630917");
		skuCode.add("8019646660");
		Map<String,Object> skuStatusParams = new HashMap<String,Object>();
		skuStatusParams.put("skuCode", skuCode);
		getInterfaceResult("com_cmall_channel_api_ApiForSkuStatus",JSONObject.toJSONString(skuStatusParams),"获取SKU状态：");
						
	}
	
	/**
	 * 获取配送区域
	 */
	public static void getAreaCode() {
		getInterfaceResult("com_cmall_channel_api_ApiForDistributionArea","","获取配送区域：");
	}
	
	/**
	 * 申请售后
	 */
	public static void submitAsaleOrder() {
		Map<String,Object> asaleMap = new HashMap<String,Object>();
		asaleMap.put("hjyOrderCode", "DD377634104");
		asaleMap.put("type", "1");
		asaleMap.put("received", "1");
		asaleMap.put("reasonCode", "1");
		asaleMap.put("reasonDesc", "zlwt");
		List<Map<String,Object>> skuList = new ArrayList<Map<String,Object>>();
		Map<String,Object> sku = new  HashMap<String,Object>();
		sku.put("skuCode", "8019630917");
		sku.put("skuNum", 1);
		skuList.add(sku);
		asaleMap.put("skuList", sku);
		getInterfaceResult("com_cmall_channel_api_ApiForChannelAsale",JSONObject.toJSONString(asaleMap),"申请售后：");
	}
	
	/**
	 * 取消退换货
	 */
	public static void cancelAsaleOrder() {

		Map<String,String> cancelAsaleParam = new HashMap<String,String>();
		cancelAsaleParam.put("asaleCode", "RGS191212100040");
		getInterfaceResult("com_cmall_channel_api_ApiForCancelChannelAsale",JSONObject.toJSONString(cancelAsaleParam),"取消退换货：");
		
	}
	
	/**
	 * 完善退换货物流
	 */
	public static void submitLogisticCode() {

		Map<String,Object> logisticParam = new HashMap<String,Object>();
		logisticParam.put("asaleCode", "RGS191212100041");
		logisticParam.put("logisticsCode", "shunfeng");
		logisticParam.put("waybill", "992222999889");
		logisticParam.put("transportMoney", 12.00);
		getInterfaceResult("com_cmall_channel_api_ApiForSubmitLogisticsInfo",JSONObject.toJSONString(logisticParam),"完善退换货物流：");
		
	}
	
	/**
	 * 查询退换货状态
	 */
	public static void getAsaleOrderStatus() {

		List<String> asaleOrder = new ArrayList<String>();
		asaleOrder.add("CGS191212100006");
		Map<String,Object> asaleOrderStatusParma = new HashMap<String,Object>();
		asaleOrderStatusParma.put("asaleCode", asaleOrder);
		getInterfaceResult("com_cmall_channel_api_ApiForAsaleStatus",JSONObject.toJSONString(asaleOrderStatusParma),"查询退换货状态：");
		
	}
	
	/**
	 * 查询渠道商预付款余额
	 */
	public static void getChannelBalance() {

		getInterfaceResult("com_cmall_channel_api_ApiForChannelBalance","","查询渠道商预付款余额：");
		
	}

	/**
	 * 
	 */
	public static void getInterfaceResult(String api_target,String api_input,String remark) {
		Map<String,String> map = new HashMap<String,String>();
		String api_timespan = DateUtil.getSysDateTimeString();
		String api_secret = MD5Util.MD5(api_target+api_key+api_input+api_timespan+secret);
		map.put("api_key", api_key);
		map.put("api_target", api_target);
		map.put("api_timespan", api_timespan);
		map.put("api_secret", api_secret);
		map.put("api_input", api_input);
		String result = "";
		try {
			result = httpPostWithForm(url+api_target,map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(remark+result);
	}

	private static String httpPostWithForm(String url, Map<String, String> params) throws ClientProtocolException, IOException {
		List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
		for (Entry<String, String> param : params.entrySet()) {
			pairList.add(new BasicNameValuePair(param.getKey(), param.getValue()));
		}
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(pairList, "utf-8"));
		String respContent = null;
		HttpClient _httpClient = HttpClients.createDefault();
		HttpResponse resp = _httpClient.execute(httpPost);
		HttpEntity he = resp.getEntity();
		respContent = EntityUtils.toString(he, "UTF-8");
		return respContent;
	}
	
	
}
