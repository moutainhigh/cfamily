var cshop_chartSellerQualification = {
	init:function(){
		
		$("#zw_f_brand_code_list").select2();
		$("#s2id_zw_f_brand_code_list").attr("style","width:220px");
		
	},
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/chartSellerQualification", function() {
		return cshop_chartSellerQualification;
	});
}
