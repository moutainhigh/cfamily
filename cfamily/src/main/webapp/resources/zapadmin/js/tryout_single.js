var tryout_single = {

	temp : {
		opts : {},
		checkdata : {}

	},

	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:true,
			callback:'parent.tryout_single.result'
		};

		var s = $.extend({}, defaults, options || {});

		tryout_single.temp.opts[s.id] = s;
		
		tryout_single.show_text(s.id);
	},
	show_box : function(sId,sAppCode) {
		var s=tryout_single.temp.opts[sId];
		
		zapjs.f.window_box({
			id : sId + 'tryout_single_showbox',
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
		zapjs.f.window_close(sId + 'tryout_single_showbox');
	},
	show_text : function(sId) {
		
		var sText = $('#zw_f_product_code').val();
		var val = $('#zw_f_product_name').val();
		
		$('#' + sId + '_show_code').html('');
		$('#' + sId + '_show_name').html('');
		
		$('#' + sId + '_show_code').html(sText);
		$('#' + sId + '_show_name').html(val);

	},

	result : function(sId, sValues, sTexts) {
		$('#zw_f_product_code').val(sValues);
		$('#zw_f_product_name').val(sTexts);
		$('#zw_f_product_link').val("goods_num:"+sValues);
		zapjs.f.window_close(sId + 'tryout_single_showbox');
		
		tryout_single.show_text(sId);

	},

};
if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/tryout_single", ["lib/easyui/locale/easyui-lang-zh_CN","zapadmin/js/tryout_chartajax"], function() {
		return tryout_single;
	});
}
