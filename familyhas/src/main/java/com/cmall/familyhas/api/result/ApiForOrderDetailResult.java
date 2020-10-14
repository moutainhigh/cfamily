package com.cmall.familyhas.api.result;

import java.util.LinkedList;
import java.util.List;

import com.srnpr.zapcom.baseannotation.ZapcomApi;
import com.srnpr.zapweb.webapi.RootResultWeb;


/**
 * 闪购返回值
 * @author jlin
 *
 */
public class ApiForOrderDetailResult extends RootResultWeb {

	@ZapcomApi(value="",demo="[]")
	private List<orderDetail> details=new LinkedList<orderDetail>();
	
	

	/**
	 * 订单详情
	 * @author jlin
	 *
	 */
	public static class orderDetail {
		
		@ZapcomApi(value="SKU编号")
		private String sku_code;
		
		@ZapcomApi(value="商品名称")
		private String sku_name;
		
		@ZapcomApi(value="商品数量")
		private int sku_num;

		public String getSku_code() {
			return sku_code;
		}

		public void setSku_code(String sku_code) {
			this.sku_code = sku_code;
		}

		public String getSku_name() {
			return sku_name;
		}

		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		public int getSku_num() {
			return sku_num;
		}

		public void setSku_num(int sku_num) {
			this.sku_num = sku_num;
		}
		
	}



	public List<orderDetail> getDetails() {
		return details;
	}



	public void setDetails(List<orderDetail> details) {
		this.details = details;
	}
	
	
	 
	
}
