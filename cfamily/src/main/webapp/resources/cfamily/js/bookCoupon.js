var bookCoupon = {
	init : function(){
		bookCoupon.initProductType();
		$("form input").attr("disabled", true);
		$("form select").attr("disabled", true);
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
	define("cfamily/js/bookCoupon", function() {
		return bookCoupon;
	});
}

