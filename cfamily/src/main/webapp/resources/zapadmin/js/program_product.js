var program_product = {

	temp : {
		opts : {},
		checkdata : {}

	},

	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:true,
			callback:'parent.program_product.result'
		};

		var s = $.extend({}, defaults, options || {});

		program_product.temp.opts[s.id] = s;
		
		program_product.show_text(s.id);
	},
	show_box : function(sId,sAppCode) {
		var s=program_product.temp.opts[sId];
		
		zapjs.f.window_box({
			id : sId + 'program_product_showbox',
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
		zapjs.f.window_close(sId + 'program_product_showbox');
	},
	show_text : function(sId) {
		
	},

	result : function(sId, sValues, sTexts) {
		
		var program_code = $("#hide_program_code").val();
		var url= "../func/4698c0f3f49211e48d2d005056925439?program_code="+program_code+"&pc_code="+sValues;
		$.getJSON(url, function(data){
			zapjs.f.autorefresh();
		});
		zapjs.f.window_close(sId + 'program_product_showbox');

	},

};
if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/program_product", ["lib/easyui/locale/easyui-lang-zh_CN","zapadmin/js/tryout_chartajax"], function() {
		return program_product;
	});
}
