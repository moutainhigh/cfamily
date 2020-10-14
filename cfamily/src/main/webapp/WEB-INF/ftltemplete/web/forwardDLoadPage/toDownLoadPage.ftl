<!DOCTYPE >
<html>
<head>
    <meta charset="utf-8">
    <title>惠家有app下载页</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    
<script src="../../resources/lib/jquery/jquery-last.min.js"></script>
<script src="../../resources/lib/jquery/jquery-plugins-zap.min.js"></script>
<script src="../../resources/zapjs/zapjs.js"></script>
<script src="../../resources/zapjs/zapjs.zw.js"></script>
<script >
	$(function(){
		var uqcode = up_urlparam("id","");
			
		var sTarget = 'com_cmall_groupcenter_recommend_api_ApiClickLinkToDowndLoadPage';
		var oData = {id:uqcode};
		var defaults = oData?{
			api_target : sTarget,
			api_input : zapjs.f.tojson(oData),
			api_key : 'betafamilyhas'
		}:{api_key : 'betafamilyhas',api_input:''};
		var turl = document.location.href.toString();
		zapjs.f.ajaxjson(turl.substring(0,turl.indexOf('/web')) +'/jsonapi/'+ sTarget, defaults, function(data) {
			if (data.resultCode == "1") {
				window.location.href="http://www.ichsy.cn/apps/";
			}
			
		});
		//获取参数
		function up_urlparam(key,sUrl) {
			var sReturn = "";
			if (!sUrl) {
				sUrl = window.location.href;
				if (sUrl.indexOf('?') < 1) {
					sUrl = sUrl + "?";
				}
			}
	
			var sParams = sUrl.split('?')[1].split('&');
	
			for (var i = 0, j = sParams.length; i < j; i++) {
	
				var sKv = sParams[i].split("=");
				if (sKv[0] == key) {
					sReturn = sKv[1];
					break;
				}
			}
			return sReturn;
		}
	});
</script>
</head>
<body>
</body>
</html>