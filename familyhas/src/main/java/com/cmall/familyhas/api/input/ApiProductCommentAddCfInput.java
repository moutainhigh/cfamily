package com.cmall.familyhas.api.input;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.AddCommentModel;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootInput;

public class ApiProductCommentAddCfInput extends RootInput {

	@ZapcomApi(value = "发布评论集合", remark = "发布评论集合", demo = "")
	private List<AddCommentModel> comments = new ArrayList<AddCommentModel>();
	@ZapcomApi(value = "评价标识", remark = "1 默认、2 晒图、3 追评、4 修改差评")
	private String flag = "1";
	@ZapcomApi(value = "原评价UID", remark = "flag等于2或3 时提供原评价UID")
	private String uid = "";
	
	@ZapcomApi(value = "是否分享到买家秀", remark = "flag等于1或2 时:0否;1是 (好评有文字+晒单显示) (不满足条件不传)")
	private String is_share = "";

	public String getIs_share() {
		return is_share;
	}

	public void setIs_share(String is_share) {
		this.is_share = is_share;
	}

	public List<AddCommentModel> getComments() {
		return comments;
	}

	public void setComments(List<AddCommentModel> comments) {
		this.comments = comments;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
