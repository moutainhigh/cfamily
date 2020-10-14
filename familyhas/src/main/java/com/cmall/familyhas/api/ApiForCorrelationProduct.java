package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForCorrelationProductInput;
import com.cmall.familyhas.api.model.CorrelationProduct;
import com.cmall.familyhas.api.result.ApiForCorrelationProductResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 支付结果页关联商品接口
 * @remark 
 * @author 任宏斌
 * @date 2019年12月25日
 */
public class ApiForCorrelationProduct extends RootApiForVersion<ApiForCorrelationProductResult, ApiForCorrelationProductInput> {

	@Override
	public ApiForCorrelationProductResult Process(ApiForCorrelationProductInput inputParam, MDataMap mRequestMap) {
		ApiForCorrelationProductResult result = new ApiForCorrelationProductResult();
		String channelId = getChannelId();
		//优先判断开关
		if("on".equals(bConfig("familyhas.pay_success_correlation_product_switch"))) {
			
			String bigOrderCode = inputParam.getOrderCode();
			if(StringUtils.isEmpty(bigOrderCode)) return result;
			
			String sql1 = "SELECT DISTINCT od.product_code "
					+ "FROM ordercenter.oc_orderinfo oi LEFT JOIN ordercenter.oc_orderdetail od "
					+ "ON oi.order_code=od.order_code WHERE oi.big_order_code=:big_order_code";
			List<Map<String, Object>> productList = DbUp.upTable("oc_orderinfo").dataSqlList(sql1, new MDataMap("big_order_code", bigOrderCode));
			if(null != productList && !productList.isEmpty()) {
				
				Set<String> temp = new HashSet<String>(); //用来拼接sql的分类编号
				Set<String> correlationCategory = new HashSet<String>(); //分类编号
				List<Map<String, Object>> allProductList = new ArrayList<Map<String, Object>>();
				String show_ld = "0";
				
				for (Map<String, Object> map : productList) {
					String product_code = MapUtils.getString(map, "product_code");
					MDataMap correlation = DbUp.upTable("pc_product_correlation").one("product_code", product_code);
					if(null != correlation && StringUtils.isNotEmpty(correlation.get("correlation_category"))) {
						show_ld = correlation.get("show_ld");
						String correlation_category = correlation.get("correlation_category");
						
						String[] split = correlation_category.split(",");
						for (String s : split) {
							correlationCategory.add(s);
							temp.add("'" + s + "'");
						}
					}
				}
				
				if(temp.size() > 0) {
					
					//根据分类查商品、近一个月销量
					String sql2 = " SELECT DISTINCT u.product_code,ifnull(m.sales,0) sales FROM usercenter.uc_sellercategory_product_relation u "
							+ " LEFT JOIN productcenter.pc_productinfo p ON u.product_code=p.product_code "
							+ " LEFT JOIN productcenter.pc_productsales_month m ON u.product_code= m.product_code "
							+ " WHERE u.category_code in ( " + StringUtils.join(temp, ",")
							+ " ) AND p.product_status='4497153900060002' ";
					if("0".equals(show_ld)) sql2 += " AND p.small_seller_code != 'SI2003' ";
					List<Map<String, Object>> categoryProductList = DbUp.upTable("uc_sellercategory_product_relation").dataSqlList(sql2, new MDataMap());
					if(null != categoryProductList && !categoryProductList.isEmpty()) {
						Iterator<Map<String, Object>> iterator = categoryProductList.iterator();
						while (iterator.hasNext()) {
							Map<String, Object> next = iterator.next();
							String productCode = MapUtils.getString(next, "product_code");
							int stock = new PlusSupportStock().upAllStockForProduct(productCode);
							if(stock<=0) iterator.remove();
						}
						allProductList.addAll(categoryProductList);
					}
				}
				
				if(allProductList.size() >= 3) {
					Collections.sort(allProductList, new Comparator<Map<String, Object>>() {
						@Override
						public int compare(Map<String, Object> m1, Map<String, Object> m2) {
							Integer s1 = MapUtils.getInteger(m1, "sales");
							Integer s2 = MapUtils.getInteger(m2, "sales");
							int result = 0;
							if (s1 > s2)      result =  1;
							else if (s1 < s2) result = -1;
							else              result =  0;
							return result;
						}
					});
					List<Map<String, Object>> subList = allProductList.size() > 6 ? allProductList.subList(0, 6) : allProductList;
					
					for (Map<String, Object> map : subList) {
						CorrelationProduct correlationProduct = new CorrelationProduct();
						String productCode = MapUtils.getString(map, "product_code");
						
						PlusModelProductInfo plusModelProductInfo = new LoadProductInfo()
								.upInfoByCode(new PlusModelProductQuery(productCode));
						
						correlationProduct.setProductCode(plusModelProductInfo.getProductCode());
						correlationProduct.setProductName(plusModelProductInfo.getProductName());
						correlationProduct.setProductPic(plusModelProductInfo.getMainpicUrl());
						
						PlusModelSkuQuery plusModelSkuQuery = new PlusModelSkuQuery();
						plusModelSkuQuery.setCode(productCode);
						plusModelSkuQuery.setChannelId(channelId);
						Map<String, BigDecimal> productMinPrice = new ProductPriceService()
								.getProductMinPrice(plusModelSkuQuery);
						
						correlationProduct.setProductPrice(productMinPrice.get(productCode));
						
						result.getProducts().add(correlationProduct);
					}
					
					result.setcCategory(StringUtils.join(correlationCategory, ","));
				}
			}
		}
		
		return result;
	}
}
