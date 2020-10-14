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

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <#if (e_list_index == 0)>
			     <#else>
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
	      		<#if (e_index == 0)>
		      	<#else>
		      		<td>
		      			${e?default("")}
		      		</td>
	      		</#if>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>