package com.cmall.familyhas.webfunc;

import java.util.Map;
import java.util.UUID;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd; 
import com.srnpr.zapweb.webmodel.MWebResult;

public class AddNewsNotification extends FuncAdd{
	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) { 
		
		MWebResult result = new MWebResult();
		
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		//插入消息通知表
		String execSql = " INSERT INTO familyhas.fh_news_notification(uid,news_code,company_code,company_name,notice_topic,notice_type,notice_content,opening_merchant_confirmation,proclamation_code)  VALUES ";
		String sUid = UUID.randomUUID().toString().replace("-", "");
		String tzCode = WebHelper.upCode("TZ");
		
		String noticeType = mAddMaps.get("notice_type");
		String noticeTopic = mAddMaps.get("notice_topic");
		if("4497471600370002".equals(noticeType)) {//处罚
			try {
				String companyCode = mAddMaps.get("company_code");
				String sssql = "SELECT seller_name  from  usercenter.uc_sellerinfo  WHERE small_seller_code = '"+companyCode+"' ;";
				Map<String,Object> map = DbUp.upTable("uc_sellerinfo").dataSqlOne(sssql, null);
				String companyName =  map.get("seller_name")==null?"":map.get("seller_name").toString();
				//插入消息通知表
				execSql  = execSql + "('"+sUid+"','"+tzCode+"','"+companyCode+"','"+companyName+"','"+noticeTopic+"','"+noticeType+"','','','')";
				DbUp.upTable("fh_news_notification").dataExec(execSql, new MDataMap());
				
				//插入处罚明细表 
				StringBuffer sql1 = new StringBuffer();
				String execsql1 = " INSERT INTO familyhas.fh_news_punish_detail(uid,news_code,order_code,order_time,product_code,product_name,product_cost,product_sell_price,punish_money,punish_reason)  VALUES ";
				
				
				String orderTime = mAddMaps.get("order_time");
				String orderCode = mAddMaps.get("order_code");
				
				String productsInfo = mAddMaps.get("products_info");
				//处理罚款商品信息
				String[] split = productsInfo.split(",");
				for(String str : split) {
					String[] split2 = str.split("=");
					String skuCode = split2[0];
					String skuName = split2[1];
					String skuCost = split2[2];
					String skuSellPrice = split2[3];
					String punishMoney = split2[4];
					String punishReason = split2[5];
					
					//处罚明细表
					String sUid1 = UUID.randomUUID().toString().replace("-", "");
					sql1.append("('"+sUid1+"',");
					sql1.append("'"+tzCode+"',");
					sql1.append("'"+orderCode.replace("'", "\\'")+"',");
					sql1.append("'"+orderTime.replace("'", "\\'")+"',");
					sql1.append("'"+skuCode.replace("'", "\\'")+"',");
					sql1.append("'"+skuName.replace("'", "\\'")+"',");
					sql1.append("'"+skuCost.toString().replace("'", "\\'")+"',");
					sql1.append("'"+skuSellPrice.toString().replace("'", "\\'")+"',");
					sql1.append("'"+punishMoney.toString().replace("'", "\\'")+"',");
					sql1.append("'"+punishReason.replace("'", "\\'")+"'),");
				}
				
				if(sql1.length() > 0) {
					execsql1 += sql1.substring(0, sql1.length()-1);
						DbUp.upTable("fh_news_punish_detail").dataExec(execsql1, new MDataMap());
				}
			}
			catch(Exception e) {
				result.setResultCode(0);
				result.setResultMessage("添加失败");
			}
		}else if("4497471600370001".equals(noticeType)){//消息
			try {
				String noticeContent = mAddMaps.get("notice_content");
				String openingMerchantConfirmation = mDataMap.get("zw_f_opening_merchant_confirmation");
				String proclamation_code = WebHelper.upCode("GG");
				execSql  = execSql + "('"+sUid+"','"+tzCode+"','','全部','"+noticeTopic+"','"+noticeType+"','"+noticeContent+"','"+openingMerchantConfirmation+"','"+proclamation_code+"')";
			
				DbUp.upTable("fh_news_notification").dataExec(execSql, new MDataMap());
				//插入消息表
				MDataMap mDataMap2 = new MDataMap();
				String createUser = UserFactory.INSTANCE.create().getLoginName();
				String proclamationTitle = mDataMap.get("zw_f_notice_topic");
				String possessProject = "4497467900050002";
				//String releaseTime = mDataMap.get("zw_f_release_time");
				String proclamationText = noticeContent;
				
				if(null!=openingMerchantConfirmation&&"449746250001".equals(openingMerchantConfirmation)) {
					mDataMap2.put("opening_merchant_confirmation", openingMerchantConfirmation);
					String proclamationTitleConfirmation = mDataMap.get("zw_f_notice_topic");;
					mDataMap2.put("proclamation_title_confirmation", proclamationTitleConfirmation);
				}
				mDataMap2.put("create_user", createUser);
				mDataMap2.put("proclamation_title", proclamationTitle);
				mDataMap2.put("possess_project", possessProject);
				//mDataMap2.put("release_time", releaseTime);
				mDataMap2.put("proclamation_text", proclamationText);
				mDataMap2.put("proclamation_code", proclamation_code);
				mDataMap2.put("proclamation_or_news", "2");
				DbUp.upTable("fh_proclamation_manage").dataInsert(mDataMap2);
			}catch(Exception e) {
				result.setResultCode(0);
				result.setResultMessage("添加失败");
			}
		}
		return result;
		
	}
}
