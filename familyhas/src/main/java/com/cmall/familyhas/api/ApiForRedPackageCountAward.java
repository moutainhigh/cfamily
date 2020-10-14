package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForRedPackageAwardCountInput;
import com.cmall.familyhas.api.result.ApiForRedPackageCountAwardResult;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.ordercenter.util.CouponUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapdata.helper.KvHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webmodel.MOauthInfo;

/**
 * 用户抽奖结果接口
 * @author Angel Joy
 *
 */
public class ApiForRedPackageCountAward extends RootApiForVersion<ApiForRedPackageCountAwardResult , ApiForRedPackageAwardCountInput > {

	@Override
	public ApiForRedPackageCountAwardResult Process(ApiForRedPackageAwardCountInput inputParam, MDataMap mRequestMap) {
		ApiForRedPackageCountAwardResult result = new ApiForRedPackageCountAwardResult();
		String eventCode = inputParam.getEventCode();//获取活动编号
		boolean flag = getFlagLogin();
		if(!flag) {
			result.setResultCode(2);
			result.setResultMessage("红包已抢光");
			return result;
		}
		MOauthInfo mOauthInfo =  getOauthInfo();
		String userCode = mOauthInfo.getUserCode();
		Integer count = inputParam.getCount(); 
		String sql = "SELECT * FROM systemcenter.sc_hudong_event_jx_info WHERE event_code = '"+eventCode+"' AND jx_status = '4497472700020002' AND begin_time <= sysdate() AND end_time >= sysdate() AND is_delete = '0' LIMIT 1";
		Map<String,Object> jpInfo = DbUp.upTable("sc_hudong_event_jx_info").dataSqlOne(sql, null);
		if(jpInfo == null) {//当前无活动
			result.setResultCode(2);
			result.setResultMessage("当前暂无活动");
			return result;
		}
		String personLimitNum = jpInfo.get("person_limit_num")!=null?jpInfo.get("person_limit_num").toString():"0";
		Integer limitTimes = Integer.parseInt(personLimitNum);
		String jpCode = jpInfo.get("jx_code")!= null ? jpInfo.get("jx_code").toString():"";
		if(StringUtils.isEmpty(jpCode)) {
			result.setResultCode(2);
			result.setResultMessage("当前暂无活动");
			return result;
		}
		String lockKey = KvHelper.lockCodes(5, "ApiForRedPackageAward-"+userCode);
		if(StringUtils.isBlank(lockKey)) {
			result.setResultCode(-1);
			result.setResultMessage("请勿频繁操作，请稍后再试");
			return result;
		}
		//查询当前用户中奖次数
		Integer countLog = DbUp.upTable("lc_red_package_get_log").count("member_code",userCode,"jx_code",jpCode);
		if(countLog >= limitTimes) {//用户次数耗光
			result.setStatus("2");
			result.setResultMessage("您次数已经用完");
			result.setTimes(0);
			return result;
		}else {//还可以抽奖，需要根据概率换算中奖率
			String couponTypeCode = this.awardForUser(jpCode,userCode,count);//实际抽奖方法，抽中返回抽中优惠券类型编号，抽不中返回值为空
			if(StringUtils.isEmpty(couponTypeCode)) {
				result.setStatus("3");
				result.setResultMessage("很遗憾，您本次未中奖");
			}else if("wdb".equals(couponTypeCode)){
//				this.setPro(couponTypeCode, result);//给结果赋值方法
				result.setStatus("4");
				result.setResultMessage("您数的次数太少了，请再接再厉");
			}else {
				this.setPro(couponTypeCode, result);//给结果赋值方法
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
			result.setTimes(limitTimes - (countLog +1));
		}
		KvHelper.unLockCodes("ApiForRedPackageAward-"+userCode, lockKey);
		return result;
	}
	
	/**
	 * 用户抽奖逻辑
	 * @param jpCode 奖项编号
	 * @param userCode 用户编号
	 * @param count 数红包次数
	 * @return
	 */
	private String awardForUser(String jpCode, String userCode,Integer count) {
		String couponTypeCode = "";//未达标
		//获取当前奖项下所有的红包，包括未中奖红包（空包）
		List<MDataMap> list = DbUp.upTable("sc_hudong_event_hongbao_info").query("", "award_probability desc", "event_code =:event_code", new MDataMap("event_code",jpCode), -1, -1);
		if(list == null || list.size() == 0) {
			return "";
		}
		//校验是否满足抽奖条件
		String checkMinSql = "SELECT MIN(award_probability) mincount FROM systemcenter.sc_hudong_event_hongbao_info WHERE event_code = '"+jpCode+"'";
		Map<String,Object> min =  DbUp.upTable("sc_hudong_event_hongbao_info").dataSqlOne(checkMinSql, null);
		if(min != null && !min.isEmpty()) {
			String minCountStr = min.get("mincount")!=null?min.get("mincount").toString():"0";
			Integer minCount = Integer.parseInt(minCountStr);
			if(count < minCount) {//
				return "wdb";//未达标
			}
		}
		//根据用户数红包次数决定用户获得哪个红包。
		for(MDataMap map : list) {
			Integer allowCount = Integer.parseInt(StringUtils.isEmpty(map.get("award_probability"))?"0":map.get("award_probability"));
			if(count >= allowCount) {
				couponTypeCode = map.get("event_award");
				//校验是否还有剩余有优惠券。
				String  sqlCount = "SELECT COUNT(1) num FROM ordercenter.oc_coupon_type WHERE coupon_type_code = '"+couponTypeCode+"' and surplus_money <= 0";
				Map<String,Object> mapCount = DbUp.upTable("oc_coupon_type").dataSqlOne(sqlCount, null);
				Integer count2 = 0;
				if(mapCount != null && mapCount.get("num") != null) {
					count2 = Integer.parseInt(mapCount.get("num").toString());
				}
				if(count2 <= 0) {//优惠券还有剩余
					break;
				}
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
	private void setPro(String couponTypeCode,ApiForRedPackageCountAwardResult result) {
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
