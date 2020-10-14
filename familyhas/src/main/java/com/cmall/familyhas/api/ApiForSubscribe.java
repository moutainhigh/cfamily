package com.cmall.familyhas.api;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForSubscribeInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;
import com.srnpr.zapweb.webapi.RootResultWeb;

/** 
* @author Angel Joy
* @Time 2020-8-4 17:17:17 
* @Version 1.0
* <p>Description:</p>
* 特殊编码：999，如果已经订阅过，并且未推送时，需要返回该码，然后更新订阅时间
*/
public class ApiForSubscribe extends RootApiForVersion<RootResultWeb, ApiForSubscribeInput> {

	@Override
	public RootResultWeb Process(ApiForSubscribeInput inputParam, MDataMap mRequestMap) {
		RootResultWeb result = new RootResultWeb();
		String userCode = "";
		if(getFlagLogin()) {
			userCode = getOauthInfo().getUserCode();
		}
		String productCode = inputParam.getProductCode();
		String openId = inputParam.getOpenId();
		String source = inputParam.getSource();
		String subscribeType = inputParam.getSubscribeType();
		int time = inputParam.getTime();
		int type = inputParam.getType();
		MDataMap insert = new MDataMap();
		insert.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
		insert.put("product_code", productCode);
		insert.put("open_id", openId);
		insert.put("source", source);
		insert.put("subscribe_type", subscribeType);
		insert.put("user_code", userCode);
		insert.put("subscribe_time", DateUtil.getSysDateTimeString());
		insert.put("pre_push_time", DateUtil.addMinute(-time));
		if("449748680001".equals(subscribeType)) {//打卡提醒，每个人只有一个
			if(1 == type) {//订阅推送消息
				if(DbUp.upTable("nc_push_news_subscribe_pro").count("open_id",openId,"subscribe_type",subscribeType,"if_post","0")>0) {
					result.setResultCode(999);
					result.setResultMessage("");
					DbUp.upTable("nc_push_news_subscribe_pro").dataUpdate(insert,"subscribe_time","subscribe_type,open_id");
				}else {
					result.setResultCode(1);
					result.setResultMessage("订阅成功");
					DbUp.upTable("nc_push_news_subscribe_pro").dataInsert(insert);
				}
			}else {//取消订阅
				result.setResultCode(1);
				result.setResultMessage("取消订阅成功");
				DbUp.upTable("nc_push_news_subscribe_pro").delete("open_id",openId,"subscribe_type",subscribeType);
			}
		}else if("449748680002".equals(subscribeType)||"449748680003".equals(subscribeType)){//商品降价提醒
			if(StringUtils.isEmpty(productCode)) {
				result.setResultCode(0);
				result.setResultMessage("商品编号不能为空");
				return result;
			}
			if(1 == type) {//订阅推送消息
				if(DbUp.upTable("nc_push_news_subscribe_pro").count("open_id",openId,"subscribe_type",subscribeType,"product_code",productCode,"if_post","0")>0) {
					result.setResultCode(999);
					result.setResultMessage("");
					DbUp.upTable("nc_push_news_subscribe_pro").dataUpdate(insert,"subscribe_time","subscribe_type,product_code,open_id");
				}else {
					result.setResultCode(1);
					result.setResultMessage("订阅成功");
					DbUp.upTable("nc_push_news_subscribe_pro").dataInsert(insert);
				}
			}else {//取消订阅
				result.setResultCode(1);
				result.setResultMessage("取消订阅成功");
				DbUp.upTable("nc_push_news_subscribe_pro").delete("open_id",openId,"subscribe_type",subscribeType,"product_code",productCode);
			}
		}
		return result;
	}

}
