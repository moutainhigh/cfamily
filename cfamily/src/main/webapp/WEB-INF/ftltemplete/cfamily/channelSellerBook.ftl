
<@m_zapmacro_common_page_book b_page />

<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	
	<#list e_page.upBookData()  as e>
		
		<#if (e_index <= 5)>
	  		<@m_zapmacro_common_book_field e e_page/>
      	</#if>
	  	
	</#list>
	
	<div class="zab_info_page_title  w_clear">
		<span>分销渠道发票信息</span>
	</div>
	
	<#list e_page.upBookData()  as e>
		
		<#if (e_index > 5)>		
	  		<@m_zapmacro_common_book_field e e_page/>
		<#elseif (e_index == 1)>
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
			<#elseif  e_field.getFieldTypeAid()=="104005103">
				<#list e_page.upDataSource(e_field) as e_key>
					<input type="checkbox" disabled="disabled" value="${e_key.getV()}" 
						<#if  (((e_field.getPageFieldValue()+",")?index_of(e_key.getV()+","))>-1)> checked="checked" </#if> name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}_${e_key_index}"/>
					<label for="${e_field.getPageFieldName()}_${e_key_index}">${e_key.getK()}</label>
						
				</#list>
      		<#else>
      			${e_field.getPageFieldValue()?default("")}
      		</#if>
  		</div>
	<@m_zapmacro_common_field_end />
</#macro>


