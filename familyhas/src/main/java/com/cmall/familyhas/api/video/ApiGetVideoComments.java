package com.cmall.familyhas.api.video;

import com.cmall.familyhas.api.video.input.ApiGetVideoCommentsInput;
import com.cmall.familyhas.api.video.result.ApiGetVideoCommentsResult;
import com.cmall.familyhas.api.video.result.ApiGetVideoCommentsResult.CommentsInfo;
import com.cmall.groupcenter.model.MPageData;
import com.cmall.groupcenter.model.PageOption;
import com.cmall.groupcenter.util.DataPaging;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;

/**
 * 获取视频评论接口
 * @author sunyan
 * @Date 2020-06-16
 *
 */
public class ApiGetVideoComments extends RootApi<ApiGetVideoCommentsResult,ApiGetVideoCommentsInput> {
	
	static final Integer pageSize = 10;
	
	public ApiGetVideoCommentsResult Process(ApiGetVideoCommentsInput input, MDataMap mRequestMap) {
		
		ApiGetVideoCommentsResult result = new ApiGetVideoCommentsResult();
		//分页
		PageOption pageOption = new PageOption();
		pageOption.setLimit(pageSize);
		pageOption.setOffset(input.getPage()==0?1:input.getPage()-1);
		MPageData mPageData = new MPageData();
		MDataMap mWhereMap = new MDataMap();
		String sql = "SELECT vc.*,ms.nickname,ms.avatar from lv_video_comment vc LEFT JOIN membercenter.mc_member_sync ms on vc.comment_member = ms.member_code where vc.video_code = '"+input.getVideoCode()+"' order by vc.create_time desc";
		mPageData = DataPaging.upPageData("lv_video_info", sql, mWhereMap, pageOption);
		
		//总页码
		int cnt = mPageData.getPageResults().getTotal();
		int page = 0;
		if(cnt%pageSize==0){
			page=cnt/pageSize;
		}else {
			page=cnt/pageSize+1;
		}
		result.setTotalPage(page);
		result.setEvaluationNum(cnt);
		
		for(MDataMap map : mPageData.getListData()){
			CommentsInfo info = new CommentsInfo();
			info.setMemberCode(map.get("comment_member").toString());
			info.setNickName(map.get("nickname")==null?"":map.get("nickname").toString());
			info.setAvatar(map.get("avatar")==null?"":map.get("avatar").toString());
			info.setContent(map.get("content")==null?"":map.get("content").toString());
			info.setCreateTime(map.get("create_time")==null?"":map.get("create_time").toString().substring(0, map.get("create_time").toString().length()-2));
			result.getInfoList().add(info);
		}
		
		return result;
	}
}
