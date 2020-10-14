package com.cmall.familyhas.api;

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

public class ApiChargeConfigDel extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		String uid = mDataMap.get("zw_f_uid");
		
		MWebResult mResult = new MWebResult();
		MDataMap configMap = DbUp.upTable("fh_bf_charge_config").one("uid", uid);
		String oldRatio = configMap.get("charge_ratio");
		String oldProfit = configMap.get("profit_ratio");
		String oldSellerType = configMap.get("seller_type"); 
		
		int count = DbUp.upTable("fh_bf_charge_config").delete("uid", uid);
		if(count == 1) {
			//记录缤纷利润佣金操作日志
			String createTime = DateUtil.getNowTime();
			String user = UserFactory.INSTANCE.create().getRealName();
			MDataMap chargeNameMap = DbUp.upTable("sc_define").one("define_code", configMap.get("charge_name"));
			MDataMap chargeTypeMap = DbUp.upTable("uc_sellercategory").one("category_code", configMap.get("charge_type"), "seller_code", "SI2003", "level", "2");
			DbUp.upTable("fh_bf_charge_log").insert("operate_type", "删除", "good_type", chargeTypeMap.get("category_name"), "charge_name", chargeNameMap.get("define_name"), "charge_ratio", oldRatio, 
					"operator", user, "operate_time", createTime, "profit_ratio", oldProfit, "seller_type", oldSellerType);
			
			//判断佣金百分比和利润百分比是否变更(仅针对除自营缤纷在惠家有销售百分比以外)
			String chargeName = configMap.get("charge_name");
			if(!"449748060003".equals(chargeName)) {
				String charge_type = configMap.get("charge_type");
				String seller_type = configMap.get("seller_type");
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
						remark = "佣金配置删除导致强制下架";
					}else {//其他状态
						newStatus = "50";
						remark = "佣金配置删除自动驳回";
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
			
			mResult.setResultCode(1);
			mResult.setResultMessage("删除成功!");
		}else {
			mResult.setResultCode(0);
			mResult.setResultMessage("删除失败!");
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
