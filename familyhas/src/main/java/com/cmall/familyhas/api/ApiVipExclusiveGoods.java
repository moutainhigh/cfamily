package com.cmall.familyhas.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.cmall.familyhas.api.input.ApiVipExclusiveGoodInput;
import com.cmall.familyhas.api.result.ApiVipExclusiveGoodResult;
import com.cmall.familyhas.model.VipActivity;
import com.cmall.familyhas.model.VipAreaPoint;
import com.cmall.familyhas.model.VipBargain;
import com.cmall.familyhas.model.VipClassRoom;
import com.cmall.productcenter.model.PicInfo;
import com.cmall.productcenter.service.ProductService;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForToken;

/**
 * 会员专属，商品列表页
 * @author ljj
 *
 */
public class ApiVipExclusiveGoods extends RootApiForToken<ApiVipExclusiveGoodResult, ApiVipExclusiveGoodInput> {

	@Override
	public ApiVipExclusiveGoodResult Process(ApiVipExclusiveGoodInput inputParam, MDataMap mRequestMap) {
		//用户积分
		String custPoint = inputParam.getCustPoint();
		String maxWidth = StringUtils.isBlank(inputParam.getMaxWidth()) ? "0" : inputParam.getMaxWidth(); // 最大宽度
		String picType = StringUtils.isBlank(inputParam.getPicType()) ? "" : inputParam.getPicType(); // 图片格式
		
		List<String> urlList = new ArrayList<String>();
		ProductService pService = new ProductService();
		
		//加价购
		List<VipAreaPoint> pList = new ArrayList<VipAreaPoint>();
		String priceUpSql = "select d.uid, d.title, d.pic_url, 200 * d.jf_cost jf_cost, d.allow_count, d.extra_charges, d.product_code, "
				+ "(select info.mainpic_url from productcenter.pc_productinfo info where info.product_code = d.product_code) product_photo, "
				+ "(select count(1) from productcenter.pc_productinfo i where i.product_code = d.product_code and i.product_status = '4497153900060002') is_down "
				+ "from fh_apphome_channel c, fh_apphome_channel_details d where c.channel_type = '449748130001' and c.uid = d.channel_uid "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' and d.start_time <= sysdate() and d.end_time >= sysdate() "
				+ "order by abs((d.jf_cost * 200) - '" + custPoint + "') , d.zid limit 0, 8";
		List<Map<String, Object>> priceUpList = DbUp.upTable("fh_apphome_channel").dataSqlList(priceUpSql, new MDataMap());
		for(Map<String, Object> priceUp : priceUpList) {
			urlList.clear();
			VipAreaPoint point = new VipAreaPoint();
			point.setInfoId(priceUp.get("uid") == null ? "" : priceUp.get("uid").toString());
			point.setProductCode(priceUp.get("product_code") == null ? "" : priceUp.get("product_code").toString());
			point.setTitle(priceUp.get("title") == null ? "" : priceUp.get("title").toString());
			point.setJfCost(priceUp.get("jf_cost") == null ? "" : priceUp.get("jf_cost").toString());
			String charge = priceUp.get("extra_charges") == null ? "0" : priceUp.get("extra_charges").toString();
			if(!"".equals(charge)) {
				point.setCharge(new BigDecimal(charge).stripTrailingZeros().toPlainString());
			}
			
			int allowCount = priceUp.get("allow_count") == null ? 0 : Integer.parseInt(priceUp.get("allow_count").toString());
			boolean isLoot = allowCount > 0 ? false : true;
			point.setAllowCount(allowCount);
			point.setLoot(isLoot);
			
			int proCount = priceUp.get("is_down") == null ? 0 : Integer.parseInt(priceUp.get("is_down").toString());
			boolean isDown = proCount > 0 ? false : true;
			point.setDown(isDown);
			
			//压缩图片
			String productImg = "";
			String photoUrl = MapUtils.getString(priceUp, "product_photo", "");
			if(!"".equals(photoUrl)) {
				urlList.add(photoUrl);
				List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(Integer.parseInt(maxWidth), urlList, picType);
				if(picInfoList.size() > 0) {
					PicInfo info = picInfoList.get(0);
					productImg = info.getPicNewUrl();
				}
			}
			point.setProductImg(productImg);
			pList.add(point);
		}
		Map<String, Object> priceUpData = new HashMap<String, Object>();
		priceUpData.put("items", pList);
		
		//是否显示更多以及更多标题
		String priceShareSql = "select c.channel_name, c.more_title, c.is_more, c.seq from fh_apphome_channel c where c.channel_type = '449748130001' "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' limit 0, 1";
		Map<String, Object> priceShareMap = DbUp.upTable("fh_apphome_channel").dataSqlOne(priceShareSql, new MDataMap());
		if(priceShareMap != null) {
			priceUpData.put("channel_name", priceShareMap.get("channel_name") == null ? "" : priceShareMap.get("channel_name").toString());
			priceUpData.put("more_title", priceShareMap.get("more_title") == null ? "" : priceShareMap.get("more_title").toString());
			priceUpData.put("is_more", priceShareMap.get("is_more") == null ? "" : priceShareMap.get("is_more").toString());
			priceUpData.put("seq", priceShareMap.get("seq") == null ? "" : priceShareMap.get("seq").toString());
		}
		
		//积分兑换
		List<VipAreaPoint> poList = new ArrayList<VipAreaPoint>();
		String pointSql = "select d.uid, d.title, d.pic_url, 200 * d.jf_cost jf_cost, d.allow_count, d.product_code, "
				+ "(select info.mainpic_url from productcenter.pc_productinfo info where info.product_code = d.product_code) product_photo, "
				+ "(select count(1) from productcenter.pc_productinfo i where i.product_code = d.product_code and i.product_status = '4497153900060002') is_down "
				+ "from fh_apphome_channel c, fh_apphome_channel_details d where c.channel_type = '449748130002' and c.uid = d.channel_uid "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' and d.start_time <= sysdate() and d.end_time >= sysdate() "
				+ "order by abs((d.jf_cost * 200) - '" + custPoint + "') , d.zid limit 0, 8";
		List<Map<String, Object>> pointList = DbUp.upTable("fh_apphome_channel").dataSqlList(pointSql, new MDataMap());
		for(Map<String, Object> point : pointList) {
			urlList.clear();
			VipAreaPoint p = new VipAreaPoint();
			p.setInfoId(point.get("uid") == null ? "" : point.get("uid").toString());
			p.setProductCode(point.get("product_code") == null ? "" : point.get("product_code").toString());
			p.setTitle(point.get("title") == null ? "" : point.get("title").toString());
			p.setJfCost(point.get("jf_cost") == null ? "" : point.get("jf_cost").toString());
			
			int allowCount = point.get("allow_count") == null ? 0 : Integer.parseInt(point.get("allow_count").toString());
			boolean isLoot = allowCount > 0 ? false : true;
			p.setAllowCount(allowCount);
			p.setLoot(isLoot);
			
			int proCount = point.get("is_down") == null ? 0 : Integer.parseInt(point.get("is_down").toString());
			boolean isDown = proCount > 0 ? false : true;
			p.setDown(isDown);
			
			//压缩图片
			String productImg = "";
			String photoUrl = MapUtils.getString(point, "product_photo", "");
			if(!"".equals(photoUrl)) {
				urlList.add(photoUrl);
				List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(Integer.parseInt(maxWidth), urlList, picType);
				if(picInfoList.size() > 0) {
					PicInfo info = picInfoList.get(0);
					productImg = info.getPicNewUrl();
				}
			}
			p.setProductImg(productImg);
			poList.add(p);
		}
		Map<String, Object> pointData = new HashMap<String, Object>();
		pointData.put("items", poList);
		
		//是否显示更多以及更多标题
		String pointShareSql = "select c.channel_name, c.more_title, c.is_more, c.seq from fh_apphome_channel c where c.channel_type = '449748130002' "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' limit 0, 1";
		Map<String, Object> pointShareMap = DbUp.upTable("fh_apphome_channel").dataSqlOne(pointShareSql, new MDataMap());
		if(pointShareMap != null) {
			pointData.put("channel_name", pointShareMap.get("channel_name") == null ? "" : pointShareMap.get("channel_name").toString());
			pointData.put("more_title", pointShareMap.get("more_title") == null ? "" : pointShareMap.get("more_title").toString());
			pointData.put("is_more", pointShareMap.get("is_more") == null ? "" : pointShareMap.get("is_more").toString());
			pointData.put("seq", pointShareMap.get("seq") == null ? "" : pointShareMap.get("seq").toString());
		}
		
		//会员专享
		List<VipBargain> barList = new ArrayList<VipBargain>();
		Map<String, Object> bargainData = new HashMap<String, Object>();
		String bargainShareSql = "select c.channel_name, c.more_title, c.is_more, c.seq from fh_apphome_channel c where c.channel_type = '449748130003' "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' limit 0, 1";
		Map<String, Object> bargainShareMap = DbUp.upTable("fh_apphome_channel").dataSqlOne(bargainShareSql, new MDataMap());
		if(bargainShareMap != null) {
			//是否显示更多以及更多标题
			bargainData.put("channel_name", bargainShareMap.get("channel_name") == null ? "" : bargainShareMap.get("channel_name").toString());
			bargainData.put("more_title", bargainShareMap.get("more_title") == null ? "" : bargainShareMap.get("more_title").toString());
			bargainData.put("is_more", bargainShareMap.get("is_more") == null ? "" : bargainShareMap.get("is_more").toString());
			bargainData.put("seq", bargainShareMap.get("seq") == null ? "" : bargainShareMap.get("seq").toString());
			
			//获取会员专享数据
			String bargainSql = "select t.product_code, t.product_name, min(t.selling_price) selling_price, min(t.favorable_price) favorable_price, max(t.sales_num) stock_count, t.product_photo, t.is_down from ("
					+ "select p.product_code, (select info.product_name from productcenter.pc_productinfo info where info.product_code = p.product_code) product_name,"
					+ " p.selling_price, p.favorable_price, p.sales_num,"
					+ " (select info.mainpic_url from productcenter.pc_productinfo info where info.product_code = p.product_code) product_photo,"
					+ " (select count(1) from productcenter.pc_productinfo info where info.product_code = p.product_code and info.product_status = '4497153900060002') is_down" 
					+ " from sc_event_info i, sc_event_item_product p where i.event_type_code = '4497472600010020' and i.begin_time <= sysdate() and i.end_time >= sysdate() "
					+ "and i.event_code = p.event_code and i.event_status = '4497472700020002' and p.flag_enable = '1') t group by t.product_code limit 0, 4";
			List<Map<String, Object>> bargainList = DbUp.upTable("sc_event_info").dataSqlList(bargainSql, new MDataMap());
			for(Map<String, Object> bargain : bargainList) {
				urlList.clear();
				VipBargain v = new VipBargain();
				v.setProductCode(bargain.get("product_code") == null ? "" : bargain.get("product_code").toString());
				v.setProductName(bargain.get("product_name") == null ? "" : bargain.get("product_name").toString());
				
				String sellPrice = bargain.get("selling_price") == null ? "" : bargain.get("selling_price").toString();
				v.setSellPrice(new BigDecimal(sellPrice).stripTrailingZeros().toPlainString());
				v.setFavorablePrice(bargain.get("favorable_price") == null ? "" : bargain.get("favorable_price").toString());
				
				int stockCount = bargain.get("stock_count") == null ? 0 : Integer.parseInt(bargain.get("stock_count").toString());
				boolean isLoot = stockCount > 0 ? false : true;
				v.setStockCount(stockCount);
				v.setLoot(isLoot);
				
				int proCount = bargain.get("is_down") == null ? 0 : Integer.parseInt(bargain.get("is_down").toString());
				boolean isDown = proCount > 0 ? false : true;
				v.setDown(isDown);
				
				//压缩图片
				String productImg = "";
				String photoUrl = MapUtils.getString(bargain, "product_photo", "");
				if(!"".equals(photoUrl)) {
					urlList.add(photoUrl);
					List<PicInfo> picInfoList = pService.getPicInfoOprBigForMulti(Integer.parseInt(maxWidth), urlList, picType);
					if(picInfoList.size() > 0) {
						PicInfo info = picInfoList.get(0);
						productImg = info.getPicNewUrl();
					}
				}
				v.setProductImg(productImg);
				barList.add(v);
			}
		}
		bargainData.put("items", barList);
		
		//活动
		List<VipActivity> acList = new ArrayList<VipActivity>();
		String activitySql = "select d.uid, d.title, d.pic_url, d.activity_location, date_format(d.activity_start_time, '%Y/%m/%d') activity_time, (dayofweek(d.activity_start_time) - 1) week_num,"
				+ " (case when d.activity_start_time > sysdate() then '0' else '1' end) is_finish"
				+ " from fh_apphome_channel c, fh_apphome_channel_details d where c.channel_type = '449748130004' and c.uid = d.channel_uid "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' and d.start_time <= sysdate() and d.end_time >= sysdate() " 
				+ "order by d.seq , d.zid limit 0, 4";
		List<Map<String, Object>> activityList = DbUp.upTable("fh_apphome_channel").dataSqlList(activitySql, new MDataMap());
		for(Map<String, Object> activity : activityList) {
			VipActivity a = new VipActivity();
			a.setActivityCode(activity.get("uid") == null ? "" : activity.get("uid").toString());
			a.setTitle(activity.get("title") == null ? "" : activity.get("title").toString());
			a.setPicUrl(activity.get("pic_url") == null ? "" : activity.get("pic_url").toString());
			a.setActivityLocation(activity.get("activity_location") == null ? "" : activity.get("activity_location").toString());
			a.setActivityTime(activity.get("activity_time") == null ? "" : activity.get("activity_time").toString());
			a.setIsFinish(activity.get("is_finish") == null ? "" : activity.get("is_finish").toString());
			
			String weekStr = "";
			String weekNum = activity.get("week_num") == null ? "" : activity.get("week_num").toString();
			if("0".equals(weekNum)) {
				weekStr = "周日";
			}else if("1".equals(weekNum)) {
				weekStr = "周一";
			}else if("2".equals(weekNum)) {
				weekStr = "周二";
			}else if("3".equals(weekNum)) {
				weekStr = "周三";
			}else if("4".equals(weekNum)) {
				weekStr = "周四";
			}else if("5".equals(weekNum)) {
				weekStr = "周五";
			}else if("6".equals(weekNum)) {
				weekStr = "周六";
			}
			a.setWeekNum(weekStr);
			acList.add(a);
		}
		Map<String, Object> activityData = new HashMap<String, Object>();
		activityData.put("items", acList);
		
		//是否显示更多以及更多标题
		String activityShareSql = "select c.channel_name, c.more_title, c.is_more, c.seq from fh_apphome_channel c where c.channel_type = '449748130004' "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' limit 0, 1";
		Map<String, Object> activityShareMap = DbUp.upTable("fh_apphome_channel").dataSqlOne(activityShareSql, new MDataMap());
		if(activityShareMap != null) {
			activityData.put("channel_name", activityShareMap.get("channel_name") == null ? "" : activityShareMap.get("channel_name").toString());
			activityData.put("more_title", activityShareMap.get("more_title") == null ? "" : activityShareMap.get("more_title").toString());
			activityData.put("is_more", activityShareMap.get("is_more") == null ? "" : activityShareMap.get("is_more").toString());
			activityData.put("seq", activityShareMap.get("seq") == null ? "" : activityShareMap.get("seq").toString());
		}
		
		//视频
		List<VipClassRoom> viList = new ArrayList<VipClassRoom>();
		String videoSql = "select d.uid, d.title, d.pic_url, d.vcr_url from fh_apphome_channel c, fh_apphome_channel_details d where c.channel_type = '449748130005' and c.uid = d.channel_uid "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' and d.start_time <= sysdate() and d.end_time >= sysdate() order by d.seq , d.zid limit 0, 4";
		List<Map<String, Object>> videoList = DbUp.upTable("fh_apphome_channel").dataSqlList(videoSql, new MDataMap());
		for(Map<String, Object> video : videoList) {
			VipClassRoom c = new VipClassRoom();
			c.setInfoId(video.get("uid") == null ? "" : video.get("uid").toString());
			c.setTitle(video.get("title") == null ? "" : video.get("title").toString());
			c.setPicUrl(video.get("pic_url") == null ? "" : video.get("pic_url").toString());
			c.setVcrUrl(video.get("vcr_url") == null ? "" : video.get("vcr_url").toString());
			viList.add(c);
		}
		Map<String, Object> videoData = new HashMap<String, Object>();
		videoData.put("items", viList);
		
		//是否显示更多以及更多标题
		String videoShareSql = "select c.channel_name, c.more_title, c.is_more, c.seq from fh_apphome_channel c where c.channel_type = '449748130005' "
				+ "and c.start_time <= sysdate() and c.end_time >= sysdate() and c.status = '2' limit 0, 1";
		Map<String, Object> videoShareMap = DbUp.upTable("fh_apphome_channel").dataSqlOne(videoShareSql, new MDataMap());
		if(videoShareMap != null) {
			videoData.put("channel_name", videoShareMap.get("channel_name") == null ? "" : videoShareMap.get("channel_name").toString());
			videoData.put("more_title", videoShareMap.get("more_title") == null ? "" : videoShareMap.get("more_title").toString());
			videoData.put("is_more", videoShareMap.get("is_more") == null ? "" : videoShareMap.get("is_more").toString());
			videoData.put("seq", videoShareMap.get("seq") == null ? "" : videoShareMap.get("seq").toString());
		}
		
		ApiVipExclusiveGoodResult apiResult = new ApiVipExclusiveGoodResult();
		apiResult.setPriceUpData(priceUpData);
		apiResult.setPointData(pointData);
		apiResult.setBargainData(bargainData);
		apiResult.setActivityData(activityData);
		apiResult.setVideoData(videoData);
		return apiResult;
	}
}
