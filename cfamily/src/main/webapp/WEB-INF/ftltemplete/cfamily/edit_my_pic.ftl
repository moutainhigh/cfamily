

<@m_zapmacro_common_page_edit b_page />


<#-- 字段：上传 -->
<#macro m_zapmacro_common_field_upload e_field e_page>
	<#if e_field.getPageFieldName() == 'zw_f_photo_url'>
		<span style="float:left;margin-left:370px;position: absolute;line-height:32px;color:red">建议上传尺寸1080*800</span>
	</#if>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e_field.getSourceParam()}"    id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
		<span class="control-upload_iframe"></span>
		<span class="control-upload_process"></span>
		<span class="control-upload"></span>
		
	<@m_zapmacro_common_field_end />
	<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('"+e_field.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"');}); });" />
</#macro>


