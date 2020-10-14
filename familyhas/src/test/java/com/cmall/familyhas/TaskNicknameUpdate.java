package com.cmall.familyhas;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.ApiUserNickNameUpdate;
import com.cmall.familyhas.api.input.UserNickNameUpdateInput;
import com.cmall.familyhas.api.result.UserNickNameUpdateResult;

public class TaskNicknameUpdate implements Callable<Integer> {

	private List<Map<String, Object>> list;
	private ApiUserNickNameUpdate u;
	
	public TaskNicknameUpdate(List<Map<String, Object>> list, ApiUserNickNameUpdate u) {
		this.list = list;
		this.u = u;
	}

	public Integer call() throws Exception {
		for(Map<String, Object> m : list){
			UserNickNameUpdateInput e = new UserNickNameUpdateInput();
			e.setMemberCode(m.get("MemberCode").toString());
			e.setNickname(m.get("NickName") == null ? "" : m.get("NickName").toString());
			e.setMemberAvatar(m.get("Avatar") == null ? "" : m.get("Avatar").toString());
			UserNickNameUpdateResult r = u.Process(e, null);
			System.out.println(r.getStatus());    //  + "|" + r.getMsg()
		}
		
		return 100;
	}
}














