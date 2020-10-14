package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 京东商品池商品推送
 */
public class FuncAddChoosedProduct extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String uidText = StringUtils.trimToEmpty(mDataMap.get("zw_f_uid"));
		
		String[] uids = uidText.split(",");
		for(String uid : uids) {
			MDataMap product = DbUp.upTable("pc_jingdong_product").one("uid", uid);
			
			// 不存在的情况下再添加
			if(DbUp.upTable("pc_jingdong_choosed_products").count("jd_sku_id",product.get("sku_code")) == 0) {
				DbUp.upTable("pc_jingdong_choosed_products").dataInsert(new MDataMap(
						"jd_sku_id", product.get("sku_code"),
						"jd_erppid", product.get("spu_code"),
						"is_enabled", "1"
						));
			}
			
			product.put("push_state", "449746250001");
			DbUp.upTable("pc_jingdong_product").dataUpdate(product, "push_state", "zid");
		}
		
		return mResult;
	}

}
