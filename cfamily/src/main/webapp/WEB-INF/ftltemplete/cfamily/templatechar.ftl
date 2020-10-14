<@m_zapmacro_common_page_chart b_page />

<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

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
	      		<td style="max-width:200px;">
	      			<#if e_index == 6>
	      				<#if (e?length > 0 )>
	      					<img src="${e?default("")}" style="width:150px;max-height:200px;"/>
	      				<#else>
	      					${e?default("")}
	      				</#if>
	      			<#elseif e_index == 7>
	      				<#if (e?length > 0 )>
	      					<a href="${e?default("")}" target="_blank">${e?default("")}</a>
	      				<#else>
	      					${e?default("")}
	      				</#if>
	      			<#else>
		      			${e?default("")}
	      			</#if>
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
	</tbody>
</table>
</#macro>