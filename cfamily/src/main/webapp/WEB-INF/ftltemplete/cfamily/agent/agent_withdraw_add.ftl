<@m_common_html_script "require(['cfamily/js/agent/agent_withdraw_add'],function(a){a.init_page()});" />
<form class="form-horizontal" method="POST">
<div class="control-group">
	<!-- <label class="control-label" for="zw_f_apply_code"><span class="w_regex_need">*</span>提现申请编号：</label> -->
	<div class="controls">

		<input class="btn" type="button" value="选择" onclick="agent_withdraw_add.show_windows()">
	</div>
</div>
<div class="control-group">
	<label class="control-label" for="zw_f_member_code"><span class="w_regex_need">*</span>分销人ID：</label>
	<div class="controls">

		<input type="text" id="zw_f_member_code" name="zw_f_member_code" zapweb_attr_regex_id="469923180002" value="">
	</div>
</div>
<div class="control-group">
	<label class="control-label" for="zw_f_nickname"><span class="w_regex_need">*</span>分销人姓名：</label>
	<div class="controls">

		<input type="text" id="zw_f_nickname" name="zw_f_nickname" zapweb_attr_regex_id="469923180002" value="">
	</div>
</div>
<div class="control-group">
	<label class="control-label" for="zw_f_login_name"><span class="w_regex_need">*</span>分销人电话：</label>
	<div class="controls">

		<input type="text" id="zw_f_login_name" name="zw_f_login_name" zapweb_attr_regex_id="469923180002" value="">
	</div>
</div>
<div class="control-group">
	<label class="control-label" for="zw_f_real_money"><span class="w_regex_need">*</span>可提现余额：</label>
	<div class="controls">

		<input type="text" id="zw_f_real_money" name="zw_f_real_money" zapweb_attr_regex_id="469923180002" value="">
	</div>
</div>
<div class="control-group">
	<label class="control-label" for="zw_f_withdraw_money"><span class="w_regex_need">*</span>申请金额：</label>
	<div class="controls">

		<input type="text" id="zw_f_withdraw_money" name="zw_f_withdraw_money" zapweb_attr_regex_id="469923180002" value="">
	</div>
</div>
 	
	<div class="control-group">
    	<div class="controls">
		<input type="button" class="btn  btn-success" zapweb_attr_operate_id="3e12e4ff521511eaabac005056165069" onclick="zapjs.zw.func_add(this)" value="提交新增">
    	</div>
	</div>
</form>


