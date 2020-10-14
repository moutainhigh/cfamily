<script>
require(['cfamily/js/brandPreferenceContentEdit'],

function()
{
	zapjs.f.ready(function()
		{
			brandPreferenceContentEdit.init();
		}
	);
}

);
</script>


<@m_zapmacro_common_page_edit b_page />

<#-- 编辑页 -->
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
		<#if e.getPageFieldName() = "zw_f_brand_location">
			<div class="control-group">
				<label class="control-label" for="zw_f_brand_location">
				<span class="w_regex_need">*</span>
				位置：
				</label>
				<div class="controls">
					<select id="zw_f_brand_location" name="zw_f_brand_location" value="">
						<option value="1" <#if e.getPageFieldValue()=="1"> selected="selected" </#if>>头部</option>
						<option value="2" <#if e.getPageFieldValue()=="2"> selected="selected" </#if>>尾部</option>
					</select>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_link_type">
			<#if e.getPageFieldValue()=="4497471600020003">
				<#assign sellercategoryService=b_method.upClass("com.cmall.usercenter.service.SellercategoryService")>
				<#assign categroyCode=b_method.upFiledByFieldName(b_page.upBookData(),"link_value").getPageFieldValue() /> 
				<#assign categoryMap=sellercategoryService.getCateGoryShow(categroyCode,"SI2003")>
				<input id="categroyName" type="hidden" value= "${categoryMap.categoryName}">
			</#if>
			<@m_zapmacro_common_auto_field e e_page/>
		<#elseif e.getPageFieldName() = "zw_f_link_value">
			<div id="linkvalueDiv" class="control-group">
				<script type="text/javascript">
		         zapjs.f.require(['zapadmin/js/ProductPopSelect'],function(a){a.init({"text":"","source":"page_chart_v_cf_pc_productinfo_multiSelect","id":"zw_f_link_value","value":"","max":"1"});});
	           </script>
					<label class="control-label" for="zw_f_link_value">
						URL：
					</label>
					<input id="zw_f_link_value_show_text" type="hidden" value="">
					<input id="zw_f_showmore_linkvalue" type="hidden">
					<div id="slId" class="controls">
						<input id="zw_f_link_value" zapweb_attr_regex_id="469923180002" type="text" value="${e.getPageFieldValue()}" name="zw_f_link_value">
					</div>
				</div>
		<#else>			
	  		<@m_zapmacro_common_auto_field e e_page/>
		</#if>
	  	
	</#list>
	</#if>
</#macro>

