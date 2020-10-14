package com.cmall.familyhas.job;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.service.WXMusicAlbumService;
import com.cmall.familyhas.util.HttpUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.rootweb.RootJob;
/**
 * 定时获取小程序回放
 * @param context
 */
public class JobForGetPlayBack extends RootJob {
	
	private final String getlivePlayBackurl = "http://api.weixin.qq.com/wxa/business/getliveinfo?access_token=%s";
	static AtomicInteger idx = new AtomicInteger();
	static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	private WXMusicAlbumService service = new WXMusicAlbumService();
	public void doExecute(JobExecutionContext context) {
		MDataMap mWhereMap = new MDataMap();
		mWhereMap.put("live_status", "103");
		mWhereMap.put("status", "449746250001");
		String sql = "select li.roomid from fh_liveinfo li LEFT JOIN fh_livereplay lp ON li.roomid = lp.roomid WHERE li.`live_status` =:live_status AND li.`status` =:status AND lp.roomid is NULL";
		List<Map<String,Object>> dataSqlList = DbUp.upTable("fh_livegoodinfo").dataSqlList(sql, mWhereMap);
		for(Map<String,Object> map : dataSqlList) {
			Integer roomid = MapUtils.getInteger(map, "roomid");
			String result = getliveinfo(0,20,roomid);
			if(StringUtils.isNotBlank(result)) {
				JSONObject parseObject = JSON.parseObject(result);
				Integer errcode = parseObject.getInteger("errcode");
				if(errcode == 0) {
					handler(parseObject,roomid);
					Integer total = parseObject.getInteger("total");
					//如果总数比20大分批次拉取
					for(int i = 20;total - i > 0;i+=20) {
						result = getliveinfo(i,20,roomid);
						if(StringUtils.isNotBlank(result)) {
							errcode = parseObject.getInteger("errcode");
							if(errcode == 0) {
								handler(parseObject,roomid);
							}
						}
					}
				}
			}
		}
	}
	public void handler(JSONObject parseObject,Integer roomid) {
		JSONArray jsonArray = parseObject.getJSONArray("live_replay");
		for(int i=0;i < jsonArray.size();i++) {
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			String expire_time = jsonObject.getString("expire_time");
			String create_time = jsonObject.getString("create_time");
			expire_time = expire_time.replace("T", " ").replace("Z", "");
			create_time = create_time.replace("T", " ").replace("Z", "");
			String media_url = jsonObject.getString("media_url");
			//组织数据
			MDataMap insertDatamap = new MDataMap();
			insertDatamap.put("expire_time", expire_time);
			insertDatamap.put("create_time",create_time);
			insertDatamap.put("media_url", media_url);
			insertDatamap.put("roomid", roomid.toString());
			DbUp.upTable("fh_livereplay").dataInsert(insertDatamap);
		}
	}
	public String getliveinfo(Integer start,Integer limit,Integer roomid) {
		String token = service.getToken();
		String getlivePlayBackurlFormat = String.format(getlivePlayBackurl,token);
		JSONObject param = new JSONObject();
		param.put("start", 0);
		param.put("limit", 20);
		param.put("room_id", roomid);
		param.put("action", "get_replay");
		String result = HttpUtil.post(getlivePlayBackurlFormat, param.toJSONString(), "UTF-8", "application/json");
		return result;
	}
}