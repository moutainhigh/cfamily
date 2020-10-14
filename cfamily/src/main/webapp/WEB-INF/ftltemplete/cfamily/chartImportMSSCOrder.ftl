<script>
function exportDialog(){
		zapjs.f.window_box({
			id : 'importDSBSCOrder',
			content : '<iframe src="../show/page_import_v_oc_orderinfo_import_mssc'
			+'" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});
}
function editDialog(uuid){
		zapjs.f.window_box({
			id : 'editImportDSBSCOrder',
			content : '<iframe src="page_edit_v_oc_orderinfo_import_mssc?zw_f_uid='+uuid
			+'" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});
}
</script>
<#macro m_zapmacro_common_page_chart e_page>
	<div class="zw_page_common_inquire">
		<@m_zapmacro_common_page_inquire e_page />
	</div>
	<hr/>
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
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
		  		 		<#if e_index = 11>
		  		 			<#if e_list[11] == "1">
		  		 				已同步
		  		 			<#else>
		  		 				未同步
		  		 			</#if>
			  		 	<#elseif e_index = 20>
			  		 		<#if e_list[11] != "">
			  		 			<#if e_list[11] == "1">
				  		 			<#if e_list[13] == "否">
				  		 				${e?default("")}
				  		 			</#if>
				  		 		</#if>
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
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	</div>
</#macro>
<@m_zapmacro_common_page_chart b_page />