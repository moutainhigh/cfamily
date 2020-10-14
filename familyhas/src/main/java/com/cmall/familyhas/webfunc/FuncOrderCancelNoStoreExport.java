package com.cmall.familyhas.webfunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webexport.ExportChart;
import com.srnpr.zapweb.webmodel.MPageData;

/**
 * 无库存取消订单导出
 */
public class FuncOrderCancelNoStoreExport extends ExportChart {

	@Override
	public void exportExcel(String sOperateId, HttpServletRequest request, HttpServletResponse response) {
		super.exportExcel(sOperateId, request, response);
		
		MPageData pageData = getPageData();
		int orderCodeIndex = pageData.getPageField().indexOf("order_code");
		
		MPageData newPageData = new MPageData();
		newPageData.getPageHead().add("订单号");
		newPageData.getPageHead().add("取消时间");
		newPageData.getPageHead().add("下单时间");
		newPageData.getPageHead().add("商户编号");
		newPageData.getPageHead().add("商户类型");
		newPageData.getPageHead().add("结算周期");
		newPageData.getPageHead().add("商户名称");
		newPageData.getPageHead().add("商品编号");
		newPageData.getPageHead().add("SKU编号");
		newPageData.getPageHead().add("SKU名称");
		newPageData.getPageHead().add("大分类");
		newPageData.getPageHead().add("中分类");
		newPageData.getPageHead().add("商品数量");
		newPageData.getPageHead().add("订单成本");
		newPageData.getPageHead().add("订单金额");
		newPageData.getPageHead().add("使用优惠金额");
		newPageData.getPageHead().add("质量管理规定");
		newPageData.getPageHead().add("罚款金额");
		
		if(pageData.getPageData() == null
				|| orderCodeIndex < 0) {
			return;
		}
		
		newPageData.setPageData(new ArrayList<List<String>>());
		
		String sSql = "SELECT o.create_time, d.product_code, d.sku_code, d.sku_name, d.sku_num, d.sku_num*d.cost_price cost_money, d.sku_num*d.sku_price product_money, d.sku_num*d.coupon_price coupon_money FROM `oc_orderdetail` d,oc_orderinfo o WHERE o.order_code = d.order_code AND d.order_code = :order_code GROUP BY d.sku_code";
		
		// 商户类型
		Map<String,String> sellerTypeMap = getDefineNameMap("449747810005");
		// 结算周期
		Map<String,String> accountTypeMap = getDefineNameMap("449747810003");
		
		String orderCode = "";
		List<Map<String,Object>> skuDetailList;
		Map<String,String> catMap;
		for(List<String> dataList : pageData.getPageData()) {
			orderCode = dataList.get(orderCodeIndex);
			
			Map<String,Object> orderTime = getOrderTime(orderCode);
			
			String smallSellerCode = (String)orderTime.get("small_seller_code");
			
			Map<String,Object> sellerInfo = getSellerInfo(smallSellerCode);
			
			skuDetailList = DbUp.upTable("oc_orderdetail").dataSqlList(sSql, new MDataMap("order_code", orderCode));
			for(Map<String,Object> map : skuDetailList) {
				String productCode = (String)map.get("product_code");
				catMap = getCategoryInfo(productCode);
				
				newPageData.getPageData().add(Arrays.asList(
						orderCode,
						orderTime.get("cancel_time").toString(),
						orderTime.get("order_time").toString(),
						smallSellerCode,
						sellerTypeMap.get(sellerInfo.get("uc_seller_type")),
						accountTypeMap.get(sellerInfo.get("account_clear_type")),
						sellerInfo.get("seller_company_name").toString(),
						map.get("product_code").toString(),
						map.get("sku_code").toString(),
						map.get("sku_name").toString(),
						catMap.get("lv1"),
						catMap.get("lv2"),
						map.get("sku_num").toString(),
						map.get("cost_money").toString(),
						map.get("product_money").toString(),
						map.get("coupon_money").toString(),
						"",
						""
						));
			}
		}
		
		setPageData(newPageData);
	}
	
	// 查询订单下单时间和取消时间
	private Map<String,Object> getOrderTime(String orderCode) {
		String sql = "SELECT o.create_time order_time,s.create_time cancel_time,o.small_seller_code FROM ordercenter.oc_orderinfo o,logcenter.lc_orderstatus s WHERE o.order_code = s.`code` and o.order_code = :order_code and s.now_status = '4497153900010006' LIMIT 1";
		return DbUp.upTable("oc_orderinfo").dataSqlOne(sql, new MDataMap("order_code", orderCode));
	}
	
	// 查询商户信息
	private Map<String,Object> getSellerInfo(String smallSellerCode) {
		String sql = "SELECT e.small_seller_code,e.uc_seller_type,e.seller_company_name,e.account_clear_type FROM `uc_seller_info_extend` e WHERE e.small_seller_code = :smallSellerCode";
		return DbUp.upTable("uc_seller_info_extend").dataSqlOne(sql, new MDataMap("smallSellerCode", smallSellerCode));
	}
	
	// 查询字典值
	private Map<String,String> getDefineNameMap(String parentCode) {
		Map<String,String> dm = new HashMap<String, String>();
		List<MDataMap> list = DbUp.upTable("sc_define").queryByWhere("parent_code", parentCode);
		for(MDataMap m : list) {
			dm.put(m.get("define_code"), m.get("define_name"));
		}
		return dm;
	}
	
	// 查询分类信息
	private Map<String,String> getCategoryInfo(String productCode) {
		Map<String,String> catMap = new HashMap<String, String>();
		MDataMap map = DbUp.upTable("uc_sellercategory_product_relation").one("product_code", productCode, "seller_code", "SI2003");
		if(map == null) {
			return map;
		}
		
		String code = map.get("category_code");
		String catCodeLv1 = code.substring(0,12);
		String catCodeLv2 = code.substring(0,16);
		
		catMap.put("lv1", (String)DbUp.upTable("uc_sellercategory").dataGet("category_name", "seller_code = 'SI2003' AND level = '2' AND category_code = :code", new MDataMap("code", catCodeLv1)));
		catMap.put("lv2", (String)DbUp.upTable("uc_sellercategory").dataGet("category_name", "seller_code = 'SI2003' AND level = '3' AND category_code = :code", new MDataMap("code", catCodeLv2)));
		return catMap;
	}
	
}
