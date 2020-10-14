package com.cmall.familyhas.webfunc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 会员管理提交修改
 * @author liqt
 *
 */
public class FuncEditStar extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		
		String nickName = mDataMap.get("zw_f_nickname");
		Pattern pattern = Pattern.compile("^[\u4E00-\u9FA5A-Za-z0-9-_]+$");
	    Matcher matcher = pattern.matcher(nickName);
		if(matcher.matches() && nickName.length()>=2 && nickName.length()<=7){
			MDataMap mDataMap2 = new MDataMap();
			mDataMap2.put("nickname", nickName);
			mDataMap2.put("uid",  mDataMap.get("zw_f_uid"));
			mDataMap2.put("member_avatar", mDataMap.get("zw_f_member_avatar"));
			DbUp.upTable("mc_extend_info_star").dataUpdate(mDataMap2, "nickname,member_avatar", "uid");
		}else {
			mResult.inErrorMessage(916422122);
		}
		return mResult;
	}

}
