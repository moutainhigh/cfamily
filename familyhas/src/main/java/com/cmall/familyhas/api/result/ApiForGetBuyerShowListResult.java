package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.BuyerShow;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForGetBuyerShowListResult extends RootResult {

	@ZapcomApi(value="买家秀内容分页总页码")
	private int totalPage = 1;
	
	@ZapcomApi(value="是否显示评价或晒单送积分顶部悬浮提醒框",remark="有待评价或者待晒单的订单则显示提醒框:0否1是")
	private String isRemind = "0";
	
	@ZapcomApi(value="评价+晒单可以获得的总积分",remark="如果有待评价或者待晒单的订单,计算可获得积分总和")
	private String total_integral = "0";
	
	@ZapcomApi(value="可评价晒单的商品主图",remark="如果有待评价或者待晒单的订单,去最新订单商品图")
	private String canEvaluateMainpic = "";
	
	@ZapcomApi(value = "买家秀列表List")
	private List<BuyerShow> buyerShowList = new ArrayList<BuyerShow>();

	@ZapcomApi(value = "评价中心标签", remark = "1 待评价、2 待晒单 、 3 已评价 ")
	private String tagType = "1";
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getIsRemind() {
		return isRemind;
	}

	public void setIsRemind(String isRemind) {
		this.isRemind = isRemind;
	}

	public String getTotal_integral() {
		return total_integral;
	}

	public void setTotal_integral(String total_integral) {
		this.total_integral = total_integral;
	}

	public String getCanEvaluateMainpic() {
		return canEvaluateMainpic;
	}

	public void setCanEvaluateMainpic(String canEvaluateMainpic) {
		this.canEvaluateMainpic = canEvaluateMainpic;
	}

	public List<BuyerShow> getBuyerShowList() {
		return buyerShowList;
	}

	public void setBuyerShowList(List<BuyerShow> buyerShowList) {
		this.buyerShowList = buyerShowList;
	}
	
	
}
