package com.cmall.familyhas.webfunc;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/** 
* @ClassName: BrandRelProductFunChangeSta 
* @Description: 更新品牌特惠关联商品的状态为可用或者冻结
* @author 张海生
* @date 2015-5-7 下午7:05:27 
*  
*/
public class BrandRelProductFunChangeSta extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String uid = mDataMap.get("zw_f_uid");
		MDataMap mdata = DbUp.upTable("pc_brand_rel_product").one("uid", uid);
		String status = mdata.get("product_status");
		MDataMap dataMap = new MDataMap();
		dataMap.put("uid", uid);
		if ("1".equals(status)) {// 可用就设置成冻结
			dataMap.put("product_status", "2");
			DbUp.upTable("pc_brand_rel_product").dataUpdate(dataMap,
					"product_status", "uid");
		} else if ("2".equals(status)) {// 冻结就设置成为可用
			dataMap.put("product_status", "1");
			DbUp.upTable("pc_brand_rel_product").dataUpdate(dataMap,
					"product_status", "uid");
		}
		return mResult;
	}

}
