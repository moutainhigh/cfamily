<script type="text/javascript">
var appid = '${appid}';
var timestamp = '${timestamp}';
var nonce_str = '${nonce_str}';
var prepay_id = '${prepay_id}';
var sign = '${sign}';
var orderCode = '${orderCode}';
function onBridgeReady() {
   	WeixinJSBridge.invoke('getBrandWCPayRequest', {
		"appId": appid,
		"timeStamp": timestamp,
		"nonceStr": nonce_str,
		"package": "prepay_id="+prepay_id,
		"signType": "MD5",
		"paySign": sign
	}, function(res) {
		var msg = res.err_msg;
		msg = msg.substring(msg.lastIndexOf(":")+1, msg.length);
		if(msg=='ok') {
			msg = 'TRADE_SUCCESS';
			msg = 'ordersucceed.html?trade_status='+msg+'&out_trade_no='+orderCode;
			window.location = './web/shareshopping/'+msg;
		} else {
			//msg = 'TRADE_ERROR';
			//msg = 'payfail.html?trade_status='+msg+'&out_trade_no='+orderCode;
			window.location = './web/shareshopping/pay_redirect.html';
		}
	});
}
if (typeof WeixinJSBridge == "undefined"){
   if( document.addEventListener ){
       document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
   }else if (document.attachEvent){
       document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
       document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
   }
}else{
   onBridgeReady();
}
</script>