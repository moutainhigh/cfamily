package com.cmall.familyhas.service;

import org.apache.commons.lang.StringUtils;

import com.srnpr.xmassystem.enumer.EKvSchema;
import com.srnpr.xmassystem.up.XmasKv;
import com.srnpr.zapcom.baseclass.BaseClass;

/**
 * 缓存相关
 * 
 * @author Lee
 *
 */
public class CacheOperateService extends BaseClass {

	/**
	 * 缓存中获取微公社余额开关
	 * 
	 * @param key
	 * @return value
	 */
	public String getCgroupMoneySwitch(String key) {

		if (StringUtils.isNotBlank(key)) {
//			String v = XmasKv.upFactory(EKvSchema.CgroupMoney).get(key);
			String v = XmasKv.upFactory(EKvSchema.CgroupMoney).hget("Config", key);
			return (StringUtils.isBlank(v) ? "0" : v);
		}
		
		return "";
	}
	
	/**
	 * 缓存中获取微公社余额提现开关
	 * @param key
	 * @return value
	 */
	public String getCgroupMoneyWithdrawSwitch(String key) {
		
		if (StringUtils.isNotBlank(key)) {
//			String v = XmasKv.upFactory(EKvSchema.CgroupMoney).get(key);
			String v = XmasKv.upFactory(EKvSchema.CgroupMoney).hget("Config", key);
			return (StringUtils.isBlank(v) ? "0" : v);
		}

		return "";
	}

	/**
	 * 
	 * 方法: getHjyBeanUsableFlag <br>
	 * 描述: 设置惠豆是否可用 <br>
	 * 作者: zhy<br>
	 * 时间: 2016年12月15日 下午6:10:14
	 * 
	 * @param key
	 *            默认switch
	 * @return
	 */
	public String getHjyBeanUsableFlag(String key) {
		if (StringUtils.isNotBlank(key)) {
			String v = XmasKv.upFactory(EKvSchema.HomehasBeanConfig).get(key);
			return (StringUtils.isBlank(v) ? "0" : v);
		}

		return "";
	}
}
