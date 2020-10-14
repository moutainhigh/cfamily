package com.cmall.familyhas.api;

import com.cmall.dborm.txmodel.membercenter.McExtendInfoStar;
import com.cmall.membercenter.memberdo.ScoredEnumer;
import com.cmall.membercenter.model.HomePoolMemberChangeInput;
import com.cmall.membercenter.model.MemberChangeResult;
import com.cmall.membercenter.support.MemberInfoSupport;
import com.cmall.membercenter.support.ScoredSupport;
import com.cmall.membercenter.txservice.TxMemberForStar;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

/**
 * 修改用户信息
 * 
 * @author dyc
 * 
 */
public class ApiForPcChangeMemberInfo extends
		RootApiForToken<MemberChangeResult, HomePoolMemberChangeInput> {

	public MemberChangeResult Process(
			HomePoolMemberChangeInput inputParam, MDataMap mRequestMap) {

		MemberChangeResult memberChangeResult = new MemberChangeResult();
		MemberInfoSupport memberInfoSupport = new MemberInfoSupport();
		String nickName = inputParam.getNickname();
		RootResultWeb webResult = memberInfoSupport.checkNickName(nickName, getUserCode());
		if(webResult.getResultCode() == 934105114){//用户昵称已存在
			memberChangeResult.inErrorMessage(webResult.getResultCode());
			return memberChangeResult;
		}
		
		if (memberChangeResult.upFlagTrue()) {
			
			McExtendInfoStar mcExtendInfoStar = new McExtendInfoStar();
			mcExtendInfoStar.setNickname(inputParam.getNickname());
			mcExtendInfoStar.setMemberSex(inputParam.getGender());
			mcExtendInfoStar.setBirthday(inputParam.getBirthday());
			mcExtendInfoStar.setMobilePhone(inputParam.getMobile());
			mcExtendInfoStar.setEmail(inputParam.getEmail());
			mcExtendInfoStar.setMemberAvatar(inputParam.getHeadPic());
			
			TxMemberForStar memberService = BeansHelper
					.upBean("bean_com_cmall_membercenter_txservice_TxMemberForStar");
			memberService.updateMemberInfo(mcExtendInfoStar, getUserCode());
			memberChangeResult.setUser(memberInfoSupport.upMemberInfo(getUserCode()));
			
			memberChangeResult.setScored(new ScoredSupport().ChangeScored(getUserCode(), ScoredEnumer.MemberChangeInfo));
							
		}
		return memberChangeResult;

	}

}
