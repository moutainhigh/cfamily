var afterServiceAddproduct = {
	show_windows : function(pcs){
			
			zapjs.f.window_box({
				id : 'as_productlist_productids'+pcs,
				content : '<iframe src="../show/page_chart_v_as_pc_productinfo?zw_f_service_code='+pcs+'&zw_s_iframe_select_source=as_productlist_productids'+pcs+'&zw_s_iframe_select_page=page_chart_v_as_pc_productinfo&zw_s_iframe_max_select=0&zw_s_iframe_select_callback=parent.afterServiceAddproduct.addcb" frameborder="0" style="width:100%;height:500px;"></iframe>',
				width : '700',
				height : '550'
			});
	},
	addcb : function(sId, sVal){
			var obj = {};
			obj.product_codes = sVal;
			obj.category_code = sId.substring(25);
			zapjs.zw.api_call('com_cmall_productcenter_process_AfterServiceProductsApi',obj,function(result) {
				if(result.code=='909101001'){
					zapadmin.model_message(result.name);
					zapjs.f.window_close(sId);
				}else{
					zapadmin.model_message(result.name);
				}
			});
	}
};
if (typeof define === "function" && define.amd) {
	define("cmanage/js/afterServiceAddproduct",["zapjs/zapjs",'zs/zs',"zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return afterServiceAddproduct;
	});
}
