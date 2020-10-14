<div class="zab_info_page"> 
	<div class="zab_info_page_title  w_clear">
		<span>栏目信息</span>
	</div>
	<@m_zapmacro_common_page_book b_page />
</div>

<#assign page_code = b_page.getReqMap()["zw_f_page_number"] >

<div class="form-horizontal control-group">
	<a href="../page/page_add_v_fh_page_template?zw_f_page_number=${page_code}" class="btn btn-link"  target="_blank">
		<input class="btn btn-success" type="button" value="新增模版"/>
	</a>
</div>

<div class="zab_info_page_title  w_clear">
<span>关联模版列表</span>&nbsp;&nbsp;&nbsp;
</div>

<#assign logData=b_method.upControlPage("page_chart_v_fh_page_template","zw_p_sql_where=page_number='${page_code}' and dal_status = '1001'&zw_p_size=-1").upChartData()>
<#assign  e_pagedata=logData />


<@m_zapmacro_common_table e_pagedata/>


<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
		      	 <#if e_list_index == 1>
		      	 	<th>
			      	 	模板类型
			      	</th>
		      	 </#if>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	      		<td>
	      			${e?default("")}
	      		</td>
	      		<#if e_index == 1>
	      			<#assign template_type_code = b_method.upDataQuery('fh_data_template','','','template_number','${e}') />
	      			<#list template_type_code as e>
	      				<#assign template_type = b_method.upDataQuery('sc_define','','','define_code',e["template_type"]) />
					</#list>
		      	 	<td>
		      	 		<#list template_type as e>
							${e["define_name"]}
						</#list>
			      	</td>
		      	</#if>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>


