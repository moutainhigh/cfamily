package com.cmall.familyhas.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.baseclass.BaseClass;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;

/** 
* @ClassName: BrandService 
* @Description: 品牌管理
* @author 张海生
* @date 2015-6-8 下午3:16:16 
*  
*/
public class BrandService extends BaseClass {

	/** 
	* @Description:根据多个品牌编号查询相关品牌
	* @param brandCodes
	* @author 张海生
	* @date 2015-6-8 下午3:41:47
	* @return MDataMap 
	* @throws 
	*/
	public MDataMap getBrandNames(String brandCodes) {
		MDataMap mapResult = new MDataMap();
		mapResult.put("brandNames", "");
		if(StringUtils.isNotBlank(brandCodes)){
			String brandCodeArray[] = brandCodes.split(",");
			List<MDataMap> brandList = DbUp.upTable("pc_brandinfo").queryAll(
					"brand_name", "", "brand_code in('"+StringUtils.join(brandCodeArray, "','")+"')",
					new MDataMap());//查询品牌
			if(brandList != null && brandList.size() > 0){
				List<String> brandNameList = new ArrayList<String>();
				for (MDataMap mDataMap : brandList) {
					brandNameList.add(mDataMap.get("brand_name"));
				}
				mapResult.put("brandNames", StringUtils.join(brandNameList, ","));
			}
		}
		return mapResult;
	}
}
