package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGrantCouponsRemindInput;
import com.cmall.familyhas.api.result.ApiGrantCouponsRemindResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.model.CouponInfo;
import com.cmall.ordercenter.util.CouponUtil;
import com.srnpr.xmassystem.load.LoadCouponType;
import com.srnpr.xmassystem.modelevent.PlusModelCouponType;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 优惠券发放提醒接口<br/>
 * 仅APP使用
 * @remark 
 * @author 任宏斌
 * @date 2019年11月18日
 */
public class ApiGrantCouponsRemind extends
		RootApiForToken<ApiGrantCouponsRemindResult, ApiGrantCouponsRemindInput> {

	CouponUtil couponUtil = new CouponUtil();
	
	LoadCouponType loadCouponType = new LoadCouponType();
	
	public ApiGrantCouponsRemindResult Process(ApiGrantCouponsRemindInput inputParam,
			MDataMap mRequestMap) {
		ApiGrantCouponsRemindResult result = new ApiGrantCouponsRemindResult();
		
		String member_code = getUserCode();
		String closeTerminalTime = inputParam.getClose_terminal_time();
		
		//设备上次关闭时间不能为空
		if(StringUtils.isEmpty(closeTerminalTime)) return result;
		
		String nowTime = DateUtil.getSysDateTimeString();
		
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("member_code", member_code);
		mWhereMap.put("manage_code", getManageCode());
		mWhereMap.put("sysTime", nowTime);
		mWhereMap.put("beginTime", closeTerminalTime);
		
		String sql = "select ci.coupon_type_code,ci.activity_code,ci.coupon_code,ci.surplus_money,ci.status,ci.end_time,ci.limit_money,ci.start_time,ci.initial_money,ci.is_see,ci.blocked,ci.big_order_code,ct.money_type,ct.creater,ct.status ct_status,oa.flag,ct.limit_condition,oa.is_change ";
		sql	+= " from ordercenter.oc_coupon_info ci,ordercenter.oc_coupon_type ct,ordercenter.oc_activity oa";
		sql += " where ci.coupon_type_code = ct.coupon_type_code and ci.activity_code=oa.activity_code and ci.member_code=:member_code and ci.manage_code=:manage_code";
		sql += " and (oa.provide_type='4497471600060002')"; //发放类型 仅支持系统发放
		sql += " and (ct.creater!='ld' or ( ct.status='4497469400030002' and oa.flag=1 and ci.out_coupon_code!='' and ci.out_coupon_code is not null))"; //兼容优惠券一体化的券
		sql += " and (DATE_FORMAT(ci.end_time,'%Y-%m-%d %H:%i:%s')>:sysTime AND ci.status = 0 AND is_see = 0)"; //未过期 未使用 未查看
		sql += " and (DATE_FORMAT(ci.create_time,'%Y-%m-%d %H:%i:%s') BETWEEN :beginTime AND :sysTime)"; //券的创建时间在时间段内
		sql += " order by ci.update_time desc";
		
		List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_coupon_info").dataSqlList(sql, mWhereMap);
		
		//565新人券提示
		Map<String, Object> tMap = DbUp.upTable("oc_coupon_relative").dataSqlOne("SELECT cr.activity_code from oc_coupon_relative cr where cr.manage_code = 'SI2003' and cr.relative_type = 1", null);
		String tcode = tMap.get("activity_code")==null?"":tMap.get("activity_code").toString();
		
		double deadlineDay = Double.parseDouble(StringUtils.isEmpty(bConfig("ordercenter.COUPON_DEADLINE_DAY")) ? "2.0" : bConfig("ordercenter.COUPON_DEADLINE_DAY"));
		PlusModelCouponType modelCouponType;
		for (Map<String, Object> maps : dataSqlList) {
			CouponInfo couponInfo = new CouponInfo();
			couponInfo.setIsSee(maps.get("is_see")+"");//是否已经查看
			couponInfo.setCouponCode(maps.get("coupon_code")+"");
			couponInfo.setInitialMoney(new BigDecimal(maps.get("initial_money")+""));
			couponInfo.setSurplusMoney(new BigDecimal(maps.get("surplus_money")+""));
			couponInfo.setStatus(Integer.parseInt(maps.get("status")+""));
			couponInfo.setEndTime(maps.get("end_time")+"");
			couponInfo.setStartTime(maps.get("start_time")+"");
			couponInfo.setLimitMoney(new BigDecimal(maps.get("limit_money")+""));
			couponInfo.setUseLimit(bConfig("familyhas.coupon_limit"));
			couponInfo.setMoneyType(maps.get("money_type")+"");
			couponInfo.setIs_change(maps.get("is_change")+"");
			couponInfo.setActivityCode(maps.get("activity_code")+"");
			couponUtil.convertMoneyShow(couponInfo);
			
			//542版本返回优惠券类型编号
			String coupon_type_code = maps.get("coupon_type_code")+"";
			couponInfo.setCouponTypeCode(coupon_type_code);
			/**
			 * 542增加限制条件
			 * 优惠券类型定义    使用下限金额     限制条件
			 * 无门槛                  0                       无限制
			 * 无金额限制           0                       指定
			 * 满X元可用             X             ——
			 */
			String limitCondition = "";
			if("0".equals(maps.get("limit_money")+"")) {
				if("4497471600070001".equals(maps.get("limit_condition"))) {
					limitCondition = bConfig("familyhas.no_threshold");
				}
				if("4497471600070002".equals(maps.get("limit_condition"))) {
					limitCondition = bConfig("familyhas.unlimited_amount");
				}
			}else {
				limitCondition = FormatHelper.formatString(bConfig("familyhas.with_x_available"),maps.get("limit_money")+"");
			}
			couponInfo.setLimitCondition(limitCondition);
			
			modelCouponType = loadCouponType.upInfoByCode(new PlusModelQuery(maps.get("coupon_type_code")+""));
			
			if(modelCouponType == null || StringUtils.isBlank(modelCouponType.getCouponTypeCode())) {
				continue;
			}
			
			couponInfo.setLimitExplain(modelCouponType.getLimitExplain());
			if(StringUtils.isNotBlank(modelCouponType.getLimitScope())){
				couponInfo.setUseLimit(modelCouponType.getLimitScope());
			}
			
			if ("4497471600070002".equals(modelCouponType.getLimitCondition())
					&& "4497471600070002".equals(modelCouponType.getCouponTypeLimit().getChannelLimit())) {
				//按渠道过滤 
				String channelCodeInput = StringUtils.isEmpty(inputParam.getChannel_id()) ? "" : inputParam.getChannel_id();
				String channel_codes = modelCouponType.getCouponTypeLimit().getChannelCodes();
				boolean limitChannel = true;
				// 如果传入的使用渠道不在指定限制的渠道内
				for (String channelCode : channel_codes.split(",")) {
					if (channelCode.equals(channelCodeInput)) {
						limitChannel = false;
						break;
					}
				}
				if (limitChannel) {
					continue;
				}
			}
			
			String actionValue = modelCouponType.getActionValue();
			String actionType = modelCouponType.getActionType();
			
			// 指定了商品限制的优惠券可以跳转到地址
			if("4497471600070002".equals(modelCouponType.getLimitCondition())
					&& "4497471600070002".equals(modelCouponType.getCouponTypeLimit().getProductLimit())
					&& 0 == modelCouponType.getCouponTypeLimit().getExceptProduct()
					&& StringUtils.isNotBlank(actionValue)) {
				couponInfo.setActionType(actionType);
				couponInfo.setActionValue(actionValue);
			}
			
			// 已过期优惠券判断 : 未使用 且 结束时间小于当前时间
			if(couponInfo.getStatus() == 0 && couponInfo.getEndTime().compareTo(nowTime) < 0) {
				couponInfo.setStatus(2);
			}
			
			//判断增加未激活标志，只对未使用的做判断。如果优惠券未使用且已过期则优先展示为已过期
			if(couponInfo.getStatus() == 0 && StringUtils.isNotEmpty(maps.get("blocked")+"") && maps.get("blocked").equals("1")) {
				couponInfo.setStatus(5);
			}
			
			//判断增加送此优惠券的大订单号
			if(StringUtils.isNotEmpty(maps.get("big_order_code")+"")) {
				couponInfo.setBigOrderCode(maps.get("big_order_code")+"");
			}
			
			//判断增加即将过期天数
			String endTime = maps.get("end_time").toString();
			try {
				
				double diffDay = DateHelper.daysBetween(DateHelper.upNow(), endTime);
				if(diffDay>=1.0 && diffDay <= deadlineDay) {
					couponInfo.setDeadline("还剩" + (new Double(diffDay).intValue() + 1) + "天");
				} else if(diffDay >= 0.0 && diffDay < 1.0) {
					int diffHour = DateHelper.hoursBetween(DateHelper.upNow(), endTime);
					if(diffHour == 0) {
						diffHour = 1;
					}
					couponInfo.setDeadline("还剩" + diffHour + "小时");
				}
				//542版本增加快过期标签
				if(diffDay>=0 && diffDay<=2.0) {
					couponInfo.setIsShowDue("1");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			result.getCouponInfoList().add(couponInfo);
			//565新人券提示
			if(couponInfo.getActivityCode().equals(tcode)){
				result.setContainXrq("Y");
			}
		}
		
		couponUtil.setCouponInfoSee(result.getCouponInfoList());//设置未查看为已查看
		
		result.setTotal(result.getCouponInfoList().size());
		
		return result;
	}
	
}
