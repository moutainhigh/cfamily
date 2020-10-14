package com.cmall.familyhas.api;


import com.cmall.familyhas.api.input.ApiAlipayMoveProcessInput;
import com.cmall.familyhas.api.result.ApiAlipayMoveProcessResult;
import com.cmall.ordercenter.model.api.ApiAlipayMoveProcessOrderResult;
import com.cmall.ordercenter.service.ApiAlipayMoveProcessService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;
/**
 * 支付宝移动支付(后台)
 * @author wz
 *
 */
@Deprecated
public class ApiAlipayMoveProcess extends RootApiForManage<ApiAlipayMoveProcessResult, ApiAlipayMoveProcessInput>{

	public ApiAlipayMoveProcessResult Process(
			ApiAlipayMoveProcessInput inputParam, MDataMap mRequestMap) {
		
		ApiAlipayMoveProcessResult resultHalf = new ApiAlipayMoveProcessResult();
		ApiAlipayMoveProcessOrderResult resultOrder = new ApiAlipayMoveProcessOrderResult();
		ApiAlipayMoveProcessService apiAlipayMoveProcessService = new ApiAlipayMoveProcessService();
		
		String out_trade_no = inputParam.getOut_trade_no();  //订单编号
		String body = inputParam.getBody();   //商品详情
		String subject = inputParam.getSubject();   //商品名称
		
		resultOrder = apiAlipayMoveProcessService.alipayMoveProcessRequest(out_trade_no, body,subject);
		
		resultHalf.set_input_charset(resultOrder.get_input_charset());
		resultHalf.setBody(resultOrder.getBody());
		resultHalf.setNotify_url(resultOrder.getNotify_url());
		resultHalf.setOut_trade_no(resultOrder.getOut_trade_no());
		resultHalf.setPartner(resultOrder.getPartner());
		resultHalf.setPayment_type(resultOrder.getPayment_type());
		resultHalf.setResultCode(resultOrder.getResultCode());
		resultHalf.setResultMessage(resultOrder.getResultMessage());
		resultHalf.setSeller_id(resultOrder.getSeller_id());
		resultHalf.setService(resultOrder.getService());
		resultHalf.setSign(resultOrder.getSign());
		resultHalf.setSign_type(resultOrder.getSign_type());
		resultHalf.setSubject(resultOrder.getSubject());
		resultHalf.setTotal_fee(resultOrder.getTotal_fee());
		return resultHalf;
	}
}
