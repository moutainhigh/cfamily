package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.Reason;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiForReturnGoodsInfoResult extends RootResult {

	@ZapcomApi(value = "是否过期", remark = "是否过期 列如（1：过期 0：未过期）", require = 1, demo = "0")
	private String outDateFlag = "0";

	@ZapcomApi(value = "过期提示信息", remark = "过期提示信息", require = 1, demo = "您的单子已超出售后期限，一边玩去吧")
	private String outDateMsg = "";

	@ZapcomApi(value = "数量", remark = "sku可退换最大数量", require = 1, demo = "3")
	private int maxReturnNum = 0;

	@ZapcomApi(value = "价格", remark = "sku单价", require = 1, demo = "3.00")
	private String skuPrice = "0";

	@ZapcomApi(value = "微公社价格", remark = "微公社单个sku价格", require = 1, demo = "3.00")
	private String wgsPrise = "0";

	@ZapcomApi(value = "运费", remark = "目前所有运费统一为0.00", require = 1, demo = "0.00")
	private String freightPrise = "0";

	@ZapcomApi(value = "退款类型", remark = "", require = 1, demo = "")
	private List<Reimburse> reimburseType = new ArrayList<Reimburse>();

	@ZapcomApi(value = "退款原因", remark = "", require = 1, demo = "")
	private List<Reason> reimburseReason = new ArrayList<Reason>();
	
	@ZapcomApi(value = "换货原因", remark = "", require = 1, demo = "")
	private List<Reason> changeGoodsReason = new ArrayList<Reason>();
	
	@ZapcomApi(value = "通知内容", require = 1)
	private String notice_content = "";
	
	@ZapcomApi(value = "微公社退换提示", remark = "", require = 1, demo = "")
	private String wgsReturnAlert = "";
	
	@ZapcomApi(value = "积分退换提示", remark = "", require = 1, demo = "")
	private String integralReturnAlert = "";
	
	@ZapcomApi(value = "是否跳售后详情页", remark = "0：返回上一页面，1：进入售后详情页面", require = 1, demo = "0")
	private String turnToAfterSale = "0";

	
	public String getTurnToAfterSale() {
		return turnToAfterSale;
	}


	public void setTurnToAfterSale(String turnToAfterSale) {
		this.turnToAfterSale = turnToAfterSale;
	}


	public String getIntegralReturnAlert() {
		return integralReturnAlert;
	}


	public void setIntegralReturnAlert(String integralReturnAlert) {
		this.integralReturnAlert = integralReturnAlert;
	}


	public static class Reimburse {
		@ZapcomApi(value = "售后类型编号", remark = "4497477800030001-退货退款  4497477800030002-仅退款  4497477800030003-换货", require = 1, demo = "4497477800030001")
		private String reimburseCode = "";
		@ZapcomApi(value = "售后类型", remark = "", require = 1, demo = "退货退款")
		private String reimburseContext = "";
		public String getReimburseCode() {
			return reimburseCode;
		}
		public void setReimburseCode(String reimburseCode) {
			this.reimburseCode = reimburseCode;
		}
		public String getReimburseContext() {
			return reimburseContext;
		}
		public void setReimburseContext(String reimburseContext) {
			this.reimburseContext = reimburseContext;
		}
		public Reimburse(String reimburseCode, String reimburseContext) {
			super();
			this.reimburseCode = reimburseCode;
			this.reimburseContext = reimburseContext;
		}
		public Reimburse() {
			super();
		}
		
	}

	public ApiForReturnGoodsInfoResult() {
		super();
	}
	

	public ApiForReturnGoodsInfoResult(String outDateFlag, String outDateMsg, int maxReturnNum, String skuPrice, String wgsPrise,
			String freightPrise, List<Reimburse> reimburseType, List<Reason> reimburseReason) {
		super();
		this.outDateFlag = outDateFlag;
		this.outDateMsg = outDateMsg;
		this.maxReturnNum = maxReturnNum;
		this.skuPrice = skuPrice;
		this.wgsPrise = wgsPrise;
		this.freightPrise = freightPrise;
		this.reimburseType = reimburseType;
		this.reimburseReason = reimburseReason;
	}


	public String getWgsReturnAlert() {
		return wgsReturnAlert;
	}


	public void setWgsReturnAlert(String wgsReturnAlert) {
		this.wgsReturnAlert = wgsReturnAlert;
	}


	public String getOutDateFlag() {
		return outDateFlag;
	}

	public void setOutDateFlag(String outDateFlag) {
		this.outDateFlag = outDateFlag;
	}

	public String getOutDateMsg() {
		return outDateMsg;
	}

	public void setOutDateMsg(String outDateMsg) {
		this.outDateMsg = outDateMsg;
	}

	public int getMaxReturnNum() {
		return maxReturnNum;
	}

	public void setMaxReturnNum(int maxReturnNum) {
		this.maxReturnNum = maxReturnNum;
	}

	public String getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}

	public String getWgsPrise() {
		return wgsPrise;
	}

	public void setWgsPrise(String wgsPrise) {
		this.wgsPrise = wgsPrise;
	}

	public String getFreightPrise() {
		return freightPrise;
	}

	public void setFreightPrise(String freightPrise) {
		this.freightPrise = freightPrise;
	}

	public List<Reimburse> getReimburseType() {
		return reimburseType;
	}

	public void setReimburseType(List<Reimburse> reimburseType) {
		this.reimburseType = reimburseType;
	}

	public List<Reason> getReimburseReason() {
		return reimburseReason;
	}

	public void setReimburseReason(List<Reason> reimburseReason) {
		this.reimburseReason = reimburseReason;
	}


	public List<Reason> getChangeGoodsReason() {
		return changeGoodsReason;
	}


	public void setChangeGoodsReason(List<Reason> changeGoodsReason) {
		this.changeGoodsReason = changeGoodsReason;
	}


	public String getNotice_content() {
		return notice_content;
	}


	public void setNotice_content(String notice_content) {
		this.notice_content = notice_content;
	}
	
}
