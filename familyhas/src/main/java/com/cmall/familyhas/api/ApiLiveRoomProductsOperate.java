package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;
import com.cmall.familyhas.api.input.ApiLiveRoomProductsOperateInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiLiveRoomProductsOperate extends RootApi<RootResultWeb, ApiLiveRoomProductsOperateInput> {

	public RootResultWeb Process(ApiLiveRoomProductsOperateInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub
		RootResultWeb result = new RootResultWeb();
		String liveRoomId = inputParam.getLiveRoomId();
		String doFlag = inputParam.getDoFlag();
		String productCode = inputParam.getpCode();
	   
		
		if("upOne".equals(doFlag)) {
			int index = 0;
			List<Map<String, Object>> sqlList = DbUp.upTable("lv_live_room_product").dataSqlList("select * from lv_live_room_product where delete_flag='1' and live_room_id=:live_room_id  order by sort desc,zid desc ", new MDataMap("live_room_id",liveRoomId));
            for (int i=0;i<sqlList.size();i++) {
            	Map<String, Object> map = sqlList.get(i);
				if(productCode.equals(map.get("product_code"))) {
					index=i;
					break;
				}
			}
            if(index!=0&&sqlList.size()>1) {
            	Map<String, Object> map = sqlList.get(index);
            	Map<String, Object> map1 = sqlList.get(index-1);           	
            	MDataMap mDataMap = new MDataMap(map);
            	MDataMap mDataMap1 = new MDataMap(map1);           	
            	mDataMap.put("sort", map1.get("sort").toString());
            	mDataMap1.put("sort", map.get("sort").toString());          	
            	DbUp.upTable("lv_live_room_product").update(mDataMap);
            	DbUp.upTable("lv_live_room_product").update(mDataMap1);
            }
			
		}else if("downOne".equals(doFlag)) {
			int index = 0;
			List<Map<String, Object>> sqlList = DbUp.upTable("lv_live_room_product").dataSqlList("select * from lv_live_room_product where delete_flag='1' and live_room_id=:live_room_id  order by sort desc,zid desc ", new MDataMap("live_room_id",liveRoomId));
            for (int i=0;i<sqlList.size();i++) {
            	Map<String, Object> map = sqlList.get(i);
				if(productCode.equals(map.get("product_code"))) {
					index=i;
					break;
				}
			}
            if(index!=(sqlList.size()-1)&&sqlList.size()>1) {
            	Map<String, Object> map = sqlList.get(index);
            	Map<String, Object> map1 = sqlList.get(index+1);           	
            	MDataMap mDataMap = new MDataMap(map);
            	MDataMap mDataMap1 = new MDataMap(map1);           	
            	mDataMap.put("sort", map1.get("sort").toString());
            	mDataMap1.put("sort", map.get("sort").toString());          	
            	DbUp.upTable("lv_live_room_product").update(mDataMap);
            	DbUp.upTable("lv_live_room_product").update(mDataMap1);
            }
		}else if("topOne".equals(doFlag)) {
			int index = 0;
			List<Map<String, Object>> sqlList = DbUp.upTable("lv_live_room_product").dataSqlList("select * from lv_live_room_product where delete_flag='1' and live_room_id=:live_room_id  order by sort desc,zid desc ", new MDataMap("live_room_id",liveRoomId));
            for (int i=0;i<sqlList.size();i++) {
            	Map<String, Object> map = sqlList.get(i);
				if(productCode.equals(map.get("product_code"))) {
					index=i;
					break;
				}
			}
            if(index!=0&&sqlList.size()>1) {
            	Map<String, Object> map = sqlList.get(index);
            	Map<String, Object> map1 = sqlList.get(0); 
            	int newSort = Integer.parseInt(map1.get("sort").toString())+1;
            	MDataMap mDataMap = new MDataMap(map);         	
            	mDataMap.put("sort", newSort+"");         	
            	DbUp.upTable("lv_live_room_product").update(mDataMap);
            }
			
		}else if("delOne".equals(doFlag)||"batchDel".equals(doFlag)) {
			String join = productCode.replace(",", "','");
			productCode = "('"+join+"')";
			DbUp.upTable("lv_live_room_product").dataExec("update lv_live_room_product set delete_flag='0' where live_room_id='"+liveRoomId+"' and product_code in "+productCode, null);

		}else {
			result.setResultCode(0);
			result.setResultMessage("无此操作类型!");
			return result;
		}
		
		int count = DbUp.upTable("lv_live_room_product").count("live_room_id",liveRoomId,"delete_flag","1");
		result.setResultMessage(count+"");
		result.setResultCode(1);

		return result;
	}

	

		

}
