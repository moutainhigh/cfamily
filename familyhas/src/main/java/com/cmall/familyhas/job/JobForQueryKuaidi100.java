package com.cmall.familyhas.job;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.webfunc.SynExpress;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 定时主动查询快递100物流轨迹
 */
public class JobForQueryKuaidi100  extends RootJob {
	
	private final ReentrantLock lock = new ReentrantLock();

	public void doExecute(JobExecutionContext context) {
		// 防止多次运行
		if(!lock.tryLock()) return;
		
		try {
			// 查询已发货且有运单号的订单，忽略第三方商品订单
			String sql = "SELECT s.order_code, s.logisticse_code, s.waybill FROM `oc_orderinfo` o"
					+ " LEFT JOIN oc_order_shipments s ON o.order_code = s.order_code"
					+ " WHERE o.seller_code = 'SI2003' AND o.order_status = '4497153900010003'"
					+ " AND o.small_seller_code != 'SI2003' AND o.order_code like 'DD%' AND s.is_send100_flag = 1"
					+ " AND o.small_seller_code NOT IN('SI2003','SF031JDSC','SF03WYKLPT')"
					+ " AND s.create_time > DATE_SUB(DATE(NOW()),INTERVAL 5 DAY)";
			
			List<Map<String, Object>> orderList = DbUp.upTable("oc_order_shipments").dataSqlList(sql, new MDataMap());
			String orderCode,logisticseCode,waybill;
			SynExpress synExpress = new SynExpress();
			for(Map<String, Object> order : orderList) {
				orderCode = (String)order.get("order_code");
				logisticseCode = (String)order.get("logisticse_code");
				waybill = (String)order.get("waybill");
				
				// 忽略已经签收的
				// 忽略无任何物流轨迹的，避免查询无效运单号
				List<MDataMap> list = DbUp.upTable("oc_express_detail").query("status", "`time` desc", "", new MDataMap("order_code",orderCode,"waybill",waybill),0,1);
				if(list.isEmpty() || "签收".equals(list.get(0).get("status"))) {
					continue;
				}
				
				// 查询物流轨迹
				synExpress.funcDo("", new MDataMap("zw_f_logisticseCode", logisticseCode, "zw_f_waybill", waybill,"zw_f_orderCode", orderCode));
			}
		} finally {
			lock.unlock();
		}

	}

}
