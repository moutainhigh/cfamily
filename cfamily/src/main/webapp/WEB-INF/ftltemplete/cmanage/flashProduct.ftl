<#assign activity_code = b_page.getReqMap()["zw_f_activity_code"] >
<#assign e_pagedata=b_page.upChartData()>
	<div class="zw_page_common_data">
	
<form class="form-horizontal" method="POST" >
<input type="hidden" name="zw_f_activity_code" value="${activity_code}">
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	        <#list e_pagedata.getPageHead() as e_list>
		      	<#if (e_list_index >0)>
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
	      		<#if (e_index >0&& e_index <4)>
	      		<td>
	      			${e?default("")}
	      		</td>
	      		
	      		<#elseif (e_index=4)>
	      		
	      		<td>
	      		 <input type="text" class="input-small" maxlength="4" name="zw_f_pro${e_list[1]}" zapweb_attr_regex_id="469923180003" onkeyup="this.value=this.value.replace(/[^\d]/g,'') " onafterpaste="this.value=this.value.replace(/[^\d]/g,'') " value="${e_list[4]}">
	      		</td>
	      		
	      		</#if>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>
	
		
	<center>
	
		<input type="button" class="btn" zapweb_attr_operate_id="2e488f4e188a45b9bec65b224da04987"  onclick="zapjs.zw.func_form_regex(this)" value="确定">
		&nbsp;&nbsp;
		<button type="button" id="clo" class="btn" onclick="zapadmin.window_close()">取消</button>
	</center>

	</form>	
</div>

