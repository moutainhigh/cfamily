var ProductPopSelect = {

	temp : {
		opts : {},
		checkdata : {}

	},

	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:true,
			callback:'parent.ProductPopSelect.result'
		};

		var s = $.extend({}, defaults, options || {});

		ProductPopSelect.temp.opts[s.id] = s;
		
		ProductPopSelect.show_text(s.id);
	},
	show_box : function(sId) {
		var s=ProductPopSelect.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'ProductPopSelect_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select=1&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	// 品牌显示专用
	show_box_for_brand : function(sId) {
		zapjs.f.window_box({
			id : sId + 'BrandPopSelect_showbox',
			content : '<iframe src="../show/page_chart_v_pc_brandinfo_for_select?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=page_chart_v_pc_brandinfo_for_select&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.ProductPopSelect.result_for_brand" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});
	},
	close_box:function(sId)
	{    
		zapjs.f.window_close(sId + 'ProductPopSelect_showbox');
		
	},
	show_text : function(sId) {

		var sText = $('#' + sId + "_show_text").val();
		$('#' + sId + '_show_ul').html('');

		if (sText) {
			var aT = sText.split(',');
			for (var i in aT) {
				$('#' + sId + '_show_ul').append('<li>' + aT[i] + '</li>');
			}

		}

	},

	result : function(sId, sValues, sTexts) {
		$("#"+ sId).val(sValues);
		$("#"+ sId + "_text").text(sTexts);
		zapjs.f.window_close(sId + 'ProductPopSelect_showbox');
	},
	result_for_brand : function(sId, sValues, sTexts) {
		$("#"+ sId).val(sValues);
		$("#"+ sId + "_text").text(sTexts);
		zapjs.f.window_close(sId + 'BrandPopSelect_showbox');
	},
	
	// 【视频播放列表】专用
	show_box_for_radio : function() {
		var sId = 'zw_f_showmore_linkvalue';   
		var s = ProductPopSelect.temp.opts[sId]; 
		s.callback = 'parent.ProductPopSelect.result_for_radio';   
		zapjs.f.window_box({
			id : sId + 'ProductPopSelect_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId 
							+ '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select=1&zw_s_iframe_select_callback='
							+ s.callback + '" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});
	},
	// 【视频播放列表】专用
	result_for_radio : function(sId , productCode , productName , price ) {
		$("#zw_f_product_name").val(productName);
		$("#zw_f_product_code").val(productCode);  
		$("#zw_f_product_price").val(price.split("--")[0]);  
		zapjs.f.window_close(sId + 'ProductPopSelect_showbox');
	},


};
if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/ProductPopSelect", ["lib/easyui/locale/easyui-lang-zh_CN","zapadmin/js/qrCode_chartajax"], function() {
		return ProductPopSelect;
	});
}






















