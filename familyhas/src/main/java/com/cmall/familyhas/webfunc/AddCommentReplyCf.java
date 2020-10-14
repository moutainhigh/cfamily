package com.cmall.familyhas.webfunc;

import com.cmall.ordercenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;

public class AddCommentReplyCf extends RootFunc{

	
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
	
		MWebResult mResult = new MWebResult();
		try{
			if(mResult.upFlagTrue()){
				String replyContent = mDataMap.get("reply_content");
				if(replyContent.equals("0")){
					replyContent = mDataMap.get("custom_reply");
				}
				DbUp.upTable("nc_order_evaluation").dataUpdate(new MDataMap("reply_content",replyContent,"reply_createtime",DateUtil.getNowTime(),"uid",mDataMap.get("zw_f_uid")), "reply_content,reply_createtime", "uid");
			}
		}catch(Exception e){
			e.printStackTrace();
			mResult.inErrorMessage(941901059);
		}
		return mResult;
	}

}
