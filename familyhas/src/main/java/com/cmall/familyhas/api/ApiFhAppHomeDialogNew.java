package com.cmall.familyhas.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import com.cmall.familyhas.api.input.ApiFhAppHomeDialogNewInput;
import com.cmall.familyhas.api.result.ApiFhAppHomeDialogNewResult;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * @descriptions 5.6.7版本 首页弹窗 - 广告弹窗 - Api接口
 * 
 * @refactor
 * @author wangmeng
 * @date 2020-08-05
 * @version 1.0.0
 */
public class ApiFhAppHomeDialogNew extends RootApiForVersion<ApiFhAppHomeDialogNewResult, ApiFhAppHomeDialogNewInput> {

	public ApiFhAppHomeDialogNewResult Process(ApiFhAppHomeDialogNewInput inputParam, MDataMap mRequestMap) {

		String channelId = getChannelId();
		if (StringUtils.isNotBlank(channelId) && channelId.equalsIgnoreCase("449747430023")) {// 小程序
			inputParam.setChannels("449748660002");
		} else {
			inputParam.setChannels("449748660001");
		}

		ApiFhAppHomeDialogNewResult result = new ApiFhAppHomeDialogNewResult();
		try {
			String currentTime_ = inputParam.getCurentTime();
			if (!StringUtils.isNotBlank(currentTime_)) {
				currentTime_ = com.cmall.familyhas.util.DateUtil.getNowTime();
			}
					
			String sql = "select * from fh_app_home_dialog_new where delete_flag = 1 and (channels = '"
					+ inputParam.getChannels()
					+ "' or channel_limit = '449747640001') and end_time >= '" + currentTime_
					+ "' and start_time <= '" + currentTime_ + "'";
			sql += " and (show_location = '449748700001' or show_location = '" + inputParam.getShowLocation() + "') ";
			List<Map<String, Object>> list = DbUp.upTable("fh_app_home_dialog_new").dataSqlList(sql, new MDataMap());
			for (Map<String, Object> map : list) { // 与当天记录进行比对
				String startTime_ = (String) map.get("start_time");
				String endTime_ = (String) map.get("end_time");
				if (!StringUtils.isNotBlank(startTime_) || !StringUtils.isNotBlank(endTime_)) {
					continue;
				}
				if (this.compareDate(currentTime_, startTime_) && this.compareDate(endTime_, currentTime_)) {
					result.setStartTime((String) map.get("start_time"));
					result.setEndTime((String) map.get("end_time"));
					result.setCurentTime(currentTime_);
					result.setEventUrl((String) map.get("url"));
					result.setFlag(true);
					result.setPopCount(NumberUtils.toInt(map.get("pop_count") + "", 1));
					result.setPictureUrl(map.get("picture_url").toString());
					result.setJumpType(map.get("jump_type").toString());
					break;
				}
			}
			/*
			 * TODO 是否要处理 if(!result.getFlag()){ // 当前时间点 没有找到匹配的记录 Map<String, Object>
			 * entity = this.getNearDate(list, cTime, sdf); if(entity != null){
			 * result.setStartTime((String) entity.get("start_time"));
			 * result.setEndTime((String) entity.get("end_time"));
			 * result.setCurentTime(currentTime_); result.setEventUrl((String)
			 * entity.get("url")); result.setFlag(true); }else{
			 * result.setResultCode(959701033);
			 * result.setResultMessage(currentTime_.split(" ")[0] +" 日没有查到相关信息 "); return
			 * result; } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(959701033);
			result.setResultMessage("Api测试接口传入时间格式错误, 请遵循此格式 yyyy-MM-dd HH:mm:ss ");
		}

		return result;
	}

	/**
	 * @descriptions 比较两个时间的大小 如果两个时间相等则返回0
	 * @param a
	 *            not null
	 * @param b
	 *            not null
	 * @return boolean
	 * 
	 * @refactor
	 * @author Yangcl
	 * @date 2016-5-5-下午2:52:13
	 * @version 1.0.0.1
	 */
	private boolean compareDate(String a, String b) {
		return a.compareTo(b) > 0;
	}

	/**
	 * @descriptions 返回一个记录集合中结束日期接近当前指定时间点的记录。 this.compareDate(startTime_, cTime)
	 *               && this.compareDate(cTime, endTime_) 会出现
	 *               cTime不在这些时间段内的情况，此时需要配合该方法使用，返回最接近指定时间点之前的 一条记录。
	 * 
	 * @param list
	 * @param target
	 * @param sdf
	 * @return
	 * @throws ParseException
	 * 
	 * @refactor no
	 * @author Yangcl
	 * @date 2016-5-6-下午5:20:45
	 * @version 1.0.0.1
	 */
	private Map<String, Object> getNearDate(List<Map<String, Object>> list, Date target, SimpleDateFormat sdf)
			throws ParseException {
		if (list.size() == 0)
			return null;
		if (list.size() == 1)
			return list.get(0);

		TreeMap<Long, Map<String, Object>> sortMap = new TreeMap<Long, Map<String, Object>>();
		for (Map<String, Object> entity : list) {
			Date endTime_ = sdf.parse((String) entity.get("end_time"));
			Long key = target.getTime() - endTime_.getTime();
			if (key > 0)
				sortMap.put(key, entity);
		}
		return sortMap.get(sortMap.firstKey());
	}

}
