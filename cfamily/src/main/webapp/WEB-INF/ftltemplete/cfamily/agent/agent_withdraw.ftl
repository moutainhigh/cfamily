<@m_common_html_script "require(['cfamily/js/agent/agent_withdraw'],
	function(a){
		
	
	});" />
<@m_zapmacro_common_page_chart b_page />
<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<#assign e_pageField=e_pagedata.getPageField() />
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	   <tr>
	      <#list e_pagedata.getPageHead() as e_list>
	      		<td>
	      			${e_list}
	      		</td>
	      </#list>
	    </tr>
  	</thead>
  	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  		 	 <#if (e_index ==12||e_index ==13)>
	  		 	 	<#if e_list[11] == '待运营审核'>
	  		 			<td>
	  		 				${e?default("")}
	  		 			</td>
	  		 		<#else>
		  			 	<td>
			      			
		      			</td>
	      			</#if>
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