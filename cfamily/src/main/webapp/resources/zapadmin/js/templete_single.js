/*var templete_single = {

	temp : {
		opts : {},
		checkdata : {}

	},

	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:true,
			callback:'parent.templete_single.result'
		};

		var s = $.extend({}, defaults, options || {});

		templete_single.temp.opts[s.id] = s;
		
		templete_single.show_text(s.id);
	},
	show_box : function(sId,sAppCode) {
		var s=templete_single.temp.opts[sId];
		
		zapjs.f.window_box({
			id : sId + 'templete_single_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + 
					'&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+
					'&zw_s_iframe_select_callback='+s.callback+'&zw_f_product_status=4497153900060002&zw_f_seller_code='+sAppCode+
					' " frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	close_box:function(sId)
	{
		zapjs.f.window_close(sId + 'templete_single_showbox');
	},
	show_text : function(sId) {
		
		var sText = $('#zw_f_commodity_name').val();
		var val = $('#zw_f_commodity_number').val();
		
		$('#' + sId + '_show_code').html('');
		$('#' + sId + '_show_name').html('');
		
		$('#' + sId + '_show_code').html(sText);
		$('#' + sId + '_show_name').html(val);

	},

	result : function(sId, sValues, sTexts) {
		$('#zw_f_commodity_number').val(sValues);
		$('#zw_f_commodity_name').val(sTexts);
		zapjs.f.window_close(sId + 'templete_single_showbox');
		
		templete_single.show_text(sId);

	},
	select_eventItemProduct ：function (sid){
		eventItemProduct_select.show_box(sid);
	}

};
if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/templete_single", ["lib/easyui/locale/easyui-lang-zh_CN","zapadmin/js/tryout_chartajax"], function() {
		return templete_single;
	});
}*/
var eventItemProduct_select = {

		temp : {
			opts : {},
			checkdata : {}

		},

		init : function(options) {

			var defaults = {
				pagecode : '',
				id : '',
				buttonflag:true,
				callback:'parent.eventItemProduct_select.result'
			};

			var s = $.extend({}, defaults, options || {});

			eventItemProduct_select.temp.opts[s.id] = s;

			if(s.buttonflag)
			{
				$('#' + s.id+"_show_text").parent().append('<div class="w_left"><input class="btn" type="button" value="选择" onclick="eventItemProduct_select.show_box(\'' + s.id + '\')" /></div>');
			
				$('#' + s.id+"_show_text").parent().append('<div class="w_left w_w_70p"><ul class="zab_js_zapadmin_single_ul" id="' + s.id + '_show_ul"></ul></div><div class="w_clear"></div>');
			}
			eventItemProduct_select.show_text(s.id);
		},
		show_box : function(sId) {
			var s=eventItemProduct_select.temp.opts[sId];
			zapjs.f.window_box({
				id : sId + 'eventItemProduct_select_showbox',
				content : '<iframe src="../show/'+s.source+'?zw_f_seller_code='+s.seller_code+'&zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

				width : '700',
				height : '550'
			});

		},
		close_box:function(sId)
		{
			zapjs.f.window_close(sId + 'eventItemProduct_select_showbox');
		},
		show_text : function(sId) {

			var sText = $('#' + sId + "_show_text").val();
			$('#' + sId + '_show_ul').html('');
			
			var sValues =  $('#' + sId).val();
			
			if (sText && sValues) {
				var aT = sText.split(',');
				var aV = sValues.split(',');
				for (var i in aT) {
					$('#' + sId + '_show_ul').append('<li id="li_'+aV[i]+'" style="position: relative;margin-left:9px;margin-botton:9px">' + aT[i] + '<i style="position: absolute;background: #f1f1f1;border: solid 1px #ccc;text-align: center;  right: -10px;top: -10px;font-style:normal;  width: 15px;height: 15px;line-height: 15px;cursor: pointer;display: inline-block;" onClick="javascript:eventItemProduct_select.delSkuCode(\''+aV[i]+'\')">X</i></li>');
				}

			}

		},
		delSkuCode : function(skuCode){
			$("#li_"+skuCode).remove();
			var skuCodes = $("#zw_f_sku_code").val();
			if (skuCodes == skuCode) {
				$("#zw_f_sku_code").val('');
				$("#zw_f_store_num").val(0);
				$("#zw_f_selling_price").val(0);
			}else if(skuCodes != null && skuCodes != "" && skuCodes.length != 0 && skuCodes.length>skuCode.length){
				//如果要删除的sku在词首，替换时在skuCode后面加逗号，否则在skuCode前加逗号
				if(skuCodes.substr(0,skuCode.length)==skuCode){		//在句首时
					skuCodes = skuCodes.replace(skuCode+",","");
				}else{
					skuCodes = skuCodes.replace(","+skuCode,"");
				}
				$("#zw_f_sku_code").val(skuCodes);
				
				var skuCodeArr = skuCodes.split(",");
				var opt ={};
				opt.skuCodes = skuCodeArr;
				opt.eventCode = $("#zw_f_event_code").val();
//				查sku重复，获取最低库存数量
				var minSellPrice = -1;		//最低价格
				var minStock = -1;			//最少库存
				zapjs.zw.api_call('com_cmall_eventcall_api_ApiCheckEventRepeatAndGetStock',opt,function(result) {
					if (1 != result.checkRepeat) {
						zapjs.f.message(result.checkRepeatMessage);
					}
					for ( var int = 0; int < skuCodeArr.length; int++) {
							
						//取最低价格
						var sellPrice = result.sellPriceMap[skuCodeArr[int]];
						if (-1 == minSellPrice) {
							minSellPrice = sellPrice;
						}else{
							if (parseFloat(minSellPrice) > parseFloat(sellPrice) ) {
								minSellPrice = sellPrice;
							}
						}
						//取最少库存
						var stock = result.stockMap[skuCodeArr[int]];
						if (-1 == minStock) {
							minStock = stock;
						}else{
							if (parseInt(minStock) > parseInt(stock) ) {
								minStock = stock;
							}
						}
					}
					$("#zw_f_store_num").val(minStock);
					$("#zw_f_selling_price").val(minSellPrice);
				});
			}
		},
		result : function(sId, sValues, sTexts,minSellPrice,minStock) {

			$('#' + sId).val(sValues);
			$('#' + sId + "_show_text").val(sTexts);

			zapjs.f.window_close(sId + 'eventItemProduct_select_showbox');

			$("#zw_f_store_num").val(minStock);
			$("#zw_f_selling_price").val(minSellPrice);
			
			eventItemProduct_select.show_text(sId);

		}
	};

	if ( typeof define === "function" && define.amd) {
		define("cevent/js/eventItemProduct_select", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
			return eventItemProduct_select;
		});
	}
