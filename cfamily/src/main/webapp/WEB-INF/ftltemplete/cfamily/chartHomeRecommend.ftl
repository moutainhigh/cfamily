<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
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
	      		<td>
	      			<#if e_index==0 >
						<#assign categoryMap=sellercategoryService.getCateGoryShow(e,"SI2003")>
						<#assign keys=categoryMap?keys>
						<#list keys as key>
							${categoryMap[key]?trim}<br>
						</#list>
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