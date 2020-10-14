var love520 = {
		receiveImmediate:function(){
			var mobile = $("#mobile").val();
			if(mobile == null || mobile == ""){
				alert("请输入手机号");
				return;
			}
			var checkMobile = new RegExp("^(1)[0-9]{10}$").test(mobile);
			if (checkMobile) {
				love520.callApi(mobile);
			}else{
				alert("手机号码输入错误");
			}
		},
		callApi : function(mobile){
			var opt ={};
			opt.mobile = mobile;
			love520.api_call('com_cmall_ordercenter_service_api_ApiSaveCouponsUser',opt,function(result) {
				var resultCode = result.resultCode;
				if(resultCode == "1"){
					alert("领取成功");
				}else if(resultCode == "939301303"){
					alert("亲，您已经领取了优惠券~");
				}else if(resultCode == "939301314"){
					alert("活动未开始");
				}else if(resultCode == "939301301"){
					alert("活动已结束");
				}else if(resultCode == "939301302"){
					alert("亲,来晚了,优惠券已经领取完~");
				}else{
					alert("领取失败");
				}
				openApp(-1);
			});
		},
		api_call : function(sTarget, oData, fCallBack) {

			//判断如果传入了oData则自动拼接 否则无所只传入key认证
			var defaults = oData?{
				api_target : sTarget,
				api_input : zapjs.f.tojson(oData),
				api_key : 'jsapi'
			}:{api_key : 'jsapi',api_input:''};
			
			//oData = $.extend({}, defaults, oData || {});

			zapjs.f.ajaxjson("../jsonapi/" + sTarget, defaults, function(data) {
				//fCallBack(data);			
				fCallBack(data);
			});
		}
};
if (typeof define === "function" && define.amd) {
	define("cfamily/js/love520",function() {
		return love520;
	});
}
