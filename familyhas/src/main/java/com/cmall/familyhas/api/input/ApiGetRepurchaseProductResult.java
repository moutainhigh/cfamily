package com.cmall.familyhas.api.input;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import com.srnpr.xmasorder.model.RepurchaseEvent;
import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapcom.topapi.RootResult;


/**
 * 
 * @author zhangbo
 *  加价购返回结果
 */
public class ApiGetRepurchaseProductResult extends RootResult{
	
	@ZapcomApi(value="加价购活动下商品集合",demo="[]")
	private List<ActiceProduct> productList= new LinkedList<ActiceProduct>();
	
	@ZapcomApi(value="总页数",remark="返回商品总页数")
	private int pageNum;
	
	@ZapcomApi(value="换购总金额")
	private String sumRepurchaseMoney = "0";
	
	@ZapcomApi(value="换购活动信息")
	private RepurchaseEvent repurchaseEvent = new RepurchaseEvent();;
		
	@ZapcomApi(value="用户已经选择了的商品数量")
	private int selectCount=0;
	

	public int getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(int selectCount) {
		this.selectCount = selectCount;
	}

	public RepurchaseEvent getRepurchaseEvent() {
		return repurchaseEvent;
	}

	public void setRepurchaseEvent(RepurchaseEvent repurchaseEvent) {
		this.repurchaseEvent = repurchaseEvent;
	}

	public String getSumRepurchaseMoney() {
		return sumRepurchaseMoney;
	}

	public void setSumRepurchaseMoney(String sumRepurchaseMoney) {
		this.sumRepurchaseMoney = sumRepurchaseMoney;
	}

	public List<ActiceProduct> getProductList() {
		return productList;
	}



	public void setProductList(List<ActiceProduct> productList) {
		this.productList = productList;
	}



	public int getPageNum() {
		return pageNum;
	}



	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}



	/**
	 * 活动商品
	 * @author zhangbo
	 *
	 */
	public static class ActiceProduct {
		
		@ZapcomApi(value="是否勾选",demo="",remark="1:勾选了 0未勾选")
		private String selectedFlag="0";
		
		@ZapcomApi(value="销售价",demo="33",remark="价格向下取整")
		private BigDecimal sell_price=new BigDecimal(0);
		
		@ZapcomApi(value="加价购活动优惠价",demo="33",remark="价格向下取整")
		private BigDecimal jjg_price=new BigDecimal(0);
		
		@ZapcomApi(value="商品名称",demo="【依波Ebohr】天翼超薄圆形白面石英表 女款 156142 白面")
		private String product_name="";
		
		@ZapcomApi(value="商品编码",demo="564874")
		private String product_code="";
		
		@ZapcomApi(value="sku编码",demo="564874")
		private String sku_code="";
		
		@ZapcomApi(value="sku规格",demo="")
		private String standard="";
		
		@ZapcomApi(value="图片地址",demo="",remark="用户列表展示的方图")
		private String mainpic_url="";

		@ZapcomApi(value="促销库存",remark="商品下所有sku促销库存之和")
		private int sales_num=0;
		
		@ZapcomApi(value="sku名称")
		private String sku_name="";
		
		@ZapcomApi(value="商品状态",remark="4497471600050002：售罄，4497153900060002 ：上架")
		private String productStatus="";

		@ZapcomApi(value="所有库存和，用于库存不足提醒使用")
		private int allSkuRealStoc=0;
		
		public int getAllSkuRealStoc() {
			return allSkuRealStoc;
		}

		public void setAllSkuRealStoc(int allSkuRealStoc) {
			this.allSkuRealStoc = allSkuRealStoc;
		}

		/*@ZapcomApi(value="品详情的连接",demo="",remark="")
		private String goods_link="";
		
		public String getGoods_link() {
			return goods_link;
		}

		public void setGoods_link(String goods_link) {
			this.goods_link = goods_link;
		}*/
		@ZapcomApi(value="自营标签")
		private String proClassifyTag="";
		
		public String getProductStatus() {
			return productStatus;
		}

		public void setProductStatus(String productStatus) {
			this.productStatus = productStatus;
		}
		
		public String getProClassifyTag() {
			return proClassifyTag;
		}

		public void setProClassifyTag(String proClassifyTag) {
			this.proClassifyTag = proClassifyTag;
		}

		public String getSku_name() {
			return sku_name;
		}

		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		public String getSelectedFlag() {
			return selectedFlag;
		}

		public void setSelectedFlag(String selectedFlag) {
			this.selectedFlag = selectedFlag;
		}

		public BigDecimal getSell_price() {
			return sell_price;
		}

		public void setSell_price(BigDecimal sell_price) {
			this.sell_price = sell_price;
		}

		public BigDecimal getJjg_price() {
			return jjg_price;
		}

		public void setJjg_price(BigDecimal jjg_price) {
			this.jjg_price = jjg_price;
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

		public String getSku_code() {
			return sku_code;
		}

		public void setSku_code(String sku_code) {
			this.sku_code = sku_code;
		}

		public String getStandard() {
			return standard;
		}

		public void setStandard(String standard) {
			this.standard = standard;
		}

		public String getMainpic_url() {
			return mainpic_url;
		}

		public void setMainpic_url(String mainpic_url) {
			this.mainpic_url = mainpic_url;
		}

		public int getSales_num() {
			return sales_num;
		}

		public void setSales_num(int sales_num) {
			this.sales_num = sales_num;
		}

		

	}
}
	
