package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiHomeAddEvaluationProductInput;
import com.cmall.familyhas.api.result.ApiHomeAddEvaluationProductResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 首页商品评论模板维护添加商品
 * @author lgx
 *
 */
public class ApiHomeAddEvaluationProduct extends RootApi<ApiHomeAddEvaluationProductResult, ApiHomeAddEvaluationProductInput>{
	
	public ApiHomeAddEvaluationProductResult Process(ApiHomeAddEvaluationProductInput input,MDataMap mDataMap){
		ApiHomeAddEvaluationProductResult result = new ApiHomeAddEvaluationProductResult();
		
		// 按条件查询  审核通过的好评加晒单评价>5,状态为已上架,库存>0的  商品,按最新评论时间倒序取前100条数据商品
		String sql = "SELECT  " + 
				"	z.product_code,  " + 
				"	(SELECT noe.uid FROM newscenter.nc_order_evaluation noe WHERE noe.product_code = z.product_code " + 
				"	AND noe.flag_show = '449746530001' " + 
				"	AND noe.check_flag = '4497172100030002' " + 
				"	AND noe.pic_status = '449747510001' " + 
				"	AND noe.grade >= 4 " + 
				"	AND (noe.oder_photos != '' OR (noe.ccvids != '' AND noe.ccpics != '')) " + 
				"	ORDER BY noe.oder_creattime DESC LIMIT 1) uid " + 
				"FROM ( " + 
				"	SELECT " + 
				"		n.product_code " + 
				"	FROM " + 
				"		newscenter.nc_order_evaluation n  " + 
				"	LEFT JOIN ( " + 
				"		SELECT pp.product_code, sum(sss.stock_num) num FROM productcenter.pc_productinfo pp  " + 
				"		LEFT JOIN productcenter.pc_skuinfo ps ON pp.product_code = ps.product_code " + 
				"		LEFT JOIN systemcenter.sc_store_skunum sss ON sss.sku_code = ps.sku_code " + 
				"		WHERE pp.product_status = '4497153900060002' AND pp.seller_code = 'SI2003' " + 
				"		GROUP BY pp.product_code " + 
				"	) ppp ON n.product_code = ppp.product_code " + 
				"	WHERE " + 
				"		n.flag_show = '449746530001' " + 
				"	AND n.check_flag = '4497172100030002' " + 
				"	AND n.pic_status = '449747510001' " + 
				"	AND n.grade >= 4 " + 
				"	AND (n.oder_photos != '' OR (n.ccvids != '' AND n.ccpics != '')) " + 
				"	AND ppp.num != 0  " + 
				"	GROUP BY n.product_code " + 
				"	HAVING count(n.product_code) > 5 " + 
				"	LIMIT 100 " + 
				") z";
		List<Map<String, Object>> prodList = DbUp.upTable("nc_order_evaluation").dataSqlList(sql, new MDataMap());
		// 清空表中全部序号
		DbUp.upTable("fh_apphome_evaluation").dataUpdate(new MDataMap("location_num","0"), "location_num", "");
		// 按顺序编辑1-100序号循环,查询表中是否有该商品;
		for (int i = 0; i < prodList.size(); i++) {
			// 商品编号
			String product_code = prodList.get(i).get("product_code")+"";
			// 商品对应最新评论uid
			String evaluation_uid = prodList.get(i).get("uid")+"";
			String sql1 = "SELECT * FROM fh_apphome_evaluation WHERE product_code = '"+product_code+"'";
			Map<String, Object> evaProd = DbUp.upTable("fh_apphome_evaluation").dataSqlOne(sql1, new MDataMap());
			if(evaProd != null && evaProd.size() > 0) { // 如果有该商品
				// 先看是否主动删除,删除过的不更新
				String is_delete = evaProd.get("is_delete")+"";
				if("1".equals(is_delete)) {
					continue;
				}
				//看是否修改过
				String is_edit = evaProd.get("is_edit")+"";
				if("1".equals(is_edit)) {
					// 修改过直接改  序号,其他(评论uid,评价内容和第一张晒图)不改
					MDataMap mDataMap2 = new MDataMap();
					mDataMap2.put("product_code", product_code);
					mDataMap2.put("location_num", i+1+"");
					DbUp.upTable("fh_apphome_evaluation").dataUpdate(mDataMap2, "location_num", "product_code");
				}else {
					// 没修过过,更新  序号,评论uid,评价内容和第一张晒图
					String sql2 = "SELECT * FROM nc_order_evaluation WHERE uid = '"+evaluation_uid+"'";
					Map<String, Object> evaMap = DbUp.upTable("nc_order_evaluation").dataSqlOne(sql2, new MDataMap());
					String ccvids = (String) evaMap.get("ccvids");
					String ccpics = (String) evaMap.get("ccpics");
					String imgStr = evaMap.get("oder_photos")+"";
					String[] imgs = imgStr.split("\\|");
					String cover_img = "";
					if(imgs.length > 0) {
						cover_img = imgs[0];
					}else {
						if(!"".equals(ccvids) && !"".equals(ccpics)) {
							cover_img = ccpics;
						}
					}
					MDataMap mDataMap3 = new MDataMap();
					mDataMap3.put("product_code", product_code);
					mDataMap3.put("location_num", i+1+"");
					mDataMap3.put("evaluation_uid", evaluation_uid);
					mDataMap3.put("order_assessment", evaMap.get("order_assessment")+"");
					mDataMap3.put("cover_img", cover_img);
					DbUp.upTable("fh_apphome_evaluation").dataUpdate(mDataMap3, "location_num", "product_code");
				}
			}else { // 如果没有该商品
				// 直接添加(序号,商品id,评论uid,评价内容和第一张晒图)
				String sql3 = "SELECT * FROM nc_order_evaluation WHERE uid = '"+evaluation_uid+"'";
				Map<String, Object> evaMap1 = DbUp.upTable("nc_order_evaluation").dataSqlOne(sql3, new MDataMap());
				String ccvids = (String) evaMap1.get("ccvids");
				String ccpics = (String) evaMap1.get("ccpics");
				String imgStr = evaMap1.get("oder_photos")+"";
				String[] imgs = imgStr.split("\\|");
				String cover_img = "";
				if(imgs.length > 0) {
					cover_img = imgs[0];
				}else {
					if(!"".equals(ccvids) && !"".equals(ccpics)) {
						cover_img = ccpics;
					}
				}
				// 记录商品分类
				String sql4 = "SELECT s.* FROM uc_sellercategory s WHERE s.seller_code = 'SI2003' AND s.category_code = " + 
						"(SELECT r.category_code FROM uc_sellercategory_product_relation r WHERE r.product_code = '"+product_code+"' AND r.seller_code = 'SI2003' LIMIT 1)";
				Map<String, Object> tcategoryMap = DbUp.upTable("uc_sellercategory").dataSqlOne(sql4, new MDataMap());
				Map<String, Object> fcategoryMap = null;
				if(tcategoryMap != null) {					
					fcategoryMap = DbUp.upTable("uc_sellercategory").dataSqlOne("SELECT * FROM uc_sellercategory WHERE seller_code = 'SI2003' AND category_code = '"+tcategoryMap.get("parent_code")+"'", new MDataMap());
				}
				Map<String, Object> prodMap = DbUp.upTable("pc_productinfo").dataSqlOne("SELECT product_name FROM pc_productinfo WHERE product_code = '"+product_code+"'", new MDataMap());
				MDataMap mDataMap4 = new MDataMap();
				mDataMap4.put("product_code", product_code);
				mDataMap4.put("product_name", (String) prodMap.get("product_name"));
				mDataMap4.put("location_num", i+1+"");
				mDataMap4.put("evaluation_uid", evaluation_uid);
				mDataMap4.put("order_assessment", evaMap1.get("order_assessment")+"");
				mDataMap4.put("cover_img", cover_img);
				if(fcategoryMap != null) {
					mDataMap4.put("first_category_code", fcategoryMap.get("category_code")+"");
				}
				if(tcategoryMap != null) {
					mDataMap4.put("second_category_code", tcategoryMap.get("parent_code")+"");
					mDataMap4.put("third_category_code", tcategoryMap.get("category_code")+"");
				}
				DbUp.upTable("fh_apphome_evaluation").dataInsert(mDataMap4);
			}
			
		}
		
		return result;
	}
}
