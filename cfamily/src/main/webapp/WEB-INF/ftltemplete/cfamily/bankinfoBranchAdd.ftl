<#assign area=b_method.upClass("com.cmall.systemcenter.service.ChinaAreaService")>
<form class="form-horizontal" method="POST" >
    <@m_zapmacro_common_auto_list b_page.upAddData() b_page />
	<div class="control-group">
		<label class="control-label" for="zw_f_visible"><span class='w_regex_need'>*</span>支行名称：</label>
		<div class="controls">
			<input type="text" name="zw_f_bank_name_branch" id="zw_f_bank_name_branch" value="">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_visible"><span class='w_regex_need'>*</span>银联编号：</label>
		<div class="controls">
			<input type="text" name="zw_f_bank_number" id="zw_f_bank_number" value="">
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="zw_f_branch_area_address"><span class="w_regex_need">*</span>支行所在省市：</label>
		<div class="controls">
			<select name="zw_f_branch_area_address" id="branch_area_address_provice" onChange="merchantAdd.selectProvince(this)">
				<#list area.getProvince() as provices>
					<option value="${provices.code}">${provices.name}</option>
				</#list>
  			</select>
  			<select name="zw_f_branch_area_address_city" id="branch_area_address_city" onChange="merchantAdd.selectCity(this.value)">
  				<#list area.getCity('110000') as city>
					<option value="${city.code}">${city.name}</option>
				</#list>
			</select>
		</div>
	</div>			
	<@m_zapmacro_common_auto_operate   b_page.getWebPage().getPageOperate()  "116001016" />
</form>
<script>
require(['cfamily/js/merchantAdd','cfamily/js/select2/select2']);
</script>