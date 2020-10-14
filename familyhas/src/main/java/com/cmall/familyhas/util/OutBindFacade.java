package com.cmall.familyhas.util;


import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.baseface.IBaseApi;
import com.srnpr.zapcom.baseface.IBaseInput;
import com.srnpr.zapcom.baseface.IBaseResult;
import com.srnpr.zapcom.basehelper.JsonHelper;
import com.srnpr.zapcom.basemodel.MApiModel;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.WebClientRequest;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topapi.RootResult;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapcom.topdo.TopDir;
import com.srnpr.zapweb.webfactory.ApiFactory;

/**
 * 调用ld系统
 * @author Administrator
 *
 */

public class OutBindFacade extends BaseClass {
	
	private  static OutBindFacade INSTANCE = null;
	
	/**
	 * 获取实例
	 * @return
	 */
	public static OutBindFacade getInstance(){
		
		if(INSTANCE == null){
			
			INSTANCE = new OutBindFacade();
		
		}
		
		return INSTANCE;
		
	}
	
	
	/**
	 * 获取请求的url
	 * 
	 * @return
	 */
	public String upRequestUrl() {
		return bConfig("groupcenter.rsync_homehas_url");
	}
	
	/**
	 * 调用处理逻辑 返回操作
	 * 
	 * @param sRequestString
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyStoreException
	 * @throws CertificateException
	 */
	public String getHttps(String sUrl, String sRequestString) throws Exception {
		WebClientRequest webClientRequest = new WebClientRequest();

		String sDir = bConfig("groupcenter.homehas_key");

		if (StringUtils.isEmpty(sDir)) {
			TopDir topDir = new TopDir();
			sDir = topDir.upCustomPath("") + "tomcat.keystore";
		}

		webClientRequest.setFilePath(sDir);
		
		webClientRequest.setUrl(sUrl);

		HttpEntity httpEntity;
		
		String sResponseString = "";
		
		httpEntity = new StringEntity(sRequestString,
					TopConst.CONST_BASE_ENCODING);
		webClientRequest.setConentType("application/json");

		webClientRequest
					.setPassword(bConfig("groupcenter.rsync_homehas_password"));

		bLogInfo(0, EntityUtils.toString(httpEntity));

		webClientRequest.setHttpEntity(httpEntity);

		sResponseString = WebClientSupport.upHttpsPost(webClientRequest);
			

		return sResponseString;
	}
	
	/**
	 * 反射相关实体
	 * @param clazz
	 * 		实体class
	 * @param mDataMap
	 * 		实体数据信息
	 * @return Object
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object invoke(Class clazz,MDataMap mDataMap) throws Exception{
		
		Field[] fields = clazz.getDeclaredFields();
		
		Object object = clazz.newInstance();

		for (Field field : fields) {
			
			String name = field.getName();
			
			String prex = name.substring(0, 1);
			
			String methodName = "set"+prex.toUpperCase()+name.substring(1);
			
			Method method = clazz.getDeclaredMethod(methodName, field.getType());
			
			if(mDataMap.containsKey(name)){
				
				method.invoke(object, mDataMap.get(name));
				
			}
			
		}
		
		return object;	
		
	}
	
	
	/**
	 * 处理逻辑
	 * 
	 * @param sClassName
	 * @param sInputJson
	 * @param mDataMap
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public String doProcess(String sClassName, String sInputJson,
			MDataMap mDataMap) throws IOException {

		String sReturnString = "";

		IBaseApi<IBaseResult, IBaseInput> iBaseApi = null;

		IBaseInput iBaseInput = null;

		try {

			MApiModel mApiModel = ApiFactory.INSTANCE.upApiModel(sClassName);

			iBaseApi = (IBaseApi<IBaseResult, IBaseInput>) mApiModel
					.getApiClass().newInstance();
			// if (StringUtils.isNotBlank(sInputJson))
			iBaseInput = (IBaseInput) mApiModel.getInputClass().newInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}

		JsonHelper<IBaseInput> jsonInput = new JsonHelper<IBaseInput>();
		if (StringUtils.isNotBlank(sInputJson))
			iBaseInput = jsonInput.GsonFromJson(sInputJson, iBaseInput);

		

		RootResult rootResult = (RootResult) iBaseApi.Process(iBaseInput, mDataMap);
		

		JsonHelper<IBaseResult> jsonResult = new JsonHelper<IBaseResult>();

		sReturnString = jsonResult.ObjToString(rootResult);

		return sReturnString;

	}
	

}
