package com.cmall.familyhas.service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cmall.familyhas.model.NewsNotificationModel;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
/**
 * 转换输入类
 * @author shiyz
 *
 */
public class NewsNotificationService extends BaseClass {
	
	private int pageSize = 12;
	
	
	public NewsNotificationResult getNewsInfos(String uid){
		
		NewsNotificationResult result = new NewsNotificationResult();
		String sql = "SELECT * FROM familyhas.fh_news_notification a  LEFT JOIN familyhas.fh_news_punish_detail b ON a.news_code = b.news_code WHERE a.uid = '"+uid+"';";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_news_notification").dataSqlList(sql, null);
		Map<String, Object> map = dataSqlList.get(0);
		String noticeTopic = map.get("notice_topic").toString();
		String publishTime = map.get("publish_time").toString();
		String noticeType = map.get("notice_type").toString();
		result.setNoticeTopic(noticeTopic);
		result.setPublishTime(publishTime);
		result.setNoticeType(noticeType);
		if("4497471600370001".equals(noticeType)) {//消息
			String noticeContent = map.get("notice_content").toString();
			result.setNoticeContent(noticeContent);
		}else if("4497471600370002".equals(noticeType)) {//处罚
			List<PunishModel> list = new ArrayList<>();
			for(Map<String, Object> map1  :  dataSqlList) {
				PunishModel model =  new PunishModel();
				model.setOrderCode(map1.get("order_code")==null?"":map1.get("order_code").toString());
				model.setOrderTime(map1.get("order_time")==null?"":map1.get("order_time").toString());
				model.setProductCode(map1.get("product_code")==null?"":map1.get("product_code").toString());
				model.setProductCost(map1.get("product_cost")==null?new BigDecimal(0.00):new BigDecimal(map1.get("product_cost").toString()));
				model.setProductName(map1.get("product_name")==null?"":map1.get("product_name").toString());
				model.setProductSellPrice(map1.get("product_sell_price")==null?new BigDecimal(0.00):new BigDecimal(map1.get("product_sell_price").toString()));
				model.setPunishMoney(map1.get("punish_money")==null?new BigDecimal(0.00):new BigDecimal(map1.get("punish_money").toString()));
				model.setPunishReason(map1.get("punish_reason")==null?"":map1.get("punish_reason").toString());
				list.add(model);
			}
			result.setList(list);
		}
		return result;
		
	}
	
	
	
	public SellerNewsNotificationResult getUserNewsList(String userCode,Integer pageNum){
		SellerNewsNotificationResult result =  new  SellerNewsNotificationResult();
		List<NewsNotificationModel> list = new  ArrayList<>();
		int  num =  1;
		if(1!=pageNum&&pageNum!=null) {
			num = pageNum;
		}
		int startLimit = (num-1)*pageSize;
		
		
		String sql3 =  "(SELECT  manage_code FROM zapdata.za_userinfo  WHERE user_code =  '"+userCode+"')";
		List<Map<String, Object>> newsList3 = DbUp.upTable("za_userinfo").dataSqlList(sql3, null);
		String factoryCode  =  newsList3.get(0).get("manage_code").toString();
		
		String  sql1 =  "SELECT count(*) num FROM familyhas.fh_news_notification a  WHERE status = '4497469400030002' and (company_name =  '全部' OR company_code  =  " + 
				"(SELECT  manage_code FROM zapdata.za_userinfo  WHERE user_code =  '"+userCode+"'))";
		List<Map<String, Object>> newsList1 = DbUp.upTable("fh_news_notification").dataSqlList(sql1, null);
		String totalCounts  = newsList1.get(0).get("num").toString();
		
		String  sql2 =  "SELECT count(*) num FROM familyhas.fh_news_notification a  where  (a.company_code = '"+factoryCode+"'  or a.company_name = '全部')  and a.status =  '4497469400030002' " + 
				"and not EXISTS (select b.news_code from familyhas.fh_news_confirmation b where  b.small_seller_code='"+factoryCode+"' and b.news_code =  a.news_code)";
		List<Map<String, Object>> newsList2 = DbUp.upTable("fh_news_notification").dataSqlList(sql2, null);
		String noReadCounts  = newsList2.get(0).get("num").toString();
		
		
		String  sql =  "SELECT a.*,CASE WHEN EXISTS(select b.news_code from familyhas.fh_news_confirmation b where  b.small_seller_code='"+factoryCode+"' and b.news_code =  a.news_code)  THEN 'Y' ELSE 'N' END flag " + 
				"FROM 	familyhas.fh_news_notification a  where  (a.company_code = '"+factoryCode+"'  or a.company_name = '全部')  and a.status =  '4497469400030002' ORDER BY a.publish_time DESC LIMIT "+startLimit+","+pageSize;
		
		List<Map<String, Object>> newsList = DbUp.upTable("fh_news_notification").dataSqlList(sql, null);
		for(Map<String, Object>  map : newsList) {
			String noticeType = map.get("notice_type").toString();
			String noticeTopic = map.get("notice_topic").toString();
			String newsCode = map.get("news_code").toString();
			String publishTime = "".equals(map.get("publish_time").toString().trim())?"":map.get("publish_time").toString().substring(0, 10);
			String newsUid = map.get("uid").toString();
			
			String flag = map.get("flag").toString();
			NewsNotificationModel model = new  NewsNotificationModel();
			model.setNewsCode(newsCode);
			model.setNoticeTopic(noticeTopic);
			model.setNoticeType(noticeType);
			model.setPublishTime(publishTime);
			model.setReadFlag("N".equals(flag)?"0":"1");
			model.setNewsUid(newsUid);
			list.add(model);
		}
		int totalNum = "".equals(totalCounts)?0:Integer.parseInt(totalCounts);
		result.setFactoryCode(factoryCode);
		result.setPageSize((totalNum%pageSize==0?totalNum/pageSize:(totalNum/pageSize+1))+"");
		result.setTotalNews(totalCounts);
		result.setNoReadNews(noReadCounts);
		result.setList(list);
		//如果一条数据都没有，pageSize 改为1
		if("0".equals(result.getPageSize())){
			result.setPageSize("1");
		}
		return result;
	}
	
