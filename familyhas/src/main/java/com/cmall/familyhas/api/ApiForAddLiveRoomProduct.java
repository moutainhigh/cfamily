package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.cmall.familyhas.api.input.ApiForAddLiveRoomProductInput;
import com.cmall.familyhas.api.result.ApiForAddLiveRoomProductResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;


public class ApiForAddLiveRoomProduct extends RootApi<ApiForAddLiveRoomProductResult,  ApiForAddLiveRoomProductInput>{

	@Override
	public ApiForAddLiveRoomProductResult Process(ApiForAddLiveRoomProductInput inputParam, MDataMap mRequestMap) {
		// TODO Auto-generated method stub\
		ApiForAddLiveRoomProductResult result = new ApiForAddLiveRoomProductResult();
		String productCodes = inputParam.getProductCodes();
		String liveRoomId = inputParam.getLiveRoomId();
		int maxNum = 200;
		if (StringUtils.isEmpty(productCodes)) {
			result.setResultCode(0);
			result.setResultMessage("添加商品不能为空!");
			return result;
		} else {
			productCodes = productCodes.replace(",,", ",");
			if(StringUtils.startsWith(productCodes, ",")) {
				productCodes = StringUtils.substringAfter(productCodes, ",");
			}
			if(StringUtils.endsWith(productCodes, ",")) {
				productCodes = StringUtils.substringBeforeLast(productCodes, ",");
			}
		}
		String sWhere = "product_code in ('"+productCodes.replace(",", "','")+"')";
		String sFields = "";
		List<MDataMap> mapList=DbUp.upTable("pc_productinfo").queryAll(sFields, "", sWhere,null);
		//总数不能超过200
		List<Map<String, Object>> list = DbUp.upTable("lv_live_room_product").queryMapPriLibList("live_room_id",liveRoomId,"delete_flag","1");
		List<String> validateList = new ArrayList<String>();
		for (Map<String, Object> map : list) {
			validateList.add(map.get("product_code").toString());
		}
		List<String> asList = Arrays.asList(productCodes.split(","));
		validateList.retainAll(asList);
		int totalNum = mapList.size()+list.size();
		 
		if(totalNum>maxNum) {
			result.setResultCode(0);
			result.setResultMessage("添加商品总数超过200个,请重新导入!");
			return result;
		}else if(validateList.size()>0){
			result.setResultCode(0);
			result.setResultMessage("添加有重复商品,重复的商品编号为:"+StringUtils.join(validateList, ","));
			return result;
		}else {
			result.setTotalNum(totalNum);
			result.setLeftNum(maxNum-totalNum);
		} 
		//查询最大顺序
		 List<Map<String, Object>> dataSqlList = DbUp.upTable("lv_live_room_product").dataSqlList("select IFNULL(MAX(sort),0) maxsort from lv_live_room_product where live_room_id='"+liveRoomId+"' and delete_flag='1' ",null);
		int startSort = 1;
		if(dataSqlList!=null&&dataSqlList.size()>0) {
			startSort = Integer.parseInt(dataSqlList.get(0).get("maxsort").toString())+1;
		}
	    for (MDataMap mDataMap : mapList) {
			MDataMap insertMap = new MDataMap();
			insertMap.put("uid",WebHelper.upUuid() );
			insertMap.put("live_room_id",liveRoomId );
			insertMap.put("product_code",mDataMap.get("product_code") );
			insertMap.put("product_name",mDataMap.get("product_name") );
			insertMap.put("product_picture",mDataMap.get("mainpic_url") );
			insertMap.put("product_price",mDataMap.get("min_sell_price") );
			insertMap.put("product_market_price", mDataMap.get("market_price"));
			insertMap.put("sort",startSort+"" );
	    	DbUp.upTable("lv_live_room_product").dataInsert(insertMap);
	    	startSort++;
		}
		return result;
	}



}
