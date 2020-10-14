package com.cmall.familyhas.service;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

public class ShoppingCartlikeService extends BaseClass {
	/**
	 * 
	 * 方法: getHjyBeanUsableFlag <br>
	 * 描述: 设置购物车猜你喜欢是否可用 <br>
	 * 作者: lzf<br>
	 * 时间: 2017年6月21日15:23:01
	 * 
	 * @param key
	 *            默认switch
	 * @return
	 */
	public String getHjyBeanUsableFlag(String key) {
		if (StringUtils.isNotBlank(key)) {
			MDataMap v = DbUp.upTable("sc_relevant_configure").one();
			if (v != null) {
				return v.get("is_flag");
			} else {
				return "";
			}
		}
		return "";
	}
	
	public String getMessageUsableFlag(String key) {
		if (StringUtils.isNotBlank(key)) {
			MDataMap v = null;
			try {
				v = DbUp.upTable("sc_message_configure").one();
			} catch (Exception e) {
				
			}
			if (v != null) {
				return v.get("is_flag");
			} else {
				return "";
			}
		}
		return "";
	}
}
