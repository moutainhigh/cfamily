package com.cmall.familyhas.job;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.quartz.JobExecutionContext;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

public class JobForUpdateFourthCategoryProducts extends RootJob{

	
	@Override
	public void doExecute(JobExecutionContext context) {
		String sql11 = "select category_code,count(category_code) product_count from usercenter.uc_sellercategory_product_relation a " + 
				"LEFT JOIN productcenter.pc_productinfo b ON a.product_code = b.product_code WHERE b.product_status = '4497153900060002' " + 
				"group by category_code"; 
		List<Map<String, Object>> dataSqlList = DbUp.upTable("uc_sellercategory_product_relation").dataSqlList(sql11, new MDataMap());
		 
		String insertSql =  "";
		if(null!=dataSqlList&&dataSqlList.size()>0) {
			insertSql += "insert into uc_sellercategory_product_count(uid,category_code,product_count) values";
			for(Map<String, Object> map : dataSqlList) {
				String sUid = UUID.randomUUID().toString().replace("-", "");
				String category_code = map.get("category_code").toString();
				String product_count = map.get("product_count").toString();
				insertSql+="('"+sUid+"','"+category_code+"','"+product_count+"'),";
			} 
		}
		if(insertSql.length()>0) {
			insertSql = insertSql.substring(0, insertSql.length()-1);
			//删除分类商品数量表记录
			String sql12 = "delete from uc_sellercategory_product_count"; 
			DbUp.upTable("uc_sellercategory_product_count").dataExec(sql12, new MDataMap()); 
			DbUp.upTable("uc_sellercategory_product_count").dataExec(insertSql, new MDataMap()); 
		}
	}
	
	

	
}
