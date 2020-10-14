<@m_zapmacro_common_page_chart b_page />


<form class="form-horizontal" method="POST" >
	
	<input type="hidden" id="zw_f_state" name="zw_f_state" value="1" />	
	
</form>




<#-- 列表的自动输出 -->
<#macro m_zapmacro_common_table e_pagedata>

<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	   <tr>
	      	<th>编号</th>
	      	<th>结算日</th>
	      	<th>销售统计周期</th>
	      	<th>退货统计周期</th>
	        <th>操作</th>
	    </tr>
  	</thead>
  
	<tbody>
		
			<#list e_pagedata.getPageData() as e_list>
			<tr>
				<td>${e_list[1]}</td>
				<td>${e_list[2]}</td>
				<td>${e_list[3]}${e_list[4]}<#if e_list[4] != '月末'>日</#if> - ${e_list[6]}${e_list[7]}<#if e_list[7] != '月末'>日</#if></td>
				<td>${e_list[9]}${e_list[10]}<#if e_list[10] != '月末'>日</#if> - ${e_list[12]}${e_list[13]}<#if e_list[13] != '月末'>日</#if></td>
			
				<td>
				
					<#if e_list[15] == '1'>
					<a class="btn btn-link" zapweb_attr_operate_id="b1b565f26f1a11e5aad9005056925439"   href="javascript:void(0)" onclick="zapjs.zw.func_delete(this,'${e_list[0]}')">删除</a></td>
					</#if>
					
			</tr>
	 		</#list>
	 		
	 			 		
		
	</tbody>
</table>
</#macro>
	 