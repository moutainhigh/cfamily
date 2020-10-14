<@m_zapmacro_common_page_add b_page />


<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	<div class="control-group">
		<label class="control-label" for="zw_f_view_code"><span class='w_regex_need'>*</span>订单号：</label>
		<div class="controls">
			<input type="text" id="zw_f_order_code" name="zw_f_order_code" value="" placeholder="DD开头的小订单号">
		</div>	
	</div>

	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>