var group_sku_rebate = {
	show_windows : function(){
			zapjs.f.window_box({
				id : 'group_sku_rebate',
				content : '<iframe src="../show/page_chart_v_group_sku_rebate_pc_skuinfo?zw_f_seller_code=SI2003&zw_s_iframe_select_source=group_sku_rebate&zw_s_iframe_select_page=page_chart_v_group_sku_rebate_pc_skuinfo&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.group_sku_rebate.addcb" frameborder="0" style="width:100%;height:700px;"></iframe>',
				width : '700',
				height : '550'
			});
	},
	addcb : function(sId, sVal,a,b,c){
			var obj = {};
			obj.skuCode = sVal;
			zapjs.zw.api_call('com_cmall_productcenter_service_api_ApiGetSku',obj,function(result) {
				if(result.resultCode==1){
					//设置值
					htmlStr="<tr><td><input type=\"text\" name=\"zw_f_product_code\" value=\""+result.product_code+"\" readonly=\"readonly\"/></td>"+
					"<td><input type=\"text\" name=\"zw_f_sku_code\" value=\""+result.sku_code+"\" readonly=\"readonly\"/></td>"+
					"<td><input type=\"text\" name=\"zw_f_product_name\" value=\""+result.product_name+"\" readonly=\"readonly\"/></td>"+
					"<td><input type=\"text\" name=\"zw_f_sell_price\" value=\""+result.sell_price+"\" readonly=\"readonly\"/></td>"+
					"<td><input type=\"text\" name=\"zw_f_product_status\" value=\""+result.product_status+"\" readonly=\"readonly\"/></td>"+
					"<td><input type=\"text\" name=\"zw_f_stock_num\" value=\""+result.stock_num+"\" readonly=\"readonly\"/><input type=\"hidden\" name=\"zw_f_seller_code\" value=\""+result.seller_code+"\"/></td>"+
					"<td><a onclick='removeTr(this)'>删除</a></td>"+
					"</tr>";
					$('#addskubody').append(htmlStr);
					zapjs.f.window_close(sId);
				}else{
					zapadmin.model_message('查询操作失败');
				}
			});
	}
};
if (typeof define === "function" && define.amd) {
	define("cmanage/js/group_sku_rebate",["zapjs/zapjs",'zs/zs',"zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return group_sku_rebate;
	});
}
function removeTr(temp){
    $(temp).parent().parent().remove(); 
}