package com.cmall.familyhas.job;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.JobExecutionContext;

import com.cmall.groupcenter.homehas.HomehasSupport;
import com.cmall.ordercenter.common.DateUtil;
import com.cmall.ordercenter.util.CouponUtil;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 根据家有回传的订单数据定时发送折扣券
 */
public class JobForLDOrderCoupon extends RootJob {
	
	CouponUtil couponUtil = new CouponUtil();
	
	public void doExecute(JobExecutionContext context) {
		// 只查询3天内的任务
		MDataMap paramMap = new MDataMap();
		paramMap.put("status", "0");
		List<MDataMap> taskDataList = DbUp.upTable("oc_order_ld_coupon_task").query("zid,phone,member_code,sell_price,ld_order_code,product_count,exec_num,create_time", "", "status = :status and exec_num < 3 and create_time > DATE_SUB(NOW(),INTERVAL 3 DAY)", paramMap, 0, 1000);

		// 折扣卷的相关配置
		List<MDataMap> defList = DbUp.upTable("zw_define").queryByWhere("parent_did","469923280003");
		Map<String,String> defMap = new HashMap<String,String>();
		for(MDataMap map : defList){
			defMap.put(map.get("define_name"), map.get("define_remark"));
		}
		
		RootResult result = null;
		for(MDataMap m : taskDataList){
			result = hanlder(m,defMap);
			
			if(result.getResultCode() == 1){
				m.put("coupon_type", result.getResultMessage());
				m.put("status", "1");
				m.put("remark", "");
			}else{
				m.put("coupon_type", "");
				m.put("status", "0");
				m.put("remark", result.getResultMessage());
			}
			
			m.put("update_time", FormatHelper.upDateTime());
			m.put("exec_num", Integer.parseInt(m.get("exec_num"))+1+"");
			
			DbUp.upTable("oc_order_ld_coupon_task").dataUpdate(m, "coupon_type,status,remark,update_time,exec_num", "zid");
		}
	}
	
	private RootResult hanlder(MDataMap taskItem,Map<String,String> defMap){
		String phone = taskItem.get("phone");
		String createTime = taskItem.get("create_time");
		String ldOrderCode = taskItem.get("ld_order_code");
		BigDecimal sellPrice = new BigDecimal(taskItem.get("sell_price"));
		int productCount = Integer.parseInt(taskItem.get("product_count"));
		
		RootResult result = new RootResult();
		
		// 三档价格区间
		String v1Price = StringUtils.trimToEmpty(defMap.get("coupon_def_v1_price"));
		String v2Price = StringUtils.trimToEmpty(defMap.get("coupon_def_v2_price"));
		String v3Price = StringUtils.trimToEmpty(defMap.get("coupon_def_v3_price"));
		// 三档优惠券编码
		String v1Code = StringUtils.trimToEmpty(defMap.get("coupon_def_v1_code"));
		String v2Code = StringUtils.trimToEmpty(defMap.get("coupon_def_v2_code"));
		String v3Code = StringUtils.trimToEmpty(defMap.get("coupon_def_v3_code"));
		
		String couponTypeCode = "";
		
		// 三档优先顺序   v3 > v2 > v1
		if(isContain(v3Price, sellPrice)){
			couponTypeCode = v3Code;
		}else if(isContain(v2Price, sellPrice)){
			couponTypeCode = v2Code;
		}else if(isContain(v1Price, sellPrice)){
			couponTypeCode = v1Code;
		}
		
		if(StringUtils.isBlank(couponTypeCode)){
			result.setResultCode(0);
			result.setResultMessage("未能匹配到优惠券类型编码");
			return result;
		}
		
		// 忽略不是手机号的情况
		if(!phone.matches("1\\d{10}")){
			result.setResultCode(0);
			result.setResultMessage("不正确的手机号");
			return result;
		}
		
		String memberCode = getMemberCode(phone);
		if(StringUtils.isBlank(memberCode)){
			result.setResultCode(0);
			result.setResultMessage("手机号未注册");
			return result;
		}
		
		MDataMap couponTypeMap = DbUp.upTable("oc_coupon_type").one("coupon_type_code",couponTypeCode);
		if(couponTypeMap == null) {
			result.setResultCode(0);
			result.setResultMessage("优惠券类型不存在："+couponTypeCode);
			return result;
		}
		
		// 判断活动是否有效
		MDataMap couponActMap = DbUp.upTable("oc_activity").one("activity_code",couponTypeMap.get("activity_code"));
		if(couponActMap != null){
			if(DateUtil.compareTime(couponActMap.get("begin_time"), createTime, "yyyy-MM-dd HH:mm:ss") > 0
					|| DateUtil.compareTime(couponActMap.get("end_time"), createTime, "yyyy-MM-dd HH:mm:ss") < 0){
				result.setResultCode(0);
				result.setResultMessage("不在优惠券活动有效时间内："+couponTypeCode);
				return result;
			}
			
			if(!"1".equals(couponActMap.get("flag"))){
				result.setResultCode(0);
				result.setResultMessage("此优惠券活动未启用："+couponTypeCode);
				return result;
			}
		}
		
		result = couponUtil.provideCoupon(memberCode, couponTypeCode, "0", ldOrderCode, "", productCount);
		
		if(result.getResultCode() == 1){
			result.setResultMessage(couponTypeCode);
		}
		return result;
	}
	
	/**
	 * 是否在价格区间之内<br>
	 * @param priceRange  格式如： 100-500(大于等于100，小于500)
	 * @param sellPrice
	 * @return
	 */
	private boolean isContain(String priceRange, BigDecimal sellPrice){
		// 前补0
		if(priceRange.startsWith("-")){
			priceRange = "0"+priceRange;
		}
		
		// 后补默认最大值
		if(priceRange.endsWith("-")){
			priceRange = priceRange+Integer.MAX_VALUE;
		}
		
		String[] vs = priceRange.split("-");
		if(vs.length != 2) return false;
		if(StringUtils.isBlank(vs[0]) && StringUtils.isBlank(vs[1])) return false;
		
		int min = NumberUtils.toInt(vs[0]);
		int max = NumberUtils.toInt(vs[1]);
		return sellPrice.intValue() >= min && sellPrice.intValue() < max;
	}
	
	/**
	 * 根据手机号查询用户编号，如果未注册则自动注册一个
	 * @param phone
	 * @return
	 */
	private String getMemberCode(String phone){
		MDataMap loginInfo = DbUp.upTable("mc_login_info").one("manage_code","SI2003","login_name",phone);
		if(loginInfo != null) return loginInfo.get("member_code");
		MWebResult result = new HomehasSupport().registerWithResult(phone, RandomStringUtils.randomNumeric(8));
		if(result.getResultCode() != 1) return "";
		return ((MDataMap)result.getResultObject()).get("memberCode");
	}
}
