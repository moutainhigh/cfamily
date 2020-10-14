var merchantReset = {

	resetPwd : function() {//重置商户密码
		var userName = $("#userName").val();
		var opt ={};
		opt.userName = userName;
		zapjs.zw.api_call('com_cmall_familyhas_api_ApiForResetMerchantPwd', opt, function(result) {
			if(result.resultCode==1){
				zapadmin.model_message('重置密码成功');
			}else{
				zapadmin.model_message('重置密码失败');
			}
		});
	}
};

if (typeof define === "function" && define.amd) {
	define("cfamily/js/merchantReset",function() {
		return merchantReset;
	});
}