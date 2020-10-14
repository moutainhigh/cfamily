package com.cmall.familyhas.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;

/**
 * 定时生成地址临时表
 * @remark 
 * @author 任宏斌
 * @date 2019年8月20日
 */
public class JobForDealAddressInfo extends RootJob{

	@Override
	public void doExecute(JobExecutionContext context) {
		//清空临时表
		DbUp.upTable("sc_tmp_tmp").dataExec("truncate systemcenter.sc_tmp_tmp", new MDataMap());
		//重新生成临时数据
		List<MDataMap> queryByWhere = DbUp.upTable("sc_tmp").queryByWhere("show_yn", "Y", "code_lvl", "4");
		if(null != queryByWhere && !queryByWhere.isEmpty()) {
			for (MDataMap mDataMap : queryByWhere) {
				MDataMap addressInfo = getAddressInfo(mDataMap.get("code"));
				DbUp.upTable("sc_tmp_tmp").insert(
					"first_code", null == addressInfo.get("firstCode") ? "" : addressInfo.get("firstCode"), 
					"first_name", null == addressInfo.get("firstName") ? "" : addressInfo.get("firstName"), 
					"second_code", null == addressInfo.get("secondCode") ? "" : addressInfo.get("secondCode"), 
					"second_name", null == addressInfo.get("secondName") ? "" : addressInfo.get("secondName"),
					"third_code", null == addressInfo.get("thirdCode") ? "" : addressInfo.get("thirdCode"), 
					"third_name", null == addressInfo.get("thirdName") ? "" : addressInfo.get("thirdName"),
					"fourth_code", null == addressInfo.get("fourthCode") ? "" : addressInfo.get("fourthCode"), 
					"fourth_name", null == addressInfo.get("fourthName") ? "" : addressInfo.get("fourthName"));
			}
		}
	}
	
	private MDataMap getAddressInfo(String areaCode) {
		if(StringUtils.isEmpty(areaCode)) return null;
		
		MDataMap result = new MDataMap();
		
		List<MDataMap> list = new ArrayList<MDataMap>();
		// 递归获取本级以及父级地址
		getHjyAddressAndParent(list, areaCode);
		
		for (int i = 0; i < list.size(); i ++) {
			if("Y".equals(list.get(i).get("show_yn"))){
				switch (list.get(i).get("code_lvl")) {
				case "1":
					result.put("firstCode", list.get(i).get("code"));
					result.put("firstName", list.get(i).get("name"));
					break;
				case "2":
					result.put("secondCode", list.get(i).get("code"));
					result.put("secondName", list.get(i).get("name"));
					break;
				case "3":
					result.put("thirdCode", list.get(i).get("code"));
					result.put("thirdName", list.get(i).get("name"));
					break;
				case "4":
					result.put("fourthCode", list.get(i).get("code"));
					result.put("fourthName", list.get(i).get("name"));
					break;
				default:
					break;
				}
			}
		}
		return result;
	}
	
	private void getHjyAddressAndParent(List<MDataMap> list, String areaCode) {
		// 如果地址编码已经获取过了则直接返回
		for(MDataMap map : list) {
			if(map.get("code").equals(areaCode)) {
				return;
			}
		}
		
		List<MDataMap> res = DbUp.upTable("sc_tmp").queryAll("code,name,p_code,code_lvl,show_yn", "", "", new MDataMap("code", areaCode));
		if(!res.isEmpty()) {
			list.add(res.get(0));
			
			// 如果有父级则递归获取
			if(StringUtils.isNotBlank(res.get(0).get("p_code"))) {
				getHjyAddressAndParent(list,res.get(0).get("p_code"));
			}
		}
	}

}
