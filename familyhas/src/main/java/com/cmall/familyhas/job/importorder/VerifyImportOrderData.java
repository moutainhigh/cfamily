package com.cmall.familyhas.job.importorder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.api.orderimport.ImportOutOrder;
import com.cmall.familyhas.api.orderimport.ImportOutOrderByBdwm;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.MailSupport;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 
 * 类: VerifyImportOrderData <br>
 * 描述: 验证导入订单是否包含处于执行中状态的数据 <br>
 * 作者: zhy<br>
 * 时间: 2016年12月22日 上午10:25:27
 */
public class VerifyImportOrderData extends RootJob {

	@SuppressWarnings("unchecked")
	@Override
	public void doExecute(JobExecutionContext context) {
		StringBuffer content = new StringBuffer("问好<br>导入订单中存在长期处于执行中的状态信息，已执行删除操作，请导入人员根据邮件提示查看导入订单数据,重新导入<br>");
		List<Map<String, Object>> importOrderList = new ArrayList<Map<String, Object>>();
		// /**
		// * 民生商城
		// */
		// List<Map<String, Object>> msscData =
		// getImportOrderOvertime("4497151900140001");
		// boolean msscDataFlag = msscData != null && msscData.size() > 0;
		// if (msscDataFlag) {
		// content.append("民生商城：<br>");
		// content.append(createHtml(msscData));
		// importOrderList.addAll(msscData);
		// }
		// /**
		// * 电视宝商城
		// */
		// List<Map<String, Object>> dsbData =
		// getImportOrderOvertime("449715190013");
		// boolean dsbDataFlag = dsbData != null && dsbData.size() > 0;
		// if (dsbDataFlag) {
		// content.append("电视宝商城：<br>");
		// content.append(createHtml(dsbData));
		// importOrderList.addAll(dsbData);
		// }
		//
		boolean dataFlag = false;
		List<Map<String, Object>> array = getOrderSource();
		if (array != null && array.size() > 0) {
			for (Map<String, Object> map : array) {
				List<Map<String, Object>> data = getImportOrderOvertime(map.get("code").toString());
				dataFlag = data != null && data.size() > 0;
				if (dataFlag) {
					content.append(map.get("name") + "：<br>");
					content.append(createHtml(data));
					importOrderList.addAll(data);
				}
			}
		}

		if (importOrderList != null && importOrderList.size() > 0) {
			/**
			 * 分组查询导入订单
			 */
			Map<String, Object> keyMap = groupImportOrderDataKey(importOrderList);
			List<Map<String, Object>> keys = (List<Map<String, Object>>) keyMap.get("keys");
			String orderNumbers = keyMap.get("orderNumbers").toString().substring(0,
					keyMap.get("orderNumbers").toString().lastIndexOf(","));
			if (keys != null && keys.size() > 0) {
				/**
				 * 开启线程池
				 */
				ExecutorService fixedThreadPool = null;
				try {
					fixedThreadPool = Executors.newFixedThreadPool(50);
					for (int i = 0; i < keys.size(); i++) {
						Map<String, Object> key = keys.get(i);
						MDataMap importOrder = new MDataMap(key);
						List<Map<String, Object>> importData = DbUp.upTable("oc_orderinfo_import").dataSqlList(
								"select * from oc_orderinfo_import where import_status='4497479500010001' and order_source=:order_source and buyer_mobile=:buyer_mobile and order_number in("
										+ orderNumbers + ")",
								importOrder);
						if (importData != null) {
							fixedThreadPool.execute(new ImportOutOrder(importData));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					/**
					 * 关闭线程池
					 */
					if (fixedThreadPool != null) {
						fixedThreadPool.shutdown();
					}
				}
			}
		}
		/**
		 * 百度外卖
		 */
		List<Map<String, Object>> bdwmData = getImportBdwmOrderOvertime();
		boolean bdwmDataFlag = bdwmData != null && bdwmData.size() > 0;
		if (bdwmDataFlag) {
			content.append("百度外卖：<br>");
			content.append(createHtml(bdwmData));
			/**
			 * 分组查询导入订单
			 */
			Map<String, Object> keyMap = groupImportOrderDataKey(bdwmData);
			List<Map<String, Object>> keys = (List<Map<String, Object>>) keyMap.get("keys");
			String orderNumbers = keyMap.get("orderNumbers").toString().substring(0,
					keyMap.get("orderNumbers").toString().lastIndexOf(","));
			if (keys != null && keys.size() > 0) {
				/**
				 * 开启线程池
				 */
				ExecutorService fixedThreadPool = null;
				try {
					fixedThreadPool = Executors.newFixedThreadPool(50);
					for (int i = 0; i < keys.size(); i++) {
						Map<String, Object> key = keys.get(i);
						MDataMap importOrder = new MDataMap(key);
						List<Map<String, Object>> importData = DbUp.upTable("oc_orderinfo_bdwm").dataSqlList(
								"select * from oc_orderinfo_bdwm where import_status='4497479500010001' and order_source=:order_source and buyer_mobile=:buyer_mobile and order_number in("
										+ orderNumbers + ")",
								importOrder);
						if (importData != null) {
							fixedThreadPool.execute(new ImportOutOrderByBdwm(importData));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					/**
					 * 关闭线程池
					 */
					if (fixedThreadPool != null) {
						fixedThreadPool.shutdown();
					}
				}
			}
		}
		if (dataFlag || bdwmDataFlag) {
			MailSupport.INSTANCE.sendMail(bConfig("familyhas.import_order_dev"), "告知:删除导入订单长时间处于执行中的数据",
					content.toString());
			MailSupport.INSTANCE.sendMail(bConfig("familyhas.import_order_test"), "告知:删除导入订单长时间处于执行中的数据",
					content.toString());
		}

		/**
		 * 开启线程池批量修改数据
		 */
	}

	/**
	 * 
	 * 方法: getImportOrderOvertime <br>
	 * 描述: 更加订单来源查询导入订单表中执行状态为执行中，并且执行时间超过toLong的数据 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月22日 下午2:37:54
	 * 
	 * @param source
	 * @return
	 */
	private List<Map<String, Object>> getImportOrderOvertime(String order_source) {
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		StringBuffer sql = new StringBuffer("select * from ordercenter.oc_orderinfo_import");
		sql.append(" where order_source=:order_source");
		sql.append(" and import_status='4497479500010001'");
		sql.append(" and (oc_order_code ='' or oc_order_code is NULL) ");
		sql.append(" and create_time <=:create_time ");
		MDataMap param = new MDataMap();
		param.put("order_source", order_source);
		param.put("create_time", getImportOrderCreateTime());
		List<Map<String, Object>> list = DbUp.upTable("oc_orderinfo_import").dataSqlList(sql.toString(), param);
		String orderSelSql = "SELECT o.order_code FROM ordercenter.oc_orderinfo AS o,ordercenter.oc_orderdetail AS d WHERE o.order_code = d.order_code AND o.out_order_code =:out_order_code AND o.order_source =:order_source AND d.sku_code =:sku_code ORDER BY o.zid DESC LIMIT 1";
		for (Map<String, Object> map : list) {
			String zid = map.get("zid").toString();
			String uid = map.get("uid").toString();
			String order_number = map.get("order_number").toString();
			String sku_code = map.get("sku_code").toString();
			Map<String, Object> order = DbUp.upTable("oc_orderinfo").dataSqlOne(orderSelSql,
					new MDataMap("out_order_code", order_number, "order_source", order_source, "sku_code", sku_code));
			/**
			 * 如果已存在惠家有订单，修改导入订单状态并将惠家有订单存入导入订单表
			 */
			if (order != null) {
				MDataMap update = new MDataMap();
				update.put("zid", zid);
				update.put("uid", uid);
				update.put("error", "");
				update.put("oc_order_code", order.get("order_code").toString());
				update.put("import_status", "4497479500010002");
				update.put("update_time", DateUtil.getSysDateTimeString());
				DbUp.upTable("oc_orderinfo_import").update(update);
			} else {
				newList.add(map);
			}
		}
		return newList;
	}

	/**
	 * 
	 * 方法: getImportBdwmOrderOvertime <br>
	 * 描述: 更加订单来源查询导入百度订单表中执行状态为执行中，并且执行时间超过toLong的数据 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月22日 下午2:52:18
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getImportBdwmOrderOvertime() {
		List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
		StringBuffer sql = new StringBuffer("select * from ordercenter.oc_orderinfo_bdwm");
		sql.append(" where order_source=:order_source");
		sql.append(" and import_status='4497479500010001'");
		sql.append(" and (oc_order_code ='' or oc_order_code is NULL) ");
		sql.append(" and create_time <=:create_time ");
		MDataMap param = new MDataMap();
		param.put("order_source", "449715190012");
		param.put("create_time", getImportOrderCreateTime());
		List<Map<String, Object>> list = DbUp.upTable("oc_orderinfo_bdwm").dataSqlList(sql.toString(), param);

		String orderSelSql = "SELECT o.order_code FROM ordercenter.oc_orderinfo AS o,ordercenter.oc_orderdetail AS d WHERE o.order_code = d.order_code AND o.out_order_code =:out_order_code AND o.order_source =:order_source AND d.sku_code =:sku_code ORDER BY o.zid DESC LIMIT 1";
		for (Map<String, Object> map : list) {
			String zid = map.get("zid").toString();
			String uid = map.get("uid").toString();
			String order_number = map.get("order_number").toString();
			String sku_code = map.get("sku_code").toString();
			Map<String, Object> order = DbUp.upTable("oc_orderinfo").dataSqlOne(orderSelSql,
					new MDataMap("out_order_code", order_number, "order_source", "449715190012", "sku_code", sku_code));

			/**
			 * 如果已存在惠家有订单，修改导入订单状态并将惠家有订单存入导入订单表
			 */
			if (order != null) {
				MDataMap update = new MDataMap();
				update.put("zid", zid);
				update.put("uid", uid);
				update.put("error", "");
				update.put("oc_order_code", order.get("order_code").toString());
				update.put("import_status", "4497479500010002");
				update.put("update_time", DateUtil.getSysDateTimeString());
				DbUp.upTable("oc_orderinfo_bdwm").update(update);
			} else {
				newList.add(map);
			}
		}
		return newList;
	}

	/**
	 * 
	 * 方法: getImportOrderCreateTime <br>
	 * 描述: 根据执行过期限制时间获取订单导入创建时间 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月22日 下午2:50:07
	 * 
	 * @return
	 */
	private String getImportOrderCreateTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY,
				c.get(Calendar.HOUR_OF_DAY) - Integer.valueOf(bConfig("familyhas.import_order_long")));
		String createTime = DateUtil.toString(c.getTime(), "yyyy-MM-dd HH:mm:ss");
		return createTime;
	}

