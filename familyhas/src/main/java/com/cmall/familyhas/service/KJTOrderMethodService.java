package com.cmall.familyhas.service;

import java.util.List;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.result.ApiKJTOrderOutInfoResult;
import com.cmall.familyhas.util.OutBindFacade;
import com.cmall.systemcenter.bill.HexUtil;
import com.cmall.systemcenter.bill.MD5Util;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 跨境通提供接口信息
 * @author pangjh
 *
 */
public class KJTOrderMethodService extends BaseClass {

	/**
	 * KJT通知分销渠道订单已出关区
	 * @param sInputJson
	 * 		传入参数
	 * @return
	 */
	public String orderSOOutputCustoms(MDataMap mDataMap){
		
		String returnStr = "";
		
		try {				
			
			String sInputJson = mDataMap.get("data");
			
			RootResultWeb resultObject = checkSign(mDataMap);
			
			JsonHelper<RootResultWeb> roots = new JsonHelper<RootResultWeb>();
			
			if(resultObject.upFlagTrue()){
				
				String jsonResult = OutBindFacade.getInstance().doProcess("com.cmall.familyhas.api.ApiKJTOrderOutInfo", sInputJson, mDataMap);
				
				resultObject = new ApiKJTOrderOutInfoResult();
				
				roots.StringToObj(jsonResult, resultObject);
				
				if(resultObject.upFlagTrue()){
					
					returnStr = "SUCCESS";
					
				}else{
					
					returnStr = "FAILURE";
					
				}			
				
			}else{
				
				returnStr = "FAILURE";
				
				bLogInfo(0, "code:"+resultObject.getResultCode()+","+resultObject.getResultMessage());
				
			}
			
		} catch (Exception e) {
			
				returnStr = "FAILURE";
				
		}
		
		return returnStr;
		
	}
	
	/**
	 * 校验签名是否正确
	 * @param mDataMap
	 * 		请求参数
	 * @return
	 * 		检验结果code=-2签名校验错误
	 * @throws UnsupportedEncodingException 
	 */
	public RootResultWeb checkSign(MDataMap mDataMap) throws UnsupportedEncodingException{
		
		RootResultWeb result = new RootResultWeb();
		
		String requestSign = mDataMap.get("sign");
		
		mDataMap.remove("sign");
		
		String str = requestParams(mDataMap);
		
		String password = bConfig("groupcenter.rsync_kjt_password");
		
		byte[] requestParams = MD5Util.md5(str+"&"+password);
		
		String targetSign = HexUtil.toHexString(requestParams);
		
		if(!StringUtils.equals(requestSign, targetSign)){
			
			result.setResultCode(-2);
			
			result.setResultMessage("签名校验错误");
			
		}
		
		return result;
		
	}
	

	
	/**
	 * 组装成请求url
	 * @param requestMaps
	 * 		请求参数
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String requestParams(MDataMap requestMaps) throws UnsupportedEncodingException{
		
		StringBuffer requestStr = new StringBuffer();
		
		for(String item : sortParams(requestMaps)){
			
			requestStr.append(item + "&");
			
		}		
		
		return requestStr.substring(0, requestStr.length()-1);		
		
	}
	
	/**
	 * 针对参数进行排序
	 * @param requestMaps
	 * 		请求参数
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public List<String> sortParams(MDataMap requestMaps) throws UnsupportedEncodingException{
		
		Enumeration<String> keys = requestMaps.keys();
		
		List<String> list = new ArrayList<String>();
		
		while (keys.hasMoreElements()) {
			
			String key = (String) keys.nextElement();	
			
			String value = requestMaps.get(key);
			
			if(StringUtils.equals("data", key)){
				
				value = URLEncoder.encode(value,"UTF-8");
				
			}
			
			list.add(key+"="+value);
			
		}
		
		Collections.sort(list);
		
		return list;
		
	}

}
