<@m_zapmacro_common_page_add b_page />





<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<#if e_field.getPageFieldName() == "zw_f_template_number" >
			<div>
				<input type="hidden" id="zw_f_template_number_show_text" value="${e_field.getPageFieldValue()}">
				<script type="text/javascript">
					zapjs.f.require(['zapadmin/js/pageTemplete_single'],function(a){pageTemplete_single.init({"text":"","source":"page_chart_v_fh_pageTemple_single","id":"zw_f_template_number","value":"","seller_code":"SI2003","max":"100","single":true});});
				</script>
			</div>
			<input type="hidden" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<#else>
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		</#if>
	<@m_zapmacro_common_field_end />
</#macro>



