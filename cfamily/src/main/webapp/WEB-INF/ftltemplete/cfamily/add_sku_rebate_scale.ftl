
<@m_common_html_script "require(['cmanage/js/group_sku_rebate']);" />

<form class="form-horizontal" method="POST">
	<div class="control-group">
		<label class="control-label" for="zw_f_start_time">开始时间：</label>
		<div class="controls">
			<input type="text" id="zw_f_start_time" name="zw_f_start_time" value="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		</div>
		<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});" />
	</div>
	  	
	<div class="control-group">
		<label class="control-label" for="zw_f_end_time">结束时间：</label>
		<div class="controls">
			<input type="text" id="zw_f_end_time" name="zw_f_end_time"  value="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
		</div>
	</div>
	

	<div class="control-group">
		<label class="control-label" for="zw_f_rebate_scale"><span class="w_regex_need">*</span>返现比例：</label>
		<div class="controls">
			V1<input type="text" id="zw_f_rebate_scale" name="zw_f_rebate_scale"  value="" zapweb_attr_regex_id="469923180003">(百分比)<br/>
			V2<input type="text" id="zw_f_rebate_scale" name="zw_f_rebate_scale"  value="" zapweb_attr_regex_id="469923180003">(百分比)<br/>
			V3<input type="text" id="zw_f_rebate_scale" name="zw_f_rebate_scale"  value="" zapweb_attr_regex_id="469923180003">(百分比)<br/>
			V4<input type="text" id="zw_f_rebate_scale" name="zw_f_rebate_scale"  value="" zapweb_attr_regex_id="469923180003">(百分比)
		</div>
	</div>
	  	
<div class="control-group">
    <label class="control-label" for="zw_f_end_time">添加商品：</label>
	<div class="controls">
<input class="btn btn-small" onclick="group_sku_rebate.show_windows()" type="button" value="选择商品">
	</div>
</div>	 
 	
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <th>商品编码</th><th>SKU编号</th><th>商品名称</th><th>售价</th><th>商品状态</th><th>库存</th><th>操作</th>
	    </tr>
  	</thead>
  
	<tbody id="addskubody">
	</tbody>
</table>	  	               
<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
</form>

