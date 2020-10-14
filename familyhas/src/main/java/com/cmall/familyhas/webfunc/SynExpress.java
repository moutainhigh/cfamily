package com.cmall.familyhas.webfunc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.cmall.ordercenter.common.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordercenter.express.service.OrderShipmentsService;
import com.srnpr.zapcom.basehelper.ALibabaJsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.dbsupport.DbTemplate;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;


/**
 * 实时查询(同步)快递运单流水
 * @author wpt
 * 
 */
public class SynExpress extends RootFunc{

	//快递100给我们分配的实时快递查询接口的授权密钥
	public static String SECRET_KEY = "RXcJgYVX6346";
	//快递100给我们分配的实时快递查询接口的公司编码
	public static String COMPANY_CODE = "BD7DBE4466F61BE5997EAE27108038B0";
	//快递100提供的实时查询订单的接口url
	public static String CURRENT_URL = "http://poll.kuaidi100.com/poll/query.do";
	//目前给我们分配的实时快递查询接口的最高查询线程数是1单/秒
	
	static OrderShipmentsService shipmentsService = new OrderShipmentsService();
	
	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String logisticseCode = mDataMap.get("zw_f_logisticseCode");//快递公司编码
		String waybill = mDataMap.get("zw_f_waybill");//运单编号
		String orderCode = mDataMap.get("zw_f_orderCode");//运单编号
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("orderCode", orderCode);
		
		MDataMap pmMap = new MDataMap();
		pmMap.put("com", logisticseCode);
		pmMap.put("num", waybill);
		// 548需求添加resultv2标记
		pmMap.put("resultv2", "1");
		
		// 快递100接口要求顺风单号单独添加一个手机号
		if("shunfeng".equalsIgnoreCase(logisticseCode)){
			String phone = (String)DbUp.upTable("oc_orderadress").dataGet("mobilephone", "", new MDataMap("order_code",orderCode));
			pmMap.put("phone", StringUtils.trimToEmpty(phone));
		}
		
		//调用快递100所需参数：com-快递公司编码，num-快递单号，授权密钥，公司编码
		String param = ALibabaJsonHelper.toJson(pmMap);
		String customer = COMPANY_CODE;
		String key = SECRET_KEY;
		//快递公司编码+快递单号+本公司编码+授权密钥的MD5加密字符串
		String sign = encode(param+key+customer);
		
