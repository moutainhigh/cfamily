<script>
require(['cfamily/js/advertInfoAdd'],

function()
{
	zapjs.f.ready(function()
		{
			advertisementInfoAdd.init();
		}
	);
}

);
</script>

<@m_zapmacro_common_page_add b_page />


<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	<input type="hidden" id="zw_f_uniform_price_code" name="zw_f_uniform_price_code" value="" />
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
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
	  		<#if e_field.getPageFieldName() == 'zw_f_is_marketing'>
	  			<@m_zapmacro_common_field_select  e_field  e_page "请选择"/>
	  		<#else>
	  			<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  		</#if>
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<#if e_field.getPageFieldName() == 'zw_f_programa_num'>
	  		<div class="control-group">
				<label class="control-label" for="zw_f_programa_num"><span class="w_regex_need">*</span>广告栏数：</label>
				<div class="controls">
					<select name="zw_f_programa_num" id="zw_f_programa_num">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
		      		</select>
				</div>
			</div>
	  		<#else>
	  			<@m_zapmacro_common_field_text  e_field />
	  		</#if>
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>






