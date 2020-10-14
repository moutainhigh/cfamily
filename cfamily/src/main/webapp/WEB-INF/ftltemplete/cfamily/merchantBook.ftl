<@m_common_html_script "require(['cfamily/js/merchantReset']);" />
<div class="zab_info_page">
	<div class="zab_info_page_title  w_clear">
		<span>第三方用户信息-查看</span>
	</div>
	<@m_zapmacro_common_page_book b_page />
	
</div>

<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<#assign  userName=b_method.upFiledByFieldName(b_page.upBookData(),"user_name").getPageFieldValue() />
<input type="hidden" id="userName" value="${userName}">
<form class="form-horizontal" method="POST" >
	<#list e_page.upBookData()  as e>
	  	<@m_zapmacro_common_book_field e e_page/>
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
		      			<#if (e_field.getFieldName() == "flag_enable")>
		      				<#if e_field.getPageFieldValue() == "0">
								不可用
							<#elseif e_field.getPageFieldValue() == "1">
								可用
							</#if>
		      			<#else>
		      				${e_field.getPageFieldValue()?default("")}
		      			</#if>
		      		</#if>
	      		</div>
	<@m_zapmacro_common_field_end />
</#macro>

<div class="form-horizontal control-group">
	<input class="btn btn-success" type="button" value="重置密码" onclick="merchantReset.resetPwd()"/>
</div>


