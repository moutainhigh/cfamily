package com.cmall.familyhas.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;

import com.cmall.groupcenter.homehas.RsyncModRcvAddress;
import com.cmall.systemcenter.service.ChinaAreaService;
import com.srnpr.xmasorder.service.TeslaFreight;
import com.srnpr.xmassystem.Constants;
import com.srnpr.xmassystem.helper.PlusHelperNotice;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadTemplateAreaCode;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAeraCode;
import com.srnpr.xmassystem.modelproduct.PlusModelTemplateAreaQuery;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.usermodel.MUserInfo;
import com.srnpr.zapweb.webfactory.UserFactory;

public class OrderEditService {

	static LoadProductInfo loadProductInfo = new LoadProductInfo();
	
	/**
	 * 检查订单是否可以修改收货地址
	 * @param orderCode
	 * @return
	 */
	public RootResult checkOrderEditLimit(String orderCode) {
		RootResult res = new RootResult();
		
		MDataMap orderMap = DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
		if("SF03WYKLPT".equalsIgnoreCase(orderMap.get("small_seller_code"))) {
			res.setResultCode(0);
			res.setResultMessage("考拉商品不支持修改，请取消订单后重新下单");
			return res;
		}
		
		if("SF031JDSC".equalsIgnoreCase(orderMap.get("small_seller_code"))) {
			res.setResultCode(0);
			res.setResultMessage("京东商品不支持修改，请联系京东客服取消订单后重新下单");
			return res;
		}
			
		if("449715200007".equals(orderMap.get("order_type"))) {
			res.setResultCode(0);
			res.setResultMessage("内购订单不支持修改");
			return res;
		}
		
		if(!"4497153900010002".equals(orderMap.get("order_status"))) {
			res.setResultCode(0);
			res.setResultMessage("仅支持未发货状态订单修改");
			return res;
		}
		
		if(DbUp.upTable("lc_order_export").count("order_code", orderCode) > 0) {
			res.setResultCode(0);
			res.setResultMessage("商户已经导出了发货订单，请联系商户修改");
			return res;
		}
		
		return res;
	}
	
	/**
	 * 检查订单是否可以修改SKU
	 * @param orderCode
	 * @return
	 */
	public RootResult checkOrderSkuEditLimit(String orderCode, String skuCode) {
		RootResult res = checkOrderEditLimit(orderCode);
		if(res.getResultCode() != 1) {
			return res;
		}
		
		if(orderCode.startsWith("HH")) {
			res.setResultCode(0);
			res.setResultMessage("仅支持从惠家有下的订单");
			return res;
		}
		
		MDataMap detail = DbUp.upTable("oc_orderdetail").one("order_code",orderCode,"sku_code", skuCode);
		if(!"1".equals(detail.get("gift_flag"))) {
			res.setResultCode(0);
			res.setResultMessage("赠品不支持修改");
			return res;
		}
		
		List<MDataMap> skuList = getOtherSkuList(orderCode, skuCode);
		
		if(skuList.isEmpty()) {
			res.setResultCode(0);
			res.setResultMessage("无可选规格属性");
		}
		
		return res;
	}
	
