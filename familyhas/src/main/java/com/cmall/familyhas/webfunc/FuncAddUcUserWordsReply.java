package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 非工作时间用户留言的处理
 */
public class FuncAddUcUserWordsReply extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		String createTime = DateUtil.getNowTime();
		
		String content = StringUtils.trimToEmpty(mDataMap.get("content"));
		String uid = mDataMap.get("zw_f_uid");
		
		if(StringUtils.isBlank(content)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("内容不能为空");
			return mResult;
		}
		
		// 超过200后不显示
		if(content.length() > 200) {
			content = content.substring(0,200);
		}
		
		MDataMap map = DbUp.upTable("uc_user_words").oneWhere("uid,deal_time", "", "", "uid", uid);
		if(map == null) {
			return mResult;
		}
		
		if(StringUtils.isNotBlank(map.get("deal_time"))) {
			mResult.setResultCode(0);
			mResult.setResultMessage("此留言已经被处理过了");
			return mResult;
		}
		
		map.put("deal_content", content);
		map.put("deal_time", createTime);
		map.put("deal_user", create_user);
		
		DbUp.upTable("uc_user_words").dataUpdate(map, "deal_content,deal_time,deal_user", "uid");
		return mResult;
	}
}
