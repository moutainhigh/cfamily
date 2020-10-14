package com.cmall.familyhas.api.input;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;


/**
 * 闪购返回值
 * @author jlin
 *
 */
public class ApiForFlashActiveResult extends RootResultWeb {

	@ZapcomApi(value="闪购活动集合",demo="[]")
	private List<Activity> activeList=new LinkedList<Activity>();
	
	@ZapcomApi(value="系统时间",demo="2014-12-17 00:00:00")
	private String systemTime = "";
	
	@ZapcomApi(value = "栏目图片",demo="http://qhbeta-cfiles.qhw.srnpr.com/cfiles/staticfiles/upload/22676/2bf6697a4c1a4536bcec0b0c417a7356.jpg")
	private String banner_img = "";
	
	@ZapcomApi(value = "栏目名称",demo="品牌")
	private String banner_name = "";
	
	@ZapcomApi(value = "链接地址",demo="www.baidu.com")
	private String banner_link = "";
	
	public List<Activity> getActiveList() {
		return activeList;
	}


	public void setActiveList(List<Activity> activeList) {
		this.activeList = activeList;
	}


	public String getSystemTime() {
		return systemTime;
	}


	public void setSystemTime(String systemTime) {
		this.systemTime = systemTime;
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
	 * 活动
	 * @author jlin
	 *
	 */
	public static class Activity {
		
		@ZapcomApi(value="活动编码",demo="SG1410101003",remark="活动唯一编码")
		private String activity_code;
		
		@ZapcomApi(value="活动名称",demo="活动1")
		private String activity_name;
		
		@ZapcomApi(value="活动开始时间",demo="2015-05-22 16:00:00")
		private String start_time;
		
		@ZapcomApi(value="活动结束时间",demo="2015-05-22 17:59:59")
		private String end_time;
		
//		@ZapcomApi(value="活动下商品集合",demo="[]")
//		private List<ActiceProduct> productList= new LinkedList<ActiceProduct>();
		
		public String getActivity_code() {
			return activity_code;
		}
		public void setActivity_code(String activity_code) {
			this.activity_code = activity_code;
		}
		public String getActivity_name() {
			return activity_name;
		}
		public void setActivity_name(String activity_name) {
			this.activity_name = activity_name;
		}
		public String getStart_time() {
			return start_time;
		}
		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}
		public String getEnd_time() {
			return end_time;
		}
		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}
	}
	
	
	/**
	 * 活动商品
	 * @author jlin
	 *
	 */
	public static class ActiceProduct {
		
		@ZapcomApi(value="销售价",demo="33",remark="价格向下取整")
		private BigDecimal sell_price=new BigDecimal(0);
		
		@ZapcomApi(value="优惠价",demo="33",remark="价格向下取整")
		private BigDecimal vip_price=new BigDecimal(0);
		
		@ZapcomApi(value="商品名称",demo="【依波Ebohr】天翼超薄圆形白面石英表 女款 156142 白面")
		private String product_name="";
		
		@ZapcomApi(value="商品编码",demo="564874")
		private String product_code="";
		
		@ZapcomApi(value="促销库存",remark="商品下所有sku促销库存之和")
		private int sales_num=0;
		
		@ZapcomApi(value="是否有视频",demo="0",remark="1:有  0:无")
		private int is_video= 0;
		
		@ZapcomApi(value="卖出的数量",demo="100",remark="商品卖出的数量,默认为0")
		private int sell_count= 0 ;
		
		@ZapcomApi(value="图片地址",demo="",remark="用户列表展示的方图")
		private String img_url="";
		
		@ZapcomApi(value="广告地址",demo="",remark="当内容类型为1时，这个地址不为空")
		private String activity_url="";
		
		@ZapcomApi(value="图片或视频地址",demo="",remark="")
		private String file_url="";
		
		@ZapcomApi(value="打折",demo="￥20",remark="差价")
		private String discount="￥0";
		
		@ZapcomApi(value="折扣率",demo="50",remark="比值")
		private String discountRate="";
		
		@ZapcomApi(value="品详情的连接",demo="",remark="")
		private String goods_link="";
		
		@ZapcomApi(value="位置信息",demo="0")
		private int location=0;

		public BigDecimal getSell_price() {
			return sell_price;
		}

		public void setSell_price(BigDecimal sell_price) {
			this.sell_price = sell_price;
		}

		public BigDecimal getVip_price() {
			return vip_price;
		}

		public void setVip_price(BigDecimal vip_price) {
			this.vip_price = vip_price;
		}

		public String getProduct_name() {
			return product_name;
		}

		public void setProduct_name(String product_name) {
			this.product_name = product_name;
		}

		public String getProduct_code() {
			return product_code;
		}

		public void setProduct_code(String product_code) {
			this.product_code = product_code;
		}

		public int getSales_num() {
			return sales_num;
		}

		public void setSales_num(int sales_num) {
			this.sales_num = sales_num;
		}

		public int getIs_video() {
			return is_video;
		}

		public void setIs_video(int is_video) {
			this.is_video = is_video;
		}

		public int getSell_count() {
			return sell_count;
		}

		public void setSell_count(int sell_count) {
			this.sell_count = sell_count;
		}

		public String getImg_url() {
			return img_url;
		}

		public void setImg_url(String img_url) {
			this.img_url = img_url;
		}

		public String getActivity_url() {
			return activity_url;
		}

		public void setActivity_url(String activity_url) {
			this.activity_url = activity_url;
		}

		public String getFile_url() {
			return file_url;
		}

		public void setFile_url(String file_url) {
			this.file_url = file_url;
		}

		public String getDiscount() {
			return discount;
		}

		public void setDiscount(String discount) {
			this.discount = discount;
		}

		public String getDiscountRate() {
			return discountRate;
		}

		public void setDiscountRate(String discountRate) {
			this.discountRate = discountRate;
		}

		public String getGoods_link() {
			return goods_link;
		}

		public void setGoods_link(String goods_link) {
			this.goods_link = goods_link;
		}

		public int getLocation() {
			return location;
		}

		public void setLocation(int location) {
			this.location = location;
		}
		
	}
	
}
