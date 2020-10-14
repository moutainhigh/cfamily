
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
	        	<#if (e_list_index <=10)&&(e_list_index > 0)>
		      	 <th>
		      	 	${e_list}
		      	 </th>
		      	</#if>
	      </#list>
	      <th colspan="4" width="20%">	操作</th>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
	  		 <#list e_list as e>
	  		 	<#if (e_index <=10)&&(e_index > 0)>
		      		<td>
		      			${e?default("")}
		      		</td>
	      		</#if>
	      	</#list>
	      	<th colspan="4">
				<a href="page_edit_v_fh_program?zw_f_uid=${e_list[0]}" class="btn btn-small">修改</a> 
				<#if (e_list[4] == "未发布")>
					<input zapweb_attr_operate_id="239c12ecf81a4b129e74844eaf69d5c0" class="btn btn-small" onclick="zapjs.zw.func_tip(this,'${e_list[0]}','发布')" type="button" value="发布">
				</#if>
				 <#if (e_list[4] == "已发布")>
					<input zapweb_attr_operate_id="239c12ecf81a4b129e74844eaf69d5c0" class="btn btn-small" onclick="zapjs.zw.func_tip(this,'${e_list[0]}','取消发布') " type="button" value="取消">
				</#if>
				<a href="page_book_v_addsku_fh_program?zw_f_uid=${e_list[0]}" class="btn btn-small">维护商品</a>
				<a href="page_book_v_fh_program?zw_f_uid=${e_list[0]} " class="btn btn-small" target="_blank">查看</a>
			</th>
	      	</tr>
	 	</#list>
		</tbody>
</table>
</#macro>
