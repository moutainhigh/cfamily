package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.familyhas.api.input.ApiHbOrderListInput;
import com.cmall.familyhas.api.result.ApiForQueryHbAgentOrderInfo;
import com.cmall.familyhas.api.result.ApiHbOrderListResult;
import com.cmall.familyhas.api.result.ApiSellerListResult;
import com.cmall.familyhas.api.result.ApiSellerStandardAndStyleResult;
import com.cmall.ordercenter.service.OrderService;
import com.cmall.productcenter.model.PcPropertyinfoForFamily;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuQuery;
import com.srnpr.xmassystem.very.PlusVeryImage;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webmodel.MFileItem;

/**
 * 惠币订单列表接口
 * 
 * @author wz
 */
public class ApiHdOrderList extends
		RootApiForToken<ApiHbOrderListResult, ApiHbOrderListInput> {
	
	static int pageSize = 10;
	static LoadProductInfo loadProductInfo = new LoadProductInfo();
	static LoadSkuInfo loadSkuInfo = new LoadSkuInfo();
	static PlusVeryImage plusVeryImage = new PlusVeryImage();
	static OrderService orderService = new OrderService();
	
	

	public ApiHbOrderListResult Process(ApiHbOrderListInput inputParam,
			MDataMap mRequestMap) {
		
		ApiHbOrderListResult apiResult = new ApiHbOrderListResult();
		
		if(!getFlagLogin()) {
			return apiResult;
		}
		
		int nextPage = NumberUtils.toInt(inputParam.getNextPage(), 1);
		if(nextPage < 1) nextPage = 1;
		
		MDataMap mQueryMap = new MDataMap();
		mQueryMap.put("user_code", getUserCode());
		
		List<String> whereList = new ArrayList<String>();
//		if("1".equals(inputParam.getQueryType())) {
//			whereList.add(" AND aod.agent_code = :user_code");
//		} else {
//			whereList.add(" AND aod.agent_parent_code = :user_code");
//		}
//		
		if(StringUtils.isNotBlank(inputParam.getOrderStatus())) {
			whereList.add(" AND oi.order_status = :order_status");
			mQueryMap.put("order_status", inputParam.getOrderStatus());
		}
		
//		Date startDate = null,endDate = null;
//		try {
//			startDate = DateUtils.parseDate(inputParam.getStartDate(), "yyyy-MM-dd");
//			endDate = DateUtils.parseDate(inputParam.getEndDate(), "yyyy-MM-dd");
//		} catch (ParseException e) {
//			startDate = null;
//			endDate = null;
//			LogFactory.getLog(getClass()).warn("日期格式错误:["+inputParam.getStartDate()+", "+inputParam.getEndDate()+"]");
//		}
		
//		// 开始时间包含当天
//		if(startDate != null) {
//			whereList.add(" AND aod.create_time >= '"+DateFormatUtils.format(startDate, "yyyy-MM-dd")+"'");
//		}
//		
//		// 结束时间也包含当天
//		if(endDate != null) {
//			whereList.add(" AND aod.create_time < '"+DateFormatUtils.format(DateUtils.addDays(endDate, 1), "yyyy-MM-dd")+"'");
//		}
		
		// 查询总数
		String sql = "SELECT count(DISTINCT aod.order_code) num FROM `fh_tgz_order_detail` aod,ordercenter.oc_orderinfo oi WHERE oi.order_code = aod.order_code and aod.tgz_member_code =:user_code";
		sql += StringUtils.join(whereList,"");
		Map<String, Object> totalMap = DbUp.upTable("fh_tgz_order_detail").dataSqlOne(sql, mQueryMap);
		int totalNum = NumberUtils.toInt(totalMap.get("num").toString(), 0);
		
		// 计算总页数
		int totalPage = totalNum/pageSize;
		if(totalNum % pageSize > 0) {
			totalPage++;
		}
		apiResult.setCountPage(totalPage);
		apiResult.setNowPage(nextPage);
		
		// 查询订单明细
		//sql = "SELECT DISTINCT aod.order_code,aod.tgz_type FROM `fh_tgz_order_detail` aod,ordercenter.oc_orderinfo oi WHERE oi.order_code = aod.order_code and aod.buyer_code =:user_code";
		sql = "SELECT aod.tgz_type,SUM(aod.tgz_money) AS tgz_money, aod.order_code FROM `fh_tgz_order_detail` aod,ordercenter.oc_orderinfo oi WHERE oi.order_code = aod.order_code AND aod.tgz_member_code = :user_code ";
		sql += StringUtils.join(whereList,"");
		sql += " GROUP BY aod.order_code";
		sql += " ORDER BY aod.create_time DESC LIMIT "+(nextPage-1)*pageSize+", "+pageSize;
		List<Map<String, Object>> orderCodeList = DbUp.upTable("fh_tgz_order_detail").dataSqlList(sql, mQueryMap);
		
		String orderCode,productCode;
		ApiForQueryHbAgentOrderInfo orderInfo;
		PlusModelProductInfo productInfo;
		PlusModelSkuQuery skuQuery = new PlusModelSkuQuery();
		PlusModelSkuInfo skuInfo;
		MDataMap orderMap,orderdetail;
		List<MDataMap> detailList;
		ApiSellerListResult apiSellerListResult;
		DecimalFormat df = new DecimalFormat("#.##");
		for(Map<String, Object> m : orderCodeList) {
			orderCode = m.get("order_code").toString();
			orderMap = DbUp.upTable("oc_orderinfo").oneWhere("create_time,order_status", "", "", "order_code",orderCode);
			orderInfo = new ApiForQueryHbAgentOrderInfo();
			orderInfo.setOrder_code(orderCode);
			orderInfo.setCreate_time(orderMap.get("create_time"));
			orderInfo.setOrder_status(orderMap.get("order_status"));
			orderInfo.setTgz_type(m.get("tgz_type").toString());
			orderInfo.setTgz_money(m.get("tgz_money").toString());
			//orderInfo.setPredict_money(getPredictMoney(getOauthInfo().getUserCode(), orderCode));
			
			BigDecimal dueMoney = BigDecimal.ZERO;
			BigDecimal totalProductNumber = BigDecimal.ZERO;
			
			detailList = DbUp.upTable("fh_tgz_order_detail").queryByWhere("order_code", orderCode);
			for(MDataMap detailMap : detailList) {
				orderdetail = DbUp.upTable("oc_orderdetail").one("order_code", orderCode, "sku_code", detailMap.get("sku_code"));
				productCode = orderdetail.get("product_code");
				skuQuery.setCode(detailMap.get("sku_code"));
				productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(productCode));
				skuInfo = loadSkuInfo.upInfoByCode(skuQuery);
				
				apiSellerListResult = new ApiSellerListResult();
				apiSellerListResult.setProduct_code(detailMap.get("product_code"));
				apiSellerListResult.setProduct_name(productInfo.getProductName());
				apiSellerListResult.setSku_code(detailMap.get("sku_code"));
				apiSellerListResult.setMainpic_url(getScalaImageUrl(productInfo.getMainpicUrl()));
				apiSellerListResult.setProduct_number(detailMap.get("sku_num"));
				apiSellerListResult.setSell_price(orderdetail.get("show_price"));
				apiSellerListResult.setStandardAndStyleList(getStandardAndStyleResult(skuInfo.getSkuKeyvalue()));
				//apiSellerListResult.setTgz_money(df.format(new BigDecimal(detailMap.get("tgz_money"))));
				orderInfo.getApiSellerList().add(apiSellerListResult);
				
				// 累加每个sku的实付款
				// 实付款 = 优惠后价格 * 数量  - 积分抵扣 - 储值金抵扣 - 暂存款抵扣
				dueMoney = dueMoney.add(
						new BigDecimal(orderdetail.get("sku_price")).multiply(new BigDecimal(orderdetail.get("sku_num")))
							.subtract(new BigDecimal(orderdetail.get("integral_money")))
							.subtract(new BigDecimal(orderdetail.get("czj_money")))
							.subtract(new BigDecimal(orderdetail.get("zck_money")))
							.subtract(new BigDecimal(orderdetail.get("hjycoin")))
						);
				
				totalProductNumber = totalProductNumber.add(new BigDecimal(apiSellerListResult.getProduct_number()));
			}
			
			if(dueMoney.compareTo(BigDecimal.ZERO) < 0) {
				dueMoney = BigDecimal.ZERO;
			}
			 if (new BigDecimal(dueMoney.intValue()).compareTo(dueMoney)==0){
	                //整数
				 orderInfo.setDue_money(dueMoney.setScale(0, BigDecimal.ROUND_DOWN).toString());
	            }else {
	            	orderInfo.setDue_money(dueMoney.toString());
	                //小数
	            }
			
			orderInfo.setTotal_product_number(totalProductNumber.intValue()+"");
			
			if(!orderInfo.getApiSellerList().isEmpty()) {
				apiResult.getSellerOrderList().add(orderInfo);
			}
		}
		
		return apiResult;
	}
	/**
	 * 查询预估金额
	 */
