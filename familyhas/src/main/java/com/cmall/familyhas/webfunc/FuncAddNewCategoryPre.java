package com.cmall.familyhas.webfunc;

import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncAddNewCategoryPre extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		// 插入当前用户所在店铺
		String seller_code = UserFactory.INSTANCE.create().getManageCode();
		String category_name = mDataMap.get("zw_f_category_name").trim();
		String parent_code = mDataMap.get("zw_f_parent_code");
		String flaginable = mDataMap.get("zw_f_flaginable");
		String photo = mDataMap.get("zw_f_photo");
		String category_type = mDataMap.get("zw_f_category_type");
		
		if(null == parent_code || "".equals(parent_code)) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("没有传父级分类");
			return mResult;
		}
		
		if(null == category_name || "".equals(category_name)) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("请填写分类名称");
			return mResult;
		}
		
		MDataMap parentCategory = DbUp.upTable("uc_sellercategory_pre").one("category_code",parent_code,"seller_code",seller_code,"flaginable","449746250001");
		if(null == parentCategory) {
			mResult.setResultCode(-1);
			mResult.setResultMessage("父级分类不存在");
			return mResult;
		}else {
			if(!"3".equals(parentCategory.get("level"))) {
				mResult.setResultCode(-1);
				mResult.setResultMessage("该级分类不能新建子分类");
				return mResult;
			}
			if(!"449748510001".equals(parentCategory.get("category_type"))) {
				mResult.setResultCode(-1);
				mResult.setResultMessage("分类类型有误");
				return mResult;
			}
		}
		
		String category_code = WebHelper.upCode("44971604");
		MDataMap insertMap = new MDataMap();
		insertMap.put("seller_code", seller_code);
		insertMap.put("category_code", category_code );
		insertMap.put("category_name", category_name);
		insertMap.put("parent_code", parent_code);
		// 修改sort，影响其他地方排序
		MDataMap sortParamsMap = new MDataMap();
		sortParamsMap.put("seller_code", seller_code);
		sortParamsMap.put("parent_code", parent_code);
		Map<String, Object> sortMap = DbUp.upTable("uc_sellercategory_pre").dataSqlOne(
				"select max(sort) from uc_sellercategory_pre where parent_code=:parent_code and seller_code=:seller_code",
				sortParamsMap);
		if (!sortMap.isEmpty() && sortMap.get("max(sort)") != null && !"".equals(sortMap.get("max(sort)"))) {
			String four = String.valueOf(Integer.valueOf(sortMap.get("max(sort)").toString().substring(
					sortMap.get("max(sort)").toString().length() - 4,
					sortMap.get("max(sort)").toString().length())) + 1);
			for (int i = 0, j = (4 - four.length()); i < j; i++) {
				four = "0" + four;
			}
			String so = sortMap.get("max(sort)").toString().substring(0,
					sortMap.get("max(sort)").toString().length() - 4) + four;
			insertMap.put("sort", so);
		} else {
			Map<String, Object> caMap = DbUp.upTable("uc_sellercategory_pre").dataSqlOne(
					"select sort from uc_sellercategory_pre where category_code=:parent_code and seller_code=:seller_code",
					sortParamsMap);
			insertMap.put("sort", caMap.get("sort") + "0001");
		}
		insertMap.put("flaginable", flaginable);
		insertMap.put("level", "4");
		insertMap.put("photo", photo);
		insertMap.put("category_type", category_type);
		DbUp.upTable("uc_sellercategory_pre").dataInsert(insertMap );

		return mResult;
	}
}
