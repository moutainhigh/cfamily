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
public class FuncAddMessage1 extends FuncAdd {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		String create_user = UserFactory.INSTANCE.create().getLoginName();

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		
		MDataMap dataMap = new MDataMap();
		dataMap.put("msg_receive", mAddMaps.get("msg_receive"));
		dataMap.put("send_time", mAddMaps.get("send_time"));
		dataMap.put("send_source", mAddMaps.get("send_source"));
		dataMap.put("msg_content", mAddMaps.get("msg_content"));
		dataMap.put("create_time", DateUtil.getSysDateTimeString());
		dataMap.put("create_user", create_user);
		String msg_uid = DbUp.upTable("za_message").dataInsert(dataMap);
		DbUp.upTable("lc_message").insert("msg_uid", msg_uid, "update_txt", "flag_finish=0|del_flag=0", "create_time",dataMap.get("create_time"), "create_user", create_user);
		
		return mResult;
	}
}
