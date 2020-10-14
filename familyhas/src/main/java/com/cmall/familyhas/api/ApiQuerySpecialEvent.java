package com.cmall.familyhas.api;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiQuerySpecialEventInput;
import com.cmall.familyhas.api.result.ApiQuerySpecialEventResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webmodel.MOauthInfo;
import com.srnpr.zapweb.websupport.OauthSupport;

/**
 * 查询当前时间特殊活动编号、商品
 * @remark 
 * @author 任宏斌
 * @date 2020年3月17日
 */
public class ApiQuerySpecialEvent extends RootApi<ApiQuerySpecialEventResult, ApiQuerySpecialEventInput>{

	@Override
	public ApiQuerySpecialEventResult Process(ApiQuerySpecialEventInput inputParam, MDataMap mRequestMap) {
		ApiQuerySpecialEventResult result = new ApiQuerySpecialEventResult();
		
		String sql1 = "SELECT ei.event_code,ep.item_code,ep.product_code,ep.sku_code "
				+ "FROM systemcenter.sc_event_item_product ep "
				+ "LEFT JOIN systemcenter.sc_event_info ei ON ep.event_code=ei.event_code "
				+ "WHERE event_type_code='4497472600010029' AND event_status='4497472700020002' AND begin_time <= NOW() AND end_time > NOW()";
		Map<String, Object> event = DbUp.upTable("sc_hudong_event_info").dataSqlOne(sql1, new MDataMap());
		if(null != event) {
			result.setEvent_code(MapUtils.getString(event, "event_code"));
			result.setProduct_code(MapUtils.getString(event, "product_code"));
			result.setSku_code(MapUtils.getString(event, "sku_code"));
			LoadProductInfo loadProductInfo = new LoadProductInfo();
			PlusModelProductInfo plusModelProductInfo = loadProductInfo.upInfoByCode(new PlusModelProductQuery(MapUtils.getString(event, "product_code")));
			result.setProduct_pic(plusModelProductInfo.getMainpicUrl());
			
			// 如果存在token 判断用户是否参与过
			MOauthInfo mOauthInfo = null;
			if(null != mRequestMap.get("api_token") && null != (mOauthInfo = new OauthSupport().upOauthInfo(mRequestMap.get("api_token")))) {
				String sql2 = "SELECT count(1) count FROM ordercenter.oc_order_activity oa LEFT JOIN ordercenter.oc_orderinfo oi ON oa.order_code=oi.order_code " + 
						" WHERE oa.activity_code=:activity_code AND oi.buyer_code=:buyer_code";
				Map<String, Object> countMap = DbUp.upTable("oc_order_activity")
						.dataSqlOne(sql2, new MDataMap("activity_code", MapUtils.getString(event, "event_code"), "buyer_code", mOauthInfo.getUserCode()));
				if(MapUtils.getInteger(countMap, "count") == 0) {
					result.setBuy_flag("1");
				}
			}
		}
		
		return result;
	}
	
}
