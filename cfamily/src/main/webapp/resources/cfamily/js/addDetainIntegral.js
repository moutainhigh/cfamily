var addDetainIntegral = {
	
	init_page:function (){
		
		$('#zw_f_order_code,#zw_f_mobilephone,#zw_f_order_money').attr("readonly","readonly");
		
		$('#zw_f_order_status').attr("disabled","disabled");
		$("#zw_f_give_status").attr("disabled","disabled");
		
		$('#zw_f_order_code').after('&nbsp;<input class="btn" type="button" value="选择" onclick="addDetainIntegral.show_windows()">');
		
//		$('#zw_f_mobilephone').after('&nbsp;<input class="btn" type="button" value="发送短信" onclick="addDetainIntegral.sendMessage()">');
		
	},
	
	message:function (flag,id){
		
		var order_code=$("#zw_f_order_code").val();
		var after_sale_person=$("#zw_f_contacts").val();
		var after_sale_mobile=$("#zw_f_mobile").val();
		var after_sale_address=$("#zw_f_address").val();
		var after_sale_postcode=$("#zw_f_receiver_area_code").val();
		
		if(order_code==''){
			zapjs.f.message("请选择需要售后的订单");
			return ;
		}
		
		if(after_sale_address==''){
			zapjs.f.message("请选择需要售后地址");
			return ;
		}
		
		var obj = {};
		obj.flag = flag;
		obj.after_sale_person=after_sale_person;
		obj.after_sale_mobile=after_sale_mobile;
		obj.after_sale_address=after_sale_address;
		obj.after_sale_postcode=after_sale_postcode;
		obj.order_code=order_code;
		obj.type="ret";
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForAfterSaleMessage2',obj,function(result) {
			if(result.resultCode==1){
				
				if('check'==flag){
					
					var id='zapjs_f_id_modal_message';
					var sModel = '<div id="' + id + '" ></div>';
					$(document.body).append(sModel);
					$('#' + id).html('<div class="w_p_20">' + result.resultMessage + '</div>');
					
					
					$('#'+id).dialog({
						title : "发送短信",
						closed : false,
						cache : false,
						modal : true,
						buttons:[{
							text: '收货人', 
							handler: function() { 
								addReturnGoodsNew.message("owner",id);
								zapjs.f.window_close(id);
							}
						},{
							text: '下单人', 
							handler: function() { 
								addReturnGoodsNew.message("buyer",id);
								zapjs.f.window_close(id);
							}
							
						}]
						
					});
					
					
				}
			}
		});
		
	},
	
	sendMessage:function (){
		addReturnGoodsNew.message("check");
	},
	
	
	
	show_windows : function(){
			zapjs.f.window_box({
				id : 'as_return_goods',
				content : '<iframe src="../show/page_chart_v_cf_sellororder_manage_cc?zw_s_iframe_select_source=as_return_goods&zw_s_iframe_select_page=page_chart_v_cf_sellororder_manage_cc&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.addDetainIntegral.addcb" frameborder="0" style="width:100%;height:600px;"></iframe>',
				width : '800',
				height : '650'
			});
	},
	
	addcb : function(sId,va, sVal){
		var obj = {};
		obj.order_code = sVal;
		obj.do_type = "D";
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForOrderAChange',obj,function(result) {
			if(result.resultCode==1){
				
				$("#zw_f_order_code").val(sVal);
				$("#zw_f_mobilephone").val(result.buyer_mobile);
				$("#zw_f_order_money").val(result.order_money);
				$("#zw_f_order_status").val(result.order_status);
				$("#zw_f_integral").val(result.integral);
					
				zapjs.f.window_close(sId);
			}
		});
	},
	
	
	returnMoney:function (oElm){
		
		var oForm = $(oElm).parents("form");
		var price=0;
		$(oForm).find(".ttxx").each(function(n, el) {
			
			var sku_price=eval($(el).find("td:eq(3)").html());
			var num=$(el).find("input").val();
			var _k=0.00;
			if(!isNaN(num)){
				_k=eval(num);
			}
			
			price+=sku_price*_k;
		});
		
		$('#return_money').val(price.toFixed(2));
	},
	
	func_add : function (oElm) {
		if($("#zw_f_integral").val() % 100 != 0) {
			zapjs.f.modal({
				content : '积分填写错误!'
			});
			return;			
		}
		$('#zw_f_order_status').removeAttr("disabled");
		$('#zw_f_give_status').removeAttr("disabled");
		console.log($("#zw_f_give_status").val());
		if($("#zw_f_give_status").val()=='4497471600460003'){
			$("#zw_f_give_status").val('4497471600460001');
		}
		var oForm = $(oElm).parents("form");
		zapjs.zw.modal_process();
		if (zapjs.f.ajaxsubmit(oForm, "../func/" + $(oElm).attr('zapweb_attr_operate_id'), zapjs.zw.func_success, zapjs.zw.func_error)) {

		} else {
			zapjs.f.modal_close();
		}
		
	}
	
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addDetainIntegral",["zapjs/zapjs","zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return addDetainIntegral;
	});
}
