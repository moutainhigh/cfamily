package com.cmall.familyhas.api;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.cmall.familyhas.api.input.ApiCallLogInput;
import com.cmall.familyhas.api.result.ApiCallLogResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.WebClientRequest;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapcom.topdo.TopDir;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 电话订购及呼叫记录
 * @author fq
 *
 */
public class ApiCallLog extends RootApiForManage<ApiCallLogResult,ApiCallLogInput>{

	public ApiCallLogResult Process(ApiCallLogInput inputParam,
			MDataMap mRequestMap) {
		ApiCallLogResult result = new ApiCallLogResult();
		
		try {
			String rtnMsg = callJY(inputParam.getProductCode(),inputParam.getMobile());
			JSONObject rtnJson = new JSONObject(rtnMsg);
			Boolean isTrue = (Boolean) rtnJson.get("success");
			String call_status = "";
			if(isTrue ) {
				call_status = "1001";
			} else {
				call_status = "1000";
				result.setResultCode(0);
				result.setResultMessage("接口调用失败:"+rtnJson.getString("result"));
			}
			DbUp.upTable("lc_call_log").insert("call_mobile",inputParam.getMobile(),
					"syn_message",rtnJson.toString(),
					"jy_call_num",rtnJson.getString("result"),
					"product_code",inputParam.getProductCode(),
					"call_status",call_status,
					"syn_time",DateUtil.getSysDateTimeString());
		} catch (Exception e) {
			result.setResultCode(0);
			result.setResultMessage("接口调用失败:"+e.getMessage());
			e.printStackTrace();
		} 
		return result;
	}
	
	/**
	 * 同步数据到家有
	 * @param pcStr
	 * @return
	 * @throws Exception
	 */
	private String callJY(String pcStr,String mobile) throws Exception {
		JSONObject pcJson = new JSONObject();
		JSONArray pcInfo = new JSONArray();
		String rtnMessage = "";
		if(null != pcStr) {
			String[] pcArray = pcStr.split(",");
			for (String pc : pcArray) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("good_id", pc);
				pcInfo.put(jsonObj);
			}
			pcJson.put("mobile", mobile);
			pcJson.put("goods", pcInfo);
			rtnMessage = getHttps(upRequestUrl() ,pcJson.toString());
		}
		
		return rtnMessage;
	}
	
	/**
	 * 获取请求的url
	 * 
	 * @return
	 */
	private String upRequestUrl() {
		return bConfig("groupcenter.rsync_homehas_url")
				+ "requestOrder";
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
