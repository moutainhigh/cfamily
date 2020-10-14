package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.DLQCommentModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;

public class ApiGetDLQCommentResult extends RootResult{
	
	@ZapcomApi(value="评论列表")
	private List<DLQCommentModel> commentList = new ArrayList<DLQCommentModel>();


	public List<DLQCommentModel> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<DLQCommentModel> commentList) {
		this.commentList = commentList;
	}

	
	
}
