package com.cmall.familyhas.service.ldmsgpay;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/** 
* @author Angel Joy
* @Time 2020-8-19 10:33:25 
* @Version 1.0
* <p>Description:</p>
*/
public class LdMsgPayService extends BaseClass{

	
	/**
	 * 获取商品配置页面UID，目前只有一条，如果数据库为空则返回空串
	 * @return
	 * 2020-8-19
	 * Angel Joy
	 * String
	 */
	public String getProductOperateUid() {
		Map<String,Object> map = new HashMap<String,Object>();
		String sql = "SELECT * FROM systemcenter.sc_recommend_product_ldpay limit 1";
		map = DbUp.upTable("sc_recommend_product_ldpay").dataSqlOne(sql, null);
		if(map != null && !map.isEmpty()) {
			return MapUtils.getString(map, "uid","");
		}
		return "";
	}
	
	/**
	 * 获取联系我们配置页面UID
	 * @return
	 * 2020-8-19
	 * Angel Joy
	 * String
	 */
	public String getContantUsUid() {
		Map<String,Object> map = new HashMap<String,Object>();
		String sql = "SELECT * FROM systemcenter.sc_contact_us_ldpay limit 1";
		map = DbUp.upTable("sc_contact_us_ldpay").dataSqlOne(sql, null);
		if(map != null && !map.isEmpty()) {
			return MapUtils.getString(map, "uid","");
		}
		return "";
	}
	
	/**
	 * 获取商品
	 * @return
	 * 2020-8-19
	 * Angel Joy
	 * String
	 */
	public String getProductCodes() {
		Map<String,Object> map = new HashMap<String,Object>();
		String sql = "SELECT * FROM systemcenter.sc_recommend_product_ldpay limit 1";
		map = DbUp.upTable("sc_recommend_product_ldpay").dataSqlOne(sql, null);
		if(map != null && !map.isEmpty()) {
			return MapUtils.getString(map, "product_codes","");
		}
		return "";
	}
	
	public MDataMap getProductOperateInfo() {
		Map<String,Object> map = new HashMap<String,Object>();
		String sql = "SELECT * FROM systemcenter.sc_recommend_product_ldpay limit 1";
		map = DbUp.upTable("sc_recommend_product_ldpay").dataSqlOne(sql, null);
		if(map != null && !map.isEmpty()) {
			return new MDataMap(map);
		}
		return new MDataMap("if_recommend","449748600001","recommend_type","4497471600660002");//默认配置商品
	}
}
