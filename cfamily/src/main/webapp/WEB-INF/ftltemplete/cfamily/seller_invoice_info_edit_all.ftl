
<@m_zapmacro_common_page_edit b_page />

<style>
.zab_info_page_title {
    height: 25px;
    margin-bottom: 20px;
    margin-top: 10px;
    border-bottom: solid 1px #008299;
    color: #fff;
}

.zab_info_page_title span {
    background-color: #008299;
    padding: 5px 10px 6px 10px;
    text-align: center;
}
</style>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
	   <#if e_field.getPageFieldName()=="zw_f_taxpayer_certificate_input">
	  		<div class="zab_info_page_title" w_clear >
				<span>开票信息</span>
			</div>
	  	<#elseif  e_field.getPageFieldName()=="zw_f_receiver_address">
	  	    <div class="zab_info_page_title" w_clear >
				<span>收件人信息</span>
			</div>
	  	<#else>
	  	</#if>
	
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
	  	<#elseif  e_field.getFieldTypeAid()=="104005023">
	  		<@m_zapmacro_common_field_upload_upgrade  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005024">
	  		<@m_zapmacro_common_field_upload_modify  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	   <#if  e_field.getPageFieldName()=='zw_f_small_seller_code'||e_field.getPageFieldName()=='zw_f_seller_company_name'||e_field.getPageFieldName()=='zw_f_tax_identification_number'>
	   <input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}" readOnly="true">
	   <#else>
	  <input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
	   </#if>
		
	<@m_zapmacro_common_field_end />
</#macro>


<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	<#if e_field.getPageFieldName()=='zw_f_account_clear_type' ||e_field.getPageFieldName()=='zw_f_uc_seller_type'>
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}" readOnly="true">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
	 <#else>
	           <select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
	 </#if>
	<@m_zapmacro_common_field_end />
</#macro>


