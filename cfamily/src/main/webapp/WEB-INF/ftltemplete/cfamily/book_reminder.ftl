
<script>
require(['cfamily/js/book_reminder'],

function()
{
	zapjs.f.ready(function()
		{
			book_reminder.init();
		}
	);
}

);
</script>

<@m_zapmacro_common_page_edit b_page />

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
		<#if e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005001">
	  		  <#-- 内部处理  不输出 -->
	  	<#elseif  e_field.getFieldTypeAid()=="104005003">
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
		<#elseif  e_field.getFieldTypeAid()=="104005022">
  			<@m_zapmacro_common_field_datesfm  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<#if (e_field.getFieldName()=="seller_codes")>
	  			<div id="pointDiv" style="display:none">
	  				<@m_zapmacro_common_field_text  e_field />
	  			</div>
  			<#else>
  				<@m_zapmacro_common_field_text  e_field />
	  		</#if>
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>


<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if (e_field.getFieldName()=="seller_codes")>
			<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			<table id="sellerTable"></table>
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
	</#if>
	<@m_zapmacro_common_field_end />
</#macro>

<#-- 字段：上传 -->
<#macro m_zapmacro_common_field_upload e_field e_page>
	<#if (e_field.getFieldName()=="pic_url")>
		<div class="control-group">
			<label class="control-label" for="zw_f_content">提示图片</label>
			<div class="controls">
				<div class="w_left w_w_100">
					<a href="${e_field.getPageFieldValue()}" target="_blank">
						<img src="${e_field.getPageFieldValue()}">
					</a>
				</div>
			</div>
		</div>
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			<input type="hidden" zapweb_attr_target_url="${e_page.upConfig("zapweb.upload_target")}" zapweb_attr_set_params="${e_field.getSourceParam()}"    id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}">
			<span class="control-upload_iframe"></span>
			<span class="control-upload_process"></span>
			<span class="control-upload"></span>
		<@m_zapmacro_common_field_end />
		<@m_zapmacro_common_html_script "zapjs.f.ready(function(){zapjs.f.require(['zapweb/js/zapweb_upload'],function(a){a.upload_file('"+e_field.getPageFieldName()+"','"+e_page.upConfig("zapweb.upload_target")+"');}); });" />
	</#if>
	
	  
</#macro>