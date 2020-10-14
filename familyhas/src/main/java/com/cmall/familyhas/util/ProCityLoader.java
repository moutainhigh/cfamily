package com.cmall.familyhas.util;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.rootclass.CacheDefine;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 缓存省市区信息
 * 类: ProCityLoader <br>
 * 时间: 2016年11月7日 下午10:10:00
 */
public class ProCityLoader {
	
	private static Cache cache;
	
	private static ProCityLoader instance = new ProCityLoader();
	
	private ProCityLoader() {
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

	@SuppressWarnings("unchecked")
	private List<MDataMap> upValue(String k) {
		Object oReturnObject = null;
		Element eCachElement = cache.get(k);
		if (eCachElement != null) {
			oReturnObject = eCachElement.getObjectValue();
		}

		if(oReturnObject == null){
			List<MDataMap> pcgovList = DbUp.upTable("sc_tmp").queryAll("code,name", "", "", new MDataMap());//查出所有的省市区信息
			oReturnObject = pcgovList;
			
			if(!pcgovList.isEmpty()){
				cache.put(new Element(k, oReturnObject));
			}
		}

		return (List<MDataMap>)oReturnObject;
	}
	
	public static List<MDataMap> queryAll(){
		return instance.upValue("all");
	}
}
