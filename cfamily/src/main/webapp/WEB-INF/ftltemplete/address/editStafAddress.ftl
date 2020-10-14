
<@m_zapmacro_common_page_edit b_page />
<#-- 修改页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
			
	
	<div class="control-group">
		<label class="control-label" for="zw_f_area_code"><span class="w_regex_need">*</span>收货人地址：</label>
		
		<div class="controls">
			<span id="province_city_area" name="province_city_area">
				<select id='_province' name='_province'></select>&nbsp;
				<select id='_city' name='_city'></select>&nbsp;
				<select id='_area' name='_area'></select>&nbsp;
				<select id='_street' name='_street'></select>
			</span>
		</div>
	</div>
		
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<script type="text/javascript" src="../resources/address/edit_staf_address.js"></script>












