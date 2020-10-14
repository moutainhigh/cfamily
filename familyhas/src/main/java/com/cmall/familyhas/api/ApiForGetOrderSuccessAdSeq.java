package com.cmall.familyhas.api;

import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.cmall.familyhas.api.result.ApiForGetOrderSuccessAdSeqResult;
import com.cmall.familyhas.api.result.ld.ShowLinkForLDMsgPaySuccess;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootInput;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/** 
* @author Angel Joy
* @Time 2020-8-26 14:10:14 
* @Version 1.0
* <p>Description:</p>
* LD短信支付成功页获取广告配置接口
*/
public class ApiForGetOrderSuccessAdSeq extends RootApiForVersion<ApiForGetOrderSuccessAdSeqResult, RootInput> {

	@Override
	public ApiForGetOrderSuccessAdSeqResult Process(RootInput inputParam, MDataMap mRequestMap) {
		ApiForGetOrderSuccessAdSeqResult result = new ApiForGetOrderSuccessAdSeqResult();
		ShowLinkForLDMsgPaySuccess showLinkOne = new ShowLinkForLDMsgPaySuccess();
		showLinkOne.setChooseFlag(0);
		showLinkOne.setTitle("节目表");
		showLinkOne.setTarget("com_cmall_familyhas_api_ApiForGetTVData");
		Map<String,Object> operateProduct = DbUp.upTable("sc_recommend_product_ldpay").dataSqlOne("SELECT * FROM systemcenter.sc_recommend_product_ldpay limit 1", null);
		if(operateProduct != null && !operateProduct.isEmpty()) {
			ShowLinkForLDMsgPaySuccess showLinkTwo = new ShowLinkForLDMsgPaySuccess();
			if("449748600001".equals(MapUtils.getString(operateProduct, "if_recommend",""))) {//推荐
				showLinkTwo.setChooseFlag(1);
			}else {
				showLinkTwo.setChooseFlag(0);
				showLinkOne.setChooseFlag(1);
			}
			if("4497471600660001".equals(MapUtils.getString(operateProduct, "recommend_type",""))) {//猜你喜欢
				showLinkTwo.setTarget("com_cmall_familyhas_api_ApiDgGetRecommend");
			}else {
				showLinkTwo.setTarget("com_cmall_familyhas_api_ApiRecommendForLdPaySuccess");
			}
			showLinkTwo.setTitle("为您推荐");
			result.getShowLink().add(showLinkTwo);
		}else {
			showLinkOne.setChooseFlag(1);
		}
		result.getShowLink().add(showLinkOne);
		return result;
	}

}
