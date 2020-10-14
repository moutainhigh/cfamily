package com.cmall.familyhas.webfunc;

import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebResult;
/**
 * 消息推送新增
 * @author dyc
 * */
public class CommentPushAdd extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MDataMap map = new MDataMap();
		
		if (mResult.upFlagTrue()) {
			/*系统当前时间*/
			String create_time = DateUtil.getNowTime();
			/*获取当前登录人*/
			String create_user = UserFactory.INSTANCE.create().getLoginName();
			/*获取当前app*/
			String appCode = UserFactory.INSTANCE.create().getManageCode();
			map.put("title", mAddMaps.get("title"));
			map.put("comment", mAddMaps.get("comment"));
			map.put("push_time", mAddMaps.get("push_time"));
			map.put("jump_type", mAddMaps.get("jump_type"));
			map.put("jump_position", mAddMaps.get("jump_position"));
			map.put("push_status", "4497465000070001");//默认未完成
			map.put("create_user", create_user);
			map.put("create_time", create_time);
			map.put("app_code", appCode);
			
			/**将消息推送信息插入nc_comment_push表中*/
			DbUp.upTable("nc_comment_push").dataInsert(map);
		}
		return mResult;
   }

}
