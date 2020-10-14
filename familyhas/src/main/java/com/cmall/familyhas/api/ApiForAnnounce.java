package com.cmall.familyhas.api;

import java.util.List;

import com.cmall.familyhas.api.input.ApiForAnnounceInput;
import com.cmall.familyhas.api.result.ApiForAnnounceResult;
import com.cmall.familyhas.api.result.ApiForAnnounceResult.Announce;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.groupcenter.baidupush.core.utility.StringUtility;
import com.cmall.groupcenter.model.MPageData;
import com.cmall.groupcenter.util.DataPaging;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;

/***
 * 家有惠所有的公告
 * @author jlin
 *
 */
public class ApiForAnnounce extends RootApiForManage<ApiForAnnounceResult, ApiForAnnounceInput> {

	public ApiForAnnounceResult Process(ApiForAnnounceInput inputParam, MDataMap mRequestMap) {
		ApiForAnnounceResult re = new ApiForAnnounceResult();
		String where="1=1";
		MDataMap param = new MDataMap();
		
		
		if(StringUtility.isNotNull(inputParam.getUid())){
			where = "mess_code=:mess_code";
			param.put("mess_code", inputParam.getUid());
		}		
		
		String sql="SELECT mess_code,mess_title,mess_note,update_time from hp_message where "+where+" and `status`='449746250001' AND manage_code=:manage_code and mess_category=:mess_category and (flag_show_time='449746250002' or (flag_show_time='449746250001' and show_time<=:now)) ORDER BY update_time desc ";
		
		param.put("manage_code",getManageCode());
		param.put("mess_category", "449716040001");
		param.put("now", DateUtil.getSysDateTimeString());
		
		MPageData pageData = DataPaging.upPageData("hp_message", sql, param, inputParam.getPaging());
		
		List<Announce> announceList = re.getAnnounceList();
		 
		 for (MDataMap map : pageData.getListData()) {
			 Announce announce = new Announce();
			 announce.setId(map.get("mess_code"));
			 announce.setContent(map.get("mess_note"));
			 announce.setTitle(map.get("mess_title"));
			 announce.setUpdate_time(map.get("update_time"));
			 announceList.add(announce);
		}
		 re.setPaged(pageData.getPageResults());
		return re;
	}

}
