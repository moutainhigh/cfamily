	
<#assign e_pagedata=b_page.upChartData()>
<div class="zw_page_common_data">
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	          <#if (e_list_index > 1)>
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
	  		 	<#if (e_index > 1)>
		      		<td>
		      			<#if e_index=4>
		      				<div class="w_left w_w_100">
								<a href="${e?default("")}" target="_blank">
									<img src="${e?default("")}">
								</a>
							</div> 
		  			    <#elseif e_index=9>
		  			    	<#if e_list[1] = "4497469400030001">
			      				<input class="btn btn-small" type="button" value="发布" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="beb535385fa94c53b7c481771dba1af3">
			      			<#else>
		  			    		<input class="btn btn-small" type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="beb535385fa94c53b7c481771dba1af3">
			      			</#if>
			      		<#elseif e_index=6>
			      		 	<#if e_list[1] = "4497469400030001">
			      		 	 	${e?default("")}
			      		 	</#if>
			      		 <#elseif e_index=7>
			      		 	<#if e_list[1] = "4497469400030001">
			      		 		 ${e?default("")}
			      		 	</#if>
		  			   <#else>
		  				    ${e?default("")}
		  			   </#if>
	      		   </td>
	      		 </#if>
	      	</#list>
	      	</tr>
	 	</#list>
</tbody>
</table>
<@m_zapmacro_common_page_pagination b_page  e_pagedata />
</div>