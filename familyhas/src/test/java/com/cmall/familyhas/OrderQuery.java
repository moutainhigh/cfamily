package com.cmall.familyhas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 订单通路统计
 * @author Administrator
 *
 */
public class OrderQuery {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public void statDaily1(MDataMap map) {
		Integer dailyBroadcast = new Integer(0);
		Integer app = new Integer(0);
		Integer wechat = new Integer(0);
		Integer smgWechat = new Integer(0);
		Integer smg = new Integer(0);
		Integer noBroadcast = new Integer(0);
		Integer webSite = new Integer(0);
		Integer tel01 = new Integer(0);
		Map<String,Integer> dailyType = new HashMap<String, Integer>();
		//按日期查询所有订单
		String sql ="SELECT DISTINCT order_code,order_source,order_type,app_version, create_time FROM oc_orderinfo info "
				+ "WHERE info.create_time>=:startTime AND info.create_time<=:endTime and (small_seller_code='SI2003' or small_seller_code ='')";
		List<Map<String, Object>> orderMap = DbUp.upTable("oc_orderinfo").dataSqlList(sql, map);
		if(orderMap != null && !orderMap.isEmpty()) {
			for(Map<String, Object> order : orderMap) {
				String orderCode = order.get("order_code").toString();
				sql = "select DISTINCT product_code from oc_orderdetail where order_code = '" + orderCode + "'";
				List<Map<String, Object>> orderDetailMap = DbUp.upTable("oc_orderdetail").dataSqlList(sql, null);
				List<String> productCodes = new ArrayList<String>();
				for(Map<String, Object> orderDetail : orderDetailMap) {
					productCodes.add("'" + orderDetail.get("product_code").toString() + "'");
				}
				if(inTV80(order.get("create_time").toString(), productCodes)) {
					dailyBroadcast++;
					String orderType = order.get("order_type").toString();
					String orderSource = order.get("order_source").toString();
					if(orderSource.equals("449715190002") || orderSource.equals("449715190003")) {
						app++;
					} else if(orderSource.equals("449715190006")) {
						String appversion = order.get("app_version").toString();
						Integer count = dailyType.get(appversion);
						if(count == null) {
							dailyType.put(appversion, new Integer(1));
						} else {
							dailyType.put(appversion, ++count);
						}
						wechat++;
					} else if(orderSource.equals("449715190007")) {
						smg++;
					} else if(orderSource.equals("449715190001")) {
						tel01++;
					} else if(orderSource.equals("449715190005")) {
						webSite++;
					}
				} else {
					noBroadcast++;
				}
			}
			
			System.out.println(map.get("startTime").toString() + " wechat:" + wechat + "  ");
			Set<String> keys = dailyType.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext()) {
				String key = it.next();
				System.out.print(key + ":" + dailyType.get(key) + "  ");
			}
			System.out.println();
		}
	}
	
	@Test
	public void query() {
		List<String> list = getStartEndTime("2017-05-01");
		MDataMap map = new MDataMap();
		for(String date : list) {
			String[] arr = date.split(",");
			if(!afterToday(arr[0])) {
				map.put("startTime", arr[0]);
				map.put("endTime", arr[1]);
				statDaily(map);
//				statDaily1(map);
			} else {
				break;
			}
		}
	}
	
	private boolean afterToday(String time) {
		Date date = DateUtil.toDate(time, DateUtil.sdfDateTime);
		return date.after(new Date()); 
	}
	
	private void statDaily(MDataMap map) {
		Integer dailyBroadcast = new Integer(0);
		Integer app = new Integer(0);
		Integer wechat = new Integer(0);
		Integer smgWechat = new Integer(0);
		Integer smg = new Integer(0);
		Integer noBroadcast = new Integer(0);
		Integer webSite = new Integer(0);
		Integer tel01 = new Integer(0);
		
		//按日期查询所有订单
		

		String sql ="SELECT DISTINCT order_code,order_source,order_type,app_version, create_time FROM oc_orderinfo info "
				+ "WHERE info.create_time>=:startTime AND info.create_time<=:endTime and (small_seller_code='SI2003' or small_seller_code ='')";
		List<Map<String, Object>> orderMap = DbUp.upTable("oc_orderinfo").dataSqlList(sql, map);
		if(orderMap != null && !orderMap.isEmpty()) {
			for(Map<String, Object> order : orderMap) {
				String orderCode = order.get("order_code").toString();
				sql = "select DISTINCT product_code from oc_orderdetail where order_code = '" + orderCode + "'";
				List<Map<String, Object>> orderDetailMap = DbUp.upTable("oc_orderdetail").dataSqlList(sql, null);
				List<String> productCodes = new ArrayList<String>();
				for(Map<String, Object> orderDetail : orderDetailMap) {
					productCodes.add("'" + orderDetail.get("product_code").toString() + "'");
				}
				if(inTV80(order.get("create_time").toString(), productCodes)) {
					dailyBroadcast++;
					String orderType = order.get("order_type").toString();
					String orderSource = order.get("order_source").toString();
					if(orderSource.equals("449715190002") || orderSource.equals("449715190003")) {
						app++;
					} else if(orderSource.equals("449715190006")) {
						String appversion = order.get("app_version").toString();
						if(appversion.equals("smghjy") || appversion.equals("smg_hjy")) {
							smgWechat++;
						}else {
							wechat++;
						}
					} else if(orderSource.equals("449715190007")) {
						smg++;
					} else if(orderSource.equals("449715190001")) {
						tel01++;
					} else if(orderSource.equals("449715190005")) {
						webSite++;
					}
				} else {
					noBroadcast++;
				}
			}
		}
		
		
		int changeDelay = DbUp.upTable("lc_change_channel").dataCount("create_time>=:startTime AND create_time<=:endTime and is_delay='1'", map);
//		dailyBroadcast +=changeDelay;
		
		System.out.println(map.get("startTime").toString() + "    Order:" + orderMap.size());
		System.out.print("Boradcast:");
		System.out.print("app:" + app + "  ");
		System.out.print("wechat:" + wechat + "  ");
		System.out.print("smg:" + smg + "  ");
		System.out.print("smgWechat:" + smgWechat + "  ");
		System.out.print("website:" + webSite + "  ");
		System.out.print("tel01:" + tel01 + "  ");
		System.out.println("No Boradcast:" + noBroadcast);
		
		
		System.out.print("Change:" + changeDelay + "  ");
//		Map<String, Integer> channelDelay = new HashMap<String, Integer>();
		//delay: smg->app
		int smg_app_dealy = DbUp.upTable("lc_change_channel").dataCount("create_time>=:startTime AND create_time<=:endTime and is_delay='1' and is_send='1' and appversion='smghjy'", map);
		System.out.print("smg->delay->app:" + smg_app_dealy + "  ");
		
		int smg_app_imm = DbUp.upTable("lc_change_channel").dataCount("create_time>=:startTime AND create_time<=:endTime and is_delay='0' and is_send='1' and appversion='smghjy'", map);
		System.out.print("smg->imm->app:" + smg_app_imm + "  ");
		
		//delay: app->app 
		int app_app_dealy = DbUp.upTable("lc_change_channel").dataCount("create_time>=:startTime AND create_time<=:endTime and is_delay='1' and is_send='1' "
																+ "and change_type='app->BroadDelay->app'", map);
		System.out.print("app->delay->app:" + app_app_dealy + "  ");
		
		//delay: wechat->wechat
		int wechat_wechat_dealy = DbUp.upTable("lc_change_channel").dataCount("create_time>=:startTime AND create_time<=:endTime and is_delay='1' and is_send='1' "
																+ "and change_type='wechat->BroadDelay->wechat'", map);
		System.out.print("wechat->delay->wechat:" + wechat_wechat_dealy + "  ");
		
		
		//addFlow: app->wechat
		int app_wechat_addFlow = DbUp.upTable("lc_change_channel").dataCount("create_time>=:startTime AND create_time<=:endTime and is_delay='0' and is_send='1' "
																			+ "and lclass_id='10' and mclass_id='34' and new_lclass_id='10' and new_mclass_id='42'", map);
		System.out.println("app->addFlow->wechat:" + app_wechat_addFlow + "  ");
		
		String startTime = map.get("startTime").toString().substring(0, 10);
		int count = DbUp.upTable("lc_change_channel_stat").dataCount("daytime=:daytime", new MDataMap("daytime", startTime));
		if(count >=1) {
			DbUp.upTable("lc_change_channel_stat").dataUpdate(new MDataMap("daytime", startTime, "order_ld", String.valueOf(orderMap.size()),
					"app_in_broadcast", String.valueOf(app), "wechat_in_broadcast", String.valueOf(wechat), "smg_in_broadcast", String.valueOf(smg),"website_in_broadcast",String.valueOf(webSite),
					"tel_in_broadcast",String.valueOf(tel01), "not_in_broadcast",String.valueOf(noBroadcast), "change_delay",String.valueOf(changeDelay), "smg_app_delay", String.valueOf(smg_app_dealy),
					"app_app_delay",String.valueOf(app_app_dealy), "wechat_wechat_delay",String.valueOf(wechat_wechat_dealy), "app_wechat_addflow", String.valueOf(app_wechat_addFlow), 
					"smg_app_imm", String.valueOf(smg_app_imm)), 
					"order_ld,app_in_broadcast,wechat_in_broadcast,smg_in_broadcast,website_in_broadcast,tel_in_broadcast,"
					+ "not_in_broadcast,change_delay,smg_app_delay,app_app_delay,wechat_wechat_delay,app_wechat_addflow,smg_app_imm",
					"daytime");
		} else {
			DbUp.upTable("lc_change_channel_stat").dataInsert(new MDataMap("daytime", startTime, "order_ld", String.valueOf(orderMap.size()),
					"app_in_broadcast", String.valueOf(app), "wechat_in_broadcast", String.valueOf(wechat), "smg_in_broadcast", String.valueOf(smg),"website_in_broadcast",String.valueOf(webSite),
					"tel_in_broadcast",String.valueOf(tel01), "not_in_broadcast",String.valueOf(noBroadcast), "change_delay",String.valueOf(changeDelay), "smg_app_delay", String.valueOf(smg_app_dealy),
					"app_app_delay",String.valueOf(app_app_dealy), "wechat_wechat_delay",String.valueOf(wechat_wechat_dealy), "app_wechat_addflow", String.valueOf(app_wechat_addFlow), 
					"smg_app_imm", String.valueOf(smg_app_imm)));
		}
	}
	
	
	private List<String> getStartEndTime(String strDate) {
		
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(sdf.parse(strDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int totalDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		List<String> list = new ArrayList<String>();
		for(int i=1; i<=totalDays; i++){
		  c.set(Calendar.DAY_OF_MONTH, i);
		  Date date = c.getTime();
		  String today = sdf.format(date);
		  list.add(today + " 00:00:00" + "," + today + " 23:59:59");
		}
		return list;
	}
	
	private boolean inTV80(String create_time, List<String> goodids) {
		// 扫码购添加新逻辑
		// 订单创建时间-TV开始时间<80分钟
//		String create_time = order.getCreateTime();// 订单创建时间
		MDataMap tvInfo = DbUp.upTable("pc_tv").oneWhere("form_fr_date,form_end_date", "form_fr_date desc",
				"form_fr_date<:create_time and good_id in (" + StringUtils.join(goodids, ",") + ")", "create_time",
				create_time);
		//System.out.println("select * from productcenter.pc_tv where form_fr_date<'" + create_time + "' and good_id in (" + StringUtils.join(goodids, ",") + ")");
		if (tvInfo != null && !tvInfo.isEmpty()) {
			String form_fr_date = tvInfo.get("form_fr_date");
			if (!timeDiffer(create_time, form_fr_date, 80 * 60)) {
				return true;
			}
		} 
		return false;
	}
	
	private synchronized boolean timeDiffer(String time1,String time2,long sec){
		
		Date date1=DateUtil.toDate(time1,DateUtil.sdfDateTime);
		Date date2=DateUtil.toDate(time2,DateUtil.sdfDateTime);
		if((date1.getTime()-date2.getTime())>sec*1000){
			return true;
		}
		return false;
	}
}
