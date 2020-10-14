<@m_common_html_script "require(['cmanage/js/flashsalesSku']);" />
<#assign activityUid = b_page.getReqMap()["zw_f_uid"]>
<#assign activity_code_map = b_method.upDataOne("oc_activity_flashsales","activity_code","","uid=:uid","uid",activityUid)>
<#assign activity_code = activity_code_map['activity_code'] >

<form class="form-horizontal" method="POST">

<div class="control-group">
	<div class="controls">
<input class="btn btn-small" onclick="flashsalesSku.show_windows('${activity_code}')" type="button" value="选择商品">
	</div>
</div>


	
<input type="hidden" id="zw_f_activity_code" name="zw_f_activity_code" value="${activity_code}">
		
	

<div class="control-group">
	<label class="control-label" for="zw_f_sku_code"><span class="w_regex_need">*</span>SKU 编号：</label>
	<div class="controls">

		<input type="text" id="zw_f_sku_code" name="zw_f_sku_code" readonly="readonly" value="">
	</div>
</div>
	  	
<div class="control-group">
	<label class="control-label" for="zw_f_product_name"><span class="w_regex_need">*</span>商品名称：</label>
	<div class="controls">

		<input type="text" id="zw_f_product_name" name="zw_f_product_name" readonly="readonly" value="">
	</div>
</div>
	

<div class="control-group">
	<label class="control-label" for="zw_f_sku_name"><span class="w_regex_need">*</span>SKU 名称：</label>
	<div class="controls">

		<input type="text" id="zw_f_sku_name" name="zw_f_sku_name" readonly="readonly" value="">
	</div>
</div>
	  	
		
<div class="control-group">
	<label class="control-label" for="zw_f_product_status"><span class="w_regex_need">*</span>商品状态：</label>
	<div class="controls">

		<input type="text" id="zw_f_product_status" name="zw_f_product_status" readonly="readonly" value="">
	</div>
</div>
		
	

<div class="control-group">
	<label class="control-label" for="zw_f_stock_num"><span class="w_regex_need">*</span>库存数：</label>
	<div class="controls">

		<input type="text" id="zw_f_stock_num" name="zw_f_stock_num" readonly="readonly" value="">
	</div>
</div>
	  	
		
	

<div class="control-group">
	<label class="control-label" for="zw_f_sales_num"><span class="w_regex_need">*</span>促销库存：</label>
	<div class="controls">

		<input type="text" id="zw_f_sales_num" name="zw_f_sales_num" zapweb_attr_regex_id="469923180003" value="">
	</div>
</div>
	  	
		
	

<div class="control-group">
	<label class="control-label" for="zw_f_sell_price"><span class="w_regex_need">*</span>销售价：</label>
	<div class="controls">

		<input type="text" id="zw_f_sell_price" name="zw_f_sell_price" readonly="readonly" value="">
	</div>
</div>
	  	
		
	

<div class="control-group">
	<label class="control-label" for="zw_f_vip_price"><span class="w_regex_need">*</span>优惠价：</label>
	<div class="controls">

		<input type="text" id="zw_f_vip_price" name="zw_f_vip_price" zapweb_attr_regex_id="469923180007" value="">
	</div>
</div>
	  	
		
	

<div class="control-group">
	<label class="control-label" for="zw_f_purchase_limit_vip_num"><span class="w_regex_need">*</span>每会员限购数：</label>
	<div class="controls">

		<input type="text" id="zw_f_purchase_limit_vip_num" name="zw_f_purchase_limit_vip_num" zapweb_attr_regex_id="469923180003" value="">
	</div>
</div>
	  	
		
	

<div class="control-group">
	<label class="control-label" for="zw_f_purchase_limit_order_num"><span class="w_regex_need">*</span>每单限购数量：</label>
	<div class="controls">

		<input type="text" id="zw_f_purchase_limit_order_num" name="zw_f_purchase_limit_order_num" zapweb_attr_regex_id="469923180003" value="">
	</div>
</div>

<div class="control-group">
	<label class="control-label" for="zw_f_purchase_limit_day_num"><span class="w_regex_need">*</span>每日限购数量：</label>
	<div class="controls">

		<input type="text" id="zw_f_purchase_limit_day_num" name="zw_f_purchase_limit_day_num" zapweb_attr_regex_id="469923180003" value="">
	</div>
</div>
	  	

<div class="control-group">
	<label class="control-label" for="zw_f_location"><span class="w_regex_need">*</span>位置：</label>
	<div class="controls">

		<input type="text" id="zw_f_location" name="zw_f_location" zapweb_attr_regex_id="469923180003" value="0">
	</div>
</div>
	  	
	  	
<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
</form>

