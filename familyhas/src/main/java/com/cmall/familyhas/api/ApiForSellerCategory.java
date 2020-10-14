package com.cmall.familyhas.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.SellerCategoryInput;
import com.cmall.familyhas.api.result.SellerCategoryResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * @description: 获取分类列表
 *
 * @author Yangcl
 * @date 2017年6月19日 下午2:45:31 
 * @version 1.0.0
 */
public class ApiForSellerCategory extends RootApi<SellerCategoryResult, SellerCategoryInput>{

	public SellerCategoryResult Process(SellerCategoryInput in, MDataMap mRequestMap) {
		SellerCategoryResult result = new SellerCategoryResult();
		if(StringUtils.isBlank(in.getSellerCode()) || StringUtils.isBlank(in.getParentCode())){
			result.setStatus("error");
			result.setResultCode(941901133);
			result.setResultMessage("请求参数不得为空!");  
			return result;
		}
		
		String sql = "select category_code , category_name , sort , adv_flag , adv_pic , adv_href from usercenter.uc_sellercategory "
	            + "where " 
	            + " flaginable = '449746250001' "
	            + " and seller_code = '" + in.getSellerCode() + "' "
	            + " and parent_code = '" + in.getParentCode() + "' ";
		List<Map<String, Object>> list = DbUp.upTable("uc_sellercategory").dataSqlList(sql, null);
		if(list == null || list.size() == 0){
			result.setStatus("error");
			result.setResultCode(941901133);
			result.setResultMessage("查询结果为空，请重试!");  
			return result;
		}
		result.setList(list); 
		return result;
	}
	
	
}










