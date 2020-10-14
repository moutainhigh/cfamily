<script>
require(['cfamily/js/cdkeyAdd'],

function()
{
	zapjs.f.ready(function()
		{
			cdkeyAdd.init();
		}
	);
}

);
</script>
<@m_zapmacro_common_page_add b_page />

<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<#assign activity_code = b_page.getReqMap()["zw_f_activity_code"] >
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<input type="hidden" name="zw_f_activity_code" id="zw_f_activity_code" value="${activity_code}"> 
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>
