<@m_zapmacro_common_page_book b_page />


<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >
	
	<#list e_page.upBookData()  as e>
	  	<#if e.getFieldName()?? && e.getFieldName()=="qa_title">
	  			<h4 align="center">${e.getPageFieldValue()}</h4><hr size=2px style="color:#000;"/>
	  	</#if>
	  	<#if e.getFieldName()?? && e.getFieldName()=="qa_content">
	  		<p style="padding: 20px; line-height:25px;">${e.getPageFieldValue()}</p>
	  	</#if>
	  	
	</#list>
	
</form>
</#macro>

