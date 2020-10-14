package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiForCutCakeDrawInput;
import com.cmall.familyhas.api.model.CakePrize;
import com.cmall.familyhas.api.result.ApiForCutCakeDrawResult;
import com.cmall.groupcenter.util.HttpUtil;
import com.cmall.ordercenter.service.CouponsService;
import com.srnpr.xmassystem.invoke.ref.model.UpdateCustAmtInput;
import com.srnpr.xmassystem.service.PlusServiceAccm;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.JobExecHelper;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 切蛋糕-抽奖接口
 * @author lgx
 *
 */
public class ApiForCutCakeDraw extends RootApiForToken<ApiForCutCakeDrawResult, ApiForCutCakeDrawInput> {

	public ApiForCutCakeDrawResult Process(ApiForCutCakeDrawInput inputParam, MDataMap mRequestMap) {
		ApiForCutCakeDrawResult result = new ApiForCutCakeDrawResult();
		
		String memberCode = getUserCode();
		String eventCode = "";
		
		int cutCakeNum = 0;
		String jpName = "";
		int couponAmount = 0;
		int jfNum = 0;
		String jpType = "";
		String canAddNum = "";
		String isBlessing = "0";
		
		String nowTime = FormatHelper.upDateTime();
		String startTime = nowTime.substring(0, 10)+" 00:00:00";
		String endTime = nowTime.substring(0, 10)+" 23:59:59";
		
		// 查询当前时间段内已经发布状态的切蛋糕活动(1条)
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210011' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的切蛋糕活动!");
			return result;
		}else {
			eventCode = (String) eventInfoMap.get("event_code");
		}
		
		// 获取登录用户手机号
		String loginSql = "SELECT * FROM mc_login_info WHERE member_code = '"+memberCode+"' AND manage_code = 'SI2003' ORDER BY create_time DESC LIMIT 1";
		Map<String, Object> loginInfoMap = DbUp.upTable("mc_login_info").dataSqlOne(loginSql, new MDataMap());
		if(loginInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("用户信息有误!");
			return result;
		}
		String login_name = (String) loginInfoMap.get("login_name");
		// 根据手机号查询该用户今天在LD支付成功订单数
		int ldOrderCount = 0;
		String url = bConfig("groupcenter.rsync_homehas_url")+"getCutCakeNum";
		Map<String, Object> postParams = new HashMap<String, Object>();
		postParams.put("tel", login_name);
		String postResult = HttpUtil.post(url, JSONArray.toJSONString(postParams), "UTF-8");
		JSONObject jo = JSONObject.parseObject(postResult);
		if((boolean) jo.get("success")){			
			ldOrderCount = (int) jo.get("result");
		}
		
		// 查询该用户在惠家有支付成功订单数
		String orderCountSql = "SELECT count(1) num FROM ( " + 
				" SELECT o1.zid  FROM oc_orderinfo o1 WHERE buyer_code = '"+memberCode+"' AND create_time >= '"+startTime+"' AND create_time <= '"+endTime+"' " + 
				"	AND order_status in ('4497153900010002','4497153900010003','4497153900010004','4497153900010005') AND small_seller_code != 'SI2003' " + 
				" UNION ALL " + 
				" SELECT o2.zid  FROM oc_orderinfo o2 WHERE buyer_code = '"+memberCode+"' AND create_time >= '"+startTime+"' AND create_time <= '"+endTime+"' " + 
				"	AND order_status in ('4497153900010002','4497153900010003','4497153900010004','4497153900010005') AND small_seller_code = 'SI2003' AND out_order_code = ''" + 
				") u";
		Map<String, Object> orderCountMap = DbUp.upTable("oc_orderinfo").dataSqlOne(orderCountSql, new MDataMap());
		int orderCount = 0;
		if(orderCountMap != null) {
			orderCount = MapUtils.getIntValue(orderCountMap, "num");
		}
		
