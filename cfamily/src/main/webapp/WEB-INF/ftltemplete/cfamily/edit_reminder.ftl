
<script>
require(['cfamily/js/reminder'],

function()
{
	zapjs.f.ready(function()
		{
			reminder.init(2);
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
			<div style="margin-top:-9px;width:400px">
				<input id="zw_f_seller_codes" type="hidden" value="${e_field.getPageFieldValue()}" name="zw_f_seller_codes">
				<input id="zw_f_seller_codes_show_text" type="hidden" value="">
				<script type="text/javascript">
					zapjs.f.require(['cfamily/js/seller_select'],function(a){a.init({"text":"","source":"page_chart_v_uc_sellerinfo_multiSelect","id":"zw_f_seller_codes","max":"","value":""});});
				</script>
			</div>
			<p></p>
			<table id="sellerTable"></table>
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
	</#if>
	<@m_zapmacro_common_field_end />
</#macro>