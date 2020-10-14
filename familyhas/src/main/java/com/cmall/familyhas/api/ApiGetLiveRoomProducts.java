package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.input.ApiGetLiveRoomProductsInput;
import com.cmall.familyhas.api.result.ApiGetLiveRoomProductsResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiGetLiveRoomProducts extends RootApi<ApiGetLiveRoomProductsResult, ApiGetLiveRoomProductsInput> {

	public ApiGetLiveRoomProductsResult Process(ApiGetLiveRoomProductsInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		ApiGetLiveRoomProductsResult result = new ApiGetLiveRoomProductsResult();
		String liveRoomId = inputParam.getLiveRoomId();
		String pageNum = inputParam.getPageNum();
		String productCode = inputParam.getProductCode();
		String productName = inputParam.getProductName();
	    int size = 10;					
		String sql = "select * from lv_live_room_product where live_room_id='"+liveRoomId+"' and delete_flag='1' ";
		String sWhere = "";
		if(StringUtils.isNotBlank(productName)) {
			sWhere = sWhere+" and product_name like '%"+productName+"%' ";
		}
		if(StringUtils.isNotBlank(productCode)) {
			sWhere = sWhere+" and product_code = '"+productCode+"' ";
		}
		
		int count =DbUp.upTable("lv_live_room_product").dataCount(" delete_flag='1' and live_room_id ='"+liveRoomId+"'"+sWhere, new MDataMap());	
		int totalPage = count/size;
		if(count%size!=0) {
			totalPage=totalPage+1;
		}	
		result.setTotalNum(count);
		result.setTotalPage(totalPage);
		result.setCurrentPage(Integer.parseInt(pageNum));
		sWhere=sWhere+" order by sort desc,zid desc ";
		if(StringUtils.isNotBlank(pageNum)) {
			int start = (Integer.parseInt(pageNum)-1)*size;
			sWhere = sWhere+" limit "+start+","+size;
		}else {
			sWhere = sWhere+" limit 0,"+size;
		}
		sql = sql+sWhere;
		List<Map<String, Object>> sqlList = DbUp.upTable("lv_live_room_product").dataSqlList(sql, null);
		for (Map<String, Object> map : sqlList) {
			MDataMap mDataMap = new MDataMap(map);
			//int clickNum = DbUp.upTable("lv_live_room_product_statistics").count("live_room_id",liveRoomId,"product_code",map.get("product_code").toString(),"behavior_type","449748620001");
			Map<String, Object> dataSqlOne = DbUp.upTable("lv_live_room_product_statistics").dataSqlOne("select IFNULL(sum(num),0) clickNum from lv_live_room_product_statistics where live_room_id=:live_room_id and product_code=:product_code and behavior_type=:behavior_type ", new MDataMap("live_room_id",liveRoomId,"product_code",map.get("product_code").toString(),"behavior_type","449748620001"));
			mDataMap.put("clickNum", (dataSqlOne==null?"0":dataSqlOne.get("clickNum").toString()));
			result.getProductList().add(mDataMap);
		}

		return result;
	}

	

		

}
