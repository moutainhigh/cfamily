var addCouponNew3 = {
		init:function(){
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',addCouponNew3.beforeSubmit);
			$("#zw_f_start_time").parent().parent().find("label").html('<span class="w_regex_need">*</span>开始时间：');
			$("#zw_f_end_time").parent().parent().find("label").html('<span class="w_regex_need">*</span>结束时间：');
			$("#zw_f_valid_day").parent().parent().find("label").html('<span class="w_regex_need">*</span>有效天数：');
			$('#zw_f_limit_condition').val('4497471600070002');
			$("#zw_f_exchange_value").parent().append('<span class="w_regex_need"></span>');
			$("[for='zw_f_exchange_value']").prepend('<span class="w_regex_need">*</span>"')
			addCouponNew3.initValidType();
			addCouponNew3.initSkpType();
			addCouponNew3.initCouponType();
			addCouponNew3.initExchangeType();
			
			// 只支持系统发放的优惠券显示兑换类型
			if(activity.provide_type != '4497471600060002') {
				$("#zw_f_exchange_type").parent().parent().hide();
				$("#zw_f_exchange_value").parent().parent().hide();
			}
		},
		initValidType : function(){
			//有效类型为天数时，有效天数字段显示，开始结束时间隐藏
			if($("#zw_f_valid_type").val() == "4497471600080001"){
				$("#zw_f_valid_day").parent().parent().show();
				$("#zw_f_start_time").parent().parent().hide();
				$("#zw_f_end_time").parent().parent().hide();
				
				$("#zw_f_valid_day").val(0);
				$("#zw_f_valid_day").attr("zapweb_attr_regex_id","469923180003");
				$("#zw_f_start_time").removeAttr("zapweb_attr_regex_id");
				$("#zw_f_end_time").removeAttr("zapweb_attr_regex_id");
				
			}else{
				$("#zw_f_valid_day").parent().parent().hide();
				$("#zw_f_start_time").parent().parent().show();
				$("#zw_f_end_time").parent().parent().show();
				
				$("#zw_f_valid_day").removeAttr("zapweb_attr_regex_id");
				$("#zw_f_start_time").attr("zapweb_attr_regex_id","469923180002");
				$("#zw_f_end_time").attr("zapweb_attr_regex_id","469923180002");
			}
		},
		initSkpType : function(){
			//跳转类型为URL时，跳转链接字段显示，否则隐藏
			if($("#zw_f_action_type").val() == "4497471600280002"){
				$("#zw_f_action_value").parent().parent().show();
				$("#zw_f_action_value").attr("zapweb_attr_regex_id","469923180002");
			}else{
				$("#zw_f_action_value").parent().parent().hide();
				$("#zw_f_action_value").removeAttr("zapweb_attr_regex_id");
			}			
		},
		initCouponType : function(){
			//优惠券类型为金额券时，优惠券面额字段显示，否则隐藏
			if($("#zw_f_money_type").val() == "449748120001" || $("#zw_f_money_type").val() == "449748120003"){
				$("#zw_f_money").parent().parent().show();				
				$("#zw_f_discount").parent().parent().hide();
				$("#zw_f_money").attr("zapweb_attr_regex_id","469923180003");
				$("#zw_f_discount").removeAttr("zapweb_attr_regex_id");
			}else{
				$("#zw_f_money").parent().parent().hide();				
				$("#zw_f_discount").parent().parent().show();
				$("#zw_f_money").removeAttr("zapweb_attr_regex_id");
				$("#zw_f_discount").attr("zapweb_attr_regex_id","469923180003");
			}
			//不显示礼金券
			$("#zw_f_money_type option[value='449748120003']").remove();
		},
		initExchangeType : function(){
			//兑换类型
			if($("#zw_f_exchange_type").val() == '4497471600390001'){
				$("#zw_f_exchange_value").parent().parent().hide();
			}else{
				$("#zw_f_exchange_value").parent().parent().show();
				$("#zw_f_exchange_value").next('.w_regex_need').text('(200积分=1元，只支持整数数字)');
			}
			//不显示礼金券
			$("#zw_f_money_type option[value='449748120003']").remove();
		},		
		beforeSubmit : function() {
			if($("#zw_f_money_type").val() == "449748120002"){
				var reg = /^\d{2}$/;
				if(!reg.test($('#zw_f_discount').val())){
					zapjs.f.message('优惠券折扣：10到99之间的数字，请重新填写！');
					return false;
				}
			}
			// 积分兑换填写的积分必须是整数
			if($("#zw_f_exchange_value").val() == "4497471600390002"){
				var reg = /^[1-9]\d*$/;
				if(!reg.test($('#zw_f_exchange_value').val())){
					zapjs.f.message('积分只支持整数，请重新填写！');
					return false;
				}
			}
			if($("#zw_f_limit_condition").val() == "4497471600070002"){
				if ($('#zw_f_limit_scope').val().length>20) {
					zapjs.f.message('使用范围：长度超过限制，最长为20个字符，请重新填写！');
					return false;
				};
				if ($('#zw_f_limit_explain').val().length>200) {
					zapjs.f.message('使用说明：长度超过限制，最长为200个字符，请重新填写！');
					return false;
				};
			}
			return true;
	}
};

$(document).ready(function(){
	//有效类型
	$("#zw_f_valid_type").change(function(){
		addCouponNew3.initValidType();
	});
	//跳转类型
	$("#zw_f_action_type").change(function(){
		addCouponNew3.initSkpType();
	});
	//优惠券类型
	$("#zw_f_money_type").change(function(){
		addCouponNew3.initCouponType();
	});
	//兑换类型
	$("#zw_f_exchange_type").change(function(){
		addCouponNew3.initExchangeType();
	});
});
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addCouponNew3", function() {
		return addCouponNew3;
	});
}
