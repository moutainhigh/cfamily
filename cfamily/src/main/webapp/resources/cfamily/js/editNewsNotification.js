var editNewsNotification = {
	init : function() {
		$('#zw_f_notice_type').attr("disabled", true);
		zapjs.e('zapjs_e_zapjs_f_ajaxsubmit_submit', editNewsNotification.beforeSubmit);
	},
	beforeSubmit : function() {
		var  content  = $('#zw_f_notice_content').val();
		if(""==content.trim()){
			zapjs.f.message("消息通知内容不能为空");
			return  false;
		}
		return true;
	},
	appendElements: function(){

	},
	search:function(){
		
	},
	del : function(obj){
		
	},
	initNewsInfo : function(){
		
	}

};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/addColumn", function() {
		return addColumn;
	});
}
// 切换是否显示更多时使用
$(document)
		.ready(
				function() {
				});