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
<@m_common_html_js ["cfamily/js/cdkeyExamination.js"]/>
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
 				<#if e_index = 8>
 					<#if e_list[5] == '否'>
 						<td>
		      			</td>
 					<#else>
 						<td>
			      			${e?default("")}
			      		</td>
 					</#if>
		  	 <#elseif e_index = 10>
		  	 	<td>
  					<input type="button" value="审批" onclick="cdkeyExamination.et.cdkeyApproval('${e_list[0]}','${e_list[3]}')" class="btn btn-small" zapweb_attr_operate_id="51f911336c2940f58bcb9d9f15a33525">
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

<script>
	$(document).ready(function(){
		$("#zw_f_task_status").parent().parent().hide();
	});
</script>