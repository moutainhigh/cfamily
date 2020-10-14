var securityProduct_single = {

	temp : {
		opts : {},
		checkdata : {}

	},

	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:true,
			callback:'parent.securityProduct_single.result'
		};

		var s = $.extend({}, defaults, options || {});

		securityProduct_single.temp.opts[s.id] = s;
		
		securityProduct_single.show_text(s.id);
	},
	show_box : function(sId) {
		var s=securityProduct_single.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'securityProduct_single_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	close_box:function(sId)
	{
		zapjs.f.window_close(sId + 'securityProduct_single_showbox');
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

//		$('#' + sId).val(sValues);
		$("#zw_f_sku_name").val(sTexts);
		$("#zw_f_jump_position").val(sValues);
		zapjs.f.window_close(sId + 'securityProduct_single_showbox');
	},

};
if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/securityProduct_single", ["lib/easyui/locale/easyui-lang-zh_CN","zapadmin/js/security_chartajax"], function() {
		return securityProduct_single;
	});
}
