package com.cmall.familyhas.webfunc;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 根据商品分类编号 查询所有可用子分类编号
 * @remark 【商品推荐关联】-【设置】-二级分类变动时调用
 * @author 任宏斌
 * @date 2019年12月25日
 */
public class FuncGetProductCategorys extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String pCode = mDataMap.get("pCode");
		if (StringUtils.isNotEmpty(pCode)) {
			List<MDataMap> listCategory = DbUp.upTable("uc_sellercategory").queryAll("category_code,category_name", "",
					" seller_code=:seller_code and parent_code=:parent_code and flaginable='449746250001'",
					new MDataMap("seller_code", "SI2003", "parent_code", pCode));
			mResult.setResultObject(listCategory);
		}
		return mResult;
	}
}
