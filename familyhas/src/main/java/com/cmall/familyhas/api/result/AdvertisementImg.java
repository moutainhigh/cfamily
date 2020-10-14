package com.cmall.familyhas.api.result;

import com.srnpr.zapcom.baseannotation.ZapcomApi;

/**
 * 闪购页面轮播
 * zb
 */
	public class AdvertisementImg {
		
		@ZapcomApi(value="图片链接")
		private String imgUrl = "";
		
		@ZapcomApi(value="图片宽",remark="picWidth")
		private Integer picWidth = new Integer(0);	

		@ZapcomApi(value="图片高",remark="picHeight")
		private Integer picHeight= new Integer(0);	
		
		@ZapcomApi(value="跳转链接",remark="url")
		private String forwardUrl = "";
		
		@ZapcomApi(value="跳转类型",remark=" 4497471600020001:URL跳转  4497471600020004：商品详情跳转")
		private String skipType = "4497471600020001";
		
		public Integer getPicWidth() {
			return picWidth;
		}

		public void setPicWidth(Integer picWidth) {
			this.picWidth = picWidth;
		}

		public Integer getPicHeight() {
			return picHeight;
		}

		public void setPicHeight(Integer picHeight) {
			this.picHeight = picHeight;
		}

		public String getSkipType() {
			return skipType;
		}

		public void setSkipType(String skipType) {
			this.skipType = skipType;
		}

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
		
	}