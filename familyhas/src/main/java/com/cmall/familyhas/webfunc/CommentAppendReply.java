package com.cmall.familyhas.webfunc;

import org.apache.commons.lang3.StringUtils;

import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 追评的回复
 */
public class CommentAppendReply extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();
		String replyContent = mDataMap.get("reply_content");
		if(StringUtils.isBlank(replyContent)){
			replyContent = mDataMap.get("custom_reply");
		}
		
		if(StringUtils.isBlank(replyContent)){
			mResult.setResultCode(0);
			mResult.setResultMessage("回复不能为空");
		}else{
			
			MDataMap map = new MDataMap();
			map.put("reply_content", replyContent);
			map.put("reply_createtime", DateUtil.getNowTime());
			map.put("zid", mDataMap.get("zw_f_zid"));
			
			DbUp.upTable("nc_order_evaluation_append").dataUpdate(map, "reply_content,reply_createtime", "zid");
		}

		return mResult;
	}

}
