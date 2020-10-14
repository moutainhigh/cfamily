//订单支付完成后的页面
var ordersucceed = {
	init : function() {
		//支付状态
		var trade_status = zapbase.up_urlparam("trade_status");
		//订单编号
		var order_code = zapbase.up_urlparam("out_trade_no");
		$("#outTradeNoId").val(order_code);
		$("#tradeStatus").val(trade_status);
		//预计返利金额
		var toRebateMoney = "";
		//订单金额
		var orderMoney = "";
		//是不是新用户
		var isNewUser = "";
		//支付方式
		var payType = "";
		var pageType = "";
		var token = '';
		var c = zapbase.up_urlparam("c");
		if(c==undefined||c=='undefined'||c=='') {
			token = zapbase.cookie("token");
		} else {
			token = c;
			zapbase.cookie("token", c);
		}
		capi.call_api({
			target : 'com_cmall_familyhas_api_ApiOrderDetails',
			params : {'order_code':order_code},
			callback : function(result) {
				if(result.resultCode == 1) {
					toRebateMoney = result.cashBackMoney;
					orderMoney = result.order_money;
					isNewUser = result.newuserFlag;
					payType = result.pay_type;
					pageType = ordersucceed.judgeUserOrderType(isNewUser, payType);
					ordersucceed.initPageView(pageType);
					$("#orderNoId").html(order_code);
					$("#actualMoneyId").html("￥"+orderMoney);
					$("#toRebateMoneyId").html("￥"+toRebateMoney);
					$("#amount").val(orderMoney);
					//获取商品编号
					var productCode = ordersucceed.getProductCode(result.orderSellerList);
					$("#productCodeId").val(productCode);
					ordersucceed.setBackButtonHref(productCode);
				}
				if(trade_status == "TRADE_SUCCESS" || trade_status == "TRADE_FINISHED") {
					console.log("ok");
				} else {
					pageType = 'userPayError';
				}
				ordersucceed.initPageView(pageType);
			},
			token : token
		});
	},
	//初始化页面显示
	initPageView : function(pagetype) {
		if(!pagetype || pagetype == 'newUserNotPay') { //新用户货到付款
			console.log("新用户货到付款");
		} else if(pagetype == 'newUserPayed') { //新用户已支付
			$("#pageTitleId").html("支付成功");
			$("#payTypeTextId").html("支付成功！");
		} else if(pagetype == 'userNotPay') { //已注册用户货到付款
			$("#setPasswdDivId").hide();
			$("#bodyDivId").removeClass("pay-ok2");
		} else if(pagetype == 'userPayed') { //已注册用户已支付
			$("#pageTitleId").html("支付成功");
			$("#payTypeTextId").html("支付成功！");
			$("#setPasswdDivId").hide();
			$("#bodyDivId").removeClass("pay-ok2");
		} else if(pagetype == 'userPayError') { //支付失败
			$("#setPasswdDivId").hide();
			$("#bodyDivId").removeClass("pay-ok2");
			$("#bodyDivId").removeClass("pay-ok");
			$("#pageTitleId").html("支付失败");
			$("#payTypeTextId").html("支付失败！");
			$("#orderOperateId").html("继续支付");
			$("#orderOperateId").attr("href", "javascript:ordersucceed.goToPay();");
			var baseUri = $("#baseUri").val() + "img/bg-nodata2.png";
			$("#payStatusImgId").attr("src", baseUri);
		}
	},
	//设置密码
	saveNewUserPasswd : function() {
		var passwd = $("#newPassWdId").val();
		
		if(passwd.length <= 0){
			capi.show_message("请输入密码");
			return;
		}
		if(!validateRules.betweenLength(passwd, 6, 16) || validateRules.checkSpace(passwd)){
			capi.show_message("请输入6-16位字符且不能包含空格");
			return;
		}
		ordersucceed.savePasswd(passwd);
	},
	//修改密码
	savePasswd : function(passwd) {
		var token = '';
		var c = zapbase.up_urlparam("c");
		if(c==undefined||c=='undefined'||c=='') {
			token = zapbase.cookie("token");
		} else {
			zapbase.cookie("token", c);
			token = c;
		}
		capi.call_api({
			target : 'com_cmall_familyhas_api_ChangePasswordForHtml',
			params : {'new_password':passwd},
			callback : function(result) {
				if(result.resultCode == 1) {
					capi.show_message("密码设置成功");
					//跳到下载页面
					setTimeout(function() {document.location = 'downloadapp?showRebateMsg=1';}, 1000);
				}
			},
			token : token
		});
	},
	//失败订单继续支付
	goToPay : function() {
		window.location = '../../alipay/'+$("#outTradeNoId").val();
		//window.location = 'payagain.html?amount='+$('#amount').val()+'&orderCode='+$("#outTradeNoId").val()+"&tradeStatus="+$('#tradeStatus').val();
	},
	//判断用户、订单类型
	judgeUserOrderType : function(isNewUser, payType) {
		var pageType = "";
		if(isNewUser == '1') { //新用户
			//货到付款
			if(payType == '449716200002') {
				pageType = "newUserNotPay";
			} else {
				pageType = "newUserPayed";
			}
		} else { //老用户
			//货到付款
			if(payType == '449716200002') {
				pageType = "userNotPay";
			} else {
				pageType = "userPayed";
			}
		}
		return pageType;
	},
	//获取商品编号
	getProductCode : function(orderSellerDetails) {
		var productCode = "";
		if(orderSellerDetails && orderSellerDetails != null 
				&& orderSellerDetails.length > 0) {
			var productDetail = orderSellerDetails[0];
			productCode = productDetail.productCode;
		}
		return productCode;
	},
	//设置返回按钮链接
	setBackButtonHref : function(productCode) {
		var parentMobile = zapbase.cookie(capi.config.cookie_parent_mobile);
		$("#backHrefId").attr("href", "productdetail.html?pc="+productCode+"&pm="+parentMobile);
	},
	payfail: function() {
		ordersucceed.laoding_gif("show");
		$('.jt').click(function() {
			var backUrl = zapbase.cookie('backUrl');
			window.location = backUrl;
		});
		//支付状态
		var trade_status = zapbase.up_urlparam("trade_status");
		//订单编号
		var order_code = zapbase.up_urlparam("out_trade_no");
		var token = zapbase.cookie("token");
		capi.call_api({
			target : 'com_cmall_familyhas_api_ApiOrderDetails',
			params : {'order_code':order_code},
			callback : function(result) {
				if(result.resultCode == 1) {
					$("#orderNoId").html('订单编号：'+order_code);
					$("#actualMoneyId").html("实付金额：￥"+result.order_money);
					$("#toRebateMoneyId").html("预计返利：￥"+result.cashBackMoney);
					$("#btn").click(function() {
						window.location = 'payagain.html?order_code='+order_code+'&order_money='+result.order_money+'&token='+token;
					});
				} else {
					capi.show_message(data.resultMessage);
				}
				ordersucceed.laoding_gif("hide");
			},
			token : token
		});
	},
	payagain: function() {
		ordersucceed.laoding_gif("show");
		var order_code = zapbase.up_urlparam("order_code");
		var order_money = zapbase.up_urlparam("order_money");
		$('#order_money').html('<b>￥</b>'+order_money);
		var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == "micromessenger") {//微信
			$('.lid:eq(1)').show().find('a').addClass('on');
		} else {//支付宝
			$('.lid:eq(0)').show().find('a').addClass('on');
		}
		$('.jt').click(function() {
			var msg = 'TRADE_ERROR';
			window.location = 'payfail.html?trade_status='+msg+'&out_trade_no='+order_code;
		});
		
		$("#btn").click(function() {
			if($.trim(order_code)=='') {
				return false;
			}
			if(ua.match(/MicroMessenger/i) == "micromessenger") {//微信
//				window.location = cfamily.wxConfig(order_code);
				cfamily.call_api("get_wx_pay_info", {'merchantid':'ichsywap', 'tradetype':'oauth', 'orderno':'b', 'TradeCode':'WeiXin', 'channelid':'6', 'CallBackURL':'http://api-family.syapi.ichsy.com/cfamily/wechatWAPNet?orderCode='+order_code}, function(da) {
					if(da.resultCode!=1) {
						capi.show_message(da.resultMessage);
					} else {
						var url = cfamily.wxConfig_test(data.order_code, da.mac, da.tradetime);
						window.location = url;
					}
				});
			} else {//支付宝
				window.location = '../../alipay/'+order_code;
			}
		});
		ordersucceed.laoding_gif("hide");
	},
	laoding_gif : function(activityId){
		if(activityId){
			if(activityId == "show"){
				$('#mask').css({'display':'block'});
				$('#waiting_id').show();
			}else if(activityId == "hide"){
				$('#mask').css('display','none');
				$('#waiting_id').hide();
			}
		}
	}
};