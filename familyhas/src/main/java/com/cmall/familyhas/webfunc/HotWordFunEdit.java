package com.cmall.familyhas.webfunc;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfactory.UserFactory;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 修改惠家友热词维护方法
 * @author zhouguohui
 *
 */
public class HotWordFunEdit extends RootFunc{

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		MWebOperate mOperate = WebUp.upOperate(sOperateUid);
		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());
		recheckMapField(mResult, mPage, mAddMaps);
		//创建时间为当年系统时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		
		/*获取当前登录人*/
		
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		
		
		mAddMaps.put("top_time", df.format(new Date()));
		
		
		mAddMaps.put("top_user", create_user);
		
		try{
			if (mResult.upFlagTrue()) {
				/**将惠家友息插入pc_hot_word表中*/
				DbUp.upTable("pc_hot_word").update(mAddMaps);
			}
		}catch (Exception e) {
			mResult.inErrorMessage(959701033);
		}
		return mResult;
	}

}
