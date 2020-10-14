package com.cmall.familyhas.job;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobForClearUserBrowesHistory extends RootJob{

	
//	@Override
//	public void doExecute(JobExecutionContext context) {
//		Date startTime = new Date();
//		System.out.println("浏览历史定时开始时间为:"+startTime+"-------------------------------------");
//		//查询浏览记录超过200的用户编号
//		String sql = "SELECT member_code,COUNT(*) totalNum FROM pc_browse_history GROUP BY member_code HAVING COUNT(*) > 200;";
//		List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_browse_history").dataSqlList(sql, null);
//		if(null!=dataSqlList&&dataSqlList.size()>0) {
//			for(int j= 0 ;j < dataSqlList.size();j++) {
//				
//				if(j%50==0) {
//					//每执行50个人，看看定时有没有超过2小时，如果超过两小时，跳出循环
//					Date currentTime = new Date();
//					if(currentTime.getTime() - startTime.getTime() >= 2*60*60*1000) {
//						break;
//					}
//				}
//				
//				Map<String, Object> map = dataSqlList.get(j);
//				String memberCode = map.get("member_code").toString();
//				String sql1 = "SELECT zid,member_code,product_code FROM pc_browse_history WHERE member_code = '"+memberCode+"' ORDER BY create_time DESC;";
//				List<Map<String, Object>> dataSqlList1 = DbUp.upTable("pc_browse_history").dataSqlList(sql1, null);
//				if(null!=dataSqlList1&&dataSqlList1.size()>200) {
//					for(int i = 200;i<dataSqlList1.size();i++) {
//						Map<String, Object> map2 = dataSqlList1.get(i);
//						String zid = map2.get("zid").toString();
//						String member_code = map2.get("member_code").toString();
//						String product_code = map2.get("product_code").toString();
//						
//						//获取登录人编号
//						//清空指定浏览记录
//						DbUp.upTable("pc_browse_history").delete("zid",zid);
//						//浏览记录日志表置清除位. add by zht
//						MDataMap map3 = new MDataMap();
//						map3.put("clear_time", DateUtil.getNowTime());
//						map3.put("status", "1");
//						map3.put("member_code", member_code);
//						map3.put("product_code", product_code);
//						String sql3 = "update pc_browse_history_log set status=:status, clear_time=:clear_time where member_code=:member_code and product_code=:product_code and status=0";
//						DbUp.upTable("pc_browse_history_log").dataExec(sql3, map3);
//					}
//				}
//			}
//		}
//		System.out.println("耗时:"+(new Date().getTime()-startTime.getTime())/1000+"秒-------------------------------------");
//	}
	
	
	@Override
	public void doExecute(JobExecutionContext context) {
		Date startTime = new Date();
		System.out.println("浏览历史定时开始时间为:"+startTime+"-------------------------------------");
		//查询浏览记录超过200的用户编号
		String sql = "SELECT member_code,COUNT(*) totalNum FROM pc_browse_history GROUP BY member_code HAVING COUNT(*) > 200;";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_browse_history").dataSqlList(sql, null);
		if(null!=dataSqlList&&dataSqlList.size()>0) {
			for(int j= 0 ;j < dataSqlList.size();j++) {
				
				if(j%100==0) {
					//每执行30个人，看看定时有没有超过2小时，如果超过两小时，跳出循环
					Date currentTime = new Date();
					if(currentTime.getTime() - startTime.getTime() >= 2*60*60*1000) {
						break;
					}
				}
				
				Map<String, Object> map = dataSqlList.get(j);
				String memberCode = map.get("member_code").toString();
				String sql1 = "SELECT zid,member_code,product_code FROM pc_browse_history WHERE member_code = '"+memberCode+"' ORDER BY create_time DESC limit 200,20000;";
				List<Map<String, Object>> dataSqlList1 = DbUp.upTable("pc_browse_history").dataSqlList(sql1, null);
				if(null!=dataSqlList1&&dataSqlList1.size()>0) {
//					Map<String,String>[] batchValues = new Map[dataSqlList1.size()-200];
					StringBuffer zids = new StringBuffer();
					
					StringBuffer products = new StringBuffer();
					
					for(int i = 0;i<dataSqlList1.size();i++) {
						Map<String, Object> map2 = dataSqlList1.get(i);
						String zid = map2.get("zid").toString();
//						String member_code = map2.get("member_code").toString();
						String product_code = map2.get("product_code").toString();
						
						//获取登录人编号
						//清空指定浏览记录
						if(zids.length()==0) {
							zids.append("(");
						}
						if(products.length()==0) {
							products.append("(");
						}
						zids = zids.append(zid+",");
						
						products = products.append("'"+product_code+"',");
						
//						Map<String,String> value1 = new HashMap<>();
//						value1.put("clear_time", DateUtil.getNowTime());
//						value1.put("status", "1");
//						value1.put("member_code", member_code);
//						value1.put("product_code", product_code);
//						batchValues[i-200] = value1;
					}
					
					if(zids.length()>0) {
						zids = zids.deleteCharAt(zids.length()-1);
						zids.append(")");
						String sql5 = "delete from pc_browse_history where zid in " +zids;
						DbUp.upTable("pc_browse_history").dataExec(sql5, new MDataMap());
					}
					
					
					
					if(products.length()>0) {
						products = products.deleteCharAt(products.length()-1);
						products.append(")");
						String sql5 = "update pc_browse_history_log set status=:status, clear_time=:clear_time where member_code=:member_code and product_code in "+products+" and status=0";
						MDataMap mDataMap = new MDataMap();
						mDataMap.put("clear_time", DateUtil.getNowTime());
						mDataMap.put("status", "1");
						mDataMap.put("member_code", memberCode);
						DbUp.upTable("pc_browse_history_log").dataExec(sql5,mDataMap );
					}
					
					
//					String sql3 = "update pc_browse_history_log set status=:status, clear_time=:clear_time where member_code=:member_code and product_code=:product_code and status=0";
//					DbUp.upTable("pc_browse_history_log").upTemplate().batchUpdate(sql3, batchValues);
				}
			}
		}
		System.out.println("耗时:"+(new Date().getTime()-startTime.getTime())/1000+"秒-------------------------------------");
	}
	
}
