package com.cmall.familyhas.api.result;

import java.util.ArrayList;
import java.util.List;

import com.cmall.familyhas.api.model.PageResults;
import com.cmall.familyhas.api.model.TodayProduct;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topdo.TopUp;
import com.srnpr.zapweb.webapi.RootResultWeb;

public class ApiForGetTVDataResult extends RootResultWeb {

	@ZapcomApi(value = "今日直播商品")
	private List<TodayProduct> products = new ArrayList<TodayProduct>();

	@ZapcomApi(value = "翻页结果")
	private PageResults paged = new PageResults();
	
	@ZapcomApi(value = "栏目图片",demo="http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/22676/2bf6697a4c1a4536bcec0b0c417a7356.jpg")
	private String banner_img = "";
	
	@ZapcomApi(value = "栏目名称",demo="品牌")
	private String banner_name = "";
	
	@ZapcomApi(value = "链接地址",demo="www.baidu.com")
	private String banner_link = "";
	
	@ZapcomApi(value="系统时间")
	private String systemDate = "";
	
	@ZapcomApi(value="页面轮播广告图",remark="list")
	private List<AdvertisementImg> imgList = new ArrayList<AdvertisementImg>();
	
	@ZapcomApi(value="视频模板是否开启直播互动标识",remark="0:不开启,1:开启")
	private String ifShowLiveInteractionFlag=TopUp.upConfig("familyhas.ifShowLiveInteractionFlag");
	
	@ZapcomApi(value = "TV直播链接",remark="直播互动需要此字段")
	private String videoUrlTV = TopUp.upConfig("familyhas.video_url_TV");
	
	@ZapcomApi(value="节目主持人信息列表",remark="list")
	private List<HostInfo> hostList = new ArrayList<HostInfo>();

	public List<HostInfo> getHostList() {
		return hostList;
	}

	public void setHostList(List<HostInfo> hostList) {
		this.hostList = hostList;
	}

	public String getVideoUrlTV() {
		return videoUrlTV;
	}

	public void setVideoUrlTV(String videoUrlTV) {
		this.videoUrlTV = videoUrlTV;
	}

	public String getIfShowLiveInteractionFlag() {
		return ifShowLiveInteractionFlag;
	}

	public void setIfShowLiveInteractionFlag(String ifShowLiveInteractionFlag) {
		this.ifShowLiveInteractionFlag = ifShowLiveInteractionFlag;
	}

	public List<TodayProduct> getProducts() {
		return products;
	}

	public void setProducts(List<TodayProduct> products) {
		this.products = products;
	}

	public List<AdvertisementImg> getImgList() {
		return imgList;
	}

	public void setImgList(List<AdvertisementImg> imgList) {
		this.imgList = imgList;
	}

	public PageResults getPaged() {
		return paged;
	}

	public void setPaged(PageResults paged) {
		this.paged = paged;
	}

	public String getBanner_img() {
		return banner_img;
	}

	public void setBanner_img(String banner_img) {
		this.banner_img = banner_img;
	}

	public String getBanner_name() {
		return banner_name;
	}

	public void setBanner_name(String banner_name) {
		this.banner_name = banner_name;
	}

	public String getBanner_link() {
		return banner_link;
	}

	public void setBanner_link(String banner_link) {
		this.banner_link = banner_link;
	}

	/**
	 * 获取  systemDate
	 */
	public String getSystemDate() {
		return systemDate;
	}

	/**
	 * 设置 
	 * @param systemDate 
	 */
	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}
	
	/**
	 * 闪购页面轮播
	 * @author fq
	 *
	 */
	public static class AdvertisementImg {
		
		@ZapcomApi(value="图片链接")
		private String imgUrl = "";
		
		@ZapcomApi(value="跳转链接",remark="url")
		private String forwardUrl = "";
		
		@ZapcomApi(value="图片宽度")
		private Integer width = 0;
		
		@ZapcomApi(value="图片高度")
		private Integer height = 0;
		
		public String getImgUrl() {
			return imgUrl;
		}
		
		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}
		
		public String getForwardUrl() {
			return forwardUrl;
		}
		
		public void setForwardUrl(String forwardUrl) {
			this.forwardUrl = forwardUrl;
		}

		public Integer getWidth() {
			return width;
		}

		public void setWidth(Integer width) {
			this.width = width;
		}

		public Integer getHeight() {
			return height;
		}

		public void setHeight(Integer height) {
			this.height = height;
		}
		
	}
	
	/**
	 * 主持人信息
	 * @author fq
	 *
	 */
	public static class HostInfo {
		
		@ZapcomApi(value="主持人ID编号")
		private String zid = "";
		
		@ZapcomApi(value="主持人姓名")
		private String hostName = "";
		
		@ZapcomApi(value="主持人头像")
		private String hostPic = "";

		public String getZid() {
			return zid;
		}

		public void setZid(String zid) {
			this.zid = zid;
		}

		public String getHostName() {
			return hostName;
		}

		public void setHostName(String hostName) {
			this.hostName = hostName;
		}

		public String getHostPic() {
			return hostPic;
		}

		public void setHostPic(String hostPic) {
			this.hostPic = hostPic;
		}	
		
	}

	
}
