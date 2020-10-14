package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * @description: 版式维护列表中的数据通过这个类，复制到其他【首页导航】中
 *
 * @author Yangcl
 * @date 2017年5月16日 下午6:04:24
 * @version 1.0.0
 */
public class ColumnCopyToOtherNav extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap map) {
		MWebResult result = new MWebResult();
		String columnCode = map.get("columnCode");
		String ncs = map.get("ncs"); // nav_code_list

		String sql = "select  * from familyhas.fh_apphome_column " + "where " + " column_code = '" + columnCode + "' ";
		List<Map<String, Object>> list = DbUp.upTable("fh_apphome_column").dataSqlList(sql, null);
		if (list == null || list.size() == 0) {
			result.setResultCode(941901133);
			result.setResultMessage("column code:" + columnCode + "查询结果为空，请重试!");
			return result;
		}

		Map<String, Object> m = list.get(0); // 只有一条数据
		String[] arr = ncs.split(",");
		int k = 0;
		String name = "";
		for (int i = 0; i < arr.length; i++) {
			MDataMap d = new MDataMap();
			d.put("uid", UUID.randomUUID().toString().replace("-", ""));
			d.put("nav_code", arr[i]);
			String oldColumCode = m.get("column_code").toString(); // 保存原有的column_code

			d.put("column_code", WebHelper.upCode("COL"));
			d.put("column_name",
					StringUtils.isBlank(m.get("column_name").toString()) ? "" : m.get("column_name").toString());
			d.put("show_name", StringUtils.isBlank(m.get("show_name").toString()) ? "" : m.get("show_name").toString());
			d.put("column_type",
					StringUtils.isBlank(m.get("column_type").toString()) ? "" : m.get("column_type").toString());
			d.put("start_time",
					StringUtils.isBlank(m.get("start_time").toString()) ? "" : m.get("start_time").toString());
			d.put("num_languanggao", StringUtils.isBlank(m.get("num_languanggao").toString()) ? ""
					: m.get("num_languanggao").toString());

			sql = "select  * from familyhas.fh_apphome_column " + "where " + " nav_code = '" + arr[i] + "' "
					+ " and column_name = '" + d.get("column_name") + "'  and is_delete='449746250002'";
			List<Map<String, Object>> list_ = DbUp.upTable("fh_apphome_column").dataSqlList(sql, null);
			if (list_ != null && list_.size() != 0) { // 复制时，导航名称不允许重复
				k++;
				MDataMap mapFather = DbUp.upTable("fh_apphome_nav").one("nav_code", arr[i]);
				if (k != 1) {
					name += ("," + mapFather.get("nav_name"));
				} else {
					name += mapFather.get("nav_name");
				}
				continue;
			}

			d.put("end_time", StringUtils.isBlank(m.get("end_time").toString()) ? "" : m.get("end_time").toString());
			d.put("position", StringUtils.isBlank(m.get("position").toString()) ? "" : m.get("position").toString());
			d.put("is_showmore",
					StringUtils.isBlank(m.get("is_showmore").toString()) ? "" : m.get("is_showmore").toString());
			d.put("showmore_title",
					StringUtils.isBlank(m.get("showmore_title").toString()) ? "" : m.get("showmore_title").toString());
			d.put("showmore_linktype", StringUtils.isBlank(m.get("showmore_linktype").toString()) ? ""
					: m.get("showmore_linktype").toString());

			d.put("showmore_linkvalue", StringUtils.isBlank(m.get("showmore_linkvalue").toString()) ? ""
					: m.get("showmore_linkvalue").toString());
			d.put("pic_url", StringUtils.isBlank(m.get("pic_url").toString()) ? "" : m.get("pic_url").toString());
			d.put("seller_code",
					StringUtils.isBlank(m.get("seller_code").toString()) ? "" : m.get("seller_code").toString());
			d.put("view_type", StringUtils.isBlank(m.get("view_type").toString()) ? "" : m.get("view_type").toString());
			d.put("notice_type",
					StringUtils.isBlank(m.get("notice_type").toString()) ? "" : m.get("notice_type").toString());

			d.put("interval_second", StringUtils.isBlank(m.get("interval_second").toString()) ? ""
					: m.get("interval_second").toString());
			d.put("column_bgpic",
					StringUtils.isBlank(m.get("column_bgpic").toString()) ? "" : m.get("column_bgpic").toString());
			d.put("is_delete", StringUtils.isBlank(m.get("is_delete").toString()) ? "" : m.get("is_delete").toString());
			d.put("create_time",
					StringUtils.isBlank(m.get("create_time").toString()) ? "" : m.get("create_time").toString());
			d.put("create_user",
					StringUtils.isBlank(m.get("create_user").toString()) ? "" : m.get("create_user").toString());

			d.put("update_time",
					StringUtils.isBlank(m.get("update_time").toString()) ? "" : m.get("update_time").toString());
			d.put("update_user", StringUtils.isBlank(m.get("update_user").toString()) ? ""
					: m.get("update_user").toString() + "【复制】");
			d.put("release_flag", "449746250002");
			d.put("is_had_edge_distance", StringUtils.isBlank(m.get("is_had_edge_distance").toString()) ? ""
					: m.get("is_had_edge_distance").toString());
			d.put("columns_per_row", StringUtils.isBlank(m.get("columns_per_row").toString()) ? ""
					: m.get("columns_per_row").toString());

			d.put("event_code",
					StringUtils.isBlank(m.get("event_code").toString()) ? "" : m.get("event_code").toString());
			d.put("show_num", StringUtils.isBlank(m.get("show_num").toString()) ? "" : m.get("show_num").toString());

			d.put("prod_recommend", StringUtils.isBlank(m.get("prod_recommend").toString()) ? "" : m.get("prod_recommend").toString());
			d.put("category_limit", StringUtils.isBlank(m.get("category_limit").toString()) ? "" : m.get("category_limit").toString());
			d.put("category_codes", StringUtils.isBlank(m.get("category_codes").toString()) ? "" : m.get("category_codes").toString());
			
			DbUp.upTable("fh_apphome_column").dataInsert(d);

			// 开始复制 fh_apphome_column 下的内容，即fh_apphome_column_content表中的数据 oldColumCode
			String ccsql = "select  * from familyhas.fh_apphome_column_content " + "where " + "column_code = '"
					+ oldColumCode + "' ";
			List<Map<String, Object>> cclist = DbUp.upTable("fh_apphome_column_content").dataSqlList(ccsql, null);
			if (cclist == null || cclist.size() == 0) {
				continue;
			}
			for (int c = 0; c < cclist.size(); c++) {
				Map<String, Object> e = cclist.get(c);

				MDataMap cc = new MDataMap();
				cc.put("uid", UUID.randomUUID().toString().replace("-", ""));

				cc.put("column_code", d.get("column_code"));
				cc.put("picture", StringUtils.isBlank(e.get("picture").toString()) ? "" : e.get("picture").toString());
				cc.put("start_time",
						StringUtils.isBlank(e.get("start_time").toString()) ? "" : e.get("start_time").toString());
				cc.put("end_time",
						StringUtils.isBlank(e.get("end_time").toString()) ? "" : e.get("end_time").toString());
				cc.put("position",
						StringUtils.isBlank(e.get("position").toString()) ? "" : e.get("position").toString());

				cc.put("title", StringUtils.isBlank(e.get("title").toString()) ? "" : e.get("title").toString());
				cc.put("title_color",
						StringUtils.isBlank(e.get("title_color").toString()) ? "" : e.get("title_color").toString());
				cc.put("description",
						StringUtils.isBlank(e.get("description").toString()) ? "" : e.get("description").toString());
				cc.put("description_color", StringUtils.isBlank(e.get("description_color").toString()) ? ""
						: e.get("description_color").toString());
				cc.put("showmore_linktype", StringUtils.isBlank(e.get("showmore_linktype").toString()) ? ""
						: e.get("showmore_linktype").toString());

				cc.put("showmore_linkvalue", StringUtils.isBlank(e.get("showmore_linkvalue").toString()) ? ""
						: e.get("showmore_linkvalue").toString());
				cc.put("is_share",
						StringUtils.isBlank(e.get("is_share").toString()) ? "" : e.get("is_share").toString());
				cc.put("is_delete",
						StringUtils.isBlank(e.get("is_delete").toString()) ? "" : e.get("is_delete").toString());
				cc.put("create_time",
						StringUtils.isBlank(e.get("create_time").toString()) ? "" : e.get("create_time").toString());
				cc.put("create_user",
						StringUtils.isBlank(e.get("create_user").toString()) ? "" : e.get("create_user").toString());

				cc.put("update_time",
						StringUtils.isBlank(e.get("update_time").toString()) ? "" : e.get("update_time").toString());
				cc.put("update_user", StringUtils.isBlank(e.get("update_user").toString()) ? ""
						: e.get("update_user").toString() + "【复制】");
				cc.put("delete_time",
						StringUtils.isBlank(e.get("delete_time").toString()) ? "" : e.get("delete_time").toString());
				cc.put("delete_user",
						StringUtils.isBlank(e.get("delete_user").toString()) ? "" : e.get("delete_user").toString());
				cc.put("share_pic",
						StringUtils.isBlank(e.get("share_pic").toString()) ? "" : e.get("share_pic").toString());

				cc.put("share_title",
						StringUtils.isBlank(e.get("share_title").toString()) ? "" : e.get("share_title").toString());
				cc.put("share_content", StringUtils.isBlank(e.get("share_content").toString()) ? ""
						: e.get("share_content").toString());
				cc.put("share_link",
						StringUtils.isBlank(e.get("share_link").toString()) ? "" : e.get("share_link").toString());
				cc.put("skip_place",
						StringUtils.isBlank(e.get("skip_place").toString()) ? "" : e.get("skip_place").toString());
				cc.put("place_time",
						StringUtils.isBlank(e.get("place_time").toString()) ? "" : e.get("place_time").toString());

				cc.put("floor_model",
						StringUtils.isBlank(e.get("floor_model").toString()) ? "" : e.get("floor_model").toString());
				cc.put("product_name",
						StringUtils.isBlank(e.get("product_name").toString()) ? "" : e.get("product_name").toString());
				cc.put("product_price", StringUtils.isBlank(e.get("product_price").toString()) ? ""
						: e.get("product_price").toString());
				cc.put("product_code",
						StringUtils.isBlank(e.get("product_code").toString()) ? "" : e.get("product_code").toString());
				cc.put("video_ad",
						StringUtils.isBlank(e.get("video_ad").toString()) ? "" : e.get("video_ad").toString());
				cc.put("video_link",
						StringUtils.isBlank(e.get("video_link").toString()) ? "" : e.get("video_link").toString());

				DbUp.upTable("fh_apphome_column_content").dataInsert(cc);
			}

		}

		if (k == arr.length) {
			result.setResultCode(100000);
			result.setResultMessage("该导航下已存在相同栏目！！！");
		} else if (k != 0) {
			result.setResultCode(100000);
			result.setResultMessage("导航:" + name + "下已存在相同栏目！！！");
		}
		return result;

	}

}
