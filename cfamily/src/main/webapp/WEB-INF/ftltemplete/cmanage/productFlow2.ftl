<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
<@m_zapmacro_cmanage_flow_page_chart b_page,b_method  />
<#assign last_status = b_page.getReqMap()["zw_f_last_status"]! >
<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<script>
require(['cmanage/js/flowChart','cfamily/js/select2/select2'],
function(p)
{
	zapjs.f.ready(function()
	{
		flowChart.laststatus = "${last_status}";
		p.init_flowChart();
	});
});
$(function(){
checkClickIds();
})

var clickedIds = [];
var checkedVales = [];
var userkey_spfb_wzbj = "${user_info.getUserCode()?default("")}";
function checkClickIds(){
	var obj = {};
	obj.operation="get";
	obj.type = "string";
	obj.key = userkey_spfb_wzbj;
	//obj.value;
	//time;
	obj.preKey="sbfb_wzbj";
	zapjs.zw.api_call('com_cmall_systemcenter_api_ApiRedisOperation',obj,function(result) {
		if(result.success==true){
			if(result.data==undefined){
				clickedIds = [];
			}else{
				clickedIds = result.data.split(",");
			}
		}
	});
}
function pushClickId(code){
	var obj = {};
	obj.operation="set";
	obj.type = "string";
	obj.key = userkey_spfb_wzbj;
	obj.value = code;
	obj.time = 60*60*4;
	obj.preKey="sbfb_wzbj";
	zapjs.zw.api_call('com_cmall_systemcenter_api_ApiRedisOperation',obj,function(result) {
		if(result.success==true){
			
		}
	});
}
function deletec(){

var obj = {};
	obj.operation="delete";
	obj.type = "string";
	obj.key = userkey_spfb_wzbj;
	obj.preKey="sbfb_wzbj";
	zapjs.zw.api_call('com_cmall_systemcenter_api_ApiRedisOperation',obj,function(result) {
		if(result.success==true){
			alert("成功");
		}
	});
}

function previewClick(code){
    if(clickedIds.indexOf(code) < 0){
    	pushClickId(code);
    	clickedIds.push(code);
    }
    
}

function batchApprove(){
	checkClickIds();
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



function submitThisFlow(button){
	var tostatus = $(button).attr("zap_tostatus_attr");
	$("#zw_f_to_status").val(tostatus);
	zapjs.zw.func_call(button);
}
</script>
<#macro m_zapmacro_cmanage_flow_table e_pagedata,b_method>
<#assign user_support=b_method.upClass("com.srnpr.zapweb.websupport.UserSupport")>
<#assign user_info=user_support.getUserInfo()>

<@m_common_html_js ["cmanage/js/flow.js"]/>
<table  class="table  table-condensed table-striped table-bordered table-hover">
	<thead>
	    <tr>

	   		<th><input id="chb-item-all" type="checkbox" onclick=$('input[id^="chb-item-"]').not("[disabled='disabled']").prop('checked',$(this).prop('checked'))></th>
	        <#list e_pagedata.getPageHead() as e_list> 	
				 
			     	<th>
			      	 	${e_list}
			      	 </th>
		      	 	<#if e_list_index==4 >
					 	 <th>商品分类</th>
					</#if>
			      	 
	        </#list>	
	    </tr>
  	</thead>
   <tbody>
		<#list e_pagedata.getPageData() as e_list>
			<tr>
			<#assign productCode=e_list[3]>
			<#if e_list[6]="网站编辑待审批">
<td><input id="chb-item-${e_list[0]}" type="checkbox" value="${e_list[0]}" onclick="$('#chb-item-all').prop('checked',false)" data-productcode="${e_list[3]}"></td>
			<#else>
<td><input id="chb-item-${e_list[0]}" type="checkbox" disabled="disabled" value="${e_list[0]}" onclick="$('#chb-item-all').prop('checked',false)" data-productcode="${e_list[3]}"></td>			
			</#if>
			

	  		
	  		 <#list e_list as e>
	 
	  		 	<#if e_index = 7>
	  		 		
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
			 		 <td>
						<#assign categoryMap=sellercategoryService.getCateGoryByProduct(productCode,"SI2003")>
						<#assign keys=categoryMap?keys>
						<#list keys as key>
							${categoryMap[key]?trim}<br>
						</#list>
					</td>
		      	<#elseif e_index = 8>
		      		<td>
		      			<#if e_list[6]="编辑待审批">
		      				${e?default("")}
		      			</#if>
		      			
		      		</td>
		      	<#elseif e_index = 9>
		      		<td>
		      			<#if e_list[6]="网站编辑待审批">
		      				${e?default("")}
		      			</#if>
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
	</form>
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
	  		<div class="control-group">
				<label class="control-label" for="zw_f_current_status">当前状态</label>
				<div class="controls">
					<select name="zw_f_current_status" id="zw_f_current_status">
						<option value="">请选择</option>
						<option value="4497172300160013" <#if  e.getPageFieldValue()=="4497172300160013"> selected="selected" </#if> >编辑待审批</option>
						<option value="4497172300160003" <#if  e.getPageFieldValue()=="4497172300160003"> selected="selected" </#if> >网站编辑待审批</option>
					</select>
				</div>
			</div>
		<#elseif  (e.getFieldTypeAid()=="104005019" && e.getPageFieldName() = "zw_f_last_status")>
			<div class="control-group">
				<label class="control-label" for="zw_f_last_status">上一审批状态</label>
				<div class="controls">
					<select name="zw_f_last_status" id="zw_f_last_status">
						<option value="">请选择</option>
						<option value="4497172300160002">普通运营待审批</option>
						<option value="4497172300160004">跨境运营待审批</option>
						<option value="4497172300160003">网站编辑待审批</option>
						<option value="4497172300160012">法务驳回</option>
					</select>
				</div>
			</div>
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
	<input type="hidden" id="zw_f_to_status" name="zw_f_to_status" value="4497172300160013">
	<input type="hidden" id="zw_f_flow_code" name="zw_f_flow_code" value="#zw_f_flow_code#">
	<input type="hidden" id="zw_f_current_status" name="zw_f_current_status" value="4497172300160003">
	<div class="control-group">
		<label class="control-label" for="zw_f_source_code">审批意见:</label>
		<div class="controls">
			<textarea id="zw_f_remark" name="zw_f_remark"></textarea>
		</div>
	</div>
	
	<div class="control-group">
		<div class="controls">
			<div class="btn-toolbar">
				<input id="flow_form_btn_submit" type="button" style="margin-right:5px;" class="btn  btn-primary" zapweb_attr_operate_id="115793e80b38485aaba8223e0ea101b7" zap_tostatus_attr="4497172300160013" value="同意" onclick="zapjs.zw.func_call(this)">
				
<#--    
				<input id="flow_form_btn_submit" type="button" style="margin-right:5px;" class="btn  btn-primary" zapweb_attr_operate_id="115793e80b38485aaba8223e0ea101b7" zap_tostatus_attr="4497172300160008" value="驳回" onclick="submitThisFlow(this)">
				 -->
			</div>
		</div>
	</div>
	</form>	
</script>


