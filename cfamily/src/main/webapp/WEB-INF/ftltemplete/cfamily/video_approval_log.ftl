<#assign service=b_method.upClass("com.cmall.familyhas.service.LiveVideoService")/>
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
	  		 <#if e_index==1>
	  		    <#assign relName = service.getUserName('${e?default("")}') >
	  		    <td>
	      			${relName}
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