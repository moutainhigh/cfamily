package com.cmall.familyhas.util;

import org.apache.commons.lang.StringUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.rootclass.CacheDefine;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 缓存调编后的商品编号对应
 */
public class ProductCodeCopyLoader {
	
	private static Cache cache;
	
	private static ProductCodeCopyLoader instance = new ProductCodeCopyLoader();
	
	private ProductCodeCopyLoader() {
		CacheDefine cDefine = new CacheDefine();
		String sCacheName = this.getClass().getName();
		CacheConfiguration cacheConfiguration = new CacheConfiguration();

		cacheConfiguration.setName(sCacheName);

		// 设置最大数量
		cacheConfiguration.setMaxEntriesLocalHeap(9999999);
		// 设置最长存活时间
		cacheConfiguration.setTimeToIdleSeconds(900);
		cacheConfiguration.setTimeToLiveSeconds(3600);

		cacheConfiguration.setMemoryStoreEvictionPolicy("FIFO");

		cache = cDefine.inCustomCache(sCacheName, cacheConfiguration);
	}

	private String upValue(String k) {
		String oReturnObject = null;
		Element eCachElement = cache.get(k);
		if (eCachElement != null) {
			oReturnObject = (String)eCachElement.getObjectValue();
		}

		if(oReturnObject == null){
			oReturnObject = (String)DbUp.upTable("pc_product_code_copy").dataGet("product_code_new", "", new MDataMap("product_code_old",k));
			
			cache.put(new Element(k, StringUtils.trimToEmpty(oReturnObject)));
		}

		return oReturnObject;
	}
	
	public static String queryCode(String productCode){
		return instance.upValue(productCode);
	}
}
