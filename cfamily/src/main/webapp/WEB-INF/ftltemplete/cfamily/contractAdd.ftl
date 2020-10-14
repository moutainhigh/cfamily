
<script>
require(['cfamily/js/contractAdd'],

function()
{
	zapjs.f.ready(function()
		{
			contractAdd.init();
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
	<#if e_pagedata??>
	<#list e_pagedata as e>
		<#if e.getPageFieldName() = "zw_f_end_time">
			<div class="control-group">
				<label class="control-label" for="zw_f_end_time">
				<span class="w_regex_need">*</span>
				合同到期日：
				</label>
				<div class="controls">
					<input id="zw_f_end_time" type="text" value="" name="zw_f_end_time" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})">
				</div>
			</div>
			<div id="addPro" class="controls">
				<input class="btn btn-success" type="button" value="添加商品" onclick="contractAdd.show_windows()"/>
			</div>
			<br>
		<#elseif e.getPageFieldName() = "zw_f_dissolution_time">
			<div class="control-group">
				<label class="control-label" for="zw_f_dissolution_time">
				<span class="w_regex_need">*</span>
				解除协议日期：
				</label>
				<div class="controls">
					<input id="zw_f_dissolution_time" type="text" value="" name="zw_f_dissolution_time" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',realDateFmt:'yyyy-MM-dd HH-mm-ss',realTimeFmt:'HH:mm:ss HH-mm-ss'})">
				</div>
			</div>
		<#else>			
	  		<@m_zapmacro_common_auto_field e e_page/>
	</#if>
	</#list>
	</#if>
</#macro>


