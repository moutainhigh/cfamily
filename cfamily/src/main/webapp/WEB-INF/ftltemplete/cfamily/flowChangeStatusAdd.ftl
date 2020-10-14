<@m_zapmacro_flowchange_page_add b_page,b_method />

<#macro m_zapmacro_flowchange_page_add e_page,b_method>
<form class="form-horizontal" method="POST" >
	<#assign sc_defineService=b_method.upClass("com.cmall.systemcenter.service.ScFlowBase")>
	<#assign flowType = b_page.getReqMap()["zw_f_flow_type"]>
	
	<#assign e_pagedata=e_page.upAddData()> 
		
	  <#if flowType??>
		  <#if e_pagedata??>
				<#list e_pagedata as e_field>
					<#if e_field.getPageFieldName() = "zw_f_flow_type">
						
						  <div class="control-group">
						    	<label class="control-label" for="${e_field.getPageFieldName()}">${e_field.getFieldNote()}</label>
						    	<div class="controls" style="margin-top:5px;">
						      		<#if sc_defineService??>
						  		 		<label>${sc_defineService.getTypeNameByCode(flowType)}</label>
						 			<#else>
						 				 <label>未知类型</label>
						 			</#if>
						 			<input type="hidden" id="zw_f_flow_type" name="zw_f_flow_type" value="${flowType?default("")}" />
						    	</div>
						  </div>
					<#elseif (e_field.getPageFieldName() = "zw_f_from_status" || e_field.getPageFieldName() = "zw_f_to_status")>
						<#assign scDefineList = sc_defineService.getFlowStatusListByTypeCode(flowType)>
						<div class="control-group">
					    	<label class="control-label" for="${e_field.getPageFieldName()}">${e_field.getFieldNote()}</label>
					    	<div class="controls">
					      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
					      			<option value="">请选择</option>
					      			<#list scDefineList as e_key>
										<option value="${e_key.getDefineCode()}" <#if  e_field.getPageFieldValue()==e_key.getDefineCode()> selected="selected" </#if>>${e_key.getDefineName()}</option>
									</#list>
					      		</select>
					    	</div>
					  </div>
					<#else>
						<@m_zapmacro_common_auto_field e_field e_page/>
					</#if>
				</#list>
		  </#if>
	  </#if>
	  
		 <div class="control-group">
	    	<div class="controls">
				 <input type="button" class="btn  btn-success" zapweb_attr_operate_id="a024351424d911e3a2e7000c298b20fd" onclick="zapjs.zw.func_add(this)" value="提交新增">
	    	</div>
		</div>
	  
	  
	 
</form>
</#macro>