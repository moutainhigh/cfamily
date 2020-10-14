package com.cmall.familyhas.job;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.quartz.JobExecutionContext;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobForWhetherUpdateBrandProductCount extends RootJob{

	
	@Override
	public void doExecute(JobExecutionContext context) {
		//从redis获取是否需要更新数据库标记
		String string = XmasKv.upFactory(EKvSchema.IsUpdateBrandProductCount).get("isUpdateBrandProductCount");
		if(!"no".equals(string)) {
			String sql11 = "SELECT " + 
					"	brand_code," + 
					"	count(brand_code) brand_count " + 
					"FROM " + 
					"	productcenter.pc_productinfo a " + 
					" WHERE  " + 
					"	a.product_status = '4497153900060002' AND brand_code != '' " + 
					" GROUP BY " + 
					"	brand_code"; 
			List<Map<String, Object>> dataSqlList = DbUp.upTable("pc_productinfo").dataSqlList(sql11, new MDataMap());
			 
			String insertSql =  "";
			if(null!=dataSqlList&&dataSqlList.size()>0) {
				insertSql += "insert into uc_brand_product_count(uid,brand_code,product_count) values";
				for(Map<String, Object> map : dataSqlList) {
					String sUid = UUID.randomUUID().toString().replace("-", "");
					String brand_code = map.get("brand_code").toString();
					String brand_count = map.get("brand_count").toString();
					insertSql+="('"+sUid+"','"+brand_code+"','"+brand_count+"'),";
				} 
			}
			if(insertSql.length()>0) {
				insertSql = insertSql.substring(0, insertSql.length()-1);
				//删除分类商品数量表记录
				String sql12 = "delete from uc_brand_product_count"; 
				DbUp.upTable("uc_brand_product_count").dataExec(sql12, new MDataMap()); 
				DbUp.upTable("uc_brand_product_count").dataExec(insertSql, new MDataMap()); 
			}
			XmasKv.upFactory(EKvSchema.IsUpdateBrandProductCount).set("isUpdateBrandProductCount","no");
		}
	}
	
	

	
}
