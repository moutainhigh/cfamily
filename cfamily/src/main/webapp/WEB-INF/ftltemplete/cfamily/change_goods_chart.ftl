

<div class="zw_page_common_inquire">
	<@m_zapmacro_common_page_inquire b_page />
</div>
<hr/>

<#assign e_pagedata=b_page.upChartData()>

<div class="zw_page_common_data">


<#assign memberLoginSupport=b_method.upClass("com.cmall.membercenter.support.MemberLoginSupport")>

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
	      		<#if e_index=2>
	      			<#if e_pagedata.getPageData()?size lt 2>
	  		 			${memberLoginSupport.getMemberLoginName(e)}
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







<@m_zapmacro_common_page_pagination b_page  e_pagedata />

</div>

