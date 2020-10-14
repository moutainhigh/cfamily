
<@m_zapmacro_flowchangestatuslist_page_chart b_page />



<#macro m_zapmacro_cmanage_flowchangestatus_table e_pagedata>

<#assign sc_defineService=b_method.upClass("com.cmall.systemcenter.service.ScFlowBase")>
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
	  		 	<#if (e_index = 0)>
		  		 	<td>
		  		 		<#if sc_defineService??>
	  		 				${sc_defineService.getDefineNameByTypeCode(e)}
	  		 			<#else>
	  		 				 未知状态
	  		 			</#if>
		      		</td>
		      	<#elseif (e_index = 1 || e_index = 2)>
		  		 	<td>
		  		 		<#if sc_defineService??>
	  		 				${sc_defineService.getDefineNameByCode(e)}
	  		 			<#else>
	  		 				 未知状态
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



<#-- 列表页 -->
<#macro m_zapmacro_flowchangestatuslist_page_chart e_page>

	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_cmanage_flowchangestatus_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>