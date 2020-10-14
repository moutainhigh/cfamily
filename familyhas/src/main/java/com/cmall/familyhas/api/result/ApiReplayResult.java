package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

public class ApiReplayResult {
	@ZapcomApi(value = "视频过期时间",remark = "视频过期时间")
	private String expire_time;
	@ZapcomApi(value = "视频创建时间",remark = "视频创建时间")
    private String create_time;
	@ZapcomApi(value = "视频回放url",remark = "视频回放url")
    private String media_url;
	public String getExpire_time() {
		return expire_time;
	}
	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getMedia_url() {
		return media_url;
	}
	public void setMedia_url(String media_url) {
		this.media_url = media_url;
	}

    
}
