package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.cmall.familyhas.api.input.ApiSynchCustIdSinceCouponInput;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 根据LD同步到惠家有的优惠券 截取cust_id 回填membercenter.mc_extend_info_homehas
 * @remark 
 * @author 任宏斌
 * @date 2019年11月14日
 */
public class ApiSynchCustIdSinceCoupon extends RootApi<RootResultWeb, ApiSynchCustIdSinceCouponInput>{
	public RootResultWeb Process(ApiSynchCustIdSinceCouponInput input,MDataMap mDataMap){
		RootResultWeb result = new RootResultWeb();
		String sql1 = "SELECT c.member_code, substring_index(substring_index(c.out_coupon_code, '-', - 2),'-','1') cust_id FROM ordercenter.oc_coupon_info c " 
				+" WHERE c.create_time BETWEEN :begin_time AND :end_time AND c.out_coupon_code != ''";
		List<Map<String, Object>> list = DbUp.upTable("oc_coupon_info").dataSqlList(sql1, new MDataMap("begin_time",input.getBegin_time(),"end_time",input.getEnd_time()));
		if(null != list && !list.isEmpty()) {
			for (Map<String, Object> coupon : list) {
				String member_code = coupon.get("member_code")+"";
				String cust_id = coupon.get("cust_id")+"";
				
				List<Map<String, Object>> listByWhere = DbUp.upTable("mc_extend_info_homehas").listByWhere("member_code",member_code);
				
				int changeCount = 0;
				//通过member_code查惠家有的cust_id 如果不存在则插入
				if(listByWhere.size() > 0) {
					for (Map<String, Object> member : listByWhere) {
						//cust_id为空 则根据zid更新cust_id
						if(null == member.get("homehas_code") || "".equals(member.get("homehas_code").toString())) {
							MDataMap params = new MDataMap();
							params.put("zid", member.get("zid").toString());
							params.put("homehas_code", cust_id);
							DbUp.upTable("mc_extend_info_homehas").dataUpdate(params, "homehas_code", "zid");
							
							changeCount ++;
						}
						
						if(cust_id.equals(member.get("homehas_code"))) {
							changeCount ++;
						}
					}
				}
				
				if(listByWhere.size() == 0 || changeCount == 0) {
					DbUp.upTable("mc_extend_info_homehas").insert("uid",
							UUID.randomUUID().toString().replace("-", ""), "member_code", member_code,
							"homehas_code", cust_id);
				}
			}
		}
		return result;
	}
	
}
