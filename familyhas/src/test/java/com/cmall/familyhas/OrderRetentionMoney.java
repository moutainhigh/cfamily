package com.cmall.familyhas;

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopTest;
import com.srnpr.zapdata.dbdo.DbUp;

public class OrderRetentionMoney extends TopTest {

	public void initData() {
		// String insertSql = "INSERT INTO ordercenter.oc_seller_retention_money
		// (uid,small_seller_code,small_seller_name,max_retention_money,create_time,creator,update_time,updator,money_collection_way)(SELECT
		// REPLACE (UUID(), '-',
		// ''),small_seller_code,seller_name,quality_retention_money,'test',now(),'test',now(),money_collection_way
		// FROM usercenter.v_base_uc_sellerinfo)";
		// DbUp.upTable("oc_seller_retention_money").dataExec(insertSql, new
		// MDataMap());
		String selSql = "select * from oc_seller_retention_money";
		List<Map<String, Object>> list = DbUp.upTable("oc_seller_retention_money").upTemplate().queryForList(selSql,
				new MDataMap());
		for (Map<String, Object> map : list) {
			MDataMap update = new MDataMap();
			String small_seller_code = map.get("small_seller_code").toString();
			String editSql = "select sum(period_retention_money) as period_retention_money from ordercenter.oc_bill_seller_retention_money where small_seller_code='"
					+ small_seller_code + "'";
			Map<String, Object> sellerMoney = DbUp.upTable("oc_bill_seller_retention_money").dataSqlOne(editSql, null);
			String settle_type = settleType(small_seller_code);
			update.put("small_seller_code", small_seller_code);
			update.put("settle_type", settle_type);
			if (sellerMoney != null) {
				Double period_retention_money = Double.valueOf(sellerMoney.get("period_retention_money") != null
						? sellerMoney.get("period_retention_money").toString() : "0.00");
				update.put("deduct_retention_money", String.valueOf(period_retention_money));
				update.put("retention_money_sum", String.valueOf(period_retention_money));
			} else {
				update.put("deduct_retention_money", "0.00");
				update.put("retention_money_sum", "0.00");
			}
			DbUp.upTable("oc_seller_retention_money").dataUpdate(update, "settle_type,deduct_retention_money,retention_money_sum",
					"small_seller_code");

			MDataMap detail = new MDataMap();
			detail.put("small_seller_code", small_seller_code);
			detail.put("settle_type", settle_type);
			String accountType = accountType(small_seller_code);
			detail.put("account_type", accountType);
			DbUp.upTable("oc_retention_money_merchant").dataUpdate(detail, "settle_type,account_type",
					"small_seller_code");
		}
	}

	private static String settleType(String smallSellerCode) {
		String settleType = "";
		Map<String, Object> seller = DbUp.upTable("uc_seller_info_extend").dataSqlOne(
				"select * from uc_seller_info_extend where small_seller_code=:small_seller_code",
				new MDataMap("small_seller_code", smallSellerCode));
		if (StringUtils.equals("4497478100050001", seller.get("uc_seller_type").toString())) {
			settleType = "4497477900040001";
		} else if (StringUtils.equals("4497478100050002", seller.get("uc_seller_type").toString())) {
			settleType = "4497477900040002";
		} else if (StringUtils.equals("4497478100050003", seller.get("uc_seller_type").toString())) {
			settleType = "4497477900040003";
		} else if (StringUtils.equals("4497478100050004", seller.get("uc_seller_type").toString())) {
			settleType = "4497477900040004";
		}
		return settleType;
	}

	private static String accountType(String smallSellerCode) {
		String accountType = "";
		Map<String, Object> seller = DbUp.upTable("uc_seller_info_extend").dataSqlOne(
				"select * from uc_seller_info_extend where small_seller_code=:small_seller_code",
				new MDataMap("small_seller_code", smallSellerCode));
		if (StringUtils.equals("4497478100030003", seller.get("account_clear_type").toString())) {
			accountType = "4497477900030001";
		} else if (StringUtils.equals("4497478100030004", seller.get("account_clear_type").toString())) {
			accountType = "4497477900030002";
		}
		return accountType;
	}

	public static void main(String[] args) {
		new OrderRetentionMoney().initData();
//		Map<String, Object> map = DbUp.upTable("pc_productinfo").dataSqlOne("select * from productcenter.pc_productinfo where product_code = '7016113449'", new MDataMap());
//		System.out.println(map);
	}
}
