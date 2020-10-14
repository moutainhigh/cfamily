var infoCfChart = {
	init_infoCfChart : function(){
		if($("#zw_f_small_seller_code_select") != null && typeof($("#zw_f_small_seller_code_select")) != undefined){
				
			$("#zw_f_small_seller_code_select").select2(); 
			$("#s2id_zw_f_small_seller_code_select").attr("style","width:220px");
				
		}
			
	}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/infoCfChart", ["zapadmin/js/zapadmin_single" ], function() {
		return infoCfChart;
	});
}
