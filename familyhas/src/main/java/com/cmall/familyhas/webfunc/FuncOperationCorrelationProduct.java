package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 保存商品关联分类
 * @remark 【商品推荐关联】-【设置】-【提交】
 * @author 任宏斌
 * @date 2019年12月25日
 */
public class FuncOperationCorrelationProduct extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String productCode = mDataMap.get("productCode");
		String correlationCategory = mDataMap.get("correlationCategory");
		String showLd = mDataMap.get("showLd");
		MDataMap dataMap = DbUp.upTable("pc_product_correlation").one("product_code", productCode);
		if (null == dataMap) {
			DbUp.upTable("pc_product_correlation").insert("uid", WebHelper.upUuid(), "product_code", productCode,
					"correlation_category", correlationCategory, "show_ld", showLd);
		} else {
			DbUp.upTable("pc_product_correlation")
					.update(new MDataMap("zid", dataMap.get("zid"), "uid", dataMap.get("uid"), "product_code",
							productCode, "correlation_category", correlationCategory, "show_ld", showLd));
		}
		return mResult;
	}
}
