package com.cmall.familyhas.api;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForSmallProceduresSubscribeInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.xmassystem.load.LoadEventInfo;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventInfo;
import com.srnpr.xmassystem.modelevent.PlusModelEventQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * @desc 预约推送通知接口
 * @for 小程序、
 * @author Bloodline
 *
 */
public class ApiForSmallProceduresSubscribe extends RootApi<RootResult, ApiForSmallProceduresSubscribeInput> {
	
	@Override
	public RootResult Process(ApiForSmallProceduresSubscribeInput inputParam, MDataMap mRequestMap) {
		RootResult result = new RootResult();
		String type = inputParam.getType();
		if("1".equals(type)) {
			result = this.add(inputParam);
		}else {
			result = this.cancel(inputParam);
		}
		return result;
	}
	
	/**
	 * 预约
	 * @param inputParam
	 */
	private RootResult add(ApiForSmallProceduresSubscribeInput inputParam) {
		RootResult result = new RootResult();
		String formId = inputParam.getFormId();
		String eventCode = inputParam.getEventCode();
		String productCode = inputParam.getProductCode();
		String time = inputParam.getTime();
		String openId = inputParam.getOpenId();
		String source = inputParam.getSource();
		MDataMap  subscribeMap = new MDataMap();
		subscribeMap.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
		subscribeMap.put("form_id", formId);
		subscribeMap.put("event_code", eventCode);
		PlusModelEventQuery query = new PlusModelEventQuery();
		LoadEventInfo load = new LoadEventInfo();
		query.setCode(eventCode);
		PlusModelEventInfo eventinfo = load.upInfoByCode(query);
		if(eventinfo == null) {
			result.setResultCode(-1);
			result.setResultMessage("订阅通知的活动不存在");
			return result;
		}
		subscribeMap.put("event_name", eventinfo.getEventName());
		Date pushTime = null;
		if(StringUtils.isEmpty(time)) {
			pushTime = DateUtil.addMinute(DateUtil.toDate(eventinfo.getBeginTime(),DateUtil.DATE_FORMAT_DATETIME), -5);
		}else {
			pushTime = DateUtil.addMinute(DateUtil.toDate(eventinfo.getBeginTime(),DateUtil.DATE_FORMAT_DATETIME), -(Integer.parseInt(time)));
		}
		subscribeMap.put("push_time", DateUtil.toString(pushTime,DateUtil.DATE_FORMAT_DATETIME));
		subscribeMap.put("event_start_time", eventinfo.getBeginTime());
		subscribeMap.put("open_id", openId);
		subscribeMap.put("product_code", productCode);
		LoadProductInfo loadProduct = new LoadProductInfo();
		PlusModelProductQuery queryProduct = new PlusModelProductQuery(productCode);
		PlusModelProductInfo productInfo = loadProduct.upInfoByCode(queryProduct);
		if(productInfo == null) {
			result.setResultCode(-1);
			result.setResultMessage("订阅通知的商品不存在");
			return result;
		}
		subscribeMap.put("product_name", productInfo.getProductName());
		subscribeMap.put("source", source);
		Integer count = DbUp.upTable("nc_push_news_subscribe").count("open_id",openId,"source",source,"event_code",eventCode,"product_code",productCode);
		if(count == null || count == 0) {
			DbUp.upTable("nc_push_news_subscribe").dataInsert(subscribeMap);
		}else {
			result.setResultCode(-1);
			result.setResultMessage("您已经订阅，请勿重复订阅");
			return result;
		}
		return result;
	}
	
	/**
	 * 取消预约
	 * @param inputParam
	 * @date 2019-09-11 09:25:00
	 */
	private RootResult cancel(ApiForSmallProceduresSubscribeInput inputParam) {
		RootResult result = new RootResult();
		String openId = inputParam.getOpenId();
		String eventCode = inputParam.getEventCode();
		String productCode = inputParam.getProductCode();
		String source = inputParam.getSource();
		String sql = "open_id = '"+openId+"' AND event_code= '"+eventCode+"' AND product_code = '"+productCode+"' AND source = '"+source+"'";
		DbUp.upTable("nc_push_news_subscribe").dataDelete(sql, new MDataMap(),"");
		return result;
	}

}
