package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForRedPackageAwardInput;
import com.cmall.familyhas.api.result.ApiForRedPackageAwardResult;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.ordercenter.util.CouponUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 用户抽奖结果接口
 * @author Angel Joy
 *
 */
public class ApiForRedPackageAward extends RootApiForToken<ApiForRedPackageAwardResult , ApiForRedPackageAwardInput > {

	@Override
	public ApiForRedPackageAwardResult Process(ApiForRedPackageAwardInput inputParam, MDataMap mRequestMap) {
		ApiForRedPackageAwardResult result = new ApiForRedPackageAwardResult();
		String eventCode = inputParam.getEventCode();//获取活动编号
		String userCode = getUserCode();
		String sql = "SELECT * FROM systemcenter.sc_hudong_event_jx_info WHERE event_code = '"+eventCode+"' AND begin_time <= sysdate() AND end_time >= sysdate() AND is_delete = '0' LIMIT 1";
		Map<String,Object> jpInfo = DbUp.upTable("sc_hudong_event_jx_info").dataSqlOne(sql, null);
		if(jpInfo == null) {//当前无活动
			result.setResultCode(-1);
			result.setResultMessage("当前暂无活动");
			return result;
		}
		String personLimitNum = jpInfo.get("person_limit_num")!=null?jpInfo.get("person_limit_num").toString():"0";
		Integer limitTimes = Integer.parseInt(personLimitNum);
		//查询当前用户中奖次数
		String jpCode = jpInfo.get("jx_code")!= null ? jpInfo.get("jx_code").toString():"";
		if(StringUtils.isEmpty(jpCode)) {
			result.setResultCode(-1);
			result.setResultMessage("当前暂无活动");
			return result;
		}
		String lockKey = KvHelper.lockCodes(10, "ApiForRedPackageAward-"+getUserCode());
		if(StringUtils.isBlank(lockKey)) {
			result.setResultCode(-1);
			result.setResultMessage("请勿频繁操作，请稍后再试");
			return result;
		}
		Integer count = DbUp.upTable("lc_red_package_get_log").count("member_code",userCode,"jx_code",jpCode);
		if(count >= limitTimes) {//用户已经中过奖了，需要返回之前中奖信息
			String sqlAward = "SELECT * FROM logcenter.lc_red_package_get_log WHERE jx_code = '"+jpCode+"' AND member_code = '"+userCode+"' ORDER BY create_time DESC LIMIT 1";
			Map<String,Object> logInfo = DbUp.upTable("lc_red_package_get_log").dataSqlOne(sqlAward, null);
			if(logInfo == null) {
				result.setResultCode(-1);
				result.setResultMessage("系统异常");
				KvHelper.unLockCodes("ApiForRedPackageAward-"+getUserCode(), lockKey);
				return result;
			}
			String couponTypeCode = logInfo.get("coupon_type_code") != null?logInfo.get("coupon_type_code").toString():"";
			if(StringUtils.isEmpty(couponTypeCode)) {
				result.setResultMessage("很遗憾，您本次未中奖");
				result.setStatus("3");
			}else {
				this.setPro(couponTypeCode, result);//返回结果赋值
				result.setStatus("2");
				result.setResultMessage("你已经参加过本次活动");
			}
		}else {//还可以抽奖，需要根据概率换算中奖率
			String couponTypeCode = this.awardForUser(jpCode,userCode);//实际抽奖方法，抽中返回抽中优惠券类型编号，抽不中返回值为空
			if(StringUtils.isEmpty(couponTypeCode)) {
				result.setStatus("3");
				result.setResultMessage("很遗憾，您本次未中奖");
			}else {
				this.setPro(couponTypeCode, result);//给结果辅助方法
				result.setStatus("1");
				result.setResultMessage("恭喜您！！！");
			}
			//插入中奖记录表
			MDataMap logMap = new MDataMap();
			logMap.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
			logMap.put("member_code", userCode);
			logMap.put("jx_code", jpCode);
			logMap.put("coupon_type_code", couponTypeCode);
			logMap.put("create_time", DateUtil.getSysDateTimeString());
			DbUp.upTable("lc_red_package_get_log").dataInsert(logMap);
			
		}
		KvHelper.unLockCodes("ApiForRedPackageAward-"+getUserCode(), lockKey);
		return result;
	}
	
