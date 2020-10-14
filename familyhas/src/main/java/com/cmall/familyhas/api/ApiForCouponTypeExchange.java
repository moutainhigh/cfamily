package com.cmall.familyhas.api;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.familyhas.api.input.ApiForCouponTypeExchangeInput;
import com.cmall.familyhas.api.result.APiForCouponTypeExchangeResult;
import com.cmall.ordercenter.util.CouponUtil;
import com.srnpr.xmassystem.invoke.ref.CustRelAmtRef;
import com.srnpr.xmassystem.invoke.ref.model.GetCustAmtResult;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.load.LoadCouponActivity;
import com.srnpr.xmassystem.load.LoadCouponType;
import com.srnpr.xmassystem.modelevent.PlusModelCouponActivity;
import com.srnpr.xmassystem.modelevent.PlusModelCouponType;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 积分兑换优惠券
 */
public class ApiForCouponTypeExchange extends RootApiForToken<APiForCouponTypeExchangeResult, ApiForCouponTypeExchangeInput>{

	LoadCouponActivity loadCouponActivity = new LoadCouponActivity();
	LoadCouponType loadCouponType = new LoadCouponType();
	PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
	
	@Override
	public APiForCouponTypeExchangeResult Process(ApiForCouponTypeExchangeInput inputParam, MDataMap mRequestMap) {
		APiForCouponTypeExchangeResult result = new APiForCouponTypeExchangeResult();
		result.setCouponTypeCode(inputParam.getCouponTypeCode());
		
		PlusModelCouponType couponType = loadCouponType.upInfoByCode(new PlusModelQuery(inputParam.getCouponTypeCode()));
		if(StringUtils.isBlank(couponType.getActivityCode())) {
			result.setResultCode(0);
			result.setResultMessage("优惠券不存在");
			return result;
		}
		
		if(!couponType.getUid().equalsIgnoreCase(inputParam.getUid())) {
			result.setResultCode(0);
			result.setResultMessage("非法的请求参数");
			return result;
		}
		
		PlusModelCouponActivity couponActivity = loadCouponActivity.upInfoByCode(new PlusModelQuery(couponType.getActivityCode()));
		
		// 优惠券未发布
		if(!couponType.getStatus().equals("4497469400030002") || couponActivity.getFlag() != 1) {
			result.setResultCode(0);
			result.setResultMessage("优惠券暂不可兑换~");
			return result;
		}
		
		// 检查是否是系统发放，以及兑换类型是否是积分兑换
		if(!"4497471600390002".equals(couponType.getExchangeType()) || !"4497471600060002".equals(couponActivity.getProvideType())) {
			result.setResultCode(0);
			result.setResultMessage("优惠券暂不可兑换~~");
			return result;
		}
		
		// 检查设置的积分是否大于0
		int exchangeValue = NumberUtils.toInt(couponType.getExchangeValue());
		if(exchangeValue <= 0) {
			result.setResultCode(0);
			result.setResultMessage("优惠券暂不可兑换~~~");
			return result;
		}
		
		String custId = plusServiceAccm.getCustId(getUserCode());
		if(StringUtils.isBlank(custId)) {
			result.setResultCode(0);
			result.setResultMessage("抱歉，您的积分余额不足");
			return result;
		}
		
		BigDecimal accmMoney = plusServiceAccm.accmAmtToMoney(new BigDecimal(exchangeValue));
		GetCustAmtResult custAmtResult = plusServiceAccm.getPlusModelCustAmt(custId);
		if(custAmtResult.getPossAccmAmt().compareTo(accmMoney) < 0) {
			result.setResultCode(0);
			result.setResultMessage("抱歉，您的积分余额不足");
			return result;
		}
		
		// 检查是否已经兑换过了
		if(DbUp.upTable("oc_coupon_info").count("coupon_type_code", inputParam.getCouponTypeCode(), "member_code", getUserCode()) > 0) {
			result.setResultCode(3);
			result.setResultMessage("您已经兑换过此优惠券了");
			return result;
		}
		
		// 是否已经兑完了
		if(DbUp.upTable("oc_coupon_type").count("coupon_type_code", inputParam.getCouponTypeCode(), "surplus_money", "0") > 0) {
			result.setResultCode(2);
			result.setResultMessage("抱歉，优惠券抢光了");
			return result;
		}
		
		String lockKey = KvHelper.lockCodes(10, "ApiForCouponTypeExchange-"+getUserCode());
		if(StringUtils.isBlank(lockKey)) {
			result.setResultCode(0);
			result.setResultMessage("您的操作太快了");
			return result;
		}
		
		RootResult updateRes = updateCustAmt(accmMoney, inputParam.getCouponTypeCode(), custId);
		if(updateRes.getResultCode() != 1){
			KvHelper.unLockCodes("ApiForCouponTypeExchange-"+getUserCode(), lockKey);
			result.setResultCode(0);
			result.setResultMessage("兑换失败，请稍后再试");
			
			// 转换特定的提示内容
			if(updateRes.getResultCode() == 99) {
				if(updateRes.getResultMessage().contains("客户积分不足")) {
					result.setResultMessage("抱歉，您的积分余额不足");
				} else {
					result.setResultMessage(updateRes.getResultMessage());
				}
			}
			return result;
		}
		
		// 记录积分变更日志
		MDataMap changeDataMap = new MDataMap();
		changeDataMap.put("member_code", getUserCode());
		changeDataMap.put("cust_id", custId);
		changeDataMap.put("change_type", "449748080009"); // 积分兑换优惠券
		changeDataMap.put("change_money", accmMoney.toString());
		changeDataMap.put("remark", inputParam.getCouponTypeCode());
		changeDataMap.put("create_time", FormatHelper.upDateTime());
		DbUp.upTable("mc_member_integral_change").dataInsert(changeDataMap);
		
		try {
			new CouponUtil().provideCoupon(getUserCode(), inputParam.getCouponTypeCode(), "0", "","",1);
		} catch (Exception e) {
			result.setResultCode(0);
			result.setResultMessage("兑换操作异常，如果您的积分已经扣除请联系客服处理");
		}
		
		KvHelper.unLockCodes("ApiForCouponTypeExchange-"+getUserCode(), lockKey);
		
		return result;
	}
	
	// 扣减积分
	private RootResult updateCustAmt(BigDecimal accmMoney, String couponTypeCode, String custId) {
		CustRelAmtRef custRelAmtRef = (CustRelAmtRef)BeansHelper.upBean(CustRelAmtRef.NAME);
		
		UpdateCustAmtInput custInput = new UpdateCustAmtInput();   // 占用
		UpdateCustAmtInput.ChildOrder childOrder = new UpdateCustAmtInput.ChildOrder();
		
		childOrder.setChildAccmAmt(accmMoney);
		childOrder.setAppChildOrdId(couponTypeCode);
		custInput.getOrderList().add(childOrder);
		
		custInput.setBigOrderCode(couponTypeCode);
		custInput.setCustId(custId);
		custInput.setCurdFlag(UpdateCustAmtInput.CurdFlag.JFU);
		return custRelAmtRef.updateCustAmt(custInput);
	}

	
}
