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
public class FuncEditCouponType extends FuncEdit {
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		String privideMoney  = mDataMap.get("zw_f_privide_money");
		/*系统当前时间*/
		String createTime = com.cmall.familyhas.util.DateUtil.getNowTime();

		//已发放金额为0时
		if (StringUtils.isEmpty(privideMoney) || privideMoney.compareTo("0") <= 0) {
			String startTime = mDataMap.get("zw_f_start_time");
			String endTime = mDataMap.get("zw_f_end_time");
			mDataMap.put("zw_f_privide_money", "0");
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
//			/* 获取当前修改人 */
			String updater = UserFactory.INSTANCE.create().getLoginName();
			mDataMap.put("zw_f_update_time", createTime);
			mDataMap.put("zw_f_updater", updater);
			
			// 不更新成本限额和剩余金额字段
			mDataMap.remove("zw_f_surplus_money");
			mDataMap.remove("zw_f_total_money");
		}
		try {
			if (mResult.upFlagTrue()) {
				mResult = super.funcDo(sOperateUid, mDataMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}
}
