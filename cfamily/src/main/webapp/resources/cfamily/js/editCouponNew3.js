var editCouponNew3 = {
	init : function(){
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCouponNew3.beforeSubmit);
		$("#zw_f_privide_money").attr("readonly", "readonly");//已发放金额设置为不可更改
		$("#zw_f_surplus_money").attr("readonly", "readonly");//剩余金额设置为不可更改
		
		$("#zw_f_start_time").parent().parent().find("label").html('<span class="w_regex_need">*</span>开始时间：');
		$("#zw_f_end_time").parent().parent().find("label").html('<span class="w_regex_need">*</span>结束时间：');
		$("#zw_f_valid_day").parent().parent().find("label").html('<span class="w_regex_need">*</span>有效天数：');
		$("#zw_f_exchange_value").parent().append('<span class="w_regex_need"></span>');
		$("[for='zw_f_exchange_value']").prepend('<span class="w_regex_need">*</span>"')
		
		editCouponNew3.initValidType();
		editCouponNew3.initSkpType();
		editCouponNew3.initCouponType();
		editCouponNew3.initExchangeType();
		
		// 只支持系统发放的优惠券显示兑换类型
		if(activity.provide_type != '4497471600060002') {
			$("#zw_f_exchange_type").parent().parent().hide();
			$("#zw_f_exchange_value").parent().parent().hide();
		}
		
		if($("#zw_f_money_type").val() == "449748120001"){
			$("#zw_f_discount").val(0);
		} else {
			$("#zw_f_money").val(0);
		}
		//已发放金额大于0，优惠券类型，优惠券面额，优惠券折扣不能编辑
		//已发放金额大于0，优惠券面额、限定金额以及有效期不能编辑
		//已发放金额大于0，“限制条件”字段，不可修改		2016-01-21 14点增加此逻辑
		var privide_money = $("#zw_f_privide_money").val();
		
		if (privide_money > 0) {
			$("#zw_f_money_type").attr("disabled", "disabled");
			// 动态插入隐藏字段，解决zw_f_money_type被禁用后提交参数到后台获取不到zw_f_money_type值的 问题
			$("#zw_f_money_type").parent().append('<input type="hidden" name="zw_f_money_type" value="'+$("#zw_f_money_type").val()+'">');
			
			$("#zw_f_money").attr("readonly", "readonly");
			$("#zw_f_discount").attr("readonly", "readonly");
			$("#zw_f_limit_money").attr("readonly", "readonly");
			$("#zw_f_start_time").attr("readonly", "readonly");
			$("#zw_f_end_time").attr("readonly", "readonly");
			$("#zw_f_valid_type").attr("readonly", "readonly");
			$("#zw_f_valid_day").attr("readonly", "readonly");
			
			$("#zw_f_limit_condition").attr("disabled", "disabled");			//禁用限制条件下拉框
			$("#limit_condition_input").removeAttr("disabled");
		}
	},
		beforeSubmit : function() {
			//var total_money = $("#zw_f_total_money").val();
			//var privide_money = $("#zw_f_privide_money").val();
			//成本限额大于已发放金额
			//if (parseInt(total_money) < parseInt(privide_money)) {
			//	zapjs.f.message("发放总金额不能小于已发放金额");
			//	return false;
			//}
			// 积分兑换填写的积分必须是整数
			if($("#zw_f_exchange_type").val() == "4497471600390002"){
				var reg = /^[1-9]\d*$/;
				if(!reg.test($('#zw_f_exchange_value').val())){
					zapjs.f.message('积分只支持整数，请重新填写！');
					return false;
				}
			}			
			if($("#zw_f_exchange_value").val() == "449748120002"){
				var reg = /^\d{2}$/;
				if(!reg.test($('#zw_f_exchange_value').val())){
					zapjs.f.message('优惠券折扣：10到99之间的数字，请重新填写！');
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
	},
	initValidType:function(){
		//有效类型为天数时，有效天数字段显示，开始结束时间隐藏
		if($("#zw_f_valid_type").val() == "4497471600080001"){
			$("#zw_f_valid_day").parent().parent().show();
			$("#zw_f_start_time").parent().parent().hide();
			$("#zw_f_end_time").parent().parent().hide();
			
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
			//已发放金额显示为XX元			
			//$("#zw_f_privide_money").parent().children("span:first").html("<span>元<span>");
			//剩余金额显示原值
			//var surplusmoney = parseInt($("#zw_f_surplus_money").val()) / parseInt($("#zw_f_money").val());
			
			if(parseInt($("#zw_f_money").val()) == 0) {
				$("#zw_f_surplus_money").parent().parent().hide();
				$("#zw_f_privide_money").parent().parent().hide();
			} else {
				$("#zw_f_surplus_money").val(Math.round(parseInt($("#zw_f_surplus_money").val()) / parseInt($("#zw_f_money").val())));
				$("#zw_f_privide_money").val(Math.round(parseInt($("#zw_f_privide_money").val()) / parseInt($("#zw_f_money").val())));
			}
		}else{
			$("#zw_f_money").parent().parent().hide();				
			$("#zw_f_discount").parent().parent().show();
			$("#zw_f_money").removeAttr("zapweb_attr_regex_id");
			$("#zw_f_discount").attr("zapweb_attr_regex_id","469923180003");			
			//已发放金额显示为XX张
			//$("#zw_f_privide_money").parent().children("span:first").html("<span>张<span>");
			//剩余金额显示为空
			//$("#zw_f_surplus_money").val(null);
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
	}
};
$(document).ready(function(){
	//有效类型
	$("#zw_f_valid_type").change(function(){
		editCouponNew3.initValidType();
	});
	//跳转类型
	$("#zw_f_action_type").change(function(){
		editCouponNew3.initSkpType();
	});
	//优惠券类型
	$("#zw_f_money_type").change(function(){
		editCouponNew3.initCouponType();
	});
	//兑换类型
	$("#zw_f_exchange_type").change(function(){
		editCouponNew3.initExchangeType();
	});	
});
if (typeof define === "function" && define.amd) {
	define("cfamily/js/editCouponNew3", function() {
		return editCouponNew3;
	});
}