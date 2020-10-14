package com.cmall.familyhas.job;

import java.util.List;

import org.quartz.JobExecutionContext;

import com.cmall.familyhas.webfunc.FuncRnsyLDProductInfo;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;
import com.srnpr.zapweb.webdo.WebConst;

/**
 * 同步所有上架状态的LD商品信息
 */
public class JobForSyncLDGoods extends RootJob {
	
	public void doExecute(JobExecutionContext context) {
		List<MDataMap> productList = DbUp.upTable("pc_productinfo").queryAll("", "", "", new MDataMap("seller_code","SI2003","small_seller_code","SI2003","product_status","4497153900060002"));
		
		for(MDataMap product : productList){
			try {
				new FuncRnsyLDProductInfo().funcDo("", new MDataMap(WebConst.CONST_WEB_FIELD_NAME+"product_code",product.get("product_code")));
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
