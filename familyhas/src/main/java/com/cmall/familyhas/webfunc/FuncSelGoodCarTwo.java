package com.cmall.familyhas.webfunc;

import java.util.List;
import java.util.Map;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 根据一级分类查询二级分类
 * @author 12154
 *
 */
public class FuncSelGoodCarTwo extends RootFunc {

	@Override
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		String string = mDataMap.get("cate").toString();
		List<Map<String, Object>> dataList=DbUp.upTable("uc_sellercategory").dataSqlList("SELECT uc.category_code,uc.category_name FROM usercenter.uc_sellercategory uc where seller_code='SI2003' and `level`=3 and flaginable = '449746250001' and parent_code = '"+string+"'", new MDataMap());
		MWebResult webResult = new MWebResult();
		webResult.setResultObject(dataList);
		return webResult;
	}

}
