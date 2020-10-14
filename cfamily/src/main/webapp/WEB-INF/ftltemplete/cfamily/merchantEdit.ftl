<#assign area=b_method.upClass("com.cmall.systemcenter.service.ChinaAreaService")>
<#assign business_license_type=b_method.upFiledByFieldName(b_page.upBookData(),"business_license_type").getPageFieldValue() />
<@m_common_html_css ["cfamily/js/select2/select2.css"] />
<script>
require(['cfamily/js/merchantEdit','cfamily/js/select2/select2'],
function()
{
	zapjs.f.ready(function()
		{
			merchantEdit.init('${business_license_type}');
		}
	);
}

);
</script>


<@m_zapmacro_common_page_edit b_page />


<#-- 编辑页 -->
<#macro m_zapmacro_common_page_edit e_page>
<form class="form-horizontal" method="POST" >
	<@m_zapmacro_common_auto_list  e_page.upEditData()   e_page  />
	<@m_zapmacro_common_auto_operate   e_page.getWebPage().getPageOperate()  "116001016" />
</form>
</#macro>

<#-- 页面字段的自动输出判断 -->
<#macro m_zapmacro_common_auto_list e_pagedata   e_page>
	<#assign sellerInfoService=b_method.upClass("com.cmall.usercenter.service.SellerInfoService")>
	<#if e_pagedata??>
	<#list e_pagedata as e>
		<#if e.getPageFieldName() = "zw_f_small_seller_code">
			<div class="control-group">
				<label class="control-label" for="">卖家编号：</label>
				<div class="controls">
					<input type="text" id="zw_f_seller_code" name="zw_f_seller_code" readOnly="true" value="${sellerInfoService.getSellerCode(e.getPageFieldValue())}" ><br/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="">公司编号：</label>
				<div class="controls">
					<input type="text" id="zw_f_small_seller_code" name="zw_f_small_seller_code" readOnly="true" value="${e.getPageFieldValue()}"><br/>
				</div>
			</div>
		<#elseif e.getPageFieldName()=="zw_f_uc_seller_type">
			<input type="hidden" id="zw_f_uc_seller_type" name="zw_f_uc_seller_type" value="${e.getPageFieldValue()}"/>
			<div class="control-group">
				<label class="control-label" for="zw_f_uc_seller_type_h"><span class="w_regex_need">*</span>商户类型：</label>
				<div class="controls">
		      		<select name="zw_f_uc_seller_type_h" id="zw_f_uc_seller_type_h" disabled="disabled">
		      			<#if '${e.getPageFieldValue()}'=='4497478100050001'>
		      				<option value="4497478100050001" selected="selected">普通商户</option>
							<option value="4497478100050002">跨境商户</option>
							<option value="4497478100050003">跨境直邮</option>
							<option value="4497478100050004">平台入驻</option>
							<option value="4497478100050005">缤纷商户</option>
						<#elseif '${e.getPageFieldValue()}'=='4497478100050002'>
		      				<option value="4497478100050001">普通商户</option>
							<option value="4497478100050002" selected="selected">跨境商户</option>
							<option value="4497478100050003">跨境直邮</option>
							<option value="4497478100050004">平台入驻</option>
							<option value="4497478100050005">缤纷商户</option>
						<#elseif '${e.getPageFieldValue()}'=='4497478100050003'>
		      				<option value="4497478100050001">普通商户</option>
							<option value="4497478100050002">跨境商户</option>
							<option value="4497478100050003" selected="selected">跨境直邮</option>
							<option value="4497478100050004">平台入驻</option>
							<option value="4497478100050005">缤纷商户</option>
						<#elseif '${e.getPageFieldValue()}'=='4497478100050004'>
		      				<option value="4497478100050001">普通商户</option>
							<option value="4497478100050002">跨境商户</option>
							<option value="4497478100050003">跨境直邮</option>
							<option value="4497478100050004" selected="selected">平台入驻</option>
							<option value="4497478100050005">缤纷商户</option>
						<#elseif '${e.getPageFieldValue()}'=='4497478100050005'>
		      				<option value="4497478100050001">普通商户</option>
							<option value="4497478100050002">跨境商户</option>
							<option value="4497478100050003">跨境直邮</option>
							<option value="4497478100050004">平台入驻</option>
							<option value="4497478100050005" selected="selected">缤纷商户</option>
						</#if>
		      		</select>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_seller_company_name">
			<div class="control-group">
				<label class="control-label" for="zw_f_seller_company_name">
				<span class="w_regex_need">*</span>
				公司名称：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_seller_company_name" name="zw_f_seller_company_name" value="${e.getPageFieldValue()}" readOnly="true"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_seller_short_name">
			<div class="control-group">
				<label class="control-label" for="zw_f_seller_short_name">
				<span class="w_regex_need">*</span>
				公司简称：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_seller_short_name" name="zw_f_seller_short_name" value="${e.getPageFieldValue()}" readOnly="true"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_establishment_date">
			<div class="control-group">
				<label class="control-label" for="zw_f_establishment_date">
				<span class="w_regex_need">*</span>
				成立日期：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_establishment_date" name="zw_f_establishment_date" value="${e.getPageFieldValue()}" readOnly="true"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_register_money">
			<div class="control-group">
				<label class="control-label" for="zw_f_register_money">
				<span class="w_regex_need">*</span>
				注册资本：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_register_money" name="zw_f_register_money" value="${e.getPageFieldValue()}"/>&nbsp;&nbsp;万元	
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_register_money_type">
			<div class="control-group">
				<label class="control-label" for="zw_f_register_money_type">
				<span class="w_regex_need">*</span>
				注册资本币种类型：
				</label>
				<div class="controls">
		      		<select name="zw_f_register_money_type" id="zw_f_register_money_type">
		      			<#if '${e.getPageFieldValue()}'=='449747850002'>
		      				<option value="449747850001">人民币</option>
							<option value="449747850002" selected="selected">美元</option>
						<#else>
		      				<option value="449747850001" selected="selected">人民币</option>
							<option value="449747850002">美元</option>
						</#if>
		      		</select>	
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_business_start_time">
			<div class="control-group">
				<label class="control-label" for="zw_f_business_start_time">
				营业期开始：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_business_start_time" name="zw_f_business_start_time" value="${e.getPageFieldValue()}" />
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_business_end_time">
			<div class="control-group">
				<label class="control-label" for="zw_f_business_end_time">
				营业期结束：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_business_end_time" name="zw_f_business_end_time" value="${e.getPageFieldValue()}" />
				</div>
			</div>
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
					<select name="branch_area_address_provice" id="branch_area_address_provice">
						<#list area.getProvice('${cityCode}') as provices>
							<option value="${provices.code}">${provices.name}</option>
						</#list>
	      			</select>
	      			<select name="branch_area_address_city" id="branch_area_address_city">
	      				<#list area.getCity('${cityCode}') as city>
							<option value="${city.code}">${city.name}</option>
						</#list>
					</select>
					<select name="branch_area_address_county" id="branch_area_address_county">
	      				<#list area.getCounty('${cityCode}') as city>
							<option value="${city.code}">${city.name}</option>
						</#list>
					</select>
				</div>
		</div>
		
			<!-- 
				<   # elseif e.getPageFieldName() = "zw_f_upload_business_license">
				此处清除ftl格式，否则不显示。
			 	上传营业执照 根据新需求，此处打开，允许用户编辑。
			 	以前是写死的，具体请看上一版本的代码 - 2016-09-28  -  Yangcl
			 -->
			
			
		<#elseif e.getPageFieldName()=="zw_f_business_license_type">
			<input type="hidden" id="zw_f_business_license_type" name="zw_f_business_license_type" value="${e.getPageFieldValue()}"/>
			<div class="control-group">
				<label class="control-label" for="zw_f_business_license_type_h"><span class="w_regex_need">*</span>营业执照类型：</label>
				<div class="controls">
		      		<select name="zw_f_business_license_type_h" id="zw_f_business_license_type_h" onchange="merchantEdit.selectBusinessLicense()">
		      			<#if '${e.getPageFieldValue()}'=='4497478100060001'>
		      				<option value="4497478100060001" selected="selected">普通营业执照</option>
							<option value="4497478100060002">三证合一营业执照</option>
						<#elseif '${e.getPageFieldValue()}'=='4497478100060002'>
							<option value="4497478100060001">普通营业执照</option>
							<option value="4497478100060002" selected="selected">三证合一营业执照</option>
						</#if>
		      		</select>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_registration_number">
			<div class="control-group">
				<label class="control-label" for="zw_f_registration_number">
				<span class="w_regex_need">*</span>
				注册号(营业执照号)：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_registration_number" name="zw_f_registration_number" value="${e.getPageFieldValue()}" />
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_business_scope">
			<div class="control-group">
				<label class="control-label" for="zw_f_business_scope">
				<span class="w_regex_need">*</span>
				经营范围：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_business_scope" name="zw_f_business_scope" value="${e.getPageFieldValue()}" />
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_register_address">
			<div class="control-group">
				<label class="control-label" for="zw_f_register_address">
				<span class="w_regex_need">*</span>
				注册所在地：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_register_address" name="zw_f_register_address" value="${e.getPageFieldValue()}" />
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_legal_person">
			<div class="control-group">
				<label class="control-label" for="zw_f_legal_person">
				<span class="w_regex_need">*</span>
				法人代表姓名：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_legal_person" name="zw_f_legal_person" value="${e.getPageFieldValue()}"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_organization_number">
			<div class="control-group">
				<label class="control-label" for="zw_f_legal_person">
				<span class="w_regex_need"></span>
				组织机构代码：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_organization_number" name="zw_f_organization_number" value="${e.getPageFieldValue()}"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_organization_number_validity_period">
			<div class="control-group">
				<label class="control-label" for="zw_f_organization_number_validity_period">
				<span class="w_regex_need"></span>
				组织机构代码有效期开始：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_organization_number_validity_period" name="zw_f_organization_number_validity_period" value="${e.getPageFieldValue()}" />
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_organization_number_validity_period_end">
			<div class="control-group">
				<label class="control-label" for="zw_f_organization_number_validity_period_end">
				<span class="w_regex_need"></span>
				组织机构代码有效期结束：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_organization_number_validity_period_end" name="zw_f_organization_number_validity_period_end" value="${e.getPageFieldValue()}" />
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_tax_identification_number">
			<div class="control-group">
				<label class="control-label" for="zw_f_tax_identification_number">
				<span class="w_regex_need">*</span>
				纳税人识别号：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_tax_identification_number" name="zw_f_tax_identification_number" value="${e.getPageFieldValue()}"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_account_line">
			<div class="control-group">
				<label class="control-label" for="zw_f_account_line">
				<span class="w_regex_need">*</span>
				开户名：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_account_line" name="zw_f_account_line" value="${e.getPageFieldValue()}"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_bank_account">
			<div class="control-group">
				<label class="control-label" for="zw_f_bank_account">
				<span class="w_regex_need">*</span>
				银行账号：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_bank_account" name="zw_f_bank_account" value="${e.getPageFieldValue()}"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_branch_name">
			<div class="control-group">
				<label class="control-label" for="zw_f_branch_name">
				<span class="w_regex_need">*</span>
				开户行支行名称：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_branch_name" name="zw_f_branch_name" value="${e.getPageFieldValue()}"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_branch_address">
			<div class="control-group">
				<label class="control-label" for="zw_f_branch_address">
				<span class="w_regex_need">*</span>
				开户行支行所在地：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_branch_address" name="zw_f_branch_address" value="${e.getPageFieldValue()}"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_joint_number">
			<div class="control-group">
				<label class="control-label" for="zw_f_joint_number">
				<span class="w_regex_need">*</span>
				开户行支行联行号：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_joint_number" name="zw_f_joint_number" value="${e.getPageFieldValue()}"/>
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_quality_retention_money">
			<div class="control-group">
				<label class="control-label" for="zw_f_quality_retention_money">
				质保金：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_quality_retention_money" name="zw_f_quality_retention_money" value="${e.getPageFieldValue()}" readOnly="true"/>&nbsp;&nbsp;元
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_money_proportion">
			<div class="control-group">
				<label class="control-label" for="zw_f_money_proportion">
				<span class="w_regex_need">*</span>
				质保金比例：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_money_proportion" name="zw_f_money_proportion" value="${e.getPageFieldValue()?eval*100}" readOnly="true">&nbsp;&nbsp;%
				</div>
			</div>
		<#elseif e.getPageFieldName() = "zw_f_register_name">
			<div class="control-group">
				<label class="control-label" for="zw_f_register_name">
				登记人：
				</label>
				<div class="controls">
					<input type="text" id="zw_f_register_name" name="zw_f_register_name" value="${e.getPageFieldValue()}"/>
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
		<#elseif e.getPageFieldName() = "zw_f_account_clear_type">
			<div class="control-group">
				<label class="control-label" for="zw_f_account_clear_type">
				<span class="w_regex_need">*</span>
				结算周期：
				</label>
				<div class="controls">
					<#if '${e.getPageFieldValue()}'!=''>
						<select name="zw_f_account_clear_type" id="zw_f_account_clear_type" disabled="disabled">
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
					<#else>
			      		<select name="zw_f_account_clear_type" id="zw_f_account_clear_type" disabled="disabled">
			      			<option value="4497478100030003" selected>整月结算</option>
							<option value="4497478100030004">半月结算</option>
							<option value="4497478100030004">特殊结算</option>
							<option value="4497478100030006">自定义</option>
						</select>
					</#if>
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

<#-- 字段：输入框 -->
<#macro m_zapmacro_common_field_text e_field>
	<@m_zapmacro_common_field_start text=e_field.getFieldNote() for=e_field.getPageFieldName() />
		<input type="text" id="${e_field.getPageFieldName()}" name="${e_field.getPageFieldName()}" ${e_field.getFieldExtend()} value="${e_field.getPageFieldValue()}">
		<#if e_field.getPageFieldName() == 'zw_f_register_money'>
			<#-- &nbsp;&nbsp;万元  -->	
		<#elseif e_field.getPageFieldName() == 'zw_f_quality_retention_money'>
			<#-- &nbsp;&nbsp;元 -->	
		<#elseif e_field.getPageFieldName() == 'zw_f_bill_day'>
			&nbsp;&nbsp;天
		<#else>
		</#if>
	
	<@m_zapmacro_common_field_end />
</#macro>
