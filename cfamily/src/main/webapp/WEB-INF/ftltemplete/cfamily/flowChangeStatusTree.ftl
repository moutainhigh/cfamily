

<div class="w_left zw_page_tree_left">

	<@m_common_html_script "require(['cfamily/js/flowStatusChange'],function(a){cfamily.flowStatusChange.init('flowStatusChangeId');});" />
	<input type="hidden" value="" id="selectDefineValueId">
	<a class="btn btn-small"  onclick="cfamily.flowStatusChange.add('flowStatusChangeId')">添加流程配置</a>
	
	<div class="zw_page_tree_box">
		<select style="width: 80%; height: 300px;" id="flowStatusChangeId" size="30" >
		
			<#assign sc_defineService=b_method.upClass("com.cmall.systemcenter.service.ScFlowBase")>
			<#if sc_defineService??>
				<#assign scDefineList = sc_defineService.getFlowTypeByType("0")>
				<#if scDefineList??>
					<#list scDefineList as e_list>
						 <#assign defineCode=e_list["define_code"]>
						 <#assign defineName=e_list["define_name"]>
				      	<option value="${defineCode?default("")}">${defineName?default("")}</option>
				     </#list>
				 </#if>
			</#if>
			
		</select>
	</div>
</div>



<div class="zw_page_tree_right" id="zw_page_tree_right">
	
	<div id="zw_page_flow_add"></div>
	<div id="zw_page_flow_chart"></div>
	
</div>

