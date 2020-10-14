
<@m_zapmacro_common_page_chart b_page />

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
		
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        <#if e_list_index=6>
	             <th>
		      	 	操作
		      	 </th>
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
	      		<td>
	      			${e?default("")}
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>