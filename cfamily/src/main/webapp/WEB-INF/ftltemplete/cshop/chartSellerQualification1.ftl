<script type="text/javascript" charset="utf-8" async="" data-requirecontext="_" data-requiremodule="lib/datepicker/WdatePicker" src="../resources/lib/datepicker/WdatePicker.js"></script>
<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<script>
	require(['cfamily/js/chartSellerQualification','cfamily/js/select2/select2'],
		function(p)
		{
			zapjs.f.ready(function()
			{
				p.init();
			});
		}
	);
</script>

<@m_zapmacro_common_page_chart b_page />
<#-- 列表页 -->
<#macro m_zapmacro_common_page_chart e_page>
	
	<#local e_pagedata=e_page.upChartData()>
	<div class="zw_page_common_data">
	<@m_zapmacro_common_table e_pagedata />
	<@m_zapmacro_common_page_pagination e_page  e_pagedata />
	
	</div>
</#macro>