	public NewsNotificationResult getNewsInfosByUidAndCompanyCode(String uid,String companyCode){
		NewsNotificationResult result = new NewsNotificationResult();
		//当前uid对应的消息内容 
		String sql = "SELECT a.*,b.order_code,b.order_time,b.product_code,b.product_name,b.product_cost,b.product_sell_price,b.punish_money,b.punish_reason FROM familyhas.fh_news_notification a  LEFT JOIN familyhas.fh_news_punish_detail b ON a.news_code = b.news_code WHERE a.uid = '"+uid+"';";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_news_notification").dataSqlList(sql, null);
		Map<String, Object> map = dataSqlList.get(0);
		String noticeTopic = map.get("notice_topic").toString();
		String publishTime = map.get("publish_time").toString();
		String noticeType = map.get("notice_type").toString();
		result.setNoticeTopic(noticeTopic);
		result.setPublishTime(publishTime);
		result.setNoticeType(noticeType);
		if("4497471600370001".equals(noticeType)) {//消息
			String noticeContent = map.get("notice_content").toString();
			result.setNoticeContent(noticeContent);
		}else if("4497471600370002".equals(noticeType)) {//处罚
			List<PunishModel> list = new ArrayList<>();
			for(Map<String, Object> map1  :  dataSqlList) {
				PunishModel model =  new PunishModel();
				model.setOrderCode(map1.get("order_code")==null?"":map1.get("order_code").toString());
				model.setOrderTime(map1.get("order_time")==null?"":map1.get("order_time").toString());
				model.setProductCode(map1.get("product_code")==null?"":map1.get("product_code").toString());
				model.setProductCost(map1.get("product_cost")==null?new BigDecimal(0.00):new BigDecimal(map1.get("product_cost").toString()));
				model.setProductName(map1.get("product_name")==null?"":map1.get("product_name").toString());
				model.setProductSellPrice(map1.get("product_sell_price")==null?new BigDecimal(0.00):new BigDecimal(map1.get("product_sell_price").toString()));
				model.setPunishMoney(map1.get("punish_money")==null?new BigDecimal(0.00):new BigDecimal(map1.get("punish_money").toString()));
				model.setPunishReason(map1.get("punish_reason")==null?"":map1.get("punish_reason").toString());
				list.add(model);
			}
			result.setList(list);
		}
		
		//获取上下消息uid
		String  sql1 =  "SELECT uid  FROM familyhas.fh_news_notification   WHERE status = '4497469400030002' and (company_name =  '全部' OR company_code  =  '"+companyCode+"') ORDER BY publish_time DESC" ;
		List<Map<String, Object>> dataSqlList1 = DbUp.upTable("fh_news_notification").dataSqlList(sql1, null);
		List<String> list =  new ArrayList<>(dataSqlList1.size());
		Integer bingoNum = -1;
		for(int i =  0;i<dataSqlList1.size();i++) {
			String uid1 = dataSqlList1.get(i).get("uid").toString();
			if(uid1.equals(uid)) {
				bingoNum = i;
			}
			list.add(uid1);
		}
		if(bingoNum!=-1) {
			if(bingoNum==0) {
				if(list.size()==1) {
					result.setNextUid(list.get(0));
					result.setPreUid(list.get(0));
				}else {
					result.setNextUid(list.get(bingoNum+1));
					result.setPreUid(list.get(bingoNum));
				}
			}else if(bingoNum==dataSqlList1.size()-1) {
				result.setNextUid(list.get(bingoNum));
				result.setPreUid(list.get(bingoNum-1));
			}else {
				result.setNextUid(list.get(bingoNum+1));
				result.setPreUid(list.get(bingoNum-1));
			}
			
		}
		
		//判断当前用户当前消息 是否已读，如果未读将当前消息设置为用户已读
		String newsCode = map.get("news_code").toString();
		String  sql10 =  " select * from fh_news_confirmation where news_code = '"+newsCode+"' and small_seller_code = '"+companyCode+"'" ;
		List<Map<String, Object>> dataSqlList10 = DbUp.upTable("fh_news_confirmation").dataSqlList(sql10, null);
		if(dataSqlList10!=null&&dataSqlList10.size()>0) {
			
		}else {
			userRead(newsCode,companyCode);
			//获取未读条数
			String  sql2 =  "SELECT count(*) num FROM familyhas.fh_news_notification a  where  (a.company_code = '"+companyCode+"'  or a.company_name = '全部')  and a.status =  '4497469400030002' " + 
					" and not EXISTS (select b.news_code from familyhas.fh_news_confirmation b where  b.small_seller_code='"+companyCode+"' and b.news_code =  a.news_code)";
			List<Map<String, Object>> newsList2 = DbUp.upTable("fh_news_notification").dataSqlList(sql2, null);
			String noReadCounts  = newsList2.get(0).get("num").toString();
			result.setNoReadCounts(noReadCounts);
		}
		
		return result;
	}

