var flashsalesSku = {
	show_windows : function(activity_code){
			zapjs.f.window_box({
				id : 'as_productlist_productids'+activity_code,
				content : '<iframe src="../show/page_chart_v_cf_pc_skuinfo_status?zw_f_activity_code='+activity_code+'&zw_f_seller_code=SI2003&zw_s_iframe_select_source=as_productlist_productids'+activity_code+'&zw_s_iframe_select_page=page_chart_v_cf_pc_skuinfo_status&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.flashsalesSku.addcb" frameborder="0" style="width:100%;height:500px;"></iframe>',
				width : '700',
				height : '550'
			});
	},
	addcb : function(sId, sVal,a,b,c){
			var obj = {};
			obj.skuCode = sVal;
//			obj.activity_code = sId.substring(25);
			zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetSku',obj,function(result) {
				if(result.resultCode==1){
					//设置值
					$('#zw_f_sku_code').val(result.sku_code);
					$('#zw_f_sku_name').val(result.sku_name);
					$('#zw_f_stock_num').val(result.stock_num);
					$('#zw_f_sell_price').val(result.sell_price);
					$('#zw_f_product_name').val(result.product_name);
					$('#zw_f_product_status').val(result.product_status);
					zapjs.f.window_close(sId);
				}else{
					zapadmin.model_message('查询操作失败');
				}
			});
	}
};
if (typeof define === "function" && define.amd) {
	define("cmanage/js/flashsalesSku",["zapjs/zapjs",'zs/zs',"zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return flashsalesSku;
	});
}
