<@m_zapmacro_cmanage_flow_page_chart b_page,b_method  />
<@m_common_html_css ["cfamily/js/select2/select2.css"] />

<script>
require(['cmanage/js/flowChart','cfamily/js/select2/select2'],
function(p)
{
	zapjs.f.ready(function()
	{
		p.init_flowChart();
	});
});

var clickedIds = [];
var checkedVales = [];

function previewClick(code){
    if(clickedIds.indexOf(code) < 0){
    	clickedIds.push(code)
    }
}

function batchApprove(){
	checkedVales = [];
	var unPreviewClickValues = [];
	var unPreviewProductCodeValues = [];
	$('input[id^="chb-item-"]').each(function(){
		if($(this).prop('checked') && $(this).attr('id') != 'chb-item-all'){
			checkedVales.push($(this).val());
			if(clickedIds.indexOf($(this).val()) < 0){
				unPreviewClickValues.push($(this).val());
				unPreviewProductCodeValues.push($(this).data('productcode'));
			}		
		}
	});
	
	if(checkedVales.length == 0){
		alert("请勾选待审批的项")
		return;
	}
	
	// 批量审批不能审批未点过预览的
	if(unPreviewClickValues.length > 0){
		var modalOption = {content:"<h4>未预览的项不能批量审批!</h4><p>未预览商品编号："+unPreviewProductCodeValues.join(',')+"</p>",title:"提示",oktext:"关闭",height:200};
		zapjs.f.window_box(modalOption);
		return;
	}
	
	var showHtml = $('#flow_form_forsubmit_temp').html();
	showHtml = showHtml.replace('#zw_f_flow_code#',checkedVales.join(','));
	var modalOption = {content:showHtml,title:"批量审批",oktext:"关闭",height:200};
	zapjs.f.window_box(modalOption);
	
	//$("#flow_form_forsubmit").remove();
	//$(document.body).append($('#flow_form_forsubmit_temp').html());
	
	//$('#zw_f_flow_code').val(checkedVales.join(','));
	//zapjs.zw.func_call($('#flow_form_btn_submit'));
}
</script>

<#macro m_zapmacro_cmanage_flow_table e_pagedata,b_method>

<@m_common_html_js ["cmanage/js/flow.js"]/>
<#assign sc_defineService=b_method.upClass("com.cmall.systemcenter.service.ScFlowBase")>
<#assign productCodeService=b_method.upClass("com.cmall.productcenter.service.ProductCheck")>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>
	    	<th><input id="chb-item-all" type="checkbox" onclick=$('input[id^="chb-item-"]').prop('checked',$(this).prop('checked'))></th>
	    	
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
			<td><input id="chb-item-${e_list[0]}" type="checkbox" value="${e_list[0]}" onclick="$('#chb-item-all').prop('checked',false)" data-productcode="${e_list[3]}"></td>
	  		 <#list e_list as e>
	 
	  		 	<#if e_index = 7	>
	  		 		
	  		 		<td>
	  		 			<#if e?default("") = "">
	  		 				无内容
	  		 			<#else>
	  		 				<a target="_blank" href="${e?default("")}" onclick="previewClick('${e_list[0]}')">预览</a>
	  		 			</#if>
		      			
		      		</td>
		      	<#elseif e_index = 4>
		      	
		      		<td>  				
		      			
		      			<a target="_blank" href="page_book_v_v_product_base_info_flow?zw_f_product_code=${e_list[3]}">${e?default("")}</a>	   
		      			   		
		      		</td>
		      	
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


<#-- 列表页 -->
<#macro m_zapmacro_cmanage_flow_page_chart e_page,b_method >

	<div class="zw_page_common_inquire">
		<@m_flow_common_page_inquire e_page />
	</div>
	
	<hr/>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_cmanage_flow_table e_pagedata,b_method />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>


<#-- 查询区域 -->
<#macro m_flow_common_page_inquire e_page>
	<form class="form-horizontal" method="POST" >
		<@m_flow_common_auto_inquire e_page />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate() "116001009" />
</#macro>



<#--查询的自动输出判断 -->
<#macro m_flow_common_auto_inquire e_page>
	<#list e_page.upInquireData() as e>
	
		<#if e.getQueryTypeAid()=="104009002">
			<@m_zapmacro_common_field_between e  e_page/>
		<#elseif e.getQueryTypeAid()=="104009001">
			<#-- url专用  不显示 -->
		<#elseif e.getQueryTypeAid()=="104009020">
			<@m_zapmacro_common_field_betweensfm e  e_page/>
	  	<#elseif  (e.getFieldTypeAid()=="104005019" && e.getPageFieldName() = "zw_f_current_status")>
	  		<@m_flow_common_field_select  e  e_page "请选择"/>
	  	<#elseif  e.getFieldTypeAid()=="104005019">
	  		<@m_zapmacro_common_field_select  e  e_page "请选择"/>
	  	<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  		
	  	</#if>
	  	
	</#list>

</#macro>

<#-- 字段：下拉框            e_text_select:是否显示请选择       -->
<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSourceByParam(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
	<@m_zapmacro_common_field_end />
</#macro>

<script type="text/template" id="flow_form_forsubmit_temp">
	<form class="form-horizontal" id="flow_form_forsubmit" method="POST" style='margin-top:20px;'>
	<input type="hidden" id="zw_f_to_status" name="zw_f_to_status" value="4497172300160005">
	<input type="hidden" id="zw_f_flow_code" name="zw_f_flow_code" value="#zw_f_flow_code#">
	<input type="hidden" id="zw_f_current_status" name="zw_f_current_status" value="4497172300160011">
	<div class="control-group">
		<label class="control-label" for="zw_f_source_code">审批意见:</label>
		<div class="controls">
			<textarea id="zw_f_remark" name="zw_f_remark"></textarea>
		</div>
	</div>
	
	<div class="control-group">
		<div class="controls">
			<div class="btn-toolbar">
				<input id="flow_form_btn_submit" type="button" style="margin-right:5px;" class="btn  btn-primary" zapweb_attr_operate_id="115793e80b38485aaba8223e0ea101b7" zap_tostatus_attr="4497172300160005" value="同意" onclick="zapjs.zw.func_call(this)">
			</div>
		</div>
	</div>
	</form>	
</script>