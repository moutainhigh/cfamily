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
 * 添加分类属性
 * @author lgx
 *
 */
public class FuncAddPropertiesKey extends RootFunc{
      
		public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
			MWebResult mWebResult = new MWebResult();
			
			String category_code = mDataMap.get("zw_f_category_code");//分类编号
			String properties_name = mDataMap.get("zw_f_properties_name").trim();
			String is_must = mDataMap.get("zw_f_is_must");
			String properties_value_type = mDataMap.get("zw_f_properties_value_type");
			
			Map<String, Object> sellercategory = DbUp.upTable("uc_sellercategory").dataSqlOne("SELECT * FROM uc_sellercategory WHERE category_code = '"+category_code+"' AND seller_code = 'SI2003'", new MDataMap());
			if(sellercategory == null) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("没有该分类!");
				return mWebResult;
			}
			if(StringUtils.isEmpty(properties_name)) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("请填写属性名称!");
				return mWebResult;
			}
			if(StringUtils.isEmpty(is_must)) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("请选择是否必填!");
				return mWebResult;
			}
			if(StringUtils.isEmpty(properties_value_type)) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("请选择属性值类型!");
				return mWebResult;
			}
			
			// 判断属性名称重复(上一级分类如果创建了属性名称，则下级的所有分类都不能再创建该属性名称。同一个分类下相同名称的属性只能创建一个。下级分类有重复名称没问题)
			String level = (String) sellercategory.get("level"); // 分类级别
			if("1".equals(level)) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("系统错误!");
				return mWebResult;
			}
			// 查看该分类下是否有重名属性
			String sql = "SELECT sp.zid FROM uc_sellercategory_properties sp LEFT JOIN uc_properties_key pk ON sp.properties_code = pk.properties_code " + 
					"WHERE sp.category_code = '"+category_code+"' AND pk.is_delete = '0' AND pk.properties_name = '"+properties_name+"'";
			List<Map<String, Object>> sqlList = DbUp.upTable("uc_sellercategory_properties").dataSqlList(sql, new MDataMap());
			if(null != sqlList && sqlList.size() > 0) {
				mWebResult.setResultCode(-1);
				mWebResult.setResultMessage("同一个分类下相同名称的属性只能创建一个!");
				return mWebResult;
			}
			// 下级分类创建的属性必须删除,上级分类才能创建
			if("3".equals(level) || "2".equals(level)) {
				// 如果是二级或一级分类,需要查看下级是否有同名属性
				String sql1 = "SELECT sp.category_code FROM uc_sellercategory_properties sp LEFT JOIN uc_properties_key pk ON sp.properties_code = pk.properties_code "
						+ " WHERE  pk.is_delete = '0' AND pk.properties_name = '"+properties_name+"' AND sp.category_code in "
						+ " (SELECT s.category_code FROM uc_sellercategory s WHERE s.parent_code = '"+category_code+"' AND s.seller_code = 'SI2003')";
				List<Map<String, Object>> sqlList1 = DbUp.upTable("uc_sellercategory_properties").dataSqlList(sql1, new MDataMap());
				if(null != sqlList1 && sqlList1.size() > 0) {
					// 重复属性分类名称
					String category = "";
					for (Map<String, Object> map : sqlList1) {
						String categoryCode = (String) map.get("category_code");
						Map<String, Object> uc_sellercategory = DbUp.upTable("uc_sellercategory").dataSqlOne("SELECT * FROM uc_sellercategory WHERE category_code = '"+categoryCode+"' AND seller_code = 'SI2003'", new MDataMap());
						if(null != uc_sellercategory) {							
							category += uc_sellercategory.get("category_name") + "；";
						}else {
							mWebResult.setResultCode(-1);
							mWebResult.setResultMessage("分类有误!");
							return mWebResult;
						}
					}
					mWebResult.setResultCode(-1);
					mWebResult.setResultMessage("下一级分类已经创建该属性名称,请删除再添加："+category);
					return mWebResult;
				}
				if("2".equals(level)) {
					// 如果是一级分类,还要查看下下级的三级分类是否有同名属性
					String sql12 = "SELECT sp.category_code FROM uc_sellercategory_properties sp LEFT JOIN uc_properties_key pk ON sp.properties_code = pk.properties_code " + 
							" WHERE  pk.is_delete = '0' AND pk.properties_name = '"+properties_name+"' AND sp.category_code in " + 
							" (SELECT us.category_code FROM uc_sellercategory us WHERE us.seller_code = 'SI2003' AND us.parent_code in " + 
							" (SELECT s.category_code FROM uc_sellercategory s WHERE s.parent_code = '"+category_code+"' AND s.seller_code = 'SI2003'))";
					List<Map<String, Object>> sqlList12 = DbUp.upTable("uc_sellercategory_properties").dataSqlList(sql12, new MDataMap());
					if(null != sqlList12 && sqlList12.size() > 0) {
						// 重复属性分类名称
						String category = "";
						for (Map<String, Object> map : sqlList12) {
							String categoryCode = (String) map.get("category_code");
							Map<String, Object> uc_sellercategory = DbUp.upTable("uc_sellercategory").dataSqlOne("SELECT * FROM uc_sellercategory WHERE category_code = '"+categoryCode+"' AND seller_code = 'SI2003'", new MDataMap());
							if(null != uc_sellercategory) {							
								category += uc_sellercategory.get("category_name") + "；";
							}else {
								mWebResult.setResultCode(-1);
								mWebResult.setResultMessage("分类有误!");
								return mWebResult;
							}
						}
						mWebResult.setResultCode(-1);
						mWebResult.setResultMessage("下下级分类已经创建该属性名称,请删除再添加："+category);
						return mWebResult;
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
					// 重复属性分类名称
					String category = "";
					Map<String, Object> uc_sellercategory = DbUp.upTable("uc_sellercategory").dataSqlOne("SELECT * FROM uc_sellercategory WHERE category_code = '"+parent_code+"' AND seller_code = 'SI2003'", new MDataMap());
					if(null != uc_sellercategory) {							
						category += uc_sellercategory.get("category_name") + "；";
					}else {
						mWebResult.setResultCode(-1);
						mWebResult.setResultMessage("分类有误!");
						return mWebResult;
					}
					mWebResult.setResultCode(-1);
					mWebResult.setResultMessage("上一级分类已经创建该属性名称："+category);
					return mWebResult;
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
							// 重复属性分类名称
							String category = "";
							Map<String, Object> uc_sellercategory = DbUp.upTable("uc_sellercategory").dataSqlOne("SELECT * FROM uc_sellercategory WHERE category_code = '"+firstCode+"' AND seller_code = 'SI2003'", new MDataMap());
							if(null != uc_sellercategory) {							
								category += uc_sellercategory.get("category_name") + "；";
							}else {
								mWebResult.setResultCode(-1);
								mWebResult.setResultMessage("分类有误!");
								return mWebResult;
							}
							mWebResult.setResultCode(-1);
							mWebResult.setResultMessage("上上级分类已经创建该属性名称："+category);
							return mWebResult;
						}
					}else {
						mWebResult.setResultCode(-1);
						mWebResult.setResultMessage("该分类的上上级分类不存在!");
						return mWebResult;
					}
				}
			}
			
			String upDateTime = FormatHelper.upDateTime();
			// 最后一条属性排序值
			Map<String, Object> dataSqlOne = DbUp.upTable("uc_sellercategory_properties").dataSqlOne("SELECT * FROM uc_sellercategory_properties WHERE category_code = '"+category_code+"' ORDER BY sort_num DESC LIMIT 1", new MDataMap());
			int dataCount = 0;
			if(null != dataSqlOne) {
				dataCount = (int) dataSqlOne.get("sort_num");
			}
			String properties_code = WebHelper.upCode("PK");
			MDataMap propertiesMap = new MDataMap();
			propertiesMap.put("category_code", category_code);
			propertiesMap.put("properties_code", properties_code);
			propertiesMap.put("sort_num", dataCount+1+"");
			propertiesMap.put("create_time", upDateTime);
			DbUp.upTable("uc_sellercategory_properties").dataInsert(propertiesMap);
			
			MDataMap keyMap = new MDataMap();
			keyMap.put("properties_code", properties_code);
			keyMap.put("properties_name", properties_name);
			keyMap.put("is_must", is_must);
			keyMap.put("properties_value_type", properties_value_type);
			keyMap.put("create_time", upDateTime);
			DbUp.upTable("uc_properties_key").dataInsert(keyMap);
			
			return mWebResult;
		}
		
	}
