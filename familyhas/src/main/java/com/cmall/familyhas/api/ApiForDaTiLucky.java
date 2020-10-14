package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.cmall.familyhas.api.input.HongbaoLuckyInput;
import com.cmall.familyhas.api.model.HongbaoLuckyModel;
import com.cmall.familyhas.api.result.HongbaoLuckyResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.ordercenter.service.CouponsService;
import com.srnpr.zapcom.basehelper.SecrurityHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForDaTiLucky extends RootApiForToken<HongbaoLuckyResult, HongbaoLuckyInput> {

	String  secKey = bConfig("familyhas.sec_key");
	@Override
	public HongbaoLuckyResult Process(HongbaoLuckyInput inputParam, MDataMap mRequestMap) {
		HongbaoLuckyResult result =new HongbaoLuckyResult();
		String phone = inputParam.getPhone();
		String reqTime = inputParam.getReqTime();
		String strSec = phone + reqTime + secKey;
		String secResult = inputParam.getResultStr();
		
		String localSecResult = SecrurityHelper.MD5(strSec).toLowerCase();
		
		if(!localSecResult.equals(secResult)) {
			result.setResultCode(0);
			result.setResultMessage("验签失败");
			return  result;
		}
		
		String sql = "select * from lc_hongbaoyu_lucky_log a where a.choujiang_flag = '"+ (phone +reqTime)+"'";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("lc_hongbaoyu_lucky_log").dataSqlList(sql, null);
		if(null!=dataSqlList&&dataSqlList.size()>0) {
			result.setResultCode(0);
			result.setResultMessage("不可重复拆红包");
			return  result;
		}
		//查询当前答题 活动优惠券信息
		
		String sql1 = "select a.* from sc_hudong_event_hongbao_info a left join sc_hudong_event_info b on a.event_code = b.event_code"
						+ " where b.event_type_code = '449748210002' and b.event_status = '4497472700020002' and b.begin_time < now() and b.end_time > now()"
				+ "";
		List<Map<String, Object>> dataSqlList1 = DbUp.upTable("sc_hudong_event_hongbao_info").dataSqlList(sql1, null);
		if(null!=dataSqlList1&&dataSqlList1.size()>0) {
			List<HongbaoLuckyModel> list = new ArrayList<HongbaoLuckyModel>();
			Integer totalNum = 0;
			for(Map<String, Object> map : dataSqlList1) {
				HongbaoLuckyModel hongbaoLuckyModel = new HongbaoLuckyModel();
				String eventAward = map.get("event_award").toString();
				Integer probability = (Integer)map.get("award_probability");
				hongbaoLuckyModel.setLittleNum(totalNum+1);
				totalNum += probability;
				hongbaoLuckyModel.setEventAward(eventAward);
				hongbaoLuckyModel.setProbability(probability);
				hongbaoLuckyModel.setMaxNum(totalNum);
				list.add(hongbaoLuckyModel);
			}
			
			//产生1-totalNum 的随机数
			Random random = new Random();
			int nextInt = random.nextInt(totalNum) + 1;
			HongbaoLuckyModel hongbaoLuckyModel = new HongbaoLuckyModel();
			for(HongbaoLuckyModel hongbaoLucky : list) {
				Integer littleNum = hongbaoLucky.getLittleNum();
				Integer maxNum = hongbaoLucky.getMaxNum();
				if(littleNum<=nextInt && maxNum>=nextInt) {
					hongbaoLuckyModel = hongbaoLucky;
					break;
				}
			}
			
			//抽完奖，发奖励，写日志
			String eventAward = hongbaoLuckyModel.getEventAward();
			if("".equals(eventAward)) {
				result.setBingoFlag(0);
				result.setBingoDes("很遗憾，没有中奖");
			}else {
				String sql2 = "select coupon_type_name,money,money_type from oc_coupon_type a where a.coupon_type_code = '"+ eventAward +"'";
				List<Map<String, Object>> dataSqlList2 = DbUp.upTable("oc_coupon_type").dataSqlList(sql2, null);
				if(null!=dataSqlList2&&dataSqlList2.size()>0) {
					for(Map<String, Object>  map : dataSqlList2 ) {
						result.setBingoFlag(1);
						result.setJiangpinType(map.get("money_type").toString());
						result.setJiangpinNum(((BigDecimal)map.get("money")).doubleValue());
						result.setBingoDes(map.get("coupon_type_name").toString());
					}
					//发优惠券
					CouponsService couponsService = new CouponsService();
					RootResultWeb result2 = couponsService.distributeCouponsByCouponCode(eventAward, phone, "2");
					if(result2.getResultCode()==1) {
						
					}else {
						result.setResultCode(result2.getResultCode());
						result.setResultMessage(result2.getResultMessage());
					}
				}
			}
			
			
			//写日志 lc_hongbaoyu_lucky_log
			MDataMap log = new MDataMap();
			log.put("choujiang_time", DateUtil.getNowTime());
			log.put("zhongjiang_num", eventAward);
			log.put("choujiang_flag", phone+reqTime);
			if(result.getResultCode()!=1) {
				log.put("success_flag", "0");
			}
			
			DbUp.upTable("lc_hongbaoyu_lucky_log").dataInsert(log);
		}else {
			result.setResultCode(0);
			result.setResultMessage("当前时间无答题活动");
			return  result;
		}
		
		return result;
	}


}
