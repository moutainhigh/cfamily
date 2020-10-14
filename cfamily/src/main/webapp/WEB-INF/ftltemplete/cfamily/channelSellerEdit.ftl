
<script>

require(['cfamily/js/channelSellerClear'],

	function() {
		zapjs.f.ready(function(){
			$("#zw_f_channel_seller_name").after("<span class='w_regex_need'>务必填写合作商全称避免开票错误</span>");
			$("#zw_f_channel_link_mobile").after("<span class='w_regex_need'>（手机号或座机）</span>");
			$("#zw_f_telephone").after("<span class='w_regex_need'>（座机）</span>");
		});
	}
	
);
</script>


<@m_zapmacro_common_page_edit b_page />


<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		
	  	<@m_zapmacro_common_auto_field e e_page/>
	  	
      	
	</#list>
	</#if>
</#macro>

