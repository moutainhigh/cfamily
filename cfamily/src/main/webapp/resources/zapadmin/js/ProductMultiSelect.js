var ProductMultiSelect = {

	temp : {
		opts : {},
		checkdata : {}

	},

	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:true,
			callback:'parent.ProductMultiSelect.result'
		};

		var s = $.extend({}, defaults, options || {});

		ProductMultiSelect.temp.opts[s.id] = s;
		
		ProductMultiSelect.show_text(s.id);
	},
	show_box : function(sId) {
		var s=ProductMultiSelect.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'ProductMultiSelect_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	close_box:function(sId)
	{
		zapjs.f.window_close(sId + 'ProductMultiSelect_showbox');
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
		$("#keyValueList").val(sValues);
		$("#keyTextList").val(sTexts);
		zapjs.f.window_close(sId + 'ProductMultiSelect_showbox');
	},

};
if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/ProductMultiSelect", ["lib/easyui/locale/easyui-lang-zh_CN","zapadmin/js/qrCode_chartajax"], function() {
		return ProductMultiSelect;
	});
}
