package com.cmall.familyhas.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiCheckUserIntegralInput;
import com.cmall.familyhas.api.result.ApiCheckUserIntegralResult;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.WebClientRequest;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapcom.topdo.TopDir;
import com.srnpr.zapweb.webapi.RootApiForToken;

public class ApiCheckUserIntegral extends RootApiForToken<ApiCheckUserIntegralResult, ApiCheckUserIntegralInput> {
	
	private final String JY_URL = bConfig("groupcenter.rsync_homehas_url");
	
	@Override
	public ApiCheckUserIntegralResult Process(ApiCheckUserIntegralInput inputParam, MDataMap mRequestMap) {
		String integral = inputParam.getIntegral();//购买商品所需积分
		//获取积分
		String custPoint = "0";
		try {
			PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(getUserCode()));	
			JSONObject infoMap = new JSONObject();
			infoMap.put("cust_id", levelInfo.getCustId());
			String jyResult = getHttps(JY_URL + "getCustAccmByCustId", infoMap.toString());
			JSONObject jyObject = JSONObject.parseObject(jyResult);
			boolean success = jyObject.getBoolean("success");
			if(success) {
				custPoint = jyObject.getString("ACCM_AMT");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		ApiCheckUserIntegralResult apiResult = new ApiCheckUserIntegralResult();
		if(!"".equals(custPoint)) {
			BigDecimal integralD = new BigDecimal(integral);
			BigDecimal custPointD = new BigDecimal(custPoint);
			if(custPointD.compareTo(integralD) >= 0) {
				apiResult.setResultCode(1);
			}else {
				apiResult.setResultCode(0);
				apiResult.setResultMessage("您账户中积分余额不足，赚取积分再来兑换!");
			}
		}else {
			apiResult.setResultCode(0);
			apiResult.setResultMessage("用户积分获取失败!");
		}
		return apiResult;
	}
	
	private String getHttps(String sUrl, String sRequestString)
			throws ParseException, IOException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException, CertificateException {
		WebClientRequest webClientRequest = new WebClientRequest();

		String sDir = bConfig("groupcenter.homehas_key");

		if (StringUtils.isEmpty(sDir)) {
			TopDir topDir = new TopDir();
			sDir = topDir.upCustomPath("") + "tomcat.keystore";
		}
//		sDir = "C:/etc/zapsrnpr/c__users_bloodline/tomcat.keystore";
		
		webClientRequest.setFilePath(sDir);
		webClientRequest.setUrl(sUrl);

		HttpEntity httpEntity = new StringEntity(sRequestString,
				TopConst.CONST_BASE_ENCODING);

		webClientRequest.setConentType("application/json");

		webClientRequest
				.setPassword(bConfig("groupcenter.rsync_homehas_password"));


		webClientRequest.setHttpEntity(httpEntity);

		String sResponseString = WebClientSupport.upHttpsPost(webClientRequest);

		return sResponseString;
	}
}
