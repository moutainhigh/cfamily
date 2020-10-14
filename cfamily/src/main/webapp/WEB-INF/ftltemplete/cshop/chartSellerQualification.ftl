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