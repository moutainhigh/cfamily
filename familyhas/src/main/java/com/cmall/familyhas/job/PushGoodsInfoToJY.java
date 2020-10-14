package com.cmall.familyhas.job;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.quartz.JobExecutionContext;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basemodel.WebClientRequest;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapcom.topdo.TopDir;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 如果有更新商品信息，则向家有推送提醒
 * @author fq
 *
 */
public class PushGoodsInfoToJY extends RootJob{
	
	public void doExecute(JobExecutionContext context) {

		String url = upRequestUrl();
		//提取更新过的信息
		saveUpdateGoodsInfo();
		
		List<MDataMap> all = DbUp.upTable("pc_push_productinfo").query("product_code", "", "flag_push_jy=:flag_push_jy", new MDataMap("flag_push_jy","0"), 0, 20);
		JSONObject json = new JSONObject();
		StringBuffer product_nums = new StringBuffer();
		for (MDataMap mDataMap : all) {
			product_nums.append(mDataMap.get("product_code"));
			product_nums.append(",");
		}
		String good_id = "";
		if(product_nums.length() > 0) {
			good_id = product_nums.substring(0, product_nums.length()-1);
			json.put("good_id", good_id);
		}
		try {
			getHttps(url, json.toString());
			
			String[] id = good_id.split(",");
			for (int i = 0; i < id.length; i++) {
				MDataMap mDataMap = new MDataMap();
				//System.out.println("修改的商品编号："+id[i]);
				mDataMap.put("product_code", id[i]);
				mDataMap.put("flag_push_jy", "1");
				mDataMap.put("push_jy_time", DateUtil.getSysDateTimeString());
				DbUp.upTable("pc_push_productinfo").dataUpdate(mDataMap,"push_jy_time,flag_push_jy","product_code");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	
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
	
	/**
	 * 获取请求的url
	 * 
	 * @return
	 */
	private String upRequestUrl() {
		return bConfig("groupcenter.wx_url")
				+ "createGoods";
	}
	
	
	/**
	 * 添加更新过的商品信息添加到推送列表
	 */
	private void saveUpdateGoodsInfo() {
		
		Map<String, Object> map= DbUp.upTable("pc_push_productinfo").dataSqlOne("SELECT max(update_time) max_time from pc_push_productinfo where flag_push_jy = 1  ", new MDataMap());
		Object max_time = map.get("max_time");
		if(null == max_time || "".equals(max_time)) {
			max_time = "2014-10-20 00:00:00";
		}
		List<MDataMap> saveAll = DbUp.upTable("pc_productinfo").query("product_code,update_time,product_status", "", "date_format(update_time,'%Y-%m-%d %H:%i:%s')>date_format(:update_time,'%Y-%m-%d %H:%i:%s') and seller_code = 'SI2003' ", new MDataMap("update_time",String.valueOf(max_time)), -1, -1);
		if(saveAll.size() > 0) {
			for (MDataMap mDataMap : saveAll) {
				String product_status = mDataMap.get("product_status");
				//判断编辑过的商品是否是下架状态
				if("4497153900060002".equals(product_status)) {//如果是上架状态，则不标识推送给微信
					DbUp.upTable("pc_push_productinfo").insert("product_code",mDataMap.get("product_code"),"update_time",mDataMap.get("update_time"));
				} else {
					DbUp.upTable("pc_push_productinfo").insert("product_code",mDataMap.get("product_code"),"update_time",mDataMap.get("update_time"),"flag_push_wx","0");
				}
			}
			
		}
		
	} 
	
}
