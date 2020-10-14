package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.model.QaTypeModel;
import com.cmall.familyhas.api.result.ApiGetQaTypeListResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;

/**
 * 
 * @remark 获取客服与帮助-问题 列表
 * @author 任宏斌
 * @date 2018年12月3日
 */
public class ApiGetQaTypeList extends RootApi<ApiGetQaTypeListResult, RootInput>{

	@Override
	public ApiGetQaTypeListResult Process(RootInput inputParam, MDataMap mRequestMap) {
		ApiGetQaTypeListResult result = new ApiGetQaTypeListResult();
		
		String sql = "select sc.define_code type_code,sc.define_name type_title from systemcenter.sc_define sc where sc.parent_code='44974826' order by define_code";
		List<Map<String, Object>> typeList = DbUp.upTable("sc_define").dataSqlList(sql, null);
		
		List<QaTypeModel> tempList = new ArrayList<QaTypeModel>();
		for (Map<String, Object> type : typeList) {
			
			QaTypeModel qaTypeModel = new QaTypeModel();
			
			String typeCode = type.get("type_code")+"";
			String typeTitle = type.get("type_title")+"";
			
			String sSql = "select qa_code,qa_title from familyhas.fh_common_problem_new where qa_type=:qa_type and qa_status='449748270001' order by qa_sorting asc,update_time desc";
			List questions = DbUp.upTable("fh_common_problem_new").dataSqlList(sSql, new MDataMap("qa_type",typeCode));
		
			qaTypeModel.setTypeCode(typeCode);
			qaTypeModel.setTypeTitle(typeTitle);
			qaTypeModel.setQuestions(questions);
			tempList.add(qaTypeModel);
		}
		
		result.setList(tempList);
		
		
		return result;
	}
	
}
