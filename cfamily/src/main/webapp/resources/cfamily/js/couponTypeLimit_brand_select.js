var couponTypeLimit_brand_select = {

	temp : {
		opts : {},
		checkdata : {}

	},

	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:true,
			callback:'parent.couponTypeLimit_brand_select.result'
		};

		var s = $.extend({}, defaults, options || {});

		couponTypeLimit_brand_select.temp.opts[s.id] = s;

		if(s.buttonflag)
		{
			$('#' + s.id+"_show_text").parent().append('<div class="w_left"><input class="btn" type="button" value="选择品牌" onclick="couponTypeLimit_brand_select.show_box(\'' + s.id + '\')" /></div>');
		
			$('#' + s.id+"_show_text").parent().append('<div class="w_left w_w_70p"><ul class="zab_js_couponTypeLimit_brand_select_ul" id="' + s.id + '_show_ul"></ul></div><div class="w_clear"></div>');
		}
		couponTypeLimit_brand_select.show_text(s.id);
	},
	show_box : function(sId) {
		var s=couponTypeLimit_brand_select.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'couponTypeLimit_brand_select_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	close_box:function(sId)
	{
		zapjs.f.window_close(sId + 'couponTypeLimit_brand_select_showbox');
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

		zapjs.f.window_close(sId + 'couponTypeLimit_brand_select_showbox');

		couponTypeLimit_brand_select.show_text(sId);
		
		var obj = {};
		obj.brandCodeArr = sValues;
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForCouponLimitBrandBaseInfo',obj,function(result) {
			if(result.resultCode==1){
				couponTypeLimitNew3.temp.brandJson = result.brandInfoList;
				couponTypeLimitNew3.brandTable_init();
			}else{
				zapadmin.model_message('添加品牌失败');
			}
		});


	}
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/couponTypeLimit_brand_select", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
		return couponTypeLimit_brand_select;
	});
}
