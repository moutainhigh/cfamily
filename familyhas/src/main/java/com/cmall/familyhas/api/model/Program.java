package com.cmall.familyhas.api.model;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 节目
 * @author fq
 *
 */
public class Program {
	
	@ZapcomApi(value="视频图片")
	private String vedio_img = "";
	@ZapcomApi(value="节目标题")
	private String title = "";
	@ZapcomApi(value="播放日期",remark="格式为yyyymmdd，例如：20150401")
	private String play_time = "";
	@ZapcomApi(value="期数",remark="格式为第XX集，例如：第三十九集")
	private String count = "";
	@ZapcomApi(value="播放时长")
	private String on_time = "";
	@ZapcomApi(value="链接")
	private String link = "";
	
	public String getVedio_img() {
		return vedio_img;
	}
	public void setVedio_img(String vedio_img) {
		this.vedio_img = vedio_img;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPlay_time() {
		return play_time;
	}
	public void setPlay_time(String play_time) {
		this.play_time = play_time;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getOn_time() {
		return on_time;
	}
	public void setOn_time(String on_time) {
		this.on_time = on_time;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
