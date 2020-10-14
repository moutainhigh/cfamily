<#assign uid = b_page.getReqMap()["zw_f_uid"]>

<script>
require(['cfamily/js/homeRecommend'],
function(p)
{
	<#assign service=b_method.upClass("com.cmall.familyhas.service.HomeColumnService")>
	var categoryCode=${service.getHomeRecoomendCategoryByUid(uid)};
	zapjs.f.ready(function()
	{
		p.init(categoryCode);
	});
});
</script>
<@m_zapmacro_common_page_edit b_page />

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<#if e_field.getPageFieldName() == 'zw_f_category_code'>
		<div>
			<span style="float:left;margin-left:400px;position: absolute;line-height:32px"><input class="btn btn-success" type="button" value="修改分类" onclick="zapadmin.window_url('../show/page_chart_v_uc_sellercategory_select')"></span>
			<div class="control-group">
				<label class="control-label">
					<span class="w_regex_need">*</span>分类：
				</label>
				<div class="controls">
					<input type="hidden" id='zw_f_showmore_linkvalue' name='zw_f_category_code'  value="${e_field.getPageFieldValue()}"/>
					<div class="control_book" id='zw_f_showmore_linkvalue_text'></div>
				</div>
			</div>
		</div>
	<#elseif e_field.getPageFieldName() == 'zw_f_sort'>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">数字大，展示在前
		<@m_zapmacro_common_field_end />
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
			<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<@m_zapmacro_common_field_end />
	</#if>

</#macro>


