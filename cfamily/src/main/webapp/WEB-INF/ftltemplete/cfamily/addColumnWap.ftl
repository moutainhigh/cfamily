<link type="text/css" href="../resources/lib/datepicker/laydate.css" rel="stylesheet">
<script>
require(['cfamily/js/addColumnWap'],

function()
{
	zapjs.f.ready(function()
		{
			addColumnWap.init();
		}
	);
}

);
</script>

<@m_zapmacro_common_page_add b_page />

<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>




<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>
	<input id="showmore_type" type="hidden" name= "zw_f_is_showmore">
	<input id="view_type" type="hidden" name= "zw_f_view_type">
	<#if e_pagedata??>
	<#list e_pagedata as e>
	<#if e.getPageFieldName() = "zw_f_showmore_linkvalue">
				<div id="linkvalueDiv" class="control-group">
				<script type="text/javascript">
		         zapjs.f.require(['zapadmin/js/ProductPopSelect'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_showmore_linkvalue","value":"","max":"1"});});
		         
	           </script>
					<label class="control-label" for="zw_f_showmore_linkvalue">
						URL：
					</label>
					<input id="zw_f_showmore_linkvalue_show_text" type="hidden" value="">
					<div id="slId" class="controls">
						<input id="zw_f_showmore_linkvalue" zapweb_attr_regex_id="469923180002" type="text" value="" name="zw_f_showmore_linkvalue">
						<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(URL地址不能包含http://share)</span>
					</div>
				</div>
		<#else>			
	  		<@m_zapmacro_common_auto_field e e_page/>
		</#if>
	  	
	</#list>
	</#if>
</#macro>


