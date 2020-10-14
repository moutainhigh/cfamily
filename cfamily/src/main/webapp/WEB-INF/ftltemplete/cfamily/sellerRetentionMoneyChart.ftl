<#macro m_zapmacro_common_page_chart e_page>
	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
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
		  		 		<#if e_index = 4>
		  		 			<a target="_self" href="../page/page_chart_v_oc_retention_money_merchant?zw_p_sql_where=small_seller_code='${e_list[0]}'">${e?default("")}</a>
		  		 		<#elseif e_index = 5>
		  		 			<a target="_self" href="../page/page_edit_retention_money_receive?small_seller_code=${e_list[0]}" >${e?default("")}</a>
		  		 		<#elseif e_index = 7>
		  		 			<a target="_self" href="../page/page_edit_retention_money_adjust?small_seller_code=${e_list[0]}" >${e?default("")}</a>
			  		 	<#else>
			  		 		${e?default("")}
			  		 	</#if>
		      		</td>
		      	</#list>
		      	</tr>
		 	</#list>
			</tbody>
	</table>
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	</div>
</#macro>
<@m_zapmacro_common_page_chart b_page />