
<@m_zapmacro_common_page_chart b_page />
<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>
<#assign getCategoryIgnoreService=b_method.upClass("com.cmall.familyhas.service.GetCategoryIgnoreService")>
<#assign str = getCategoryIgnoreService.getCategory()>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
	        	<#if (e_list_index > 0)>
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
	      			<#if e_index==3>
	      				<#if e_list[3]=="N">
	      					关闭
	      				<#else>
	      					开启
	      				</#if>
	      			<#elseif e_index==2>
	      				${str}
	      			<#elseif e_index==4>
	      				<#if e_list[3]=="N">
	      					<input type="button" value="开启过滤" onclick="if(confirm('确认开启过滤类别？')){zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});}" class="btn btn-small" zapweb_attr_operate_id="151beb1dbc3a11eaabac005056165069">
	      				<#else>
	      					<input type="button" value="关闭过滤" onclick="if(confirm('确认关闭过滤类别？')){zapjs.zw.func_do(this, null, {zw_f_uid:'${e_list[0]}'});}" class="btn btn-small" zapweb_attr_operate_id="151beb1dbc3a11eaabac005056165069">
	      				</#if>
	      			<#else>
	      				${e?default("")}
	      			</#if >
	      		</td>
	      	</#if >
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>