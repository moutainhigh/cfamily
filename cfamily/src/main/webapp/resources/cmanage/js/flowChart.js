var flowChart = {
	laststatus : "",
	// 初始化
	init_flowChart : function() {
		if(flowChart.laststatus){
			$("#zw_f_last_status").val(flowChart.laststatus);
		}
		$("#zw_f_real_name").select2(); 
		$("#s2id_zw_f_real_name").attr("style","width:220px");
		$("#zw_f_brand_code").select2();
		$("#s2id_zw_f_brand_code").attr("style","width:220px");
		
	}
};

if (typeof define === "function" && define.amd) {
	define("cmanage/js/flowChart", ["zapadmin/js/zapadmin_single" ], function() {
		return flowChart;
	});
}
