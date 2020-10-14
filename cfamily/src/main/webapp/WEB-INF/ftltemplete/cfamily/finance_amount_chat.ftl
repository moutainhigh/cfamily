	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire b_page />
	</div>
	<hr/>
	
	<#assign e_pagedata=b_page.upChartData()>
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
	      		<#if e_index==10&&(e_list[6]=="待财务确认")>
	      		&nbsp;
	      		
	      		
	      		<#elseif e_index==8&&(e_list[6]=="已确认"||e_list[6]=="已发布")>
	      		
	      		
	      		&nbsp;
	      		
	      		<#elseif e_index==10&&e_list[6]=="已发布">
	      		&nbsp;
	      		
	      		<#else>
	      		${e?default("")}
	      		</#if>
	      			
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>





	<@m_zapmacro_common_page_pagination b_page  e_pagedata />
	
	</div>
	
	
	
	