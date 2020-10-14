package com.cmall.familyhas.webfunc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

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
 * 添加搜索配置方法
 * @author zhouguohui
 *
 */
public class SearchWordsAdd extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime = mAddMaps.get("begin_time");
			String endTime = mAddMaps.get("end_time");
			String words = mAddMaps.get("words");
			Date date1 = format.parse(startTime);
			Date date2 = format.parse(endTime);
			if(words.length() > 12) {
				mResult.setResultCode(-1);
				mResult.setResultMessage("搜索关键字不能超过12个字符");
				return mResult;
			}
			if(new Date().after(date1)) {
				mResult.setResultCode(-1);
				mResult.setResultMessage("开始时间不能小于当前时间");
				return mResult;
			}
			if(date1.after(date2)) {
				mResult.setResultCode(-1);
				mResult.setResultMessage("开始时间不能小于结束时间");
				return mResult;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//创建时间为当年系统时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		
		/*获取当前登录人*/
		
		String create_user = UserFactory.INSTANCE.create().getLoginName();
		
		
		mAddMaps.put("create_time", df.format(new Date()));
		mAddMaps.put("creator", create_user);
		
		try{
			if (mResult.upFlagTrue()) {
				/**将惠家友息插入pc_hot_word表中*/
				String stringUrl = mAddMaps.get("click_value");
				if(StringUtils.isBlank(stringUrl)) {
					mAddMaps.put("click_type", "449748360001");
				}else {
					mAddMaps.put("click_type", "449748360002");
				}
				DbUp.upTable("fh_search_words").dataInsert(mAddMaps);
			}
		}catch (Exception e) {
			mResult.inErrorMessage(959701033);
		}
		
		
		return mResult;
	}

}
