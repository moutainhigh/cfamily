var message_add = {
		
	init : function() {
		
		var zw_f_msg_receive=$("#zw_f_msg_receive");
		if(!zw_f_msg_receive){
			return ;
		}

		//初始化一个添加按钮
		zw_f_msg_receive.parent().append('<input class="btn btn-small" onclick="message_add.add(this)" type="button" value="添加">');
		
	},

	add : function (obj){
		
		
		var html='<div class="control-group">';
			html+='<label class="control-label" for="zw_f_msg_receive"><span class="w_regex_need"></span></label>';
			html+='<div class="controls">';
			html+='<input type="text" id="zw_f_msg_receive" name="zw_f_msg_receive" zapweb_attr_regex_id="469923180010" value="">';
			html+='<input class="btn btn-small" onclick="message_add.del(this)" type="button" value="删除">&nbsp;<input class="btn btn-small" onclick="message_add.add(this)" type="button" value="添加"></div>';
			html+='</div>';
		
		$(obj).parent().parent().after(html);
	},
	
	del : function (obj){
		
		$(obj).parent().parent().remove();
	}
	
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/message_add", function() {
		return message_add;
	});
}
