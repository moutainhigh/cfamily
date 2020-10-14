<@m_zapmacro_common_page_chart b_page />

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<#assign e_pageField=e_pagedata.getPageField() />

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
	  		 	<#if e_pageField[e_index] == "处理">
	      		<td>
	      			<#if e_list[e_pageField?seq_index_of("reply_time")] == "">
	      				${e?default("")}
	      			</#if>
	      		</td>	  		 	
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