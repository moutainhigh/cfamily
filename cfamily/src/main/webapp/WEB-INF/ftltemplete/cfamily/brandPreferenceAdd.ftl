
<script>
require(['cfamily/js/brandPreferenceAdd'],

function()
{
	zapjs.f.ready(function()
		{
			brandPreferenceAdd.init();
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
		<#if e.getPageFieldName() = "zw_f_flag_show">
			<div class="control-group">
				<label class="control-label" for="zw_f_flag_show">
				<span class="w_regex_need">*</span>
				状态：
				</label>
				<div class="controls">
					<select id="zw_f_flag_show" name="zw_f_flag_show">
						<option value="449746530002">未发布</option>
						<option value="449746530001">已发布</option>
					</select>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_discount_type">
			<div class="control-group">
				<label class="control-label" for="zw_f_discount_type">
				显示折扣类型：
				</label>
				<div class="controls">
					<select id="zw_f_discount_type" name="zw_f_discount_type">
						<option value="1">不显示折扣</option>
						<option value="2">x.x折起</option>
					</select>
				</div>
			</div>
		<#else>			
	  		<@m_zapmacro_common_auto_field e e_page/>
		</#if>
	  	
	</#list>
	</#if>
</#macro>

