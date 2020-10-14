package com.cmall.familyhas.api.model;


import com.srnpr.zapcom.baseannotation.ZapcomApi;


/**
 * 促销活动
 * @author 李国杰
 *
 */
public class Title {

	@ZapcomApi(value = "问题",remark="")
	private String question = "";

	@ZapcomApi(value = "答案A",remark="")
	private String answerA = "";
	
	@ZapcomApi(value = "答案B",remark="")
	private String answerB = "";
	
	@ZapcomApi(value = "答案C",remark="")
	private String answerC = "";
	
	@ZapcomApi(value = "答案D",remark="")
	private String answerD = "";
	
	@ZapcomApi(value = "正确答案",remark="A or B or C or D")
	private String bingo = "";

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswerA() {
		return answerA;
	}

	public void setAnswerA(String answerA) {
		this.answerA = answerA;
	}

	public String getAnswerB() {
		return answerB;
	}

	public void setAnswerB(String answerB) {
		this.answerB = answerB;
	}

	public String getAnswerC() {
		return answerC;
	}

	public void setAnswerC(String answerC) {
		this.answerC = answerC;
	}

	public String getAnswerD() {
		return answerD;
	}

	public void setAnswerD(String answerD) {
		this.answerD = answerD;
	}

	public String getBingo() {
		return bingo;
	}

	public void setBingo(String bingo) {
		this.bingo = bingo;
	}
	
	
	
}
