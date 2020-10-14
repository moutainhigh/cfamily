<#assign uid = b_page.getReqMap()["zw_f_uid"]! >
<#assign xls = b_page.getReqMap()["xls"]! >
<#assign tips = b_page.getReqMap()["tips"]! >

<#if xls?? && xls != ''>
<div class="control-group">
    <div class="controls">
		<a class="btn  btn-success" href="../resources/cfamily/${xls}">下载模板</a>
	</div>
</div>
</#if>

<form class="form-horizontal" method="POST" >
	<input type="hidden" name="zw_f_uid" value="${uid!}">
		
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
</form>

<#if tips?? && tips != ''>
<p>${tips}</p>
</#if>