package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cmall.familyhas.api.input.SellerBatchReadInput;
import com.cmall.familyhas.api.result.SellerBatchReadResult;
import com.cmall.familyhas.service.NewsNotificationService;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 惠家有类目接口
 * 
 * @author xiegj
 *
 */
public class APiForSellerBatchRead extends RootApi<SellerBatchReadResult, SellerBatchReadInput> {

	public SellerBatchReadResult Process(SellerBatchReadInput input,
			MDataMap mRequestMap) {
		SellerBatchReadResult result = new SellerBatchReadResult();
		String userCode = input.getUserCode();
		String newsCodes = input.getNewsCodes();
		String sql3 =  "(SELECT  manage_code FROM zapdata.za_userinfo  WHERE user_code =  '"+userCode+"')";
		List<Map<String, Object>> newsList3 = DbUp.upTable("za_userinfo").dataSqlList(sql3, null);
		String companyCode  =  newsList3.get(0).get("manage_code").toString();
		StringBuffer sql = new StringBuffer();
		String execsql = " INSERT INTO familyhas.fh_news_confirmation(uid,news_code,confirm_time,small_seller_code)  VALUES ";
		String[] split = newsCodes.split(",");
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
		
		String noReadNews = new NewsNotificationService().getUserNoReadCounts(userCode).getNoReadNews();
		result.setNoReadCounts(noReadNews);
		return result;
	}
}
