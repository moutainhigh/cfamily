package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.CdogProductComment;
import com.cmall.groupcenter.model.PageResults;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class CfProductCommentListResult extends RootResultWeb{
	@ZapcomApi(value = "商品评论列表")
	private List<CdogProductComment> productComment = new ArrayList<CdogProductComment>();
	
	@ZapcomApi(value = "好评率")
	private String highPraiseRate = "";
	
	@ZapcomApi(value = "全部评价数量")
	private Integer commentSumCounts = 0;
	
	@ZapcomApi(value = "好评数量")
	private Integer highPraiseCounts = 0;
	
	@ZapcomApi(value = "中评数量")
	private Integer commonPraiseCounts = 0;
	
	@ZapcomApi(value = "差评数量")
	private Integer lowPraiseCounts = 0;
	
	@ZapcomApi(value = "有图数量")
	private Integer pictureCounts = 0;
	
	@ZapcomApi(value = "翻页结果")
	private PageResults paged = new PageResults();
	
	public List<CdogProductComment> getProductComment() {
		return productComment;
	}

	public void setProductComment(List<CdogProductComment> productComment) {
		this.productComment = productComment;
	}

	public PageResults getPaged() {
		return paged;
	}

	public void setPaged(PageResults paged) {
		this.paged = paged;
	}

	public String getHighPraiseRate() {
		return highPraiseRate;
	}

	public void setHighPraiseRate(String highPraiseRate) {
		this.highPraiseRate = highPraiseRate;
	}

	public Integer getCommentSumCounts() {
		return commentSumCounts;
	}

	public void setCommentSumCounts(Integer commentSumCounts) {
		this.commentSumCounts = commentSumCounts;
	}

	public Integer getHighPraiseCounts() {
		return highPraiseCounts;
	}

	public void setHighPraiseCounts(Integer highPraiseCounts) {
		this.highPraiseCounts = highPraiseCounts;
	}

	public Integer getCommonPraiseCounts() {
		return commonPraiseCounts;
	}

	public void setCommonPraiseCounts(Integer commonPraiseCounts) {
		this.commonPraiseCounts = commonPraiseCounts;
	}

	public Integer getLowPraiseCounts() {
		return lowPraiseCounts;
	}

	public void setLowPraiseCounts(Integer lowPraiseCounts) {
		this.lowPraiseCounts = lowPraiseCounts;
	}

	public Integer getPictureCounts() {
		return pictureCounts;
	}

	public void setPictureCounts(Integer pictureCounts) {
		this.pictureCounts = pictureCounts;
	}
}
