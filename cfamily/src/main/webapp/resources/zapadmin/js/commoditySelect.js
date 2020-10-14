var commoditySelect = {

	temp : {
		opts : {},
		checkdata : {}

	},

	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:true,
			callback:'parent.commoditySelect.result'
		};

		var s = $.extend({}, defaults, options || {});

		commoditySelect.temp.opts[s.id] = s;
		
		commoditySelect.show_text(s.id);
	},
	show_box : function(sId) {
		var s=commoditySelect.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'commoditySelect_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select=1&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	close_box:function(sId)
	{
		zapjs.f.window_close(sId + 'commoditySelect_showbox');
	},
	show_text : function(sId) {


	},

	result : function(sId, sValues, sTexts) {
		zapjs.f.window_close(sId + 'commoditySelect_showbox');
	},

};
if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/commoditySelect", ["lib/easyui/locale/easyui-lang-zh_CN","zapadmin/js/qrCode_chartajax"], function() {
		return commoditySelect;
	});
}
