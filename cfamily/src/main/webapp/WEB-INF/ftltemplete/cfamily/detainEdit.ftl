<@m_zapmacro_common_page_edit b_page />
<#assign uid = b_page.getReqMap()['zw_f_uid']>
<#assign detainList = b_method.upDataQueryToJson("sc_detain_configure","","","uid <> '"+uid+"'")>

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
		<#if e_field.getPageFieldName() == "zw_f_start_amt" || e_field.getPageFieldName() == "zw_f_end_amt" >
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
			<span>元</span>
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
	function presubmit(obj) {
		if($("#zw_f_integral").val() % 100 != 0) {
			zapjs.f.modal({
				content : '积分填写错误!'
			});
			return;			
		}
		var detainList = ${detainList};
		for(var i=0;i<detainList.length;i++){
			if(detainList[i].start_amt<=$("#zw_f_start_amt").val()&&$("#zw_f_start_amt").val()<detainList[i].end_amt){
				zapjs.f.modal({
					content : '价格范围重叠!'
				});
				return;
			}
			if(detainList[i].start_amt<=$("#zw_f_end_amt").val()&&$("#zw_f_end_amt").val()<detainList[i].end_amt){
				zapjs.f.modal({
					content : '价格范围重叠!'
				});
				return;
			}
		}
		
		zapjs.zw.func_edit(obj);
	}
</script>