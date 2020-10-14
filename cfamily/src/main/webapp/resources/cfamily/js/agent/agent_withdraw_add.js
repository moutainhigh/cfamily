var agent_withdraw_add = {
	
	init_page:function (){
		
		$('#zw_f_member_code,#zw_f_nickname,#zw_f_real_money,#zw_f_login_name').attr("readonly","readonly");
		
	},
	
	sendMessage:function (){
		agent_withdraw_add.message("check");
	},
	
	
	
	show_windows : function(){
			zapjs.f.window_box({
				id : 'as_widthdraw',
				content : '<iframe src="../show/page_chart_v_v_fh_agent_member_info?zw_s_iframe_select_source=as_widthdraw&zw_s_iframe_select_page=page_chart_v_v_fh_agent_member_info&zw_s_iframe_max_select=1&zw_s_iframe_select_callback=parent.agent_withdraw_add.addcb" frameborder="0" style="width:100%;height:600px;"></iframe>',
				width : '800',
				height : '650'
			});
	},
	
	addcb : function(sId,sVal,sText,b){
		debugger;
		var obj = {};
		obj.member_code = sVal;
		zapjs.f.ajaxjson("../func/aef30b87464447a7ab090e245dd20a67", obj, function(data) {
			debugger;
			if(data.resultCode == 1){
				$("#zw_f_member_code").val(sVal);
				$("#zw_f_nickname").val(sText);
				$("#zw_f_real_money").val(data.resultObject.real_money);
				$("#zw_f_login_name").val(data.resultObject.login_name);
				$("#zw_f_withdraw_money").val(data.resultObject.real_money);
				zapjs.f.window_close(sId);
			}else{
				alert(data.resultMessage);
			}
			
		});
	}
	
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/agent/agent_withdraw_add",["zapjs/zapjs","zapjs/zapjs.zw","zapadmin/js/zapadmin_single"],function() {
		return agent_withdraw_add;
	});
}
