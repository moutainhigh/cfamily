var editCoupon = {
	init : function(){
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',editCoupon.beforeSubmit);
		$("#zw_f_privide_money").attr("readonly", "readonly");//已发放金额设置为不可更改
		$("#zw_f_surplus_money").attr("disabled", true);//剩余金额设置为不可更改
		editCoupon.initProductType();
		
		//已发放金额大于0，优惠券面额、限定金额以及有效期不能编辑
		var privide_money = $("#zw_f_privide_money").val();
		if (privide_money > 0) {
			$("#zw_f_money").attr("disabled", "readonly");
			$("#zw_f_limit_money").attr("readonly", "readonly");
			$("#zw_f_start_time").attr("disabled", true);
			$("#zw_f_end_time").attr("disabled", true);
			
			$("#zw_f_produce_type").attr("disabled", true);		//生成类型不可编辑
		}
		
	},
		beforeSubmit : function() {
			var total_money = $("#zw_f_total_money").val();
			var privide_money = $("#zw_f_privide_money").val();
			//成本限额大于已发放金额
			if (parseInt(total_money) < parseInt(privide_money)) {
				zapjs.f.message("发放总金额不能小于已发放金额");
				return false;
			}
			if($("#zw_f_produce_type").val() == "4497471600040002"){
				if ($('#zw_f_cdkey').val().length>30) {
					zapjs.f.message('优惠码：长度超过限制，最长为30个字符，请重新填写！');
					return false;
				};
			}
		return true;
	},
	initProductType:function(){
		//类型为优惠码时多账户使用，每账户可使用次数，优惠码字段显示
		if($("#zw_f_produce_type").val() == "4497471600040002"){
			$("#zw_f_multi_account").parent().parent().show();
			$("#zw_f_account_useTime").parent().parent().show();
//			$("#zw_f_account_useTime").val(1);
			$("#zw_f_cdkey").parent().parent().show();
			
			$("#zw_f_cdkey").attr("zapweb_attr_regex_id","469923180002");
		}else{
			$("#zw_f_multi_account").parent().parent().hide();
			$("#zw_f_account_useTime").parent().parent().hide();
			$("#zw_f_cdkey").parent().parent().hide();
			$("#zw_f_cdkey").removeAttr("zapweb_attr_regex_id");
//			$("#zw_f_account_useTime").val(1);
			$("#zw_f_cdkey").val();
		}
	}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/editCoupon", function() {
		return editCoupon;
	});
}

$(document).ready(function(){
	//生成类型
	$("#zw_f_produce_type").change(function(){
		editCoupon.initProductType();
	});
});