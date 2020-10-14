var couponTypeLimit_product_select = {

	temp : {
		opts : {},
		checkdata : {}

	},

	init : function(options) {

		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:true,
			callback:'parent.couponTypeLimit_product_select.result'
		};

		var s = $.extend({}, defaults, options || {});

		couponTypeLimit_product_select.temp.opts[s.id] = s;

		if(s.buttonflag)
		{
			$('#' + s.id+"_show_text").parent().append('<div class="w_left"><input class="btn" type="button" value="选择商品" onclick="couponTypeLimit_product_select.show_box(\'' + s.id + '\')" /></div>');
			$('#' + s.id+"_show_text").parent().append('<div class="w_left">&nbsp;&nbsp;<input class="btn" type="button" value="导   入" onclick="couponTypeLimit_product_select.import_product_box(\'' + s.id + '\')" /></div>');
			$('#' + s.id+"_show_text").parent().append('<div class="w_left w_w_70p"><ul class="zab_js_couponTypeLimit_product_select_ul" id="' + s.id + '_show_ul"></ul></div><div class="w_clear"></div>');
		}
		couponTypeLimit_product_select.show_text(s.id);
	},
	show_box : function(sId) {
		var s=couponTypeLimit_product_select.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'couponTypeLimit_product_select_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	//弹窗导入商品excel
	import_product_box : function(sId) {
		var s=couponTypeLimit_product_select.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'couponTypeLimit_import_product',
			content : '<iframe src="../show/page_v_oc_coupon_type_new3_cfamily_import_product?zw_s_iframe_select_source=' + sId 
			+ '&zw_s_iframe_select_page=' + s.source
			+ '&zw_s_iframe_max_select='+s.max
//			+ '&zw_s_iframe_select_callback=parent.couponTypeLimit_product_select.importProductResult'
			+'" frameborder="0" style="width:100%;height:500px;"></iframe>',
			width : '700',
			height : '550'
		});

	},	
	
	//couponTypeLimit_importProduct.ftl导入excel
	//商品后回调检查与现有表格内商品是否重复
	checkProductSame : function(newProductCode) {
		var rows = $('#productTable').datagrid("getRows");
		for ( var int = 0; int < rows.length; int++) {
			if(newProductCode == rows[int].productCode) {
				return true;
			}
		}
		return false;
	},
	
	//向商品表增加excel中导入的非重复数据
	addProductRow : function(productCode, productName) {
		var productObj = { "productCode":productCode, "productName":productName};
		$('#productTable').datagrid("appendRow", productObj);
	},
	
	//向商品表增加excel中导入的非重复数据后,关闭导入窗口
	close_import_product_win:function(msg) {
		var rows = $('#productTable').datagrid("getRows");
		 var productCodes = "";
		 for ( var int = 0; int < rows.length; int++) {
			 productCodes += rows[int].productCode+",";
		}
		if (productCodes.length > 0) {
			productCodes = productCodes.substr(0, productCodes.length - 1);
		}
		$('#zw_f_product_codes').val(productCodes);
		
		zapjs.f.window_close('zw_f_product_codescouponTypeLimit_import_product');
		if(msg!='') {
			zapadmin.model_message('导入限制商品的失败记录:<br>' + msg);
		}
	},
	
	close_box:function(sId)
	{
		zapjs.f.window_close(sId + 'couponTypeLimit_product_select_showbox');
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

		zapjs.f.window_close(sId + 'couponTypeLimit_product_select_showbox');

		couponTypeLimit_product_select.show_text(sId);
		
		var obj = {};
		obj.productCodeArr = sValues;
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForCouponLimitProductBaseInfo',obj,function(result) {
			if(result.resultCode==1){
				couponTypeLimitNew3.temp.productJson = result.productInfoList;
				couponTypeLimitNew3.productTable_init();
			}else{
				zapadmin.model_message('添加商品失败');
			}
		});


	}
};

if ( typeof define === "function" && define.amd) {
	define("cfamily/js/couponTypeLimit_product_select", ["lib/easyui/locale/easyui-lang-zh_CN"], function() {
		return couponTypeLimit_product_select;
	});
}
