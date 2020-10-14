<#assign  mess_note=b_method.upFiledByFieldName(b_page.upBookData(),"mess_note").getPageFieldValue() /> 
<#assign  flag_out_link=b_method.upFiledByFieldName(b_page.upBookData(),"flag_out_link").getPageFieldValue() /> 
<#assign  mess_title=b_method.upFiledByFieldName(b_page.upBookData(),"mess_title").getPageFieldValue() /> 
<#assign  out_link=b_method.upFiledByFieldName(b_page.upBookData(),"out_link").getPageFieldValue() /> 


<#if flag_out_link='449746250001'>
	<a href='${out_link}'>${mess_title}</a>
<#else>	
${mess_note}
</#if>
