package com.cmall.familyhas.service;

import com.cmall.familyhas.util.OutBindFacade;
import com.srnpr.zapcom.baseclass.BaseClass;

/**
 * 接口调用业务实现类
 * @author pangjh
 *
 */
public class OutBindService extends BaseClass {
	
	/**
	 * 调用外部接口
	 * @param sUrl
	 * 		请求路径
	 * @param sRequestString
	 * 		请求参数Json格式
	 * @return
	 *      相应结果
	 */
	public String callInterface(String sUrl,String sRequestString){
		
		String results = "";
		
		try {
			results = OutBindFacade.getInstance().getHttps(sUrl, sRequestString);
		} catch (Exception e) {
			bLogError(0, e.getMessage());
			results = e.getMessage();
		}
		
		return results;
		
	}

}
