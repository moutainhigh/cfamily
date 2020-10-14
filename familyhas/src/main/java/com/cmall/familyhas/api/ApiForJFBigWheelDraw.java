package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiForJFBigWheelDrawInput;
import com.cmall.familyhas.api.model.HuodongEventDzpjfRule;
import com.cmall.familyhas.api.result.ApiForJFBigWheelDrawResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 积分大转盘-抽奖接口
 * @author lgx
 *
 */
public class ApiForJFBigWheelDraw extends RootApiForToken<ApiForJFBigWheelDrawResult, ApiForJFBigWheelDrawInput> {
	
	// 积分转盘兑换抽奖次数时消耗的积分数
	private final String jf_bigwheel_need_jfnum = bConfig("familyhas.jf_bigwheel_need_jfnum");
	// 积分转盘每次兑换的抽奖次数
	private final String jf_bigWheel_drawnum = bConfig("familyhas.jf_bigWheel_drawnum");
	// 积分转盘抽奖限定开始时间
	//private final String jf_bigWheel_begin_time = bConfig("familyhas.jf_bigWheel_begin_time");
	// 积分转盘抽奖限定结束时间
	//private final String jf_bigWheel_end_time = bConfig("familyhas.jf_bigWheel_end_time");

	public ApiForJFBigWheelDrawResult Process(ApiForJFBigWheelDrawInput inputParam, MDataMap mRequestMap) {
		ApiForJFBigWheelDrawResult result = new ApiForJFBigWheelDrawResult();
		
		String memberCode = getUserCode();
		String eventCode = "";
		
		String nowTime = FormatHelper.upDateTime();
		//String startTime = nowTime.substring(0, 10)+" 00:00:00";
		//String endTime = nowTime.substring(0, 10)+" 23:59:59";
		
		String sSql1 = "SELECT * FROM sc_hudong_event_info WHERE event_type_code = '449748210006' AND event_status = '4497472700020002' AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time ";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(eventInfoMap == null) {
			result.setResultCode(-1);
			result.setResultMessage("当前时间没有发布的积分转盘活动!");
			return result;
		}else {
			eventCode = (String) eventInfoMap.get("event_code");
		}
		
		// 获取用户剩余抽奖次数
		String sql1 = "SELECT * FROM sc_huodong_event_sycs WHERE member_code = '"+ memberCode +"' AND event_code = '" + eventCode + "'";
		Map<String, Object> drawNumMap = DbUp.upTable("sc_huodong_event_sycs").dataSqlOne(sql1, new MDataMap());
		int sycs = (int) drawNumMap.get("sycs");
		if(sycs > 0) {
			// 大转盘奖品信息
			String sSql2 = "SELECT * FROM sc_huodong_event_dzpjf_rule WHERE event_code = '" + eventCode +"'";
			List<Map<String, Object>> eventDzpjfList = DbUp.upTable("sc_huodong_event_dzpjf_rule").dataSqlList(sSql2, new MDataMap());
			
			List<HuodongEventDzpjfRule> initDrawList = new ArrayList<HuodongEventDzpjfRule>();
			for (Map<String, Object> map : eventDzpjfList) {
				HuodongEventDzpjfRule huodongEventDzpjfRule = new HuodongEventDzpjfRule();
				huodongEventDzpjfRule.setJpCode(map.get("jp_code").toString());
				huodongEventDzpjfRule.setJpTitle(map.get("jp_title").toString());
				huodongEventDzpjfRule.setJpZjgl((int) map.get("jp_zjgl"));
				huodongEventDzpjfRule.setJpImg(map.get("jp_img").toString());
				huodongEventDzpjfRule.setJpNum((int) map.get("jp_num"));
				initDrawList.add(huodongEventDzpjfRule);
			}
			
			// 开始抽奖,返回抽中奖品信息
			HuodongEventDzpjfRule draw = generateAward(initDrawList);
			
			if("惠".equals(draw.getJpTitle())) {
				// 如果抽到"惠",首先查询当前用户是否已经抽到过"惠"
				int countHui = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '惠'", new MDataMap());
				if(countHui > 0) {
					// 如果抽到了"惠",则返回"爱"
					String sqlAi = "SELECT * FROM sc_huodong_event_dzpjf_rule WHERE event_code = '" + eventCode +"' and jp_title = '爱'";
					Map<String, Object> aiMap = DbUp.upTable("sc_huodong_event_dzpjf_rule").dataSqlOne(sqlAi, new MDataMap());
					draw = new HuodongEventDzpjfRule();
					draw.setJpCode((String) aiMap.get("jp_code"));
					draw.setJpTitle((String) aiMap.get("jp_title"));
				}else {
					// 如果没抽到过,看当前时间段内"惠"字是否有剩余
					String sqlxd = "SELECT * FROM lc_huodong_event_zjxd WHERE event_code = '"+eventCode+"' AND begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"'";
					Map<String, Object> zjxdMap = DbUp.upTable("lc_huodong_event_zjxd").dataSqlOne(sqlxd, new MDataMap());
					// 当前时间段每小时抽中
					int hnum = 0;
					if(zjxdMap != null) {						
						hnum = (int) zjxdMap.get("hjp_num");
					}
					if(hnum > 0) {
						// "惠"字有剩余,更新当前时间段的"惠"字减1,并且更新总的"惠"字减1
						// 更新当前时间段的"惠"字减1
						hnum = hnum - 1;
						MDataMap mDataMap1 = new MDataMap();
						mDataMap1.put("uid", (String) zjxdMap.get("uid"));
						mDataMap1.put("hjp_num", hnum+"");
						DbUp.upTable("lc_huodong_event_zjxd").dataUpdate(mDataMap1, "hjp_num", "uid");
						// 更新总的"惠"字减1
						String sqlHui = "SELECT * FROM sc_huodong_event_dzpjf_rule WHERE event_code = '" + eventCode +"' and jp_title = '惠'";
						Map<String, Object> huiMap = DbUp.upTable("sc_huodong_event_dzpjf_rule").dataSqlOne(sqlHui, new MDataMap());
						int jpnum = (int) huiMap.get("jp_num");
						if(jpnum > 0) {
							jpnum = jpnum - 1;
							MDataMap mDataMap2 = new MDataMap();
							mDataMap2.put("uid", (String) huiMap.get("uid"));
							mDataMap2.put("jp_num", jpnum+"");
							DbUp.upTable("sc_huodong_event_dzpjf_rule").dataUpdate(mDataMap2, "jp_num", "uid");
						}
					}else {
						// 当前时间段"惠"字没有了,返回"家"
						String sqlJia = "SELECT * FROM sc_huodong_event_dzpjf_rule WHERE event_code = '" + eventCode +"' and jp_title = '家'";
						Map<String, Object> jiaMap = DbUp.upTable("sc_huodong_event_dzpjf_rule").dataSqlOne(sqlJia, new MDataMap());
						draw = new HuodongEventDzpjfRule();
						draw.setJpCode((String) jiaMap.get("jp_code"));
						draw.setJpTitle((String) jiaMap.get("jp_title"));
					}
				}
			}

			// 如果抽到的不是"惠",直接返回抽到的奖品名称
			result.setJpTitle(draw.getJpTitle());
			
			// 更新剩余抽奖次数减1
			sycs = sycs - 1;
			MDataMap mDataMap4 = new MDataMap();				
			mDataMap4.put("uid", (String) drawNumMap.get("uid"));
			mDataMap4.put("sycs", sycs+"");
			mDataMap4.put("update_time", nowTime);
			DbUp.upTable("sc_huodong_event_sycs").dataUpdate(mDataMap4, "sycs,update_time", "uid");
			result.setRemainDrawNum(sycs);
			
			// 每个字抽中个数			
			int count1 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '我'", new MDataMap());
			int count2 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '爱'", new MDataMap());
			int count3 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '惠'", new MDataMap());
			int count4 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '家'", new MDataMap());
			int count5 = DbUp.upTable("lc_huodong_event_jl").dataCount("event_code = '"+eventCode+"' and member_code = '"+memberCode+"' and jp_title = '有'", new MDataMap());
			result.setCount1("我".equals(draw.getJpTitle())?(count1+1):count1);
			result.setCount2("爱".equals(draw.getJpTitle())?(count2+1):count2);
			result.setCount3("惠".equals(draw.getJpTitle())?(count3+1):count3);
			result.setCount4("家".equals(draw.getJpTitle())?(count4+1):count4);
			result.setCount5("有".equals(draw.getJpTitle())?(count5+1):count5);
			
			// 添加抽奖记录
			MDataMap mDataMap5 = new MDataMap();
			mDataMap5.put("uid", WebHelper.upUuid());
			mDataMap5.put("event_code", eventCode);
			// 抽奖编码
			String jpCodeSeq = WebHelper.upCode("CJJL");
			mDataMap5.put("jp_code_seq", jpCodeSeq);
			mDataMap5.put("jp_code", draw.getJpCode());
			mDataMap5.put("member_code", memberCode);
			// 用户昵称
			String sql2 = "SELECT * FROM mc_member_sync WHERE member_code = '"+memberCode+"'";
			Map<String, Object> member = DbUp.upTable("mc_member_sync").dataSqlOne(sql2, new MDataMap());
			mDataMap5.put("nickname", member.get("nickname").toString());
			mDataMap5.put("lj_yn", "Y");
			mDataMap5.put("zj_time", nowTime);
			mDataMap5.put("jp_title", draw.getJpTitle());
			DbUp.upTable("lc_huodong_event_jl").dataInsert(mDataMap5);
			
			// 不用弹出积分兑换次数窗口
			result.setIsNeedExchange("N");
			
		}else {
			// 弹出积分兑换次数窗口
			result.setIsNeedExchange("Y");
			result.setResultMessage("是否花费"+jf_bigwheel_need_jfnum+"积分兑换"+jf_bigWheel_drawnum+"次抽奖机会?");
			return result;
		}
		
		return result;
	}



	    /**
	     * 生成奖项
	     * @return
	     */
	    public static HuodongEventDzpjfRule generateAward(List<HuodongEventDzpjfRule> initDrawList) {
	        //List<HuodongEventDzpjpRule> initData = initDrawList;
	        long result = randomnum(1, 100);
	        int line = 0;
	        int temp = 0;
	        HuodongEventDzpjfRule returnobj = null;
	        //int index = 0;
	        for (int i = 0; i < initDrawList.size(); i++) {
	        	HuodongEventDzpjfRule obj2 = initDrawList.get(i);
	            int c = obj2.getJpZjgl();
	            temp = temp + c;
	            line = 100 - temp;
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

