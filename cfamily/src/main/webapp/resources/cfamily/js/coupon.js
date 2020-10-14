var coupon = {
		temp : {
			browser : "",	//打开惠家有客户端
		},
		init : function(mobile){
			var opt ={};
			opt.mobile = mobile;
			coupon.api_call('com_cmall_ordercenter_service_api_ApiSaveCouponsUser',opt,function(result) {
				var resultCode = result.resultCode;
				if(resultCode == "1" || resultCode == "939301303"){//抢了优惠券
					$("#bd1").attr("class","bg1");
					$("#fe").attr("class","ft1");
					$("#bd1").append(
					'<h1>家有购物官方APP活动</h1><img src="../resources/images/bg1-1.jpg" alt=""/><div class="ft1"><img src="../resources/images/bg1-1-1.jpg" alt="商品列表标题"/><a id="ichsy_openingApp" val="3" href="javascript:openApp(11);" class="btn-buy">打开应用，查看现金大礼包</a><div><span>注：</span><p>领取人数较多时，发送会略有延迟，望请谅解哦~</p></div></div><div class="pro"><a ><img src="../resources/images/pro1.jpg" alt="" /></a><a ><img src="../resources/images/pro2.jpg" alt="" /></a><a ><img src="../resources/images/pro3.jpg" alt="" /></a><a><img src="../resources/images/pro4.jpg" alt="" /></a><p>更多优惠商品，详见惠家有APP</p></div>');
				}else {
					$("#bd1").attr("class","bg2");
					$("#bd1").append('<img src="../resources/images/bg2_1.jpg" alt=""/><footer class="ft2"><a id="ichsy_openingApp" val="3" href="javascript:openApp(-1);" class="btn-buy">打开应用，看看其他优惠</a></footer>');
				}
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
	define("cfamily/js/coupon",function() {
		return coupon;
	});
}