		// 查询当天是否已经发表过祝福且没有抽过该奖品,如果已经发表过祝福且没抽过奖,则赠送免费抽奖次数
		int blessCount = 0;
		String blessSql = "SELECT * FROM sc_hudong_cake_blessing WHERE member_code = '"+memberCode+"' AND create_time >= '"+startTime+"' AND create_time <= '"+endTime+"'";
		Map<String, Object> cake_blessing = DbUp.upTable("sc_hudong_cake_blessing").dataSqlOne(blessSql, new MDataMap());
		if(cake_blessing != null) {
			// 查到祝福,返回今日已经送祝福
			isBlessing = "1";
			// 今日获得免费次数
			blessCount = 1;
		}
		
		// 获取切蛋糕配置次数
		// 每天免费次数
		MDataMap cake_config1 = DbUp.upTable("sc_hudong_cake_config").one("type","1");
		int num1 = MapUtils.getIntValue(cake_config1,"num");
		// 每天下单可获得次数
		MDataMap cake_config2 = DbUp.upTable("sc_hudong_cake_config").one("type","2");
		int num2 = MapUtils.getIntValue(cake_config2,"num");
		
		// 今日切过几次蛋糕
		String drawCountsql = "SELECT count(1) num FROM sc_hudong_cake_draw_jl WHERE event_code = '"+eventCode+"' AND member_code = '"+memberCode+"' "
				+ " AND create_time >= '"+startTime+"' AND create_time <= '"+endTime+"'";
		Map<String, Object> drawCountMap = DbUp.upTable("sc_hudong_cake_draw_jl").dataSqlOne(drawCountsql, new MDataMap());
		int drawCount = 0;
		if(drawCountMap != null) {
			drawCount = MapUtils.getIntValue(drawCountMap, "num");
		}
		
		// 计算可切蛋糕次数:(LD那边接口返回有效订单数(根据手机号查) + 惠家有有效订单数(small_seller_code不是SI2003的) + 1次免费机会(最小为0,最大为5)) - 已经切过的次数 
		int count = ldOrderCount + orderCount;
		if(count > num2) {
			count = num2;
		}
		cutCakeNum = count + blessCount - drawCount;
		if(cutCakeNum < 0) {
			cutCakeNum = 0;
		}
		
		// 判断用户是否还能下单获得切蛋糕次数
		if(cake_blessing != null) {
			// 获得免费次数
			if((cutCakeNum + drawCount) >= (num1 + num2)) {
				canAddNum = "0";
			}else {
				canAddNum = "1";
			}
		}else { 
			// 没有免费抽奖机会
			if((cutCakeNum + drawCount) >= num2) {
				canAddNum = "0";
			}else {
				canAddNum = "1";
			}
		}
		
