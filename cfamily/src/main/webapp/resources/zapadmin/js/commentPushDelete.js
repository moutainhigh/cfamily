var commentPushDelete ={	
		// 删除函数调用
		func_delete : function(oElm, sUid, status) {
			var sModel = '<div id="zapjs_f_id_modal_message" ></div>';
			$(document.body).append(sModel);
			$('#zapjs_f_id_modal_message').html('<div class="w_p_20">您确认删除该条数据吗？</div>');
			var aButtons = [];
			aButtons.push({
				text : '是',
				handler : function() {
						$('#zapjs_f_id_modal_message').dialog('close');
						$('#zapjs_f_id_modal_message').remove();
						if(status=="4497465000070002"){
							zapjs.zw.modal_show({
								content : "已推送消息不能删除！"
							});
						}else{
							$.getJSON("../func/93cc214143ac11e49fed005056925439?zw_f_uid="+sUid, function(o){
								if (o.resultCode == "1") {
									if (o.resultMessage == "") {
										o.resultMessage = "操作成功";
									}
									zapjs.zw.modal_show({
										content : o.resultMessage,
										okfunc : 'zapjs.f.autorefresh()'
									});

								} else {
									zapjs.zw.modal_show({
										content : o.resultMessage
									});
								}
							});
						}
						
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
		}
};
if ( typeof define === "function" && define.amd) {
	define("zapadmin/js/commentPushDelete", [], function() {
		return commentPushDelete;
	});
}