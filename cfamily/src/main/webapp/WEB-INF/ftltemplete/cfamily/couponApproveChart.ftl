<@m_zapmacro_common_page_chart b_page />
<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page>

	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>


<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<@m_common_html_js ["cfamily/js/couponExamination.js"]/>
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
			  	 <#if e_index = 10>
			  	 	<td>
	  					<input type="button" value="审批" onclick="couponExamination.et.couponapprove('${e_list[0]}')" class="btn btn-small" zapweb_attr_operate_id="00ed4cb117dc4b54aa452ab72f81a35a">
	      				<a class="btn btn-link" target="_blank" href="../export/page_chart_v_oc_coupon_check?zw_p_size=-1&zw_f_task_code=${e_list[0]}">导出</a>
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
