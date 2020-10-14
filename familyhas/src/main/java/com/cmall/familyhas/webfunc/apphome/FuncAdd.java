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
import com.srnpr.zapweb.webfactory.UserFactory;
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
public class FuncAdd extends RootFunc {

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
			String channel_type = mInsertMap.get("channel_type");
			//检索同一时间段内是否已存在相同类型的活动。如果存在则不添加。
			String sSql = "select * from fh_apphome_channel where channel_type = "+"\'"+channel_type+"\' and end_time >= "+"\'"+start_time+"\' and start_time <= "+"\'"+end_time+"\'";
			List<Map<String,Object>> list = DbUp.upTable("fh_apphome_channel").dataSqlList(sSql, null);
			if(list.size() != 0){
				mResult.setResultMessage("交叉时间内存在统一类型活动，请确保统一类型活动时间没有交叉！！");
				mResult.setResultCode(10000);
				return mResult;
			}
			Date date = new Date();
			mInsertMap.put("create_time", formatter.format(date));
			String user = UserFactory.INSTANCE.create().getLoginName();
			mInsertMap.put("create_user", user);
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
