package com.cmall.familyhas;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.basesupport.WebClientSupport;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 批量更新商品状态
 */
public class BatchUpdateProductStatus {
	
	public static void main(String[] args) throws Exception {
		File file = new File("C:\\Users\\Administrator\\Desktop\\products.txt");
		List<String> lines = FileUtils.readLines(file);
		
		int all = 0,success = 0;
		MDataMap mDataMap = new MDataMap(); 
		String[] vs;
		for (String line : lines) {
			line = StringUtils.trim(line);
			if(StringUtils.isEmpty(line)){
				continue;
			}

			vs = line.split(",");
			if(vs.length != 2 || StringUtils.isBlank(vs[0]) || StringUtils.isBlank(vs[1])){
				continue;
			}
			
			all++;
			
			mDataMap.put("product_code", StringUtils.trim(vs[0]));
			mDataMap.put("product_status", StringUtils.trim(vs[1]));
			
			success += DbUp.upTable("pc_productinfo").dataExec("update pc_productinfo set product_status = :product_status where product_code = '"+mDataMap.get("product_code")+"'", mDataMap);
		
			System.out.println("update pc_productinfo set product_status = '"+mDataMap.get("product_status")+"' where product_code = '"+mDataMap.get("product_code")+"';");
			
			MDataMap dataMap = new MDataMap();
			dataMap.put("productCode", mDataMap.get("product_code"));
			WebClientSupport.upPost("http://api-clarge.syapi.ichsy.com/clarge/solr/query/addone", dataMap);
		}
		
		System.out.println("完成， 总数："+all+", 更新数："+success);
	}
	
}