	/**
	 * 检查是否显示修改身份证号的字段
	 * @param orderCode
	 * @return
	 */
	public boolean checkShowIdcardNumber(String orderCode) {
		String smallSellerCode = (String)DbUp.upTable("oc_orderinfo").dataGet("small_seller_code", "", new MDataMap("order_code",orderCode));
		// 海外购商户显示身份证号
		if(DbUp.upTable("uc_seller_info_extend").dataCount("small_seller_code = :small_seller_code and uc_seller_type in('4497478100050002','4497478100050003')", new MDataMap("small_seller_code",smallSellerCode)) > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 查询可以变更的规格属性
	 * @param orderCode
	 * @param skuCode
	 * @return
	 */
	public List<MDataMap> getOtherSkuList(String orderCode, String skuCode) {
		List<String> orderSkuList = new ArrayList<String>();
		List<MDataMap> detailMapList = DbUp.upTable("oc_orderdetail").queryAll("sku_code", "", "", new MDataMap("order_code", orderCode));
		for(MDataMap map : detailMapList) {
			orderSkuList.add(map.get("sku_code"));
		}
		
		MDataMap skuMap = DbUp.upTable("pc_skuinfo").one("sku_code",skuCode);
		
		// 只查询相同成本和售价的SKU
		MDataMap queryMap = new MDataMap("product_code", skuMap.get("product_code"),"sell_price","cost_price","sale_yn","Y");
		queryMap.put("product_code", skuMap.get("product_code"));
		queryMap.put("sell_price", skuMap.get("sell_price"));
		queryMap.put("cost_price", skuMap.get("cost_price"));
		queryMap.put("sku_code", skuCode);
		
		List<MDataMap> skuList = DbUp.upTable("pc_skuinfo").queryAll("sku_code,sku_keyvalue", "", "sale_yn = 'Y' AND product_code = :product_code AND sku_code != :sku_code AND sell_price = :sell_price AND cost_price = :cost_price", queryMap);
		
		// 需要排除订单里面已经存在的SKU，同一个订单里面不能包含两个相同SKU
		Iterator<MDataMap> iter = skuList.iterator();
		MDataMap map;
		while(iter.hasNext()) {
			map = iter.next();
			if(orderSkuList.contains(map.get("sku_code"))) {
				iter.remove();
			}
		}
		
		return skuList;
	}
	
	public RootResult updateOrderAddress(MDataMap newAddress) {
		RootResult res = new RootResult();
		if(StringUtils.isBlank(newAddress.get("mobilephone"))
				|| StringUtils.isBlank(newAddress.get("address"))
				|| StringUtils.isBlank(newAddress.get("receive_person"))
				|| StringUtils.isBlank(newAddress.get("area_code"))) {
			res.setResultCode(0);
			res.setResultMessage("要更新的字段不能为空");
			return res;
		}
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",newAddress.get("order_code"));
		
		List<MDataMap> produtMapList = DbUp.upTable("oc_orderdetail").queryAll("product_code,sku_num", "", "", new MDataMap("order_code",newAddress.get("order_code"),"gift_flag","1"));
		// 取完整4级编码
		Map<String, String> fullAreaCodeMap = new ChinaAreaService().getFullCode(newAddress.get("area_code"));
		
		if(!checkProductAreaLimit(fullAreaCodeMap.get("lv2"), produtMapList)) {
			res.setResultCode(0);
			res.setResultMessage("订单中有商品不支持此地址配送");
			return res;
		}
		
		if(!checkProductTransportLimit(newAddress.get("area_code"), produtMapList, new BigDecimal(orderInfo.get("transport_money")))) {
			res.setResultCode(0);
			res.setResultMessage("新收货地址商品运费超过原地址运费");
			return res;
		}
		
		if(!checkProductContrabandLimit(fullAreaCodeMap, produtMapList)) {
			res.setResultCode(0);
			res.setResultMessage("订单中有违禁品不支持此地址配送");
			return res;
		}
		
		// 如果是LD订单且已经下单到LD系统则需要把修改同步到LD
		if("SI2003".equals(orderInfo.get("small_seller_code")) && StringUtils.isNotBlank(orderInfo.get("out_order_code"))) {
			// 加锁防止下单定时同时执行
			String lockKey = KvHelper.lockCodes(20, Constants.LOCK_ORDER_UPDATE + newAddress.get("order_code"));
			if(StringUtils.isBlank(lockKey)) {
				res.setResultCode(0);
				res.setResultMessage("订单正在同步中请稍后重试");
				return res;
			}
			try {
				RsyncModRcvAddress rsyncObj = new RsyncModRcvAddress();
				rsyncObj.upRsyncRequest().setMobile(newAddress.get("mobilephone"));
				rsyncObj.upRsyncRequest().setRcver_nm(newAddress.get("receive_person"));
				rsyncObj.upRsyncRequest().setSrgn_cd(newAddress.get("area_code"));
				rsyncObj.upRsyncRequest().setSend_addr(newAddress.get("address"));
				rsyncObj.upRsyncRequest().setOrd_id(orderInfo.get("out_order_code"));
				rsyncObj.doRsync();
				RsyncModRcvAddress.RsyncResponse resp = rsyncObj.upProcessResult();
				
				if(!"true".equals(resp.getSuccess())) {
					res.setResultCode(0);
					res.setResultMessage(resp.getMessage());
					return res;
				}
			} catch (Exception e) {
				e.printStackTrace();
				res.setResultCode(0);
				res.setResultMessage("调用LD系统接口失败: "+e);
				return res;
			} finally {
				KvHelper.unLockCodes(lockKey, Constants.LOCK_ORDER_UPDATE + newAddress.get("order_code"));
			}
		}
		
		// 先查询原收货地址
		MDataMap adrMap = DbUp.upTable("oc_orderadress").one("order_code", newAddress.get("order_code"));
		// 再更新收货地址
		if(newAddress.containsKey("auth_idcard_number")) {
			DbUp.upTable("oc_orderadress").dataUpdate(newAddress, "mobilephone,address,receive_person,area_code,auth_idcard_number", "order_code");
		} else {
			DbUp.upTable("oc_orderadress").dataUpdate(newAddress, "mobilephone,address,receive_person,area_code", "order_code");
		}

		// 记录日志
		MUserInfo userInfo = UserFactory.INSTANCE.create();
		MDataMap logMap = new MDataMap();
		logMap.put("order_code", newAddress.get("order_code"));
		logMap.put("address_before", new JSONObject(adrMap).toString());
		logMap.put("address_after", new JSONObject(newAddress).toString());
		logMap.put("create_time", FormatHelper.upDateTime());
		logMap.put("create_user", userInfo == null ? "" : userInfo.getLoginName());
		DbUp.upTable("lc_order_adress_change_log").dataInsert(logMap);
		
		return res;
	}
	
	/**
	 * 检查商品是否支持此区域下单
	 * @return true 支持
	 */
	public boolean checkProductAreaLimit(String cityCode, List<MDataMap> produtMapList) {
		PlusModelProductInfo productInfo;
		PlusModelTemplateAeraCode tempAreaCode;
		for(MDataMap m : produtMapList) {
			productInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(m.get("product_code")));
			if(StringUtils.isBlank(productInfo.getAreaTemplate())) {
				continue;
			}
			
			PlusModelTemplateAreaQuery query = new PlusModelTemplateAreaQuery();
			query.setCode(productInfo.getAreaTemplate());
			tempAreaCode = new LoadTemplateAreaCode().upInfoByCode(query);
			
			if(tempAreaCode.getAreaCodes().isEmpty()) {
				continue;
			}
			
			// 有商品不支持此地址则返回false
			// 区域限制是判断的二级地址
			if(!tempAreaCode.getAreaCodes().contains(cityCode)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查商品的运费是否一致
	 * @return true 运费一致
	 */
	public boolean checkProductTransportLimit(String areaCode, List<MDataMap> produtMapList, BigDecimal orderTransportMoney) {
		Map<String,	Integer> productNumMap = new HashMap<String, Integer>();
		// 按商品汇总数量
		for(MDataMap m : produtMapList) {
			String productCode = m.get("product_code");
			int skuNum = NumberUtils.toInt(m.get("sku_num"));
			int num = NumberUtils.toInt(productNumMap.get(productCode)+"");
			productNumMap.put(productCode, num + skuNum);
		}
		
		TeslaFreight teslaFreight = new TeslaFreight();
		//计算运费，子单中，所有的商品计算一次运费；子单运费 取 单下商品运费最高的一个
		BigDecimal freightMoneyMax = BigDecimal.ZERO;
		for (Map.Entry<String, Integer> freightMap : productNumMap.entrySet()) {
			String productCode = freightMap.getKey();
			int num = freightMap.getValue();
			
			//商品运费  取一个最大的值作为运费
			BigDecimal freightMoney = teslaFreight.getFreightMoney(productCode, num, areaCode);

			if (freightMoneyMax.compareTo(freightMoney) < 0) {
				freightMoneyMax = freightMoney;
			}
		}
		
		// 如果商品运费高于原地址则返回false
		if(freightMoneyMax.compareTo(orderTransportMoney) > 0) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 检查商品违禁品限制是否支持下单
	 * @return true 支持下单
	 */
	public boolean checkProductContrabandLimit(Map<String, String> fullAreaCodeMap, List<MDataMap> produtMapList) {
		for(MDataMap map : produtMapList) {
			if(!checkProductContrabandLimit(map.get("product_code"), fullAreaCodeMap.get("lv1"), fullAreaCodeMap.get("lv2"), fullAreaCodeMap.get("lv3"))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 检查商品违禁品限制是否支持下单
	 * @param productCode
	 * @param lv1Code
	 * @param lv2Code
	 * @param lv3Code
	 * @return
	 */
	public boolean checkProductContrabandLimit(String productCode,String lv1Code, String lv2Code, String lv3Code) {
		MDataMap mProductExtMap = DbUp.upTable("pc_productinfo_ext").one("product_code", productCode, "check_danger", "Y");
		// 非违禁品商品直接返回
		if(mProductExtMap == null) {
			return true;
		}
		
		String is_danger = "";
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String sql = "select danger_type,toplimit from pc_product_contraband where ((lrgn_cd =:lAreaCode and mrgn_cd = '000000') or (mrgn_cd =:mAreaCode and srgn_cd = '000000') or srgn_cd =:area_code) and danger_type =:is_danger and vl_yn='Y'";
		
		BigDecimal wd = new BigDecimal(mProductExtMap.get("wd").toString()); //长
		BigDecimal dp = new BigDecimal(mProductExtMap.get("dp").toString()); //宽
		BigDecimal hg = new BigDecimal(mProductExtMap.get("hg").toString()); //高
		BigDecimal wg = new BigDecimal(mProductExtMap.get("wg").toString()); //重量
		String is_unpack = mProductExtMap.get("is_unpack").toString();//是否为拆包件
		//处理长宽高
		if(wd.compareTo(BigDecimal.ZERO) > 0 || dp.compareTo(BigDecimal.ZERO) > 0 || hg.compareTo(BigDecimal.ZERO) > 0) {
			is_danger = "E";
			list = DbUp.upTable("pc_product_contraband").dataSqlList(sql, new MDataMap("lAreaCode",lv1Code,"mAreaCode",lv2Code,"area_code",lv3Code,"is_danger",is_danger));						
			if(list != null && list.size() > 0) {
				map = list.get(0);
				BigDecimal toplimit = new BigDecimal(map.get("toplimit").toString());						
				if(toplimit.compareTo(wd) <= 0 || toplimit.compareTo(dp) <= 0 || toplimit.compareTo(hg) <= 0) {
					//有违禁品，不准下单
					//contrabandStr = "商品长宽高超过限制，商品长：" + wd.toString() + ",宽：" + dp.toString() + ",高：" + hg.toString() + ",限制的最大边距为：" + toplimit.toString();
					return false;
				}
			}						
		} 
		//处理重量
		if(wg.compareTo(BigDecimal.ZERO) > 0) {
			is_danger = "D";
			list = DbUp.upTable("pc_product_contraband").dataSqlList(sql, new MDataMap("lAreaCode",lv1Code,"mAreaCode",lv2Code,"area_code",lv3Code,"is_danger",is_danger));
			if(list != null && list.size() > 0) {
				map = list.get(0);
				BigDecimal toplimit = new BigDecimal(map.get("toplimit").toString());						
				if(toplimit.compareTo(wg) <= 0) {
					//有违禁品，不准下单
					//contrabandStr = "商品重量超过限制，商品重：" + wg.toString() + ",限制的最大重量为：" + toplimit.toString();
					return false;
				}
			}
		}
		//处理拆包件
		if("Y".equals(is_unpack)) {
			is_danger = "F";
			list = DbUp.upTable("pc_product_contraband").dataSqlList(sql, new MDataMap("lAreaCode",lv1Code,"mAreaCode",lv2Code,"area_code",lv3Code,"is_danger",is_danger));
			if(list != null && list.size() > 0) {
				//有违禁品，不准下单
				//contrabandStr = "商品为拆包件,该地区拆包件为违禁品";
				return false;
			}
		}
		is_danger = mProductExtMap.get("is_danger");//不包含D、E、F
		list = DbUp.upTable("pc_product_contraband").dataSqlList(sql, new MDataMap("lAreaCode",lv1Code,"mAreaCode",lv2Code,"area_code",lv3Code,"is_danger",is_danger));														
		if(list != null && list.size() > 0) {
			//匹配违禁品属性+地区编号(A：酒水 B：粉末 C：易燃易爆 Y：刀具)
			if("A".equals(is_danger) || "B".equals(is_danger) || "C".equals(is_danger) || "Y".equals(is_danger)) {
				/**
				 * 1.商品违禁品属性+收货地址 匹配 违禁品配置表 ABCY 
				 */						
				//有违禁品，不准下单
				//contrabandStr = "商品属于" + is_danger + "类物品,在该地区为违禁品";
				return false;
			} 
		}
		
		return true;
	}
	
	public RootResult updateOrderSku(String orderCode, String oldSkuCode, String newSkuCode) {
		RootResult mResult = new RootResult();
		
		MDataMap oldSkuDetail = DbUp.upTable("oc_orderdetail").one("order_code",orderCode,"sku_code",oldSkuCode);
		MDataMap oldSkuInfo = DbUp.upTable("pc_skuinfo").one("sku_code", oldSkuCode);
		MDataMap newSkuInfo = DbUp.upTable("pc_skuinfo").one("sku_code", newSkuCode);
		int skuNum = NumberUtils.toInt(oldSkuDetail.get("sku_num"));
		
		// 检查SKU是否属于同一个商品
		if(!newSkuInfo.get("product_code").equals(oldSkuDetail.get("product_code"))) {
			mResult.setResultCode(0);
			mResult.setResultMessage("新规格跟原规格不属于同一个商品");
			return mResult;
		}
		
		// 检查库存
		PlusSupportStock supportStock = new PlusSupportStock();
		if(supportStock.upAllStock(newSkuCode) < skuNum) {
			mResult.setResultCode(0);
			mResult.setResultMessage("新规格库存不足: "+supportStock.upAllStock(newSkuCode) + " < " +skuNum);
			return mResult;
		}
		
		MDataMap orderInfo = DbUp.upTable("oc_orderinfo").one("order_code",orderCode);
		// 如果是LD订单且已经下单到LD系统则需要把修改同步到LD
		if("SI2003".equals(orderInfo.get("small_seller_code")) && StringUtils.isNotBlank(orderInfo.get("out_order_code"))) {
			// 加锁防止下单定时同时执行
			String lockKey = KvHelper.lockCodes(20, Constants.LOCK_ORDER_UPDATE + orderCode);
			if(StringUtils.isBlank(lockKey)) {
				mResult.setResultCode(0);
				mResult.setResultMessage("订单正在同步中请稍后重试");
				return mResult;
			}
			try {
				String[] oldColorStyle = getColorStyle(oldSkuInfo.get("sku_key"));
				String[] newColorStyle = getColorStyle(newSkuInfo.get("sku_key"));
				
				RsyncModRcvAddress.GoodInfo goodInfo = new RsyncModRcvAddress.GoodInfo();
				goodInfo.setGood_id(oldSkuInfo.get("product_code"));
				goodInfo.setColor_id_old(oldColorStyle[0]);
				goodInfo.setStyle_id_old(oldColorStyle[1]);
				goodInfo.setColor_id(newColorStyle[0]);
				goodInfo.setStyle_id(newColorStyle[1]);
				
				RsyncModRcvAddress rsyncObj = new RsyncModRcvAddress();
				rsyncObj.upRsyncRequest().setGood_info(Arrays.asList(goodInfo));
				rsyncObj.upRsyncRequest().setOrd_id(orderInfo.get("out_order_code"));
				rsyncObj.doRsync();
				RsyncModRcvAddress.RsyncResponse resp = rsyncObj.upProcessResult();
				
				if(!"true".equals(resp.getSuccess())) {
					mResult.setResultCode(0);
					mResult.setResultMessage(resp.getMessage());
					return mResult;
				}
			} catch (Exception e) {
				e.printStackTrace();
				mResult.setResultCode(0);
				mResult.setResultMessage("调用LD系统接口失败: "+e);
				return mResult;
			} finally {
				KvHelper.unLockCodes(lockKey, Constants.LOCK_ORDER_UPDATE + orderCode);
			}
		}
		
		// 更新订单明细表
		oldSkuDetail.put("sku_code", newSkuCode);
		oldSkuDetail.put("sku_name", newSkuInfo.get("sku_name"));
		DbUp.upTable("oc_orderdetail").dataUpdate(oldSkuDetail, "sku_code,sku_name", "zid");
		
		// 更新活动明细表
		String updateSql = "UPDATE oc_order_activity SET sku_code = :newSkuCode WHERE order_code = :orderCode AND sku_code = :oldSkuCode";
		DbUp.upTable("oc_order_activity").dataExec(updateSql, new MDataMap("orderCode",orderCode,"oldSkuCode", oldSkuCode,"newSkuCode",newSkuCode));
		
		// 还原老SKU的库存，减少新SKU的库存
		PlusHelperNotice.onCancelIcOrder(orderCode, orderInfo.get("buyer_code"), oldSkuCode);
		supportStock.skuStockForCancelOrder(orderCode, newSkuCode, 0 - skuNum);
		supportStock.subtractSkuStock(orderCode, oldSkuCode, skuNum);
		
		// 记录变更日志表
		MUserInfo userInfo = UserFactory.INSTANCE.create();
		MDataMap logMap = new MDataMap();
		logMap.put("order_code", orderCode);
		logMap.put("sku_code_before", oldSkuCode);
		logMap.put("sku_code_after", newSkuCode);
		logMap.put("create_time", FormatHelper.upDateTime());
		logMap.put("create_user", userInfo == null ? "" : userInfo.getLoginName());
		DbUp.upTable("lc_order_sku_change_log").dataInsert(logMap);
		
		return mResult;
	}
	
	private String[] getColorStyle(String val) {
		String[] vs = val.split("&");
		return new String[]{vs[0].replace("color_id=", ""), vs[1].replace("style_id=", "")};
	}
}
