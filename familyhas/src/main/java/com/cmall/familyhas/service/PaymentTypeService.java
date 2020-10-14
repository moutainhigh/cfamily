package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.rootclass.CacheDefine;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;

public class PaymentTypeService {
	
	static LoadSellerInfo loadSellerInfo = new LoadSellerInfo();

	public static enum Channel{
		/** 安卓渠道 */
		ANDROID,
		/** IOS渠道 */
		IOS,
		/** PC渠道 */
		WEB,
		/** 微信渠道 */
		WAP
	}
	
	public List<String> getSupportPayTypeList(BigDecimal dueMoney, Channel channel, List<String> productCodeList){
		List<String> smallSellerCodeList = new ArrayList<String>();
		LoadProductInfo load = new LoadProductInfo();
		PlusModelProductInfo pi = null;
		PlusModelProductQuery query = null;
		for(String productCode : productCodeList){
			query = new PlusModelProductQuery(productCode);
			pi = load.upInfoByCode(query);
			smallSellerCodeList.add(pi.getSmallSellerCode());
		}
		
		return getPayTypeList(dueMoney, channel, smallSellerCodeList, productCodeList);
	}
	
	public List<String> getSupportPayTypeList(Channel channel, String bigOrderCode){
		String sql = "SELECT DISTINCT small_seller_code,order_code FROM oc_orderinfo WHERE big_order_code = :big_order_code";
		List<Map<String, Object>> itemList = DbUp.upTable("oc_orderinfo").upTemplate().queryForList(sql, new MDataMap("big_order_code",bigOrderCode));
		List<String> smallSellerCodeList = new ArrayList<String>();
		List<String> orderCodeList = new ArrayList<String>();
		for(Map<String, Object> map : itemList){
			smallSellerCodeList.add(""+map.get("small_seller_code"));
			orderCodeList.add(""+map.get("order_code"));
		}
		
		// 读取主库，避免从库延迟导致查询失败
		MDataMap upperMap = DbUp.upTable("oc_orderinfo_upper").onePriLib("big_order_code",bigOrderCode);
		BigDecimal dueMoney = new BigDecimal(upperMap.get("due_money"));
		List<MDataMap> productCodeList = DbUp.upTable("oc_orderdetail").queryIn("product_code", "", "", 
				new MDataMap(), 0, 0, "order_code", StringUtils.join(orderCodeList, ","));
		List<String> productCodes = new ArrayList<String>();
		for (MDataMap data : productCodeList) {
			productCodes.add(data.get("product_code"));
		}
		
		List<String> typeList = getPayTypeList(dueMoney, channel, smallSellerCodeList, productCodes);
		
		// 微匠支付不支持拆单
		sql = "SELECT count(1) FROM oc_orderinfo WHERE big_order_code = :big_order_code";
		Number number = DbUp.upTable("oc_orderinfo").upTemplate().queryForObject(sql, new MDataMap("big_order_code",bigOrderCode), Integer.class);
		if(number != null && number.intValue() > 1){
			typeList.remove("449746280016");
		}
		
		return typeList;
	}
	
	private List<String> getPayTypeList(BigDecimal duDecimal, Channel channel, List<String> smallSellerCodeList, List<String> productCodeList){
		List<PayTypeInfo> typeInfoList = new ArrayList<PaymentTypeService.PayTypeInfo>(DataLoader.queryAllType());
		Iterator<PayTypeInfo> iterator = typeInfoList.iterator();

		List<String> list = new ArrayList<String>();
		PayTypeInfo typeInfo;
		MDataMap sellerTypeMap = null;
		// 过滤支付类型
		while(iterator.hasNext()){
			typeInfo = iterator.next();
			
			// 微匠支付不支持拆单的订单
			if("449746280016".equals(typeInfo.payType) && smallSellerCodeList.size() > 1){
				iterator.remove();
				continue;
			}
			
			// 根据前端渠道过滤支付类型
			if(channel != null){
				// 如果支持的前端渠道不包含当前渠道，则不返回此类型
				if(!typeInfo.channelList.contains(channel)){
					iterator.remove();
					continue;
				}
			}
			
			// 根据商户的禁用设置过滤支付类型
			if(smallSellerCodeList != null){
				for(String smallSellerCode : smallSellerCodeList){
					// 如果有任一个商户被禁用了当前的支付方式则整单都不支持
					if(typeInfo.sellerList.contains(StringUtils.trimToEmpty(smallSellerCode).toUpperCase())){
						iterator.remove();
						break;
					}
					
					// 如果商户类型在被屏蔽中则整单都不支持
					PlusModelSellerInfo sellerInfo = loadSellerInfo.upInfoByCode(new PlusModelSellerQuery(smallSellerCode));
					if(sellerInfo != null && typeInfo.sellerTypeList.contains(sellerInfo.getUc_seller_type())){
						iterator.remove();
						break;
					}
				}
			}
			
			if("449746280020".equals(typeInfo.payType)) {
				boolean flag = true;//是否继续校验标识
				//校验商品
				for (String productCode : productCodeList) {
					if(!typeInfo.typeProductList.contains(productCode)) {
						flag = false;
						iterator.remove();
						break;
					}
				}
				
				if(flag) {
					//校验支付金额
					BigDecimal minMoney = new BigDecimal(TopUp.upConfig("familyhas.unionpay_stages_min_money"));
					if(duDecimal.compareTo(minMoney) < 0) {
						iterator.remove();
						continue;
					}
				}
			}
		}
		
		// 返回过滤后剩余的支付列表
		for(PayTypeInfo info : typeInfoList){
			list.add(info.payType);
		}
		
		return list;
	}
	
