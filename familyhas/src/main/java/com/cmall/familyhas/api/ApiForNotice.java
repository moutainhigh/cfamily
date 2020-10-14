package com.cmall.familyhas.api;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForNoticeInput;
import com.cmall.familyhas.api.result.ApiForNoticeResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * app上的通知
 * 
 * @author jlin
 *
 */
public class ApiForNotice extends RootApiForManage<ApiForNoticeResult, ApiForNoticeInput> {

	@Override
	public ApiForNoticeResult Process(ApiForNoticeInput inputParam, MDataMap mRequestMap) {

		ApiForNoticeResult result = new ApiForNoticeResult();
		String notice_show_place = inputParam.getNotice_show_place();

		String sWhere = "notice_status=:notice_status";
		MDataMap mWhereMap = new MDataMap("notice_status", "4497477800020001");
		
		if (StringUtils.isNotBlank(notice_show_place)) {
			sWhere += " and notice_show_place=:notice_show_place";
			mWhereMap.put("notice_show_place", notice_show_place);
		}

		List<MDataMap> list = DbUp.upTable("fh_notice").queryAll("notice_show_place,notice_content", "", sWhere, mWhereMap);

		if (list != null) {
			for (MDataMap mDataMap : list) {
				result.getNoticeList().add(new ApiForNoticeResult.Notice(mDataMap.get("notice_show_place"), mDataMap.get("notice_content")));
			}
		}

		return result;
	}

}
