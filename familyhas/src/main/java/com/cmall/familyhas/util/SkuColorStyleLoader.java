package com.cmall.familyhas.util;

import org.apache.commons.lang.StringUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.rootclass.CacheDefine;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 缓存SKU的颜色款式
 */
public class SkuColorStyleLoader {
	
	private static Cache cache;
	
	public static SkuColorStyleLoader instance = new SkuColorStyleLoader();
	
	private SkuColorStyleLoader() {
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

	public ColorStyle upValue(String k) {
		if(StringUtils.isBlank(k)) return null;
		
		ColorStyle oReturnObject = null;
		Element eCachElement = cache.get(k);
		if (eCachElement != null) {
			oReturnObject = (ColorStyle)eCachElement.getObjectValue();
		}

		if(oReturnObject == null){
			// 解析颜色尺码的值  color_id=0&style_id=0
			String skuKey = (String)DbUp.upTable("pc_skuinfo").dataGet("sku_key", "", new MDataMap("sku_code", k));
			if(skuKey != null && StringUtils.isNotBlank(skuKey)){
				 String[] vs = StringUtils.split(skuKey,"&");
				 if(vs != null && vs.length == 2){
					 String[] colorV = StringUtils.split(vs[0],"=");
					 String[] colorS = StringUtils.split(vs[1],"=");
					 
					 if(colorV.length == 2 && colorS.length == 2){
						 oReturnObject = new ColorStyle(colorV[1], colorS[1]);
					 }
				 }
			}
			
			if(oReturnObject != null){
				cache.put(new Element(k, oReturnObject));
			}
		}

		return oReturnObject;
	}
	
	public static class ColorStyle {
		private String color;
		private String style;
		
		public ColorStyle(String color, String style) {
			super();
			this.color = color;
			this.style = style;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public String getStyle() {
			return style;
		}
		public void setStyle(String style) {
			this.style = style;
		}
	}
	
}
