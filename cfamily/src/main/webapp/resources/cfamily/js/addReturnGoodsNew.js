var addReturnGoodsNew = {
	
	init_page:function (){
		
		$('#zw_f_order_code,#zw_f_buyer_mobile,#zw_f_contacts,#zw_f_mobile,#zw_f_address,#zw_f_receiver_area_code,#zw_f_freight').attr("readonly","readonly");
		$('#zw_f_freight').val('4497476900040002');
		
		$('#zw_f_order_code').after('&nbsp;<input class="btn" type="button" value="选择" onclick="addReturnGoodsNew.show_windows()">');
		
		$('#zw_f_contacts').after('&nbsp;<input class="btn" type="button" value="选择地址" onclick="addReturnGoodsNew.changeAddr()">');
		
		$('#zw_f_buyer_mobile').after('&nbsp;<input class="btn" type="button" value="发送短信" onclick="addReturnGoodsNew.sendMessage()">');
		
		$('#zw_f_goods_receipt').change(function (){
			
			if($('#zw_f_goods_receipt').val()=='4497476900040002'){
				$('#zw_f_flag_return_goods').val('4497477800090002');
				$('#zw_f_flag_return_goods').attr("readonly","readonly");
			}else{
				$('#zw_f_flag_return_goods').removeAttr("readonly");
			}
		});
		
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	changeAddr : function(){
		
		var zw_f_order_code= $("#zw_f_order_code").val();
		if(zw_f_order_code==''){
			zapjs.f.message("请选择需要售后的订单");
			return ;
		}
		
		var small_seller_code= $("#small_seller_code").val();
		
		zapjs.f.window_box({
			id : 'changeAddr',
			content : '<iframe src="../show/page_chart_v_select_oc_address_info?zw_s_iframe_select_source=changeAddr&zw_f_small_seller_code='+small_seller_code+'&zw_s_iframe_select_page=page_chart_v_select_oc_address_info&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.addReturnGoodsNew.addAddr" frameborder="0" style="width:100%;height:600px;"></iframe>',
			width : '800',
			height : '650'
		});
	},
	
	addAddr : function(sId,va, sVal){
		var obj = {};
		obj.uid = va;
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForUserAddressInfo',obj,function(result) {
			if(result.resultCode==1){
				
				$("#zw_f_contacts").val(result.after_sale_person);
				$("#zw_f_mobile").val(result.after_sale_mobile);
				$("#zw_f_address").val(result.after_sale_address);
				$("#zw_f_receiver_area_code").val(result.after_sale_postcode);
				
				zapjs.f.window_close(sId);
			}
		});
	},
	
	
	show_windows : function(){
			zapjs.f.window_box({
				id : 'as_return_goods',
				content : '<iframe src="../show/page_chart_v_cf_sellororder_manage_cc?zw_s_iframe_select_source=as_return_goods&zw_s_iframe_select_page=page_chart_v_cf_sellororder_manage_cc&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.addReturnGoodsNew.addcb" frameborder="0" style="width:100%;height:600px;"></iframe>',
				width : '800',
				height : '650'
			});
	},
	
	addcb : function(sId,va, sVal){
		debugger;
		var obj = {};
		obj.order_code = sVal;
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForOrderAChange',obj,function(result) {
			if(result.resultCode==1){
				
				$("#zw_f_order_code").val(sVal);
				$("#zw_f_buyer_mobile").val(result.buyer_mobile);
				$("#zw_f_contacts").val(result.contacts);
				$("#zw_f_mobile").val(result.mobile);
				$("#zw_f_address").val(result.address);
				$("#zw_f_receiver_area_code").val(result.receiver_area_code);
				$("#small_seller_code").val(result.small_seller_code);
				
				$('#details').empty();
				
				$('#return_money').val("0.00");
				
				var html='<div class="zab_info_page_title  w_clear"><span>订单商品</span><input type="hidden" name="zw_f_detail" id="zw_f_detail"></div>';
					html+='<table class="table  table-condensed table-striped table-bordered table-hover" style="width:70%"> ';
					html+='<thead> <tr> <th>商品编号</th><th>SKU编号</th><th>SKU名称</th><th>购买价格（元）</th><th>购买件数</th><th>已退件数</th> <th>进行中件数</th> <th>本次退货件数</th></tr></thead>';
					html+='<tbody>';
					
					for (var i = 0; i < result.details.length; i++) {
						html+='<tr class="ttxx"><td>'+result.details[i].product_code+'</td><td>'+result.details[i].sku_code+'</td><td>'+result.details[i].sku_name+'</td><td>'+result.details[i].sku_price+'</td><td>'+result.details[i].sku_num+'</td><td>'+result.details[i].return_num+'</td><td>'+result.details[i].occupy_num+'</td><td>     <div class="control-group"> <label for="dtt'+i+'" class="control-label" style="display:none">退货件数</label><div><input id="dtt'+i+'" name="dtt'+i+'" class="input-mini" type="text" value="0" placeholder="number" zapweb_attr_regex_id="469923180003" onKeyup="addReturnGoodsNew.returnMoney(this)"></div></div>   </td></tr>';
					}
					
					html+='</tbody>';
					html+='</table>';
					
					$('#details').append(html);
					
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
		
		$("#zw_f_detail").val("");
		var oForm = $(oElm).parents("form");
		if (zapjs.zw.func_regex(oForm)) {
			
			var ds="";
			var off=false;
			$(oForm).find(".ttxx").each(function(n, el) {
				
				var sku_code=$(el).find("td:eq(1)").html();
				var sku_num=eval($(el).find("td:eq(4)").html());
				var return_num=eval($(el).find("td:eq(5)").html());
				var occupy_num=eval($(el).find("td:eq(6)").html());
				var now_num=eval($(el).find("input").val());
				
				ds+=(sku_code+"_"+now_num+",");
				
				if(sku_num<(return_num+occupy_num+now_num)){
					zapjs.zw.validate_error($(el).find("input"),"本次退货件数不可大于"+(sku_num-return_num-occupy_num));
					off=true;
					return false ;
				}
			});
			
			if(ds.length>0){
				ds=ds.substr(0,ds.length-1);
			}
			$('#zw_f_detail').val(ds);
			
			if(off){return ;}
			
			var re_num=0
			$(oForm).find(".ttxx input").each(function(n, el) {
				re_num+=eval($(el).val());
			});
			
			if(re_num<1){off=true;}
			if(off){
				zapjs.f.message("请正确填写退货的件数");
				return ;
			}
			
			zapjs.zw.modal_process();
			if (zapjs.f.ajaxsubmit(oForm, "../func/" + $(oElm).attr('zapweb_attr_operate_id'), zapjs.zw.func_success, zapjs.zw.func_error)) {

			} else {
				zapjs.f.modal_close();
			}
		}
		
	}
	
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addReturnGoodsNew",["zapjs/zapjs","zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return addReturnGoodsNew;
	});
}
