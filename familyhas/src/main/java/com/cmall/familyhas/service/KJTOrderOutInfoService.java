package com.cmall.familyhas.service;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiKJTOrderOutInfoInput;
import com.cmall.familyhas.api.result.ApiKJTOrderOutInfoResult;
import com.cmall.groupcenter.accountmarketing.util.DateFormatUtil;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.MailSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webpage.RootProcess;

/**
 * 提供给跨境通（KJT）
 * KJT通知分销渠道订单已出关区,判断出关是否成功，若成功，则将KJT订单编号
 * 物流公司编号、订单物流编号、出关失败的原因同步到惠家有系统
 * @author pangjh
 *
 */
public class KJTOrderOutInfoService extends BaseClass {
	
	public ApiKJTOrderOutInfoResult doProcess(ApiKJTOrderOutInfoInput input,MDataMap mRequestMap){
		
		/*创建返回结果对象*/
		ApiKJTOrderOutInfoResult result = new ApiKJTOrderOutInfoResult();
		
		try {
			
			/*出关成功*/
			if(StringUtils.equals(input.getStatus(), "1")){
				
				MDataMap mDataMap = convertParams(input);
				
				if(exist(mDataMap)){
					
					updateOrderShipment(mDataMap);
					
				}else{
					
					saveOrderShipment(mDataMap);
					
				}
				
				
			}
			
			/*出关失败,则发送邮件给运维人员*/
			if(StringUtils.equals(input.getStatus(), "-1")){
				
				sendEmail(input);
				
			}	
			
			MDataMap requestParamMap = convertLogInfo(input); 
			
			JsonHelper<MDataMap> requestHelper = new JsonHelper<MDataMap>();
			
			requestParamMap.put("request_data", requestHelper.ObjToString(mRequestMap));
			
			/*将请求参数记录到日志表中*/
			record_log(requestParamMap);
			
		} catch (Exception e) {
			
			bLogError(0, e.getMessage());
			
			result.setResultCode(-1);
			
			result.setResultMessage(e.getMessage());
			
		}
		
		
		return result;
		
	}
	
	/**
	 * 保存物流信息
	 * @param mDataMap
	 * 		物流信息数据集合
	 */
	public void saveOrderShipment(MDataMap mDataMap){
		
		DbUp.upTable("oc_order_shipments").dataInsert(mDataMap);
		
	}
	
	/**
	 * 更新物流信息
	 * @param mDataMap
	 * 		待修改的数据集合
	 */
	public void updateOrderShipment(MDataMap mDataMap){
		
		DbUp.upTable("oc_order_shipments").dataUpdate(mDataMap, "", "order_code,logisticse_code,waybill");
		
	}
	
	/**
	 * 判断物流信息是否已经存在
	 * @param mDataMap
	 * 		物流信息
	 * @return true|存在 false|不存在
	 */
	public boolean exist(MDataMap mDataMap){
		
		MDataMap qo = query(mDataMap);
		
		boolean flag = false;
		
		if(qo != null){
			
			flag = true;
			
		}
		
		return flag;
		
	}
	
	/**
	 * 查询物流信息
	 * @param mDataMap
	 * 		查询条件
	 * @return
	 */
	public MDataMap query(MDataMap mDataMap){
		
		return DbUp.upTable("oc_order_shipments").one("order_code",mDataMap.get("order_code"),"logisticse_code",
				mDataMap.get("logisticse_code"),"waybill",mDataMap.get("waybill"));
		
	}
	
	/**
	 * 若出关成功，则将跨境通相关参数转换为惠家有物流信息
	 * @param input
	 * 		跨境通传入参数
	 * @return MDataMap
	 * 		出关后的订单信息
	 */
	public MDataMap convertParams(ApiKJTOrderOutInfoInput input) throws Exception{
		
		MDataMap dataMap = new MDataMap();
		
		String orderCode = convertOrderInfo(input.getMerchantOrderID()).get("order_code");
		
		dataMap.put("order_code", orderCode);
		
		String shipTypeId = "";
		
		if(convertShipInfo().containsKey(input.getShipTypeID())){
			
			shipTypeId = convertShipInfo().get(input.getShipTypeID());
			
		}else{
			
			shipTypeId = input.getShipTypeID();
			
		}
		
		dataMap.put("logisticse_code", shipTypeId);
		
		dataMap.put("waybill", input.getTrackingNumber());
		
		dataMap.put("creator","system");
		
		Date commitTime = DateFormatUtil.convertToDate(input.getCommitTime(), "yyyyMMddHHmmss");
		
		dataMap.put("create_time", DateUtil.toString(commitTime, DateUtil.DATE_FORMAT_DATETIME));
		
		dataMap.put("order_code_seq", input.getMerchantOrderID());
		
		return dataMap;
		
	}
	
