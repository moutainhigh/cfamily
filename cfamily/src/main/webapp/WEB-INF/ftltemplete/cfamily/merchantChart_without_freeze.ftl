<@m_zapmacro_common_page_chart b_page />
<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page>

	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>

<#-- 查询区域 -->
<#macro m_zapmacro_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_zapmacro_common_auto_inquire e_page />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />
	</form>
</#macro>

<#--查询的自动输出判断 -->
<#macro m_zapmacro_common_auto_inquire e_page>
	<#list e_page.upInquireData() as e>
	
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009020">
			<@m_zapmacro_common_field_betweensfm e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->

	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	  	
	</#list>
	<#--兼容form中input如果只有一个自动提交的情况-->
	<input style="display:none;"/>
</#macro>


<#-- 字段：文本范围 -->
<#macro m_zapmacro_common_field_between e_field  e_page >

	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_page.upConst("126022001",e_field.getPageFieldName(),"between_from") />

	    		从
	      		<input type="text" <#if e_field.getFieldTypeAid()=="104005004">  onClick="WdatePicker({dateFmt:'yyyy-MM',maxDate:'#F{$dp.$D(\'${e_page.upConst("126022001",e_field.getPageFieldName(),"between_to")}\',{d:-1});}'})"  </#if>   id="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_from")}" name="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_from")}" value="${e_page.upReqValue(e_page.upConst("126022001",e_field.getPageFieldName(),"between_from"))?default("")}">
	      		到
	      		<input type="text" <#if e_field.getFieldTypeAid()=="104005004">  onClick="WdatePicker({dateFmt:'yyyy-MM',minDate:'#F{$dp.$D(\'${e_page.upConst("126022001",e_field.getPageFieldName(),"between_from")}\',{d:1});}'})"</#if>  id="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_to")}" name="${e_page.upConst("126022001",e_field.getPageFieldName(),"between_to")}" value="${e_page.upReqValue(e_page.upConst("126022001",e_field.getPageFieldName(),"between_to"))?default("")}">
	      		
	      		
	<@m_zapmacro_common_field_end />
	  
	<@m_zapmacro_common_html_script "zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});" />
	  
</#macro>


<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	       <#list e_pagedata.getPageHead() as e_list>
		       <#if (e_list_index >1)>
			      	 <th>
			      	 	${e_list}
			      	 </th>
			   </#if>
	      </#list>
	    </tr>
  	</thead>
  	
  	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
				<#list e_list as e>
		 			<#if (e_index >1)>
				  	  <#if e_index = 12>
				  	  		<#if e_list[10] == '未冻结'>
				  				<td>
				  					${e?default("")}
								</td>
				  			<#elseif e_list[10] == '冻结'>
				  				<td>
								</td>
				  			</#if>
		  			  
				  			
				  	  <#elseif e_index = 5>
				  	  		<td>
				      			${e?default("")}&nbsp;&nbsp;万元
				      		</td>
				  	  <#else>
				      		<td>
				      			${e?default("")}
				      		</td>
				      </#if>
			      	</#if>
		  		</#list>
	  		</tr>
	 	</#list>
	</tbody>
</table>
</#macro>