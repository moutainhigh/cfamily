var addCoupon = {
		init:function(){
			zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit',addCoupon.beforeSubmit);
			addCoupon.initProductType();
		},
		initProductType:function(){
			//类型为优惠码时多账户使用，每账户可使用次数，优惠码字段显示
			if($("#zw_f_produce_type").val() == "4497471600040002"){
				$("#zw_f_multi_account").parent().parent().show();
				$("#zw_f_account_useTime").parent().parent().show();
				$("#zw_f_account_useTime").val(1);
				$("#zw_f_cdkey").parent().parent().show();
				
				$("#zw_f_cdkey").attr("zapweb_attr_regex_id","469923180002");
			}else{
				$("#zw_f_multi_account").parent().parent().hide();
				$("#zw_f_account_useTime").parent().parent().hide();
				$("#zw_f_cdkey").parent().parent().hide();
				$("#zw_f_cdkey").removeAttr("zapweb_attr_regex_id");
				$("#zw_f_account_useTime").val(1);
				$("#zw_f_cdkey").val();
			}
		},
		beforeSubmit : function() {
			if($("#zw_f_produce_type").val() == "4497471600040002"){
				if ($('#zw_f_cdkey').val().length>30) {
					zapjs.f.message('优惠码：长度超过限制，最长为30个字符，请重新填写！');
					return false;
				};
			}
			return true;
	},
};

$(document).ready(function(){
	//生成类型
	$("#zw_f_produce_type").change(function(){
		addCoupon.initProductType();
	});
});
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addCoupon", function() {
		return addCoupon;
	});
}
