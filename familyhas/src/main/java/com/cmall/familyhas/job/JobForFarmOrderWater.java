package com.cmall.familyhas.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.quartz.JobExecutionContext;

import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 下单发货赋予水滴
 * @remark 每小时执行一次,扫lc_orderstatus表前一小时到当前时间的数据
 * @author lgx
 */
public class JobForFarmOrderWater extends RootJob {

	@Override
	public void doExecute(JobExecutionContext context) {
		
		String nowTime = DateUtil.getSysDateTimeString();
		String sSql1 = "SELECT event_code FROM sc_hudong_event_info "
				+ "WHERE event_type_code = '449748210010' AND event_status = '4497472700020002' "
				+ "AND  begin_time <= '"+nowTime+"' AND end_time >= '"+nowTime+"' ORDER BY begin_time DESC";
		Map<String, Object> eventInfoMap = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sSql1, new MDataMap());
		
		if(null!= eventInfoMap) {
			// 查询lc_orderstatus表前一小时到当前时间的数据
			String dateHour = DateUtil.addDateHour(nowTime,-1);
			String orsql = "SELECT * FROM lc_orderstatus WHERE old_status = '4497153900010002' AND now_status = '4497153900010003' AND create_time > '"+dateHour+"' AND zid > 115095565";
			List<Map<String, Object>> orList = DbUp.upTable("lc_orderstatus").dataSqlList(orsql, new MDataMap());
			if(null != orList && orList.size() > 0) {
				for (Map<String, Object> map : orList) {
					// 订单编号
					String order_code = MapUtils.getString(map, "code");
					// 查询订单信息
					MDataMap orderinfo = DbUp.upTable("oc_orderinfo").one("order_code",order_code);
					if(orderinfo != null) {
						// 看这个订单是否赠送过雨滴
						MDataMap user_water = DbUp.upTable("sc_huodong_farm_user_water").one("link_code",order_code);
						if(user_water == null) {
							// 没赠送过雨滴
							String memberCode = MapUtils.getString(orderinfo, "buyer_code");
							String eventCode = MapUtils.getString(eventInfoMap, "event_code");
							String sSql2 = "select * from systemcenter.sc_huodong_farm_user_tree where event_code=:event_code and member_code=:member_code and tree_Stage!='449748450005'";
							Map<String, Object> tree = DbUp.upTable("sc_huodong_farm_user_tree").dataSqlOne(sSql2, new MDataMap("event_code", eventCode, "member_code", memberCode));
							if(null != tree) {
								// 判断是否到达每日下单发货最大赠送雨滴次数
								String countSql = "SELECT count(1) num FROM sc_huodong_farm_user_water WHERE event_code = '"+eventCode+"' AND member_code = '"+memberCode+"' AND water_type = '449748530002' "
										+ " AND create_time >= '"+nowTime.substring(0, 10)+" 00:00:00"+"' AND create_time <= '"+nowTime.substring(0, 10)+" 23:59:59"+"'";
								Map<String, Object> countMap = DbUp.upTable("sc_huodong_farm_user_water").dataSqlOne(countSql, new MDataMap());
								String count = "0";
								if(countMap != null) {
									count = MapUtils.getString(countMap, "num");
								}
								MDataMap config = DbUp.upTable("sc_huodong_farm_config").one("type", "449748480003");
								if(Integer.parseInt(count) <= new Double(config.get("end_num")).intValue()) {
									
									DbUp.upTable("sc_huodong_farm_user_water").insert(
											"uid", WebHelper.upUuid(), 
											"event_code", eventCode, 
											"member_code", memberCode, 
											"water_code", WebHelper.upCode("FW"), 
											"water_num", config.get("begin_num"), 
											"original_water_num", config.get("begin_num"), 
											"create_time", DateUtil.getSysDateTimeString(), 
											"water_type", "449748530002", 
											"link_code", order_code, 
											"flag", "1");
								}
								
							}
						}
					}
				}
			}
		}
	}


}
