<#assign area=b_method.upClass("com.cmall.systemcenter.service.ChinaAreaService")>
<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<script>
require(['cfamily/js/merchantAdd','cfamily/js/select2/select2'],
function() {
	zapjs.f.ready(function()
		{
			merchantAdd.init();
		}
	);
});
</script>
<@m_zapmacro_common_page_add b_page />
<#-- 添加页 -->
<#macro m_zapmacro_common_page_add e_page>
<form class="form-horizontal" method="POST" >
	<input type="hidden" value="" name="zw_f_json" id="zw_f_json"/>
	<@m_zapmacro_common_auto_list  e_page.upAddData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>
<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>

	<#if e_pagedata??>
	<#list e_pagedata as e>
		<#if e.getPageFieldName() = "zw_f_establishment_date">
			<div class="control-group">
				<label class="control-label" for="zw_f_establishment_date">
				<span class="w_regex_need">*</span>
				成立日期：
				</label>
				<div class="controls">
				<input id="zw_f_establishment_date" type="text" value="" name="zw_f_establishment_date" onclick="WdatePicker({dateFmt:'yyyy-MM'})">
				</div>
			</div>
			<script type="text/javascript">
			
					zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});
				
			</script>
		<#elseif e.getPageFieldName() = "zw_f_business_start_time">
			<div class="control-group">
				<label class="control-label" for="zw_f_business_start_time">
				营业期限开始：
				</label>
				<div class="controls">
					<input id="zw_f_business_start_time" type="text" value="" name="zw_f_business_start_time" onclick="WdatePicker({dateFmt:'yyyy-MM'})">
				</div>
			</div>
			<script type="text/javascript">
			
					zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});
				
			</script>
		<#elseif e.getPageFieldName() = "zw_f_business_end_time">
			<div class="control-group">
				<label class="control-label" for="zw_f_business_end_time">
				营业期限结束：
				</label>
				<div class="controls">
					<input id="zw_f_business_end_time" type="text" value="" name="zw_f_business_end_time" onclick="WdatePicker({dateFmt:'yyyy-MM'})">
				</div>
			</div>
			<script type="text/javascript">
					zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});
			</script>
		<#elseif e.getPageFieldName() = "zw_f_branch_area_address">
			<div class="control-group">
				<label class="control-label" for="zw_f_branch_area_address"><span class="w_regex_need">*</span>开户支行所在省市：</label>
				<div class="controls">
					<input type="hidden" id="zw_f_branch_area_address" name="zw_f_branch_area_address" zapweb_attr_regex_id="469923180002" value="">
					<select name="branch_area_address_provice" id="branch_area_address_provice" onChange="merchantAdd.selectProvince(this)">
						<#list area.getProvince() as provices>
							<option value="${provices.code}">${provices.name}</option>
						</#list>
	      			</select>
	      			<select name="branch_area_address_city" id="branch_area_address_city" onChange="merchantAdd.selectCity(this.value)">
	      				<#list area.getCity('110000') as city>
							<option value="${city.code}">${city.name}</option>
						</#list>
					</select>
					<select name="branch_area_address_county" id="branch_area_address_county" onChange="merchantAdd.selectCounty()">
	      				<#list area.getCounty('110100') as city>
							<option value="${city.code}">${city.name}</option>
						</#list>
					</select>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_joint_number">
			<div class="control-group">
				<label class="control-label" for="zw_f_joint_number">
				<span class="w_regex_need">*</span>
				开户行支行联行号:
				</label>
				<div class="controls">
					<input id="zw_f_joint_number" type="text" name="zw_f_joint_number" zapweb_attr_regex_id="469923180002" value="" />
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_organization_code">
			<div class="zab_info_page_title" w_clear id="organization_code_event">
				<span>组织机构信息</span>
			</div>
			<@m_zapmacro_common_auto_field e e_page/>
		<#elseif e.getPageFieldName() = "zw_f_tax_registration_certificate_copy">
			<div class="zab_info_page_title" w_clear id="tax_registration_certificate_copy_event">
				<span>税务登记信息</span>
			</div>
			<@m_zapmacro_common_auto_field e e_page/>
		<#elseif e.getPageFieldName() = "zw_f_open_bank_photocopy_certificate">
			<div class="zab_info_page_title" w_clear>
				<span>开户行信息</span>
			</div>
			<@m_zapmacro_common_auto_field e e_page/>
		<#elseif e.getPageFieldName() = "zw_f_after_sale_person">
			<div class="zab_info_page_title" w_clear>
				<span>售后信息</span>
			</div>
			<@m_zapmacro_common_auto_field e e_page/>
		<#elseif e.getPageFieldName() = "zw_f_account_clear_type">
			<div class="zab_info_page_title" w_clear>
				<span>结算信息</span>
			</div>
			<@m_zapmacro_common_auto_field e e_page/>
		<#else>
	  		<@m_zapmacro_common_auto_field e e_page/>
		</#if>
	  	
	</#list>
	</#if>
</#macro>
<#-- 页面按钮 -->
<#macro m_zapmacro_common_operate_button  e_operate  e_style_css>
	<#if e_operate.getOperateUid() == '311b4b9c0f4911e5a451005056925439'>
		<input type="button" class="${e_style_css}" onclick="merchantAdd.beforeSubmit()" value="${e_operate.getOperateName()}" />
		<input type="button" id="submitApprove" style="display:none" class="btn  btn-success" zapweb_attr_operate_id="311b4b9c0f4911e5a451005056925439" onclick="zapjs.zw.func_add(this)" value="${e_operate.getOperateName()}">
	<#elseif e_operate.getOperateUid() == '3ae2caac7cf04d06bc5a180c9f0bf1ab'>
		<#-- 保存到草稿箱 -->
		<input type="button" class="${e_style_css}" onclick="merchantAdd.saveToDrafts(this)"  value="${e_operate.getOperateName()}" />
		<input type="button" id="draftBoxBtnSubmit" style="display:none" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}" onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" />
	</#if>
</#macro>
<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<#if e_field.getPageFieldName() == 'zw_f_register_money'>
			&nbsp;&nbsp;万元	
		<#elseif e_field.getPageFieldName() == 'zw_f_quality_retention_money'>
			&nbsp;&nbsp;元
		<#elseif e_field.getPageFieldName() == 'zw_f_money_proportion'>
			&nbsp;&nbsp;%
		<#elseif e_field.getPageFieldName() == 'zw_f_bill_day'>
			&nbsp;&nbsp;天
		<#else>
			
		</#if>
	
	<@m_zapmacro_common_field_end />
</#macro>

