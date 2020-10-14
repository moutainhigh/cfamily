var cshop_sellerQualification = {
	init:function(){
		
		$("#zw_f_brand_code").select2();
		$("#s2id_zw_f_brand_code").attr("style","width:220px");
		
	},
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/sellerQualification", function() {
		return cshop_sellerQualification;
	});
}
