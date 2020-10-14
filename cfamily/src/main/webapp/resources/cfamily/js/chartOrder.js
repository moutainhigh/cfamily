chartOrder = {
		
		func_closeTrading:function(oElm, sUid,sEndTime){
			var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
			$(document.body).append(sModel);
			$('#zapjs_f_id_modal_message').html('<div class="w_p_20">您确认关闭该订单吗？</div>');
			var aButtons = [];
			aButtons.push({
				text : '是',
				handler : function() {
						$('#zapjs_f_id_modal_message').dialog('close');
						$('#zapjs_f_id_modal_message').remove();
						zapjs.zw.func_do(oElm, null, {zw_f_uid : sUid});
					}
			},{
				text : '否',
				handler : function() {
						$('#zapjs_f_id_modal_message').dialog('close');
						$('#zapjs_f_id_modal_message').remove();
					}
			});
			
			$('#zapjs_f_id_modal_message').dialog({
				title : '提示消息',
				width : '400',
				resizable : true,
				closed : false,
				cache : false,
				modal : true,
				buttons : aButtons
			});
		},
		func_liandan:function(oElm, order_code,sEndTime){
			var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
			$(document.body).append(sModel);
			$('#zapjs_f_id_modal_message').html('<div class="w_p_20">您确认联单吗？</div>');
			var aButtons = [];
			aButtons.push({
				text : '是',
				handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
					zapjs.zw.func_do(oElm, null, {zw_f_order_code : order_code});
				}
			},{
				text : '否',
				handler : function() {
					$('#zapjs_f_id_modal_message').dialog('close');
					$('#zapjs_f_id_modal_message').remove();
				}
			});
			
			$('#zapjs_f_id_modal_message').dialog({
				title : '提示消息',
				width : '400',
				resizable : true,
				closed : false,
				cache : false,
				modal : true,
				buttons : aButtons
			});
		},
		
};
