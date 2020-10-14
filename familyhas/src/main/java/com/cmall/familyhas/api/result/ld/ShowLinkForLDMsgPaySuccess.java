package com.cmall.familyhas.api.result.ld;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.baseclass.BaseClass;

/**
 * @author Angel Joy
 * @Time 2020-8-17 15:41:53
 * @Version 1.0
 *          <p>
 *          Description:
 *          </p>
 *          支付成功推荐页调用内容
 */
public class ShowLinkForLDMsgPaySuccess extends BaseClass {

	@ZapcomApi(value = "是否选中", remark = "选中：1，未选中：0，选中配置靠前。", demo = "0")
	private int chooseFlag = 0;

	@ZapcomApi(value = "调用接口target", remark = "com_cmall_familyhas_api_ApiRecommendForLdPaySuccess:为您推荐配置接口，"
			+ "com_cmall_familyhas_api_ApiDgGetRecommend：达观猜你喜欢接口，"
			+ "com_cmall_familyhas_api_ApiForGetTVData：TV直播列表", demo = "")
	private String target = "";

	@ZapcomApi(value = "标题", remark = "", demo = "节目表、为您推荐")
	private String title = "";

	@ZapcomApi(value = "订单购买商品", remark = "调用猜你喜欢时，可能会用到此参数", demo = "80169999")
	private String productCode = "";

	public int getChooseFlag() {
		return chooseFlag;
	}

	public void setChooseFlag(int chooseFlag) {
		this.chooseFlag = chooseFlag;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

}
