package com.cmall.familyhas.service.cc;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.cmall.familyhas.api.model.CcVideo;
import com.cmall.familyhas.util.HttpUtil;
import com.cmall.familyhas.util.MD5Util;
import com.cmall.familyhas.util.QueryStringUtil;
import com.srnpr.zapcom.baseclass.BaseClass;

public class CCVideoService extends BaseClass{

	/**
	 * 获取视频图片以及时长
	 * @param CCvid
	 * @return
	 */
	public CcVideo getVideoInfo(String CCvid) {
    	String apiKey = bConfig("familyhas.cckey");
        String userId = bConfig("familyhas.ccuid");
        String videoid = CCvid;
        Map<String,String> map = new HashMap<String,String>();
		map.put("userid", userId);
		map.put("videoid", videoid);
		map.put("format", "json");
		String totalStr = QueryStringUtil.createHashedQueryString(map, System.currentTimeMillis(), apiKey);
        String videoHost = bConfig("familyhas.ccGetVideoInfoUrl");
        String allurl = videoHost.concat("?").concat(totalStr);
        String result = "";
		result =HttpUtil.post(allurl, "{}", "UTF-8");
		if(StringUtils.isEmpty(result)) {
			return null;
		}
		CcVideo resultMap = new CcVideo();
		JSONObject jo = JSONObject.parseObject(result);
		String video = jo.getString("video");
		if(StringUtils.isEmpty(video)) {
			return null;
		}
		JSONObject jv = JSONObject.parseObject(video);
		String duration = jv.getString("duration");//时长，单位：秒
		String image = jv.getString("image");
		image = image.replace("-0.jpg", "-1.jpg");
		resultMap.setTime(Integer.parseInt(duration));
		resultMap.setImg(image);
		resultMap.setCcvid(CCvid);
		return resultMap;
	}
	
	/**
	 * 获取视频的播放链接
	 * @param ccvid
	 * @return
	 * 2020-8-21
	 * Angel Joy
	 * String
	 */
	public String getPlayUrl(String ccvid) {
		String apiKey = bConfig("familyhas.cckey");
        String userId = bConfig("familyhas.ccuid");
        String videoid = ccvid;
        Date date = new Date();
        String time = date.getTime()+"";
        String params2 = "userid="+userId;
        String params3 = "videoid="+videoid;
        String totalStr = params2+"&"+params3;
        String params1 = "time="+time;
        String md5Parmas = MD5Util.MD5(totalStr+"&"+params1+"&salt="+apiKey);
        String videoHost = "http://union.bokecc.com/api/mobile";
        String allurl = videoHost.concat("?").concat(totalStr).concat("&").concat(params1).concat("&").concat("hash=").concat(md5Parmas);
        String result = "";
		result = HttpUtil.post(allurl, "{}", "UTF-8");
		if(StringUtils.isEmpty(result)) {
			return "";
		}
		String temp = HttpUtil.xmlElements(result); 
		return temp;
	}
}
