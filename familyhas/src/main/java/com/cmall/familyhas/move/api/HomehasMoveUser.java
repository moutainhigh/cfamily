package com.cmall.familyhas.move.api;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.move.api.input.UserInfo;
import com.cmall.familyhas.move.api.input.UserResult;
import com.cmall.membercenter.enumer.ELoginType;
import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.membercenter.model.MLoginInputHomehas;
import com.cmall.membercenter.model.MReginsterResult;
import com.cmall.membercenter.txservice.TxMemberForHomeHas;
import com.srnpr.zapcom.basehelper.BeansHelper;
import com.srnpr.zapcom.basehelper.SecrurityHelper;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topdo.TopConst;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;
import com.srnpr.zapweb.webapi.RootResultWeb;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.websupport.OauthSupport;

public class HomehasMoveUser extends RootApiForManage<UserResult, UserInfo> {

	public UserResult Process(UserInfo inputParam, MDataMap mRequestMap) {

		UserResult userResult = new UserResult();

		MDataMap mUserMap = DbUp.upTable("mc_login_info").one("login_name",
				inputParam.getLoginName(), "manage_code", getManageCode());

		if (StringUtils.isBlank(inputParam.getHomehasCode())
				|| inputParam.getHomehasCode().equals("null")
				|| inputParam.getHomehasCode().equals("-1")) {
			inputParam.setHomehasCode("");
		}

		if (StringUtils.isBlank(inputParam.getOldCode())
				|| inputParam.getOldCode().equals("null")
				|| inputParam.getOldCode().equals("-1")) {
			inputParam.setOldCode("");
		}

		if (StringUtils.isBlank(inputParam.getLoginPassword())
				|| inputParam.getLoginPassword().equals("null")) {
			inputParam.setLoginPassword("");
		} else {

			try {
				inputParam.setLoginPassword(URLDecoder.decode(
						inputParam.getLoginPassword(),
						TopConst.CONST_BASE_ENCODING));
			} catch (UnsupportedEncodingException e) {
				inputParam.setLoginPassword("");
			}
		}

		// 特定判断 如果家有编号在系统中已存在 则将家有编号置为空
		if (StringUtils.isNotEmpty(inputParam.getHomehasCode())) {
			if (DbUp.upTable("mc_extend_info_homehas").count("old_code",
					inputParam.getHomehasCode()) > 0) {
				inputParam.setHomehasCode("");
			}
		}

		// 如果存在用户 则取出homehas表
		if (mUserMap != null && mUserMap.size() > 0) {

			// 如果这边密码为空 则用同步过来的密码更新
			if (StringUtils.isEmpty(mUserMap.get("login_pass"))
					&& StringUtils.isNotEmpty(inputParam.getLoginPassword())) {

				String sPassword = SecrurityHelper.MD5Secruity(inputParam
						.getLoginPassword());
				mUserMap.put("login_pass", sPassword);
				DbUp.upTable("mc_login_info").dataUpdate(mUserMap,
						"login_pass", "zid");

			}

			// 开始取出家有扩展信息表
			MDataMap mHasMap = DbUp.upTable("mc_extend_info_homehas").one(
					"member_code", mUserMap.get("member_code"));

			if (mHasMap != null && mHasMap.size() > 0) {

				// 判断如果编号为空或者级别不对应 则修改编号
				if (StringUtils.isEmpty(mHasMap.get("old_code"))
						|| !StringUtils.equals(mHasMap.get("member_sign"),
								inputParam.getMemberSign())) {

					mHasMap.put("old_code", inputParam.getOldCode());
					mHasMap.put("member_sign", inputParam.getMemberSign());
					DbUp.upTable("mc_extend_info_homehas").dataUpdate(mHasMap,
							"old_code,member_sign", "zid");

				}

			} else {

				// 插入扩展信息表
				MDataMap mExtendMap = new MDataMap();
				mExtendMap.inAllValues("member_code",
						mUserMap.get("member_code"), "old_code",
						inputParam.getOldCode());
				DbUp.upTable("mc_extend_info_homehas").dataInsert(mExtendMap);
			}

			userResult.setMemberCode(mUserMap.get("member_code"));

		} else {

			TxMemberForHomeHas txMemberForHomeHas = BeansHelper
					.upBean("bean_com_cmall_membercenter_txservice_TxMemberForHomeHas");

			MLoginInputHomehas mLoginInputHomehas = new MLoginInputHomehas();

			mLoginInputHomehas.setHomeHasCode(inputParam.getHomehasCode());

			mLoginInputHomehas.setLoginGroup(MemberConst.LOGIN_GROUP_DEFAULT);
			mLoginInputHomehas.setLoginName(inputParam.getLoginName());
			mLoginInputHomehas.setLoginPassword(inputParam.getLoginPassword());
			// mLoginInputHomehas.setLoginType(ELoginType.);
			mLoginInputHomehas.setManageCode(getManageCode());

			mLoginInputHomehas.setMemberSign(inputParam.getMemberSign());
			mLoginInputHomehas.setOldCode(inputParam.getOldCode());

			MReginsterResult mReginsterResult = txMemberForHomeHas
					.createMemberInfo(mLoginInputHomehas);

			userResult.setMemberCode(mReginsterResult.getMemberInfo()
					.getMemberCode());

		}

		// 判断如果用户编号不为空 则取出token信息
		if (StringUtils.isNotEmpty(userResult.getMemberCode())) {
			OauthSupport oauthSupport = new OauthSupport();
			String sTokenString = oauthSupport.insertOauth(
					userResult.getMemberCode(), getManageCode(),
					inputParam.getLoginName(), "90d", "");

			userResult.setToken(sTokenString);
		}

		return userResult;

	}

}
