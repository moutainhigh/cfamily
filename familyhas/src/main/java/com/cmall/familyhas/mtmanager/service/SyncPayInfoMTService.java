package com.cmall.familyhas.mtmanager.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.FamilyConfig;
import com.cmall.familyhas.mtmanager.inputresult.SyncPayInfoMTInput;
import com.cmall.familyhas.mtmanager.inputresult.SyncPayInfoMTResult;
import com.cmall.familyhas.mtmanager.model.MTAliPayInfo;
import com.cmall.familyhas.mtmanager.model.MTOrderPayInfo;
import com.cmall.familyhas.mtmanager.model.MTWeChatPayInfo;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 同步MT支付信息
 * @author pang_jhui
 *
 */
public class SyncPayInfoMTService extends BaseClass {
	
	/**
	 * 处理相关请求
	 * @param syncPayInfoMTInput
	 * 		支付信息同步相关参数
	 * @return SyncPayInfoMTResult
	 * 		支付信息同步处理结果
	 * @throws Exception 
	 */
	public SyncPayInfoMTResult doProcess(SyncPayInfoMTInput syncPayInfoMTInput) throws Exception{
		
		SyncPayInfoMTResult  syncPayInfoMTResult = new SyncPayInfoMTResult();
		
		if(syncPayInfoMTInput.getMtOrderPayInfo() != null){
			
			/*支付宝支付*/
			if(StringUtils.equals(syncPayInfoMTInput.getMtOrderPayInfo().getPay_type(), FamilyConfig.OC_PAYTYPE_ALIPAY)){				
			
				
				rsyncAlipayInfo(syncPayInfoMTInput.getMtAliPayInfo());
				
				rsyncOrderPayInfo(syncPayInfoMTInput.getMtOrderPayInfo(), syncPayInfoMTInput.getMtAliPayInfo());
				
			}
			
			/*微信支付*/
			if(StringUtils.equals(syncPayInfoMTInput.getMtOrderPayInfo().getPay_type(), FamilyConfig.OC_PAYTYPE_WECHAT)){
				
				rsyncWebChatPayInfo(syncPayInfoMTInput.getMtWeChatPayInfo());
				
				rsyncOrderPayInfo(syncPayInfoMTInput.getMtOrderPayInfo(), syncPayInfoMTInput.getMtWeChatPayInfo());
				
			}
			
			
			
			
			
			
		}else{
			
			syncPayInfoMTResult.setResultCode(-1);
			syncPayInfoMTResult.setResultMessage("缺少订单相关支付信息");
			
		}
		
		return syncPayInfoMTResult;		
		
	}
	
	/**
	 * 同步支付信息
	 * @param mtAliPayInfo
	 * 		支付信息
	 * @throws Exception 
	 */
	public void rsyncAlipayInfo(MTAliPayInfo mtAliPayInfo) throws Exception{
		
		MDataMap mDataMap = objectToMap(mtAliPayInfo);
		
		mDataMap.put("create_time", DateUtil.toString(new Date(), DateUtil.DATE_FORMAT_DATETIME));
		
		DbUp.upTable("oc_payment").dataInsert(mDataMap);
		
	}
	
	/**
	 * 同步微信支付信息
	 * @param mtWeChatPayInfo
	 * 		微信支付信息
	 * @throws Exception 
	 */
	public void rsyncWebChatPayInfo(MTWeChatPayInfo mtWeChatPayInfo) throws Exception{
		
		
		MDataMap mDataMap = objectToMap(mtWeChatPayInfo);
		
		DbUp.upTable("oc_payment_wechatNew").dataInsert(mDataMap);
		
		
		
	}
	
	/**
	 * 同步订单支付信息(支付宝)
	 * @param mtOrderPayInfo
	 * 		订单支付信息
	 * @return SyncPayInfoMTResult
	 * 		订单支付信息同步结果
	 * @throws Exception 
	 */
	public void rsyncOrderPayInfo(MTOrderPayInfo mtOrderPayInfo, MTAliPayInfo mtAliPayInfo) throws Exception{
		
		MDataMap orderPayInfoDataMap = objectToMap(mtOrderPayInfo);
		
		orderPayInfoDataMap.put("pay_sequenceid", mtAliPayInfo.getTrade_no());
		
		orderPayInfoDataMap.put("payed_money", mtAliPayInfo.getTotal_fee());
		
		DbUp.upTable("oc_order_pay").dataInsert(orderPayInfoDataMap);
		
	}
	
	/**
	 * 同步订单支付信息(微信)
	 * @param mtOrderPayInfo
	 * 		订单支付信息
	 * @return SyncPayInfoMTResult
	 * 		订单支付信息同步结果
	 * @throws Exception 
	 */
	public void rsyncOrderPayInfo(MTOrderPayInfo mtOrderPayInfo, MTWeChatPayInfo mtWeChatPayInfo) throws Exception{
		
		MDataMap orderPayInfoDataMap = objectToMap(mtOrderPayInfo);
		
		orderPayInfoDataMap.put("pay_sequenceid", mtWeChatPayInfo.getTransaction_id());
		
		orderPayInfoDataMap.put("payed_money", mtWeChatPayInfo.getTotal_fee());
		
		DbUp.upTable("oc_order_pay").dataInsert(orderPayInfoDataMap);
		
	}
	
	
	/**
	 * 将对象属性字段转换为Map集合
	 * @param object
	 * 		待转换对象
	 * @return MDataMap
	 * 		字段集合
	 * @throws Exception
	 */
	public MDataMap objectToMap(Object object) throws Exception{
		
		Field[] fields = object.getClass().getDeclaredFields();
		
		MDataMap mDataMap = new MDataMap();
		
		for (Field field : fields) {
			
			String name = field.getName();
			
			String prex = name.substring(0, 1);
			
			String methodName = "get"+prex.toUpperCase()+name.substring(1);
			
			Method method = object.getClass().getDeclaredMethod(methodName);
			
			mDataMap.put(name, method.invoke(object).toString());
			
		}
		
		return mDataMap;
		
	}

}
