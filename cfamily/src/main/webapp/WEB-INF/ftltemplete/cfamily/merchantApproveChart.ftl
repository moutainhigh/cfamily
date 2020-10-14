

<@m_zapmacro_common_page_book b_page />


<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >

	
	<#list e_page.upBookData()  as e>
		
	  	<@m_zapmacro_common_book_field e e_page/>
	  	
	</#list>
	
	
</form>
</#macro>
<#-- 显示页的自动输出判断 -->
<#macro m_zapmacro_common_book_field e_field   e_page>
	
	<@m_zapmacro_common_field_show e_field e_page/>
	<#if e_field.getFieldName() = "business_status">
			<div class="zab_info_page_title" w_clear>
				<span>组织机构信息</span>
			</div>
	<#elseif e_field.getFieldName() = "organization_number_validity_period_end">
			<div class="zab_info_page_title" w_clear>
				<span>税务登记信息</span>
			</div>
	<#elseif e_field.getFieldName() = "tax_identification_number">
			<div class="zab_info_page_title" w_clear>
				<span>开户行信息</span>
			</div>
	<#elseif e_field.getFieldName() = "joint_number">
			<div class="zab_info_page_title" w_clear>
				<span>售后信息</span>
			</div>							
	 </#if>
	 
	  	
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
		      			<#if e_field.getFieldName()=="register_money">
		      				${e_field.getPageFieldValue()?default("")}&nbsp;&nbsp;万元
		      			<#elseif e_field.getFieldName()=="quality_retention_money">
		      				${e_field.getPageFieldValue()?default("")}&nbsp;&nbsp;元
		      			<#elseif e_field.getFieldName()=="money_proportion">
			      			${e_field.getPageFieldValue()?eval*100}&nbsp;&nbsp;%
						<#else>
							${e_field.getPageFieldValue()?default("")}
						</#if>
					</#if>
				</div>
	<@m_zapmacro_common_field_end />
</#macro>

