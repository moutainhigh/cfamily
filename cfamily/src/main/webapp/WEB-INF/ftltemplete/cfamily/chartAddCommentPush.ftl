
<script>
require(['zapadmin/js/commentSelectEvent'],

function()
{
	zapjs.f.ready(function()
		{
			commentSelectEvent.init();
		}
	);
}
);
</script>

<@m_zapmacro_common_page_add b_page />

<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
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
	  		<#if  e_field.getFieldName()=="jump_position">
		  		<div class="control-group" id="div_jump_position">
					<label class="control-label" for="zw_f_jump_position"><span class="w_regex_need">*</span>跳转位置：</label>
					<div class="controls">
						<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}">
					</div>
				</div>
				
				<div class="control-group" id="div_product">
					<label class="control-label" for="zw_f_sku_code">
						<span class="w_regex_need">*</span>商品名称：
					</label>
					<div class="controls">
						<div>
							<script type="text/javascript">
								zapjs.f.require(['zapadmin/js/securityProduct_single'],function(a){a.init({"text":"","source":"page_chart_v_security_pc_skuinfo","id":"zw_f_sku_code","value":"","max":"1"});});
							</script>
							<div class="w_left">
								<input id="zw_f_sku_name" type="text" value="" name="zw_f_sku_name" >
								<input class="btn" type="button" onclick="securityProduct_single.show_box('zw_f_sku_code')" value="选择商品">
							</div>
							<div class="w_left w_w_70p"><ul id="zw_f_sku_code_show_ul" class="zab_js_zapadmin_single_ul"></ul></div>
							<div class="w_clear"></div>
						</div>
					</div>
				</div>
	  		<#else>
	  			<@m_zapmacro_common_field_text  e_field />
	  		</#if>
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>