		if(cutCakeNum > 0) {
			// 首先验证免费次数是否已经使用,未使用则先用免费次数发券,已使用则走下面概率抽奖
			boolean flag = false;
			if(cake_blessing != null) {
				if("0".equals(MapUtils.getString(cake_blessing, "is_draw"))) {
					// 有免费次数且未使用
					flag = true;
				}
			}
			
			// 中奖信息
			CakePrize draw = new CakePrize();
			if(flag) {
				// 有未使用的免费次数,写死抽奖券
				MDataMap map = DbUp.upTable("sc_hudong_cake_prize").one("event_code",eventCode,"jp_code","JP20061210012");
				draw.setCouponAmount(MapUtils.getIntValue(map, "coupon_amount"));
				draw.setCouponTypeCode(MapUtils.getString(map, "coupon_type_code"));
				draw.setJfNum(MapUtils.getIntValue(map, "jf_num"));
				draw.setJpCode(MapUtils.getString(map, "jp_code"));
				draw.setJpName(MapUtils.getString(map, "jp_name"));
				draw.setJpType(MapUtils.getString(map, "jp_type"));
				draw.setJpZjgl(MapUtils.getIntValue(map, "jp_zjgl"));
				
				// 抽完奖后将免费次数置为已使用
				MDataMap mDataMap = new MDataMap();
				mDataMap.put("uid", MapUtils.getString(cake_blessing,"uid"));
				mDataMap.put("is_draw", "1");
				DbUp.upTable("sc_hudong_cake_blessing").dataUpdate(mDataMap , "is_draw", "uid");
			}else {
				// 没有免费次数,或者免费次数已经使用,则走概率抽奖
				// 大转盘奖品信息
				String sSql2 = "SELECT * FROM sc_hudong_cake_prize WHERE jp_status = '1' AND event_code = '" + eventCode +"'";
				List<Map<String, Object>> cakePrizeList = DbUp.upTable("sc_hudong_cake_prize").dataSqlList(sSql2, new MDataMap());
				
				List<CakePrize> initDrawList = new ArrayList<CakePrize>();
				for (Map<String, Object> map : cakePrizeList) {
					CakePrize cakePrize = new CakePrize();
					cakePrize.setCouponAmount(MapUtils.getIntValue(map, "coupon_amount"));
					cakePrize.setCouponTypeCode(MapUtils.getString(map, "coupon_type_code"));
					cakePrize.setJfNum(MapUtils.getIntValue(map, "jf_num"));
					cakePrize.setJpCode(MapUtils.getString(map, "jp_code"));
					cakePrize.setJpName(MapUtils.getString(map, "jp_name"));
					cakePrize.setJpType(MapUtils.getString(map, "jp_type"));
					cakePrize.setJpZjgl(MapUtils.getIntValue(map, "jp_zjgl"));
					initDrawList.add(cakePrize);
				}
				
				// 开始抽奖,返回抽中奖品信息
				draw = generateAward(initDrawList);
				
				// 只有特等奖,一等奖和二等奖有数量限制
				if(draw.getJpCode().equals("JP20061210009") || draw.getJpCode().equals("JP20061210010") || draw.getJpCode().equals("JP20061210011")) {
					MDataMap prizeMap = DbUp.upTable("sc_hudong_cake_prize").one("event_code",eventCode,"jp_code",draw.getJpCode());
					if(prizeMap != null) {
						// 5元优惠券奖项
						MDataMap map14 = DbUp.upTable("sc_hudong_cake_prize").one("event_code",eventCode,"jp_code","JP20061210014");
						
						int jp_num = MapUtils.getIntValue(prizeMap, "jp_num");
						int jp_zjgl = MapUtils.getIntValue(prizeMap, "jp_zjgl");
						if(jp_num > 0) {
							jp_num = jp_num - 1;
							// 更新奖品数量
							MDataMap mDataMap2 = new MDataMap();
							mDataMap2.put("uid", prizeMap.get("uid"));
							mDataMap2.put("jp_num", jp_num+"");
							DbUp.upTable("sc_hudong_cake_prize").dataUpdate(mDataMap2, "jp_num", "uid");
							
							if(jp_num == 0) {
								// 如果抽完之后奖品数为0,则更新概率也改为0
								MDataMap mDataMap3 = new MDataMap();
								mDataMap3.put("uid", prizeMap.get("uid"));
								mDataMap3.put("jp_zjgl", "0");
								DbUp.upTable("sc_hudong_cake_prize").dataUpdate(mDataMap3, "jp_zjgl", "uid");
								// 将原奖品的概率加到5元优惠券上
								MDataMap mDataMap4 = new MDataMap();
								mDataMap4.put("uid", map14.get("uid"));
								mDataMap4.put("jp_zjgl", MapUtils.getIntValue(map14, "jp_zjgl")+jp_zjgl+"");
								DbUp.upTable("sc_hudong_cake_prize").dataUpdate(mDataMap4, "jp_zjgl", "uid");
							}
							
						}else {
							if(jp_zjgl > 0) {
								// 该奖品数量为0,概率也改为0
								MDataMap mDataMap3 = new MDataMap();
								mDataMap3.put("uid", prizeMap.get("uid"));
								mDataMap3.put("jp_zjgl", "0");
								DbUp.upTable("sc_hudong_cake_prize").dataUpdate(mDataMap3, "jp_zjgl", "uid");
								// 将原奖品的概率加到5元优惠券上
								MDataMap mDataMap4 = new MDataMap();
								mDataMap4.put("uid", map14.get("uid"));
								mDataMap4.put("jp_zjgl", MapUtils.getIntValue(map14, "jp_zjgl")+jp_zjgl+"");
								DbUp.upTable("sc_hudong_cake_prize").dataUpdate(mDataMap4, "jp_zjgl", "uid");
							}
							// 奖品数量为0,则赠送5元优惠券
							draw.setCouponAmount(MapUtils.getIntValue(map14, "coupon_amount"));
							draw.setCouponTypeCode(MapUtils.getString(map14, "coupon_type_code"));
							draw.setJfNum(MapUtils.getIntValue(map14, "jf_num"));
							draw.setJpCode(MapUtils.getString(map14, "jp_code"));
							draw.setJpName(MapUtils.getString(map14, "jp_name"));
							draw.setJpType(MapUtils.getString(map14, "jp_type"));
							draw.setJpZjgl(MapUtils.getIntValue(map14, "jp_zjgl"));
						}
					}
				}
				
			}
			// 剩余切蛋糕次数减1
			cutCakeNum--;
			
			jpName = draw.getJpName();
			jpType = draw.getJpType();
			couponAmount = draw.getCouponAmount();
			jfNum = draw.getJfNum();
			String jpCode = draw.getJpCode();
			
			// 添加抽奖记录
			MDataMap mDataMap5 = new MDataMap();
			mDataMap5.put("uid", WebHelper.upUuid());
			mDataMap5.put("event_code", eventCode);
			mDataMap5.put("member_code", memberCode);
			mDataMap5.put("create_time", nowTime);
			mDataMap5.put("jp_code", jpCode);
			mDataMap5.put("jp_name", jpName);
			mDataMap5.put("jp_type", jpType);
			DbUp.upTable("sc_hudong_cake_draw_jl").dataInsert(mDataMap5);
			
			// 处理抽奖
			if(jpType.equals("0")) { // 积分
				// 发积分
				PlusServiceAccm plusServiceAccm = new PlusServiceAccm();
				// 积分转钱
				BigDecimal jf_Num = new BigDecimal(jfNum);
				BigDecimal giveMoney = plusServiceAccm.accmAmtToMoney(jf_Num,2);
				// 家有客代号
				String custId = plusServiceAccm.getCustId(memberCode);
				// 如果客代号为空,调用接口生成客代
				if(null == custId || "".equals(custId)) {
					// 调用接口生成客代
					String url1 = bConfig("groupcenter.rsync_homehas_url")+"createUser";
					Map<String, Object> postParams1 = new HashMap<String, Object>();
					postParams1.put("mobile", login_name);
					String postResult1 = HttpUtil.post(url1, JSONArray.toJSONString(postParams1), "UTF-8");
					JSONObject jo1 = JSONObject.parseObject(postResult1);
					custId = jo1.get("cust_id").toString();
				}
				// 赋予积分
				RootResult teamResult = plusServiceAccm.changeForAccmAmt(UpdateCustAmtInput.CurdFlag.CC, giveMoney, custId, "", eventCode);
				// 记录积分变更日志
				if(teamResult.getResultCode() == 1) {
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("member_code", memberCode);
					mDataMap.put("cust_id", custId);
					mDataMap.put("change_type", "449748080016");
					mDataMap.put("change_money", giveMoney.toString());
					mDataMap.put("remark", "切蛋糕:"+jpCode);
					mDataMap.put("create_time", FormatHelper.upDateTime());
					DbUp.upTable("mc_member_integral_change").dataInsert(mDataMap);
				}else{
					String param = memberCode+","+giveMoney+","+eventCode+","+custId;
					// 操作不成功，加入到定时任务中进行重试
					JobExecHelper.createExecInfo("449746990032", param, null);
				}
			}else if(jpType.equals("1")) { // 优惠券
				// 发优惠券
				CouponsService couponsService = new CouponsService();
				
				RootResultWeb result2 = couponsService.distributeCouponsByCouponCode(draw.getCouponTypeCode(), login_name, "2");
				if(result2.getResultCode()==1) {
					
				}else {
					result.setResultCode(result2.getResultCode());
					result.setResultMessage(result2.getResultMessage());
				}
			}
		}else {
			// 没有切蛋糕次数
			result.setCanAddNum(canAddNum);
			result.setCutCakeNum(cutCakeNum);
			return result;
		}
		
