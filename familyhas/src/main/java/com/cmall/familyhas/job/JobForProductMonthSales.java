package com.cmall.familyhas.job;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 每天凌晨更新商品的月销量
 * @remark 
 * @author 任宏斌
 * @date 2019年12月26日
 */
public class JobForProductMonthSales extends RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		//清空表
		DbUp.upTable("pc_productsales_month").dataExec("truncate productcenter.pc_productsales_month", new MDataMap());
		//重新生成数据
		String sql = "SELECT product_code,SUM(sales) sales FROM productcenter.pc_productsales_everyday "
				+ " WHERE DAY >= DATE_SUB(CURDATE(), INTERVAL 31 DAY) AND seller_code='SI2003' "
				+ "GROUP BY product_code";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_productsales_everyday").dataSqlList(sql, new MDataMap());
		if(null != dataSqlList && !dataSqlList.isEmpty()) {
			for (Map<String, Object> map : dataSqlList) {
				DbUp.upTable("pc_productsales_month").insert("uid", WebHelper.upUuid(), "product_code",
						map.get("product_code") + "", "sales", map.get("sales") + "");
			}
		}
	}
}
