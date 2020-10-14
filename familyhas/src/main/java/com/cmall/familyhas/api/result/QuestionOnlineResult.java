package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;


/**
 * @title: com.cmall.familyhas.api.result.QuestionOnlineResult.java 
 * @description: 返回数据报文格式如下：
			 {
			    "questionList": [
			        {
			            "title": "Q1.海外商品如何保证正品？",
			            "content": "所销售的海外商品均通过官方销售渠道采购"
			        },
			        {
			            "title": "Q2.运费和税费如何收取？",
			            "content": "除新疆西藏等偏远地区都是包邮且免税的"
			        }
			    ],
			    "customer": [
			        {
			            "type": "2",                          // 1:在线客服， 2：电话客服 3： 微信客服
			            "content": "010-69568888"   //在线客服为空，电话客服返回电话号码，微信客服根据需求返回具体内容
			        }
			    ]
			}
			
			449747890001 在线客服
			449747890002 电话客服
			449747890003 微信客服
			
 * @author Yangcl
 * @date 2016年9月21日 下午5:04:12 
 * @version 1.0.0
 */
public class QuestionOnlineResult extends RootResult {
	
	@ZapcomApi(value="问题列表")
	private List<QuestionItem> questionList = new ArrayList<QuestionItem>();
	
	@ZapcomApi(value="1:在线客服(返回空) 2：电话客服 3： 微信客服(字符串)")
	private List<Customer> customer = new ArrayList<Customer>();
	
	public List<QuestionItem> getQuestionList() {
		return questionList;
	}
	public void setQuestionList(List<QuestionItem> questionList) {
		this.questionList = questionList;
	}
	public List<Customer> getCustomer() {
		return customer;
	}
	public void setCustomer(List<Customer> customer) {
		this.customer = customer;
	}
}


