	/**
	 * 用户抽奖逻辑
	 * @param jpCode
	 * @param userCode
	 * @return
	 */
	private String awardForUser(String jpCode, String userCode) {
		String couponTypeCode = "";
		//获取当前奖项下所有的红包，包括未中奖红包（空包）
		List<MDataMap> list = DbUp.upTable("sc_hudong_event_hongbao_info").query("", "award_probability", "event_code =:event_code", new MDataMap("event_code",jpCode), -1, -1);
		if(list == null || list.size() == 0) {
			return "";
		}
		BigDecimal total = new BigDecimal(0);
		List<Map<String,Object>> codeRate = new ArrayList<Map<String,Object>>();
		for(MDataMap map : list) {
			Map<String,Object> rateMap = new HashMap<String,Object>();
			if(map == null || map.isEmpty()) {
				continue;
			}
			String eventAward = map.get("event_award");
			if(StringUtils.isEmpty(eventAward)) {//空包
				rateMap.put("start", total);
				total = total.add(new BigDecimal(map.get("award_probability")));
				rateMap.put("end", total);
				rateMap.put("code", eventAward);
				codeRate.add(rateMap);
			}else {
				//需要查询当前优惠券是否还有剩余，有则参与抽奖，没有则剔除奖池。
				// 是否已经兑完了
				String  sqlCount = "SELECT COUNT(1) num FROM ordercenter.oc_coupon_type WHERE coupon_type_code = '"+eventAward+"' and surplus_money <= 0";
				Map<String,Object> mapCount = DbUp.upTable("oc_coupon_type").dataSqlOne(sqlCount, null);
				Integer count2 = 0;
				if(mapCount != null && mapCount.get("num") != null) {
					count2 = Integer.parseInt(mapCount.get("num").toString());
				}
				if(count2 <= 0) {
					rateMap.put("start", total);
					total = total.add(new BigDecimal(map.get("award_probability")));
					rateMap.put("end", total);
					rateMap.put("code",eventAward);
					codeRate.add(rateMap);
				}
			}
		}
		Integer sum = total.intValue();
		//随机一个1~sum之间的整数。
		Random random = new Random();
		int randint =(int)Math.floor((random.nextDouble()*sum));
		//根据随机数取中奖优惠券类型。
		for(Map<String,Object> map : codeRate) {
			BigDecimal start = (BigDecimal)map.get("start");
			BigDecimal end = (BigDecimal)map.get("end");
			String awardCode = map.get("code")!=null?map.get("code").toString():"";
			if(randint > start.intValue() && randint <= end.intValue()) {
				couponTypeCode = awardCode;
				break;
			}
			
		}
		if(StringUtils.isNotEmpty(couponTypeCode)) {//去发放优惠券
			String  sqlCount = "SELECT COUNT(1) num FROM ordercenter.oc_coupon_type WHERE coupon_type_code = '"+couponTypeCode+"' and surplus_money <= 0";
			Map<String,Object> mapCount = DbUp.upTable("oc_coupon_type").dataSqlOne(sqlCount, null);
			Integer count2 = 0;
			if(mapCount != null && mapCount.get("num") != null) {
				count2 = Integer.parseInt(mapCount.get("num").toString());
			}
			if(count2 <= 0) {//再次校验优惠券是否有剩余
				RootResult result = new CouponUtil().provideCoupon(userCode, couponTypeCode, "0", "","",1);
				if(result.getResultCode() == 0) {//发放失败
					couponTypeCode = "";
				}
			}else {
				couponTypeCode = "";
			}
		}
		return couponTypeCode;
	}
	
	/**
	 * 给结果赋值
	 * @param couponTypeCode
	 * @param result
	 */
	private void setPro(String couponTypeCode,ApiForRedPackageAwardResult result) {
		MDataMap couponInfo = DbUp.upTable("oc_coupon_type").one("coupon_type_code",couponTypeCode);
		if(couponInfo == null || couponInfo.isEmpty() ) {
			result.setResultCode(-1);
			result.setResultMessage("系统异常");
		}else {
			result.setCouponCode(couponTypeCode);
			result.setCouponTypeName(couponInfo.get("coupon_type_name"));
			result.setLimitMoney(couponInfo.get("limit_money"));
			result.setLimitScope(couponInfo.get("limit_scope"));
			result.setMoney(couponInfo.get("money"));
			result.setMoneyType(couponInfo.get("money_type"));
		}
	}
}
