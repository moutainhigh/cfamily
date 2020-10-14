<#assign area=b_method.upClass("com.cmall.systemcenter.service.ChinaAreaService")>
<#assign account_clear_type=b_method.upFiledByFieldName(b_page.upBookData(),"account_clear_type").getPageFieldValue() />
<@m_zapmacro_common_page_book b_page />
<#assign business_license_type=b_method.upFiledByFieldName(b_page.upBookData(),"business_license_type").getPageFieldValue() />
<script type="text/javascript">
	$(function() {
		var value = '${business_license_type}';
		if(value==="4497478100060002") {
			$("#organization_code_event,#tax_registration_certificate_copy_event").hide();
			$("#organization_code_event,#tax_registration_certificate_copy_event").nextUntil(".zab_info_page_title").not(".control-group:eq(5)").hide();/**.control-group:eq(5)标示一般纳税人资格证电子版*/
		} else {
			$("#organization_code_event,#tax_registration_certificate_copy_event").show();
			$("#organization_code_event,#tax_registration_certificate_copy_event").nextUntil(".zab_info_page_title").show();
		}
		$("#zw_f_branch_area_address").val(function() {
			if($(this).val()=="") {
				return $("#branch_area_address_county").val();
			}
			var code = $(this).val();
			var _code = code.substring(0,3)+"000";
			$("#branch_area_address_provice").find("option[value='"+_code+"']").attr("selected",true);
			_code = code.substring(0,4)+"00";
			$("#branch_area_address_city").find("option[value='"+_code+"']").attr("selected",true);
			$("#branch_area_address_county").find("option[value='"+code+"']").attr("selected",true);
			return $(this).val();
		});
	})
</script>

<#-- 查看页 -->
<#macro m_zapmacro_common_page_book e_page>
<form class="form-horizontal" method="POST" >

	
	<#list e_page.upBookData()  as e>
		
	  	<@m_zapmacro_common_book_field e e_page/>
	  	
	</#list>
	
	
</form>
</#macro>
<#-- 显示页的自动输出判断 -->
<#macro m_zapmacro_common_book_field e_field   e_page>
	
	<@m_zapmacro_common_field_show e_field e_page/>
	<#if e_field.getFieldName() = "business_status">
			<div class="zab_info_page_title" w_clear id="organization_code_event">
				<span>组织机构信息</span>
			</div>
	<#elseif e_field.getFieldName() = "organization_number_validity_period_end">
			<div class="zab_info_page_title" w_clear id="tax_registration_certificate_copy_event">
				<span>税务登记信息</span>
			</div>
	<#elseif e_field.getFieldName() = "tax_identification_number">
			<div class="zab_info_page_title" w_clear>
				<span>开户行信息</span>
			</div>
	<#elseif e_field.getFieldName() = "joint_number">
			<div class="zab_info_page_title" w_clear>
				<span>售后信息</span>
			</div>							
	 </#if>
	 
	  	
</#macro>

<#-- 字段：显示专用 -->
<#macro m_zapmacro_common_field_show e_field e_page>
	<#if e_field.getFieldName() == "bill_point">
		<#if account_clear_type == "4497478100030006">
			<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
				<div class="control_book">
					<#list e_page.upDataSource(e_field) as e_key>
						<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
					</#list>
				</div>
			<@m_zapmacro_common_field_end />
		<#else>
		
		</#if>
	<#elseif e_field.getFieldName() == "bill_day">
		<#if account_clear_type == "4497478100030006">
			<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
				<div class="control_book">
					<#list e_page.upDataSource(e_field) as e_key>
						<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} &nbsp;&nbsp;天</#if>
					</#list>
				</div>
			<@m_zapmacro_common_field_end />
		<#else>
		
		</#if>
	<#else>
		<@m_zapmacro_common_field_start text=e_field.getFieldNote()+":" />
			<#if e_field.getFieldName() == "bank_name">
					<#-- 银行名称字段单独处理，解决被冻结的银行类型不显示问题 -->
					<div class="control_book">
						<#assign extMap=b_method.upDataOneOutNull("uc_seller_info_extend","","","","uid", RequestParameters["zw_f_uid"])>
						<#assign bankinfo=b_method.upDataOneOutNull("uc_bankinfo","","","","bank_code", extMap.bank_name?default(""))>
						${bankinfo.bank_name?default("")}		
					</div>
			<#else>
		      		<div class="control_book">
			      		<#if  e_field.getFieldTypeAid()=="104005019">
			      			<#list e_page.upDataSource(e_field) as e_key>
								<#if  e_field.getPageFieldValue()==e_key.getV()> ${e_key.getK()} </#if>
							</#list>
						<#elseif e_field.getFieldTypeAid()=="104005021">
							<#if  e_field.getPageFieldValue()!="">
								<div class="w_left w_w_100">
										<a href="${e_field.getPageFieldValue()}" target="_blank">
										<img src="${e_field.getPageFieldValue()}">
										</a>
								</div> 
							</#if>
			      		<#else>	
			      			<#if e_field.getFieldName()=="register_money">
			      				${e_field.getPageFieldValue()?default("")}&nbsp;&nbsp;万元
			      			<#elseif e_field.getFieldName()=="quality_retention_money">
			      				${e_field.getPageFieldValue()?default("")}&nbsp;&nbsp;元
			      			<#elseif e_field.getFieldName()=="money_proportion">
				      			${e_field.getPageFieldValue()?eval*100}&nbsp;&nbsp;%
							<#elseif e_field.getFieldName() == "branch_area_address">
								<input type="hidden" id="zw_f_branch_area_address" name="zw_f_branch_area_address" value="${e_field.getPageFieldValue()}">
								<#if e_field.getPageFieldValue()?? && e_field.getPageFieldValue()!=''>
									<#assign cityCode = "${e_field.getPageFieldValue()}"/>
								<#else>
									<#assign cityCode = "110100"/>
								</#if>
								<select name="branch_area_address_provice" id="branch_area_address_provice" disabled="disabled">
									<#list area.getProvice('${cityCode}') as provices> 
										<option value="${provices.code}">${provices.name}</option>
									</#list>
				      			</select>
				      			<select name="branch_area_address_city" id="branch_area_address_city" disabled="disabled">
				      				<#list area.getCity('${cityCode}') as city>
										<option value="${city.code}">${city.name}</option>
									</#list>
								</select>
								<select name="branch_area_address_county" id="branch_area_address_county" disabled="disabled">
				      				<#list area.getCounty('${cityCode}') as city>
										<option value="${city.code}">${city.name}</option>
									</#list>
								</select>
							<#else>
								${e_field.getPageFieldValue()?default("")}
							</#if>
						</#if>
					</div>		
			</#if>
		<@m_zapmacro_common_field_end />
	</#if>
	
	<!-- 营业执照类型后面追加显示配送仓库 -->
	<#if e_field.getFieldName() == "business_license_type">
		<#assign extMap=b_method.upDataOneOutNull("uc_seller_info_extend","","","","uid", RequestParameters["zw_f_uid"])>
		<#assign deliveryStoreType=b_method.upDataOneOutNull("sc_define","","","","define_code", extMap["delivery_store_type"],"parent_code","449747160043")>
		<@m_zapmacro_common_field_start text="配送仓库:" />
			<div class="control_book">
				${deliveryStoreType.define_name?default("")}		
			</div>
		<@m_zapmacro_common_field_end />
	</#if>
</#macro>

