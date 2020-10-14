package com.cmall.familyhas.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import com.cmall.familyhas.api.input.ApiForGetDistrictInput;
import com.cmall.familyhas.api.result.ApiForGetDistrictNewResult;
import com.cmall.familyhas.api.result.ApiForGetDistrictNewResult.Area;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;
import com.srnpr.zapdata.dbdo.DbUp;

public class ApiForGetDistrict extends RootApi<ApiForGetDistrictNewResult, ApiForGetDistrictInput> {

	@Override
	public ApiForGetDistrictNewResult Process(ApiForGetDistrictInput inputParam, MDataMap mRequestMap) {
		ApiForGetDistrictNewResult apiResult = new ApiForGetDistrictNewResult();
		List<Area> level1AreaList = apiResult.getLevel1AreaList();
		List<Area> level2AreaList = apiResult.getLevel2AreaList();
		List<Area> level3AreaList = apiResult.getLevel3AreaList();
		List<Area> level4AreaList = apiResult.getLevel4AreaList();
		String areaCode = inputParam.getAreaCode();
		String parentCode = "";
		String grandParCode = "";
		List<Map<String, Object>> dataSqlPriLibList3 = DbUp.upTable("sc_tmp").dataSqlPriLibList("select * from sc_tmp where code=:code and use_yn = 'Y' and send_yn = 'Y' order by code ", new MDataMap("code",areaCode));
		if(dataSqlPriLibList3==null||dataSqlPriLibList3.size()==0) {
			return apiResult;
		}else {
			parentCode = MapUtils.getString(dataSqlPriLibList3.get(0), "p_code", "");
		}
		//三级
		dataSqlPriLibList3 = DbUp.upTable("sc_tmp").dataSqlPriLibList("select * from sc_tmp where p_code=:code and use_yn = 'Y' and send_yn = 'Y' order by code ", new MDataMap("code",parentCode));
		for (Map<String, Object> map : dataSqlPriLibList3) {
			String code = MapUtils.getString(map, "code", "");
			boolean flag = this.checkCodeIfSpecial(code);
			String name = MapUtils.getString(map, "name", "");
			String codeLvl = MapUtils.getString(map, "code_lvl", "");
			String isShow = MapUtils.getString(map, "show_yn", "");
			parentCode = MapUtils.getString(map, "p_code", "");
			if(flag) {
				isShow = "Y";
			}
			if(!"".equals(code) && !"".equals(name) && !"".equals(codeLvl) && !"".equals(isShow)) {
				ApiForGetDistrictNewResult.Area area = new ApiForGetDistrictNewResult.Area();
				area.setAreaId(code);
				area.setAreaName(name);
				area.setCodeLvl(codeLvl);
				area.setIsShow(isShow);
				if(areaCode.equals(code)) {
					area.setIsChoosed("Y");
				}
				level3AreaList.add(area);
			}
			
		}
		
		List<Map<String, Object>> dataSqlPriLibList4 = DbUp.upTable("sc_tmp").dataSqlPriLibList("select * from sc_tmp where p_code=:code and use_yn = 'Y' and send_yn = 'Y' order by code ", new MDataMap("code",areaCode));
        //四级
		for (Map<String, Object> map : dataSqlPriLibList4) {
			String code = MapUtils.getString(map, "code", "");
			boolean flag = this.checkCodeIfSpecial(code);
			String name = MapUtils.getString(map, "name", "");
			String codeLvl = MapUtils.getString(map, "code_lvl", "");
			String isShow = MapUtils.getString(map, "show_yn", "");
			if(flag) {
				isShow = "Y";
			}
			if(!"".equals(code) && !"".equals(name) && !"".equals(codeLvl) && !"".equals(isShow)) {
				ApiForGetDistrictNewResult.Area area = new ApiForGetDistrictNewResult.Area();
				area.setAreaId(code);
				area.setAreaName(name);
				area.setCodeLvl(codeLvl);
				area.setIsShow(isShow);
				level4AreaList.add(area);
			}
		}
		
		MDataMap one = DbUp.upTable("sc_tmp").one("code",parentCode);
		grandParCode = MapUtils.getString(one, "p_code", "");
		List<Map<String, Object>> dataSqlPriLibList2 = DbUp.upTable("sc_tmp").dataSqlPriLibList("select * from sc_tmp where p_code=:code and use_yn = 'Y' and send_yn = 'Y' order by code ", new MDataMap("code",grandParCode));
        //二级
		for (Map<String, Object> map : dataSqlPriLibList2) {
			String code = MapUtils.getString(map, "code", "");
			boolean flag = this.checkCodeIfSpecial(code);
			String name = MapUtils.getString(map, "name", "");
			String codeLvl = MapUtils.getString(map, "code_lvl", "");
			String isShow = MapUtils.getString(map, "show_yn", "");
			if(flag) {
				isShow = "Y";
			}
			
			if(!"".equals(code) && !"".equals(name) && !"".equals(codeLvl) && !"".equals(isShow)) {
				ApiForGetDistrictNewResult.Area area = new ApiForGetDistrictNewResult.Area();
				area.setAreaId(code);
				area.setAreaName(name);
				area.setCodeLvl(codeLvl);
				area.setIsShow(isShow);
				if(parentCode.equals(code)) {
					area.setIsChoosed("Y");
				}
				level2AreaList.add(area);
			}
		}
		
		List<Map<String, Object>> dataSqlPriLibList1 = DbUp.upTable("sc_tmp").dataSqlPriLibList("select * from sc_tmp where  code_lvl=1 and use_yn = 'Y' and send_yn = 'Y' order by code ", null);
        //一级
		for (Map<String, Object> map : dataSqlPriLibList1) {
			String code = MapUtils.getString(map, "code", "");
			boolean flag = this.checkCodeIfSpecial(code);
			String name = MapUtils.getString(map, "name", "");
			String codeLvl = MapUtils.getString(map, "code_lvl", "");
			String isShow = MapUtils.getString(map, "show_yn", "");
			if(flag) {
				isShow = "Y";
			}
			if(!"".equals(code) && !"".equals(name) && !"".equals(codeLvl) && !"".equals(isShow)) {
				ApiForGetDistrictNewResult.Area area = new ApiForGetDistrictNewResult.Area();
				area.setAreaId(code);
				area.setAreaName(name);
				area.setCodeLvl(codeLvl);
				area.setIsShow(isShow);
				if(grandParCode.equals(code)) {
					area.setIsChoosed("Y");
				}
				level1AreaList.add(area);
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
