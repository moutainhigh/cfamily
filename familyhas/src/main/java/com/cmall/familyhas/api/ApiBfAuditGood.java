package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.Map;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class ApiBfAuditGood extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		String sku_codes = mDataMap.get("sku_codes");
		String[] sku_code_array = sku_codes.split(",");
		String remark = mDataMap.get("remark");
		
		boolean flag = true;
		String realMsg = "";
		int failureCount = 0;
		for(int i = 0; i < sku_code_array.length; i ++) {
			String sku_code = sku_code_array[i];
			
			//通过sku编码查询sku信息,计算供货价，佣金，运营成本
			MDataMap skuInfo = DbUp.upTable("pc_skuinfo").one("sku_code", sku_code);
			String product_code = skuInfo.get("product_code");
			String cost_priceChar = skuInfo.get("cost_price");//成本价
			BigDecimal cost_price = new BigDecimal(cost_priceChar);
			MDataMap productInfo = DbUp.upTable("pc_productinfo").one("product_code", product_code);
			String small_seller_code = productInfo.get("small_seller_code");
			
			//求供货价
			String sql = "select p_bf_getSupplyPrice('" + product_code + "', '" + cost_price +"', '" + small_seller_code +"') as supplyPrice from dual";
			Map<String, Object> map = DbUp.upTable("pc_bf_skuinfo").dataSqlOne(sql, new MDataMap());
			String supplyPriceChar = map.get("supplyPrice").toString();
			BigDecimal supplyPrice = new BigDecimal(supplyPriceChar);//供货价
			
			//求佣金
			String sql1 = "select p_bf_getCommission('" + product_code + "', '" + cost_price +"', '" + small_seller_code +"') as commissionPrice from dual";
			Map<String, Object> map1 = DbUp.upTable("pc_bf_skuinfo").dataSqlOne(sql1, new MDataMap());
			String commissionPriceChar = map1.get("commissionPrice").toString();
			BigDecimal commissionPrice = new BigDecimal(commissionPriceChar);//佣金
			
			//运营成本
			String sql2 = "select p_bf_getOperatePrice('" + product_code + "', '" + cost_price +"', '" + small_seller_code +"') as operatePrice from dual";
			Map<String, Object> map2 = DbUp.upTable("pc_bf_skuinfo").dataSqlOne(sql2, new MDataMap());
			String operatePriceChar = map2.get("operatePrice").toString();
			BigDecimal operatePrice = new BigDecimal(operatePriceChar);//运营成本
			
			if(cost_price.compareTo(new BigDecimal(0)) != 0 && supplyPrice.compareTo(new BigDecimal(0)) != 0 && commissionPrice.compareTo(new BigDecimal(0)) != 0 && operatePrice.compareTo(new BigDecimal(0)) != 0) {
				String profitRadito = "0", chargeRadito = "0";
				//获取佣金百分比，利润百分比
				String goodTypeSql = "select p_bf_getCategoryCode('" + product_code + "') as goodType from dual";
				Map<String, Object> goodType = DbUp.upTable("pc_bf_skuinfo").dataSqlOne(goodTypeSql, new MDataMap());
				String good_type = goodType.get("goodType").toString();
				
				String sellerTypeSql = "select extend.uc_seller_type from usercenter.uc_seller_info_extend extend where extend.small_seller_code = '" + small_seller_code + "'";
				Map<String, Object> sellerType = DbUp.upTable("pc_bf_skuinfo").dataSqlOne(sellerTypeSql, new MDataMap());
				String uc_seller_type = sellerType.get("uc_seller_type").toString();
				if("4497478100050005".equals(uc_seller_type)) {
					//缤纷
					String configSql = "select config.charge_ratio, config.profit_ratio from familyhas.fh_bf_charge_config config where config.charge_type = '" + good_type + "' and config.charge_name = '449748060002'";
					Map<String, Object> config = DbUp.upTable("pc_bf_skuinfo").dataSqlOne(configSql, new MDataMap());
					if(config != null) {
						profitRadito = config.get("profit_ratio").toString();
						chargeRadito = config.get("charge_ratio").toString();
					}
				}else {
					//自营
					String configSql = "select config.charge_ratio, config.profit_ratio from familyhas.fh_bf_charge_config config where config.charge_type = '" + good_type + "' and config.charge_name = '449748060001'";
					Map<String, Object> config = DbUp.upTable("pc_bf_skuinfo").dataSqlOne(configSql, new MDataMap());
					if(config != null) {
						profitRadito = config.get("profit_ratio").toString();
						chargeRadito = config.get("charge_ratio").toString();
					}
				}
				
				String createTime = DateUtil.getNowTime();
				String user = UserFactory.INSTANCE.create().getRealName();
				MDataMap bfSku = DbUp.upTable("pc_bf_skuinfo").one("sku_code", sku_code);
				if(bfSku == null) {
					//添加缤纷记录,缤纷审核日志表
					DbUp.upTable("pc_bf_skuinfo").insert("sku_code", sku_code, "product_code", product_code, "sku_supply", supplyPriceChar, "sku_cost", cost_priceChar, "sku_mission", commissionPriceChar, 
							"sku_operate_cost", operatePriceChar, "sku_status", "1", "sku_charge", chargeRadito, "sku_profit", profitRadito);
					DbUp.upTable("pc_bf_review_log").insert("sku_code", sku_code, "sku_name", skuInfo.get("sku_name"), "operate_status", "招商经理审核通过", "remark", remark, "operator", user, "operate_time", createTime);
				}else {
					String skuStatus = bfSku.get("sku_status");
					if("0".equals(skuStatus) || "11".equals(skuStatus) || "12".equals(skuStatus) || "30".equals(skuStatus) || "40".equals(skuStatus) || "50".equals(skuStatus)) {
						//修改缤纷记录
						MDataMap updateMap = new MDataMap();
						updateMap.put("zid", bfSku.get("zid"));
						updateMap.put("uid", bfSku.get("uid"));
						updateMap.put("sku_supply", supplyPriceChar);
						updateMap.put("sku_cost", cost_priceChar);
						updateMap.put("sku_mission", commissionPriceChar);
						updateMap.put("sku_operate_cost", operatePriceChar);
						updateMap.put("sku_status", "1");
						updateMap.put("sku_charge", chargeRadito);
						updateMap.put("sku_profit", profitRadito);
						DbUp.upTable("pc_bf_skuinfo").update(updateMap);
						
						DbUp.upTable("pc_bf_review_log").insert("sku_code", sku_code, "sku_name", skuInfo.get("sku_name"), "operate_status", "招商经理审核通过", "remark", remark, "operator", user, 
								"operate_time", createTime);
					}else {
						flag = false;
						failureCount += 1;
						realMsg = "此sku不能推送!";
					}
				}
			}else {
				flag = false;
				failureCount += 1;
				realMsg = "价格不能为0!";
			}
		}
		
		//单个sku推送，成功则显示成功，失败，显示具体原因；多个sku推送，都显示推送成功，显示失败个数
		String msg = "";
		MWebResult mResult = new MWebResult();
		if(flag && sku_code_array.length == 1) {
			msg = "推送成功";
			mResult.setResultCode(0);
		}else if(!flag && sku_code_array.length == 1) {
			msg = realMsg;
			mResult.setResultCode(1);
		}else {
			msg = "推送成功，失败" + failureCount + "条。";
			mResult.setResultCode(0);
		}
		mResult.setResultMessage(msg);
		return mResult;
	}
}
