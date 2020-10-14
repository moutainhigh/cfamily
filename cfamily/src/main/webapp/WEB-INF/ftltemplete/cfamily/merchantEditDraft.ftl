<#assign area=b_method.upClass("com.cmall.systemcenter.service.ChinaAreaService")>
<#assign business_license_type=b_method.upFiledByFieldName(b_page.upBookData(),"business_license_type").getPageFieldValue() />
<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<script>
require(['cfamily/js/merchantEditDrfat','cfamily/js/select2/select2'],
function()
{
	zapjs.f.ready(function()
		{
			merchantEditDrfat.init('${business_license_type}');
		}
	);
}

);
</script>


<@m_zapmacro_common_page_edit b_page />


<#-- 编辑页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<#-- <input type="hidden" value="" name="zw_f_json" id="zw_f_json"/> -->
	
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>
	<#if e_pagedata??>
	<#list e_pagedata as e>
		<#if e.getPageFieldName() = "zw_f_seller_code">
			<div class="control-group">
				<label class="control-label" for="">
				卖家编号：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_seller_code" name="zw_f_seller_code"  value="${e.getPageFieldValue()}" readonly/><br/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_small_seller_code">
			<div class="control-group">
				<label class="control-label" for="">
				公司编号：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_small_seller_code" name="zw_f_small_seller_code"  value="${e.getPageFieldValue()}" readonly/><br/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_uc_seller_type">
			<div class="control-group">
				<label class="control-label" for="zw_f_uc_seller_type">
					<span class="w_regex_need">*</span>
					商户类型：
				</label>
				<div class="controls">
					<input type="hidden" name="zw_f_uc_seller_type" id="zw_f_uc_seller_type" value="${e.getPageFieldValue()}">
					<select disabled name="zw_f_uc_seller_type_" id="zw_f_uc_seller_type_">
					<#if e.getPageFieldValue() = "4497478100050001">
						<option selected="selected" value="4497478100050001">普通商户</option>
						<option value="4497478100050002">跨境商户</option>
						<option value="4497478100050003">跨境直邮</option>
						<option value="4497478100050004">平台入驻</option>
						<option value="4497478100050005">缤纷商户</option>
					<#elseif e.getPageFieldValue() = "4497478100050002">
						<option value="4497478100050001">普通商户</option>
						<option selected="selected" value="4497478100050002">跨境商户</option>
						<option value="4497478100050003">跨境直邮</option>
						<option value="4497478100050004">平台入驻</option>
						<option value="4497478100050005">缤纷商户</option>
					<#elseif e.getPageFieldValue() = "4497478100050003">
						<option value="4497478100050001">普通商户</option>
						<option value="4497478100050002">跨境商户</option>
						<option selected="selected" value="4497478100050003">跨境直邮</option>
						<option value="4497478100050004">平台入驻</option>
						<option value="4497478100050005">缤纷商户</option>
					<#elseif e.getPageFieldValue() = "4497478100050004">
						<option value="4497478100050001">普通商户</option>
						<option value="4497478100050002">跨境商户</option>
						<option value="4497478100050003">跨境直邮</option>
						<option selected="selected" value="4497478100050004">平台入驻</option>
						<option value="4497478100050005">缤纷商户</option>
					<#elseif e.getPageFieldValue() = "4497478100050005">
						<option value="4497478100050001">普通商户</option>
						<option value="4497478100050002">跨境商户</option>
						<option value="4497478100050003">跨境直邮</option>
						<option value="4497478100050004">平台入驻</option>
						<option selected="selected" value="4497478100050005">缤纷商户</option>
					</#if>
      				</select>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_establishment_date">
			<div class="control-group">
				<label class="control-label" for="zw_f_establishment_date">
				<span class="w_regex_need">*</span>
				成立日期：
				</label>
				<div class="controls">
				<input id="zw_f_establishment_date" type="text" value="${e.getPageFieldValue()}" name="zw_f_establishment_date" onclick="WdatePicker({dateFmt:'yyyy-MM'})">
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
					<input id="zw_f_business_start_time" type="text" value="${e.getPageFieldValue()}" name="zw_f_business_start_time" onclick="WdatePicker({dateFmt:'yyyy-MM'})">
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
					<input id="zw_f_business_end_time" type="text" value="${e.getPageFieldValue()}" name="zw_f_business_end_time" onclick="WdatePicker({dateFmt:'yyyy-MM'})">
				</div>
			</div>
			<script type="text/javascript">
				zapjs.f.require(['lib/datepicker/WdatePicker'],function(a){});
			</script>
		<#elseif e.getPageFieldName() = "zw_f_branch_area_address">
			<div class="control-group">
				<label class="control-label" for="zw_f_branch_area_address"><span class="w_regex_need">*</span>开户支行所在省市：</label>
				<div class="controls">
					<input type="hidden" id="zw_f_branch_area_address" name="zw_f_branch_area_address" zapweb_attr_regex_id="469923180002" value="${e.getPageFieldValue()}">
					<#if e.getPageFieldValue()?? && e.getPageFieldValue()!=''>
						<#assign cityCode = "${e.getPageFieldValue()}"/>
					<#else>
						<#assign cityCode = "110100"/>
					</#if>
					<select name="branch_area_address_provice" id="branch_area_address_provice" onChange="merchantEditDrfat.selectProvince(this)">
						<#list area.getProvince() as provices>
							<option value="${provices.code}">${provices.name}</option>
						</#list>
	      			</select>
	      			<select name="branch_area_address_city" id="branch_area_address_city" onChange="merchantEditDrfat.selectCity(this.value)">
	      				<#list area.getCity('${cityCode}') as city>
							<option value="${city.code}">${city.name}</option>
						</#list>
					</select>
					<select name="branch_area_address_county" id="branch_area_address_county" onChange="merchantEditDrfat.selectCounty()">
	      				<#list area.getCounty('${cityCode}') as city>
							<option value="${city.code}">${city.name}</option>
						</#list>
					</select>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_money_proportion">
			<div class="control-group">
				<label class="control-label" for="zw_f_money_proportion">
				<span class="w_regex_need">*</span>
				质保金比例：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_money_proportion" name="zw_f_money_proportion" value="${e.getPageFieldValue()?eval*100}"/>&nbsp;&nbsp;%
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_business_scope">
			<div class="control-group">
				<label class="control-label" for="zw_f_business_scope">
				<span class="w_regex_need">*</span>
				经营范围：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_business_scope" name="zw_f_business_scope" value="${e.getPageFieldValue()}"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_register_time">
			<div class="control-group">
				<label class="control-label" for="zw_f_register_time">
				<span class="w_regex_need">*</span>
				登记时间：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_register_time" name="zw_f_register_time" value="${e.getPageFieldValue()}" readonly/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_account_clear_type">
			<div class="control-group">
				<label class="control-label" for="zw_f_account_clear_type">
				<span class="w_regex_need">*</span>
				结算周期：
				</label>
				<div class="controls">
					<select name="zw_f_account_clear_type" id="zw_f_account_clear_type">
						<!--${e.getPageFieldValue()}-->
						<#if '${e.getPageFieldValue()}'=='4497478100030003'>
						    <option value="4497478100030003" selected>整月结算</option>
						    <option value="4497478100030004">半月结算</option>
						    <option value="4497478100030005">特殊结算</option>
						    <option value="4497478100030006">自定义</option>
						<#elseif '${e.getPageFieldValue()}'=='4497478100030004'>
							<option value="4497478100030003">整月结算</option>
							<option value="4497478100030004" selected>半月结算</option>
							<option value="4497478100030005">特殊结算</option>
							<option value="4497478100030006">自定义</option>
						<#elseif '${e.getPageFieldValue()}'=='4497478100030005'>
							<option value="4497478100030003">整月结算</option>
							<option value="4497478100030004">半月结算</option>
							<option value="4497478100030005" selected>特殊结算</option>
							<option value="4497478100030006">自定义</option>
						<#elseif '${e.getPageFieldValue()}'=='4497478100030006'>
							<option value="4497478100030003">整月结算</option>
							<option value="4497478100030004">半月结算</option>
							<option value="4497478100030005">特殊结算</option>
							<option value="4497478100030006" selected>自定义</option>
						</#if>
	      			</select>
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
	<#if e_operate.getOperateUid() == '27a983c30d0c11e6a30300505692798f'>
	<#-- 提交审批 -->
		<input type="button"  class="${e_style_css}" onclick="merchantEditDrfat.approveSubmit();" value="${e_operate.getOperateName()}" />
		<input type="button" id="submitApprove" style="display:none" class="${e_style_css}" zapweb_attr_operate_id="311b4b9c0f4911e5a451005056925439"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" />
	<#elseif e_operate.getOperateUid() == 'b889f1f7afd148e89f0dd35326e1376d'>
		<#-- 保存到草稿箱 -->
		<input type="button" class="${e_style_css}" onclick="merchantEditDrfat.saveToDrafts(this)" value="${e_operate.getOperateName()}"/>
		<input type="button" id="saveToDraft" style="display:none" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" />
	<#else>
		<input type="button" class="${e_style_css}" zapweb_attr_operate_id="${e_operate.getOperateUid()}"  onclick="${e_operate.getOperateLink()}"  value="${e_operate.getOperateName()}" />
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
		<#else>
		</#if>
	
	<@m_zapmacro_common_field_end />
</#macro>
