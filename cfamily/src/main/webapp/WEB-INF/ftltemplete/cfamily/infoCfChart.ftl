<@m_zapmacro_common_page_chart b_page />
<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<script>
require(['cfamily/js/infoCfChart','cfamily/js/select2/select2'],
function(p)
{
	zapjs.f.ready(function()
	{
		p.init_infoCfChart();
	});
});
</script>



