package com.cmall.familyhas.api;


import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ProductCommentDelInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;
/**
 * @title 删除评论
 * @author shenghaoran
 *
 */

public class ApiProductCommentDel extends RootApiForToken<RootResultWeb, ProductCommentDelInput>{
	
	
	public RootResultWeb Process(ProductCommentDelInput input, MDataMap mRequestMap) {
		RootResultWeb result = new RootResultWeb();
		MDataMap mData = DbUp.upTable("nc_order_evaluation").one("uid",input.getUid());
		if(mData == null){
            result.setResultCode(0);
			result.setResultMessage("评价不存在");
			return  result;
		}else if(!getUserCode().equalsIgnoreCase(mData.get("order_name"))){
            result.setResultCode(0);
			result.setResultMessage("原评价人和当前用户不一致");
			return  result;
		}
		
		MDataMap mdata = new MDataMap();
		mdata.put("evaluation_status_user", "449746810002");
		mdata.put("check_flag", "4497172100030001");    
		mdata.put("uid", input.getUid());
		DbUp.upTable("nc_order_evaluation").dataUpdate(mdata, "evaluation_status_user,check_flag", "uid");
		
		//查看是否有追评，如果有追评，改为待审核状态
		String sql =   "SELECT * FROM newscenter.nc_order_evaluation_append WHERE evaluation_uid= '"+input.getUid()+"'";
		List<Map<String, Object>> dataSqlList = DbUp.upTable("nc_order_evaluation_append").dataSqlList(sql, null);
		if(null!=dataSqlList&&dataSqlList.size()>0) {
			MDataMap mdata1 = new MDataMap();
			mdata1.put("check_flag", "4497172100030001");    
			mdata1.put("evaluation_uid", input.getUid());
			DbUp.upTable("nc_order_evaluation_append").dataUpdate(mdata1, "check_flag", "evaluation_uid");
		}
		return  result;
	}
	
	
	
}
