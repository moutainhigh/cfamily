package com.cmall.familyhas.webfunc.apphome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

public class FuncEdit extends RootFunc {

	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {
		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		MDataMap mInsertMap = new MDataMap();

		recheckMapField(mResult, mPage, mAddMaps);

		// 定义组件判断标记
		boolean bFlagComponent = false;

		if (mResult.upFlagTrue()) {

			// 循环所有结构
			for (MWebField mField : mPage.getPageFields()) {

				if (mField.getFieldTypeAid().equals("104005003")) {
					bFlagComponent = true;
				}

				if (mAddMaps.containsKey(mField.getColumnName())) {

					String sValue = mAddMaps.get(mField.getColumnName());

					mInsertMap.put(mField.getColumnName(), sValue);
				} else if (mField.getFieldTypeAid().equals("104005103")) {
					// 特殊判断修改时如果没有传值 则自动赋空
					mInsertMap.put(mField.getColumnName(), "");
				}

			}
		}

		if (mResult.upFlagTrue()) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long l = 0l;
			long l2 = 0l;
			String start_time = mInsertMap.get("start_time");
			String end_time = mInsertMap.get("end_time");
			try {
				Date startDate = formatter.parse(start_time);
				Date endDate = formatter.parse(end_time);
				Date currentDate = new Date();
				l2 = endDate.getTime() - currentDate.getTime();
				l = endDate.getTime() - startDate.getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if(l2<0){
				mResult.setResultMessage("结束时间小于当前时间，不合法，请重新填写");
				mResult.setResultCode(10000);
				return mResult;
			}
			if(l<0){
				mResult.setResultMessage("开始时间大于结束时间，不合法，请重新填写");
				mResult.setResultCode(10000);
				return mResult;
			}
			String is_more = mInsertMap.get("is_more");
			String more_title = mInsertMap.get("more_title");
			if("449746250001".equals(is_more)){//是否更多为是的时候，判断more_title不能为空
				if(StringUtils.isEmpty(more_title)){
					mResult.setResultMessage("当【是否显示更多】选'是'时，标题不能为空");
					mResult.setResultCode(10000);
					return mResult;
				}
			}
			String is_share = mInsertMap.get("is_share");
			String share_title = mInsertMap.get("share_title");
			String share_desc = mInsertMap.get("share_desc");
			String share_pic = mInsertMap.get("share_pic");
			if("449746250001".equals(is_share)){
				if(StringUtils.isEmpty(share_title)){
					mResult.setResultMessage("当【更多页面是否分享】选'是'时，分享标题不能为空");
					mResult.setResultCode(10000);
					return mResult;
				}
				if(StringUtils.isEmpty(share_desc)){
					mResult.setResultMessage("当【更多页面是否分享】选'是'时，分享描述不能为空");
					mResult.setResultCode(10000);
					return mResult;
				}
				if(StringUtils.isEmpty(share_pic)){
					mResult.setResultMessage("当【更多页面是否分享】选'是'时，分享图片不能为空");
					mResult.setResultCode(10000);
					return mResult;
				}
			}
			String uid = mInsertMap.get("uid");
			String channel_type = mInsertMap.get("channel_type");
			//检索同一时间段内是否已存在相同类型的活动。如果存在则不添加。
			String sSql = "select * from fh_apphome_channel where channel_type = "+"\'"+channel_type+"\' and end_time >= "+"\'"+start_time+"\' and start_time <= "+"\'"+end_time+"\' and uid !="+"\'"+uid+"\'";
			List<Map<String,Object>> list = DbUp.upTable("fh_apphome_channel").dataSqlList(sSql, null);
			if(list.size() != 0){
				mResult.setResultMessage("交叉时间内存在统一类型活动，请确保统一类型活动时间没有交叉！！");
				mResult.setResultCode(10000);
				return mResult;
			}
			DbUp.upTable(mPage.getPageTable()).dataUpdate(mInsertMap, "", "uid");

			if (bFlagComponent) {

				for (MWebField mField : mPage.getPageFields()) {
					if (mField.getFieldTypeAid().equals("104005003")) {

						WebUp.upComponent(mField.getSourceCode()).inEdit(mField, mDataMap);

					}
				}

			}

		}

		if (mResult.upFlagTrue()) {
			mResult.setResultMessage(bInfo(969909001));
		}
		return mResult;
	}

}
