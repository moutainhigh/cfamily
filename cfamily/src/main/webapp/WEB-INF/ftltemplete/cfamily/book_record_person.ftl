<#assign record_code = b_page.getReqMap()["zw_f_record_code"] >
<#assign account_code = b_page.getReqMap()["zw_f_account_code"] >
<#assign tel = b_page.getReqMap()["zw_f_tel"] >
<#assign seller_code = b_page.getReqMap()["zw_f_seller_code"] >


<div class="zab_info_page">
	<div class="zab_info_page">
	  <@m_zapmacro_common_page_book b_page />
    </div>
</div>


<#--加载商品信息数据-->
<#assign logData=b_method.upControlPage("page_chart_v_mc_record_person_new","zw_f_record_code=${record_code}&zw_p_size=-1").upChartData()>
<#assign logDataOne=b_method.upControlPage("page_chart_v_mc_record_person_new","zw_f_father_code=${record_code}&zw_p_size=-1").upChartData()>
<#assign  e_pagedata=logData />
<#assign  e_pagedataOne=logDataOne />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list> 
	       	  <#if (e_list_index <= 15) && (e_list_index >=0)>
		      	 <th>
		      	 	${e_list}
		      	 </th>
		      </#if>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedataOne.getPageData() as e_list>
			<tr>
		  		 <#list e_list as e>
		  		 	 <#if (e_index <=15) && (e_index >=0)>
			      		<#if e_index=15>
			      			<td>
								<div class="w_left w_w_100">
		  						<#if e?default("") != "">
			  						<a target="_blank" href="${e?default("")}">
			      						<img src="${e?default("")}"> 
			      					</a>
			      				</#if>
		      				</div>
				      		</td>
			      		<#else>
			  				<td>
								${e?default("")}
				      		</td>
			      		</#if>
			      		
		      		 </#if>
		      	</#list>
		      	
	      	</tr>
	 	</#list>
		</tbody>
		
		
		<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
		  		 <#list e_list as e>
		  		 	 <#if (e_index <=15) && (e_index >=0)>
			      		<#if e_index=15>
			      			<td>
								<div class="w_left w_w_100">
		  						<#if e?default("") != "">
			  						<a target="_blank" href="${e?default("")}">
			      						<img src="${e?default("")}"> 
			      					</a>
			      				</#if>
		      				</div>
				      		</td>
			      		<#else>
			  				<td>
								${e?default("")}
				      		</td>
			      		</#if>
			      		
		      		 </#if>
		      	</#list>
		      	
	      	</tr>
	 	</#list>
		</tbody>
</table>