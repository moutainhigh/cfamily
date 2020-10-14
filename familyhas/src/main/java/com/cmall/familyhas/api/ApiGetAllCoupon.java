package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetAllCouponInput;
import com.cmall.familyhas.api.result.ApiGetAllCouponResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.model.MPageData;
import com.cmall.groupcenter.model.PageOption;
import com.cmall.groupcenter.util.DataPaging;
import com.cmall.ordercenter.model.CouponInfo;
import com.cmall.ordercenter.util.CouponUtil;
import com.srnpr.xmassystem.load.LoadCouponType;
import com.srnpr.xmassystem.modelevent.PlusModelCouponType;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 全部优惠劵接口
 * 
 * @author liqt
 * 
 */
public class ApiGetAllCoupon extends
		RootApiForToken<ApiGetAllCouponResult, ApiGetAllCouponInput> {
	
	CouponUtil couponUtil = new CouponUtil();
	
	LoadCouponType loadCouponType = new LoadCouponType();
	
	public ApiGetAllCouponResult Process(ApiGetAllCouponInput input,
			MDataMap mResquestMap) {
		// 获取用户编号
		String member_code = getUserCode();
		String version = getApiClient().get("app_vision");

		ApiGetAllCouponResult result = new ApiGetAllCouponResult();

		// 获取分页每页条数
		int count = Integer.valueOf(Integer.parseInt(bConfig("familyhas.page_coupon_limit")));
		PageOption paging = new PageOption();
		if (input.getPageNum() <= 0) {
			return result;
		}
		paging.setOffset(input.getPageNum() - 1);
		paging.setLimit(count);
		MPageData mPageData = new MPageData();
		
		String now = DateUtil.getSysDateTimeString();
		
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("member_code", member_code);
		mWhereMap.put("manage_code", getManageCode());
		mWhereMap.put("sysTime", DateUtil.getSysDateTimeString());
		
		String sql = "select ci.coupon_type_code,ci.coupon_code,ci.surplus_money,ci.status,ci.end_time,ci.limit_money,ci.start_time,"
				+ "ci.initial_money,ci.is_see,ci.blocked,ci.big_order_code,ct.money_type,ct.creater,ct.status ct_status,oa.flag,ct.limit_condition,oa.is_change";
		sql	+= " from ordercenter.oc_coupon_info ci,ordercenter.oc_coupon_type ct,ordercenter.oc_activity oa";
		sql += " where ci.coupon_type_code = ct.coupon_type_code and ci.activity_code=oa.activity_code and ci.member_code=:member_code and ci.manage_code=:manage_code";
		
		// 小于5.1.4版本仅支持金额卷
		if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.1.4") < 0){
			sql += " and ct.money_type = '449748120001'";
		}
		
		//ld的优惠券 没有外部优惠券编号、版本低于5.2.8、活动未发布、活动类型未发布 任一情况 则不能使用此优惠券 -rhb 20181113
		if(StringUtils.isNotBlank(version) && AppVersionUtils.compareTo(version, "5.2.8") < 0) {
			sql += " and ct.creater!='ld'";
		} else {
			sql +=" and (ct.creater!='ld' or ( ct.status='4497469400030002' and oa.flag=1 and ci.out_coupon_code!='' and ci.out_coupon_code is not null))";
		}
		
		if (input.getCouponLocation() == 0) {
			// 未使用， 结束时间大于当前时间
			sql += " and (DATE_FORMAT(ci.end_time,'%Y-%m-%d %H:%i:%s')>:sysTime and ci.surplus_money > 0 AND (ci.status = 0 or (oa.is_change='Y' and ci.status = 1))) ";
		} else if (input.getCouponLocation() == 1) {
			// 历史优惠券，兼容历史版本包含已过期和已使用
			sql += " and (DATE_FORMAT(ci.end_time,'%Y-%m-%d %H:%i:%s')<:sysTime OR ci.status = 1) ";
		} else if (input.getCouponLocation() == 2) {
			// 已使用
			sql += " and ci.status = 1 and  ((oa.is_change='Y' and ci.surplus_money =0) or oa.is_change='N') ";
		} else if (input.getCouponLocation() == 3) {
			// 已过期，结束时间小于当前时间并且未使用
			sql += " and (DATE_FORMAT(ci.end_time,'%Y-%m-%d %H:%i:%s')<:sysTime AND (ci.status = 0 or (ci.status= 1 and oa.is_change='Y' and ci.surplus_money > 0))) ";
		}
		
		sql += " order by ci.update_time desc";
		
		mPageData = DataPaging.upPageData("oc_coupon_info", sql, mWhereMap, paging);
		
		double deadlineDay = Double.parseDouble(StringUtils.isEmpty(bConfig("ordercenter.COUPON_DEADLINE_DAY")) ? "2.0" : bConfig("ordercenter.COUPON_DEADLINE_DAY"));
		PlusModelCouponType modelCouponType;
		for (MDataMap maps : mPageData.getListData()) {
			CouponInfo couponInfo = new CouponInfo();
			couponInfo.setIsSee(maps.get("is_see"));//是否已经查看
			couponInfo.setCouponCode(maps.get("coupon_code"));
			couponInfo.setInitialMoney(new BigDecimal(maps.get("initial_money")));
			couponInfo.setSurplusMoney(new BigDecimal(maps.get("surplus_money")));
			
			//560 可找零优惠券状态特殊处理
			if("Y".equals(maps.get("is_change")) && input.getCouponLocation() == 0) {
				couponInfo.setStatus(0);
			}else {
				couponInfo.setStatus(Integer.parseInt(maps.get("status")));
			}
			
			couponInfo.setEndTime(maps.get("end_time"));
			couponInfo.setStartTime(maps.get("start_time"));
			couponInfo.setLimitMoney(new BigDecimal(maps.get("limit_money")));
			couponInfo.setUseLimit(bConfig("familyhas.coupon_limit"));
			couponInfo.setMoneyType(maps.get("money_type"));
			couponInfo.setIs_change(maps.get("is_change"));
			couponUtil.convertMoneyShow(couponInfo);
			
			//542版本返回优惠券类型编号 -rhb 20190423
			String coupon_type_code = maps.get("coupon_type_code");
			couponInfo.setCouponTypeCode(coupon_type_code);
			/**
			 * 542增加限制条件 -rhb 20190424
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
			
			// 小于520版本，礼金券当做金额券显示
			if(StringUtils.isNotBlank(version) 
					&& AppVersionUtils.compareTo(version, "5.2.0") < 0
					&& "449748120003".equals(couponInfo.getMoneyType())){
				couponInfo.setMoneyType("449748120001");
			}
			
			modelCouponType = loadCouponType.upInfoByCode(new PlusModelQuery(maps.get("coupon_type_code")));
			
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
				String channelCodeInput = StringUtils.isEmpty(input.getChannelId()) ? "" : input.getChannelId();
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
			if(couponInfo.getStatus() == 0 && couponInfo.getEndTime().compareTo(now) < 0) {
				couponInfo.setStatus(2);
			}
			
			//判断增加未激活标志，只对未使用的做判断。如果优惠券未使用且已过期则优先展示为已过期
			if(couponInfo.getStatus() == 0 && StringUtils.isNotEmpty(maps.get("blocked")) && maps.get("blocked").equals("1")) {
				couponInfo.setStatus(5);
			}
			
			//判断增加送此优惠券的大订单号
			if(StringUtils.isNotEmpty(maps.get("big_order_code"))) {
				couponInfo.setBigOrderCode(maps.get("big_order_code"));
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
				//542版本增加快过期标签 -rhb 20190412
				if(diffDay>=0 && diffDay<=2.0) {
					couponInfo.setIsShowDue("1");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			result.getCouponInfoList().add(couponInfo);
		}
		
		// 总页数
		int pagination1 = 0;
		int page1 = mPageData.getPageResults().getTotal();

		if (page1 % count == 0) {
			pagination1 = page1 / count;
		} else {
			pagination1 = page1 / count + 1;
		}
		result.setPagination(pagination1);
		result.setTotalCount(page1);
		
		mWhereMap = new MDataMap();
		mWhereMap.put("member_code", getUserCode());
		mWhereMap.put("manage_code", getManageCode());
		String sWhere = "member_code=:member_code and status=0 and surplus_money>0 and end_time>=now() and manage_code=:manage_code";

		//更新我的优惠券最新券的发放时间
		List<MDataMap> listMaps = DbUp.upTable("oc_coupon_info").queryAll("coupon_code,surplus_money,status,end_time,start_time,coupon_type_code,create_time", "create_time desc", sWhere, mWhereMap);
		
		if(null != listMaps && listMaps.size() > 0) {
			MDataMap map = listMaps.get(0);
			String newTime = map.get("create_time");
			Map<String, Object> remindMap = DbUp.upTable("oc_coupon_remind").dataSqlOne("select last_time from oc_coupon_remind where member_code=:member_code", new MDataMap("member_code", getUserCode()));
			if(remindMap != null && remindMap.size() > 0) {
				if(StringUtils.isNotEmpty((String) remindMap.get("last_time"))) {
					if(remindMap.get("last_time").toString().compareTo(newTime) < 0) {
						DbUp.upTable("oc_coupon_remind").dataUpdate(new MDataMap("last_time",newTime, "member_code",getUserCode(), "update_time",DateHelper.upNow()), "last_time,update_time", "member_code");
					}
				}
			} else {
				DbUp.upTable("oc_coupon_remind").dataInsert(new MDataMap("member_code",getUserCode(), "last_time",newTime, "update_time", DateHelper.upNow()));
			}
		}

		couponUtil.setCouponInfoSee(result.getCouponInfoList());//设置未查看为已查看
		return result;
	}
	
	/**
	 * 设置优惠券信息
	 * 
	 * @param couponList
	 *            待存放的优惠券list
	 * @param map
	 *            优惠券信息
	 */
	public void setCouponInfo(CouponInfo couponInfo, MDataMap map) {
		couponInfo.setCouponCode(map.get("coupon_code"));
		couponInfo.setInitialMoney(new BigDecimal(map.get("initial_money")));
		couponInfo.setSurplusMoney(new BigDecimal(map.get("surplus_money")));
		couponInfo.setEndTime(map.get("end_time"));
		couponInfo.setStartTime(map.get("start_time"));
		couponInfo.setLimitMoney(new BigDecimal(map.get("limit_money")));
		couponInfo
				.setUseLimit(StringUtils.isEmpty(map.get("limit_scope")) ? bConfig("familyhas.coupon_limit")
						: map.get("limit_scope"));
	}
	
	public  static void main(String[] args) {
		//System.out.println(DateHelper.daysBetween(DateHelper.upNow(), "2016-09-16 23:59:59"));
		String sDate = DateUtil.toFormatDate("2016-09-16 23:59:59", DateUtil.DATE_FORMAT_DATETIME);
		System.out.println(sDate);
	}
}
