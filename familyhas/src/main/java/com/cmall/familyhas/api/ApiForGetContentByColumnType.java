package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiForGetContentByColumnTypeInput;
import com.cmall.familyhas.api.model.HomeColumnContent;
import com.cmall.familyhas.api.result.ApiForGetContentByColumnTypeResult;
import com.cmall.familyhas.service.HomeColumnService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 分页获取首页栏目内容
 * @author sy
 * 
 */
public class ApiForGetContentByColumnType extends RootApiForVersion<ApiForGetContentByColumnTypeResult, ApiForGetContentByColumnTypeInput> {
	
	static final Integer pageSize = 10;
	
	public ApiForGetContentByColumnTypeResult Process(ApiForGetContentByColumnTypeInput inputParam, MDataMap mRequestMap) {
		ApiForGetContentByColumnTypeResult result = new ApiForGetContentByColumnTypeResult();//返回结果
		
		HomeColumnService hcService = new HomeColumnService();
		String columnType = inputParam.getColumnType();
		MDataMap columnMap = DbUp.upTable("fh_apphome_column").one("column_code",inputParam.getColumnID());
		if(StringUtils.isBlank(columnType)){
			columnType = columnMap.get("column_type");
		}
		
		// 第几页
		int page = inputParam.getPage();
		// 起始索引
		int index = (page-1) * pageSize;
		String maxWidth = StringUtils.isBlank(inputParam.getMaxWidth()) ? "0" : inputParam.getMaxWidth(); //最大宽度
		// 调用渠道
		String channelId = getChannelId();
		String userCode = getFlagLogin() ? getOauthInfo().getUserCode() : "";
		// 总页数
		int totalPage = 0;
		List<HomeColumnContent> contentList = new ArrayList<HomeColumnContent>();
		if(columnType.equals("4497471600010037")){
			contentList = hcService.getFenXiaoActivityWeApp(maxWidth,true,getApiClientValue("app_vision"),userCode,channelId);
		}
		int maxIndex = index+pageSize;
		if(contentList.size()<=maxIndex){
			maxIndex = contentList.size();
		}
		
		totalPage = (int) Math.ceil(contentList.size()/pageSize.doubleValue());
		result.setTotalPage(totalPage);
		result.setColumnList(contentList.subList(index, maxIndex));				
		
		
		return result;
	}
	
	
}
