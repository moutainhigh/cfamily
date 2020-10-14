
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
	      			<#if e_index=0>
	      				<div class="w_left w_w_100">
								<a href="${e?default("")}" target="_blank">
									<img src="${e?default("")}">
								</a>
						</div> 
					<#elseif e_index=1>
	      				<div class="w_left w_w_100">
								<a href="${e?default("")}" target="_blank">
									<img src="${e?default("")}">
								</a>
						</div>
					<#elseif e_index=4>
                        <div class="w_left w_w_100">
                            <a href="${e?default("")}" target="_blank">
                                <img src="${e?default("")}">
                            </a>
                        </div>
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
