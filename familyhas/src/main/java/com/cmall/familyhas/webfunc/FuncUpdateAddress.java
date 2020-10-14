package com.cmall.familyhas.webfunc;

import java.util.UUID;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 修改内部员工收货人地址信息
 * @author wz
 *
 */
public class FuncUpdateAddress extends FuncEdit{
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap updateData = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		String four = MapUtils.getString(mDataMap, "_street");
		updateData.put("area_code", four);
		

		MDataMap fourMap = DbUp.upTable("sc_tmp").one("code",four);
		String fourName = MapUtils.getString(fourMap, "name");
		String three = MapUtils.getString(fourMap, "p_code");
		
		MDataMap threeMap = DbUp.upTable("sc_tmp").one("code",three);
		String threeName = MapUtils.getString(threeMap, "name");
		String two = MapUtils.getString(threeMap, "p_code");
		
		MDataMap twoMap = DbUp.upTable("sc_tmp").one("code",two);
		String twoName = MapUtils.getString(twoMap, "name");
		String one = MapUtils.getString(twoMap, "p_code");
		
		MDataMap oneMap = DbUp.upTable("sc_tmp").one("code",one);
		String oneName = MapUtils.getString(oneMap, "name");
		
		
		updateData.put("address_province", oneName+twoName+threeName+fourName);
		
		String uid = updateData.get("uid");
		if(StringUtils.isBlank(uid)) {
			uid = UUID.randomUUID().toString().replace("-", "");
			updateData.put("uid", uid);
			updateData.put("create_time", DateUtil.getSysDateTimeString());
			updateData.put("address_id", WebHelper.upCode("DZ"));
			updateData.put("app_code", "SI2003");
			DbUp.upTable("nc_address").dataInsert(updateData);
		}else {
			updateData.put("update_time", DateUtil.getSysDateTimeString());
			DbUp.upTable("nc_address").dataUpdate(updateData, "address_default,address_name,address_mobile,address_province,address_street,area_code,update_time", "uid");
		}
		
		
		//默认地址
		String address_default = updateData.get("address_default");
		if ("1".equals(address_default)) {
			String sWhere = " address_default = '1' and uid <> '"+uid+"'";
			MDataMap defaultData = DbUp.upTable("nc_address").oneWhere("uid","",sWhere);
			if (null!=defaultData ) {
				MDataMap mUpdateMap = new MDataMap();
				mUpdateMap.put("uid", uid);		//保留的默认地址的uid
				mUpdateMap.put("update_time", DateUtil.getSysDateTimeString());
				String sSql = "update nc_address set update_time=:update_time , address_default = '0' where uid <> :uid and address_default='1'";
				DbUp.upTable("nc_address").dataExec(sSql, mUpdateMap);
			}
		}
		return mResult;
	}
}
