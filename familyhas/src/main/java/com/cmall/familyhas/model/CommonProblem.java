package com.cmall.familyhas.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
/**   
 * 	常见问题
*    ligj
*/
public class CommonProblem  {
//	@ZapcomApi(value = "商品编号")
//	private String productCode = "";
	@ZapcomApi(value = "标题")
	private String title = "";
	@ZapcomApi(value = "内容")
	private String content = "";
//	@ZapcomApi(value = "商家编号")
//	private String sellerCode = "";
//	@ZapcomApi(value = "位置排序")
//	private String sort = "0";
//	@ZapcomApi(value = "创建时间")
//	private String createTime = "";
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}

