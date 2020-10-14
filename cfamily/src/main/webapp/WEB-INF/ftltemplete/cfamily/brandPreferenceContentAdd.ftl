<script>
require(['cfamily/js/brandPreferenceContentAdd'],

function()
{
	zapjs.f.ready(function()
		{
			brandPreferenceContentAdd.init();
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
<#assign infoCode = b_page.getReqMap()["zw_f_info_code"] >
	<#if e_pagedata??>
	<#list e_pagedata as e>
		<#if e.getPageFieldName() = "zw_f_brand_location">
			<div class="control-group">
				<label class="control-label" for="zw_f_brand_location">
				<span class="w_regex_need">*</span>
				位置：
				</label>
				<div class="controls">
					<select id="zw_f_brand_location" name="zw_f_brand_location">
						<option value="1">头部</option>
						<option value="2">尾部</option>
					</select>
				</div>
			</div>	
		<#elseif e.getPageFieldName() = "zw_f_link_value">
			<div id="linkvalueDiv" class="control-group">
				<script type="text/javascript">
		         zapjs.f.require(['zapadmin/js/ProductPopSelect'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_link_value","value":"","max":"1"});});
	           </script>
					<label class="control-label" for="zw_f_link_value">
						URL：
					</label>
					<input id="zw_f_link_value_show_text" type="hidden" value="">
					<input id="zw_f_info_code" name="zw_f_info_code" type="hidden" value="${infoCode}">
					<input id="zw_f_showmore_linkvalue" type="hidden">
					<div id="slId" class="controls">
						<input id="zw_f_link_value" zapweb_attr_regex_id="469923180002" type="text" value="" name="zw_f_link_value">
					</div>
				</div>
		<#else>			
	  		<@m_zapmacro_common_auto_field e e_page/>
		</#if>
	  	
	</#list>
	</#if>
</#macro>

