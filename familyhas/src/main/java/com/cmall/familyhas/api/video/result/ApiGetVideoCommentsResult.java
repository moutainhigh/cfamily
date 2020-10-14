package com.cmall.familyhas.api.video.result;

import java.util.ArrayList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetVideoCommentsResult extends RootResult {

	@ZapcomApi(value="分页总页码")
	private int totalPage = 1;
	
	@ZapcomApi(value="评论总数")
	private int evaluationNum = 0;
	
	@ZapcomApi(value="短视频评论列表")
	private List<CommentsInfo> infoList=new ArrayList<ApiGetVideoCommentsResult.CommentsInfo>();
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getEvaluationNum() {
		return evaluationNum;
	}

	public void setEvaluationNum(int evaluationNum) {
		this.evaluationNum = evaluationNum;
	}

	public List<CommentsInfo> getInfoList() {
		return infoList;
	}

	public void setInfoList(List<CommentsInfo> infoList) {
		this.infoList = infoList;
	}

	public static class CommentsInfo {
		@ZapcomApi(value="评论人昵称")
		private String nickName="";
		
		@ZapcomApi(value="评论人编号")
		private String memberCode="";
		
		@ZapcomApi(value="评论人头像")
		private String avatar="";
		
		@ZapcomApi(value="评论内容")
		private String content="";	
		
		@ZapcomApi(value="评论时间")
		private String createTime="";


		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getMemberCode() {
			return memberCode;
		}

		public void setMemberCode(String memberCode) {
			this.memberCode = memberCode;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
	}	
		
	
	
}
