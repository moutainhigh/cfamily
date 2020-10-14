package com.cmall.familyhas.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 大陆桥公用service
 * @author zw
 *
 */
public class DLQService extends BaseClass{ 
	/**
	 * 查询分享信息
	 * @return 对应分享的信息
	 */
	public MDataMap getAdOneCont(String p_type,String tvNum){
		MDataMap result = new MDataMap();
		MDataMap praMap = new MDataMap();
		praMap.put("tv_number", tvNum);
		praMap.put("p_type",p_type);
		Map<String, Object> dataSqlOne = DbUp.upTable("fh_dlq_share").dataSqlOne(" SELECT * FROM familyhas.fh_dlq_share WHERE p_type = :p_type AND tv_number=:tv_number LIMIT 0,1 ", praMap);
		if(null != dataSqlOne) {
			result.put("zw_f_uid", String.valueOf(dataSqlOne.get("uid")));
			result.put("zw_f_ad_name", String.valueOf(dataSqlOne.get("ad_name")));
			result.put("zw_f_share_title", String.valueOf(dataSqlOne.get("share_title")));
			result.put("zw_f_share_content", String.valueOf(dataSqlOne.get("share_content")));
			result.put("zw_f_share_pic", String.valueOf(dataSqlOne.get("share_pic")));
		} else {
			result.put("zw_f_uid", "");
			result.put("zw_f_ad_name", "");
			result.put("zw_f_share_title", "");
			result.put("zw_f_share_content", "");
			result.put("zw_f_share_pic", "");
		}
		return result;
	}
	/**
	 * 根据页面编号查询轮播图表
	 * @param page_number
	 * @param type 1001(轮播) 1000(上下部广告)
	 * @param tv_number TV编号
	 * @return
	 */
	public Map<String, Object> getPicAll(String page_number,String type,String tv_number){
		
		Map<String, Object> mapFlash = new HashMap<String, Object>();
		String typePara = "1000";
		if("1001".equals(type)) {
			typePara = "1001";
		}
		MDataMap map = new MDataMap();
		map.put("page_number", page_number);
		map.put("type", typePara);
		map.put("tv_number", tv_number);
		
		List<MDataMap> queryAll = DbUp.upTable("fh_dlq_picture").queryAll("*", "cast(location as signed)", " page_number=:page_number and type=:type and tv_number =:tv_number and delete_state = '1001'", map);
		mapFlash.put("pic", queryAll);
		return mapFlash;
		
		
	}
	
	/**
	 * 大陆桥专题关联商品 (N2:食材盒关联商品信息;N5:嘉宾厨具直购关联的商品;)
	 * @param page_num 编号
	 * @return
	 */
	public Map<String, Object> getRelProductInfo(String page_num,String tv_number) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(!StringUtils.isEmpty(page_num) ) { 
			
			MDataMap mWhereMap = new MDataMap();
			mWhereMap.put("page_number", page_num);
			mWhereMap.put("tv_number", tv_number);
			
			List<MDataMap> queryAll = DbUp.upTable("fh_dlq_content").queryAll("uid,common_number,location,picture,id_number", "cast(location as signed)", "page_number=:page_number and tv_number=:tv_number and delete_state = '1001' and (id_number='N2' || id_number='N5')", mWhereMap);//-cast(location as signed)
			for (int i = 0; i < queryAll.size(); i++) {
				MDataMap mDataMap = queryAll.get(i);
				String product_code = mDataMap.get("common_number");
				if(product_code.length() > 0) {
					
					String id_number = mDataMap.get("id_number");
					//需要获取商品的信息
					PlusModelProductQuery plus = new PlusModelProductQuery(product_code);
					PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
					
					mDataMap.put("product_name", upInfoByCode.getProductName());
					mDataMap.put("product_status", upInfoByCode.getProductStatus());
					mDataMap.put("id_number", id_number);
					/*if(id_number.equals("N2") || id_number.equals("N5")) {
						mDataMap.put("picture", mDataMap.get("picture"));
					} else  {
						mDataMap.put("picture", upInfoByCode.getMainpicUrl());
					}*/
					String pic = mDataMap.get("picture");
					if(StringUtils.isNotBlank(pic)) {
						mDataMap.put("picture", pic);
					} else  {
						mDataMap.put("picture", upInfoByCode.getMainpicUrl());
					}
					mDataMap.put("uid", mDataMap.get("uid"));
					resultMap.put(id_number+"-"+product_code, mDataMap);
				}
				
			}
			
		}
		return resultMap;
	}
	
	/**
	 * 查询单个商品信息
	 * @param product_code 需要查询的商品编号
	 * @return
	 */
	public Map<String, Object> getProductInfo(String product_code) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		if(!StringUtils.isEmpty(product_code)) {
			
			//需要获取商品的信息
			PlusModelProductQuery plus = new PlusModelProductQuery(product_code);
			PlusModelProductInfo upInfoByCode = new LoadProductInfo().upInfoByCode(plus);
			
			resultMap.put("product_code",upInfoByCode.getProductCode());
			resultMap.put("product_name", upInfoByCode.getProductName());
			resultMap.put("product_status", upInfoByCode.getProductStatus());
			return resultMap;
		} else {
			return null;
		}
	}
	
	/**
	 * 根据uid 获取广告信息
	 * @param uid
	 * @return
	 */
	public MDataMap getADInfoByUid(String uid) {
		
		if(!StringUtils.isEmpty(uid)) {
			MDataMap one = DbUp.upTable("fh_dlq_picture").one("uid",uid);
			return one;
			
		} else {
			return null;
		}
		
	}
	
	/**
	 * 获取大陆桥tv维护内容
	 * @return
	 */
	public List<MDataMap> getTvList() {
		
		List<MDataMap> queryAll = DbUp.upTable("fh_dlq_tv").queryAll("tv_name,tv_number", "", "", new MDataMap());
		
		return queryAll;
	}
	
	/**
	 * 获取大陆桥渠道内容
	 * @return
	 */
	public List<MDataMap> getClsList() {
		
		List<MDataMap> queryAll = DbUp.upTable("fh_dlq_cls").queryAll("tv_name,tv_number", "", "", new MDataMap());
		
		return queryAll;
	}
	
	/**
	 * 根据tv编号获取所有对应的专题的发布状态
	 * @param tvNum tv编号
	 * @return
	 */
	public MDataMap getPageTvStatus(String tvNum) {
		 
		MDataMap result = new MDataMap();
		
		MDataMap paramMap = new MDataMap();
		paramMap.put("tv_number", tvNum);
		List<MDataMap> queryAll = DbUp.upTable("fh_dlq_status").queryAll("page_number,tv_number,page_status", "", " tv_number=:tv_number ", paramMap);
		if(queryAll.size() > 0) {
			
			for (MDataMap mDataMap : queryAll) {
				result.put(mDataMap.get("page_number"), "1".equals(mDataMap.get("page_status"))?"1001":"1000");
			}
			
		} 
		
		return result;
	}
	
	/**
	 * 获取专题关联的渠道号
	 * fq
	 * @param p_type 类型
	 * @param num  渠道号
	 * @return
	 */
	public List<String> getPageRelClsOrTvList (String num) {
		List<String> result = new ArrayList<String>();
		
		if(StringUtils.isNotBlank(num)){
			
			MDataMap param = new MDataMap();
			param.put("tv_number", num);
			List<MDataMap> queryAll = DbUp.upTable("fh_dlq_status").queryAll("page_number", "", "tv_number=:tv_number", param);
			for (MDataMap mDataMap : queryAll) {
				result.add(mDataMap.get("page_number"));
			}
			
		}
		
		return result;
		
	}
}
