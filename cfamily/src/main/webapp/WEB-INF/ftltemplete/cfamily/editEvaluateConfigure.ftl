
<@m_zapmacro_common_page_edit b_page />

<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
	<form class="form-horizontal" method="POST" >
		<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
		<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
	</form>
</#macro>
<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field e e_page/>
	  	
	</#list>
	</#if>
</#macro>


<script>
	$(function(){
		var evaluate_type = $('label[for="zw_f_evaluate_type"]').next().text().trim();
		if(evaluate_type == "买家秀"){
			$("#zw_f_limit_value").parent().parent().hide();
			$("#zw_f_tip").parent().parent().hide();
			$("#zw_f_limit_value").removeAttr("zapweb_attr_regex_id");
			$("#zw_f_limit_value").val(0);
			$("#zw_f_tip").val("");
		}

	});
</script>









