package com.cmall.familyhas.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiHbOrderDetailsInput;
import com.cmall.familyhas.api.model.HbOrderDetailInfo;
import com.cmall.familyhas.api.result.ApiHbOrderDetailsResult;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;


/**
 * 惠币订单明细接口
 */
public class ApiHbOrderDetails extends RootApiForToken<ApiHbOrderDetailsResult, ApiHbOrderDetailsInput> {
	@Override
	public ApiHbOrderDetailsResult Process(ApiHbOrderDetailsInput inputParam, MDataMap mRequestMap) {
		ApiHbOrderDetailsResult result = new ApiHbOrderDetailsResult();
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("buyer_code", getUserCode());
		String nextPage = inputParam.getNextPage();
		int startNumber = (Integer.parseInt(nextPage)-1) * 10;
		String sql = String.format("select * from fh_tgz_order_detail where buyer_code =:buyer_code order by zid desc limit %s,10",startNumber);
		List<Map<String,Object>> dataSqlList = DbUp.upTable("fh_tgz_order_detail").dataSqlList(sql, mWhereMap);
		List<HbOrderDetailInfo> list = result.getList();
		for(Map<String,Object> map : dataSqlList) {
			HbOrderDetailInfo info = new HbOrderDetailInfo();
			String order_code = map.get("order_code").toString();
			info.setOrder_code(order_code);
			mWhereMap.put("order_code", order_code);
			String product_code = map.get("product_code").toString();
			mWhereMap.put("product_code", product_code);
			Map<String, Object> dataSqlOne = DbUp.upTable("oc_orderinfo").dataSqlOne("select * from oc_orderinfo where order_code =:order_code and product_code =:product_code", mWhereMap);
			Map<String, Object> dataSqlOnepro = DbUp.upTable("pc_productinfo").dataSqlOne("select * from pc_productinfo where product_code =:product_code", mWhereMap);
			info.setProduct_name(dataSqlOnepro.get("product_name").toString());
			info.setOrder_status(dataSqlOne.get("order_status").toString());
			info.setOrder_time((map.get("create_time").toString()));
			info.setShow_price(map.get("show_price").toString());
			info.setTgz_money(map.get("tgz_money").toString());
			info.setTgz_type(map.get("tgz_type").toString());
			info.setSku_num(map.get("sku_num").toString());
			list.add(info);
		}
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(getUserCode()));
		String custId = levelInfo.getCustId();
		
		return null;
	}

}
