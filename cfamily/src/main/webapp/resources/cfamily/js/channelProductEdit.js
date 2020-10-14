
cfamily_channelProductEdit  = {
		
		channelProductEdit : function(productCode, skuCode, productName){
			$("#product_form_forsubmit").remove();
			
			var showHtml = "<form class='form-horizontal' id='product_form_forsubmit' method='POST' style='margin-top:20px;'>" +
								"<div class='control-group'>" +
									"<label class='control-label' for='productCode'>商品编号：</label>" +
									"<label class='control-label' for='productCode' style='text-align: left;' >" +productCode+ "</label>" +
								"</div>" +
								"<div class='control-group'>" +
									"<label class='control-label' for='skuCode'>SKU编号：</label>" +
									"<label class='control-label' for='skuCode' style='text-align: left;' >" +skuCode+ "</label>" +
									"<input type='hidden' id='skuCode' name='skuCode' value='" +skuCode+"'></input>" +
								"</div>" +
								"<div class='control-group'>" +
									"<label class='control-label' for='productName'>商品名称：</label>" +
									"<label class='control-label' for='productName' style='text-align: left;' >" +productName+ "</label>" +
								"</div>" +
								"<div class='control-group'>" +
									"<label class='control-label' for='zw_f_supply_price_proportion'>供货价比例(%)：</label>" +
									"<input id='zw_f_supply_price_proportion' name='zw_f_supply_price_proportion'></input>" +
								"</div>" +
								"<div class='control-group'>" +
									"<label class='control-label' for='zw_f_remark'>备注：</label>" +
									"<input id='zw_f_remark' name='zw_f_remark'></input>" +
								"</div>";
			showHtml += "<div class='control-group' style='text-align:center; vertical-align:middel;'>" +
							"<input type='button' style=' width:100px;' id='submitForm' class='btn  btn-primary' zapweb_attr_operate_id='85649a91172811eaabac005056165069' value='确定'>" +
					  "</div>";
			showHtml += "</form>";
			
			var modalOption="";
			modalOption = {id:"product_form", content:showHtml, title:"渠道商品修改", oktext:"关闭", width:500, height:375};
			
			zapjs.f.window_box(modalOption);
			
			$(".panel-tool-max").hide();
			
			$("#cancleForm").bind("click",function(){
				zapjs.f.window_close("product_form");
			});
			
			$("#submitForm").bind("click",function(){
				var sku_code = $("#skuCode").val();
				var supply_price_proportion = $("#zw_f_supply_price_proportion").val();
				var remark = $("#zw_f_remark").val();
				if(supply_price_proportion == null || supply_price_proportion == ""){
					zapjs.f.message("请填写供货价比例!");
					return false;
				}
				if(sku_code == null || sku_code == ""){
					zapjs.f.message("修改失败,请刷新页面重试!");
					return false;
				}
				
				var productForm = $("#submitForm").parents("form");
				zapjs.f.ajaxsubmit(
					productForm,
					'../func/'+$("#submitForm").attr('zapweb_attr_operate_id'),
					function(data){
						var o = data;
						switch (o.resultType) {
							case "116018010":
								eval(o.resultObject);
								break;
							default:
								if (o.resultCode == "1") {
									if (o.resultMessage == "") {
										o.resultMessage = "修改成功";
									}
									zapjs.zw.modal_show({
										content : o.resultMessage,
										okfunc : 'zapjs.f.tourl("page_chart_v_v_pc_channel_productinfo");'
									});
								} else {
									zapjs.zw.modal_show({
										content : o.resultMessage
									});
								}
								break;
						}
					}, function(data){
						alert('系统出现错误，请联系技术，谢谢！');
					}
				);
			});
		}
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/channelProductEdit", [], function() {
		return cfamily_channelProductEdit;
	});
}

