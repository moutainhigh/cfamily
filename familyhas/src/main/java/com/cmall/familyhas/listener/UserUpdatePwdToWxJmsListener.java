package com.cmall.familyhas.listener;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.WebClientRequest;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapcom.topdo.TopDir;
import com.srnpr.zapzero.root.RootJmsListenser;

public class UserUpdatePwdToWxJmsListener extends RootJmsListenser {

	/** 
	* @Description:把注册惠家友或微公社的修改密码信息推送给微信商城
	* @param @param sMessage 手机号
	* @param @param mDataMap 新密码
	* @author 张海生
	* @date 2015-1-4 上午11:31:10
	* @throws 
	*/
	public boolean onReceiveText(String sMessage, MDataMap mDataMap) {
		boolean ret = true;
		if (sMessage != null && mDataMap != null) {
			if(!StringUtils.isEmpty(sMessage)){
				String url = upRequestUrl();
				JSONObject json = new JSONObject();
			//	System.out.println("json:"+json.toString());		
				try {
					getHttps(url,  json.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}
	
	/**
	 * 获取请求的url
	 * 
	 * @return
	 */
	private String upRequestUrl() {
		return bConfig("groupcenter.wx_url")
				+ "delGoods";
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
	private String getHttps(String sUrl, String sRequestString)
			throws ParseException, IOException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, CertificateException {
		WebClientRequest webClientRequest = new WebClientRequest();

		String sDir = bConfig("groupcenter.homehas_key");

		if (StringUtils.isEmpty(sDir)) {
			TopDir topDir = new TopDir();
			sDir = topDir.upCustomPath("") + "tomcat.keystore";
		}
		webClientRequest.setFilePath(sDir);
		webClientRequest.setUrl(sUrl);

		HttpEntity httpEntity = new StringEntity(sRequestString,
				TopConst.CONST_BASE_ENCODING);

		webClientRequest.setConentType("application/json");

		webClientRequest
				.setPassword(bConfig("groupcenter.rsync_homehas_password"));

		bLogInfo(0, EntityUtils.toString(httpEntity));

		webClientRequest.setHttpEntity(httpEntity);

		String sResponseString = WebClientSupport.upHttpsPost(webClientRequest);

		return sResponseString;
	}
}
