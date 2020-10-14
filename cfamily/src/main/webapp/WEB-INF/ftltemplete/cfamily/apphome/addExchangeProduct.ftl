<@m_common_html_script "require(['cfamily/js/apphome/addProduct']);" />
<@m_common_html_script "require(['lib/datepicker/WdatePicker']);" />
<#assign channelUid = b_page.getReqMap()["zw_f_channel_uid"]>

<form class="form-horizontal" method="POST">

<div class="control-group">
	<div class="controls">
	<input class="btn btn-small" type="button" value="选择商品" onclick="addProduct.show_windows()">
	</div>
</div>
	
<input type="hidden" id="zw_f_channel_uid" name="zw_f_channel_uid" value="${channelUid}">
	
	
<div class="control-group">
	<label class="control-label" for="zw_f_product_code"><span class="w_regex_need">*</span>商品编号：</label>
	<div class="controls">

		<input type="text" id="zw_f_product_code" name="zw_f_product_code" readonly="readonly" value="">
	</div>
</div>
	
<div class="control-group">
	<label class="control-label" for="zw_f_title"><span class="w_regex_need">*</span>商品名称：</label>
	<div class="controls">

		<input type="text" id="zw_f_title" name="zw_f_title" readonly="readonly" value="">
	</div>
</div>
	
		
<div class="control-group">
	<label class="control-label" for="zw_f_product_info"><span class="w_regex_need">*</span>连接类型：</label>
	<div class="controls">

		<select id="zw_f_product_info" name="zw_f_product_info" readonly="readonly">
			<option value="1">商品详情</option>
		</select>
	</div>
</div>

<div class="control-group">
	<label class="control-label" for="zw_f_stock_num"><span class="w_regex_need">*</span>商品库存：</label>
	<div class="controls">

		<input type="text" id="zw_f_stock_num" name="zw_f_stock_num" readonly="readonly" value="">
	</div>
</div>

<div class="control-group">
	<label class="control-label" for="zw_f_start_time"><span class="w_regex_need">*</span>开始时间：</label>
	<div class="controls">

		<input type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})" id="zw_f_start_time" name="zw_f_start_time"  value="">
	</div>
</div>
	  	

<div class="control-group">
	<label class="control-label" for="zw_f_end_time"><span class="w_regex_need">*</span>结束时间：</label>
	<div class="controls">

		<input type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})" id="zw_f_end_time" name="zw_f_end_time"  value="">
	</div>
</div>
	  	
		
	

<div class="control-group">
	<label class="control-label" for="zw_f_one_allowed"><span class="w_regex_need">*</span>限制兑换数量：</label>
	<div class="controls">

		<select id="zw_f_one_allowed" name="zw_f_one_allowed">
			<option value="0">不限制</option>
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3">3</option>
			<option value="4">4</option>
			<option value="5">5</option>
			<option value="6">6</option>
			<option value="7">7</option>
			<option value="8">8</option>
			<option value="9">9</option>
			<option value="10">10</option>
		</select>
	</div>
</div>
	  	
	

<div class="control-group">
	<label class="control-label" for="zw_f_jf_cost"><span class="w_regex_need">*</span>兑换积分：</label>
	<div class="controls">

		<input type="text" id="zw_f_jf_cost" name="zw_f_jf_cost"  value="">
		<label class=""><span class="w_regex_need">200的整倍数、此区域填写倍数即可</span></label>
	</div>
</div>

<div class="control-group">
	<label class="control-label" for="zw_f_allow_count"><span class="w_regex_need">*</span>最大库存数：</label>
	<div class="controls">

		<input type="text" id="zw_f_allow_count" name="zw_f_allow_count"  value="">
	</div>
</div>
	  	
<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
</form>

