<#assign  payInfo=b_method.upDataOneOutNull("fh_payment_type","pay_type,pay_name","","pay_type=:pay_type","pay_type",RequestParameters['pay_type']) />
<form class="form-horizontal" method="POST" >
	<input type="hidden"  id="zw_f_pay_type" name="zw_f_pay_type" value="${payInfo.pay_type}">
	<div class="control-group">
		<label class="control-label" ><span class='w_regex_need'>*</span>支付类型：</label>
		<div class="controls">
			<input type="text" value="${payInfo.pay_name}" disabled>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_seller"><span class='w_regex_need'>*</span>商户编码：</label>
		<div class="controls">
			<input type="text" id="zw_f_seller" name="zw_f_seller" value="">
		</div>
	</div>	
	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
</form>

