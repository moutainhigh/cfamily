<#assign  payInfo=b_method.upDataOneOutNull("fh_payment_type","zid,pay_type,pay_name,visible","","pay_type=:pay_type","pay_type",RequestParameters['pay_type']) />
<form class="form-horizontal" method="POST" >
	<input type="hidden" id="zw_f_zid" name="zw_f_zid" value="${payInfo.zid}">
	<div class="control-group">
		<label class="control-label" for="zw_f_pay_type"><span class='w_regex_need'>*</span>支付类型：</label>
		<div class="controls">
			<#assign payTypeList = b_method.upDataQuery('sc_define','','','parent_code','44974628')>
			<select name="zw_f_pay_type" id="zw_f_pay_type" disabled>
				<option value="">请选择</option>
				<#list payTypeList as e>
					<option value="${e["define_code"]}">${e["define_name"]}</option>
				</#list>
			</select>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_visible"><span class='w_regex_need'>*</span>前端可见：</label>
		<div class="controls">
			<label class="checkbox-inline">
			  <input type="checkbox" name="zw_f_visible" id="zw_f_visible_WAP" value="WAP"> WAP
			</label>
			<label class="checkbox-inline">
			  <input type="checkbox" name="zw_f_visible" id="zw_f_visible_WEB" value="WEB"> WEB
			</label>
			<label class="checkbox-inline">
			  <input type="checkbox" name="zw_f_visible" id="zw_f_visible_ANDROID" value="ANDROID"> ANDROID
			</label>
			<label class="checkbox-inline">
			  <input type="checkbox" name="zw_f_visible" id="zw_f_visible_IOS" value="IOS"> IOS
			</label>									
		</div>
	</div>	
	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
	
	<div class="control-group">
		<span class='w_regex_need'>*</span>注：前端可见都不勾选则等同于禁用</label>
	</div>	
</form>
<script>
$('#zw_f_pay_type').val('${payInfo.pay_type}');
var visible = '${payInfo.visible}';
var vs = visible.split(',');
for(var i in vs){
    $('#zw_f_visible_'+vs[i]).attr('checked',true);
}
</script>