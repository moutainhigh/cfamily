<@m_zapmacro_propertylist_page_chart b_page />
<#macro m_zapmacro_cmanage_propertylist_table e_pagedata>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	        </#list>
	      		
	    </tr>
  	</thead>
   <tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
		      		<td>
		      			${e?default("")}
		      		</td>
	      	 </#list>
	      	</tr>
	 	</#list>
	</tbody>
	
</table>

</#macro>

<#-- 列表页 -->
<#macro m_zapmacro_propertylist_page_chart e_page>
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_cmanage_propertylist_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	</div>
</#macro>