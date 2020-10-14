<@m_zapmacro_common_page_chart b_page />
<#-- 列表页 -->
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


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field   e_page>
	
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
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>


<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	   <tr>
	   
	      <#list e_pagedata.getPageHead() as e_list>
	             <#if (e_list_index >=1&& e_list_index<=12)>
		             <#if e_list_index=11>
		                  <th>
			      	 	${e_list}
			      	     </th>
			      	      <th>
			      	 	     发布
			      	     </th>
		             <#else>
		             	 <th>
			      	 	${e_list}
			      	     </th>
		             </#if>
			      </#if>
	      </#list>
	       <#-- <th colspan="4" width="20%">	操作</th>-->
	       
	    </tr>
  	</thead>
  
  	<#assign statusIndex = 7>

  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  		    <#if (e_index >= 1&& e_index<=12)>
	  		    <#if e_index=6>
	  		    <td>
	  		    <img src="${e?default("")}" style="width:150px;" />
	  		    </td>
	  		    <#else>
		      	    <#if e_index=11>
		      	         <#if (e_list[10] == '未发布')>
				             <td>
			      	          ${e?default("")}
		      	             </td>
	                     <#elseif (e_list[10] == '已发布')>
	                          <td>
		      	             </td>
				         </#if>

		      	     <td>
			      	     <#if (e_list[10] == '未发布')>
				            <input type="button" value="发布" onclick="{zapjs.zw.func_tip(this,'${e_list[0]}','确认发布')}" class="btn btn-small" zapweb_attr_operate_id="053e154f9604491a97ba9527eb487c88">
	                     <#elseif (e_list[10] == '已发布')>
	                         <input type="button" value="取消发布" onclick="{zapjs.zw.func_tip(this,'${e_list[0]}','确认取消发布')}" class="btn btn-small" zapweb_attr_operate_id="053e154f9604491a97ba9527eb487c88">
				         </#if>
		      	    </td>
		             <#elseif e_index=12>
		                 <#if (e_list[10] == '未发布')>
				             <td>
			      	          ${e?default("")}
		      	             </td>
	                     <#elseif (e_list[10] == '已发布')>
	                          <td>
		      	             </td>
				         </#if>
		               <#else>
		             <td>
			      	  ${e?default("")}
		      	     </td>
		             </#if>
	  		    </#if>
	  		       
		        </#if>
	      	</#list>
	      	
	      	</tr>
	 	</#list>
	</tbody>
</table>
</#macro>
	 
