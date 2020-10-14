package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForJFBigWheelDrawResult extends RootResult {

	@ZapcomApi(value = "是否弹出积分兑换次数窗口")
	private String isNeedExchange = "N";
	
	@ZapcomApi(value = "当前中奖的奖品名称")
	private String jpTitle = "";
	
	@ZapcomApi(value = "剩余抽奖次数")
	private int remainDrawNum = 0;
	
	@ZapcomApi(value = "抽中'我'字的个数")
	private int count1 = 0;
	@ZapcomApi(value = "抽中'爱'字的个数")
	private int count2 = 0;
	@ZapcomApi(value = "抽中'惠'字的个数")
	private int count3 = 0;
	@ZapcomApi(value = "抽中'家'字的个数")
	private int count4 = 0;
	@ZapcomApi(value = "抽中'有'字的个数")
	private int count5 = 0;
	
	public String getIsNeedExchange() {
		return isNeedExchange;
	}
	public void setIsNeedExchange(String isNeedExchange) {
		this.isNeedExchange = isNeedExchange;
	}
	public String getJpTitle() {
		return jpTitle;
	}
	public void setJpTitle(String jpTitle) {
		this.jpTitle = jpTitle;
	}
	public int getRemainDrawNum() {
		return remainDrawNum;
	}
	public void setRemainDrawNum(int remainDrawNum) {
		this.remainDrawNum = remainDrawNum;
	}
	public int getCount1() {
		return count1;
	}
	public void setCount1(int count1) {
		this.count1 = count1;
	}
	public int getCount2() {
		return count2;
	}
	public void setCount2(int count2) {
		this.count2 = count2;
	}
	public int getCount3() {
		return count3;
	}
	public void setCount3(int count3) {
		this.count3 = count3;
	}
	public int getCount4() {
		return count4;
	}
	public void setCount4(int count4) {
		this.count4 = count4;
	}
	public int getCount5() {
		return count5;
	}
	public void setCount5(int count5) {
		this.count5 = count5;
	}
	
	
}
