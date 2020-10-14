var ldmsgpayproduct ={
	temp : {
		opts : {},
		checkdata : {}

	},
	init:function (options){
		ldmsgpayproduct.init_value();
		
		var defaults = {
			pagecode : '',
			id : '',
			buttonflag:false,
			callback:'parent.eventItemProduct_select.close_box'
		};

		var s = $.extend({}, defaults, options || {});

		ldmsgpayproduct.temp.opts[s.id] = s;
	},
	
	init_value:function (){
		   var uniformPriceName = $(".zab_info_page .control-label").eq(-3).html();
		   var index=0;
		   var uniformPrice='';
		   if(uniformPriceName=='价格统一:'){
			   index=-3;
		   }else{
			   index=-5;
		   }
		   var uniformPrice = $(".zab_info_page .control_book").eq(index).html();
		   if(uniformPrice && uniformPrice.indexOf('449746880001') > -1){
			   $(".zab_info_page .control_book").eq(index).html("是");
		   }else{
			   $(".zab_info_page .control_book").eq(index).html("否");
		   }
	},
	
	show_box : function(sId) {
		var s=ldmsgpayproduct.temp.opts[sId];
		zapjs.f.window_box({
			id : sId + 'ldmsgpayproduct_showbox',
			content : '<iframe src="../show/'+s.source+'?zw_f_seller_code='+s.seller_code+'&zw_s_iframe_select_source=' + sId + '&zw_s_iframe_select_page=' + s.source + '&zw_s_iframe_max_select='+s.max+'&zw_s_iframe_select_callback='+s.callback+'" frameborder="0" style="width:100%;height:500px;"></iframe>',

			width : '700',
			height : '550'
		});

	},
	close_box:function(sId)
	{
		zapjs.f.window_close(sId + 'ldmsgpayproduct_showbox');
	},
	
	init_delete:function(){
		location.reload();
	},
  	// 批量作废其他 
	batchUpdateFlagOther: function() {
		var codes = [];
		$("tbody input[name='delCheck']").each(function(){
			 if(this.checked){
				 codes.push($(this).attr("data-product"));
			 }
		})
		
		if(codes.length <= 0){
			zapjs.f.message("你还没有选中任何栏目内容！");
			return false;
		}
		if(confirm("确定要批量作废其它商品吗？")){
			getEventStatus(8,null,"",codes.join(","));
		}  	
   }
};