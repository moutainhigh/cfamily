var authorityLogo = {
		init : function(){
			authorityLogo.showPc();
		},
		showPc: function(){
			if($("#zw_f_all_flag").val()=="449747110001"){//是否全场为否
				$("#zw_f_show_product_source_0").parent().parent().show();
			}else{
				$("#zw_f_show_product_source_0").parent().parent().hide();
			}
		}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/authorityLogo",function() {
		return authorityLogo;
	});
}

$(document).ready(function(){
	$("#zw_f_all_flag").change(function(){
		authorityLogo.showPc();
	});
});
