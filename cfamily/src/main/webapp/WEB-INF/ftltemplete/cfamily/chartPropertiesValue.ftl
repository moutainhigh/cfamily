
<#assign properties_code = b_page.getReqMap()["zw_f_properties_code"]?default("") >

<@m_zapmacro_common_page_chart b_page />


<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page> 
	
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
	      			<#if e_index=2>
	      				<#if (e_pagedata.getPageData()?size  > 1)>
	      					<#if (e_list_index = 0)>
								<td>
									<input class="btn btn-small" type="button" value="下移" onclick="tree_propertiesDown('${e_list[0]}')" > 
								</td>
							<#elseif (e_list_index = e_pagedata.getPageData()?size-1)>
								<td>
									<input class="btn btn-small" type="button" value="上移" onclick="tree_propertiesUp('${e_list[0]}')" > 
								</td>
							<#else>
								<td>
									<input class="btn btn-small" type="button" value="上移" onclick="tree_propertiesUp('${e_list[0]}')" > 
									<input class="btn btn-small" type="button" value="下移" onclick="tree_propertiesDown('${e_list[0]}')" > 
								</td>
							</#if>
	      				<#else>
			      			<td> </td>
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



<script>

$(function(){
	$(".pull-right").html('<a class="btn btn-small" zapweb_attr_operate_id="bb10063e64f911eaabac005056165069" href="page_add_v_uc_properties_value?properties_code=${properties_code}">添加属性值</a>');
});

// 属性上移
function tree_propertiesUp(PVCode) {
	var obj = {};
	zapjs.zw.func_do(obj, 'f0d8b328a3f547eb94ebfd6dc90d1e15', {zw_f_properties_value_code: PVCode, zw_f_type: 'UP'});
}

// 属性下移
function tree_propertiesDown(PVCode) {
	var obj = {};
	zapjs.zw.func_do(obj, 'f0d8b328a3f547eb94ebfd6dc90d1e15', {zw_f_properties_value_code: PVCode, zw_f_type: 'DOWN'});
}

</script>