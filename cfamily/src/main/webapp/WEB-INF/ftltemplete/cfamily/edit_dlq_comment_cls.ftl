
<@m_zapmacro_common_page_edit b_page />

<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >

	<#-- 添加快捷回复选项 -->
	<#assign selectCommentRelList = b_method.upControlPage("page_chart_v_fh_dlq_comment_cls_rel","zw_p_sql_where=flg='1001'&zw_p_size=-1").upChartData()>
	<div class="control-group">
		<label class="control-label">快捷回复内容：</label>
		<div class="controls">
			<select id="select_dlq_comment_cls_rel">
			
				<#if (selectCommentRelList.getPageData()?size > 0 )>
					<option value="">请选择</option>
					<#list selectCommentRelList.getPageData() as e_list>
						<option value="${e_list[0]}">${e_list[0]}</option>
					</#list>
				<#else>
					<option value="">暂时无快捷回复内容</option>
				</#if>
				
			</select>
		</div>
	</div>
	<script type="text/javascript">
		$("#select_dlq_comment_cls_rel").change(function(){
			$("#zw_f_rtn_content").val($(this).val());
		});
	</script>
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>
