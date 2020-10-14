package com.cmall.familyhas.job;

import java.util.List;
import java.util.Map;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.service.CollageService;
import com.srnpr.xmassystem.support.PlusSupportEvent;
import com.srnpr.xmassystem.support.PlusSupportStock;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;


/**
 * @author Angel Joy
 * @date 2020年4月2日 上午11:17:41
 * @version 1.0
 * @desc 定时完成 零库存商品拼团订单。
 */
public class JobForComplateZeroStockProductCollage extends RootJob {

	
	@Override
	public void doExecute(JobExecutionContext context) {
		String sql = "SELECT * FROM systemcenter.sc_event_info WHERE event_type_code = '4497472600010024' AND event_status = '4497472700020002' AND flag_enable = '1' AND collage_type = '4497473400050001' AND begin_time <= sysdate() AND end_time >= sysdate()";
		List<Map<String,Object>> eventInfos = DbUp.upTable("sc_event_info").dataSqlList(sql, null);
		for(Map<String,Object> eventInfo : eventInfos) {
			if(eventInfo == null || eventInfo.isEmpty()) {//当前暂无拼团活动，不执行
				continue;
			}
			MDataMap mmap = new MDataMap(eventInfo);
			String event_code = mmap.get("event_code");
			MDataMap mWhereMap = new MDataMap("event_code",event_code);
			String sqlForItem = "SELECT * FROM systemcenter.sc_event_item_product WHERE event_code = :event_code AND flag_enable = '1' group by product_code";
			List<Map<String,Object>> products = DbUp.upTable("sc_event_item_product").dataSqlList(sqlForItem, mWhereMap);
			CollageService cs = new CollageService();
			for(Map<String,Object> map : products) {
				if(map == null) {
					continue;
				}
				String product_code = map.get("product_code")!=null?map.get("product_code").toString():"";
				boolean checkStock = this.checkStock(event_code,product_code);
				MDataMap mWhereMap2 = new MDataMap("product_code",product_code);
				if(checkStock) {//返回值为true时，需要获取到未成团的当前团，去补团。
					String collageSql = "SELECT a.collage_code FROM systemcenter.sc_event_collage_item a LEFT JOIN systemcenter.sc_event_collage b ON  a.collage_code = b.collage_code WHERE a.product_code = :product_code AND b.collage_status = '449748300001' group by a.collage_code";
					List<Map<String,Object>> collages = DbUp.upTable("sc_event_collage_item").dataSqlList(collageSql, mWhereMap2);
					for(Map<String,Object> collage : collages) {
						if(collage != null && !collage.isEmpty()) {
							cs.robotComplateCollage(collage.get("collage_code").toString());
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param sku_code
	 * @param product_code
	 * @return 返回值为true时，库存为0，需要机器人拼团
	 * 2020年4月2日
	 * Angel Joy
	 * boolean
	 */
	private boolean checkStock(String event_code, String product_code) {
		MDataMap mWhereMap = new MDataMap("product_code",product_code,"event_code",event_code);
		PlusSupportEvent pse = new PlusSupportEvent();
		String sql = "SELECT item_code FROM systemcenter.sc_event_item_product WHERE event_code = :event_code AND product_code = :product_code and flag_enable = 1";
		List<Map<String,Object>> items = DbUp.upTable("sc_event_item_product").dataSqlList(sql, mWhereMap);
		long eventStock = 0;
		for(Map<String,Object> item : items) {
			if(item == null || item.isEmpty() || item.get("item_code") == null) {
				continue;
			}
			eventStock += pse.upEventItemSkuStock(item.get("item_code").toString());
		}
		if(eventStock <= 0) {
			return true;
		}
		PlusSupportStock pss = new PlusSupportStock();
		Integer productStock = pss.upAllStockForProduct(product_code);
		if(productStock <= 0) {
			return true;
		}
		//查询参与拼团的真实库存。
		String sqlSku = "SELECT sku_code FROM systemcenter.sc_event_item_product WHERE event_code = :event_code AND product_code = :product_code AND flag_enable = '1'";
		List<Map<String,Object>> skus = DbUp.upTable("sc_event_item_product").dataSqlList(sqlSku,mWhereMap);
		Integer stockAll = 0;
		for(Map<String,Object> sku : skus) {
			if(sku == null || sku.isEmpty()) {
				continue;
			}
			String sku_code = sku.get("sku_code").toString();
			stockAll +=pss.upSalesStock(sku_code);
		}
		if(stockAll <= 0) {
			return true;
		}
		return false;
	}

}
