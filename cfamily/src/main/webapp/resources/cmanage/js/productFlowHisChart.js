var productFlowHisChart = {
	// 初始化
	init_flowChart : function() {
		
		if($("#zw_f_product_code") != null && typeof($("#zw_f_product_code")) != undefined){
			
			$("#zw_f_product_code").select2(); 
			$("#s2id_zw_f_product_code").attr("style","width:220px");
			
		}
		
		if($("#zw_f_product_name") != null && $("#zw_f_product_name") != undefined){
			
			$("#zw_f_product_name").select2(); 
			$("#s2id_zw_f_product_name").attr("style","width:220px");
			
		}
		
		if($("#zw_f_creator") != null && $("#zw_f_creator") != undefined){
			
			$("#zw_f_creator").select2(); 
			$("#s2id_zw_f_creator").attr("style","width:220px");
			
		}
		
	}
};

if (typeof define === "function" && define.amd) {
	define("cmanage/js/productFlowHisChart", ["zapadmin/js/zapadmin_single" ], function() {
		return productFlowHisChart;
	});
}
