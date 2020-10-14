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
 * 修改搜索配置方法
 * @author zhouguohui
 *
 */
public class SearchWordsEdit extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		
		MWebResult mResult = new MWebResult();
		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String startTime = mAddMaps.get("begin_time");
			String endTime = mAddMaps.get("end_time");
			Date date1 = format.parse(startTime);
			Date date2 = format.parse(endTime);
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
		try{
			if (mResult.upFlagTrue()) {
				/**将惠家友息插入pc_hot_word表中*/
				String stringUrl = mAddMaps.get("click_value");
				if(StringUtils.isBlank(stringUrl)) {
					mAddMaps.put("click_type", "449748360001");
				}else {
					mAddMaps.put("click_type", "449748360002");
				}
				DbUp.upTable("fh_search_words").dataUpdate(mAddMaps, "words,click_type,click_value,begin_time,end_time", "zid");
			}
		}catch (Exception e) {
			mResult.inErrorMessage(959701033);
		}
		
		
		return mResult;
	}

}
