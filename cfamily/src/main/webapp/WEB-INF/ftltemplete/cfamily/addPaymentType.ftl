<form class="form-horizontal" method="POST" >
	<div class="control-group">
		<label class="control-label" for="zw_f_pay_type"><span class='w_regex_need'>*</span>支付类型：</label>
		<div class="controls">
			<#assign payTypeList = b_method.upDataQuery('sc_define','','','parent_code','44974628')>
			<select name="zw_f_pay_type" id="zw_f_pay_type">
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
			  <input type="checkbox" name="zw_f_visible" value="WAP"> WAP
			</label>
			<label class="checkbox-inline">
			  <input type="checkbox" name="zw_f_visible" value="WEB"> WEB
			</label>
			<label class="checkbox-inline">
			  <input type="checkbox" name="zw_f_visible" value="ANDROID"> ANDROID
			</label>
			<label class="checkbox-inline">
			  <input type="checkbox" name="zw_f_visible" value="IOS"> IOS
			</label>									
		</div>
	</div>	
	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
	<div class="control-group">
		<span class='w_regex_need'>*</span>注：前端可见都不勾选则等同于禁用</label>
	</div>		
</form>