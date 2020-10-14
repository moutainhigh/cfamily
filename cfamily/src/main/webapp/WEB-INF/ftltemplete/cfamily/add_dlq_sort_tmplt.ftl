 
<#assign pageNum = b_page.getReqMap()["zw_f_page_number"] >

<#assign tv_number = b_page.getReqMap()["zw_f_tv_number"] >
<#assign IdNumber = b_page.getReqMap()["zw_f_id_number"] >

<#if pageNum == "">

	<table  class="table  table-condensed table-striped table-bordered table-hover" style="margin-top:20px;">
		<thead>
		    <tr>
		      <th>商品编号</th>
		      <th>商品名称</th>
		      <th>商品状态</th>
		      <th>位置</th>
		    </tr>
	  	</thead>
		<tbody></tbody>
	</table>
	<center>
		<input type="button" class="btn" zapweb_attr_operate_id="2e488f4e188a45b9bec65b224da04987"  onclick="zapjs.zw.func_form_regex(this)" value="确定">
		&nbsp;&nbsp;
		<button type="button" id="clo" class="btn" onclick="zapadmin.window_close()">取消</button>
	</center>
	
<#else>
	<#assign pcDate = b_method.upControlPage("page_chart_v_fh_dlq_content","zw_p_sql_where=page_number='${pageNum}' and delete_state=1001 and tv_number='${tv_number}' and id_number='${IdNumber }'&zw_p_size=-1").upChartData()>
	<#assign e_pagedata=b_page.upChartData()>
	<#assign contentService=b_method.upClass("com.cmall.familyhas.service.DLQService")>
	<#assign pcInfo = contentService.getRelProductInfo("${pageNum}","${tv_number}") >
	
	<div class="zw_page_common_data">
		<form class="form-horizontal" method="POST" >
			<input type="hidden" name="zw_f_page_number" value="${pageNum}">
			<table  class="table  table-condensed table-striped table-bordered table-hover" style="margin-top:20px;">
				<thead>
				    <tr>
				      <th>商品编号</th>
				      <th>商品名称</th>
				      <th>商品状态</th>
				      <th>位置</th>
				    </tr>
			  	</thead>
			  
				<tbody>
					<#list pcDate.getPageData() as e_list>
						<tr>
				  		 <#list e_list as e>
				  		 	<#if e_index == 4 >
					      		<#list pcInfo?keys as key>
						      		<#if pcInfo[key]?? && pcInfo[key]["id_number"] == IdNumber && key == IdNumber+"-"+e>
								      		<td>
								      			${e?default("")}
								      		</td>
							                <td>
							               		${pcInfo[key]["product_name"]}
							                </td>
							                <td>
								                <#if '4497153900060002' == pcInfo[key]["product_status"]>
								                	已上架
								                <#else>
								                	已下架
								                </#if>
							                </td>
							                <td>
							                	<input type="text" value="${pcInfo[key]["location"]}" attr_web_uid="${pcInfo[key]["uid"]}" style="width:50px;"/>
							                </td> 
							        </#if>
						        </#list>
					      		
				      		</#if>
				      	</#list>
				      	</tr>
				 	</#list>
				</tbody>
			</table>
				
			<center>
				<input type="button" class="btn"  onclick="add_dlq_info.resetSort(this)" value="确定">
				&nbsp;&nbsp;
				<button type="button" class="btn" onclick="zapadmin.window_close()">取消</button>
			</center>
	
		</form>	
	</div>

</#if>