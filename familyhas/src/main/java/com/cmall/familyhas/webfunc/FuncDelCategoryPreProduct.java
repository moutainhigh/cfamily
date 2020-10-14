package com.cmall.familyhas.webfunc;

import org.apache.commons.collections.MapUtils;

import com.cmall.systemcenter.jms.ProductJmsSupport;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncDelete;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncDelCategoryPreProduct extends FuncDelete {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String uid = mDataMap.get("zw_f_uid");
		
		MDataMap preProd = DbUp.upTable("uc_sellercategory_pre_product").one("uid",uid);
		
		if(null != preProd) {
			// 删除商品
			DbUp.upTable("uc_sellercategory_pre_product").dataExec("DELETE FROM uc_sellercategory_pre_product WHERE uid = '"+uid+"'", new MDataMap());
			
			// 刷新solr缓存
			ProductJmsSupport productJmsSupport = new ProductJmsSupport();
			String product_code = MapUtils.getString(preProd, "product_code");
			if(!"".equals(product_code)) {
				productJmsSupport.updateSolrData(product_code);
			}
		}
		
		return mResult;
	}

}
