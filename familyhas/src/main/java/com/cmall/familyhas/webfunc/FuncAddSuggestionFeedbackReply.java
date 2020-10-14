package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 意见反馈的处理
 */
public class FuncAddSuggestionFeedbackReply extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		String createTime = DateUtil.getNowTime();
		
		String content = StringUtils.trimToEmpty(mDataMap.get("content"));
		int zid = NumberUtils.toInt(mDataMap.get("zw_f_zid"));
		
		if(StringUtils.isBlank(content)) {
			mResult.setResultCode(0);
			mResult.setResultMessage("内容不能为空");
			return mResult;
		}
		
		// 超过200后不显示
		if(content.length() > 200) {
			content = content.substring(0,200);
		}
		
		MDataMap map = DbUp.upTable("lc_suggestion_feedback").oneWhere("zid,reply_time", "", "", "zid", zid + "");
		if(map == null) {
			return mResult;
		}
		
		if(StringUtils.isNotBlank(map.get("reply_time"))) {
			mResult.setResultCode(0);
			mResult.setResultMessage("此反馈已经被处理过了");
			return mResult;
		}
		
		map.put("repply_content", content);
		map.put("reply_time", createTime);
		map.put("reply_user", create_user);
		
		DbUp.upTable("lc_suggestion_feedback").dataUpdate(map, "repply_content,reply_time,reply_user", "zid");
		return mResult;
	}
}
