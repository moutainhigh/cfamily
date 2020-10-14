	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire b_page />
	</div>
	<hr/>
	
	<#assign e_pagedata=b_page.upChartData()>
	<div class="zw_page_common_data">
	



	<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	    	<th>
	    		<input id="chb-item-all" type="checkbox" onclick=$('input[id^="chb-item-"]').not("[disabled='disabled']").prop('checked',$(this).prop('checked'))>
	    	</th>
	        <#list e_pagedata.getPageHead() as e_list>
		      	 <th>
		      	 	${e_list}
		      	 </th>
	      </#list>
	    </tr>
  	</thead>
  
	<tbody>
		<#list e_pagedata.getPageData() as e_list>
			<td><input name="page_codes" id="chb-item-${e_list[0]}" type="checkbox" value="${e_list[0]}" onclick="$('#chb-item-all').prop('checked',false)" data-productcode="${e_list[0]}"></td>
	  		 <#list e_list as e>
	      		<td>
	      		${e?default("")}
	      		</td>
	      	</#list>
	      	</tr>
	 	</#list>
		</tbody>
</table>





	<@m_zapmacro_common_page_pagination b_page  e_pagedata />
	
	</div>
	
	
<Script>

function bathRefresh(oTag){
	var id = document.getElementsByName('page_codes');
    var value = new Array();
    for(var i = 0; i < id.length; i++){
    if(id[i].checked)
    	value.push(id[i].value);
    }
	zapjs.zw.func_do(oTag, null, {zw_f_page_code:value.join(',')})
}
</Script>