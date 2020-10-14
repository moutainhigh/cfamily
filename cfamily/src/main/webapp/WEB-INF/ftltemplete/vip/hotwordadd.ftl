<#assign app_list_code = b_method.upDataQueryToJson("uc_appinfo","app_code,app_name","","app_code='SI2003'") />

<script>
require(['cmanage/js/hotwordadd'],

function()
{
	zapjs.f.ready(function()
		{
			appColumnAddHost.init(${app_list_code});
		}
	);
}

);
</script>


<@m_zapmacro_common_page_add_hot b_page />

<#-- 添加页 -->
<#macro m_zapmacro_common_page_add_hot e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list_hot    e_page  />
	
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list_hot    e_page>


	  	<@m_zapmacro_common_auto_field_hot  e_page/>
	
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field_hot  e_page>
<div class="control-group">
	<label class="control-label" for="zw_f_app_code"><span class="w_regex_need">*</span>APP名称：</label>
	<div class="controls">

	      		<select name="zw_f_app_code_hot" id="zw_f_app_code_hot">

	      		</select>
	</div>
</div>
	  	

<div class="control-group">
	<label class="control-label" for="zw_f_page_codeZero_hot"><span class="w_regex_need">*</span>关键字：</label>
	<div class="controls">
	      		<input type="text" id="zw_f_column_nameZero_hot" name="zw_f_column_nameZero_hot"  value="">
	      		
	</div>
	
</div>
	  	


<div class="control-group">
	<label class="control-label" for="zw_f_page_codeOne_hot"><span class="w_regex_need">*</span>排序：</label>
	<div class="controls">
	      		<input type="text" id="zw_f_column_nameOne_hot" name="zw_f_column_nameOne_hot"  value="">
	      		
	</div>
	
</div>	  	
	  		

</#macro>




