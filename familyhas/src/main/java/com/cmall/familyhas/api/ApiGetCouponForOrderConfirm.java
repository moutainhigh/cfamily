package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.APiOrderConfirmInput;
import com.cmall.familyhas.api.input.ApiGetCouponForOrderConfirmInput;
import com.cmall.familyhas.api.result.APiOrderConfirmResult;
import com.cmall.familyhas.api.result.ApiGetCouponForOrderConfirmResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.model.Button;
import com.cmall.ordercenter.model.CouponForGetInfo;
import com.cmall.ordercenter.model.GoodsInfoForAdd;
import com.cmall.ordercenter.util.CouponUtil;
import com.srnpr.xmassystem.modelproduct.PlusModelSkuInfo;
import com.srnpr.xmassystem.service.ProductPriceService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.MoneyHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import javassist.bytecode.annotation.MemberValue;


/**
 * 查询系统返利优惠活动中优惠券发放条件是否满足
 * @author zb
 *
 */
public class ApiGetCouponForOrderConfirm extends RootApiForVersion<ApiGetCouponForOrderConfirmResult, ApiGetCouponForOrderConfirmInput> {

	@Override
	public ApiGetCouponForOrderConfirmResult Process(ApiGetCouponForOrderConfirmInput inputParam, MDataMap mRequestMap) {
		ApiGetCouponForOrderConfirmResult result = new ApiGetCouponForOrderConfirmResult();
		ProductPriceService priceService = new ProductPriceService();
		//List<String> skuList = inputParam.getSkuList();
		List<GoodsInfoForAdd> goods = inputParam.getGoods();
		// 取渠道值
		JSONObject input = JSONObject.parseObject(mRequestMap.get("api_client"));
		//449747430023
		String channelId="";
		String currentVersion = "";
		//MI150422100004
		String memberCode = "";
		CouponUtil couponUtil = new CouponUtil();
		getFlagLogin();
		if(input!=null&&input.containsKey("channelId")) { 
			channelId = input.getString("channelId");
			currentVersion = input.getString("app_vision")==null?"":input.getString("app_vision");
			}
		if(getFlagLogin()&&StringUtils.equals(channelId, "449747430023")&&goods!=null&&goods.size()==1&&!StringUtils.contains(goods.get(0).getSku_code(), "IC_SMG_")) {
		//if(goods!=null&&goods.size()==1) {
			//小程序&一单一货
			memberCode = getOauthInfo().getUserCode();
			String skuCode =goods.get(0).getSku_code();
			PlusModelSkuInfo skuInfo = priceService.getPlusModelSkuInfoForPublic(skuCode,memberCode,1,channelId,"0");
			BigDecimal sellPrice = skuInfo.getSellPrice();
			BigDecimal costPrice = skuInfo.getCostPrice();
			String productCode = skuInfo.getProductCode();
			
			String smallSellerCode = skuInfo.getSmallSellerCode();
			BigDecimal ldCost = BigDecimal.ZERO;
			if("SI2003".equals(smallSellerCode)) {
				//ld品毛利要减去10%成本，再减去50元配送费
				BigDecimal multiply = costPrice.multiply(new BigDecimal("0.1"));
				ldCost = multiply.add(new BigDecimal("50"));
				
			}
			BigDecimal profit = sellPrice.subtract(costPrice).subtract(ldCost);
			BigDecimal roundHalfUp = MoneyHelper.roundHalfUp(profit.divide(sellPrice,2));
			//订单金额要大于100
			BigDecimal payMoney = new BigDecimal(inputParam.getPay_money());

			if(payMoney.compareTo(new BigDecimal("100"))>0&&roundHalfUp.compareTo(new BigDecimal("0.15"))>0) {
				//订单金额大于100&毛利率>15%
				//满足条件后查看今天是否已有领取
				String sysDateTimeString = DateUtil.getSysDateTimeString();
				String currentDay = StringUtils.substring(sysDateTimeString, 0, 10);
				String startTime = currentDay+" 00:00:00";
				String endTime = currentDay+" 23:59:59";
				List<Map<String, Object>> sqlList = DbUp.upTable("oc_coupon_info").dataSqlList("select a.*,b.activity_type,IFNULL(b.rebate_ratio,0) rebate_ratio,IFNULL(b.validate_time,0) validate_time  from oc_coupon_info a , oc_activity b  where a.activity_code=b.activity_code and b.provide_type='4497471600060005'  and member_code='"+memberCode+"' and a.create_time>'"+startTime+"' and  a.create_time<'"+endTime+"' order by zid desc limit 0,1", null);
				 List<CouponForGetInfo> couponForGetList=new ArrayList<CouponForGetInfo>();
				if(sqlList!=null&&sqlList.size()>0) {/*
			    	//今日有领取
			    	Map<String, Object> map = sqlList.get(0);
			    	//判断领取的券是否此时订单商品，是则返回优惠券信息，否的话不返回
			    	int count = DbUp.upTable("oc_coupon_type_limit").count("coupon_type_code",map.get("coupon_type_code").toString(),"product_codes",productCode);
			    	 //DbUp.upTable("oc_coupon_type_limit").dataSqlList("select * from oc_coupon_type_limit ",new MDataMap( "coupon_type_code",map.get("coupon_type_code").toString(),"product_codes",productCode));
			    	if(count>0) {
			    		//领取的正是此订单商品的返利券
			    		if("0".equals(map.get("status").toString())) {
			    			//未使用，返回
			    			CouponForGetInfo couponForGetInfo = new CouponForGetInfo();
				    		//couponForGetInfo.setActionType();
				    		//couponForGetInfo.setActionValue();
				    		couponForGetInfo.setActivityCode(map.get("activity_code").toString());
				    		couponForGetInfo.setActivityType(map.get("activity_type").toString());
				    		Button button = new Button();
				    		button.setButtonCode("4497477800080019");
				    		button.setButtonStatus(1);
				    		button.setButtonTitle("立即使用");
				    		List<Button> buttons = new ArrayList<Button>();
				    		buttons.add(button);
				    		couponForGetInfo.setButtons(buttons);
				    		couponForGetInfo.setChannelCodes("449747430023");
				    		couponForGetInfo.setChannelLimit("4497471600070002");
				    		couponForGetInfo.setCouponCode(map.get("coupon_code").toString());
				    		couponForGetInfo.setCouponEndTime(map.get("end_time").toString());
				    		couponForGetInfo.setCouponStartTime(map.get("start_time").toString());
				    		couponForGetInfo.setCouponTypeCode(map.get("coupon_type_code").toString());
				    		couponForGetInfo.setCouponTypeName("系统返利券");
				    		couponForGetInfo.setEndTime(map.get("end_time").toString());
				    		//couponForGetInfo.setIfDistributionCoupon(map.get("").toString());
				    		//couponForGetInfo.setIfStore(map.get("").toString());
				    		couponForGetInfo.setLimitCondition("4497471600070002");
				    		//couponForGetInfo.setLimitExplain(map.get("").toString());
				    		//couponForGetInfo.setLimitMoney(map.get("").toString());
				    		couponForGetInfo.setLimitScope("指定商品可用");
				    		//BigDecimal money = MoneyHelper.roundHalfUp(profit.multiply(new BigDecimal(map.get("rebate_ratio").toString())).divide(new BigDecimal("100"),0));
				    		couponForGetInfo.setMoney(new BigDecimal(map.get("initial_money").toString()));
				    		couponForGetInfo.setMoneyType("449748120001");
				    		couponForGetInfo.setProductCodes(productCode);
				    		couponForGetInfo.setStartTime(map.get("start_time").toString());
				    		couponForGetInfo.setStatus(1);
				    		couponForGetInfo.setUid(map.get("uid").toString());
				    		//couponForGetInfo.setValidDay(map.get("").toString());
				    		couponForGetList.add(couponForGetInfo);
				    		result.setCouponForGetList(couponForGetList);
			    		}

			    }

			*/}else {
		    	//今日没有领取
				//查看是否已有系统返利活动对应的指定商品的券类型
				String couponCodeTypeSql = "select IFNULL(oa.rebate_ratio,0) rebate_ratio,IFNULL(oa.validate_time,0) validate_time,oa.activity_type activity_type, ot.valid_day coupon_valid_day,ot.start_time coupon_start_time,ot.end_time coupon_end_time,ot.action_type action_type, ot.action_value action_value, ot.uid uid, ot.surplus_money surplus_money, ot.limit_scope limit_scope ,ot.limit_explain limit_explain, ot.coupon_type_code coupon_type_code,ot.coupon_type_name coupon_type_name,ot.activity_code activity_code,ot.money money,ot.limit_money limit_money,oa.begin_time start_time,oa.end_time end_time ,ot.money_type money_type,ot.limit_condition limit_condition from oc_coupon_type ot "
						+ "LEFT JOIN oc_activity oa ON ot.activity_code = oa.activity_code  where ot.produce_type = '4497471600040001' and oa.begin_time <= now() and oa.end_time > now() and oa.provide_type ='4497471600060005' and oa.flag = 1 and oa.is_detail_show = '449748350001' and ot.exchange_type = '4497471600390001' and ot.status = '4497469400030002' order by ot.create_time desc";
				List<Map<String, Object>> couponTypeMapList = DbUp.upTable("oc_coupon_type").dataSqlList(couponCodeTypeSql,null);
				List<String> temp = new ArrayList<String>();
				if(couponTypeMapList!=null&&couponTypeMapList.size()>0) {
					for (Map<String, Object> map2 : couponTypeMapList) {
						temp.add("'"+map2.get("coupon_type_code")+"'");
					}
					String rebate_ratio = couponTypeMapList.get(0).get("rebate_ratio").toString();
					//String validate_time = couponTypeMapList.get(0).get("validate_time").toString();
					String actCode = couponTypeMapList.get(0).get("activity_code").toString();
					String actType = couponTypeMapList.get(0).get("activity_type").toString();
					String start_time = couponTypeMapList.get(0).get("start_time").toString();
					String end_time = couponTypeMapList.get(0).get("end_time").toString();
					
					String subSql = "select * from oc_coupon_type_limit where product_codes='"+productCode+"' and  coupon_type_code in ("+StringUtils.join(temp, ",")+")";
					List<Map<String, Object>> sqlList2 = DbUp.upTable("oc_coupon_type_limit").dataSqlList(subSql, null);
					if(sqlList2!=null&&sqlList2.size()>0) {		
						//已经存在
					    Map<String, Object> map = sqlList2.get(0);
			    		BigDecimal money = MoneyHelper.round(0,BigDecimal.ROUND_HALF_UP,profit.multiply(new BigDecimal(rebate_ratio)).divide(new BigDecimal("100"),0));
					    MDataMap one = DbUp.upTable("oc_coupon_type").one("coupon_type_code",map.get("coupon_type_code").toString());
						couponUtil.provideCoupon2(memberCode, map.get("coupon_type_code").toString(), "0", "","",1,money);
						List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_coupon_info").dataSqlList("select * from oc_coupon_info where coupon_type_code=:coupon_type_code and member_code=:member_code and status=0 and create_time>'"+startTime+"' order by zid desc limit 0,1", new MDataMap("member_code",memberCode,"coupon_type_code",map.get("coupon_type_code").toString()));
						if(dataSqlList!=null&&dataSqlList.size()>0) {
							Map<String, Object> map2 = dataSqlList.get(0);
							CouponForGetInfo couponForGetInfo = new CouponForGetInfo();
				    		couponForGetInfo.setActivityCode(map.get("activity_code").toString());
				    		couponForGetInfo.setActivityType(actType);
				    		Button button = new Button();
				    		button.setButtonCode("4497477800080019");
				    		button.setButtonStatus(1);
				    		button.setButtonTitle("立即使用");
				    		List<Button> buttons = new ArrayList<Button>();
				    		buttons.add(button);
				    		couponForGetInfo.setButtons(buttons);
				    		couponForGetInfo.setChannelCodes("449747430023");
				    		couponForGetInfo.setChannelLimit("4497471600070002");
				    		couponForGetInfo.setCouponTypeCode(map.get("coupon_type_code").toString());
				    		couponForGetInfo.setCouponTypeName("系统返利券");
				    		couponForGetInfo.setLimitCondition("4497471600070002");
				    		couponForGetInfo.setLimitScope("指定商品可用");
				    		couponForGetInfo.setMoney(money);
				    		couponForGetInfo.setMoneyType("449748120001");
				    		couponForGetInfo.setProductCodes(productCode);
				    		couponForGetInfo.setStatus(0);
				    		couponForGetInfo.setStartTime(map2.get("start_time").toString());
				    		couponForGetInfo.setEndTime(map2.get("end_time").toString());
				    		couponForGetInfo.setCouponCode(map2.get("coupon_code").toString());
				    		couponForGetList.add(couponForGetInfo);
				    		result.setCouponForGetList(couponForGetList);
						}
						
						
					}else {
						//未存在，则生成
						MDataMap insertMap1 = new MDataMap();
						String coupon_type_code = WebHelper.upCode("CT");
						insertMap1.put("uid", WebHelper.upUuid());
						insertMap1.put("coupon_type_code",coupon_type_code);
						insertMap1.put("coupon_type_name","系统挽回赠送");
						insertMap1.put("activity_code", actCode);
			    		BigDecimal money =  MoneyHelper.round(0,BigDecimal.ROUND_HALF_UP,profit.multiply(new BigDecimal(rebate_ratio)).divide(new BigDecimal("100"),0));
						insertMap1.put("money", money.toString());
						insertMap1.put("start_time", start_time);
						insertMap1.put("end_time", end_time);
						insertMap1.put("status", "4497469400030002");//生成即生效
						insertMap1.put("produce_type", "4497471600040001");//优惠券
						insertMap1.put("limit_condition", "4497471600070002");//指定
						insertMap1.put("limit_scope", "指定商品可用");
						insertMap1.put("valid_type", "4497471600080002");//日期范围
						insertMap1.put("creater", "system");
						insertMap1.put("create_time", sysDateTimeString);
						insertMap1.put("updater", "system");
						insertMap1.put("update_time", sysDateTimeString);
						insertMap1.put("total_money", "999999999");
						insertMap1.put("surplus_money", "999999999");
						insertMap1.put("money_type", "449748120001");//金额券		
						insertMap1.put("exchange_type", "4497471600390001");
						DbUp.upTable("oc_coupon_type").dataInsert(insertMap1);
						
						MDataMap insertMap2 = new MDataMap();
						insertMap2.put("uid", WebHelper.upUuid());
						insertMap2.put("coupon_type_code", coupon_type_code);
						insertMap2.put("activity_code", actCode);
						insertMap2.put("product_limit", "4497471600070002");
						insertMap2.put("channel_limit", "4497471600070002");
						insertMap2.put("product_codes", productCode);
						insertMap2.put("channel_codes", "449747430023");
						insertMap2.put("create_user", "system");
						insertMap2.put("create_time", sysDateTimeString);
						insertMap2.put("update_user", "system");
						insertMap2.put("update_time", sysDateTimeString);
						insertMap2.put("brand_limit", "4497471600070001");
						insertMap2.put("category_limit", "4497471600070001");
						
						//insertMap2.put("pay_limit", "4497471600070002");
						//insertMap2.put("payment_type", "449748290001");//在线支付
						insertMap2.put("activity_limit", "449747110002");//是否可以参与活动  是
						insertMap2.put("allowed_activity_type", "4497472600010001,4497472600010002,4497472600010004,4497472600010005,4497472600010008,4497472600010015,4497472600010018,4497472600010024,4497472600010030");
						DbUp.upTable("oc_coupon_type_limit").dataInsert(insertMap2);
						
						//领取
						couponUtil.provideCoupon2(memberCode, coupon_type_code, "0", "","",1,money);
						List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_coupon_info").dataSqlList("select * from oc_coupon_info where coupon_type_code=:coupon_type_code and member_code=:member_code and status=0 and create_time>'"+startTime+"' order by zid desc limit 0,1", new MDataMap("member_code",memberCode,"coupon_type_code",coupon_type_code));                       
						if(dataSqlList!=null&&dataSqlList.size()>0) {
							Map<String, Object> map2 = dataSqlList.get(0);
							CouponForGetInfo couponForGetInfo = new CouponForGetInfo();
				    		couponForGetInfo.setActivityCode(actCode);
				    		couponForGetInfo.setActivityType(actType);
				    		Button button = new Button();
				    		button.setButtonCode("4497477800080019");
				    		button.setButtonStatus(1);
				    		button.setButtonTitle("立即使用");
				    		List<Button> buttons = new ArrayList<Button>();
				    		buttons.add(button);
				    		couponForGetInfo.setButtons(buttons);
				    		couponForGetInfo.setChannelCodes("449747430023");
				    		couponForGetInfo.setChannelLimit("4497471600070002");
				    		couponForGetInfo.setCouponTypeCode(coupon_type_code);
				    		couponForGetInfo.setCouponTypeName("系统返利券");
				    		couponForGetInfo.setLimitCondition("4497471600070002");
				    		couponForGetInfo.setLimitScope("指定商品可用");
				    		couponForGetInfo.setMoney(new BigDecimal( money.toString()));
				    		couponForGetInfo.setMoneyType("449748120001");
				    		couponForGetInfo.setProductCodes(productCode);
				    		couponForGetInfo.setStatus(0);
				    		couponForGetInfo.setStartTime(map2.get("start_time").toString());
				    		couponForGetInfo.setEndTime(map2.get("end_time").toString());
				    		couponForGetInfo.setCouponCode(map2.get("coupon_code").toString());
				    		couponForGetList.add(couponForGetInfo);
				    		result.setCouponForGetList(couponForGetList);
						}		
						
					 }	
				}else {
					String activitySql = "SELECT\r\n activity_code," + 
							"	IFNULL(oa.rebate_ratio, 0) rebate_ratio,\r\n" + 
							"	IFNULL(oa.validate_time, 0) validate_time,\r\n" + 
							"	oa.activity_type activity_type,\r\n" + 
							"	oa.begin_time start_time,\r\n" + 
							"	oa.end_time end_time\r\n" + 
							"FROM\r\n" + 
							"	ordercenter.oc_activity oa\r\n" + 
							"WHERE\r\n" + 
							"	oa.begin_time <= sysdate()\r\n" + 
							"AND oa.end_time > sysdate()\r\n" + 
							"AND oa.provide_type = '4497471600060005'\r\n" + 
							"AND oa.flag = 1\r\n" + 
							"AND oa.is_detail_show = '449748350001'";
					List<Map<String, Object>> actList = DbUp.upTable("oc_activity").dataSqlList(activitySql,null);
					if(actList!=null&&actList.size()>0) {
						Map<String, Object> map = actList.get(0);
						String actCode = map.get("activity_code").toString();
						String start_time = map.get("start_time").toString();
						String end_time = map.get("end_time").toString();
						String rebate_ratio = map.get("rebate_ratio").toString();
						String validate_time = map.get("validate_time").toString();
						String activity_type = map.get("activity_type").toString();
						//未存在，则生成
						MDataMap insertMap1 = new MDataMap();
						String coupon_type_code = WebHelper.upCode("CT");
						insertMap1.put("uid", WebHelper.upUuid());
						insertMap1.put("coupon_type_code",coupon_type_code);
						insertMap1.put("activity_code", actCode);
			    		BigDecimal money =  MoneyHelper.round(0,BigDecimal.ROUND_HALF_UP,profit.multiply(new BigDecimal(rebate_ratio)).divide(new BigDecimal("100"),0));
						insertMap1.put("money", money.toString());
						insertMap1.put("start_time", start_time);
						insertMap1.put("end_time", end_time);
						insertMap1.put("status", "4497469400030002");//生成即生效
						insertMap1.put("produce_type", "4497471600040001");//优惠券
						insertMap1.put("limit_condition", "4497471600070002");//指定
						insertMap1.put("limit_scope", "指定商品可用");
						insertMap1.put("valid_type", "4497471600080002");//日期范围
						insertMap1.put("creater", "system");
						insertMap1.put("create_time", sysDateTimeString);
						insertMap1.put("updater", "system");
						insertMap1.put("update_time", sysDateTimeString);
						insertMap1.put("total_money", "999999999");
						insertMap1.put("surplus_money", "999999999");
						insertMap1.put("money_type", "449748120001");//金额券		
						insertMap1.put("exchange_type", "4497471600390001");
						DbUp.upTable("oc_coupon_type").dataInsert(insertMap1);
						
						MDataMap insertMap2 = new MDataMap();
						insertMap2.put("uid", WebHelper.upUuid());
						insertMap2.put("coupon_type_code", coupon_type_code);
						insertMap2.put("activity_code", actCode);
						insertMap2.put("product_limit", "4497471600070002");
						insertMap2.put("channel_limit", "4497471600070002");
						insertMap2.put("product_codes", productCode);
						insertMap2.put("channel_codes", "449747430023");
						insertMap2.put("create_user", "system");
						insertMap2.put("create_time", sysDateTimeString);
						insertMap2.put("update_user", "system");
						insertMap2.put("update_time", sysDateTimeString);
						//insertMap2.put("pay_limit", "4497471600070002");
						//insertMap2.put("payment_type", "449748290001");//在线支付
						insertMap2.put("activity_limit", "449747110002");//是否可以参与活动  是
						insertMap2.put("allowed_activity_type", "4497472600010001,4497472600010002,4497472600010004,4497472600010005,4497472600010008,4497472600010015,4497472600010018,4497472600010024,4497472600010030");
						DbUp.upTable("oc_coupon_type_limit").dataInsert(insertMap2);						
						
						//领取
						couponUtil.provideCoupon2(memberCode, coupon_type_code, "0", "","",1,money);
						List<Map<String, Object>> dataSqlList = DbUp.upTable("oc_coupon_info").dataSqlList("select * from oc_coupon_info where coupon_type_code=:coupon_type_code and member_code=:member_code and status=0 and create_time>'"+startTime+"' order by zid desc limit 0,1", new MDataMap("member_code",memberCode,"coupon_type_code",coupon_type_code));                       
						if(dataSqlList!=null&&dataSqlList.size()>0) {
							Map<String, Object> map2 = dataSqlList.get(0);
							CouponForGetInfo couponForGetInfo = new CouponForGetInfo();
				    		couponForGetInfo.setActivityCode(actCode);
				    		couponForGetInfo.setActivityType(activity_type);
				    		Button button = new Button();
				    		button.setButtonCode("4497477800080019");
				    		button.setButtonStatus(1);
				    		button.setButtonTitle("立即使用");
				    		List<Button> buttons = new ArrayList<Button>();
				    		buttons.add(button);
				    		couponForGetInfo.setButtons(buttons);
				    		couponForGetInfo.setChannelCodes("449747430023");
				    		couponForGetInfo.setChannelLimit("4497471600070002");
				    		couponForGetInfo.setCouponTypeCode(coupon_type_code);
				    		couponForGetInfo.setCouponTypeName("系统返利券");
				    		couponForGetInfo.setLimitCondition("4497471600070002");
				    		couponForGetInfo.setLimitScope("指定商品可用");
				    		couponForGetInfo.setMoney(new BigDecimal( money.toString()));
				    		couponForGetInfo.setMoneyType("449748120001");
				    		couponForGetInfo.setProductCodes(productCode);
				    		couponForGetInfo.setStatus(0);
				    		couponForGetInfo.setStartTime(map2.get("start_time").toString());
				    		couponForGetInfo.setEndTime(map2.get("end_time").toString());
				    		couponForGetInfo.setCouponCode(map2.get("coupon_code").toString());
				    		couponForGetList.add(couponForGetInfo);
				    		result.setCouponForGetList(couponForGetList);
						}	

					}
			
				}
		    }
			
		  }

      	}
		return result;
		}
	
}

