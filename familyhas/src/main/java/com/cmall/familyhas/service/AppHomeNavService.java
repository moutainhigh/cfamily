package com.cmall.familyhas.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;

/**
 * @description: fh_apphome_nav表的相关操作 
 *
 * @author Yangcl
 * @date 2017年6月1日 下午3:04:17 
 * @version 1.0.0
 */
public class AppHomeNavService  extends BaseClass{

	/**
	 * @description:  将惠家有的导航复制一份到微信商城
	 * 
	 * 以下三条脚本顺序执行可以清空所有复制到微信商城的测试数据
	 * delete from familyhas.`fh_apphome_column_content` where column_code in (select column_code from familyhas.`fh_apphome_column` where nav_code in(select nav_code from familyhas.`fh_apphome_nav` where nav_type = '4497471600100002'))
	 * 
	 * delete from familyhas.`fh_apphome_column` where nav_code in(select nav_code from familyhas.`fh_apphome_nav` where nav_type = '4497471600100002')
	 * 
	 * delete from familyhas.`fh_apphome_nav` where nav_type = '4497471600100002' 
	 *
	 * @author Yangcl
	 * @date 2017年6月1日 下午3:05:45 
	 * @version 1.0.0.1
	 */
	public Map<String , String> copyAppHomeNavToWechat (String navCode) {
		Map<String , String> result = new HashMap<>();
		result.put("resultCode", "1");  // 复制成功。
		if(StringUtils.isBlank(navCode)){
			result.put("resultCode", "3");  //  复制失败。
			return result;
		}
		
		
		MDataMap one = DbUp.upTable("fh_apphome_nav").one("nav_code", navCode);
		if(one != null){
			String sql = "select  * from familyhas.fh_apphome_nav "
					+ "where " ;
			if(StringUtils.isBlank(one.get("nav_name"))){ 
				sql += " nav_icon = '" + one.get("nav_icon") + "' "; 
			}else{
				sql += " nav_name = '" + one.get("nav_name") + "' ";
			}
			sql += "and is_delete = '02' ";
			sql += "and show_type = '4497480100010002' ";
			List<Map<String, Object>> list = DbUp.upTable("fh_apphome_nav").dataSqlList(sql , null); 
			if(list != null && list.size() > 0){
				result.put("resultCode", "2");  // 导航已经复制过，请核实。
				return result;
			}else { // 没有复制到微信商城过，则开始复制
				String nowTime = DateUtil.getNowTime();
				/* 获取当前登录人 */
				String createUser = UserFactory.INSTANCE.create().getLoginName();
				String newNavCode = WebHelper.upCode("NAV");  
				one.remove("zid");
				one.put("uid", UUID.randomUUID().toString().replace("-", "")); 
				one.put("nav_code" , newNavCode);
				one.put("show_type", "4497480100010002");  // 展示类型 ：微信客户端  4497480100010002
				one.put("release_flag", "02");  // 是否发布 01:是 02:否
				one.put("update_time", nowTime);
				one.put("update_user", createUser+"【复制】");
				one.put("nav_type", "4497471600100002"); // wap 或 app端（app:01;wap:02;pc:03）
				DbUp.upTable("fh_apphome_nav").dataInsert(one);                            // 导航栏插入完成
				
				// 开始插入fh_apphome_column的数据 
				sql = "select  * from familyhas.fh_apphome_column "
						+ "where " 
						+ " nav_code = '" + navCode + "'  and is_delete='449746250002'";  
				List<Map<String, Object>> clist = DbUp.upTable("fh_apphome_column").dataSqlList(sql, null);
				if(clist != null && clist.size() != 0){
					for(Map<String, Object> m : clist){
						String oldColumCode = m.get("column_code").toString();  // 保存原有的column_code
						
						MDataMap d = new MDataMap();
						d.put("uid" , UUID.randomUUID().toString().replace("-", ""));  
						d.put("nav_code", newNavCode); 
						d.put("column_code", WebHelper.upCode("COL")); 
						d.put("column_name", StringUtils.isBlank(m.get("column_name").toString()) ? "" : m.get("column_name").toString());
						d.put("show_name", StringUtils.isBlank(m.get("show_name").toString()) ? "" : m.get("show_name").toString());
						d.put("column_type", StringUtils.isBlank(m.get("column_type").toString()) ? "" : m.get("column_type").toString());
						d.put("start_time", StringUtils.isBlank(m.get("start_time").toString()) ? "" : m.get("start_time").toString());
						
						d.put("end_time", StringUtils.isBlank(m.get("end_time").toString()) ? "" : m.get("end_time").toString());
						d.put("position", StringUtils.isBlank(m.get("position").toString()) ? "" : m.get("position").toString());
						d.put("is_showmore", StringUtils.isBlank(m.get("is_showmore").toString()) ? "" : m.get("is_showmore").toString());
						d.put("showmore_title", StringUtils.isBlank(m.get("showmore_title").toString()) ? "" : m.get("showmore_title").toString());
						d.put("showmore_linktype", StringUtils.isBlank(m.get("showmore_linktype").toString()) ? "" : m.get("showmore_linktype").toString());
						
						d.put("showmore_linkvalue", StringUtils.isBlank(m.get("showmore_linkvalue").toString()) ? "" : m.get("showmore_linkvalue").toString());
						d.put("pic_url", StringUtils.isBlank(m.get("pic_url").toString()) ? "" : m.get("pic_url").toString());
						d.put("seller_code", StringUtils.isBlank(m.get("seller_code").toString()) ? "" : m.get("seller_code").toString());
						d.put("view_type", "4497471600100002");  // wap或app端（app:01;wap:02;pc:03）
						d.put("notice_type", StringUtils.isBlank(m.get("notice_type").toString()) ? "" : m.get("notice_type").toString());
						
						d.put("interval_second", StringUtils.isBlank(m.get("interval_second").toString()) ? "" : m.get("interval_second").toString());
						d.put("column_bgpic", StringUtils.isBlank(m.get("column_bgpic").toString()) ? "" : m.get("column_bgpic").toString());
						d.put("is_delete", StringUtils.isBlank(m.get("is_delete").toString()) ? "" : m.get("is_delete").toString());
						d.put("create_time", StringUtils.isBlank(m.get("create_time").toString()) ? "" : m.get("create_time").toString());
						d.put("create_user", StringUtils.isBlank(m.get("create_user").toString()) ? "" : m.get("create_user").toString());
						
						d.put("update_time", StringUtils.isBlank(m.get("update_time").toString()) ? "" : nowTime); 
						d.put("update_user", StringUtils.isBlank(m.get("update_user").toString()) ? "" : createUser+"【复制】");
						d.put("release_flag", "449746250002");
						d.put("is_had_edge_distance", StringUtils.isBlank(m.get("is_had_edge_distance").toString()) ? "" : m.get("is_had_edge_distance").toString());
						d.put("columns_per_row", StringUtils.isBlank(m.get("columns_per_row").toString()) ? "" : m.get("columns_per_row").toString());
						d.put("future_program", StringUtils.isBlank(m.get("future_program").toString()) ? "" : m.get("future_program").toString());
						d.put("num_languanggao", StringUtils.isBlank(m.get("num_languanggao").toString()) ? "" : m.get("num_languanggao").toString());
						
						DbUp.upTable("fh_apphome_column").dataInsert(d);
						
						// 开始插入fh_apphome_column_content的数据 
						String ccsql = "select  * from familyhas.fh_apphome_column_content "
								+ "where " 
								+ "column_code = '" + oldColumCode + "' and is_delete = '449746250002' ";
						List<Map<String, Object>> cclist = DbUp.upTable("fh_apphome_column_content").dataSqlList(ccsql , null);
						if(cclist != null && cclist.size() != 0){ 
							for(Map<String, Object> e : cclist){
								MDataMap cc = new MDataMap();
								cc.put("uid" , UUID.randomUUID().toString().replace("-", ""));
								
								cc.put("column_code", d.get("column_code")); 
								cc.put("picture", StringUtils.isBlank(e.get("picture").toString()) ? "" : e.get("picture").toString());
								cc.put("start_time", StringUtils.isBlank(e.get("start_time").toString()) ? "" : e.get("start_time").toString());
								cc.put("end_time", StringUtils.isBlank(e.get("end_time").toString()) ? "" : e.get("end_time").toString());
								cc.put("position", StringUtils.isBlank(e.get("position").toString()) ? "" : e.get("position").toString());
								
								cc.put("title", StringUtils.isBlank(e.get("title").toString()) ? "" : e.get("title").toString());
								cc.put("title_color", StringUtils.isBlank(e.get("title_color").toString()) ? "" : e.get("title_color").toString());
								cc.put("description", StringUtils.isBlank(e.get("description").toString()) ? "" : e.get("description").toString());
								cc.put("description_color", StringUtils.isBlank(e.get("description_color").toString()) ? "" : e.get("description_color").toString());
								cc.put("showmore_linktype", StringUtils.isBlank(e.get("showmore_linktype").toString()) ? "" : e.get("showmore_linktype").toString());
								
								cc.put("showmore_linkvalue", StringUtils.isBlank(e.get("showmore_linkvalue").toString()) ? "" : e.get("showmore_linkvalue").toString());
								cc.put("is_share", StringUtils.isBlank(e.get("is_share").toString()) ? "" : e.get("is_share").toString());
								cc.put("is_delete", StringUtils.isBlank(e.get("is_delete").toString()) ? "" : e.get("is_delete").toString());
								cc.put("create_time", StringUtils.isBlank(e.get("create_time").toString()) ? "" : e.get("create_time").toString());
								cc.put("create_user", StringUtils.isBlank(e.get("create_user").toString()) ? "" : e.get("create_user").toString());
								
								cc.put("update_time", StringUtils.isBlank(e.get("update_time").toString()) ? "" : nowTime);  
								cc.put("update_user", StringUtils.isBlank(e.get("update_user").toString()) ? "" : createUser + "【复制】");  
								cc.put("delete_time", StringUtils.isBlank(e.get("delete_time").toString()) ? "" : e.get("delete_time").toString());
								cc.put("delete_user", StringUtils.isBlank(e.get("delete_user").toString()) ? "" : e.get("delete_user").toString());
								cc.put("share_pic", StringUtils.isBlank(e.get("share_pic").toString()) ? "" : e.get("share_pic").toString());
								
								cc.put("share_title", StringUtils.isBlank(e.get("share_title").toString()) ? "" : e.get("share_title").toString());
								cc.put("share_content", StringUtils.isBlank(e.get("share_content").toString()) ? "" : e.get("share_content").toString());
								cc.put("share_link", StringUtils.isBlank(e.get("share_link").toString()) ? "" : e.get("share_link").toString());
								cc.put("skip_place", StringUtils.isBlank(e.get("skip_place").toString()) ? "" : e.get("skip_place").toString());
								cc.put("place_time", StringUtils.isBlank(e.get("place_time").toString()) ? "" : e.get("place_time").toString());
								
								cc.put("floor_model", StringUtils.isBlank(e.get("floor_model").toString()) ? "" : e.get("floor_model").toString());
								cc.put("product_name", StringUtils.isBlank(e.get("product_name").toString()) ? "" : e.get("product_name").toString());
								cc.put("product_price", StringUtils.isBlank(e.get("product_price").toString()) ? "" : e.get("product_price").toString());
								cc.put("product_code", StringUtils.isBlank(e.get("product_code").toString()) ? "" : e.get("product_code").toString());
								
								cc.put("video_ad", StringUtils.isBlank(e.get("video_ad").toString()) ? "" : e.get("video_ad").toString());
								cc.put("video_link", StringUtils.isBlank(e.get("video_link").toString()) ? "" : e.get("video_link").toString());
								
								DbUp.upTable("fh_apphome_column_content").dataInsert(cc);
							}
						}
						
					}
				}
			}
		}
		
		
		
		
		
		return result;
	}
}




































