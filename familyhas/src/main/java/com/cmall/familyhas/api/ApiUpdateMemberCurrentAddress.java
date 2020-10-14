package com.cmall.familyhas.api;

import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.DataUtil;

import com.cmall.familyhas.api.input.ApiUpdateMemberCurrentAddressInput;
import com.cmall.familyhas.api.result.ApiForGetStoreDistrictResult.City;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.membercenter.api.GetMemberInfo;
import com.ctc.wstx.util.StringUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;
import com.srnpr.zapweb.webapi.RootApiForMember;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiUpdateMemberCurrentAddress extends RootApiForMember<RootResultWeb, ApiUpdateMemberCurrentAddressInput>{

	public RootResultWeb Process(ApiUpdateMemberCurrentAddressInput inputParam,MDataMap mRequestMap) {
			RootResultWeb result=new RootResultWeb();
			
		
			if(StringUtils.isEmpty(inputParam.getSqNum())){
				return result;
			}
			if(StringUtils.isEmpty(inputParam.getCity())){
				return result;
			}
			//通过设备流水号查询地址记录
			MDataMap addressInfo=DbUp.upTable("mc_member_current_address").one("sqNum",inputParam.getSqNum());
			MDataMap map=new MDataMap();
			map.put("city", inputParam.getCity());
			map.put("longitude", inputParam.getLongitude()+"");
			map.put("latitude", inputParam.getLatitude()+"");
			String userCode="";
			if(getOauthInfo()!=null){
				userCode=getOauthInfo().getUserCode();
			}
			map.put("member_code", userCode);
			//如果当前没有改设备流水号的地址记录，则插入一条记录
			if(addressInfo==null){
				map.put("sqNum", inputParam.getSqNum());
				map.put("create_time", DateUtil.getNowTime());
				DbUp.upTable("mc_member_current_address").dataInsert(map);
			}
			//如果有用户地址记录并且记录不同则更新信息
			else{
				map.put("zid", addressInfo.get("zid"));
				if(!(addressInfo.get("city").equals(inputParam.getCity()) && 
						addressInfo.get("longitude").equals(inputParam.getLongitude()+"") && 
						addressInfo.get("latitude").equals(inputParam.getLatitude()+"") && 
						addressInfo.get("member_code").equals(userCode)
						))
					
					map.put("update_time", DateUtil.getNowTime());
					DbUp.upTable("mc_member_current_address").dataUpdate(map, "", "zid");
				}
			
			return result;
	}

}