	public void userRead(String newsCode,String companyCode) {
		StringBuffer sql = new StringBuffer();
		String execsql = " INSERT INTO familyhas.fh_news_confirmation(uid,news_code,confirm_time,small_seller_code)  VALUES ";
		String[] split = newsCode.split(",");
		for(String str :split) {
			//消息通知表
			String sUid = UUID.randomUUID().toString().replace("-", "");
			sql.append("('"+sUid+"',");
			sql.append("'"+str+"',");
			sql.append("'"+DateUtil.getSysDateTimeString()+"',");
			sql.append("'"+companyCode+"'),");
		}
		if(sql.length() > 0) {
			execsql += sql.substring(0, sql.length()-1);
			DbUp.upTable("fh_news_confirmation").dataExec(execsql, new MDataMap());
		}
	}
	
	public void userRead2(String newsCode,String userCode) {
		if(newsCode == null) {
			System.out.println("222222222------------------------------------");
		}else {
			String sql3 =  "(SELECT  manage_code FROM zapdata.za_userinfo  WHERE user_code =  '"+userCode+"')";
			List<Map<String, Object>> newsList3 = DbUp.upTable("za_userinfo").dataSqlList(sql3, null);
			String companyCode  =  newsList3.get(0).get("manage_code").toString();
			StringBuffer sql = new StringBuffer();
			String execsql = " INSERT INTO familyhas.fh_news_confirmation(uid,news_code,confirm_time,small_seller_code)  VALUES ";
			String[] split = newsCode.split(",");
			for(String str :split) {
				//消息通知表
				String sUid = UUID.randomUUID().toString().replace("-", "");
				sql.append("('"+sUid+"',");
				sql.append("'"+str+"',");
				sql.append("'"+DateUtil.getSysDateTimeString()+"',");
				sql.append("'"+companyCode+"'),");
			}
			if(sql.length() > 0) {
				execsql += sql.substring(0, sql.length()-1);
				DbUp.upTable("fh_news_confirmation").dataExec(execsql, new MDataMap());
			}
		}
	}
	
	
	public SellerNewsNotificationResult getUserNoReadCounts(String userCode) {
		SellerNewsNotificationResult result = new SellerNewsNotificationResult();
		String sql3 =  "(SELECT  manage_code FROM zapdata.za_userinfo  WHERE user_code =  '"+userCode+"')";
		List<Map<String, Object>> newsList3 = DbUp.upTable("za_userinfo").dataSqlList(sql3, null);
		String factoryCode  =  newsList3.get(0).get("manage_code").toString();
		String  sql2 =  "SELECT count(*) num FROM familyhas.fh_news_notification a  where  (a.company_code = '"+factoryCode+"'  or a.company_name = '全部')  and a.status =  '4497469400030002' " + 
				" and not EXISTS (select b.news_code from familyhas.fh_news_confirmation b where  b.small_seller_code='"+factoryCode+"' and b.news_code =  a.news_code)";
		List<Map<String, Object>> newsList2 = DbUp.upTable("fh_news_notification").dataSqlList(sql2, null);
		String noReadCounts  = newsList2.get(0).get("num").toString();
		result.setNoReadNews(noReadCounts);
		return result;
	}
	
}

	
	




