


<div class="zab_info_page">
<div class="zab_info_page_title  w_clear">
<span>上传信息</span>
</div>
<@m_zapmacro_common_page_book b_page />

</div>

<#assign relevance_uid = b_page.getReqMap()["zw_f_uid"] >
<div class="zab_info_page_title  w_clear">
<span>上传明细</span>
</div>
<#assign logData=b_method.upControlPage("page_chart_v_lc_productinfo_batch","zw_f_relevance_uid=${relevance_uid}").upChartData()>

<#assign  e_pagedata=logData />
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
		      			<#if e_index=0>
							<#if  e_list[0]?default("") = "失败">
								<span style="color:red">失败</span>
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

