<div class="control-group">
    <div class="controls">
		<a class="btn  btn-success" href="../resources/cfamily/product_bfd.xls">下载模板</a>
	</div>
</div>

<form class="form-horizontal" method="POST" >
	<div class="control-group">
		<label class="control-label" for="zw_f_day">日期</label>
		<div class="controls">
			<input type="text" onclick="WdatePicker({})" id="zw_f_day" name="zw_f_day" value="">
		</div>
	</div>			
	<div class="control-group">
		<label class="control-label" for="uploadFile">上传文件</label>
		<div class="controls">
			<input type="hidden" zapweb_attr_target_url="${b_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="" id="uploadFile" name="uploadFile" value="">
			<span class="control-upload_iframe"></span>
			<span class="control-upload_process"></span>
			<span class="control-upload"></span>
		</div>
	</div>	
	
	<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('uploadFile','"+b_page.upConfig("zapweb.upload_target")+"');}); });" />
	
	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
	
	<div class="control-group">
	<span class='w_regex_need'>*</span>注：如果选择的日期有数据则原有数据会被清除</label>
	</div>	
</form>
<script type="text/javascript">
	zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});
</script>