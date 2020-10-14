package com.cmall.familyhas.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.service.WXMusicAlbumService;
import com.cmall.familyhas.util.DateUtil;
import com.cmall.familyhas.util.HttpUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;
/**
 * 定时获取小程序直播房间号
 * @param context
 */
public class JobForGetProgramLive extends RootJob {
	private final String getliveroomurl = "https://api.weixin.qq.com/wxa/business/getliveinfo?access_token=%s";
	static AtomicInteger idx = new AtomicInteger();
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private WXMusicAlbumService service = new WXMusicAlbumService();
	public void doExecute(JobExecutionContext context) {
		String result = getliveinfo(0,20);
		if(StringUtils.isNotBlank(result)) {
			JSONObject parseObject = JSON.parseObject(result);
			Integer errcode = parseObject.getInteger("errcode");
			if(errcode == 0) {
				DbUp.upTable("fh_liveinfo").dataExec("update fh_liveinfo set live_status = '201' where live_status <> '201'", new MDataMap());
				handler(parseObject);
				Integer total = parseObject.getInteger("total");
				//如果总数比20大分批次拉取
				for(int i = 20;total - i > 0;i+=20) {
					result = getliveinfo(i,20);
					if(StringUtils.isNotBlank(result)) {
						errcode = parseObject.getInteger("errcode");
						if(errcode == 0) {
							handler(parseObject);
						}
					}
				}
			}
		}
	}
	public void handler(JSONObject parseObject) {
		JSONArray jsonArray = parseObject.getJSONArray("room_info");
		for(int i=0;i < jsonArray.size();i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String roomid = jsonObject.getString("roomid");
			MDataMap mWhereMap = new MDataMap();
			mWhereMap.put("roomid", roomid);
			int tmp = DbUp.upTable("fh_liveinfo").dataCount("roomid =:roomid", mWhereMap);
			//组织数据
			MDataMap insertDatamap = new MDataMap();
			insertDatamap.put("name", jsonObject.getString("name"));
			insertDatamap.put("roomid",roomid);
			insertDatamap.put("cover_img", jsonObject.getString("cover_img"));
			long start_time = jsonObject.getLongValue("start_time");
			long end_time = jsonObject.getLongValue("end_time");
			insertDatamap.put("start_time", DateUtil.toString(new Date(start_time*1000), DateUtil.DATE_FORMAT_DATETIME));
			insertDatamap.put("end_time", DateUtil.toString(new Date(end_time*1000), DateUtil.DATE_FORMAT_DATETIME));
			insertDatamap.put("anchor_name", jsonObject.getString("anchor_name"));
			insertDatamap.put("share_img", jsonObject.getString("share_img"));
			insertDatamap.put("live_status", jsonObject.getString("live_status"));
			Object object = jsonObject.get("anchor_img");
			if(object != null) {
				insertDatamap.put("anchor_img", jsonObject.getString("anchor_img"));
			}
			if(tmp > 0) {
				if(insertDatamap.get("anchor_img")!=null) {
					DbUp.upTable("fh_liveinfo").dataUpdate(insertDatamap, "name,cover_img,share_img,start_time,end_time,anchor_name,live_status,anchor_img", "roomid");
				}else {
					DbUp.upTable("fh_liveinfo").dataUpdate(insertDatamap, "name,cover_img,share_img,start_time,end_time,anchor_name,live_status", "roomid");
				}
				
			}else {
				DbUp.upTable("fh_liveinfo").dataInsert(insertDatamap);
			}
			JSONArray jsonGoods = jsonObject.getJSONArray("goods");
			DbUp.upTable("fh_livegoodinfo").delete("roomid",roomid);
			for(int j=0;j < jsonGoods.size();j++) {
				JSONObject jsonGood = jsonGoods.getJSONObject(j);
				MDataMap insertDataGood = new MDataMap();
				insertDataGood.put("cover_img", jsonGood.getString("cover_img"));
				insertDataGood.put("url", jsonGood.getString("url"));
				insertDataGood.put("price", jsonGood.getString("price"));
				insertDataGood.put("name", jsonGood.getString("name"));
				insertDataGood.put("price_type", jsonGood.getString("price_type") == null?"":jsonGood.getString("price_type"));
				insertDataGood.put("price_two", jsonGood.getString("price2") == null?"":jsonGood.getString("price2"));
				insertDataGood.put("roomid", roomid);
				DbUp.upTable("fh_livegoodinfo").dataInsert(insertDataGood);
			}
		}
	}
	public String getliveinfo(Integer start,Integer limit) {
		JSONObject param = new JSONObject();
		param.put("start", 0);
		param.put("limit", 20);
		String getliveroomurlformat = String.format(getliveroomurl, service.getToken());
		String result = HttpUtil.post(getliveroomurlformat, param.toJSONString(), "UTF-8", "application/json");
		return result;
	}
}