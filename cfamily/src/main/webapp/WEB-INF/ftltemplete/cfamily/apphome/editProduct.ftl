<@m_common_html_script "require(['cfamily/js/apphome/addProduct']);" />
<@m_common_html_script "require(['lib/datepicker/WdatePicker']);" />
<#assign uid = b_page.getReqMap()["zw_f_uid"]>

<form class="form-horizontal" method="POST">

<div class="control-group">
	<div class="controls">
	<input class="btn btn-small" type="button" value="选择商品" onclick="addProduct.show_windows()">
	</div>
</div>
	
<input type="hidden" id="zw_f_uid" name="zw_f_uid" value="${uid}">

<#assign extracharge=b_method.upClass("com.cmall.familyhas.service.apphome.GetDetailsInfo")>
<#assign prchType=extracharge.getPrchType(uid)>
<input type="hidden" id="zw_f_channel_uid" name="zw_f_channel_uid" value="${prchType.channel_uid?if_exists}">
<div class="control-group">
	<label class="control-label" for="zw_f_product_code"><span class="w_regex_need">*</span>商品编号：</label>
	<div class="controls">

		<input type="text" id="zw_f_product_code" name="zw_f_product_code" readonly="readonly" value="${prchType.product_code?if_exists}">
	</div>
</div>
	
<div class="control-group">
	<label class="control-label" for="zw_f_title"><span class="w_regex_need">*</span>商品名称：</label>
	<div class="controls">

		<input type="text" id="zw_f_title" name="zw_f_title" readonly="readonly" value="${prchType.title?if_exists}">
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

		<input type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})" id="zw_f_start_time" name="zw_f_start_time"  value="${prchType.start_time?if_exists}">
	</div>
</div>
	  	

<div class="control-group">
	<label class="control-label" for="zw_f_end_time"><span class="w_regex_need">*</span>结束时间：</label>
	<div class="controls">

		<input type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})" id="zw_f_end_time" name="zw_f_end_time"  value="${prchType.end_time?if_exists}">
	</div>
</div>
	  	
<input type="hidden" id="_one_allowed" value="${prchType.one_allowed?if_exists}"/>
<script type="text/javascript">
	function myfun() {
		var one_allowed = $("#_one_allowed").val();
		$("#zw_f_one_allowed").val(one_allowed);
		var obj = {};
		obj.product_code = $("#zw_f_product_code").val();
		zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetProductForFlash',obj,function(result) {
			if(result.resultCode==1){
				//设置值
				$('#zw_f_product_code').val(result.product_code);
				$('#zw_f_title').val(result.product_name);
//				$('#zw_f_product_status').val(result.product_status);
				$('#zw_f_stock_num').val(result.product_stock);
//				$('#zw_f_sell_price').val(result.sell_price);
				zapjs.f.window_close(sId);
			}else{
				zapadmin.model_message('系统交互异常，获取当前商品库存数失败');
			}
		});
	}
	window.onload=myfun;//不要括号
</script>
<div class="control-group">
	<label class="control-label" for="zw_f_one_allowed"><span class="w_regex_need">*</span>限制兑换数量：</label>
	<div class="controls">

		<select id="zw_f_one_allowed" name="zw_f_one_allowed" value="">
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

		<input type="text" id="zw_f_jf_cost" name="zw_f_jf_cost"  value="${prchType.jf_cost?if_exists}">
		<label class=""><span class="w_regex_need">200的整倍数、此区域填写倍数即可</span></label> 
	</div>
</div>
	  	
		
	

<div class="control-group">
	<label class="control-label" for="zw_f_extra_charges"><span class="w_regex_need">*</span>设置价格：</label>
	<div class="controls">

		<input type="text" id="zw_f_extra_charges" name="zw_f_extra_charges"  value="${prchType.extra_charges?if_exists}">
	</div>
</div>

<div class="control-group">
	<label class="control-label" for="zw_f_allow_count"><span class="w_regex_need">*</span>活动允许最大库存：</label>
	<div class="controls">

		<input type="text" id="zw_f_allow_count" name="zw_f_allow_count"  value="${prchType.allow_count?if_exists}">
	</div>
</div>
	  	
<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
</form>

