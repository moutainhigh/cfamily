package com.cmall.familyhas.api.video.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetVideoListPersonalResult extends RootResult {

	@ZapcomApi(value="分页总页码")
	private int totalPage = 1;
	
	@ZapcomApi(value="是否是自己的个人中心",remark="Y/N")
	private String isOwn="N";
	
	@ZapcomApi(value="短视频列表")
	private List<VideoInfo> infoList=new ArrayList<ApiGetVideoListPersonalResult.VideoInfo>();
	
	
	public int getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}


	public String getIsOwn() {
		return isOwn;
	}


	public void setIsOwn(String isOwn) {
		this.isOwn = isOwn;
	}


	public List<VideoInfo> getInfoList() {
		return infoList;
	}


	public void setInfoList(List<VideoInfo> infoList) {
		this.infoList = infoList;
	}


	public static class VideoInfo {

		@ZapcomApi(value="昵称")
		private String nickName="";	
		
		@ZapcomApi(value="用户编号")
		private String memberCode="";	
		
		@ZapcomApi(value="头像")
		private String headPic="";
		
		@ZapcomApi(value="发布时间")
		private String createTime="";
		
		@ZapcomApi(value="视频编号")
		private String videoCode="";
		
		@ZapcomApi(value="视频标题")
		private String videoTitle="";
		
		@ZapcomApi(value="视频播放地址")
		private String videoUrl="";
		
		@ZapcomApi(value="视频播封面图")
		private String videoPic="";
		
		@ZapcomApi(value="状态",remark="4497471600600001:待审核,4497471600600002:审核失败,4497471600600003:已发布,4497471600600004:暂停发布")
		private String status="";
		
		@ZapcomApi(value="观看数")
		private String scanNum;
		
		@ZapcomApi(value="获赞数")
		private String praiseNum;							
		
		@ZapcomApi(value="评论数")
		private String commentNum;
		
		@ZapcomApi(value="分享数")
		private String shareNum;
		
		@ZapcomApi(value="是否关注",remark="Y/N")
		private String isCare="N";
		
		@ZapcomApi(value="是否点赞",remark="Y/N")
		private String isPraise="N";
		
		@ZapcomApi(value="封面图图片宽")
	    private long width;
		
		@ZapcomApi(value="封面图图片高")
	    private long height;

		public String getShareNum() {
			return shareNum;
		}

		public void setShareNum(String shareNum) {
			this.shareNum = shareNum;
		}

		public String getIsPraise() {
			return isPraise;
		}

		public void setIsPraise(String isPraise) {
			this.isPraise = isPraise;
		}

		public String getMemberCode() {
			return memberCode;
		}

		public void setMemberCode(String memberCode) {
			this.memberCode = memberCode;
		}

		public long getWidth() {
			return width;
		}

		public void setWidth(long width) {
			this.width = width;
		}

		public long getHeight() {
			return height;
		}

		public void setHeight(long height) {
			this.height = height;
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getHeadPic() {
			return headPic;
		}

		public void setHeadPic(String headPic) {
			this.headPic = headPic;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public String getVideoCode() {
			return videoCode;
		}

		public void setVideoCode(String videoCode) {
			this.videoCode = videoCode;
		}

		public String getVideoTitle() {
			return videoTitle;
		}

		public void setVideoTitle(String videoTitle) {
			this.videoTitle = videoTitle;
		}

		public String getVideoUrl() {
			return videoUrl;
		}

		public void setVideoUrl(String videoUrl) {
			this.videoUrl = videoUrl;
		}

		public String getVideoPic() {
			return videoPic;
		}

		public void setVideoPic(String videoPic) {
			this.videoPic = videoPic;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getScanNum() {
			return scanNum;
		}

		public void setScanNum(String scanNum) {
			this.scanNum = scanNum;
		}

		public String getPraiseNum() {
			return praiseNum;
		}

		public void setPraiseNum(String praiseNum) {
			this.praiseNum = praiseNum;
		}

		public String getCommentNum() {
			return commentNum;
		}

		public void setCommentNum(String commentNum) {
			this.commentNum = commentNum;
		}

		public String getIsCare() {
			return isCare;
		}

		public void setIsCare(String isCare) {
			this.isCare = isCare;
		}
				
		
	}
	
	
	
}
