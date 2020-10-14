package com.cmall.familyhas.api;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.UserNickNameUpdateInput;
import com.cmall.familyhas.api.result.UserNickNameUpdateResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

/**
 * @description: 与张威的用户中心联通，更新用户的头像，昵称。
 * 	更新membercenter.mc_extend_info_star表
 * 
 * @author Yangcl
 * @date 2017年4月14日 下午4:08:12 
 * @version 1.0.0
 */
public class ApiUserNickNameUpdate extends RootApiForManage<UserNickNameUpdateResult, UserNickNameUpdateInput>{


	public UserNickNameUpdateResult Process(UserNickNameUpdateInput in, MDataMap mRequestMap) {
		UserNickNameUpdateResult result = new UserNickNameUpdateResult();
		if(StringUtils.isBlank(in.getMemberCode())){
			result.setStatus("error");
			result.setMsg("member code 不可为空"); 
		}else if(StringUtils.isBlank(in.getNickname()) && StringUtils.isBlank(in.getMemberAvatar())){
			result.setStatus("success");
			result.setMsg("昵称和头像为空"); 
		}else{
			MDataMap mDataMap = new MDataMap();
			String sql = "update mc_member_sync set";
			if(StringUtils.isNotBlank(in.getNickname())){
				mDataMap.put("nickname", in.getNickname());
				sql += " nickname=:nickname,"; 
			}
			if(StringUtils.isNotBlank(in.getMemberAvatar())){
				mDataMap.put("avatar", in.getMemberAvatar());
				sql += " avatar=:avatar,"; 
			}
			sql = sql.substring(0 , sql.length() -1);
			mDataMap.put("memberCode", in.getMemberCode());
			sql += " where member_code =:memberCode"; 
			
			int flag = DbUp.upTable("mc_member_sync").dataExec(sql, mDataMap);
			if(flag == 1){
				result.setStatus("success");
				result.setMsg("更新成功!"); 
			}else{
				result.setStatus("error");
				result.setMsg("数据更新失败!"); 
			}
			
			//同步更新外呼用户微信绑定信息
			int count = DbUp.upTable("mc_member_wechat_bound").count("member_code",in.getMemberCode());
			if(count>0) {
				MDataMap mDataMap1 = new MDataMap();
				mDataMap1.put("is_bound", "1");
				mDataMap1.put("member_code", in.getMemberCode());
				mDataMap1.put("flag", "1");
				mDataMap1.put("nick_name_wechat", in.getNickname()==null?"":in.getNickname());
				mDataMap1.put("registe_time", DateUtil.getNowTime());
				DbUp.upTable("mc_member_wechat_bound").dataUpdate(mDataMap1, "is_bound,nick_name_wechat,registe_time", "member_code");
				DbUp.upTable("oc_coupon_wechat_validate").dataUpdate(mDataMap1, "flag", "member_code");
			}
		}
		return result;
	}
	
	
}










