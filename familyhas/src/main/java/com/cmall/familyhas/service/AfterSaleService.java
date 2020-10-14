package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.util.HttpUtil;
import com.cmall.groupcenter.homehas.RsyncGetThirdOrderDetail;
import com.cmall.systemcenter.util.AppVersionUtils;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.support.PlusSupportLD;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 售后服务，退货，换货
 * @author cc
 *
 */
public class AfterSaleService extends BaseClass {
	
	/**
	 * 
	 * @param orderCode 订单号（APP订单号或LD订单号）
	 * @param skuCode 商品skuCode （如果为LD订单，可为空）
	 * @param ordSeq 订单序号 （LD订单入参，可为空） 
	 * @return
	 */
	public boolean checkIfAllowAfterSale(String orderCode, String skuCode,
			String ordSeq) {
		if(orderCode.contains("DD")||orderCode.contains("HH")){//APP下单 徐判定小商家编号SI2003
			MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
			if(orderInfo== null || orderInfo.isEmpty()){
				//订单不存在
				return false;
			}
			String small_seller_code = orderInfo.get("small_seller_code");
			String out_order_code = orderInfo.get("out_order_code");
			if("SI2003".equals(small_seller_code)){
				// 未签收不允许申请售后
				if(!"4497153900010005".equals(orderInfo.get("order_status")) && !"4497153900010003".equals(orderInfo.get("order_status"))) {
					return false;
				}
				//LD 品，需要去查询可允许售后数量
				Integer countInterface = this.getAllowCount(orderCode, skuCode);//接口返回可允许售后数量
				int count=0;
				String sql = "select SUM(good_cnt) good_cnt FROM ordercenter.oc_after_sale_ld WHERE order_code = '"+out_order_code+"' AND after_sale_status = '01' AND if_post = 2";
				Map<String,Object> mapCount = DbUp.upTable("oc_after_sale_ld").dataSqlOne(sql, null);
				if(mapCount != null){
					String countLocal = mapCount.get("good_cnt")!=null?mapCount.get("good_cnt").toString():"0";
					count += Integer.parseInt(countLocal);
				}
				if((countInterface - count) < 1){
					return false;
				}
				return true;
			}else{
				//APP 自营品。直接去售后表查询是否有可允许售后数量 需要先查询订单状态。为 已完成才可允许申请售后
				String order_status = orderInfo.get("order_status");
				//APP自营品如果订单状态不为已完成，则不允许申请售后
				// 544版本放开未签收的也可以申请售后
				if(!order_status.equals("4497153900010005") && !order_status.equals("4497153900010003")){
					return false;
				}
				
				// 京东商品未签收前不支持申请售后
				if(Constants.SMALL_SELLER_CODE_JD.equals(small_seller_code) && order_status.equals("4497153900010003")) {
					return false;
				}
				
				// 网易考拉商品未签收前不支持申请售后
				if("SF03WYKLPT".equals(small_seller_code) && order_status.equals("4497153900010003")) {
					return false;
				}
				
				MDataMap orderDetail = DbUp.upTable("oc_orderdetail").one("order_code",orderCode,"sku_code",skuCode);
				if(orderDetail == null || orderDetail.isEmpty()){
					return false;
				}
				Integer totalCount = Integer.parseInt(orderDetail.get("sku_num"));
				int count=0;
				Map<String, Object> map1=DbUp.upTable("oc_order_achange").dataSqlOne("select sum(oac_num) count from oc_order_achange where order_code=:order_code and sku_code=:sku_code and available=:available", new MDataMap("order_code",orderCode,"sku_code",skuCode,"available","0"));
				if(map1!=null&&!map1.isEmpty()){
					Object obj=map1.get("count");
					if(obj!=null){
						count=((BigDecimal) obj).intValue();
					}
				}
				Integer count2 = totalCount - count;
				if(count2<1){
					return false;
				}
				return true;
			}
		}else{
			//LD 订单 根据接口查询允许可售后数量
			if(StringUtils.isEmpty(ordSeq)){
				return false;
			}
			Integer totalCount = this.getAllowCount(orderCode, Integer.parseInt(ordSeq));
			int count=0;
			String sql = "select SUM(good_cnt) good_cnt FROM ordercenter.oc_after_sale_ld WHERE order_code = '"+orderCode+"' AND order_seq = "+ordSeq+" AND after_sale_status = '01' AND if_post = 2";
			Map<String,Object> mapCount = DbUp.upTable("oc_after_sale_ld").dataSqlOne(sql, null);
			if(mapCount != null){
				String countLocal = mapCount.get("good_cnt")!=null?mapCount.get("good_cnt").toString():"0";
				count += Integer.parseInt(countLocal);
			}
			Integer allowCount = totalCount - count;
			if(allowCount < 1){
				return false;
			}
			return true;
		}
	}
	
