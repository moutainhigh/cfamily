package com.cmall.familyhas.api;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiOpenStoreCardInput;
import com.cmall.familyhas.api.result.ApiOpenStoreCardResult;
import com.cmall.groupcenter.homehas.RsyncOpenStoreCard;
import com.cmall.groupcenter.homehas.model.RsyncRequestOpenStoreCard;
import com.cmall.groupcenter.homehas.model.RsyncResponseOpenStoreCard;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webmodel.MOauthInfo;

/**
 * 储值卡开卡接口
 * @remark 
 * @author 任宏斌
 * @date 2019年3月12日
 */
public class ApiOpenStoreCard extends RootApiForToken<ApiOpenStoreCardResult,ApiOpenStoreCardInput>{

	@Override
	public ApiOpenStoreCardResult Process(ApiOpenStoreCardInput inputParam, MDataMap mRequestMap) {

		ApiOpenStoreCardResult result = new ApiOpenStoreCardResult();
		
		MOauthInfo oauthInfo = getOauthInfo();
		String loginName = oauthInfo.getLoginName();
		String memberCode = oauthInfo.getUserCode();
		
		//查是否存在cust_id
		PlusModelMemberLevel levelInfo = new LoadMemberLevel().upInfoByCode(new PlusModelMemberLevelQuery(memberCode));
		String custId =  levelInfo.getCustId();
		
		RsyncOpenStoreCard rsyncOpenStoreCard = new RsyncOpenStoreCard();
		RsyncRequestOpenStoreCard upRsyncRequest = rsyncOpenStoreCard.upRsyncRequest();
		upRsyncRequest.setWeb_id(memberCode);
		upRsyncRequest.setMobile(loginName);
		upRsyncRequest.setCard_no(inputParam.getCard_nm());
		upRsyncRequest.setCard_pwd(inputParam.getCard_pwd());
		
		if(StringUtils.isEmpty(custId)) {
			//没有cust_id查是否有默认地址
			MDataMap addr = DbUp.upTable("nc_address").one("address_code",memberCode,"address_default","1");
			if(null == addr) {
				result.setResultCode(916423500);
				result.setResultMessage(bConfig("916423500"));
				return result;
			}else {
				upRsyncRequest.setAddr_1(addr.get("address_province")+addr.get("address_city")+addr.get("address_county"));
				upRsyncRequest.setAddr_2(addr.get("address_street"));
				upRsyncRequest.setSrgn_cd(addr.get("area_code"));
				upRsyncRequest.setZip_no(addr.get("address_postalcode"));
				upRsyncRequest.setCust_nm(addr.get("address_name"));
			}
		}else {
			upRsyncRequest.setCust_id(custId);
		}
		
		if(rsyncOpenStoreCard.doRsync()) {
			RsyncResponseOpenStoreCard upProcessResult = rsyncOpenStoreCard.upProcessResult();
			if(!"true".equals(upProcessResult.getSuccess())){
				result.setResultCode(916423502);
				result.setResultMessage(upProcessResult.getMessage());
			}
		}else {
			result.setResultCode(916423501);
			result.setResultMessage(bConfig("916423501"));
		}
		
		return result;
	}

}
