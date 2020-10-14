
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
			      			<#if e_index=11>
			      				<#if e_list[10] = "已发布">
					      			<!--已发布状态时不能再修改-->
				      			<#else>
				      				${e?default("")}
				      			</#if>
			  			    <#elseif e_index=12>
			  			    	<#if e_list[10] = "已发布">
			  			    		<input class="btn btn-small" type="button" value="取消发布" onclick="zapjs.zw.func_do(this, null, { zw_f_coupon_type_code : '${e_list[0]}'});" zapweb_attr_operate_id="9ba67baadd9811e48c25005056925439">
				      			<#else>
				      				<input class="btn btn-small" type="button" value="发布" onclick="zapjs.zw.func_do(this, null, { zw_f_coupon_type_code : '${e_list[0]}'});" zapweb_attr_operate_id="9ba67baadd9811e48c25005056925439">
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
</#macro>
	 