package com.cmall.familyhas.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.cmall.familyhas.api.input.ApiDLQAddToolsInput;
import com.cmall.familyhas.api.result.ApiDLQAddToolsResult;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.TempletePageLog;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiDLQAddTools extends RootApi<ApiDLQAddToolsResult, ApiDLQAddToolsInput> { 

	public ApiDLQAddToolsResult Process(ApiDLQAddToolsInput inputParam,
			MDataMap mRequestMap) {
		ApiDLQAddToolsResult result = new ApiDLQAddToolsResult();
		
		
		String paramType = inputParam.getParamType();
		/*
		 * 记录日志内容
		 */
		String content = "";//"在表《"+mPage.getPageTable()+"》 删除一条记录:"+JSON.toJSONString(mDataMap)
		
		if("1001".equals(paramType)) {//添加信息 
			
			String common_number = inputParam.getCommon_number();
			String describe = inputParam.getDescribe();
			String food_name = inputParam.getFood_name();
			String id_number = inputParam.getId_number();
			String location = inputParam.getLocation();
			String page_number = inputParam.getPage_number();
			String picture = inputParam.getPicture();
			String programa_english = inputParam.getPrograma_english();
			String programa_name = inputParam.getPrograma_name();
			String weight = inputParam.getWeight();
			//新增tv编号
			String tv_number = inputParam.getTv_number();
			
			PlusModelProductQuery plus = new PlusModelProductQuery(common_number);
			PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
			//判断是否填写 栏目名称 及 对应英文
			if(programa_name.length() >0 || programa_english.length() > 0) {
				//更改对应的所有  栏目名称  及  英文
				String sSql = "UPDATE familyhas.fh_dlq_content SET programa_name = '"+programa_name+"' , programa_english = '"+programa_english+"' WHERE page_number = '"+page_number+"' AND id_number = '"+id_number+"' and tv_number ='"+tv_number+"'";
				DbUp.upTable("fh_dlq_content").dataExec(sSql, new MDataMap());
			}
			
			String sUid = UUID.randomUUID().toString().replace("-", "");
			DbUp.upTable("fh_dlq_content").insert(
					"uid",sUid,
					"programa_name",programa_name,
					"programa_english",programa_english,
					"food_name",food_name,
					"weight",weight,
					"common_number",common_number,
					"picture",picture,
					"location",location,
					"co_describe",describe,
					"id_number",id_number,
					"delete_state","1001",
					"page_number",page_number,
					"tv_number",tv_number
					
					);
			
			result.setUid(sUid);
			result.setProduct_code(common_number);
			result.setProduct_name(upInfoByCode.getProductName());
			result.setProduct_status(upInfoByCode.getProductStatus());
			result.setProduct_main_url(upInfoByCode.getMainpicUrl());
			
			//记录日志
			content = "在表《fh_dlq_content》 添加一条记录:"+JSON.toJSONString(inputParam);
			TempletePageLog.upLog(content);
			
		} else if("1002".equals(paramType)) {//设置排序
			String sortByUid = inputParam.getSortByUid();
			if(sortByUid.length() > 0) {
				
				String[] sortUid = sortByUid.split(",");
				MDataMap mDataMap = new MDataMap();
				for (int i = 0; i < sortUid.length; i++) {
					if(sortUid[i].length() > 0) {
						String[] uid = sortUid[i].split("-");
						mDataMap.put("uid", uid[0]);
						mDataMap.put("location", uid[1]);
						DbUp.upTable("fh_dlq_content").dataExec("UPDATE familyhas.fh_dlq_content SET location = '"+uid[1]+"' WHERE uid = '"+uid[0]+"'", new MDataMap());
						
					}
				}
				//记录日志
				content = "在表《fh_dlq_content》 设置排序为:"+sortByUid;
				TempletePageLog.upLog(content);
				
			}
			
		} else if("1003".equals(paramType)) {//获取单个商品信息
			
			PlusModelProductQuery plus = new PlusModelProductQuery(inputParam.getCommon_number());
			PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
			result.setProduct_code(inputParam.getCommon_number());
			result.setProduct_status(upInfoByCode.getProductStatus());
			result.setProduct_name(upInfoByCode.getProductName());
			
		} else if("1004".equals(paramType)) {//添加广告
			
			String gg_end_time = inputParam.getGg_end_time();
			String gg_pic = inputParam.getGg_pic();
			String gg_start_time = inputParam.getGg_start_time();
			String gg_url = inputParam.getGg_url();
			String id_number = inputParam.getId_number();
			String location = inputParam.getLocation();
			String programa_name = inputParam.getPrograma_name();
			String page_number = inputParam.getPage_number();
			//新增tv编号
			String tv_number = inputParam.getTv_number();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");                

			try {
				
				Date gg_start_time_date = sdf.parse(gg_start_time);
				Date gg_end_time_date = sdf.parse(gg_end_time);
				if (gg_start_time_date.after(gg_end_time_date)) {
					result.setResultCode(0);
					result.setResultMessage("开始时间大于结束时间，请重新选择时间");
					return result;
				}
			} catch (Exception e) {
				result.setResultCode(0);
				result.setResultMessage("时间格式错误，请重新选择");
				return result;
			}

			
			MDataMap parmMap = new MDataMap();
			parmMap.put("start_time", gg_start_time);
			parmMap.put("end_time", gg_end_time);
			parmMap.put("page_number", page_number);
			parmMap.put("location", location);
			parmMap.put("tv_number", tv_number);
			
			List<MDataMap> queryAll = DbUp.upTable("fh_dlq_picture").queryAll("*", "", " ((start_time <:start_time and end_time >:start_time) or (start_time <:end_time and end_time >:end_time) or (end_time <=:end_time and start_time >=:start_time)) and id_number='1000' and delete_state='1001' and page_number=:page_number and location=:location and tv_number=:tv_number ", parmMap);
			
			if(queryAll.size() == 0 ) {
				
				MDataMap one = DbUp.upTable("fh_dlq_content").one("id_number","N4","page_number",page_number,"tv_number",tv_number);
				if(null != one) {
					MDataMap mDataMap = new MDataMap();
					mDataMap.put("zid", one.get("zid"));
					mDataMap.put("uid", one.get("uid"));
					mDataMap.put("programa_name", programa_name);
					DbUp.upTable("fh_dlq_content").update(mDataMap);
				} else {
					
					DbUp.upTable("fh_dlq_content").insert(
							"programa_name",programa_name,
							"id_number","N4",
							"delete_state","1001",
							"page_number",page_number,
							"tv_number",tv_number
							);
					
				}
				String sUid = UUID.randomUUID().toString().replace("-", "");
				DbUp.upTable("fh_dlq_picture").insert(
						"uid",sUid,
						"pic",gg_pic,
						"start_time",gg_start_time,
						"end_time",gg_end_time,
						"url",gg_url,
						"location",location,
						"id_number",id_number,
						"delete_state","1001",
						"page_number",page_number,
						"tv_number",tv_number
						);
				
				result.setUid(sUid);
				
				//记录日志
				content = "在表《fh_dlq_picture》 添加一条记录:"+JSON.toJSONString(inputParam);
				TempletePageLog.upLog(content);
			} else {
				result.setResultCode(0);
				String message = "";
				if (location.equals("449747760001")) {
					message = "上部";
				} else if (location.equals("449747760002")) {
					message = "下部";
				}
				result.setResultMessage("已存在该时段的"+message +"广告");
				
			}
			
			
		} else if("1005".equals(paramType)){//批量删除轮播广告
			
			String uid_str = "";
			String[] uid = inputParam.getUid_str().split(",");
			for (String id : uid) {
				uid_str += "'"+id+"',";
			}
			if(uid_str.length() >0) {
				uid_str = uid_str.substring(0, uid_str.length() -1);
			}
			String sSql = "UPDATE familyhas.fh_dlq_picture SET delete_state = '1000' WHERE uid IN ("+uid_str+") ";
			DbUp.upTable("fh_dlq_picture").dataExec(sSql, new MDataMap());
			
			//记录日志
			content = "在表《fh_dlq_picture》 批量删除"+uid.length+"条数据 uid:["+uid_str+"]";
			TempletePageLog.upLog(content);
			
		} else if("1006".equals(paramType)) {//大陆桥（发布/取消发布）
			String editStatus = inputParam.getEditStatus();
			String uid_str = inputParam.getUid_str();
			MDataMap one = DbUp.upTable("fh_dlq_status").one("tv_number",inputParam.getTv_number(),"page_number",inputParam.getPage_number());
			
			if(null != one) {
				if("1".equals(one.get("page_status"))) {
					one.put("page_status", "0");
					DbUp.upTable("fh_dlq_status").update(one);
					content = "在表《fh_dlq_page》 取消发布一条数据 page_number:["+inputParam.getPage_number()+"]  tv_number:["+inputParam.getTv_number()+"]";
				} else {
					one.put("page_status", "1");
					DbUp.upTable("fh_dlq_status").update(one);
					content = "在表《fh_dlq_page》 发布一条数据 page_number:["+inputParam.getPage_number()+"]  tv_number:["+inputParam.getTv_number()+"]";
				}
			} else {
				//没有顺序则没有发布过
				DbUp.upTable("fh_dlq_status").insert(
						"tv_number",inputParam.getTv_number(),
						"page_number",inputParam.getPage_number(),
						"page_status","1",
						"page_sort","99",
						"update_time",DateUtil.getSysDateTimeString());
				content = "在表《fh_dlq_page》 取消发布一条数据 page_number:["+inputParam.getPage_number()+"]  tv_number:["+inputParam.getTv_number()+"]";
			}
			/*
			if("0".equals(editStatus)) {//现在为未发布状态，进行发布
				DbUp.upTable("fh_dlq_page").dataExec("UPDATE familyhas.fh_dlq_page SET state = '1' WHERE uid = '"+uid_str+"'", new MDataMap());
				content = "在表《fh_dlq_page》 发布一条数据 uid:["+uid_str+"]";
			} else if("1".equals(editStatus)) {//现在是发布状态，进行取消发布
				DbUp.upTable("fh_dlq_page").dataExec("UPDATE familyhas.fh_dlq_page SET state = '0' WHERE uid = '"+uid_str+"'", new MDataMap());
				content = "在表《fh_dlq_page》 取消发布一条数据 uid:["+uid_str+"]";
			}
			*/
			
			//记录日志
			TempletePageLog.upLog(content);
		} else if("1007".equals(paramType)) {//大陆桥专题页修改 提交
			String cuisine_name = inputParam.getCuisine_name();
			String cuisine_picture = inputParam.getCuisine_picture();
			String page_number = inputParam.getPage_number();
			String picture = inputParam.getPicture();
			String programa_english = inputParam.getPrograma_english();
			String programa_name = inputParam.getPrograma_name();
			String special_name = inputParam.getSpecial_name();
			String url = inputParam.getUrl();
			String uid = inputParam.getUid_str();
			//新增tv编号
			String tv_number = inputParam.getTv_number();
			String activity_code = inputParam.getActivity_code();
			String mark = inputParam.getMark();
			
			MDataMap mDataMap = new MDataMap();
			mDataMap.put("uid", uid);
			mDataMap.put("special_name", special_name);
			mDataMap.put("cuisine_name", cuisine_name);
			mDataMap.put("cuisine_picture", cuisine_picture);
			mDataMap.put("url", url);
			mDataMap.put("is_share", inputParam.getIs_share());
			mDataMap.put("mark", mark);
			mDataMap.put("page_sort", inputParam.getPage_sort());
			
			if("449747800001".equals(inputParam.getIs_share())) {
				mDataMap.put("share_title", inputParam.getShare_title());
				mDataMap.put("share_content", inputParam.getShare_content());
				mDataMap.put("share_img", inputParam.getShare_img());
			} else {
				mDataMap.put("share_title", "");
				mDataMap.put("share_content", "");
				mDataMap.put("share_img", "");
			}
			
			DbUp.upTable("fh_dlq_page").dataExec(
					"UPDATE familyhas.fh_dlq_page SET mark =:mark,page_sort=:page_sort,special_name =:special_name,cuisine_name=:cuisine_name,cuisine_picture =:cuisine_picture,url=:url,is_share=:is_share,share_title=:share_title,share_content=:share_content,share_img=:share_img WHERE uid =:uid",
					mDataMap);
			//记录日志
			content = "在表《fh_dlq_page》 修改一条数据 :" +JSON.toJSONString(mDataMap);
			TempletePageLog.upLog(content);
			
			String[] programa_nameArr = programa_name.split("_");
			String[] programa_englishArr = programa_english.split("_");
			
			MDataMap mWhereMap = new MDataMap();
			mWhereMap.put("page_number", page_number);
			mWhereMap.put("tv_number", tv_number);
			
			boolean n1 = false,n2 = false ,n3 = false ,n4 = false,n5= false,n6=false,n7=false,n8=false,n9=false,n10=false;
			List<MDataMap> queryAll = DbUp.upTable("fh_dlq_content").queryAll("zid,uid,id_number", "", "page_number=:page_number AND delete_state = '1001' AND tv_number =:tv_number  GROUP BY id_number", mWhereMap);
			for (MDataMap mDataMap2 : queryAll) {
				String id_number = mDataMap2.get("id_number");
				if(id_number.equals("N1") && n1 == false) {
					n1 = true;
				} else if(id_number.equals("N2") && n2 == false) {
					n2 = true;
				} else if(id_number.equals("N3") && n3 == false) {
					n3 = true;
				} else if(id_number.equals("N4") && n4 == false) {
					n4 = true;
				} else if(id_number.equals("N5") && n5 == false) {
					n5 = true;
				} else if(id_number.equals("N6") && n6 == false) {
					n6 = true;
				} else if(id_number.equals("N7") && n7 == false) {
					n7 = true;
				}  else if(id_number.equals("N8") && n8 == false) {
					n8 = true;
				}
				
				if(!"1000".equals(inputParam.getCls_num())) {
					
					if(id_number.equals("N9") && n9 == false) {
						n9 = true;
					} else if(id_number.equals("N10") && n10 == false) {
						n10 = true;
					}
					
				} 
				
			}
			
			ApiDLQAddTools.addOrUpdateInfo(programa_nameArr[0],programa_englishArr[0],n1,"N1",page_number,"",tv_number,"",inputParam);
			ApiDLQAddTools.addOrUpdateInfo(programa_nameArr[1],"",n2,"N2",page_number,"",tv_number,"",inputParam);
			ApiDLQAddTools.addOrUpdateInfo(programa_nameArr[2],programa_englishArr[1],n3,"N3",page_number,"",tv_number,"",inputParam);
			if("1000".equals(inputParam.getCls_num())) {
				ApiDLQAddTools.addOrUpdateInfo(programa_nameArr[3],"",n4,"N4",page_number,"",tv_number,"",inputParam);
			}
			ApiDLQAddTools.addOrUpdateInfo(programa_nameArr[4],programa_englishArr[2],n5,"N5",page_number,"",tv_number,"",inputParam);
			ApiDLQAddTools.addOrUpdateInfo(programa_nameArr[5],"",n6,"N6",page_number,"",tv_number,"",inputParam);
			if("1000".equals(inputParam.getCls_num())) {
				ApiDLQAddTools.addOrUpdateInfo(programa_nameArr[6],"",n7,"N7",page_number,picture,tv_number,"",inputParam);
			}
			
			
			//优惠券活动编号，特殊处理
			ApiDLQAddTools.addOrUpdateInfo("优惠券活动编号","",n8,"N8",page_number,"",tv_number,activity_code,inputParam);
			
			if("1001".equals(inputParam.getCls_num())) {
				ApiDLQAddTools.addOrUpdateInfo(programa_nameArr[7],"",n9,"N9",page_number,"",tv_number,"",inputParam);
				ApiDLQAddTools.addOrUpdateInfo(programa_nameArr[8],"",n10,"N10",page_number,"",tv_number,"",inputParam);
			}
			
		}
		
		return result;
	}
	private static void addOrUpdateInfo (String programa_name,String programa_english,boolean isUpdate,String id_number,String page_number,String pic,String tv_number,String activityCode,ApiDLQAddToolsInput input) {
		MDataMap mDataMap = new MDataMap();
		if(isUpdate) {
			if(id_number.equals("N7")) {
				mDataMap.put("id_number", id_number);
				mDataMap.put("page_number", input.getPage_number());
				mDataMap.put("programa_name", programa_name);
				mDataMap.put("programa_english", programa_english);
				mDataMap.put("picture", input.getPicture());
				mDataMap.put("tv_number", input.getTv_number());
				
				DbUp.upTable("fh_dlq_content").dataExec(
						"UPDATE familyhas.fh_dlq_content SET programa_name =:programa_name,programa_english =:programa_english,picture=:picture WHERE page_number =:page_number AND tv_number=:tv_number AND id_number =:id_number AND delete_state = '1001'", 
						mDataMap);
			} else if(id_number.equals("N8")){//添加优惠券活动编号添加
				mDataMap.put("id_number", id_number);
				mDataMap.put("page_number", input.getPage_number());
				mDataMap.put("programa_name", programa_name);
				mDataMap.put("tv_number", input.getTv_number());
				mDataMap.put("activity_code", input.getActivity_code());
				
				DbUp.upTable("fh_dlq_content").dataExec(
						"UPDATE familyhas.fh_dlq_content SET programa_name =:programa_name,activity_code=:activity_code WHERE page_number =:page_number AND tv_number=:tv_number AND id_number =:id_number AND delete_state = '1001'", 
						mDataMap);
			} else {
				mDataMap.put("id_number", id_number);
				mDataMap.put("page_number", input.getPage_number());
				mDataMap.put("programa_name", programa_name);
				mDataMap.put("programa_english", programa_english);
				mDataMap.put("tv_number", input.getTv_number());
				if(id_number.equals("N9")) {
					mDataMap.put("t_describ",input.getT_describ());
				} else {
					mDataMap.put("t_describ","");
				}
				
				if(id_number.equals("N10")) {
					mDataMap.put("column_desc",input.getColumn_desc());
				} else {
					mDataMap.put("column_desc","");
				}
				
				DbUp.upTable("fh_dlq_content").dataExec(
						"UPDATE familyhas.fh_dlq_content SET programa_name =:programa_name,programa_english =:programa_english,column_desc=:column_desc,t_describ=:t_describ WHERE page_number =:page_number AND tv_number=:tv_number AND id_number =:id_number AND delete_state = '1001'", 
						mDataMap);
			}
			//记录日志
			String content = "在表《fh_dlq_page》 修改一条数据 :" +JSON.toJSONString(mDataMap);
			TempletePageLog.upLog(content);
		} else {
			if(id_number.equals("N2") && programa_name.length() <= 0) {//食材盒  为非必填项，为空则不进入数据库
			} else {
				DbUp.upTable("fh_dlq_content").insert(
						"programa_name",programa_name,
						"programa_english",programa_english,
						"page_number",input.getPage_number(),
						"id_number",id_number,
						"picture",input.getPicture(),
						"delete_state","1001",
						"tv_number",input.getTv_number(),
						"activity_code",input.getActivity_code(),
						"column_desc",input.getColumn_desc(),
						"t_describ",input.getT_describ()
						);
				mDataMap.put("programa_name", programa_name);
				mDataMap.put("programa_english", programa_english);
				mDataMap.put("page_number", input.getPage_number());
				mDataMap.put("id_number", id_number);
				mDataMap.put("picture", input.getPicture());
				mDataMap.put("delete_state", "1001");
				mDataMap.put("tv_number", input.getTv_number());
				mDataMap.put("activity_code", input.getActivity_code());
				mDataMap.put("column_desc", input.getColumn_desc());
				mDataMap.put("t_describ", input.getT_describ());
				//记录日志
				String content = "在表《fh_dlq_page》 添加一条数据 :" +JSON.toJSONString(mDataMap);
				TempletePageLog.upLog(content);
			}
		}
		
		
	}
	
}