	/**
	 *1	顺丰快递-跨境通自贸仓
	 *2	圆通快递-跨境通自贸仓
	 *42	天天果园配送
	 *81	泓丰直邮
	 *84	如风达-跨境通自贸仓
	 *93	申通快递-跨境通自贸仓
	 * @return
	 */
	public MDataMap convertShipInfo(){
		
		MDataMap mdataMap = new MDataMap();
		
		mdataMap.put("1", "shunfeng");
		mdataMap.put("2", "yuantong");
		mdataMap.put("84", "rufengda");
		mdataMap.put("93", "shentong");
		
		return mdataMap;
		
		
	}
	
	/**
	 * 在惠家有与跨境通订单对照表中
	 * 根据外部订单号查询对应的惠家有订单
	 * @param outOrderCode
	 * 		跨境通订单
	 * @return MDataMap
	 * 		订单对照信息
	 */
	public MDataMap convertOrderInfo(String outOrderCode){
		
		return DbUp.upTable("oc_order_kjt_list").one("order_code_seq",outOrderCode);
		
	}
	
	/**
	 * 若出关失败，则给运维人员发送邮件提示
	 * @param input
	 * 		跨境通提供的输入参数
	 * @return boolean
	 * 		邮件发送是否成功
	 */
	public void sendEmail(ApiKJTOrderOutInfoInput input){
		
		/*邮件接收人*/
		String mail_receive = bConfig("familyhas.mail_receive_kjt");
		/*邮件标题*/
		String mail_title = bConfig("familyhas.mail_title_kjt");
		/*邮件格式内容*/
		String mail_content = bConfig("familyhas.mail_content_kjt");
		/*惠家有订单编号*/
		String order_code = convertOrderInfo(input.getMerchantOrderID()).get("order_code");
		/*邮件内容*/
		String sContent = FormatHelper.formatString(mail_content, order_code,input.getMerchantOrderID(),input.getMessage());
		
		for (String receiveUser: mail_receive.split(",")){
			
			MailSupport.INSTANCE.sendMail(receiveUser, mail_title, sContent); 
			
		}
		
	}
	
	/**
	 * 解析跨境http請求
	 * @param request
	 * 		跨境通请求信息
	 * @return
	 */
	public String processRequest(HttpServletRequest request){
		
		MDataMap mDataMap = new RootProcess().convertRequest(request);
		
		String returnStr = "";
		
		try {
			

			String method = mDataMap.get("method");
			
			String[] methods = method.split("\\.");
			
			String methodName = StringUtils.lowerCase(methods[0]).concat(methods[1]);
			
			returnStr = processApi(KJTOrderMethodService.class, methodName, mDataMap);	
			
		} catch (Exception e) {
			bLogError(0, e.getMessage());
		}
		
		return returnStr;
		
	}
	
	
	
	
	/**
	 * 反射方法获取数据信息
	 * @param object
	 * 		反射对象
	 * @param methodName
	 * 		处理方法名称
	 * @param sInputJson
	 * 		json数据
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })	
	public String processApi(Class object,String methodName,MDataMap mDataMap) throws Exception{
		
		String str = "";
		
		Method method=  object.getMethod(methodName, MDataMap.class);	
		
		str = (String) method.invoke(object.newInstance(), mDataMap);
		
		return str;
		
	}
	
	/**
	 * 将调用参数记录log表中
	 * @param mDataMap
	 * 		参数信息
	 */
	public void record_log(MDataMap mDataMap){
		
		DbUp.upTable("lc_kjt_order_api").dataInsert(mDataMap);
		
	}
	
	public MDataMap convertLogInfo(ApiKJTOrderOutInfoInput infoInput){
		
		MDataMap mDataMap = new MDataMap();
		
		mDataMap.put("merchant_orderid", infoInput.getMerchantOrderID());
		
		mDataMap.put("status", infoInput.getStatus());
		
		mDataMap.put("ship_typeid", infoInput.getShipTypeID());
		
		mDataMap.put("tracking_number", infoInput.getTrackingNumber());
		
		mDataMap.put("commit_time", infoInput.getCommitTime());
		
		if(StringUtils.isEmpty(infoInput.getMessage())){
			
			mDataMap.put("message","" );
			
		}else{
			
			mDataMap.put("message",infoInput.getMessage());
			
		}	
		
		
		mDataMap.put("create_time", DateUtil.getSysDateTimeString());
		
		return mDataMap;
		
	}
	

}
