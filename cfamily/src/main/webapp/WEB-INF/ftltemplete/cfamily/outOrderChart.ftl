<script>
function importDialog(){
		zapjs.f.window_box({
			id : 'bdwmOrder',
			content : '<iframe src="../show/page_import_v_oc_orderinfo_import' 
			+'" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});
}
function editDialog(uuid){
		zapjs.f.window_box({
			id : 'editbdwmOrder',
			content : '<iframe src="page_edit_v_oc_orderinfo_import?zw_f_uid='+uuid
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
		  		 		<#if e_index = 12>
		  		 			<#if e_list[12] == "1">
		  		 				已同步
		  		 			<#else>
		  		 				未同步
		  		 			</#if>
			  		 	<#elseif e_index = 21>
			  		 		<#if e_list[12] != "">
			  		 			<#if e_list[12] == "1">
				  		 			<#if e_list[14] == "否">
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
<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field e_field e_page>
		<#if e_field.getPageFieldName() == "zw_f_out_number">
			<@m_zapmacro_common_field_start text='外部订单号' for=e_field.getPageFieldName() />
				<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			<@m_zapmacro_common_field_end />
		<#elseif e_field.getFieldTypeAid()=="104005008">
	  		<@m_zapmacro_common_field_hidden e_field/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005001">
	  		  <#-- 内部处理  不输出 -->
	  	<#elseif  e_field.getFieldTypeAid()=="104005003">
	  		<@m_zapmacro_common_field_component  e_field  e_page/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005004">
	  		<@m_zapmacro_common_field_date  e_field />
		<#elseif  e_field.getFieldTypeAid()=="104005022">
  			<@m_zapmacro_common_field_datesfm  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005019">
			<@m_zapmacro_common_field_select  e_field  e_page ""/>
	  	<#elseif  e_field.getFieldTypeAid()=="104005103">
	  		<@m_zapmacro_common_field_checkbox  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005020">
	  		<@m_zapmacro_common_field_textarea  e_field />
	  	<#elseif  e_field.getFieldTypeAid()=="104005005">
	  		<@m_zapmacro_common_field_editor  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005021">
	  		<@m_zapmacro_common_field_upload  e_field  e_page />
	  	<#elseif  e_field.getFieldTypeAid()=="104005009">
	  		<@m_zapmacro_common_field_text  e_field />
	  	<#else>
	  		<@m_zapmacro_common_field_span e_field/>
	  	</#if>
</#macro>
<@m_zapmacro_common_page_chart b_page />