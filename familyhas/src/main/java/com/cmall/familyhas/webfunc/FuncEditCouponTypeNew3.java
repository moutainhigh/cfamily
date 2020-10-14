package com.cmall.familyhas.webfunc;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改优惠券类型
 * @author ligj
 *
 */
public class FuncEditCouponTypeNew3 extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		String privideMoney  = mDataMap.get("zw_f_privide_money");
		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();

		//有效类型为范围时需要验证开始结束时间
		if ("4497471600080002".equals(mDataMap.get("zw_f_valid_type"))) {
			//已发放金额为0时
			if (StringUtils.isEmpty(privideMoney) || privideMoney.compareTo("0") <= 0) {
				mDataMap.put("zw_f_privide_money", "0");
				String startTime = mDataMap.get("zw_f_start_time");
				String endTime = mDataMap.get("zw_f_end_time");
				if (endTime.compareTo(startTime) <= 0) {
					//开始时间必须小于结束时间!
					mResult.inErrorMessage(916401201);
					return mResult;
				}else if (endTime.compareTo(createTime) <= 0) {
					//当前时间必须小于结束时间!
					mResult.inErrorMessage(916401214);
					return mResult;
				}
			}
			
			//有效类型为范围时天数字段设置为0
			mDataMap.put("zw_f_valid_day", "0");
		}else{
			//有效类型为天数时开始结束时间设置为空
			mDataMap.put("zw_f_start_time", "");
			mDataMap.put("zw_f_end_time", "");
			String validDay = mDataMap.get("zw_f_valid_day");
			try {
				if (Integer.parseInt(validDay) <= 0) {
					mResult.inErrorMessage(916421244);
					return mResult;
				}
			} catch (Exception e) {
				e.printStackTrace();
				mResult.inErrorMessage(916421245);
				return mResult;
			}
			
		}
		
		//跳转类型为商品详情时，跳转链接字段设置为空
		if ("4497471600280001".equals(mDataMap.get("zw_f_action_type"))) {
			mDataMap.put("zw_f_action_value", "");
		}
		//优惠券类型为折扣券时，将折扣券的值赋给优惠金额，两者公用同一字段
		if ("449748120002".equals(mDataMap.get("zw_f_money_type"))) {
			mDataMap.put("zw_f_money", mDataMap.get("zw_f_discount"));
			mDataMap.remove("zw_f_discount");
			//如果是折扣券，剩余金额重新赋值=总金额-已被领取金额
//			BigDecimal totalMoney = new BigDecimal(mDataMap.get("zw_f_total_money"));//总金额
//			BigDecimal pMoney = new BigDecimal(privideMoney);//已被领取的金额
//			BigDecimal sMoney = totalMoney.subtract(pMoney);
//			mDataMap.remove("zw_f_surplus_money");
//			mDataMap.put("zw_f_surplus_money", sMoney.toString());
		}
		if (mResult.upFlagTrue()) {
//			MDataMap mData = DbUp.upTable("oc_coupon_type").oneWhere(
//					"total_money,surplus_money", null, null, "uid", mDataMap.get("zw_f_uid"));
//			if(mData != null){
//				BigDecimal totalMoney1 = new BigDecimal(mData.get("total_money"));//库中的总金额
//				BigDecimal totalMoney2 = new BigDecimal(mDataMap.get("zw_f_total_money"));//要修改的总金额
//				if(totalMoney2.doubleValue() != totalMoney1.doubleValue()){
//					BigDecimal totalMoney3 = new BigDecimal(mData.get("surplus_money"));//剩余金额
//					totalMoney3 = totalMoney3.add(totalMoney2.subtract(totalMoney1));//计算出剩余金额
//					mDataMap.put("zw_f_surplus_money", totalMoney3.toString());
//				}
//			}
			/* 获取当前修改人 */
			String updater = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_updater", updater);
			
			// 不更新成本限额和剩余金额字段
			mDataMap.remove("zw_f_surplus_money");
			mDataMap.remove("zw_f_total_money");
		}
		
		// 兑换方式是无的话，清空对应值
		if("4497471600390001".equals(mDataMap.get("zw_f_exchange_type"))) {
			mDataMap.put("zw_f_exchange_value", "");
		}
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
				//清空限制条件
//				if ("4497471600070001".equals(mDataMap.get("zw_f_limit_condition"))) {
//					String clearLimitSql = "update oc_coupon_type_limit set brand_limit='4497471600070001' ,product_limit='4497471600070001',category_limit='4497471600070001',except_brand='0'," +
//							"except_product='0',except_category='0',brand_codes='',product_codes='',category_codes='',update_user='"+mDataMap.get("zw_f_updater")+"',update_time='"+mDataMap.get("zw_f_update_time")+"' " +
//							"where coupon_type_code='"+mDataMap.get("zw_f_coupon_type_code")+"'";
//					DbUp.upTable("oc_coupon_type_limit").dataExec(clearLimitSql, null);
//					
//					MDataMap inserLogMap = new MDataMap();
//					inserLogMap.put("uid", UUID.randomUUID().toString().replace("-", ""));
//					inserLogMap.put("info", "优惠券类型编号："+mDataMap.get("zw_f_coupon_type_code")+"，修改优惠券类型“限制条件”字段为“无限制”,清空所有限制条件");
//					inserLogMap.put("create_time", mDataMap.get("zw_f_update_time"));
//					inserLogMap.put("create_user", mDataMap.get("zw_f_updater"));
//					DbUp.upTable("lc_coupon_type_limit").dataInsert(inserLogMap);
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
