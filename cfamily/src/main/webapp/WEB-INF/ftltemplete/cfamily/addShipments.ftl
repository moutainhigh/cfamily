<#assign code = b_page.getReqMap()["asale_code"] >
<@m_zapmacro_common_page_edit b_page />

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>
	<input name="zw_f_order_code" value="${code}" type="hidden">
	
	
	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field_filter e e_page/>
	  	
	</#list>
	</#if>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field_filter e_field   e_page>
	<#-- 只展示需要的字段 -->
	<#if e_field.getFieldName()=="logisticse_code"
		|| e_field.getFieldName()=="waybill"
		|| e_field.getFieldName()=="freight_money">
		<@m_zapmacro_common_auto_field e_field e_page/>
	</#if>
</#macro>

<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	<#if e_operate.getOperateLink() != " " && e_operate.getOperateLink() != "">
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" />
	</#if>
</#macro>