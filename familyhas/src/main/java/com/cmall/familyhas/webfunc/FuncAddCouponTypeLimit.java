package com.cmall.familyhas.webfunc;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加优惠券类型限制 迭代3
 * @author ligj
 * time:2015-06-07 13:34:00
 *
 */
public class FuncAddCouponTypeLimit extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		String couponTypeCode = mDataMap.get("zw_f_coupon_type_code");
		String exceptBrand = mDataMap.get("zw_f_except_brand");
		String exceptProduct = mDataMap.get("zw_f_except_product");
		String exceptCategory = mDataMap.get("zw_f_except_category");

		String brandLimit = mDataMap.get("zw_f_brand_limit");
		String productLimit = mDataMap.get("zw_f_product_limit");
		String categoryLimit = mDataMap.get("zw_f_category_limit");
		
		String brandCodes = mDataMap.get("zw_f_brand_codes");
		String productCodes = mDataMap.get("zw_f_product_codes");
		String categoryCodes = mDataMap.get("zw_f_category_codes");
		
		if ("4497471600070002".equals(brandLimit) && StringUtils.isEmpty(brandCodes)) {
			mResult.inErrorMessage(916421247,"品牌限制","品牌编号");
			return mResult;
		}		
		if ("4497471600070002".equals(productLimit) && StringUtils.isEmpty(productCodes)) {
			mResult.inErrorMessage(916421247,"商品限制","商品编号");
			return mResult;
		}		
		if ("4497471600070002".equals(categoryLimit) && StringUtils.isEmpty(categoryCodes)) {
			mResult.inErrorMessage(916421247,"分类限制","分类编号");
			return mResult;
		}
		
		if (StringUtils.isEmpty(exceptBrand)) {
			mDataMap.put("zw_f_except_brand", "0");
		}
		if (StringUtils.isEmpty(exceptProduct)) {
			mDataMap.put("zw_f_except_product", "0");
		}
		if (StringUtils.isEmpty(exceptCategory)) {
			mDataMap.put("zw_f_except_category", "0");
		}
		String createTime = "";
		String createUser = "";
		if (mResult.upFlagTrue()) {
			/*系统当前时间*/
			createTime = com.cmall.familyhas.util.DateUtil.getNowTime();
			/*获取当前登录人*/
			createUser = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_update_user", createUser);
		}
		
		try{
			if (mResult.upFlagTrue()) {
				//判断进行添加操作还是修改操作
				MDataMap couponTypeLimitMap = DbUp.upTable("oc_coupon_type_limit").one("coupon_type_code",couponTypeCode);
				if (null != couponTypeLimitMap && !couponTypeLimitMap.isEmpty()) {
					mDataMap.put("zw_f_uid", couponTypeLimitMap.get("uid"));
					FuncEdit edit = new FuncEdit();
					mResult = edit.funcDo(sOperateUid, mDataMap);
				}else{
					//进行添加操作
					mDataMap.put("zw_f_create_time", createTime);
					mDataMap.put("zw_f_create_user", createUser);
					mResult = super.funcDo(sOperateUid, mDataMap);
				}
				MDataMap inserLogMap = new MDataMap();
				inserLogMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
				inserLogMap.put("info", JSON.toJSONString(mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME)));
				inserLogMap.put("create_time", createTime);
				inserLogMap.put("create_user", createUser);
				DbUp.upTable("lc_coupon_type_limit").dataInsert(inserLogMap);
			}
		}catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
