package com.cmall.familyhas.webfunc;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改自动选品规则
 */
public class FuncEditXuanpin extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MWebResult result = new MWebResult();
		
		if(StringUtils.isBlank(mAddMaps.get("name"))) {
			result.setResultCode(0);
			result.setResultMessage("名称不能为空");
			return result;
		}
		
		if(DbUp.upTable("pc_product_xuanpin").dataCount("name = :name and uid != :uid", new MDataMap("name",StringUtils.trimToEmpty(mAddMaps.get("name")),"uid", mAddMaps.get("uid"))) > 0) {
			result.setResultCode(0);
			result.setResultMessage("名称已存在");
			return result;
		}
		
		if("4497471600070002".equals(mAddMaps.get("category_limit"))) {
			if(StringUtils.isBlank(mAddMaps.get("category_codes"))) {
				result.setResultCode(0);
				result.setResultMessage("请至少选择一个分类");
				return result;
			}
		} else {
			mAddMaps.put("category_codes", "");
		}
		
		if("4497471600070002".equals(mAddMaps.get("uc_seller_type_limit"))) {
			if(StringUtils.isBlank(mAddMaps.get("uc_seller_type_codes"))) {
				result.setResultCode(0);
				result.setResultMessage("请至少选择一个商户类型");
				return result;
			}
		} else {
			mAddMaps.put("uc_seller_type_codes", "");
		}
		
		if("4497471600070002".equals(mAddMaps.get("small_seller_limit"))) {
			if(StringUtils.isBlank(mAddMaps.get("small_seller_codes"))) {
				result.setResultCode(0);
				result.setResultMessage("请至少选择一个商户");
				return result;
			}
		} else {
			mAddMaps.put("small_seller_codes", "");
		}
		
		if("4497471600070002".equals(mAddMaps.get("sell_price_limit"))) {
			if(StringUtils.isBlank(mAddMaps.get("sell_price_range"))
					|| mAddMaps.get("sell_price_range").split("-").length != 2) {
				result.setResultCode(0);
				result.setResultMessage("请填写销售价格范围");
				return result;
			}
			
			if(NumberUtils.toDouble(mAddMaps.get("sell_price_range").split("-")[0]) > NumberUtils.toDouble(mAddMaps.get("sell_price_range").split("-")[1])) {
				result.setResultCode(0);
				result.setResultMessage("最大价格需要大于最小价格");
				return result;
			}
		} else {
			mAddMaps.put("sell_price_range", "");
		}
		
		if("4497471600070002".equals(mAddMaps.get("product_stock_limit"))) {
			if(StringUtils.isBlank(mAddMaps.get("product_stock_range"))
					|| mAddMaps.get("product_stock_range").split("-").length != 2) {
				result.setResultCode(0);
				result.setResultMessage("请填写商品库存范围");
				return result;
			}
			
			if(NumberUtils.toDouble(mAddMaps.get("product_stock_range").split("-")[0]) > NumberUtils.toDouble(mAddMaps.get("product_stock_range").split("-")[1])) {
				result.setResultCode(0);
				result.setResultMessage("最大库存需要大于最小库存");
				return result;
			}
		} else {
			mAddMaps.put("product_stock_range", "");
		}
		
		if(NumberUtils.toInt(mAddMaps.get("max_size")) <= 0) {
			result.setResultCode(0);
			result.setResultMessage("商品数量必须大于0");
			return result;
		}
		
		MDataMap insertMap = new MDataMap();
		insertMap.put("uid", mAddMaps.get("uid"));
		insertMap.put("name", StringUtils.trimToEmpty(mAddMaps.get("name")));
		insertMap.put("category_limit", StringUtils.trimToEmpty(mAddMaps.get("category_limit")));
		insertMap.put("category_codes", StringUtils.trimToEmpty(mAddMaps.get("category_codes")));
		insertMap.put("uc_seller_type_limit", StringUtils.trimToEmpty(mAddMaps.get("uc_seller_type_limit")));
		insertMap.put("uc_seller_type_codes", StringUtils.trimToEmpty(mAddMaps.get("uc_seller_type_codes")));
		insertMap.put("small_seller_limit", StringUtils.trimToEmpty(mAddMaps.get("small_seller_limit")));
		insertMap.put("small_seller_codes", StringUtils.trimToEmpty(mAddMaps.get("small_seller_codes")));
		insertMap.put("sell_price_limit", StringUtils.trimToEmpty(mAddMaps.get("sell_price_limit")));
		insertMap.put("sell_price_range", StringUtils.trimToEmpty(mAddMaps.get("sell_price_range")));
		insertMap.put("product_status_limit", StringUtils.trimToEmpty(mAddMaps.get("product_status_limit")));
		insertMap.put("product_stock_limit", StringUtils.trimToEmpty(mAddMaps.get("product_stock_limit")));
		insertMap.put("product_stock_range", StringUtils.trimToEmpty(mAddMaps.get("product_stock_range")));
		insertMap.put("product_create_time_limit", StringUtils.trimToEmpty(mAddMaps.get("product_create_time_limit")));
		insertMap.put("product_sales_period", StringUtils.trimToEmpty(mAddMaps.get("product_sales_period")));
		insertMap.put("sorting_rule", StringUtils.trimToEmpty(mAddMaps.get("sorting_rule")));
		insertMap.put("max_size", StringUtils.trimToEmpty(mAddMaps.get("max_size")));
		insertMap.put("medical_flag", StringUtils.trimToEmpty(mAddMaps.get("medical_flag")));

		insertMap.put("brand_limit", StringUtils.trimToEmpty(mAddMaps.get("brand_limit")));
		insertMap.put("brand_codes", StringUtils.trimToEmpty(mAddMaps.get("brand_codes")));
		insertMap.put("comment_limit", StringUtils.trimToEmpty(mAddMaps.get("comment_limit")));
		insertMap.put("comment_num", NumberUtils.toInt(StringUtils.trimToEmpty(mAddMaps.get("comment_num")))+"");
		insertMap.put("comment_good_limit", StringUtils.trimToEmpty(mAddMaps.get("comment_good_limit")));
		insertMap.put("comment_good_num", NumberUtils.toInt(StringUtils.trimToEmpty(mAddMaps.get("comment_good_num")))+"");
		insertMap.put("rebuy_rate_limit", StringUtils.trimToEmpty(mAddMaps.get("rebuy_rate_limit")));
		insertMap.put("rebuy_rate", NumberUtils.toInt(StringUtils.trimToEmpty(mAddMaps.get("rebuy_rate")))+"");
		
		insertMap.put("update_time", FormatHelper.upDateTime());
		
		DbUp.upTable("pc_product_xuanpin").dataUpdate(insertMap, "", "uid");
		
		return result;
	}

}
