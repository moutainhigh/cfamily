package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cmall.familyhas.util.DateUtil;
import com.cmall.systemcenter.dcb.PushSkuStatusService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ApiChargeConfigEdit<E> extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		String uid = mDataMap.get("zw_f_uid");
		String zid = mDataMap.get("zw_f_zid");
		String charge_type = mDataMap.get("zw_f_charge_type");
		String charge_name = mDataMap.get("zw_f_charge_name");
		String charge_ratio = mDataMap.get("zw_f_charge_ratio");
		String seller_type = mDataMap.get("zw_f_seller_type");
		String profit_ratio = mDataMap.get("zw_f_profit_ratio");
		
		MWebResult mResult = new MWebResult();
		List<Map<String, Object>> exists = DbUp.upTable("fh_bf_charge_config").listByWhere("charge_type", charge_type, "charge_name", charge_name, "seller_type", seller_type);
		if(exists.size() > 0) {
			Map<String, Object> exist = exists.get(0);
			if(!zid.equals(exist.get("zid").toString())) {
				mResult.setResultCode(1);
				mResult.setResultMessage("修改失败,已存在!");
				return mResult;
			}
		}
		
		MDataMap configMap = DbUp.upTable("fh_bf_charge_config").one("uid", uid);
		String oldRatio = configMap.get("charge_ratio");
		String oldProfit = configMap.get("profit_ratio");
		
		MDataMap updateMap = new MDataMap();
		updateMap.put("uid", uid);
		updateMap.put("zid", zid);
		updateMap.put("charge_type", charge_type);
		updateMap.put("charge_name", charge_name);
		updateMap.put("charge_ratio", charge_ratio);
		updateMap.put("seller_type", seller_type);
		updateMap.put("profit_ratio", profit_ratio);
		int count = DbUp.upTable("fh_bf_charge_config").update(updateMap);
		if(count == 1) {
			//记录缤纷利润佣金操作日志
			String createTime = DateUtil.getNowTime();
			String user = UserFactory.INSTANCE.create().getRealName();
			MDataMap chargeNameMap = DbUp.upTable("sc_define").one("define_code", mDataMap.get("zw_f_charge_name"));
			MDataMap chargeTypeMap = DbUp.upTable("uc_sellercategory").one("category_code", mDataMap.get("zw_f_charge_type"), "seller_code", "SI2003", "flaginable", "449746250001", "level", "2");
			DbUp.upTable("fh_bf_charge_log").insert("operate_type", "修改", "good_type", chargeTypeMap.get("category_name"), "charge_name", chargeNameMap.get("define_name"), "charge_ratio", oldRatio, 
					"change_ratio", mDataMap.get("zw_f_charge_ratio"), "operator", user, "operate_time", createTime, "seller_type", seller_type, "profit_ratio", oldProfit, "change_profit", profit_ratio);
			
			//判断佣金百分比和利润百分比是否变更(仅针对除自营缤纷在惠家有销售百分比以外)
			String chargeName = mDataMap.get("zw_f_charge_name");
			if(!"449748060003".equals(chargeName)) {
				BigDecimal oldRatioNum = new BigDecimal(oldRatio);
				BigDecimal ratioNum = new BigDecimal(mDataMap.get("zw_f_charge_ratio"));
				BigDecimal oldProfitNum = new BigDecimal(oldProfit);
				BigDecimal profitNum = new BigDecimal(profit_ratio);
				if(oldRatioNum.compareTo(ratioNum) != 0 || oldProfitNum.compareTo(profitNum) != 0) {
					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					String sql = "select sku.*, skuinfo.sku_name from pc_bf_skuinfo sku, pc_skuinfo skuinfo, pc_productinfo product, usercenter.uc_sellercategory_product_relation rela, usercenter.uc_seller_info_extend extend "
									+ "where sku.product_code = product.product_code and product.product_code = rela.product_code and rela.category_code = '" + charge_type 
									+ "' and rela.seller_code = 'SI2003' and sku.sku_code = skuinfo.sku_code and (sku.sku_status = '1' or sku.sku_status = '2' or sku.sku_status = '10' or sku.sku_status = '20')"
									+ " and extend.small_seller_code = product.small_seller_code ";
					if("449748070002".equals(seller_type)) {//缤纷商户
						sql += "and extend.uc_seller_type = '4497478100050005'";
					}else {//自营商户
						sql += "and extend.uc_seller_type <> '4497478100050005'";
					}
					
					List<Map<String, Object>> list1 = DbUp.upTable("pc_bf_skuinfo").dataSqlList(sql, new MDataMap());
					list.addAll(list1);
					list = getAllSkuInfo(seller_type, charge_type, list);
					
					Set<Map<String, Object>> set = new HashSet<Map<String, Object>>();
					set.addAll(list);
					
					list.clear();
					list.addAll(set);
					for(Map<String, Object> map : list) {
						String newStatus = "";
						String remark = "";
						String sku_code = map.get("sku_code").toString();
						String sku_status = map.get("sku_status").toString();
						if("10".equals(sku_status)) {//已上架
							//调用多彩宝接口，让多彩宝对应的sku下架
							PushSkuStatusService pushSkuStatusService = new PushSkuStatusService();
							pushSkuStatusService.pushSkuStatus(sku_code, "N", 0, "");
							
							newStatus = "30";
							remark = "佣金配置修改导致强制下架";
						}else {//其他状态
							newStatus = "50";
							remark = "佣金配置修改自动驳回";
						}
						
						//更新pc_bf_skuinfo表状态
						MDataMap skuUpdateMap = new MDataMap();
						skuUpdateMap.put("zid", map.get("zid").toString());
						skuUpdateMap.put("uid", map.get("uid").toString());
						skuUpdateMap.put("sku_status", newStatus);
						DbUp.upTable("pc_bf_skuinfo").update(skuUpdateMap);
						
						//添加pc_bf_review_log 日志表                 
						DbUp.upTable("pc_bf_review_log").insert("sku_code", sku_code, "sku_name", map.get("sku_name").toString(), "operate_status", "佣金配置变更", "operator", "系统", 
								"operate_time", createTime, "remark", remark);
					}
				}
			}
			
			mResult.setResultCode(0);
			mResult.setResultMessage("修改成功!");
		}else {
			mResult.setResultCode(1);
			mResult.setResultMessage("修改失败!");
		}
		
		return mResult;
	}

	private List<Map<String, Object>> getAllSkuInfo(String seller_type, String charge_type, List<Map<String, Object>> list) {
		List<Map<String, Object>> sellerCategoryList = DbUp.upTable("uc_sellercategory").listByWhere("seller_code", "SI2003", "parent_code", charge_type);
		for(Map<String, Object> sellerCategory : sellerCategoryList) {
			String sql1 = "select sku.*, skuinfo.sku_name from pc_bf_skuinfo sku, pc_skuinfo skuinfo, pc_productinfo product, usercenter.uc_sellercategory_product_relation rela, usercenter.uc_seller_info_extend extend "
							+ "where sku.product_code = product.product_code and product.product_code = rela.product_code and rela.category_code = '" + sellerCategory.get("category_code").toString()
							+ "' and rela.seller_code = 'SI2003' and sku.sku_code = skuinfo.sku_code and (sku.sku_status = '1' or sku.sku_status = '2' or sku.sku_status = '10' or sku.sku_status = '20')"
							+ " and extend.small_seller_code = product.small_seller_code ";
			if("449748070002".equals(seller_type)) {//缤纷商户
				sql1 += "and extend.uc_seller_type = '4497478100050005'";
			}else {//自营商户
				sql1 += "and extend.uc_seller_type <> '4497478100050005'";
			}
			
			List<Map<String, Object>> list2 = DbUp.upTable("pc_bf_skuinfo").dataSqlList(sql1, new MDataMap());
			list.addAll(list2);
			
			list.addAll(getAllSkuInfo(seller_type, sellerCategory.get("category_code").toString(), list2));
		}
		return list;
	}
}
