package com.cmall.familyhas.job;

import java.math.BigDecimal;

import com.cmall.familyhas.service.ReturnGoodsService;
import com.srnpr.xmasorder.service.TeslaCrdtService;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJobForExec;
import com.srnpr.zapweb.webmodel.ConfigJobExec;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 返还暂存款定时
 */
public class JobForReturnZck extends RootJobForExec {

	ReturnGoodsService goodsService = new ReturnGoodsService();
	TeslaCrdtService plusServiceCrdt = new TeslaCrdtService();

	public IBaseResult execByInfo(String sInfo) {
		MWebResult mWebResult = new MWebResult();

		MDataMap mDataMap = DbUp.upTable("oc_order_pay_return").one("pay_return_code", sInfo);
		
		if (!"449748090001".equals(mDataMap.get("return_status"))) {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("不是待返还状态：" + mDataMap.get("return_status"));
			return mWebResult;
		}

		// 总使用暂存款
		BigDecimal usedMoney = (BigDecimal) DbUp.upTable("oc_order_pay").dataGet("payed_money", "",
				new MDataMap("order_code", mDataMap.get("order_code"), "pay_type", "449746280007"));
		// 已返还暂存款
		BigDecimal alreadyReturnMoney = (BigDecimal) DbUp.upTable("oc_order_pay_return").dataGet("IFNULL(sum(return_money),0.0)", "",
				new MDataMap("order_code", mDataMap.get("order_code"), "pay_type", "449746280007", "return_status", "449748090002"));
		// 待返还暂存款
		BigDecimal returnMoney = new BigDecimal(mDataMap.get("return_money"));

		// 不能超过订单使用的总暂存款
		if (alreadyReturnMoney.add(returnMoney).compareTo(usedMoney) > 0) {
			mWebResult.setResultCode(1);
			mWebResult.setResultMessage("返还的暂存款超过订单使用的暂存款");
			return mWebResult;
		}
		
		RootResult rootResult = null;

		if ("449748170001".equals(mDataMap.get("return_type"))) { // 取消订单返还暂存款
			MDataMap orderInfo = DbUp.upTable("oc_orderinfo").oneWhere("small_seller_code,out_order_code", "", "", "order_code",mDataMap.get("order_code"));
			if("SI2003".equalsIgnoreCase(orderInfo.get("small_seller_code")) || "SI2009".equalsIgnoreCase(orderInfo.get("small_seller_code"))){
				if(orderInfo.get("out_order_code").isEmpty()){
					// TV品取消订单，没有外部订单号的情况下，走取消占用标识
					rootResult = plusServiceCrdt.returnForCrdtAmt(mDataMap.get("order_code"), UpdateCustAmtInput.CurdFlag.F, returnMoney);
				}else{
					mWebResult.setResultCode(1);
					mWebResult.setResultMessage("已经存在LD订单号，不能调用返还暂存款接口");
					return mWebResult;
				}
			}else{
				rootResult = plusServiceCrdt.returnForCrdtAmt(mDataMap.get("order_code"), UpdateCustAmtInput.CurdFlag.D, returnMoney);
			}
		} else if ("449748170002".equals(mDataMap.get("return_type"))) {  // 订单退货返还暂存款
			rootResult = plusServiceCrdt.returnForCrdtAmt(mDataMap.get("order_code"), UpdateCustAmtInput.CurdFlag.R, returnMoney);
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
		}

		mWebResult.inOtherResult(rootResult);
		return mWebResult;
	}

	@Override
	public ConfigJobExec getConfig() {
		ConfigJobExec config = new ConfigJobExec();
		config.setExecType("449746990010");
		config.setMaxExecNumber(5);
		return config;
	}

}
