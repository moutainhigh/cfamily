var add_exchange_goods = {
	
	initDetail:function (){
		var kk='';
		$('input[name="cx"]:checked').each(function (){
			kk+=$(this).attr('value')+'_'+$(this).next().val()+',';
		});
		
		if(kk.length>1){
			kk=kk.substr(0,kk.length-1);
		}
		$('#zw_f_detail').val(kk);
	},
		
	initSku:function (){
		
		var order_code=$("#zw_f_order_code").val();
		if(order_code){
			//查询订单详情
			var obj = {};
			obj.order_code = order_code;
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiForOrderDetail',obj,function(result) {
				if(result.resultCode==1){
					
					if($('#detail_remove')){
						$('#detail_remove').remove();
					}
					
					//设置值
					var html='';
					html+='<div class="control-group" id="detail_remove"> <input type="hidden" name="zw_f_detail" id="zw_f_detail">';
					html+='<label class="control-label"><span class="w_regex_need">*</span>商品：</label>';
					html+='<div class="controls">';
					
					$.each(result.details,function (i,n){
						var num=n.sku_num;
						var sel='';
						for (var i = 1; i <= num; i++) {
							sel+='<option value="'+i+'">'+i+'</option>';
						}
						
						html+='<input type="checkbox" name="cx" value="'+n.sku_code+'" onclick="add_exchange_goods.initDetail()">'+n.sku_name+'  &nbsp;<select class="span1" onchange="add_exchange_goods.initDetail()">'+sel+'</select><br>';
					});
					
					html+='</div>';
					html+='</div>';
					
					$("#zw_f_order_code").parents(".control-group").append(html)
					
				}else{
					//zapadmin.model_message('查询操作失败');
				}
			});
			
		}
	}

};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/add_exchange_goods", function() {
		return add_exchange_goods;
	});
}

$(document).ready(function(){
	//生成类型
	$("#zw_f_order_code").change(function(){
		add_exchange_goods.initSku();
	});
});