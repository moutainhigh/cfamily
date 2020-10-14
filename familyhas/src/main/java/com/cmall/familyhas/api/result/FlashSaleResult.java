package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.FlashSaleEvent;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

/**
 * 秒杀列表页，返回规则：开始时间是今日的秒杀活动，以及两条明日的即将开始
 * @author Angel Joy
 *
 */
public class FlashSaleResult extends RootResult {
	@ZapcomApi(value="秒杀时间列表",remark="")
	private List<FlashSaleEvent> flashSaleEventList = new ArrayList<FlashSaleEvent>();

	public List<FlashSaleEvent> getFlashSaleEventList() {
		return flashSaleEventList;
	}

	public void setFlashSaleEventList(List<FlashSaleEvent> flashSaleEventList) {
		this.flashSaleEventList = flashSaleEventList;
	}

	
	
}
