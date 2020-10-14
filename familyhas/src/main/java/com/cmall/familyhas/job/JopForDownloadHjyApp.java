package com.cmall.familyhas.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.model.DownloadHjyApp;
import com.cmall.groupcenter.util.HttpUtil;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 查询北呼邀约客户信息接口 符合条件 修改集团用户信息
 * 
 * @author wm
 *
 */
public class JopForDownloadHjyApp extends RootJob {

	private static Logger logger = Logger.getLogger(JopForDownloadHjyApp.class);

	private final String JY_URL = bConfig("groupcenter.rsync_homehas_url");

	/*
	 * private final String JY_URL =
	 * "http://localhost:8080/AppInterface/service/interface/";
	 */

	/**
	 * 
	 */
	public void doExecute(JobExecutionContext context) {
		// TODO Auto-generated method stub
		String sysDateTimeString = DateUtil.getSysDateTimeString();
		String response = HttpUtil.postJson(JY_URL + "getDownloadHjyApp", "");
		// logger.info("response---------->>>:" + response);
		System.out.println("response---------->>>:" + response);
		// HashMap<String, String> maps = (HashMap) JSON.parse(response);
		Map<String, Object> maps = JSONObject.parseObject(response);
		if (response == null || maps.get("success").equals("false")) {
			return;
		} else {
			String sysDateTimeString2 = DateUtil.getSysDateTimeString();
			List<DownloadHjyApp> parseArray = JSONArray
					.parseArray((String) JSON.toJSONString(maps.get("downloadHjyAppList")), DownloadHjyApp.class);
			// 根据手机号查询mc_login_info表（login_name = ? and manager_code = 'SI2003'）
			String phone = "";
			for (DownloadHjyApp downloadHjyApp : parseArray) {
				phone += downloadHjyApp.getPHONE() + ",";
			}
			if (phone.length() > 0) {
				phone = phone.substring(0, phone.length() - 1);

			}
			if (!StringUtils.isEmpty(phone)) {
				List<Map<String, Object>> dataSqlList = DbUp.upTable("mc_login_info")
						.dataSqlList("select * from membercenter.mc_login_info where login_name in (" + phone
								+ ") and manage_code = 'SI2003'", null);
				updateDownloadHjyApp(dataSqlList, parseArray);
				// 插入请求日志信息
				
				DbUp.upTable("lc_accept_log").insert("uid", UUID.randomUUID().toString().replaceAll("-", ""), "code",
						"LCAL161110100001", "moi_code_cd", "MPS001", "request_data", "", "response_data", response,
						"request_time", sysDateTimeString, "response_time", sysDateTimeString2, "flag_success", "0",
						"error_expection", "", "process_time", DateUtil.getSysDateTimeString(), "process_data", "",
						"process_num", String.valueOf(parseArray.size()), "success_num",
						String.valueOf(dataSqlList.size()));
			}

		}
	}

	/**
	 * 判断是否更新数据 外小 内大的原则循环
	 * 
	 * @param dataSqlList
	 *            hjy 数据
	 * @param parseArray
	 *            ld 数据
	 */
	public void updateDownloadHjyApp(List<Map<String, Object>> dataSqlList, List<DownloadHjyApp> downloadHjyApps) {
		if (!dataSqlList.isEmpty() && dataSqlList.size() > 0) {
			for (Map<String, Object> map : dataSqlList) {
				for (DownloadHjyApp resMap : downloadHjyApps) {
					if (resMap.getPHONE().equalsIgnoreCase((String) map.get("login_name"))) {// 当手机号相同
						String createTime = (String) map.get("create_time");
						String memberCode = (String) map.get("member_code");
						String etrDte = resMap.getETR_DATE();
						String phone = resMap.getPHONE();
						if (createTime.compareTo(etrDte) > 0) {
							// 调用app接口（北呼邀约客户处理结果回写接口），修改IS_FINISH = 'Y' IS_NEW_REGISTER = 'Y'
							// IS_INSTALL_SUCCESS = 'Y'
							updateDownloadHjyAppByCustId(phone, 1);
						} else {
							String createTimeByZaOauth = UpdateMcLoginInfoByOrder(memberCode, phone, downloadHjyApps);
							if (!StringUtils.isEmpty(createTimeByZaOauth)) {
								if (createTimeByZaOauth.compareTo(etrDte) > 0) {
									// 修改IS_ACTIVATE = 'Y' IS_FINISH = 'Y' IS_INSTALL_SUCCESS = 'Y'
									updateDownloadHjyAppByCustId(phone, 2);
								} else {
									// 修改IS_FINISH = 'Y' IS_INSTALL_SUCCESS = 'Y'
									updateDownloadHjyAppByCustId(phone, 3);
								}
							}
						}
					}
				}
			}

		}

	}

	private void updateDownloadHjyAppByCustId(String phone, Integer type) {
		Map<String, Object> postMap = new HashMap<String, Object>();
		if (type == 1) {
			postMap.put("updateSql",
					"update tb_download_hjy_app set IS_FINISH = 'Y',IS_NEW_REGISTER = 'Y',IS_INSTALL_SUCCESS = 'Y' where PHONE = '"
							+ phone + "'");
		} else if (type == 2) {
			postMap.put("updateSql",
					"update tb_download_hjy_app set IS_ACTIVATE = 'Y',IS_FINISH = 'Y',IS_INSTALL_SUCCESS = 'Y' where PHONE = '"
							+ phone + "'");
		} else if (type == 3) {
			postMap.put("updateSql",
					"update tb_download_hjy_app set IS_FINISH = 'Y',IS_INSTALL_SUCCESS = 'Y' where PHONE = '" + phone
							+ "'");
		} else {
			return;
		}
		try {
			HttpUtil.postJson(JY_URL + "updateDownloadHjyApp", JSONArray.toJSONString(postMap));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private String UpdateMcLoginInfoByOrder(String memberCode, String phone, List<DownloadHjyApp> downloadHjyApps) {

		// 查询用户 最近三个月是否存在订单
		Map<String, Object> one = DbUp.upTable("oc_orderinfo").dataSqlOne(
				"select create_time,buyer_code,seller_code  as dataget from oc_orderinfo where create_time > DATE_SUB(CURDATE(),INTERVAL 3 MONTH) and buyer_code = '"
						+ memberCode + "' and seller_code ='SI2003'",
				null);
		if (one != null) {
			updateDownloadHjyAppByCustId(phone, 3);
		} else {
			Map<String, Object> dataSqlOne = DbUp.upTable("za_oauth")
					.dataSqlOne("select create_time from za_oauth where manage_code = 'SI2003' AND user_code = '"
							+ memberCode + "' group by zid desc limit 1", null);
			if (dataSqlOne != null) {
				return (String) dataSqlOne.get("create_time");
			} else {
				updateDownloadHjyAppByCustId(phone, 3);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		JopForDownloadHjyApp jopForDownloadHjyApp = new JopForDownloadHjyApp();
		jopForDownloadHjyApp.doExecute(null);
		// System.out.println("response---------->>>:" + response);
	}

}
