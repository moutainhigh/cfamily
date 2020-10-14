var chooseProduct = {

		temp : {
			opts : {},
			checkdata : {}

		},

		init : function(options) {

			var defaults = {
				pagecode : '',
				id : '',
				buttonflag:true,
				callback:'parent.chooseProduct.result'
			};

			var s = $.extend({}, defaults, options || {});

			chooseProduct.temp.opts[s.id] = s;

			if(s.buttonflag)
			{
				$('#' + s.id+"_show_text").parent().append('<div class="w_left"><input class="btn btn-success"  type="button" value="选择商品" onclick="chooseProduct.show_box(\'' + s.id + '\')" /></div>');
			}
			chooseProduct.show_text(s.id);
		},
		show_box : function(sId) {
			var s=chooseProduct.temp.opts[sId];
			//展示的列表页面
			s.source='page_chart_v_v_pc_product_sku_info_for_batchpurchase';

			zapjs.f.window_box({
				id : sId + 'chooseProduct_showbox',
				content : '<iframe src="../show/'+s.source+'?zw_f_seller_code='+s.seller_code+'&zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

				width : '700',
				height : '550'
			});
		},
		
		close_box:function(sId)
		{
			zapjs.f.window_close(sId + 'chooseProduct_showbox');
		},
		show_text : function(sId) {

//			var sText = $('#' + sId + "_show_text").val();
//			$('#' + sId + '_show_ul').html('');

//			if (sText) {
//				var aT = sText.split(',');
//				for (var i in aT) {
//					$('#' + sId + '_show_ul').append('<li>' + aT[i] + '</li>');
//				}
	//
//			}

		},
		result : function(sId, sValues, sTexts ,subsidy) {

			$('#' + sId).val(sValues);
			$('#' + sId + "_show_text").val(sTexts);
			var b = $('#' + sId + "_show_text").val(sTexts);

		//	chooseProduct.show_text(sId);
			var obj = {};
			obj.skuCodes = sValues;
			zapjs.zw.api_call('com_cmall_familyhas_api_ApiForPurchaseProduct',obj,function(result) {
				if(result.resultCode==1){
					purchaseOrderAdd.temp.purchaseOrdersJson = result.orderDetailList;
					purchaseOrderAdd.productTable_init();
					zapjs.f.window_close(sId + 'chooseProduct_showbox');
				}else{
					zapadmin.model_message('添加商品失败');
				}
			});

		}
	};

	if ( typeof define === "function" && define.amd) {
		define("cevent/js/chooseProduct", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
			return chooseProduct;
		});
	}
