package com.cmall.familyhas.api.video;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.PageInput;
import com.cmall.familyhas.api.video.result.ApiGetVideoListPersonalResult;
import com.cmall.familyhas.api.video.result.ApiGetVideoListPersonalResult.VideoInfo;
import com.cmall.groupcenter.model.MPageData;
import com.cmall.groupcenter.model.PageOption;
import com.cmall.groupcenter.util.DataPaging;
import com.srnpr.xmassystem.very.PlusVeryImage;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * 获取个人视频列表接口
 * @author sunyan
 * @Date 2020-06-16
 *
 */
public class ApiGetVideoList extends RootApiForVersion<ApiGetVideoListPersonalResult,PageInput> {
	
	static final Integer pageSize = 10;
	
	public ApiGetVideoListPersonalResult Process(PageInput input, MDataMap mRequestMap) {
		
		ApiGetVideoListPersonalResult result = new ApiGetVideoListPersonalResult();
		String memberCode = "";
		if(getFlagLogin()) {
			memberCode = getOauthInfo().getUserCode();
		}
		
		//分页
		PageOption pageOption = new PageOption();
		pageOption.setLimit(pageSize);
		pageOption.setOffset(input.getPage()==0?1:input.getPage()-1);
		
		MPageData mPageData = new MPageData();
		MDataMap mWhereMap = new MDataMap();
		String sql = "";
		if(StringUtils.isNotBlank(memberCode)){
			sql = "SELECT vi.*,ms.nickname,ms.avatar,case when pd.zid is not null then 'Y' else 'N' END is_praise from lv_video_info vi LEFT JOIN membercenter.mc_member_sync ms on vi.member_code = ms.member_code LEFT JOIN lv_praise_detail pd on pd.video_code = vi.video_code AND pd.member_code = '"+memberCode+"' where vi.is_delete = 0 and vi.status = 4497471600600003 order by vi.create_time desc";
		}else{
			sql = "SELECT vi.*,ms.nickname,ms.avatar from lv_video_info vi LEFT JOIN membercenter.mc_member_sync ms on vi.member_code = ms.member_code where vi.is_delete = 0 and vi.status = 4497471600600003 order by vi.create_time desc";
		}		
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
		for(MDataMap map : mPageData.getListData()){
			VideoInfo info = new VideoInfo();
			info.setCommentNum(map.get("comment_num")==null?"0":map.get("comment_num"));
			info.setPraiseNum(map.get("praise_num")==null?"0":map.get("praise_num"));
			info.setScanNum(map.get("watch_num")==null?"0":map.get("watch_num"));
			info.setShareNum(map.get("share_num")==null?"0":map.get("share_num"));
			BigDecimal temp = new BigDecimal(10000);
			if(Integer.parseInt(info.getCommentNum())>=10000){
				info.setCommentNum(new BigDecimal(info.getCommentNum()).divide(temp, 1, BigDecimal.ROUND_DOWN).toString()+"w");
			}
			if(Integer.parseInt(info.getScanNum())>=10000){
				info.setScanNum(new BigDecimal(info.getScanNum()).divide(temp, 1, BigDecimal.ROUND_DOWN).toString()+"w");
			}
			if(Integer.parseInt(info.getPraiseNum())>=10000){
				info.setPraiseNum(new BigDecimal(info.getPraiseNum()).divide(temp, 1, BigDecimal.ROUND_DOWN).toString()+"w");
			}
			if(Integer.parseInt(info.getShareNum())>=10000){
				info.setShareNum(new BigDecimal(info.getShareNum()).divide(temp, 1, BigDecimal.ROUND_DOWN).toString()+"w");
			}
			info.setCreateTime(map.get("create_time")==null?"":map.get("create_time").toString().substring(0, map.get("create_time").toString().length()-2));
			info.setHeadPic(map.get("avatar"));
			if(StringUtils.isNotBlank(memberCode)){
				info.setIsPraise(map.get("is_praise"));
			}
			info.setNickName(map.get("nickname")==null?"":map.get("nickname"));
			info.setStatus(map.get("status"));
			info.setVideoCode(map.get("video_code"));
			info.setVideoPic(map.get("img_url"));
			info.setVideoTitle(map.get("title"));
			info.setVideoUrl(map.get("link_url"));
			info.setMemberCode(map.get("member_code"));
			try {
				PlusVeryImage p = new PlusVeryImage();
				Map<String,Integer> retMap = p.getImagesKg(info.getVideoPic());
				info.setWidth(retMap.get("width")*1L);
				info.setHeight(retMap.get("height")*1L); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			result.getInfoList().add(info);
		}
		
		return result;
	}
}