		MDataMap expressParams = new MDataMap();
		expressParams.put("param",param);
		expressParams.put("sign",sign);
		expressParams.put("customer",customer);
		String resp;
		try {
			//请求快递100返回数据
			resp = upPost(CURRENT_URL, expressParams);
			ObjectMapper resultMapper=new ObjectMapper();
			Map<String,Object> resultMap = resultMapper.readValue(resp, Map.class);
			String resultMessage = "当前运单流水为最新数据！";
			if(null != resultMap){
				//如果有result的key并且key值为false则判断为失败，否则成功
				if(resultMap.containsKey("result") && "false".equals(resultMap.get("result").toString())){
					mResult.setResultMessage(resultMap.get("message").toString());
				}else{
					// 记录查询的日志
					shipmentsService.onCallKuaidi100(OrderShipmentsService.CALL_TYPE_QUERY, orderCode, logisticseCode, waybill);
					
					DbTemplate dt = DbUp.upTable("oc_express_detail").upTemplate();
					if(resultMap.containsKey("data")){
						List<Map<String,String>> list = (List<Map<String,String>>)resultMap.get("data");
						paramMap.put("nu", resultMap.get("nu"));//运单号
						paramMap.put("com", resultMap.get("com"));//快递公司编码
						
						
						//遍历运单流水，同步到库中
						if(null != list && list.size() > 0){
							MDataMap logisticsMap = new  MDataMap();
							for (Map<String, String> map : list) {
								paramMap.put("context", map.get("context"));//描述
								paramMap.put("ftime", map.get("ftime"));//时间
								// 548需求添加字段：状态、城市编码、城市名称
								paramMap.put("status", StringUtils.trimToEmpty(map.get("status")));
								paramMap.put("areaCode", StringUtils.trimToEmpty(map.get("areaCode")));
								paramMap.put("areaName", StringUtils.trimToEmpty(map.get("areaName")));
								List<Integer> count = dt.queryForList("select zid from oc_express_detail where order_code = :orderCode and time = :ftime",
										paramMap, Integer.class);
								// 只插入数据库中不存在的运单流水
								if (count.size() == 0) {
									// 548添加,根据订单号查询最后一条物流信息
									Map<String, Object> lastExpress = DbUp.upTable("oc_express_detail").dataSqlOne
											("SELECT * FROM oc_express_detail WHERE order_code = '" + orderCode + "' ORDER BY time DESC LIMIT 1", new MDataMap());
									
									dt.update("insert into oc_express_detail(order_code,logisticse_code,waybill,context,time,status,areaCode,areaName) value "
											+ "(:orderCode,:com,:nu,:context,:ftime,:status,:areaCode,:areaName)",	paramMap);
									
									// 548添加,城市发生变化，或者是物流签收状态变化，则往push消息表插入数据
						          	if(null != lastExpress) {
						          		if(lastExpress.get("time").toString().compareTo(paramMap.get("ftime").toString()) < 0) {
						          			String areaCode = (String) lastExpress.get("areaCode");
						          			String status = (String) lastExpress.get("status");
						          			String newAreaCode = (String) paramMap.get("areaCode");
						          			String newStatus = (String) paramMap.get("status");
						          			if(newAreaCode.equals(areaCode)) { // 城市相同则看物流状态
						          				if(newStatus.equals(status)){
						          					// 物流状态不变,不发通知
						          				}else { // 发送通知
						          					logisticsMap = this.saveLogisticsNotice(paramMap);
						          				}
						          			}else { //城市不同,发送通知
						          				if("在途".equals(paramMap.get("status"))) {
						          					logisticsMap = this.saveLogisticsNotice(paramMap);
						          				}else {
						          					if(newStatus.equals(status)){
						          						// 物流状态不变,不发通知
						          					}else { // 发送通知
						          						logisticsMap = this.saveLogisticsNotice(paramMap);
						          					}
						          				}
						          			}
						          		}
						          	}else { // 没有物流信息,直接新增
						          		logisticsMap = this.saveLogisticsNotice(paramMap);
						          	}
								}
							}
							if(null != logisticsMap && logisticsMap.size() > 0) {				
								DbUp.upTable("nc_logistics_notice_push_news").dataInsert(logisticsMap);
							}
						}else{
							mResult.setResultMessage(resultMessage);
						}
					}else{
						mResult.setResultMessage(resultMessage);
					}
				}
			}else{
				mResult.setResultMessage(resultMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mResult;
	}
	
	// 548添加,封装要发送的物流通知存入push表中
	public MDataMap saveLogisticsNotice(Map<String,Object> paramMap) {
		MDataMap logisticsMap = new  MDataMap();
		DbTemplate dt = DbUp.upTable("oc_express_detail").upTemplate();
		// 推送用户(购买人)
  		String member_code = (String)DbUp.upTable("oc_orderinfo").dataGet("buyer_code", "", new MDataMap("order_code",(String)paramMap.get("orderCode")));
  		// 根据快递公司编号查询快递名称
  		String company_name = (String)DbUp.upTable("sc_logisticscompany").dataGet("company_name", "", new MDataMap("company_code",(String)paramMap.get("com")));
  		String title = "";
  		String message = "";
  		if("揽收".equals(paramMap.get("status"))) {
  			title = "商品发货通知";
  			message = "您的商品已经通过"+company_name+"快递发货";
  		}else if("签收".equals(paramMap.get("status"))) {
  			title = "商品签收提醒";
  			message = company_name+"快递显示您的订单已签收";
  		}else if("派件".equals(paramMap.get("status"))) {
  			title = "商品派件通知";
  			message = company_name+"快递已安排为您配送，请注意查收";
  		}else if("退回".equals(paramMap.get("status"))) {
  			title = "商品拒收提醒";
  			message = company_name+"快递显示您的订单拒绝签收，请知悉";
  		}else if("在途".equals(paramMap.get("status"))) {
  			if(StringUtils.isNotBlank((String)paramMap.get("areaName"))) {  				
  				title = "商品物流通知";
  				message = "您购买的商品已经抵达"+paramMap.get("areaName");
  			}
  		}
  		// 商品主图
  		List<String> prod_main_pic = dt.queryForList("SELECT mainpic_url FROM productcenter.pc_productinfo pp WHERE pp.product_code = "
  				+ "( SELECT oo.product_code FROM ordercenter.oc_orderdetail oo WHERE oo.order_code = :orderCode LIMIT 1 )", paramMap, String.class);
  		
  		if(!"".equals(title)) {
  			logisticsMap.put("uid", UUID.randomUUID().toString().replaceAll("-", ""));
  			logisticsMap.put("member_code", StringUtils.trimToEmpty(member_code));
  			logisticsMap.put("title", title);
  			logisticsMap.put("message", message);
  			logisticsMap.put("prod_main_pic", prod_main_pic.isEmpty()?"":prod_main_pic.get(0));
  			logisticsMap.put("create_time", DateUtil.getSysDateTimeString());
  			logisticsMap.put("push_times", "0");
  			logisticsMap.put("order_code", (String)paramMap.get("orderCode"));
  			logisticsMap.put("waybill", (String) paramMap.get("nu"));
  			logisticsMap.put("to_page", "14");
  			logisticsMap.put("if_read", "0");
  			
  			//DbUp.upTable("nc_logistics_notice_push_news").dataInsert(logisticsMap);
  		}
  		return logisticsMap;
	}
	
	
	// 获得MD5摘要算法的 MessageDigest 对象
	private static MessageDigest _mdInst = null;
	private static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	private static MessageDigest getMdInst() {
		if (_mdInst == null) {
			try {
				_mdInst = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return _mdInst;
	}

	public final static String encode(String s) {
		try {
			byte[] btInput = s.getBytes();
			// 使用指定的字节更新摘要
			getMdInst().update(btInput);
			// 获得密文
			byte[] md = getMdInst().digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取请求
	 * 
	 * @param sUrl
	 * @param httpEntity
	 * @return
	 * @throws Exception
	 */
	public static String doRequest(String sUrl, HttpEntity httpEntity)
			throws Exception {
		String sReturnString = null;
		HttpClientBuilder hClientBuilder = HttpClientBuilder.create();

		CloseableHttpClient httpclient = hClientBuilder.build();

		HttpPost httppost = new HttpPost(sUrl);
		// 设置成短链接模式 关闭keep-alve
		httppost.setHeader("Connection", "close");
		CloseableHttpResponse response = null;

		try {

			httppost.setEntity(httpEntity);

			response = httpclient.execute(httppost);

			HttpEntity resEntity = response.getEntity();

			if (resEntity != null) {

				sReturnString = EntityUtils.toString(resEntity,"UTF-8");

			}
			if (resEntity != null) {

				EntityUtils.consume(resEntity);

			}

		} catch (Exception e) {
			httppost.reset();
			httpclient = null;
			e.printStackTrace();
			throw e;

		} finally {
			response.close();

			httppost.reset();
			httpclient.close();
			httpclient = null;

		}

		return sReturnString;
	}
	
	/**
	 * 根据链接获取post数据
	 * 
	 * @param sUrl
	 * @param mDataMap
	 * @return
	 * @throws Exception
	 */
	public static String upPost(String sUrl, MDataMap mDataMap)
			throws Exception {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String sKey : mDataMap.keySet()) {
			nvps.add(new BasicNameValuePair(sKey, mDataMap.get(sKey)));
		}
		HttpEntity httpEntity = new UrlEncodedFormEntity(nvps,
				TopConst.CONST_BASE_ENCODING);

		return  doRequest(sUrl, httpEntity);
	}
}
