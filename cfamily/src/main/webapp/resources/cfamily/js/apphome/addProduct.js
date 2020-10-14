var addProduct = {
	show_windows : function(){
			zapjs.f.window_box({
				id : 'as_productlist_productids',
				content : '<iframe src="../show/page_chart_v_cf_pc_productinfo_multiSelect?zw_f_seller_code=SI2003&zw_s_iframe_select_source=as_productlist_productids&zw_s_iframe_select_page=page_chart_v_cf_pc_productinfo_multiSelect&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.addProduct.addcb" frameborder="0" style="width:100%;height:500px;"></iframe>',
				width : '700',
				height : '550'
			});
	},
	addcb : function(sId, sVal,a,b,c){
			var obj = {};
			obj.product_code = sVal;
			zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetProductForFlash',obj,function(result) {
				if(result.resultCode==1){
					//设置值
					$('#zw_f_product_code').val(result.product_code);
					$('#zw_f_title').val(result.product_name);
//					$('#zw_f_product_status').val(result.product_status);
					$('#zw_f_stock_num').val(result.product_stock);
//					$('#zw_f_sell_price').val(result.sell_price);
					zapjs.f.window_close(sId);
				}else{
					zapadmin.model_message('查询操作失败');
				}
			});
	}
	
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/apphome/addProduct",["zapjs/zapjs",'zs/zs',"zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return addProduct;
	});
}
