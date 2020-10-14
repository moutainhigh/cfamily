package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncEdit;
import com.srnpr.zapweb.webmodel.MWebResult;
/***
 * 逻辑删除短信
 * @author jlin
 *
 */
public class FuncDeleteMessage extends FuncEdit{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		
		MDataMap mEditMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String uid=mEditMaps.get("uid");
		mEditMaps.put("del_flag", "1");
		
		MDataMap message = DbUp.upTable("za_message").one("uid", uid);

		if ("1".equals(message.get("del_flag"))) {
			mResult.inErrorMessage(916421161);
			return mResult;
		}
		
		
		if(DbUp.upTable("za_message").dataUpdate(mEditMaps, "del_flag", "uid")>0){
			
			String old_txt="flag_finish="+message.get("flag_finish")+"|del_flag="+message.get("del_flag");
			DbUp.upTable("lc_message").insert("msg_uid",uid,"update_txt","flag_finish="+message.get("flag_finish")+"|del_flag=1","old_txt",old_txt,"create_time",DateUtil.getSysDateTimeString(),"create_user",create_user);

		}else{
			mResult.inErrorMessage(916421163);
			return mResult;
		}
		
		return mResult;
	}
}
