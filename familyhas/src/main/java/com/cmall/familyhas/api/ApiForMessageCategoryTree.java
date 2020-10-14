package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.cmall.familyhas.api.input.ApiForMessageCategoryTreeInput;
import com.cmall.familyhas.api.result.ApiForMessageCategoryTreeResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiForMessageCategoryTree extends RootApi<ApiForMessageCategoryTreeResult, ApiForMessageCategoryTreeInput> {

	public ApiForMessageCategoryTreeResult Process(ApiForMessageCategoryTreeInput inputParam,
			MDataMap mRequestMap) {
		
		ApiForMessageCategoryTreeResult  result = new ApiForMessageCategoryTreeResult();
		
		/**获取当前所属APP*/
		String appCode =  inputParam.getApp_code();
		
		List<MDataMap> dataList=DbUp.upTable("hp_message_category").queryAll("", "sort", "manage_code=:manage_code", new MDataMap("manage_code",appCode));
		if (dataList!=null&&dataList.size()>0) {
			for (MDataMap mDataMap : dataList) {
				List<String> list = new ArrayList<String>();
				list.add(mDataMap.get("category_code"));
				list.add(mDataMap.get("category_name"));
				list.add(mDataMap.get("parent_code"));
				list.add(mDataMap.get("uid"));
				list.add(mDataMap.get("sort"));
				list.add(mDataMap.get("category_note"));
				list.add(mDataMap.get("category_img"));
				
				result.getList().add(list);
			}
		}else{
			MDataMap inputMap = new MDataMap();
			String sUid = UUID.randomUUID().toString().replace("-", "");
			inputMap.put("uid", sUid);
			inputMap.put("manage_code", appCode);
			inputMap.put("category_code", "44980001");
			inputMap.put("category_name", "信息类型");
			inputMap.put("parent_code", "4498");
			inputMap.put("sort", "44980001");
			DbUp.upTable("hp_message_category").dataInsert(inputMap);
			
			List<String> list = new ArrayList<String>();
			list.add(inputMap.get("category_code"));
			list.add(inputMap.get("category_name"));
			list.add(inputMap.get("parent_code"));
			list.add(inputMap.get("uid"));
			list.add(inputMap.get("sort"));
			list.add(inputMap.get("category_note"));
			list.add(inputMap.get("category_img"));
			
			result.getList().add(list);
		}
		
		return result;

	}
}
