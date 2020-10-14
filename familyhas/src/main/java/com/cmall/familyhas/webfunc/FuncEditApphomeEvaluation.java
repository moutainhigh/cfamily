package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改首页商品评价封面
 * @author lgx
 * 
 */
public class FuncEditApphomeEvaluation extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		
		String evaluation_uid = mDataMap.get("zw_f_evaluation_uid");
		String cover_img = mDataMap.get("zw_f_cover_img");
		String product_code = mDataMap.get("zw_f_product_code");
		String order_assessment = mDataMap.get("zw_f_order_assessment");
		
		if(StringUtils.isBlank(evaluation_uid) && StringUtils.isBlank(cover_img) && StringUtils.isBlank(product_code) && StringUtils.isBlank(order_assessment)){
			mResult.setResultCode(-1);
			mResult.setResultMessage("修改失败,请重试!");
			return mResult;
		}
		
		MDataMap updateDataMap = new MDataMap();
		updateDataMap.put("cover_img", cover_img);
		updateDataMap.put("evaluation_uid", evaluation_uid);
		updateDataMap.put("order_assessment", order_assessment);
		updateDataMap.put("product_code", product_code);
		updateDataMap.put("is_edit", "1");
		DbUp.upTable("fh_apphome_evaluation").dataUpdate(updateDataMap, "evaluation_uid,cover_img,order_assessment,is_edit", "product_code");
		
		return mResult;
	}
	
}
