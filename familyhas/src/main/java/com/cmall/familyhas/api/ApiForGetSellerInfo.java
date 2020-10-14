package com.cmall.familyhas.api;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForGetSellerInfoInput;
import com.cmall.familyhas.api.result.ApiForGetSellerInfoResult;
import com.srnpr.xmassystem.load.LoadProductInfo;
import com.srnpr.xmassystem.load.LoadSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelProductQuery;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerInfo;
import com.srnpr.xmassystem.modelproduct.PlusModelSellerQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForManage;

/***
 * 根据商品编号查询商户信息
 */
public class ApiForGetSellerInfo extends RootApiForManage<ApiForGetSellerInfoResult, ApiForGetSellerInfoInput> {

	public ApiForGetSellerInfoResult Process(ApiForGetSellerInfoInput inputParam,MDataMap mRequestMap) {
		ApiForGetSellerInfoResult result = new ApiForGetSellerInfoResult();
		PlusModelProductInfo productInfo = new LoadProductInfo().upInfoByCode(new PlusModelProductQuery(inputParam.getProductCode()));

		if(productInfo == null){
			result.setResultCode(0);
			result.setResultMessage("商品数据不存在");
			return result;
		}
		
		String smallSellerCode = productInfo.getSmallSellerCode();
		// LD商品取固定的商户编号
		if(StringUtils.isBlank(smallSellerCode) || "SI2003".equals(smallSellerCode)){
			smallSellerCode = bConfig("familyhas.small_seller_code_ld");
		}
		
		// 商户信息
		PlusModelSellerInfo sellerInfo = new LoadSellerInfo().upInfoByCode(new PlusModelSellerQuery(StringUtils.trimToEmpty(smallSellerCode)));
		
		result.setSmallSellerCode(smallSellerCode);
		if(sellerInfo != null){
			result.setUcSellerType(sellerInfo.getUc_seller_type());
			result.setOrgCodePic(sellerInfo.getOrgCodePic());
			result.setTaxRegCertCopy(sellerInfo.getTaxRegCertCopy());
			result.setBankAccountCertPhotoCopy(sellerInfo.getBankAccountCertPhotoCopy());
			result.setBizLicensePic(sellerInfo.getBizLicensePic());
			result.setLegalPersonIDCardOppPic(sellerInfo.getLegalPersonIDCardOppPic());
		}
		
		return result;
	}

}
