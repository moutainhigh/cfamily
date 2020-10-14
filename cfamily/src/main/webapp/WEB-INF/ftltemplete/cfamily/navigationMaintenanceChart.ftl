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
</#macro>	

</div>
<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
        	<#list e_pagedata.getPageHead() as e_list>
		    	<#if (e_list_index>0)>
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
	  		 	<#if (e_index>0)>
		      		<td>
		      			<#if e_index=2 || e_index=5>
		      					<div class="w_left w_w_100">
		  							<a target="_blank" href="${e?default("")}">
		      							<img src="${e?default("")}"> 
		      						</a>
		      					</div>
		      			<#elseif e_index=13>
			      			<#if e_list[8] = "是">
			      				<input class="btn btn-small" type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="c4271fc4ff11448c9a34b657556ed117">
			      			<#else>
			      				<input class="btn btn-small" type="button" value="发布" onclick="zapjs.zw.func_do(this, null, { zw_f_uid : '${e_list[0]}'});" zapweb_attr_operate_id="c4271fc4ff11448c9a34b657556ed117">
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
</#macro>