package com.cmall.familyhas.webfunc;

import org.apache.commons.lang.StringUtils;

import com.cmall.membercenter.memberdo.MemberConst;
import com.cmall.systemcenter.common.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加消息
 * @author lee
 *
 */
public class FuncAddForMessage extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		// 定义插入数据库
		MDataMap mInsertMap = new MDataMap();

		recheckMapField(mResult, mPage, mAddMaps);

		if (mResult.upFlagTrue()) {

			//判断登陆用户是否为空
			String loginname=UserFactory.INSTANCE.create().getLoginName();
			if(loginname==null||"".equals(loginname)){
				mResult.inErrorMessage(941901073);
				return mResult;
			}
			
			
			
			// 循环所有结构 初始化插入map
			for (MWebField mField : mPage.getPageFields()) {
				if (mAddMaps.containsKey(mField.getFieldName())
						&& StringUtils.isNotEmpty(mField.getColumnName())) {

					String sValue = mAddMaps.get(mField.getFieldName());

					mInsertMap.put(mField.getColumnName(), sValue);
					
				}
				
			}
			
			
			
			String now=DateUtil.getSysDateTimeString();
			mInsertMap.put("mess_code", WebHelper.upCode("MES"));
			mInsertMap.put("manage_code", MemberConst.MANAGE_CODE_HOMEHAS);//家有惠编号
			mInsertMap.put("update_user", loginname);   // 更新的用户
			mInsertMap.put("create_time", now);   // 添加时间
			mInsertMap.put("create_user", loginname);   // 添加的用户
			mInsertMap.put("update_time", now);   // 更新时间
		}
		
		
		if (mResult.upFlagTrue()) {
			
			DbUp.upTable("hp_message").dataInsert(mInsertMap);
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;

	}
	
}
