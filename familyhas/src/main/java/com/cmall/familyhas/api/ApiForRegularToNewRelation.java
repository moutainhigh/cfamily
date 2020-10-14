package com.cmall.familyhas.api;

import java.util.UUID;

import com.cmall.familyhas.api.input.ApiForRegularToNewRelationInput;
import com.cmall.groupcenter.homehas.RsyncRegularToNewRelation;
import com.srnpr.xmassystem.load.LoadMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevel;
import com.srnpr.xmassystem.modelevent.PlusModelMemberLevelQuery;
import com.srnpr.xmassystem.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForRegularToNewRelation extends RootApiForToken<RootResultWeb, ApiForRegularToNewRelationInput> {

	@Override
	public RootResultWeb Process(ApiForRegularToNewRelationInput inputParam, MDataMap mRequestMap) {
		RootResultWeb result = new RootResultWeb();
		String member_code_regular = inputParam.getMember_code();//老用户编号
		String event_code = inputParam.getEvent_code();//活动编号
		String member_code_new = getUserCode();//新用户编号
		//需要检验新用户客户等级
		LoadMemberLevel load = new LoadMemberLevel();
		PlusModelMemberLevelQuery tQuery = new PlusModelMemberLevelQuery();
		tQuery.setCode(member_code_new);
		PlusModelMemberLevel memberNew = load.upInfoByCode(tQuery);
		tQuery.setCode(member_code_regular);
		PlusModelMemberLevel memberRegular = load.upInfoByCode(tQuery);
		String lvl = memberNew.getLevel();
		if(!"10".equals(lvl)) {//10 客户等级为 ‘顾客’，非顾客客户绑定失败
			result.setResultCode(0);
			result.setResultMessage("客户为非顾客等级，绑定失败。如有问题请联系惠家有客服");
			return result;
		}
		//校验新用户是否是24小时之内的新用户
		MDataMap loginInfo = DbUp.upTable("mc_login_info").one("member_code",member_code_new,"manage_code","SI2003");
		if(loginInfo == null) {
			result.setResultCode(0);
			result.setResultMessage("请先登录！");
			return result;
		}
		String createTime = loginInfo.get("create_time");
		String expireTime = DateUtil.addDateHour(createTime, 24);
		if(DateUtil.compareDate1(DateUtil.getSysDateTimeString(),expireTime)) {
			result.setResultCode(0);
			result.setResultMessage("客户不是新用户，绑定失败。如有问题请联系惠家有客服");
			return result;
		}
		//查询是否绑定过关系，如果绑定过关系则绑定失败
		if(DbUp.upTable("sc_event_regular_new_relation").count("regular_member_code",member_code_regular,"new_member_code",member_code_new,"event_code",event_code)>0) {
			result.setResultCode(0);
			result.setResultMessage("已绑定过关系，请勿重复操作，如有问题请联系惠家有客服");
			return result;
		}
		//查询是否绑定过关系，如果绑定过关系则绑定失败
		if(DbUp.upTable("sc_event_regular_new_relation").count("new_member_code",member_code_new,"event_code",event_code)>0) {
			result.setResultCode(0);
			result.setResultMessage("已绑定过关系，请勿重复操作，如有问题请联系惠家有客服");
			return result;
		}
		//查询是否绑定过关系，如果绑定过关系则绑定失败
		if(member_code_regular.equals(member_code_new)) {
			result.setResultCode(0);
			result.setResultMessage("自己绑定自己无效");
			return result;
		}
		boolean flag = this.checkLdRelation(memberNew,memberRegular);
		if(flag) {
			MDataMap insert = new MDataMap();
			insert.put("uid", UUID.randomUUID().toString().replace("-", "").trim());
			insert.put("event_code", event_code);
			insert.put("regular_member_code", member_code_regular);
			insert.put("new_member_code", member_code_new);
			insert.put("create_time", DateUtil.getSysDateTimeString());
			DbUp.upTable("sc_event_regular_new_relation").dataInsert(insert);
		}else {
			result.setResultCode(0);
			result.setResultMessage("绑定失败");
		}
		return result;
	}

	/**
	 * 调用AppInterFace接口，查询绑定关系
	 * @param memberNew
	 * @param memberRegular
	 * @return true ：未绑定过，绑定成功，false:绑定失败
	 */
	private boolean checkLdRelation(PlusModelMemberLevel memberNew, PlusModelMemberLevel memberRegular) {
		RsyncRegularToNewRelation relation = new RsyncRegularToNewRelation();
		relation.buildRequest(memberRegular.getMemberCode(), memberNew.getMemberCode());
		return relation.doRsync();
	}

}
