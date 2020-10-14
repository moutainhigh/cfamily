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

public class FuncEditChannelDetails extends RootFunc {

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
			String uid = mInsertMap.get("uid");
			String seq = mInsertMap.get("seq");
			if("449748130005".equals(channel_type) || "449748130004".equals(channel_type)){
				String sql = "select * from fh_apphome_channel_details where channel_uid = \'"+channel_uid+"\' and seq = \'"+seq+"\' and end_time"
						+ " >= now() and start_time <= \'"+end_time+"\' and end_time >= \'"+start_time+"\' and uid != \'"+uid+"\'";
				List<Map<String,Object>> activitys = DbUp.upTable("fh_apphome_channel_details").dataSqlList(sql, null);
				if(activitys.size() > 0){
					mResult.setResultMessage("相同时间段内位置排序已重复，请核查后再填写");
					mResult.setResultCode(10000);
					return mResult;
				}
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
