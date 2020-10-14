var brandProduct = {
		
	show_windows : function(){
			zapjs.f.window_box({
				id : 'as_productlist_productids',
				content : '<iframe src="../show/page_chart_v_cf_pc_productinfo_multiSelect?zw_f_seller_code=SI2003&zw_s_iframe_select_source=as_productlist_productids&zw_s_iframe_select_page=page_chart_v_cf_pc_productinfo_multiSelect&zw_s_iframe_max_select=2&zw_s_iframe_select_callback=parent.brandProduct.saveProduct" frameborder="0" style="width:100%;height:500px;"></iframe>',
				width : '700',
				height : '550'
			});
	},
	saveProduct : function(sId, sVal,a,b,c){
			var obj = {};
			var chk_value =[];//定义一个数组    
			$(window.frames[0].document).find('input[name="f_0"]:checked').each(function(){    
            	chk_value.push($(this).val());//将选中的值添加到数组chk_value中    
            });
			obj.productCodes = chk_value.join(",");
			obj.infoCode = $("#infoCode").val();
			zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiSaveBrandProduct',obj,function(result) {
				if(result.resultCode==1){
					zapadmin.model_message('添加商品成功！');
					zapjs.f.window_close(sId);
					parent.location.reload();
				}else{
					zapadmin.model_message('添加商品失败');
				}
			});
	},
	show_import_windows : function(){
		zapjs.f.window_box({
			id : 'pph_import_product',
			content : '<iframe src="../show/page_importProduct_v_fh_brand_preference?zw_f_seller_code=SI2003&infoCode='+$("#infoCode").val()+'" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});
	}
	
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/brandProduct",["zapjs/zapjs",'zs/zs',"zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return brandProduct;
	});
}
