package com.cmall.familyhas.api;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.input.ApiVipExclusiveViperInput;
import com.cmall.familyhas.api.result.ApiVipExclusiveViperResult;
import com.cmall.ordercenter.util.DateFormatUtil;
import com.cmall.systemcenter.util.Http_Request_Post;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.WebClientRequest;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapcom.topdo.TopDir;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 获取会员用户信息
 * @author ljj
 *
 */
public class ApiVipExclusiveViper extends RootApiForToken<ApiVipExclusiveViperResult, ApiVipExclusiveViperInput> {
	
	private final String JY_URL = bConfig("groupcenter.rsync_homehas_url");
	private final String PHOTO_URL = bConfig("familyhas.c_photo_url");
	private final String MD5_KEY = bConfig("familyhas.c_md5_key");
	
	@Override
	public ApiVipExclusiveViperResult Process(ApiVipExclusiveViperInput inputParam, MDataMap mRequestMap) {
		//获取用户头像
		String nickName = "";
		String photoUrl = "";
		String api_target = "2008";
		String api_input = "{}";
		String api_timespan = DateFormatUtil.getNowTime();
		String api_secret = api_target + api_input + api_timespan + MD5_KEY;
		api_secret = DigestUtils.md5Hex(api_secret);
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("api_target", api_target);
			params.put("api_input", api_input);
			params.put("api_timespan", api_timespan);
			params.put("api_token", getOauthInfo().getAccessToken());
			params.put("api_secret", api_secret);
			params.put("api_project", "api_java");
			String result = Http_Request_Post.doPost(PHOTO_URL, params, "utf-8");
			JSONObject object = JSONObject.parseObject(result);
			String resultCode = object.get("resultCode").toString();
			if("1".equals(resultCode)) {
				JSONObject user = object.getJSONObject("user");
				JSONObject avatar = user.getJSONObject("avatar");
				photoUrl = avatar.getString("thumb");
				nickName = user.getString("nickname_hjy");
				if("".equals(nickName)) {
					String mobile = user.getString("mobile");
					nickName = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//获取积分，客户等级
		String custLvl = "10";
		String custPoint = "0";
		try {
			PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(getUserCode()));	
			JSONObject infoMap = new JSONObject();
			infoMap.put("cust_id", levelInfo.getCustId());
			String jyResult = getHttps(JY_URL + "getCustAccmByCustId", infoMap.toString());
			JSONObject jyObject = JSONObject.parseObject(jyResult);
			boolean success = jyObject.getBoolean("success");
			if(success) {
				custLvl = jyObject.getString("CUST_LVL");
				custPoint = jyObject.getString("ACCM_AMT");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		ApiVipExclusiveViperResult apiResult = new ApiVipExclusiveViperResult();
		apiResult.setNickName(nickName);
		apiResult.setCustLvl(custLvl);
		apiResult.setCustPoint(custPoint);
		apiResult.setPhotoUrl(photoUrl);
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
