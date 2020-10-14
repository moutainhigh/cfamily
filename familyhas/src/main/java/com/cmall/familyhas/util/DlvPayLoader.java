package com.cmall.familyhas.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.rootclass.CacheDefine;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 缓存货到付款地区配置信息
 */
public class DlvPayLoader {
	
	private static Cache cache;
	
	private static DlvPayLoader instance = new DlvPayLoader();
	
	private DlvPayLoader() {
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

	private MDataMap upValue(String k) {
		MDataMap oReturnObject = null;
		Element eCachElement = cache.get(k);
		if (eCachElement != null) {
			oReturnObject = (MDataMap)eCachElement.getObjectValue();
		}

		if(oReturnObject == null){
			oReturnObject = new MDataMap();
			
			List<MDataMap> dataList = DbUp.upTable("sc_dlv_district").queryAll("lrgn_cd,mrgn_cd,srgn_cd", "", "", new MDataMap());//查出所有的区域信息
			for(MDataMap v : dataList){
				oReturnObject.put(v.get("srgn_cd"), v.get("srgn_cd"));
				
				// 判断特殊区域编码：000000 不限
				// 如果三级区域编码不限则把二级区域编码进入到缓存
				if("000000".equals(v.get("srgn_cd"))){
					oReturnObject.put(v.get("mrgn_cd"), v.get("mrgn_cd"));
				}
				
				// 判断特殊区域编码：000000 不限
				// 如果二级区域编码不限则把一级区域编码进入到缓存
				if("000000".equals(v.get("mrgn_cd"))){
					oReturnObject.put(v.get("lrgn_cd"), v.get("lrgn_cd"));
				}
			}
			
			cache.put(new Element(k, oReturnObject));
		}

		return oReturnObject;
	}
	
	public static MDataMap queryAll(){
		return instance.upValue("all");
	}
	
	/**
	 * 判断提供的区域编码是否在不支持货到付款区域表中
	 * @param thirdCode 三级区域编码
	 * @return
	 */
	public static boolean hasContains(String thirdCode){
		MDataMap map = queryAll();
		
		if(thirdCode == null) {
			return false;
		}
		
		MDataMap tmp = DbUp.upTable("sc_tmp").one("code", thirdCode);
		if(tmp == null) {
			return true;
		}
		
		// 如果传入的是4级编码则取父级编码
		if("4".equals(tmp.get("code_lvl"))) {
			thirdCode = tmp.get("p_code");
		}
		
		// 判断三级编码是否在区域内
		if(map.containsKey(thirdCode)){
			return true;
		}
		
		// 判断二级编码是否在区域内
		if(map.containsKey(StringUtils.left(thirdCode, 4)+"00")){
			return true;
		}
		
		// 判断一级编码是否在区域内
		if(map.containsKey(StringUtils.left(thirdCode, 2)+"0000")){
			return true;
		}
		
		return false;
	}
}
