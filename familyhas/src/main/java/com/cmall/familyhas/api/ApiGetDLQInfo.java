package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetDLQInfoInput;
import com.cmall.familyhas.api.model.DLQpicListModel;
import com.cmall.familyhas.api.model.DLQpicture;
import com.cmall.familyhas.api.model.DLQshare;
import com.cmall.familyhas.api.result.ApiGetDLQInfoResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * 大陆桥列表信息接口
 * @author fq
 *
 */
public class ApiGetDLQInfo extends RootApiForManage<ApiGetDLQInfoResult, ApiGetDLQInfoInput>{ 

	public ApiGetDLQInfoResult Process(ApiGetDLQInfoInput inputParam,
			MDataMap mRequestMap) {
		
		ApiGetDLQInfoResult result = new ApiGetDLQInfoResult();
		List<DLQpicListModel> conntentList = new ArrayList<DLQpicListModel>();
		List<DLQpicture> picList = new ArrayList<DLQpicture>();
		
		
		MDataMap one = null;
		if("1000".equals(inputParam.getP_type())) {
			one = DbUp.upTable("fh_dlq_tv").one("tv_number",inputParam.getTv_number());
		} else if("1001".equals(inputParam.getP_type())) {
			one = DbUp.upTable("fh_dlq_cls").one("tv_number",inputParam.getTv_number());
		}
		
		if(null != one) {
			
			result.setTv_number(inputParam.getTv_number());
			result.setP_type(inputParam.getP_type());
			
			//----
			MDataMap paramMap = new MDataMap();
			paramMap.put("tv_number", inputParam.getTv_number());
			paramMap.put("p_type", inputParam.getP_type());//发布状态
			List<Map<String, Object>> dataSqlList = new ArrayList<Map<String,Object>>();
			
//		String sSql = "SELECT special_name,cuisine_name,cuisine_picture,url,page_number FROM familyhas.fh_dlq_page WHERE state = :state AND page_number IN (" +
//							"SELECT page_number FROM familyhas.fh_dlq_content WHERE tv_number= :tv_number  GROUP BY page_number" +
//							") ORDER BY zid DESC";
			String sSql = "SELECT special_name,cuisine_name,cuisine_picture,url,page_number FROM familyhas.fh_dlq_page WHERE page_type = :p_type AND page_number IN (" +
					"SELECT page_number FROM familyhas.fh_dlq_status WHERE tv_number = :tv_number AND page_status = '1'" +
					")" +
					"ORDER BY cast(page_sort as signed) DESC ";
			dataSqlList = DbUp.upTable("fh_dlq_page").dataSqlList(sSql, paramMap);
			//列表信息
			for (int i = 0; i < dataSqlList.size(); i++) {
				Map<String,Object> mDataMap = dataSqlList.get(i);
				DLQpicListModel cuisineModel = new DLQpicListModel();
				cuisineModel.setPage_number(toStr(mDataMap.get("page_number")));//编号
				cuisineModel.setPic_link(toStr(mDataMap.get("cuisine_picture")));//菜系图片
				cuisineModel.setTitle(toStr(mDataMap.get("cuisine_name")));//菜系名称 
				conntentList.add(cuisineModel);
			}
			
			//轮播图  start_time  end_time 
			String systime = DateUtil.getSysDateTimeString();
			List<MDataMap> queryAll2 = DbUp.upTable("fh_dlq_picture").queryAll("*", "cast(location as signed)", "id_number ='1001' and delete_state = '1001' and p_type =:p_type and tv_number=:tv_number and start_time<='"+systime+"' and '"+systime+"'<=end_time ", paramMap);
			for (int i = 0; i < queryAll2.size(); i++) {
				MDataMap mDataMap = queryAll2.get(i);
				DLQpicture picModel = new DLQpicture();
				
				picModel.setEnd_time(mDataMap.get("end_time"));//结束时间
				picModel.setPic(mDataMap.get("pic"));//图片
				picModel.setStart_time(mDataMap.get("start_time"));//开始时间
				picModel.setUrl(mDataMap.get("url"));//URL
				picList.add(picModel);
				
			}
			
			/*
			 * 应需求，默认标题为渠道号的名字
			 */
			if("1000".equals(inputParam.getP_type())) {//默认值
				result.setPage_title("饭好了");
			} else if("1001".equals(inputParam.getP_type())) {
				result.setPage_title(one.get("tv_name"));
			}
			
			//分享信息
			List<MDataMap> queryAll3 = DbUp.upTable("fh_dlq_share").queryAll("*", "", " p_type = :p_type and tv_number=:tv_number", paramMap);
			if(queryAll3.size() > 0) {
				MDataMap mDataMap = queryAll3.get(0);
				DLQshare share_info = new DLQshare();
				
				share_info.setAd_name(mDataMap.get("ad_name"));
				share_info.setShare_content(mDataMap.get("share_content"));
				share_info.setShare_pic(mDataMap.get("share_pic"));
				share_info.setShare_title(mDataMap.get("share_title"));
				
				/**
				 * 如果有分享信息则全集页的页面标题为分享标题
				 */
				if("1000".equals(inputParam.getP_type())) {
					result.setPage_title(share_info.getShare_title());
				}
				
				result.setShare_info(share_info);
			}
			
			result.setPicList(picList);
			result.setConntentList(conntentList);
			
		}
		
		
		return result;
	}
	
	private String toStr(Object obj) {
		
		String rtnStr = "";
		if(null != obj && !StringUtils.isEmpty(String.valueOf(obj))) {
			rtnStr = String.valueOf(obj);
		} 
		
		return rtnStr;
	}
	
}
