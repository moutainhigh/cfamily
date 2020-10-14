package com.cmall.familyhas.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.QuestionOnlineInput;
import com.cmall.familyhas.api.result.Customer;
import com.cmall.familyhas.api.result.QuestionItem;
import com.cmall.familyhas.api.result.QuestionOnlineResult;
import com.srnpr.zapcom.basehelper.DateHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForMember;

/**
 * @title: com.cmall.familyhas.api.ApiQuestionOnline.java 
 * @description: 根据渠道类型返回在线问题
 * 	位置信息：API信息 -> 惠家有 -> 根据渠道类型返回在线问题列表
 *
 * @author Yangcl
 * @date 2016年9月21日 下午4:39:53 
 * @version 1.0.0
 */
public class ApiQuestionOnline extends RootApiForMember<QuestionOnlineResult , QuestionOnlineInput>{


	public QuestionOnlineResult Process(QuestionOnlineInput inputParam, MDataMap mRequestMap){
		QuestionOnlineResult result = new QuestionOnlineResult();
		String curr = DateHelper.upDate(new Date());
		String where = " select * from sc_question_online where type = '449747900001' and ('" + curr + "' between begin_time and end_time) limit 1"; 
		List<Map<String, Object>> list = DbUp.upTable("sc_question_online").dataSqlList(where, null);
		if(list != null && list.size() > 0){
			result.getCustomer().add(new Customer("1" , ""));
		}
		result.getCustomer().add(new Customer("2" , bConfig("familyhas.serviceTelBysharpei"))); // 客服电话
		result.getCustomer().add(new Customer("3" , "hjy-home"));
		
		String datawhere = " select * from fh_common_problem where seller_code = 'SI2003' and type = '" + inputParam.getType() + "'"; 
		List<Map<String, Object>> questionList = DbUp.upTable("fh_common_problem").dataSqlList(datawhere, null);
		if(questionList == null || questionList.size() == 0){
			return result;
		}
		for(Map<String, Object> entity : questionList){
			QuestionItem i = new QuestionItem();
			i.setTitle( String.valueOf(entity.get("title")) );
			i.setContent( String.valueOf(entity.get("content")) );
			result.getQuestionList().add(i);
		}
		
		return result;  
	}


}










































































