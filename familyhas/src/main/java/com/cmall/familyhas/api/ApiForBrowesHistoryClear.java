package com.cmall.familyhas.api;

import com.cmall.familyhas.api.input.BrowesHistoryInput;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;


/** 
* @ClassName: ApiForBrowesHistoryClear 
* @Description: 删除浏览历史记录
* @author liqt
* @date 2015-8-12 下午6:38:04 
*  
*/
public class ApiForBrowesHistoryClear extends RootApiForToken<RootResultWeb, BrowesHistoryInput>{
	public RootResultWeb Process(BrowesHistoryInput input, MDataMap mDataMap){
		RootResultWeb result = new RootResultWeb();
		String memberCode = getUserCode();
		String productCode = input.getProductCode();
		if("".equals(productCode)) {
			//获取登录人编号
			//清空当前登录人的所有浏览记录
			DbUp.upTable("pc_browse_history").delete("member_code",memberCode);
			
			//浏览记录日志表置清除位. add by zht
			MDataMap map = new MDataMap();
			map.put("clear_time", DateUtil.getNowTime());
			map.put("status", "1");
			map.put("member_code", memberCode);
			String sql = "update pc_browse_history_log set status=:status, clear_time=:clear_time where member_code=:member_code and status=0";
			DbUp.upTable("pc_browse_history_log").dataExec(sql, map);
		}else {
			String[] split = productCode.split(",");
			for(String code : split) {
				//获取登录人编号
				//清空当前登录人的指定商品的浏览记录
				DbUp.upTable("pc_browse_history").delete("member_code",memberCode,"product_code",code);
				
				//浏览记录日志表置清除位. add by zht
				MDataMap map = new MDataMap();
				map.put("clear_time", DateUtil.getNowTime());
				map.put("status", "1");
				map.put("member_code", memberCode);
				map.put("product_code", code);
				String sql = "update pc_browse_history_log set status=:status, clear_time=:clear_time where member_code=:member_code and product_code=:product_code and status=0";
				DbUp.upTable("pc_browse_history_log").dataExec(sql, map);
			}
		}
		return result;
		
	}
}