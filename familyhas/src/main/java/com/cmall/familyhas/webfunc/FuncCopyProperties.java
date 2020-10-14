package com.cmall.familyhas.webfunc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basehelper.FormatHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 复制属性
 */
public class FuncCopyProperties extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		// 所选属性
		String checkProperties = mDataMap.get("zw_f_checkProperties");
		// 所选分类
		String checkCategory = mDataMap.get("zw_f_checkCategory");
		
		if (StringUtils.isBlank(checkProperties)) {
			mResult.setResultCode(99);
			mResult.setResultMessage("所选属性不能为空");
			return mResult;
		}
		if (StringUtils.isBlank(checkCategory)) {
			mResult.setResultCode(99);
			mResult.setResultMessage("所选分类不能为空");
			return mResult;
		}
		
		String[] properties = checkProperties.split(",");
		String[] category = checkCategory.split(",");
		
		// 分类属性重复提示
		StringBuffer wrongSb = new StringBuffer();
		// 循环分类
		for (String categoryCode : category) {
			// 查询分类
			MDataMap sellercategory = DbUp.upTable("uc_sellercategory").one("seller_code","SI2003","category_code",categoryCode);
			if(null == sellercategory) {
				mResult.setResultCode(-1);
				mResult.setResultMessage("所选分类有误");
				return mResult;
			}
			String category_name = sellercategory.get("category_name");
			// 属性重复无法创建提示语
			StringBuffer wrongPro = new StringBuffer();
			// 循环属性
			for (String propertiesCode : properties) {
				// 查询属性
				MDataMap properties_key = DbUp.upTable("uc_properties_key").one("is_delete","0","properties_code",propertiesCode);
				if(null == properties_key) {
					mResult.setResultCode(-1);
					mResult.setResultMessage("所选属性有误");
					return mResult;
				}
				String properties_name = properties_key.get("properties_name");
				// 验证该分类下是否可以创建该属性,不能创建
				boolean flag = checkSellercategoryProperties(properties_key, sellercategory);
				
				if(flag) {
					// 可以创建属性,直接添加
					String upDateTime = FormatHelper.upDateTime();
					// 最后一条属性排序值
					String sql = "SELECT * FROM uc_sellercategory_properties WHERE category_code = '"+categoryCode+"' ORDER BY sort_num DESC LIMIT 1";
					//Map<String, Object> dataSqlOne = DbUp.upTable("uc_sellercategory_properties").dataSqlOne("SELECT * FROM uc_sellercategory_properties WHERE category_code = '"+category_code+"' ORDER BY sort_num DESC LIMIT 1", new MDataMap());
					List<Map<String, Object>> proList = DbUp.upTable("uc_sellercategory_properties").upTemplate().queryForList(sql, new HashMap<String, String>());
					int dataCount = 0;
					if(null != proList && proList.size() > 0) {
						dataCount = (int) proList.get(0).get("sort_num");
					}
					
					MDataMap propertiesMap = new MDataMap();
					String properties_code = WebHelper.upCode("PK");
					propertiesMap.put("category_code", categoryCode);
					propertiesMap.put("properties_code", properties_code);
					propertiesMap.put("sort_num", dataCount+1+"");
					propertiesMap.put("create_time", upDateTime);
					DbUp.upTable("uc_sellercategory_properties").dataInsert(propertiesMap);
					
					MDataMap keyMap = new MDataMap();
					keyMap.put("properties_code", properties_code);
					keyMap.put("properties_name", properties_name);
					keyMap.put("is_must", properties_key.get("is_must"));
					keyMap.put("properties_value_type", properties_key.get("properties_value_type"));
					keyMap.put("create_time", upDateTime);
					DbUp.upTable("uc_properties_key").dataInsert(keyMap);
					
					if("449748500001".equals(properties_key.get("properties_value_type"))) {
						// 属性类型是固定值,添加属性值
						String sSql = "SELECT * FROM uc_properties_value WHERE properties_code = '"+propertiesCode+"' AND is_delete = '0' ORDER BY sort_num ASC";
						List<Map<String, Object>> dataSqlList = DbUp.upTable("uc_sellercategory_properties").dataSqlList(sSql, new MDataMap());
						if(null != dataSqlList && dataSqlList.size() > 0) {
							for (Map<String, Object> map : dataSqlList) {							
								String properties_value_code = WebHelper.upCode("PV");
								MDataMap valueMap = new MDataMap();
								valueMap.put("properties_code", properties_code);
								valueMap.put("properties_value_code", properties_value_code);
								valueMap.put("properties_value", (String) map.get("properties_value"));
								valueMap.put("sort_num", MapUtils.getString(map, "sort_num"));
								valueMap.put("create_time", upDateTime);
								DbUp.upTable("uc_properties_value").dataInsert(valueMap);
							}
						}
					}
					
				}else {
					// 不可以创建属性
					if(wrongPro.length() == 0) {
						wrongPro.append(category_name+"的上级或下级分类已经创建同名属性:");
					}
					wrongPro.append(properties_name+",");
				}
				
			}
			if(wrongPro.length() != 0) {
				wrongSb.append(wrongPro.toString().trim().substring(0,wrongPro.toString().trim().length()-1)+";");
			}
		}
		
		if(wrongSb.length() == 0) {
			wrongSb.append("复制成功");
		}else {
			wrongSb.append("其他分类属性复制成功");
		}
		mResult.setResultMessage(wrongSb.toString());
		
		return mResult;
	}

	private boolean checkSellercategoryProperties(MDataMap properties_key, MDataMap sellercategory) {
		String properties_name = properties_key.get("properties_name");
		String level = sellercategory.get("level");
		String category_code = sellercategory.get("category_code");
		// 查看该分类下是否有重名属性
		String sql = "SELECT sp.zid FROM uc_sellercategory_properties sp LEFT JOIN uc_properties_key pk ON sp.properties_code = pk.properties_code " + 
				"WHERE sp.category_code = '"+category_code+"' AND pk.is_delete = '0' AND pk.properties_name = '"+properties_name+"'";
		List<Map<String, Object>> sqlList = DbUp.upTable("uc_sellercategory_properties").dataSqlList(sql, new MDataMap());
		if(null != sqlList && sqlList.size() > 0) {
			return false;
		}
		// 下级分类创建的属性必须删除,上级分类才能创建
		if("3".equals(level) || "2".equals(level)) {
			// 如果是二级或一级分类,需要查看下级是否有同名属性
			String sql1 = "SELECT sp.category_code FROM uc_sellercategory_properties sp LEFT JOIN uc_properties_key pk ON sp.properties_code = pk.properties_code "
					+ " WHERE  pk.is_delete = '0' AND pk.properties_name = '"+properties_name+"' AND sp.category_code in "
					+ " (SELECT s.category_code FROM uc_sellercategory s WHERE s.parent_code = '"+category_code+"' AND s.seller_code = 'SI2003')";
			List<Map<String, Object>> sqlList1 = DbUp.upTable("uc_sellercategory_properties").dataSqlList(sql1, new MDataMap());
			if(null != sqlList1 && sqlList1.size() > 0) {
				return false;
			}
			if("2".equals(level)) {
				// 如果是一级分类,还要查看下下级的三级分类是否有同名属性
				String sql12 = "SELECT sp.category_code FROM uc_sellercategory_properties sp LEFT JOIN uc_properties_key pk ON sp.properties_code = pk.properties_code " + 
						" WHERE  pk.is_delete = '0' AND pk.properties_name = '"+properties_name+"' AND sp.category_code in " + 
						" (SELECT us.category_code FROM uc_sellercategory us WHERE us.seller_code = 'SI2003' AND us.parent_code in " + 
						" (SELECT s.category_code FROM uc_sellercategory s WHERE s.parent_code = '"+category_code+"' AND s.seller_code = 'SI2003'))";
				List<Map<String, Object>> sqlList12 = DbUp.upTable("uc_sellercategory_properties").dataSqlList(sql12, new MDataMap());
				if(null != sqlList12 && sqlList12.size() > 0) {
					return false;
				}
			}
		}
		String parent_code = (String) sellercategory.get("parent_code"); // 父分类编码
		if("3".equals(level) || "4".equals(level)) {
			// 二级分类或者三级分类,查看上级分类是否有重名属性
			String sql2 = "SELECT sp.zid FROM uc_sellercategory_properties sp LEFT JOIN uc_properties_key pk ON sp.properties_code = pk.properties_code " + 
					"WHERE sp.category_code = '"+parent_code+"' AND pk.is_delete = '0' AND pk.properties_name = '"+properties_name+"'";
			List<Map<String, Object>> sqlList2 = DbUp.upTable("uc_sellercategory_properties").dataSqlList(sql2, new MDataMap());
			if(null != sqlList2 && sqlList2.size() > 0) {
				return false;
			}
			if("4".equals(level)) {
				// 三级分类,还要查看一级分类是否有重名属性
				Map<String, Object> parentCategory = DbUp.upTable("uc_sellercategory").dataSqlOne("SELECT * FROM uc_sellercategory WHERE category_code = '"+parent_code+"' AND seller_code = 'SI2003'", new MDataMap());
				if(null != parentCategory) {
					String firstCode = (String) parentCategory.get("parent_code"); // 一级分类编码
					String sql3 = "SELECT sp.zid FROM uc_sellercategory_properties sp LEFT JOIN uc_properties_key pk ON sp.properties_code = pk.properties_code " + 
							"WHERE sp.category_code = '"+firstCode+"' AND pk.is_delete = '0' AND pk.properties_name = '"+properties_name+"'";
					List<Map<String, Object>> sqlList3 = DbUp.upTable("uc_sellercategory_properties").dataSqlList(sql3, new MDataMap());
					if(null != sqlList3 && sqlList3.size() > 0) {
						return false;
					}
				}else {
					return false;
				}
			}
		}
		return true;
	}
}
