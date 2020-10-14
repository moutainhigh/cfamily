package com.cmall.familyhas.api;


import java.util.Map;

import com.cmall.familyhas.service.GoodsCricleService;
import com.cmall.groupcenter.groupapp.model.GetGoodsCricleListInfoInput;
import com.cmall.groupcenter.groupapp.model.GetGoodsCricleListInfoResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 商圈数据（热销榜,超返利）
 * 
 * @author wangzx 
 * 
 */
public class GetGoodsCricleListInfoApi extends RootApiForManage<GetGoodsCricleListInfoResult, GetGoodsCricleListInfoInput>{ 

	public GetGoodsCricleListInfoResult Process(GetGoodsCricleListInfoInput inputParam,
			MDataMap mRequestMap) {
		
		//long start = System.currentTimeMillis();
	    GoodsCricleService gcService= new GoodsCricleService();
		GetGoodsCricleListInfoResult result =gcService.generateGoodsCricleInfo(inputParam,this.getApiKey(),null);
		//long end = System.currentTimeMillis();
		//System.out.println( "优化后的:"+(end-start));
		return result;
	}
	
	public String getApiKey(){
		String apkKey=null;
		 String productShareSql ="select api_key from  zapdata.za_apiauthorize where  manage_code=:manage_code";
    	 Map<String, Object> rmap = DbUp.upTable("gc_product_share_log").dataSqlOne(productShareSql, new MDataMap("manage_code",this.getManageCode()));
		 if(rmap!=null && rmap.get("api_key")!=null){
			 apkKey  = String .valueOf(rmap.get("api_key"));
		 }
    	 return apkKey;
	}
	
	
}
