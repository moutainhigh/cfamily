package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.input.ApiForGetStoreDistrictNewInput;
import com.cmall.familyhas.api.result.ApiForGetStoreDistrictNewResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiForGetStoreDistrictNew extends RootApi<ApiForGetStoreDistrictNewResult, ApiForGetStoreDistrictNewInput> {

	@Override
	public ApiForGetStoreDistrictNewResult Process(ApiForGetStoreDistrictNewInput inputParam, MDataMap mRequestMap) {
		ApiForGetStoreDistrictNewResult apiResult = new ApiForGetStoreDistrictNewResult();
		List<ApiForGetStoreDistrictNewResult.Area> areaList = apiResult.getAreaList();
		List<Map<String, Object>> tmpList = new ArrayList<Map<String, Object>>();
		
		String parentCode = inputParam.getParentCode();
		if("".equals(parentCode)) {//省
			tmpList = DbUp.upTable("sc_tmp").dataSqlList("select * from sc_tmp where code_lvl = 1 and (p_code = '' or p_code is null) and use_yn = 'Y' and send_yn = 'Y' order by code", new MDataMap());
		}else {//市县镇
			tmpList = DbUp.upTable("sc_tmp").dataSqlList("select * from sc_tmp where code_lvl <> 1 and p_code = :p_code and use_yn = 'Y' and send_yn = 'Y' order by code", new MDataMap("p_code", parentCode));
		}
		
		for(Map<String, Object> tmp : tmpList) {
			String code = MapUtils.getString(tmp, "code", "");
			boolean flag = this.checkCodeIfSpecial(code);
			String name = MapUtils.getString(tmp, "name", "");
			String codeLvl = MapUtils.getString(tmp, "code_lvl", "");
			String isShow = MapUtils.getString(tmp, "show_yn", "");
			if(flag) {
				isShow = "Y";
			}
			if(!"".equals(code) && !"".equals(name) && !"".equals(codeLvl) && !"".equals(isShow)) {
				ApiForGetStoreDistrictNewResult.Area area = new ApiForGetStoreDistrictNewResult.Area();
				area.setAreaId(code);
				area.setAreaName(name);
				area.setCodeLvl(codeLvl);
				area.setIsShow(isShow);
				areaList.add(area);
			}
		}
		return apiResult;
	}
	/**
	 * editor NG
	 * @param code
	 * @return
	 */
	private boolean checkCodeIfSpecial(String code) {
		List<String> codes = new ArrayList<String>();
		codes.add("659000");
		codes.add("500200");
		codes.add("469000");
		codes.add("429000");
		codes.add("419000");
		codes.add("500100");
		for (String string : codes) {
			if(code.equals(string)) {
				return true;
			}
		}
		return false;
	}
}
