var productSkuPrice = {

	init_productSkuPrice : function(){
		$("#zw_f_brand_code").select2();
		$("#s2id_zw_f_brand_code").attr("style","width:220px");
	},
	
	productSkuPriceList : [],
	
	getProductSkuPriceList : function() {
		
		var trs = $("#productSkuPriceTable").find("tr");
		
		productSkuPrice.productSkuPriceList = [];
		
		for(i = 0;i<trs.length-2;i++){
			
			var skuPriceObject = {
					
					sku_code:$("#zw_f_sku_code_"+i).val(), 
					
					product_code:$("#zw_f_product_code_"+i).val(),
						
					cost_price:$("#zw_f_cost_price_"+i).val(),
					
					sell_price:$("#zw_f_sell_price_"+i).val(),
					
					cost_price_old:$("#zw_f_cost_price_old_"+i).val(),
					
					sell_price_old:$("#zw_f_sell_price_old_"+i).val()
					
			};
			
			productSkuPrice.productSkuPriceList.push(skuPriceObject);
			
		}
		
		return productSkuPrice.productSkuPriceList;
		
	},

    submit:function(object){
    	
    	$('#zw_f_json').val(zapjs.f.tojson(productSkuPrice.getProductSkuPriceList()));
    	
    	productSkuPrice.func_call(object);  	
    	
    },
    
 // 执行成功
	func_success : function(o) {

		switch (o.resultType) {
			case "116018010":
				eval(o.resultObject);
				break;
			default:
				// alert(o.resultMessage);

				if (o.resultCode == "1") {

					if (o.resultMessage == "") {
						o.resultMessage = "操作成功";
					}

					zapjs.zw.modal_show({
						content : o.resultMessage,
						okfunc : 'zapjs.f.tourl("page_chart_v_productinfo_price");'
					});

				} else {
					zapjs.zw.modal_show({
						content : o.resultMessage
					});
				}

				break;
		}

	},

    
	func_call : function(oElm) {

		var oForm = $(oElm).parents("form");
		if (zapjs.zw.func_regex(oForm)) {
			zapjs.zw.modal_process();
			if (zapjs.f.ajaxsubmit(oForm, "../func/" + $(oElm).attr('zapweb_attr_operate_id'), productSkuPrice.func_success, zapjs.zw.func_error)) {

			} else {
				zapjs.f.modal_close();
			}
		}
	},
    
    init_flowChart : function(flag) {
		
		if(flag !='zw_f_product_name'){
			
			if($("#zw_f_product_name") != null && $("#zw_f_product_name") != undefined){
				
				$("#zw_f_product_name").select2(); 
				$("#s2id_zw_f_product_name").attr("style","width:220px");
				
			}
			
		}
		
		if($("#zw_f_merchant_name") != null && $("#zw_f_merchant_name") != undefined){
			
			$("#zw_f_merchant_name").select2(); 
			$("#s2id_zw_f_merchant_name").attr("style","width:220px");
			
		}
		
	},
	
	callChangeFlow : function(productCcode,urlParam){

		
		var url= "../func/229b78e18f6e4117a27be0460bed8699?product_code="+productCcode;
		
		$.getJSON(url, function(data){
			
			if(eval(data.resultCode) != 1){
				
				modalOption = {content:data.resultMessage,title:"提示",oktext:"关闭",height:200};
				
				zapjs.f.window_box(modalOption);
				
			}else{
				
				zapjs.f.tourl(urlParam);
				
			}
			
						
			
		});
	
	},
	//运营审核
	priceChangeFlow2YY: function(productCcode, urlParam) {
		var url= "../func/229b78e18f6e4117a27be0460bed8699?product_code="+productCcode;
		$.getJSON(url, function(data){
			if(eval(data.resultCode) != 1){
				modalOption = {content:data.resultMessage,title:"提示",oktext:"关闭",height:200};
				zapjs.f.window_box(modalOption);
			}else{
				zapjs.f.tourl(urlParam);
			}
		});
	}
    
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/productSkuPrice", ["zapadmin/js/zapadmin_single" ], function() {
		return productSkuPrice;
	});
}