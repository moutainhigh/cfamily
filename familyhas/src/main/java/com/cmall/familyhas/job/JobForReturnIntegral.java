package com.cmall.familyhas.job;

import java.math.BigDecimal;
import com.cmall.familyhas.service.ReturnGoodsService;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 返还积分
 */
public class JobForReturnIntegral extends RootJobForExec {

	ReturnGoodsService goodsService = new ReturnGoodsService();
	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();

	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();

		MDataMap mDataMap = DbUp.upTable("oc_order_pay_return").one("pay_return_code", sInfo);
		if (!"449748090001".equals(mDataMap.get("return_status"))) {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("不是待返还状态：" + mDataMap.get("return_status"));
			return mWebResult;
		}

		// 总使用积分
		BigDecimal usedMoney = (BigDecimal) DbUp.upTable("oc_order_pay").dataGet("payed_money", "",
				new MDataMap("order_code", mDataMap.get("order_code"), "pay_type", "449746280008"));
		// 已返还积分
		BigDecimal alreadyReturnMoney = (BigDecimal) DbUp.upTable("oc_order_pay_return").dataGet("IFNULL(sum(return_money),0.0)", "",
				new MDataMap("order_code", mDataMap.get("order_code"), "pay_type", "449746280008", "return_status", "449748090002"));
		// 待返还积分
		BigDecimal returnMoney = new BigDecimal(mDataMap.get("return_money"));

		// 不能超过订单使用的总积分
		if (alreadyReturnMoney.add(returnMoney).compareTo(usedMoney) > 0) {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("返还的积分超过订单使用的积分");
			return mWebResult;
		}
		
		RootResult rootResult = null;
		
		if ("449748080001".equals(mDataMap.get("return_type"))) { // 取消订单返还积分
			MDataMap orderInfo = DbUp.upTable("oc_orderinfo").oneWhere("small_seller_code,out_order_code", "", "", "order_code",mDataMap.get("order_code"));
			if("SI2003".equalsIgnoreCase(orderInfo.get("small_seller_code")) || "SI2009".equalsIgnoreCase(orderInfo.get("small_seller_code"))){
				if(orderInfo.get("out_order_code").isEmpty()){
					// TV品取消订单，没有外部订单号的情况下，走取消占用标识
					rootResult = plusServiceAccm.returnForAccmAmt(mDataMap.get("order_code"), UpdateCustAmtInput.CurdFlag.F, returnMoney);
				}else{
					mWebResult.setResultCode(1);
					mWebResult.setResultMessage("已经存在LD订单号，不能调用返还积分接口");
					return mWebResult;
				}
			}else{
				rootResult = plusServiceAccm.returnForAccmAmt(mDataMap.get("order_code"), UpdateCustAmtInput.CurdFlag.D, returnMoney);
			}
		} else if ("449748080002".equals(mDataMap.get("return_type"))) {  // 订单退货返还积分
			rootResult = plusServiceAccm.returnForAccmAmt(mDataMap.get("order_code"), UpdateCustAmtInput.CurdFlag.R, returnMoney);
		}

		if (rootResult == null) {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("未识别的类型: " + mDataMap.get("return_type"));
			return mWebResult;
		}

		if (rootResult.getResultCode() == 1) {
			mDataMap.put("return_status", "449748090002");
			mDataMap.put("update_time", FormatHelper.upDateTime());
			DbUp.upTable("oc_order_pay_return").dataUpdate(mDataMap, "return_status,update_time", "zid");
			
			// 记录积分变更日志
			MDataMap orderPay = DbUp.upTable("oc_order_pay").one("order_code", mDataMap.get("order_code"), "pay_type", "449746280008");
			MDataMap changeDataMap = new MDataMap();
			changeDataMap.put("member_code", orderPay.get("merchant_id"));
			changeDataMap.put("cust_id", orderPay.get("pay_remark"));
			changeDataMap.put("change_type", mDataMap.get("return_type"));
			changeDataMap.put("change_money", returnMoney.toString());
			
			if ("449748080001".equals(mDataMap.get("return_type"))) {
				// 取消订单存订单号
				changeDataMap.put("remark", mDataMap.get("order_code"));
			} else if ("449748080002".equals(mDataMap.get("return_type"))) {
				// 退货存退货单号
				changeDataMap.put("remark", mDataMap.get("target_info"));
			} else {
				changeDataMap.put("remark", mDataMap.get("order_code"));
			}
			
			changeDataMap.put("create_time", FormatHelper.upDateTime());
			DbUp.upTable("mc_member_integral_change").dataInsert(changeDataMap);
		}

		mWebResult.inOtherResult(rootResult);
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990003");
		config.setMaxExecNumber(5);
		return config;
	}

}
