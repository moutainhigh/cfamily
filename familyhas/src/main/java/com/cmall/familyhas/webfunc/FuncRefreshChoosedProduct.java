package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.cmall.groupcenter.jd.job.RsyncJDProductPool;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 单个刷新京东商品池商品数据
 */
public class FuncRefreshChoosedProduct extends FuncAdd{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String uid = StringUtils.trimToEmpty(mDataMap.get("zw_f_uid"));
		
		MDataMap product = DbUp.upTable("pc_jingdong_product").one("uid", uid);
		
		new RsyncJDProductPool().rsyncSkuInfo(product.get("sku_code"));
		
		return mResult;
	}

}