	/**
	 * 
	 * 方法: createHtml <br>
	 * 描述: 发送邮件内容 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月22日 下午7:42:15
	 * 
	 * @param list
	 * @return
	 */
	private static String createHtml(List<Map<String, Object>> list) {
		StringBuffer html = new StringBuffer();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				html.append(map.get("order_number")).append("    ").append(map.get("product_code")).append("    ")
						.append(map.get("buyer_mobile")).append("    ").append(map.get("order_create_time"))
						.append("<br>");
			}
		}
		return html.toString();
	}

	/**
	 * 
	 * 方法: groupImportOrderDataKey <br>
	 * 描述: 根据导入数据查询分组识别码订单编码和用户手机号 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月20日 下午3:01:31
	 * 
	 * @param list
	 * @return
	 */
	public static Map<String, Object> groupImportOrderDataKey(List<Map<String, Object>> list) {
		List<Map<String, Object>> keys = new ArrayList<Map<String, Object>>();
		StringBuffer orderNumbers = new StringBuffer();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i > 0) {
					Map<String, Object> m1 = list.get(i - 1);
					Map<String, Object> m2 = list.get(i);
					int order_number = m1.get("order_number").toString().compareTo(m2.get("order_number").toString());
					if (order_number != 0) {
						orderNumbers.append("'").append(m2.get("order_number")).append("',");
					}
					int buyer_mobile = m1.get("buyer_mobile").toString().compareTo(m2.get("buyer_mobile").toString());
					int order_source = m1.get("order_source").toString().compareTo(m2.get("order_source").toString());
					if (buyer_mobile == 0 && order_source == 0) {
						continue;
					} else {
						if (!keys.contains(keys)) {
							Map<String, Object> key = new HashMap<String, Object>();
							key.put("buyer_mobile", m2.get("buyer_mobile"));
							key.put("order_source", m2.get("order_source"));
							keys.add(key);
						}
					}
				} else {
					Map<String, Object> m1 = list.get(i);
					Map<String, Object> key = new HashMap<String, Object>();
					key.put("buyer_mobile", m1.get("buyer_mobile"));
					key.put("order_source", m1.get("order_source"));
					keys.add(key);
					orderNumbers.append("'").append(m1.get("order_number")).append("',");
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("keys", keys);
		map.put("orderNumbers", orderNumbers);
		return map;
	}

	/**
	 * 
	 * 方法: getOrderSource <br>
	 * 描述: 获取外部订单来源 <br>
	 * 作者: zhy<br>
	 * 时间: 2017年4月13日 上午11:03:24
	 * 
	 * @return
	 */
	private static List<Map<String, Object>> getOrderSource() {
		
		List<Map<String, Object>> array = DbUp.upTable("oc_import_define").dataSqlList(
				"select code,name from ordercenter.oc_import_define where flag_able='449746250001'", new MDataMap());
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map1 = new HashMap<String,Object>();
		map1.put("code", "449715190012");
		map1.put("name", "百度订单");
		list.add(map1);
		Map<String, Object> map2 = new HashMap<String,Object>();
		map2.put("code", "449715190013");
		map2.put("name", "电视宝商城");
		list.add(map2);
		Map<String, Object> map3 = new HashMap<String,Object>();
		map3.put("code", "4497151900140001");
		map3.put("name", "民生商城");
		list.add(map3);
		if(array != null && array.size()>0){
			list.addAll(array);
		}
		return list;
	}
}
