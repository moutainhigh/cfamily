var addMessage= {
		init_page:function (){
			$('.form-horizontal').prepend('<div class="control-group"><label class="control-label" for="zw_f_order_code"><span class="w_regex_need">*</span>订单编号：</label><div class="controls"><input type="text" id="zw_f_order_code" name="zw_f_order_code" zapweb_attr_regex_id="469923180002" value=""></div></div>');
			$('.form-horizontal').prepend('<div class="control-group" id="small_seller_code"><label class="control-label" for="zw_f_small_seller_code"><span class="w_regex_need">*</span>商户编号：</label><div class="controls"><input type="text" id="zw_f_small_seller_code" name="zw_f_small_seller_code" zapweb_attr_regex_id="469923180002" value=""></div></div>');
			$('#zw_f_order_code').after('&nbsp;<input class="btn" type="button" value="选择" onclick="addMessage.show_windows_order_code()">');
			$('#zw_f_msg_content').after('&nbsp;<input class="btn" type="button" value="选择" onclick="addMessage.show_windows_address()">');
			$('#zw_f_msg_content').attr("readonly","readonly");
			$('#zw_f_order_code').attr("readonly","readonly");
			$('#small_seller_code').hide();
		},
	show_windows_order_code : function(){
		zapjs.f.window_box({
			id : 'as_return_goods',
			content : '<iframe src="../show/page_chart_v_cf_sellororder_manage_cc?zw_s_iframe_select_source=as_return_goods&zw_s_iframe_select_page=page_chart_v_cf_sellororder_manage_cc&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.addMessage.order_choose" frameborder="0" style="width:100%;height:600px;"></iframe>',
			width : '800',
			height : '650'
		});
	},
	//
	show_windows_address : function(){
		var small_seller_code=$('#zw_f_small_seller_code').val();
		zapjs.f.window_box({
			id : 'as_address_info',
			content : '<iframe src="../show/page_chart_vv_oc_address_info?zw_s_iframe_select_source=as_address_info&zw_s_iframe_select_page=page_chart_vv_oc_address_info&zw_s_iframe_max_select=1&zw_f_small_seller_code='+small_seller_code+'&zw_s_iframe_select_callback=parent.addMessage.address_choose" frameborder="0" style="width:100%;height:600px;"></iframe>',
			width : '800',
			height : '650'
		});
	},
	
   order_choose : function(sId,va, sVal){
	 var obj = {};
	 obj.order_code = sVal;
	 zapjs.zw.api_call('com_cmall_familyhas_api_ApiForSmallSellerCode',obj,function(result) {
			if(result.resultCode==1){
				$("#zw_f_small_seller_code").val(result.small_seller_code);
				$("#zw_f_msg_receive").val(result.buyer_mobile);
			  }
			})
	 
			$("#zw_f_order_code").val(sVal);
			zapjs.f.window_close(sId);
	},
	
	address_choose : function(sId,va, sVal){
		 var obj = {};
		 obj.msg_content = sVal;
		 var order_code=$('#zw_f_order_code').val();
		 zapjs.zw.api_call('com_cmall_familyhas_api_ApiForAfterPersonInfo',obj,function(result) {
				if(result.resultCode==1){
					$("#zw_f_msg_content").val("尊敬的用户,您的订单"+order_code+",售后信息是：售后联系人："+result.after_sale_person+",售后联系电话："+result.after_sale_mobile+",售后地址："+sVal);
				  }
				})
				
				zapjs.f.window_close(sId);
		}
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/addMessage",["zapjs/zapjs","zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return addMessage;
	});
}
