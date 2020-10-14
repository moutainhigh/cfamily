package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.FuncAdd;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 添加短信
 * @author jlin
 *
 */
public class FuncAddMessage extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		String create_user = UserFactory.INSTANCE.create().getLoginName();

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		String msg_receives[] = mAddMaps.get("msg_receive").split(",");
		
		for (String msg_receive : msg_receives) {
			mAddMaps.put("msg_receive", msg_receive);
			mAddMaps.put("create_time", DateUtil.getSysDateTimeString());
			mAddMaps.put("create_user", create_user);
			mAddMaps.remove("uid");
			String msg_uid = DbUp.upTable("za_message").dataInsert(mAddMaps);

			DbUp.upTable("lc_message").insert("msg_uid", msg_uid, "update_txt", "flag_finish=0|del_flag=0", "create_time",mAddMaps.get("create_time"), "create_user", create_user);
		}
		return mResult;
	}
}