	/**
	 * 获取颜色款式获取LD允许售后数量
	 * @param order_code
	 * @param sku_code
	 * @return
	 */
	public Integer getAllowCount(String order_code, String sku_code) {
		PlusSupportLD ld = new PlusSupportLD();
		String isSyncLd = ld.upSyncLdOrder();
		if("N".equals(isSyncLd)){//关闭状态
			return 0;
		}
		MDataMap mapOrder = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
		MDataMap goodOrder = DbUp.upTable("pc_skuinfo").one("sku_code",sku_code);
		Integer ordId = 0,goodId=0,colorId=0,styleId=0;
		if(mapOrder != null && !mapOrder.isEmpty()){
			String out_order_code = mapOrder.get("out_order_code");
			if(!StringUtils.isEmpty(out_order_code)){
				ordId = Integer.parseInt(out_order_code);
			}
		}
		if(goodOrder != null && !goodOrder.isEmpty()){
			String product_code = goodOrder.get("product_code");
			if(!StringUtils.isEmpty(product_code)){
				goodId = Integer.parseInt(product_code);
			}
			String sku_key = goodOrder.get("sku_key");
			colorId = Integer.parseInt(sku_key.split("&")[0].replace("color_id=", ""));
			styleId = Integer.parseInt(sku_key.split("&")[1].replace("style_id=", ""));
		}
		if(ordId == 0){
			return 0;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ordId", ordId);
		params.put("goodId", goodId);
		params.put("colorId", colorId);
		params.put("styleId", styleId);
		String result = HttpUtil.post(bConfig("groupcenter.rsync_homehas_url")+"getCancelGoodCnt", JSONObject.toJSONString(params), "UTF-8");
		JSONObject jo = JSONObject.parseObject(result);
		Integer skuCount = 0;
		if(jo != null && jo.getInteger("code") == 0){
			skuCount = jo.getInteger("result");
		}
		return skuCount;
	}
	
	/**
	 * 获取订单序号获取LD允许售后数量
	 * @param order_code
	 * @param ordSeq
	 * @return
	 */
	public Integer getAllowCount(String order_code, Integer ord_seq) {
		PlusSupportLD ld = new PlusSupportLD();
		String isSyncLd = ld.upSyncLdOrder();
		if("N".equals(isSyncLd)){//关闭状态
			return 0;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isEmpty(order_code)){
			return 0;
		}
		if(ord_seq == 0){
			return 0;
		}
		params.put("ordId", Integer.parseInt(order_code));
		params.put("ordSeq", ord_seq);
		String result = HttpUtil.post(bConfig("groupcenter.rsync_homehas_url")+"getCancelGoodCnt", JSONObject.toJSONString(params), "UTF-8");
		JSONObject jo = JSONObject.parseObject(result);
		Integer skuCount = 0;
		if(jo != null && jo.getInteger("code") == 0){
			skuCount = jo.getInteger("result");
		}
		return skuCount;
	}

	/**
	 * 根据订单号，商品编号查询可允许申请数量。APP
	 * @param orderCode
	 * @param skuCode
	 * @return
	 */
	public Integer getAllowForReturn(String orderCode, String skuCode) {
		MDataMap odetail = DbUp.upTable("oc_orderdetail").one("order_code", orderCode, "sku_code", skuCode);
		if(odetail == null || odetail.isEmpty()){
			return 0;
		}
		int count=0;
		Map<String, Object> map1=DbUp.upTable("oc_order_achange").dataSqlOne("select sum(oac_num) count from oc_order_achange where order_code=:order_code and sku_code=:sku_code and available=:available", new MDataMap("order_code",orderCode,"sku_code",skuCode,"available","0"));
		if(map1!=null&&!map1.isEmpty()){
			Object obj=map1.get("count");
			if(obj!=null){
				count=((BigDecimal) obj).intValue();
			}
		}
		return Integer.valueOf(odetail.get("sku_num"))-count;
	}

	/**
	 * 判断订单是否为换货新单
	 * @param orderCode
	 * @return
	 */
	public static boolean isChangeGoodsOrder(String order_code, String is_chg) {		
		if(order_code == null || "".equals(order_code)) {
			return false;
		}
		//换货新单是HH打头订单或者LD订单
		if(order_code.contains("HH")){//APP订单
			String sql = "select order_code from oc_orderinfo where order_code=:order_code and (org_ord_id <> null or org_ord_id <> '')";
			Map<String, Object> map = DbUp.upTable("oc_orderinfo").dataSqlOne(sql, new MDataMap("order_code",order_code));
			if(map != null && map.get("order_code") != null) {
				return true;
			}
		} else {
			if("Y".equals(is_chg)) {//是否换货新单
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 对比版本
	 * appVersion > compareVersion   返回正数
	 * appVersion = compareVersion   返回0
	 * appVersion < compareVersion   返回负数
	 * @param appVersion
	 * @param compareVersion
	 */
	public static Integer compareAppVersion ( String appVersion ,String compareVersion) {
		if(StringUtils.isBlank(appVersion)) {
			appVersion = "";
		}
		if(StringUtils.isBlank(compareVersion)) {
			compareVersion = "";
		}
		return AppVersionUtils.compareTo(appVersion, compareVersion);
	}
	
	/**
	 * 根据订单号查询LD订单是否可售
	 * @param orderCode
	 * @return true 允许申请售后，false 不允许申请售后
	 */
	public boolean checkAfterSaleAllow(String orderCode) {
		RsyncGetThirdOrderDetail rsyncGetThirdOrderDetail = new RsyncGetThirdOrderDetail();
		rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_id(orderCode);
		rsyncGetThirdOrderDetail.upRsyncRequest().setOrd_seq("");
		rsyncGetThirdOrderDetail.doRsync();
		AfterSaleService as  = new AfterSaleService(); 
		Integer ordSeq = 0;
		if(rsyncGetThirdOrderDetail.getResponseObject() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult() != null && rsyncGetThirdOrderDetail.getResponseObject().getResult().size() > 0 ) {
			ordSeq = rsyncGetThirdOrderDetail.getResponseObject().getResult().size();
		}
		for(Integer i = 1;i <= ordSeq;i++) {
			boolean flag = as.checkIfAllowAfterSale(orderCode,"",i.toString());
			if(flag) {
				return flag;
			}
		}
		return false;
	}
}
