<@m_zapmacro_common_page_add b_page />
<#assign detainList = b_method.upDataQueryToJson('fh_agent_profit_setting','','','')>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		<#if e.getPageFieldName() == "zw_f_end_amt" >
			<@m_zapmacro_common_auto_field e e_page/>
		<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
	  	</#if>
	</#list>
	</#if>
</#macro>

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if e_field.getPageFieldName() == "zw_f_company_rate">
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}" readOnly>
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		</#if>
	<@m_zapmacro_common_field_end />
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
	function presubmit(obj) {
		var detainList = ${detainList};
		if(detainList.length>0){
			zapjs.f.modal({
					content : '已经配置过了，无法重复配置!'
				});
				return;
		}
		var regex = /^\d+$/;
		if(!regex.test($("#zw_f_coupon_rate").val())||$("#zw_f_coupon_rate").val()<=0||$("#zw_f_coupon_rate").val()>=100){
			zapjs.f.modal({
					content : '只能填大于0小于100的整数！'
				});
				return;
		}
		if(!regex.test($("#zw_f_agent_rate").val())||$("#zw_f_agent_rate").val()<=0||$("#zw_f_agent_rate").val()>=100){
			zapjs.f.modal({
					content : '只能填大于0小于100的整数！'
				});
				return;
		}
		if(!regex.test($("#zw_f_fans_rate").val())||$("#zw_f_fans_rate").val()<=0||$("#zw_f_fans_rate").val()>=100){
			zapjs.f.modal({
					content : '只能填大于0小于100的整数！'
				});
				return;
		}
		var temp = 100-$("#zw_f_coupon_rate").val()-$("#zw_f_agent_rate").val()-$("#zw_f_fans_rate").val();
		$("#zw_f_company_rate").val(temp);
		
		zapjs.zw.func_add(obj);
	}
</script>