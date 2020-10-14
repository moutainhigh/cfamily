package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiGetCouponsForProductInput;
import com.cmall.familyhas.api.result.ApiGetCouponsForProductResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.model.CouponForGetInfo;
import com.cmall.ordercenter.service.CouponsService;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.VersionHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapweb.webapi.RootResultWeb;
import com.srnpr.zapweb.webmodel.MOauthInfo;
import com.srnpr.zapweb.websupport.OauthSupport;

/**
 * 查询商品可领取优惠券列表
 * @author Angel Joy
 *
 */
public class ApiGetCouponsForProduct extends RootApi<ApiGetCouponsForProductResult, ApiGetCouponsForProductInput> {

	@Override
	public ApiGetCouponsForProductResult Process(ApiGetCouponsForProductInput inputParam, MDataMap mRequestMap) {
		ApiGetCouponsForProductResult result = new ApiGetCouponsForProductResult();
		// 取渠道值
		JSONObject input = JSONObject.parseObject(mRequestMap.get("api_client"));
		String channelId="";
		String currentVersion = "";
		if(input!=null&&input.containsKey("channelId")) { 
			channelId = input.getString("channelId");
			currentVersion = input.getString("app_vision")==null?"":input.getString("app_vision");
			}
		String integralFlag = inputParam.getIntegralFlag();
		String fxFlag = inputParam.getFxFlag();
		if("1".equals(integralFlag)) {
			result.setResultCode(0);
			result.setResultMessage("积分商城不支持领券");
			return result;
		}
		CouponsService couponService = new CouponsService();
		MOauthInfo oauthInfo = null;
		String memberCode = "";//获取用户编号
		if (StringUtils.isNotEmpty(mRequestMap.get("api_token"))) {
			oauthInfo = new OauthSupport().upOauthInfo(mRequestMap.get("api_token"));
			if(oauthInfo != null) {
				memberCode = oauthInfo.getUserCode();
			}
		}
		String productCode = inputParam.getProductCode();//获取商品编号
		//根据商品编号查询该商品允许使用优惠券。
		/*channelId="449747430023";
		currentVersion="5.5.8";*/
		List<CouponForGetInfo> list = couponService.getCouponListForProduct(memberCode, productCode,fxFlag);
		List<String> couponNames = new ArrayList<String>();
		for(CouponForGetInfo info : list) {
			couponNames.add(info.getCouponTypeName());
			//时间处理，去掉时分秒,排除小程序渠道
			if(!"449747430023".equals(channelId)) {
				String startTime = info.getStartTime();
				String endTime = info.getEndTime();
				if(!StringUtils.isEmpty(startTime)) {
					startTime = startTime.substring(0, 10).replaceAll("-", ".");
				}
				if(!StringUtils.isEmpty(endTime)) {
					endTime = endTime.substring(0, 10).replaceAll("-", ".");
				}
				info.setStartTime(startTime);
				info.setEndTime(endTime);	
			}
			//如果是折扣券，处理面额
			String moneyType = info.getMoneyType();
			if("449748120002".equals(moneyType)) {
				BigDecimal money = info.getMoney();
				money = money.divide(new BigDecimal(10));
				info.setMoney(money);
			}
		}
		//小程序分销优惠券判断处理，前端劵显示重新处理
		result.setSystemTime(DateUtil.getSysDateTimeString());	
		if(StringUtils.isNotBlank(channelId)&&currentVersion.compareTo("5.5.7") >=0) {
			//&&currentVersion.compareTo("5.5.7") >=0
			RootResultWeb paramResult = new RootResultWeb(); 
			list = couponService.checkIfDistributionCouponNew(memberCode,productCode,list,channelId,paramResult,fxFlag); 
			if(list.size()>0&&"1".equals(list.get(0).getIfDistributionCoupon())) {
				List<String> subCouponNames = new ArrayList<String>();
				subCouponNames.add(list.get(0).getCouponTypeName());
				result.setIfDistributionCoupon("1");		
				result.setCouponNames(subCouponNames);
				result.setCouponForGetCount(list.size());
				result.setCouponForGetList(list);
				String couponMoneyStr = paramResult.getResultMessage();
				if(StringUtils.isNotBlank(couponMoneyStr)) {
					result.setFxActIsValid("1");
					result.setFxCouponMoney(BigDecimal.valueOf(Double.parseDouble(couponMoneyStr)));
				}
				return result;
			}else {
				//没有符合条件的分销券,恢复原有的时间格式显示
				couponNames = new ArrayList<String>();
				String couponMoneyStr = paramResult.getResultMessage();
				if(StringUtils.isNotBlank(couponMoneyStr)) {
					result.setFxActIsValid("1");
					result.setFxCouponMoney(BigDecimal.valueOf(Double.parseDouble(couponMoneyStr)));
				}
				for(CouponForGetInfo info : list) {
					couponNames.add(info.getCouponTypeName());
					String startTime = info.getStartTime();
					String endTime = info.getEndTime();
					if(!StringUtils.isEmpty(startTime)) {
						startTime = startTime.substring(0, 10).replaceAll("-", ".");
					}
					if(!StringUtils.isEmpty(endTime)) {
						endTime = endTime.substring(0, 10).replaceAll("-", ".");
					}
					info.setStartTime(startTime);
					info.setEndTime(endTime);
				}
			}
		}
		result.setCouponNames(couponNames);
		result.setCouponForGetList(list);
		result.setCouponForGetCount(list.size());
		return result;
	}
	
	

}
