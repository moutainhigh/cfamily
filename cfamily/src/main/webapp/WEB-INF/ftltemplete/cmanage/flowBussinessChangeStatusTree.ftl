

<div class="w_left zw_page_tree_left">

	<@m_common_html_script "require(['cmanage/js/flowbussiness'],function(a){cmanage_flowbussiness.init('flowBussinessChangeId');});" />

	<a class="btn btn-small"  onclick="cmanage_flowbussiness.add('flowBussinessChangeId')">添加流程配置</a>
	
	<div class="zw_page_tree_box">
		<select style="width: 80%; height: 300px;" id="flowBussinessChangeId" size="30" >
		
			<#assign sc_defineService=b_method.upClass("com.cmall.systemcenter.service.ScFlowBase")>
			<#if sc_defineService??>
				<#assign scDefineList = sc_defineService.getFlowTypeByType("1")>
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