		result.setCanAddNum(canAddNum);
		result.setCutCakeNum(cutCakeNum);
		result.setCouponAmount(couponAmount);
		result.setJfNum(jfNum);
		result.setJpName(jpName);
		result.setJpType(jpType);
		result.setIsBlessing(isBlessing);
		
		return result;
	}

	public static void main(String[] args) {
		List<CakePrize> list = new ArrayList<CakePrize>();
		CakePrize cakePrize1 = new CakePrize();
		cakePrize1.setJpCode("a");
		cakePrize1.setJpZjgl(5);
		CakePrize cakePrize2 = new CakePrize();
		cakePrize2.setJpCode("b");
		cakePrize2.setJpZjgl(5);
		CakePrize cakePrize3 = new CakePrize();
		cakePrize3.setJpCode("c");
		cakePrize3.setJpZjgl(100);
		CakePrize cakePrize4 = new CakePrize();
		cakePrize4.setJpCode("d");
		cakePrize4.setJpZjgl(2000);
		CakePrize cakePrize5 = new CakePrize();
		cakePrize5.setJpCode("e");
		cakePrize5.setJpZjgl(900);
		CakePrize cakePrize6 = new CakePrize();
		cakePrize6.setJpCode("f");
		cakePrize6.setJpZjgl(6990);
		list.add(cakePrize6);
		list.add(cakePrize5);
		list.add(cakePrize4);
		list.add(cakePrize3);
		list.add(cakePrize2);
		list.add(cakePrize1);
		
		int a = 0;
		int b = 0;
		int c = 0;
		int d = 0;
		int e = 0;
		int f = 0;
		for (int i = 0; i < 10000; i++) {
			CakePrize generateAward = generateAward(list);
			if("a".equals(generateAward.getJpCode())) {
				a++;
			}else if("b".equals(generateAward.getJpCode())) {
				b++;
			}else if("c".equals(generateAward.getJpCode())) {
				c++;
			}else if("d".equals(generateAward.getJpCode())) {
				d++;
			}else if("e".equals(generateAward.getJpCode())) {
				e++;
			}else if("f".equals(generateAward.getJpCode())) {
				f++;
			}
		}
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
		System.out.println(d);
		System.out.println(e);
		System.out.println(f);
		
	}
	
    /**
     * 生成奖项
     * @return
     */
    public static CakePrize generateAward(List<CakePrize> initDrawList) {
        long result = randomnum(1, 10000);
        int line = 0;
        int temp = 0;
        CakePrize returnobj = null;
        for (int i = 0; i < initDrawList.size(); i++) {
        	CakePrize obj2 = initDrawList.get(i);
            int c = obj2.getJpZjgl();
            temp = temp + c;
            line = 10000 - temp;
            if (c != 0) {
                if (result > line && result <= (line + c)) {
                    returnobj = obj2;
                    break;
                }
            }
        }
        return returnobj;
    }

    // 获取2个值之间的随机数
    private static long randomnum(int smin, int smax){
            int range = smax - smin;
            double rand = Math.random();
            return (smin + Math.round(rand * range));
    }


}

