<@m_common_html_script "require(['cfamily/js/contractTypeAdd']);" />

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
	<#if e.getPageFieldName() = "zw_f_contract_type_name">
		<div class="control-group">
			<label class="control-label" for="zw_f_contract_type_name">
			<span class="w_regex_need">*</span>
			合同类型名称：
			</label>
			<div id="cp" class="controls">
				<input id="zw_f_contract_type_name" type="text" value="" zapweb_attr_regex_id="469923180002" name="zw_f_contract_type_name">
				<input class="btn" type="button" value="添加" onclick="contractTypeAdd.addType()">
			</div>
		</div>
	<#else>			
  		<@m_zapmacro_common_auto_field e e_page/>
	</#if>
	  	
	</#list>
	</#if>
</#macro>