//	private String getPredictMoney(String userCode, String orderCode) {
//		String sql = "SELECT SUM(profit) profit FROM fh_agent_profit_detail WHERE member_code = :userCode AND order_code = :orderCode AND profit_type = '4497484600030001' AND profit > 0";
//		Map<String, Object> map = DbUp.upTable("fh_agent_profit_detail").dataSqlOne(sql, new MDataMap("userCode", userCode, "orderCode", orderCode));
//		if(map == null || map.get("profit") == null) return "0";
//		return map.get("profit").toString();
//	}
	
	private List<ApiSellerStandardAndStyleResult> getStandardAndStyleResult(String keyvalue) {
		List<PcPropertyinfoForFamily> standardAndStyleList = orderService.sellerStandardAndStyle(keyvalue); // 截取 尺码和款型
		
		List<ApiSellerStandardAndStyleResult> result = new ArrayList<ApiSellerStandardAndStyleResult>();
		if (standardAndStyleList != null && !"".equals(standardAndStyleList)) {
			for (PcPropertyinfoForFamily pcPropertyinfoForFamily : standardAndStyleList) {
				ApiSellerStandardAndStyleResult apiSellerStandardAndStyleResult = new ApiSellerStandardAndStyleResult();
				apiSellerStandardAndStyleResult.setStandardAndStyleKey(pcPropertyinfoForFamily.getPropertyKey());
				apiSellerStandardAndStyleResult.setStandardAndStyleValue(pcPropertyinfoForFamily.getPropertyValue());
				result.add(apiSellerStandardAndStyleResult);
			}
		}
		
		return result;
	}
	
	/**
	 *  压缩商品图片
	 */
	private String getScalaImageUrl(String url) {
		if(StringUtils.isBlank(url)) return "";
		Map<String, MFileItem> maps = plusVeryImage.upImageZoom(url, Constants.IMG_WIDTH_SP02);
		MFileItem item = maps.get(url);
		return item != null && StringUtils.isNotBlank(item.getFileUrl()) ? item.getFileUrl() : url;
	}
}