	public static class PayTypeInfo{
		private String payType;
		private List<Channel> channelList = new ArrayList<PaymentTypeService.Channel>();
		private List<String> sellerList = new ArrayList<String>();
		private List<String> sellerTypeList = new ArrayList<String>();
		private List<String> typeProductList = new ArrayList<String>();//支付类型支持的商品
	}
	
	/**
	 * 缓存数据到内存中
	 */
	public static class DataLoader {
		
		private static DataLoader instance = new DataLoader();
		private Cache cache;
		
		private DataLoader() {
			CacheDefine cDefine = new CacheDefine();
			String sCacheName = this.getClass().getName();
			CacheConfiguration cacheConfiguration = new CacheConfiguration();

			cacheConfiguration.setName(sCacheName);

			// 设置最大数量
			cacheConfiguration.setMaxEntriesLocalHeap(9999999);
			// 设置最长存活时间
			cacheConfiguration.setTimeToIdleSeconds(900); // 15分钟
			cacheConfiguration.setTimeToLiveSeconds(900);

			cacheConfiguration.setMemoryStoreEvictionPolicy("FIFO");

			cache = cDefine.inCustomCache(sCacheName, cacheConfiguration);
		}
		
		@SuppressWarnings("unchecked")
		public static List<PayTypeInfo> queryAllType(){
			Object oReturnObject = null;
			Element eCachElement = instance.cache.get("queryAllType");
			if (eCachElement != null) {
				oReturnObject = eCachElement.getObjectValue();
			}
			
			if(oReturnObject == null){
				List<MDataMap> typeList = DbUp.upTable("fh_payment_type").queryAll("pay_type,visible", "pay_type", "", new MDataMap()); // 已配置支持的支付类型
				PayTypeInfo typeInfo = null;
				List<PayTypeInfo> typeInfoList = new ArrayList<PaymentTypeService.PayTypeInfo>();
				
				for(MDataMap map : typeList){
					typeInfo = new PayTypeInfo();
					typeInfo.payType = map.get("pay_type");
					
					// 取前端显示的渠道
					String[] texts = map.get("visible").split(",");
					for(String v : texts){
						if(StringUtils.isNotBlank(v)){
							try {
								typeInfo.channelList.add(Channel.valueOf(StringUtils.trimToEmpty(v).toUpperCase()));
							} catch (Exception e) {
								//e.printStackTrace();
							}
						}
					}
					
					// 如果支付类型未配置任何前端显示，则不展示
					if(typeInfo.channelList.isEmpty()){
						continue;
					}
					
					// 取支付屏蔽的商户
					List<MDataMap> dataList = DbUp.upTable("fh_payment_type_seller").queryAll("small_seller_code", "", "", new MDataMap("pay_type",typeInfo.payType));
					for(MDataMap data : dataList){
						typeInfo.sellerList.add(StringUtils.trimToEmpty(data.get("small_seller_code")).toUpperCase());
					}
					
					// 取屏蔽的商户类型
					dataList = DbUp.upTable("fh_payment_type_seller_type").queryAll("small_seller_type", "", "", new MDataMap("pay_type",typeInfo.payType));
					for(MDataMap data : dataList){
						typeInfo.sellerTypeList.add(StringUtils.trimToEmpty(data.get("small_seller_type")).toUpperCase());
					}
					
					//支付类型支持的商品
					dataList = DbUp.upTable("fh_payment_type_product").queryAll("product_code", "", "", new MDataMap("pay_type",typeInfo.payType));
					for (MDataMap data : dataList) {
						typeInfo.typeProductList.add(data.get("product_code"));
					}
					typeInfoList.add(typeInfo);
				}
				
				oReturnObject = typeInfoList;
				instance.cache.put(new Element("queryAllType", oReturnObject));
			}
			
			return (List<PayTypeInfo>)oReturnObject;
		}
	}
}
