package com.cmall.familyhas.api.burglary;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cmall.familyhas.util.MD5Util;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapcom.topapi.RootApi;

/**
 * 
 * 类: ApiBurglaryLink <br>
 * 描述: 防盗链接接口 <br>
 * 作者: zhy<br>
 * 时间: 2017年5月5日 下午3:15:22
 */
public class ApiBurglaryLink extends RootApi<ApiBurglaryLinkResult, ApiBurglaryLinkInput> {

	@Override
	public ApiBurglaryLinkResult Process(ApiBurglaryLinkInput inputParam, MDataMap mRequestMap) {
		ApiBurglaryLinkResult result = new ApiBurglaryLinkResult();
		try {
			String url = bConfig("familyhas.burglaryLink_href");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			String time = sdf.format(new Date());
			String sign = bConfig("familyhas.burglaryLink_href_sign") + time + "/JYGW/LIVE.m3u8";
			sign = MD5Util.md5Hex(sign);
			url = url + "?" + "tm=" + time + "&sign=" + sign;
			result.setUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
			result.setResultCode(-1);
			result.setResultMessage("获取链接失败，失败原因：" + e.getMessage());
		}
		return result;
	}

}
