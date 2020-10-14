var merchantEdit = {

		init:function(value){
			// 自定义结算周期
			$('#zw_f_bill_point').attr({disabled:'true'});
			$('#zw_f_bill_day').attr({disabled:'true'});
			if($("#zw_f_account_clear_type").val() == "4497478100030006"){
				// 自定义
				$('label[for="zw_f_bill_point"]').html('<span class="w_regex_need">*</span> 账单节点：');
				$('label[for="zw_f_bill_day"]').html('<span class="w_regex_need">*</span> 结算天数：');
				$("#zw_f_bill_point").parents('.control-group').show();
				$("#zw_f_bill_day").parents('.control-group').show();
			}else{
				$("#zw_f_bill_point").parents('.control-group').hide();
				$("#zw_f_bill_day").parents('.control-group').hide();
			}
			
			
			$("#zw_f_bank_name").select2(); 
			$("#s2id_zw_f_bank_name").attr("style","width:220px");
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',merchantEdit.beforeSubmit);
			$('#zw_f_register_time').attr({disabled:'true'});
			merchantEdit.redStarAdd();
			if(value==="4497478100060002") {
				$("#organization_code_event,#tax_registration_certificate_copy_event").hide();
				$("#organization_code_event,#tax_registration_certificate_copy_event").nextUntil(".zab_info_page_title").not("#zw_f_taxpayer_certificate_pic_event").hide();
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
				// $("#branch_area_address_provice").attr("disabled", "disabled").find("option[value='"+_code+"']").attr("selected",true);
				$("#branch_area_address_provice").find("option[value='"+_code+"']").attr("selected",true);
				_code = code.substring(0,4)+"00";
				// $("#branch_area_address_city").attr("disabled", "disabled").find("option[value='"+_code+"']").attr("selected",true);
				// $("#branch_area_address_county").attr("disabled", "disabled").find("option[value='"+code+"']").attr("selected",true);
				
				$("#branch_area_address_city").find("option[value='"+_code+"']").attr("selected",true);
				$("#branch_area_address_county").find("option[value='"+code+"']").attr("selected",true);
				
				return $(this).val();
			});
			//$("#zw_f_seller_short_name").after('<span class="w_regex_need">&nbsp;&nbsp;&nbsp;&nbsp;(商户简称请慎重修改！)</span>');
		},
		
		beforeSubmit : function() {
			if($("#zw_f_business_person").val().length > 50){
				zapjs.f.message('业务负责人不能超过50个字符');
				return false;
			}
			if($("#zw_f_business_person_phone").val().length > 50){
				zapjs.f.message('业务负责人联系电话不能超过50个字符');
				return false;
			}
			if($("#zw_f_contract_return_address").val().length > 100){
				zapjs.f.message('合同寄回地址不能超过100个字符');
				return false;
			}
			if($("#zw_f_after_sale_person").val().length > 50){
				zapjs.f.message('售后负责人名称不能超过50个字符');
				return false;
			}
			if($("#zw_f_after_sale_phone").val().length > 50){
				zapjs.f.message('售后负责人联系电话不能超过50个字符');
				return false;
			}
			if($("#zw_f_invoice_return_address").val().length > 100){
				zapjs.f.message('发票寄回地址不能超过100个字符');
				return false;
			}
			if($("#zw_f_seller_company_name").val().length > 50){
				zapjs.f.message('公司名称不能超过50个字符');
				return false;
			}
			if($("#zw_f_seller_short_name").val().length > 20){
				zapjs.f.message('公司简称不能超过20个字符');
				return false;
			}
			if($("#zw_f_register_address").val().length > 100){
				zapjs.f.message('注册所在地不能超过100个字符');
				return false;
			}
			if($("#zw_f_company_datail_address").val().length > 100){
				zapjs.f.message('详细地址不能超过100个字符');
				return false;
			}
			var reg = new RegExp("^[0-9]*$");
			var registerMoney = $("#zw_f_register_money").val();
			var regex = new RegExp(/^0$|^[1-9]\d{0,15}$|^[1-9]\d{0,15}\.{1}\d{1,4}$|^0\.{1}\d{1,4}$/g);
			if(!regex.test(registerMoney)){
				zapjs.f.message('注册资本不能超过4位小数');
				return false;
			}
			if(registerMoney.length > 50){
				zapjs.f.message('注册资本不能超过50个字符');
				return false;
			}
			if($("#zw_f_business_scope").val().length > 100){
				zapjs.f.message('经营范围不能超过100个字符');
				return false;
			}
			if($("#zw_f_registration_number").val().length > 100){
				zapjs.f.message('注册号不能超过100个字符');
				return false;
			}
			var zw_f_organization_code = $("#zw_f_organization_code");
			if(zw_f_organization_code.attr("zapweb_attr_regex_id").length!=0 && $.trim(zw_f_organization_code.val()).length==0){
				zapjs.f.message('组织机构代码影印件必须上传');
				return false;
			}
			var zw_f_organization_number = $("#zw_f_organization_number");
			if(!zw_f_organization_number.is(":hidden") && zw_f_organization_number.val().length == 0){
				if(zw_f_organization_number.val().length == 0) {
					zapjs.f.message('组织机构代码不能为空');
					return false;
				} else if(zw_f_organization_number.val().length > 100) {
					zapjs.f.message('组织机构代码不能超过100个字符');
					return false;
				}
			}
			var zw_f_organization_number_validity_period = $("#zw_f_organization_number_validity_period");
			if(!zw_f_organization_number_validity_period.is(":hidden") && $.trim(zw_f_organization_number_validity_period.val()).length==0){
				zapjs.f.message('组织机构代码有效期开始不能为空');
				return false;
			}
			var zw_f_organization_number_validity_period_end = $("#zw_f_organization_number_validity_period_end");
			if(!zw_f_organization_number_validity_period_end.is(":hidden") && $.trim(zw_f_organization_number_validity_period_end.val()).length==0){
				zapjs.f.message('组织机构代码有效期结束不能为空');
				return false;
			}
			var zw_f_tax_registration_certificate_copy = $("#zw_f_tax_registration_certificate_copy");
			if(zw_f_tax_registration_certificate_copy.attr("zapweb_attr_regex_id").length!=0 && $.trim(zw_f_tax_registration_certificate_copy.val()).length==0) {
				zapjs.f.message('税务登记证副本必须上传');
				return false;
			}
			var zw_f_taxpayer_certificate_pic = $("#zw_f_taxpayer_certificate_pic");
			if(zw_f_taxpayer_certificate_pic.attr("zapweb_attr_regex_id").length!=0 && $.trim(zw_f_taxpayer_certificate_pic.val()).length==0){
				zapjs.f.message('一般纳税人资格证电子版必须上传');
				return false;
			}
			var zw_f_tax_identification_number = $("#zw_f_tax_identification_number");
			if(!zw_f_tax_identification_number.is(":hidden")) {
				if(zw_f_tax_identification_number.val().length == 0) {
					zapjs.f.message('纳税人识别号不能为空');
					return false;
				} else if(zw_f_tax_identification_number.val().length > 50) {
					zapjs.f.message('纳税人识别号不能超过50个字符');
					return false;
				}
			}
			var zw_f_open_bank_photocopy_certificate = $("#zw_f_open_bank_photocopy_certificate");
			if(zw_f_open_bank_photocopy_certificate.val().length==0) {
				zapjs.f.message('请上传开户行影印证件');
				return false;
			}
			if($("#zw_f_account_line").val().length > 50){
				zapjs.f.message('开户名不能超过50个字符');
				return false;
			}
			var bankAccount = $("#zw_f_bank_account").val();
			if(bankAccount.length > 50){
				zapjs.f.message('银行账号不能超过50个字符');
				return false;
			}
			if(!reg.test(bankAccount)){
				zapjs.f.message('银行账号必须为数字!');
				return false;
		    }
			if($("#zw_f_branch_name").val().length > 100){
				zapjs.f.message('开户行支行名称不能超过100个字符');
				return false;
			}
			var jointNumber = $("#zw_f_joint_number").val();
			if(jointNumber.length > 50){
				zapjs.f.message('开户行支行联行号不能超过50个字符');
				return false;
			}
			if(!reg.test(jointNumber)){
				zapjs.f.message('开户行支行联行号必须为数字!');
				return false;
		    }
			var prg = new RegExp("^\\d+(\\.\\d+)?$");//小数
			var qualityMoney = $("#zw_f_quality_retention_money").val();
			if(!reg.test(qualityMoney) && !prg.test(qualityMoney)){
				zapjs.f.message('质保金必须为数字或小数!');
				return false;
		    }
			var mp = new RegExp("^(0|[1-9][0-9]?|100)$");//0-100的整数，包含0、100
			var moneyProportion = $("#zw_f_money_proportion").val();
			if(!mp.test(moneyProportion)){
				zapjs.f.message('质保金比例必须为0-100的整数，包含0、100!');
				return false;
			}
			
			var d = new Date(),nowDate = '';
			nowDate += d.getFullYear()+'-';
			var month = d.getMonth() + 1;
			if(month < 10){
				month = "0"+month;
			}
			nowDate  += month;
			var currentTime = new Date(nowDate).getTime();
			var start = new Date($("#zw_f_business_start_time").val()).getTime();
			var end = new Date($("#zw_f_business_end_time").val()).getTime();
			if(end < currentTime){
				zapjs.f.message('结束时间不能小于当前时间');
				return false;
			}
			if(end < start){
				zapjs.f.message('结束时间不能小于开始时间');
				return false;
			}
			return true;
		},
		saveToDrafts: function(obj) {
			$("form input").each(function(){
				$(this).removeAttr("zapweb_attr_regex_id");
			});
			$("#zw_f_json").val($(obj).parents("form").serialize());
			$("#draftBoxBtnSubmit").click();
		},
		redStarAdd: function() {
			var object = $("#organization_code_event,#tax_registration_certificate_copy_event").nextUntil(".zab_info_page_title");
			object.find("label.control-label").each(function() {
				$(this).html('<span class="w_regex_need">*</span>'+$(this).text());
			});
			if($("#zw_f_business_license_type").val()=='4497478100060001') {
				object.find('input').each(function() {
					$(this).attr('zapweb_attr_regex_id', '469923180002');
				});
			} else {
				object.find('input').each(function() {
					$(this).attr('zapweb_attr_regex_id', '');
				});
			}
			$("#zw_f_taxpayer_certificate_pic").attr('zapweb_attr_regex_id', '469923180002').parents(".control-group").attr("id", "zw_f_taxpayer_certificate_pic_event");
		},
		selectBusinessLicense:function() {
			var value = $("#zw_f_business_license_type_h").val();
			var zapweb_attr_regex_id = '469923180002';
			if(value==="4497478100060002") {
				$("#organization_code_event,#tax_registration_certificate_copy_event").hide();
				$("#organization_code_event,#tax_registration_certificate_copy_event").nextUntil(".zab_info_page_title").not("#zw_f_taxpayer_certificate_pic_event").hide().find("input[name^='zw_f_']").each(function() {
					$(this).attr('zapweb_attr_regex_id', '')
				});
			} else {
				$("#organization_code_event,#tax_registration_certificate_copy_event").show();
				$("#organization_code_event,#tax_registration_certificate_copy_event").nextUntil(".zab_info_page_title").show().find("input[name^='zw_f_']").each(function() {
					$(this).attr('zapweb_attr_regex_id', zapweb_attr_regex_id)
				});
			}
		}

};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/merchantEdit",function() {
		return merchantEdit;
	});
}









