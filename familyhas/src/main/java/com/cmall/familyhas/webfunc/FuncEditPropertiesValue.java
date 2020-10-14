package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改属性值
 * @author lgx
 *
 */
public class FuncEditPropertiesValue extends RootFunc{
      
		public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
			MWebResult mWebResult = new MWebResult();
			
			String uid = mDataMap.get("zw_f_uid");
			String properties_value = mDataMap.get("zw_f_properties_value").trim();
			
			Map<String, Object> propertiesValue = DbUp.upTable("uc_properties_value").dataSqlOne("SELECT * FROM uc_properties_value WHERE uid = '"+uid+"' AND is_delete = '0'", new MDataMap());
			if(propertiesValue == null) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("没有该属性值!");
				return mWebResult;
			}
			if(StringUtils.isEmpty(properties_value)) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("请填写属性值!");
				return mWebResult;
			}
			String properties_code = (String) propertiesValue.get("properties_code");
			
			String sql = "SELECT * FROM uc_properties_value WHERE properties_code = '"+properties_code+"' AND is_delete = '0' AND properties_value = '"+properties_value+"' AND uid != '"+uid+"'";
			List<Map<String,Object>> dataSqlList = DbUp.upTable("uc_properties_key").dataSqlList(sql, new MDataMap());
			if(null != dataSqlList && dataSqlList.size() > 0) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("相同属性下的属性值不可以重复!");
				return mWebResult;
			}
			
			String upDateTime = FormatHelper.upDateTime();
			// 该属性的属性值总数
			MDataMap propertiesMap = new MDataMap();
			propertiesMap.put("uid", uid);
			propertiesMap.put("properties_value", properties_value);
			propertiesMap.put("update_time", upDateTime);
			DbUp.upTable("uc_properties_value").dataUpdate(propertiesMap, "properties_value,update_time", "uid");
			
			return mWebResult;
		}
		
	}
