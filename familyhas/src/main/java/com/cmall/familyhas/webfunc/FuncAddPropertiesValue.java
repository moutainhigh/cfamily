package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加属性值
 * @author lgx
 *
 */
public class FuncAddPropertiesValue extends RootFunc{
      
		public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
			MWebResult mWebResult = new MWebResult();
			
			String properties_code = mDataMap.get("properties_code");//属性编号
			String properties_value = mDataMap.get("zw_f_properties_value").trim();
			
			Map<String, Object> propertiesKey = DbUp.upTable("uc_properties_key").dataSqlOne("SELECT * FROM uc_properties_key WHERE properties_code = '"+properties_code+"' AND is_delete = '0'", new MDataMap());
			if(propertiesKey == null) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("没有该属性!");
				return mWebResult;
			}
			if(StringUtils.isEmpty(properties_value)) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("请填写属性值!");
				return mWebResult;
			}
			
			String sql = "SELECT * FROM uc_properties_value WHERE properties_code = '"+properties_code+"' AND is_delete = '0' AND properties_value = '"+properties_value+"'";
			List<Map<String,Object>> dataSqlList = DbUp.upTable("uc_properties_key").dataSqlList(sql, new MDataMap());
			if(null != dataSqlList && dataSqlList.size() > 0) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("相同属性下的属性值不可以重复!");
				return mWebResult;
			}
			
			String upDateTime = FormatHelper.upDateTime();
			// 最后一条属性值排序值
			Map<String, Object> dataSqlOne = DbUp.upTable("uc_properties_value").dataSqlOne("SELECT * FROM uc_properties_value WHERE properties_code = '"+properties_code+"' ORDER BY sort_num DESC LIMIT 1", new MDataMap());
			int dataCount = 0;
			if(null != dataSqlOne) {
				dataCount = (int) dataSqlOne.get("sort_num");
			}
			String properties_value_code = WebHelper.upCode("PV");
			MDataMap propertiesMap = new MDataMap();
			propertiesMap.put("properties_code", properties_code);
			propertiesMap.put("properties_value_code", properties_value_code);
			propertiesMap.put("properties_value", properties_value);
			propertiesMap.put("sort_num", dataCount+1+"");
			propertiesMap.put("create_time", upDateTime);
			DbUp.upTable("uc_properties_value").dataInsert(propertiesMap);
			
			return mWebResult;
		}
		
	}
