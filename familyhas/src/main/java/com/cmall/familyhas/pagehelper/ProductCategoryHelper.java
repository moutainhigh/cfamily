package com.cmall.familyhas.pagehelper;


import org.apache.commons.lang3.StringUtils;

import com.cmall.usercenter.service.SellercategoryService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.helper.PageHelper;

/**
 * 展示商品分类名称<br>
 * 传入参数：商品编号
 */
public class ProductCategoryHelper implements PageHelper{

	@Override
	public Object upData(Object... params) {
		if(params == null || params.length == 0) {
			return "";
		}
		
		MDataMap map = new SellercategoryService().getCateGoryByProduct(params[0].toString(), "SI2003");
		return StringUtils.join(map.values(),"<br/>");
	}

}
