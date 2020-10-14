<script>
require(['cfamily/js/modSkuStock'],function(a){a.init_sku()});
</script>
<@m_zapmacro_common_page_edit b_page />
<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field e e_page/>
	  	
	</#list>
	</#if>
</#macro>
<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
		<#if e_field.getPageFieldName()=="zw_f_stock_num">
		<div class="control-group">
				<label class="control-label" for="zw_f_stock_num">
					库存：
				</label>
			<div class="controls">
				<input type="text" disabled="disabled" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" value="${e_field.getPageFieldValue()}" />
			</div>
		</div>
		<#elseif e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005001">
	  		  <#-- 内部处理  不输出 -->
	  	<#elseif  e_field.getFieldTypeAid()=="104005003">
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
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
	  		<#-- 
	  		<@m_zapmacro_common_field_text  e_field />
	  		-->
		<div class="control-group time_between">
				<label class="control-label" for="zw_f_stock_num">
					开始时间：
				</label>
			<div class="controls">
				<input onclick="WdatePicker({maxDate:'#F{$dp.$D(\'start_date\',{d:-1});}'})" id="start_date"  name="start_date" type="text" value="">
			</div>
		</div>
		<div class="control-group time_between">
				<label class="control-label" for="zw_f_stock_num">
					结束时间：
				</label>
			<div class="controls">
				<input onclick="WdatePicker({maxDate:'#F{$dp.$D(\'end_date\',{d:-1});}'})" id="end_date" name="end_date" type="text" value="">
			</div>
		</div>
		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>
<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});" />
