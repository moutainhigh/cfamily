package com.cmall.familyhas.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cmall.familyhas.api.input.ApiForFlashSaleInput;
import com.cmall.familyhas.api.model.FlashSaleEvent;
import com.cmall.familyhas.api.result.FlashSaleResult;
import com.srnpr.xmassystem.load.LoadFlashSaleList;
import com.srnpr.xmassystem.modelevent.PlusModelFlashSale;
import com.srnpr.xmassystem.modelevent.PlusModelFlashSaleList;
import com.srnpr.xmassystem.plusquery.PlusModelQuery;
import com.srnpr.xmassystem.util.AppVersionUtils;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapweb.webapi.RootApiForVersion;

/**
 * @Date 2019-05-10 11:43:00
 * @author Angel Joy
 * @version 1.0
 * @Desc 秒杀列表接口
 */
public class ApiForFlashSaleList extends RootApiForVersion<FlashSaleResult, ApiForFlashSaleInput> {

	@Override
	public FlashSaleResult Process(ApiForFlashSaleInput inputParam, MDataMap mRequestMap) {
		FlashSaleResult result = new FlashSaleResult();
		LoadFlashSaleList load = new LoadFlashSaleList();
		
		// 以ApiClient中的渠道编号为准，如果为空则默认APP
		if(StringUtils.isNotBlank(getApiClientValue("channelId"))) {
			inputParam.setChannelId(getChannelId());
		}
		
		PlusModelQuery tQuery = new PlusModelQuery("");
		PlusModelFlashSaleList flashSale = load.upInfoByCode(tQuery);
		List<PlusModelFlashSale> todayMaps = new ArrayList<PlusModelFlashSale>();
		List<PlusModelFlashSale> tomorowMaps = new ArrayList<PlusModelFlashSale>();
		if(flashSale != null) {
			todayMaps = flashSale.getTodayFlashSaleEventList();
			tomorowMaps = flashSale.getTomorrowFlashSaleEventList();
		}
		
		// 根据渠道过滤一下活动
		List<PlusModelFlashSale> tmpTodayMaps = new ArrayList<PlusModelFlashSale>();
		for(int i = 0;i<todayMaps.size();i++) {
			PlusModelFlashSale map = todayMaps.get(i);
			// 忽略不支持当前渠道的活动
			if(StringUtils.isNotBlank(map.getChannels()) && !map.getChannels().contains(inputParam.getChannelId())) {
				continue;
			}
			tmpTodayMaps.add(map);
		}
		
		// 过滤渠道后的活动列表
		todayMaps = tmpTodayMaps;
		
		List<FlashSaleEvent> list = new ArrayList<FlashSaleEvent>();
		for(int i = 0;i<todayMaps.size();i++) {
			PlusModelFlashSale map = todayMaps.get(i);
			
			FlashSaleEvent event = new FlashSaleEvent();
			event.setCheckStatus("0");//未选中//TODO
			event.setEventCode(map.getEventCode());
			event.setStartTime(map.getBeginTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date date = sdf.parse(map.getBeginTime());
				if(date.compareTo(new Date())<0) {//开始时间小于当前时间
					if(i != (todayMaps.size()-1)) {//不是最后一条。查询吓一跳时间是否大于当前时间，如果是，则选中，并且状态未抢购中
						Date dateNext = sdf.parse(todayMaps.get(i+1).getBeginTime());
						if(dateNext.compareTo(new Date())>0) {//下一场还未开始
							event.setStatus("2");//秒杀抢购中
							event.setCheckStatus("1");//选中
						}else {
							event.setStatus("1");//秒杀已开始
							event.setCheckStatus("0");//不选中
						}
					}else {//如果是最后一条
						event.setStatus("2");//秒杀抢购中
						event.setCheckStatus("1");//选中
					}
				}else {
					if(i == 0 ) {//如果第一条还没有开始，直接选中第一条
						event.setCheckStatus("1");//选中
					}
					event.setStatus("3");//秒杀即将开始
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			list.add(event);
		}
		
		int limitSize = 0;
		for(PlusModelFlashSale map : tomorowMaps) {
			// 忽略不支持当前渠道的活动
			if(StringUtils.isNotBlank(map.getChannels()) && !map.getChannels().contains(inputParam.getChannelId())) {
				continue;
			}
			
			FlashSaleEvent event = new FlashSaleEvent();
			event.setCheckStatus("0");//未选中
			event.setEventCode(map.getEventCode());
			event.setStartTime(map.getBeginTime());
			event.setStatus("4");//秒杀即将开始（明日开抢）
			list.add(event);
			
			// 即将开始限制两个活动，跟之前逻辑保持一致
			if(++limitSize >= 2) {
				break;
			}
		}
		if(todayMaps  == null || todayMaps.size() == 0) {//当天没有秒杀
			if(list.size()>0) {
				list.get(0).setCheckStatus("1");//如果今天没有秒杀，明天有秒杀，需要将明天的第一条设置为选中状态
			}
		}
		
		result.setFlashSaleEventList(list);
		if(StringUtils.isBlank(getApiClient().get("app_vision"))) {			
			getApiClient().put("app_vision", "5.6.4");
		}
		// 564版本改为取两档 当前档+下一档
		try {
			if(StringUtils.isNotBlank(getApiClient().get("app_vision")) && AppVersionUtils.compareTo(getApiClient().get("app_vision"), "5.6.4") >= 0) {
				List<FlashSaleEvent> temp = new ArrayList<FlashSaleEvent>();
				for (int i = 0; i< list.size(); i ++) {
					FlashSaleEvent flashSaleEvent = list.get(i);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if(sdf.parse(flashSaleEvent.getStartTime()).compareTo(new Date())<=0) {
						if(i != (list.size()-1)) {
							// 当前有秒杀活动
							if(sdf.parse(list.get(i+1).getStartTime()).compareTo(new Date()) > 0) {
								temp.add(flashSaleEvent);
								temp.add(list.get(i+1));
								break;
							}
						}else {
							temp.add(flashSaleEvent);
						}
					}else {
						// 当前没有秒杀活动
						temp.add(flashSaleEvent);
					}
				}
				result.setFlashSaleEventList(temp);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
