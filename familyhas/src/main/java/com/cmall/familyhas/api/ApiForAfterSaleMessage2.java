package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.ApiForAfterSaleMessage2Input;
import com.cmall.familyhas.api.result.ApiForAfterSaleMessage2Result;
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
public class ApiForAfterSaleMessage2 extends RootApi<ApiForAfterSaleMessage2Result, ApiForAfterSaleMessage2Input> {

	@Override
	public ApiForAfterSaleMessage2Result Process(ApiForAfterSaleMessage2Input inputParam, MDataMap mRequestMap) {

		ApiForAfterSaleMessage2Result  result = new ApiForAfterSaleMessage2Result();

		String flag = inputParam.getFlag();
		String after_sale_person=inputParam.getAfter_sale_person();
		String after_sale_mobile=inputParam.getAfter_sale_mobile();
		String after_sale_address=inputParam.getAfter_sale_address();
		String after_sale_postcode=inputParam.getAfter_sale_postcode();
		String order_code=inputParam.getOrder_code();
		String type=inputParam.getType();
		String message="";
		
		if("echange".equals(type)){
			message=FormatHelper.formatString(bConfig("familyhas.fill_shipments_message2_ec"), after_sale_address,after_sale_person,after_sale_mobile,after_sale_postcode);
		}else{
			message=FormatHelper.formatString(bConfig("familyhas.fill_shipments_message2"), after_sale_address,after_sale_person,after_sale_mobile,after_sale_postcode);
//			message=bConfig("familyhas.fill_shipments_message2");
		}
		
		if("check".equals(flag)){//查询短信内容
			
			result.setResultMessage(message);
			
		}else if("buyer".equals(flag)){//下单人
			
			MDataMap orderInfo=DbUp.upTable("oc_orderinfo").one("order_code",order_code);
			
			String buyer_mobile = (String) DbUp.upTable("mc_login_info").dataGet("login_name", "member_code=:member_code",new MDataMap("member_code", orderInfo.get("buyer_code")));
			
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
