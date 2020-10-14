<@m_zapmacro_common_page_add b_page />
<#assign detainList = b_method.upDataQueryToJson('fh_agent_profit_setting','','','')>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>
	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field1 e e_page/>
	  	
	</#list>
	</#if>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_field1 e_field   e_page>
		<#if e_field.getPageFieldName() == "zw_f_level_code">
		<input type="hidden" value="" name="zw_f_member_code" id="zw_f_member_code"/>
		<div class="control-group">
			<label class="control-label" for="zw_f_search">查询：</label>
			<div class="controls">
				<input id="zw_f_login_name" name="zw_f_login_name" value="" type="text">
				<input class="btn btn-small" onclick="search(this)" type="button" value="手机号查询">
			</div>
		</div>
		<@m_zapmacro_common_field_select  e_field  e_page ""/>
		<#elseif e_field.getPageFieldName() == "zw_f_nickname">
		<div class="control-group">
			<label class="control-label" for="zw_f_mobile">手机号：</label>
			<div class="controls">
				<input id="zw_f_mobile" name="zw_f_mobile" value="" type="text" readOnly>
			</div>
		</div>
		<@m_zapmacro_common_field_text  e_field />
		<#else>
			<@m_zapmacro_common_auto_field e_field e_page/>
		</#if>		
		
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if e_field.getPageFieldName() == "zw_f_nickname">
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}" readOnly>
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>

<#macro m_zapmacro_common_field_select   e_field    e_page    e_text_select="">
	<#if (e_field.getFieldName()=="level_code")>
    	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}" disabled>
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
		<@m_zapmacro_common_field_end />
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
	      		<select name="${e_field.getPageFieldName()}" id="${e_field.getPageFieldName()}">
	      			<#if e_text_select!="">
	      					<option value="">${e_text_select}</option>
	      				</#if>
	      			<#list e_page.upDataSource(e_field) as e_key>

						<option value="${e_key.getV()}" <#if  e_field.getPageFieldValue()==e_key.getV()> selected="selected" </#if>>${e_key.getK()}</option>
					</#list>
	      		</select>
		<@m_zapmacro_common_field_end />	
	</#if>
</#macro>

<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="presubmit(this)"  value="${e_operate.getOperateName()}" />
</#macro>

<script>	
	$(function(){
		$("#zw_f_coupon_rate").blur(function(){
			var temp = 100-$("#zw_f_coupon_rate").val()-$("#zw_f_agent_rate").val()-$("#zw_f_fans_rate").val();
			$("#zw_f_company_rate").val(temp);
		});
		$("#zw_f_agent_rate").blur(function(){
			var temp = 100-$("#zw_f_coupon_rate").val()-$("#zw_f_agent_rate").val()-$("#zw_f_fans_rate").val();
			$("#zw_f_company_rate").val(temp);
		});
		$("#zw_f_fans_rate").blur(function(){
			var temp = 100-$("#zw_f_coupon_rate").val()-$("#zw_f_agent_rate").val()-$("#zw_f_fans_rate").val();
			$("#zw_f_company_rate").val(temp);
		});
	});
	function search(obj) {
		if($("#zw_f_login_name").val()==''){
			zapjs.f.modal({
					content : '请输入手机号'
				});
				return;
		}
		var syurl="/cfamily/jsonapi/com_cmall_familyhas_api_ApiGetFxrInfo?api_key=betafamilyhas&wxCode="+$("#zw_f_login_name").val();
			$.ajax({
		 		type:"GET",
		 		url:syurl,
		 		dataType:"json",
		 		success:function(data){
		 			if(data.resultCode == 1){
		 				$("#zw_f_member_code").val(data.memberCode);
		 				$("#zw_f_nickname").val(data.nickName);
		 				$("#zw_f_mobile").val(data.mobile);
		 			} else {
						zapjs.f.modal({
							content : data.resultMessage
						});
		 			}
		 		}
		 	});
	}
	function presubmit(obj) {		
		$("#zw_f_level_code").removeAttr("disabled");
		
		zapjs.zw.func_add(obj);
	}
</script>