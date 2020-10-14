package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForAfterSaleMessageInput;
import com.cmall.familyhas.api.result.ApiForAfterSaleMessageResult;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webmodel.MMessage;
import com.srnpr.zapweb.websupport.MessageSupport;

/**
 * 
 * 发送售后短信
 * @author jlin
 *
 */
public class ApiForAfterSaleMessage extends RootApi<ApiForAfterSaleMessageResult, ApiForAfterSaleMessageInput> {

	@Override
	public ApiForAfterSaleMessageResult Process(ApiForAfterSaleMessageInput inputParam, MDataMap mRequestMap) {

		ApiForAfterSaleMessageResult  result = new ApiForAfterSaleMessageResult();

		String flag = inputParam.getFlag();
		String asale_code=inputParam.getAsale_code();
		String type=inputParam.getType();
		String message="";
		String buyer_mobile="";
		String order_code="";
		
		if("echange".equals(type)){
			
			MDataMap orgMap=DbUp.upTable("oc_exchange_goods").one("exchange_no",asale_code);
			message=FormatHelper.formatString(bConfig("familyhas.fill_shipments_message_ec"), orgMap.get("after_sale_address"),orgMap.get("after_sale_person"),orgMap.get("after_sale_mobile"),orgMap.get("after_sale_postcode"));
			
			buyer_mobile=(String) DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code",new MDataMap("member_code", orgMap.get("buyer_code")));
			order_code=orgMap.get("order_code");
			
		}else{
			
			MDataMap orgMap=DbUp.upTable("oc_return_goods").one("return_code",asale_code);
			message=FormatHelper.formatString(bConfig("familyhas.fill_shipments_message"), orgMap.get("address"),orgMap.get("contacts"),orgMap.get("mobile"),orgMap.get("receiver_area_code"));
	
			buyer_mobile=(String) DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code",new MDataMap("member_code", orgMap.get("buyer_code")));
			order_code=orgMap.get("order_code");
		}
		
		
		if("check".equals(flag)){//查询短信内容
			
			result.setResultMessage(message);
			
		}else if("buyer".equals(flag)){//下单人
			
			MMessage messages = new MMessage();
			messages.setMessageContent(message);
			messages.setMessageReceive(buyer_mobile);
			messages.setSendSource("4497467200020006");
			MessageSupport.INSTANCE.sendMessage(messages);
			
		}else if("owner".equals(flag)){//收货人
			
			MMessage messages = new MMessage();
			messages.setMessageContent(message);
			messages.setMessageReceive((String)DbUp.upTable("oc_orderadress").dataGet("mobilephone", "order_code=:order_code", new MDataMap("order_code",order_code)));
			messages.setSendSource("4497467200020006");
			MessageSupport.INSTANCE.sendMessage(messages);
			
		}
		

		return result;
	}
}
