<@m_zapmacro_common_page_book b_page />

<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	<#assign categoryParams=e_page.upReqMap()>
	<#assign tree_level=categoryParams['tree_level']?trim>
	
	<#list e_page.upBookData()  as e>
		<#if tree_level=='4'&&e.getFieldNote()=='分类图片'>
			<@m_zapmacro_common_book_field e e_page/>
		<#elseif e.getFieldNote()=='分类名称'||e.getFieldNote()=='是否可用'>
			<@m_zapmacro_common_book_field e e_page/>
		</#if>
	</#list>
</form>
</#macro>

<#-- 显示页的自动输出判断 -->
<#macro m_zapmacro_common_book_field e_field   e_page>
	<@m_zapmacro_common_field_show e_field e_page/>
</#macro>

<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
	      		<div class="control_book">
		      		<#if  e_field.getFieldTypeAid()=="104005019">
		      			<#list e_page.upDataSource(e_field) as e_key>
							<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
						</#list>
					<#elseif e_field.getFieldTypeAid()=="104005021">
						<#if  e_field.getPageFieldValue()!="">
							<div class="w_left w_w_100">
									<a href="${e_field.getPageFieldValue()}" target="_blank">
									<img src="${e_field.getPageFieldValue()}">
									</a>
							</div> 
						</#if>
		      		<#else>
		      		${e_field.getPageFieldValue()?default("")}
		      		</#if>
	      		</div>
	<@m_zapmacro_common_field_end />
</#macro>