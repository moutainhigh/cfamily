package com.cmall.familyhas.webfunc.apphome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.helper.WebHelper;
import com.srnpr.zapweb.webdo.WebConst;
import com.srnpr.zapweb.webdo.WebUp;
import com.srnpr.zapweb.webfunc.RootFunc;
import com.srnpr.zapweb.webmodel.MWebField;
import com.srnpr.zapweb.webmodel.MWebOperate;
import com.srnpr.zapweb.webmodel.MWebPage;
import com.srnpr.zapweb.webmodel.MWebResult;

/**
 * 添加
 * 
 * @author zhouenzhi 
 * 校验时间
 * 
 */
public class FuncAddChannelDetails extends RootFunc {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.srnpr.zapweb.webface.IWebFunc#funcDo(java.lang.String,
	 * com.srnpr.zapcom.basemodel.MDataMap)
	 */
	public MWebResult funcDo(String sOperateUid, MDataMap mDataMap) {

		MWebResult mResult = new MWebResult();

		MWebOperate mOperate = WebUp.upOperate(sOperateUid);

		MWebPage mPage = WebUp.upPage(mOperate.getPageCode());

		MDataMap mAddMaps = mDataMap.upSubMap(WebConst.CONST_WEB_FIELD_NAME);

		// 定义插入数据库
		MDataMap mInsertMap = new MDataMap();
		// 定义组件判断标记
		boolean bFlagComponent = false;

		recheckMapField(mResult, mPage, mAddMaps);

		if (mResult.upFlagTrue()) {

			// 循环所有结构 初始化插入map
			for (MWebField mField : mPage.getPageFields()) {

				if (mField.getFieldTypeAid().equals("104005003")) {
					bFlagComponent = true;
				}

				if (mAddMaps.containsKey(mField.getFieldName()) && StringUtils.isNotEmpty(mField.getColumnName())) {

					String sValue = mAddMaps.get(mField.getFieldName());

					mInsertMap.put(mField.getColumnName(), sValue);
				}

				// 如果设置不为空 则进行各种校验
				if (StringUtils.isNotEmpty(mField.getFieldScope())) {

					MDataMap mFieldScope = new MDataMap().inUrlParams(mField.getFieldScope());

					MDataMap mScopeMap = mFieldScope.upSubMap(WebConst.CONST_WEB_PAGINATION_NAME);

					String sDefaultValue = "";

					if (mScopeMap.containsKey("defaultvalue")) {
						sDefaultValue = mScopeMap.get("defaultvalue");
					}

					// 判断默认值
					if (StringUtils.isNotEmpty(sDefaultValue)) {
						String sValue = "";

						if (StringUtils.contains(sDefaultValue, WebConst.CONST_WEB_SET_REPLACE)) {

							// 重新格式化参数
							sValue = WebHelper.recheckReplace(sDefaultValue, mDataMap);

						} else {
							sValue = sDefaultValue;
						}

						if (StringUtils.isNotEmpty(sValue)) {
							mDataMap.put(WebConst.CONST_WEB_FIELD_NAME + mField.getFieldName(), sValue);

							mInsertMap.put(mField.getColumnName(), sValue);
						}

					}

					// 如果有附件设置
					if (mScopeMap.containsKey("targetset")) {
						String sTargetSetString = mScopeMap.get("targetset");

						// 校验字段的唯一
						if (sTargetSetString.equals("unique")) {

							if (DbUp.upTable(mPage.getPageTable()).count(mField.getColumnName(),
									mInsertMap.get(mField.getColumnName())) > 0) {

								mResult.inErrorMessage(969905004, mField.getFieldNote());
							}

						}
						// 自动生成code
						else if (sTargetSetString.equals("code")) {

							MDataMap mSetMap = mFieldScope.upSubMap(WebConst.CONST_WEB_FIELD_SET);

							String sCodeName = mSetMap.get("codename");

							String sParentValue = mAddMaps.get(sCodeName);

							MDataMap mTopDataMap = DbUp.upTable(mPage.getPageTable()).oneWhere(mField.getColumnName(),
									"-" + mField.getColumnName(), "", sCodeName, sParentValue);

							if (mTopDataMap != null) {

								String sMaxString = mTopDataMap.get(mField.getColumnName());
								long lMax = Long.parseLong(StringUtils.right(sMaxString, 4)) + 1;

								mInsertMap.put(mField.getColumnName(),
										sParentValue + StringUtils.leftPad(String.valueOf(lMax), 4, "0"));

							} else {
								String sMaxAdd = sParentValue
										+ (mSetMap.containsKey("maxadd") ? mSetMap.get("maxadd") : "0001");

								mInsertMap.put(mField.getColumnName(), String.valueOf(sMaxAdd));

							}

						}

					}

				}

			}
		}

		if (mResult.upFlagTrue()) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long l = 0l;
			long l2 = 0l;
			String start_time = mInsertMap.get("start_time");
			String end_time = mInsertMap.get("end_time");
			Date currentDate = new Date();
			try {
				Date startDate = formatter.parse(start_time);
				Date endDate = formatter.parse(end_time);
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
			String join_up_end_time = mInsertMap.get("join_up_end_time");//活动报名截止时间
			long l3 = 0l;
			long l4 = 0l;
			if(!StringUtils.isEmpty(join_up_end_time)){//不为空时
				try {
					Date join_up = formatter.parse(join_up_end_time);
					l3 = join_up.getTime() - currentDate.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(l3<0){
					mResult.setResultMessage("活动报名时间小于当前时间，不合法，请重新填写");
					mResult.setResultCode(10000);
					return mResult;
				}
			}
			String activity_start_time = mInsertMap.get("activity_start_time");//活动开始时间
			if(!StringUtils.isEmpty(activity_start_time)){//不为空时
				try {
					Date activity_start = formatter.parse(activity_start_time);
					l4 = activity_start.getTime() - currentDate.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(l4<0){
					mResult.setResultMessage("活动举办时间小于当前时间，不合法，请重新填写");
					mResult.setResultCode(10000);
					return mResult;
				}
			}
			long l5 = 0l;
			if(!StringUtils.isEmpty(join_up_end_time)&&!StringUtils.isEmpty(activity_start_time)){
				try {
					Date activity_start = formatter.parse(activity_start_time);
					Date join_up = formatter.parse(join_up_end_time);
					l5 = join_up.getTime() - activity_start.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(l5>0){
					mResult.setResultMessage("活动举办时间小于报名截止时间，不合法，请重新填写");
					mResult.setResultCode(10000);
					return mResult;
				}
			}
			String channel_uid = mInsertMap.get("channel_uid");
			MDataMap channel = DbUp.upTable("fh_apphome_channel").one("uid",channel_uid);
			String channel_type = channel.get("channel_type");
			String need_info = mInsertMap.get("need_info");
			if("449748130004".equals(channel_type)){
				if(StringUtils.isEmpty(need_info)){
					mResult.setResultMessage("报名所需信息不能为空");
					mResult.setResultCode(10000);
					return mResult;
				}
			}
			//校验位置排序，适用于视频与活动。
			String seq = mInsertMap.get("seq");
			if("449748130005".equals(channel_type) || "449748130004".equals(channel_type)){
				String sql = "select * from fh_apphome_channel_details where channel_uid = \'"+channel_uid+"\' and seq = \'"+seq+"\' and end_time"
						+ " >= now() and start_time <= \'"+end_time+"\' and end_time >= \'"+start_time+"\'";
				List<Map<String,Object>> activitys = DbUp.upTable("fh_apphome_channel_details").dataSqlList(sql, null);
				if(activitys.size() > 0){
					mResult.setResultMessage("相同时间段内位置排序已重复，请核查后再填写");
					mResult.setResultCode(10000);
					return mResult;
				}
			}
			DbUp.upTable(mPage.getPageTable()).dataInsert(mInsertMap);

			if (bFlagComponent) {

				for (MWebField mField : mPage.getPageFields()) {
					if (mField.getFieldTypeAid().equals("104005003")) {

						WebUp.upComponent(mField.getSourceCode()).inAdd(mField, mDataMap);

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
