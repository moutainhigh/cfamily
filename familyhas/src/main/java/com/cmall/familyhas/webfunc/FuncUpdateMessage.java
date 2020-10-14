package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;
/***
 * 重发短信
 * @author jlin
 *
 */
public class FuncUpdateMessage extends FuncEdit {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		
		MDataMap mEditMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String uid=mEditMaps.get("uid");
		mEditMaps.put("flag_finish", "0");
		mEditMaps.put("finish_time", "");
		
		
		MDataMap message=DbUp.upTable("za_message").one("uid",uid);
		
		if("1".equals(message.get("del_flag"))){
			mResult.inErrorMessage(916421161);
			return mResult;
		}
		
		if("0".equals(message.get("flag_finish"))){
			mResult.inErrorMessage(916421162);
			return mResult;
		}
		
		if(DbUp.upTable("za_message").dataUpdate(mEditMaps, "flag_finish,finish_time", "uid")>0){
			
			String old_txt="flag_finish="+message.get("flag_finish")+"|del_flag="+message.get("del_flag");
			DbUp.upTable("lc_message").insert("msg_uid",uid,"update_txt","flag_finish=0|del_flag=0","old_txt",old_txt,"create_time",DateUtil.getSysDateTimeString(),"create_user",create_user);

		}else{
			mResult.inErrorMessage(916421163);
			return mResult;
		}
		
		return mResult;
	}
}
