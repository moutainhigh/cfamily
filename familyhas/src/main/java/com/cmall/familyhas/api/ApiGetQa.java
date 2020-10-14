package com.cmall.familyhas.api;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiGetQaInput;
import com.cmall.familyhas.api.result.ApiGetQaResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 
 * @remark 获取客服与帮助-问题 详情
 * @author 任宏斌
 * @date 2018年12月4日
 */
public class ApiGetQa extends RootApi<ApiGetQaResult, ApiGetQaInput>{
	

	@Override
	public ApiGetQaResult Process(ApiGetQaInput inputParam, MDataMap mRequestMap) {
		ApiGetQaResult result = new ApiGetQaResult();
		
		String qaCode = inputParam.getQaCode();
		if(StringUtils.isNotBlank(qaCode)) {
			String sql = "select sc.define_name qa_type,fh.qa_code,fh.qa_title,fh.qa_content,fh.qa_status from familyhas.fh_common_problem_new fh,systemcenter.sc_define sc where fh.qa_type=sc.define_code and fh.qa_code=:qa_code";
			Map<String, Object> qaMap = DbUp.upTable("fh_common_problem_new").dataSqlOne(sql, new MDataMap("qa_code", qaCode));
			if(null != qaMap && "449748270001".equals(qaMap.get("qa_status"))) {
				result.setQaType(qaMap.get("qa_type")+"");
				result.setQaCode(qaMap.get("qa_code")+"");
				result.setQaTitle(qaMap.get("qa_title")+"");
				result.setQaContent(qaMap.get("qa_content")+"");
			}
		}
		
		return result;
	}
	
}
