var seller_select = {

	temp : {
		opts : {},
		checkdata : {}

	},

	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:true,
			callback:'parent.seller_select.result'
		};

		var s = $.extend({}, defaults, options || {});

		seller_select.temp.opts[s.id] = s;

		if(s.buttonflag)
		{
			$('#' + s.id+"_show_text").parent().append('<div class="w_left"><input class="btn" type="button" value="选择商户" onclick="seller_select.show_box(\'' + s.id + '\')" /></div>');
		
			$('#' + s.id+"_show_text").parent().append('<div class="w_left w_w_70p"><ul class="zab_js_seller_select_ul" id="' + s.id + '_show_ul"></ul></div><div class="w_clear"></div>');
		}
		seller_select.show_text(s.id);
	},
	show_box : function(sId) {
		var s=seller_select.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'seller_select_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	close_box:function(sId)
	{
		zapjs.f.window_close(sId + 'seller_select_showbox');
	},
	show_text : function(sId) {

//		var sText = $('#' + sId + "_show_text").val();
//		$('#' + sId + '_show_ul').html('');

//		if (sText) {
//			var aT = sText.split(',');
//			for (var i in aT) {
//				$('#' + sId + '_show_ul').append('<li>' + aT[i] + '</li>');
//			}
//
//		}

	},

	result : function(sId, sValues, sTexts) {

		$('#' + sId).val(sValues);
//		$('#' + sId + "_show_text").val(sTexts);

		zapjs.f.window_close(sId + 'seller_select_showbox');

		seller_select.show_text(sId);
		
		var obj = {};
		obj.smallSellerCodes = sValues;
		zapjs.zw.api_call('com_cmall_usercenter_service_api_ApiGetSellerName',obj,function(result) {
			if(result.resultCode==1){
				reminder.temp.sellerJson = result.sellerList;
				reminder.sellerTable_init();
			}else{
				zapadmin.model_message('添加商户失败');
			}
		});


	}
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/seller_select", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
		return seller_select;
	});
}
