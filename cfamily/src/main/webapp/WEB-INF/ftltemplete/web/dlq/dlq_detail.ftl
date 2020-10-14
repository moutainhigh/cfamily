<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
	<meta charset="UTF-8">
	<title></title>
	<link rel="icon" type="image/png" href="../../resources/cfamily/zzz_js/favicon.png">
	<#assign weiXinUrl=b_method.upConfig("cfamily.hjyWeiXinUrl") >
	<#assign wx_htp=b_method.upConfig("cfamily.hjyWeiXinHttp") >
    <#-- <link href="../../resources/dlq/css/style.css" rel="stylesheet" /> -->
    <link href="../../resources/dlq/css/style_add_01.css" rel="stylesheet" />
    <script src="../../resources/dlq/js/jquery-1.11.1.min.js"></script>
    <script src="../../resources/cfamily/js/lazyLoadImg.js"></script>
    <script src="../../resources/cfamily/zzz_js/prerogative/js/shareGoodsDetail.js"></script>
    <script type="text/javascript" src="../../resources/cfamily/zzz_js/ppth_todaysales_new_one/tost.js"></script>
    <script src="../../resources/dlq/dlq_detail.js"></script>
    <script type="text/javascript">
    	dlq_detail.init('${weiXinUrl}','${wx_htp}');
    </script>

</head>
<body> 

</body>
<script>
var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?4b558c865d49b9bedd272692d183c227";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();
</script>
<script type="text/javascript">
	 //是否在app端展示下拉刷新
	{function isRefresh(flag){
		loadInit();
		
		if(browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
			return pageIsRefresh;
		} else if(browser.versions.android){
			try {
				window.share.isNeedPullRefresh(pageIsRefresh);
			} catch (e) {}
		}
		
	}}
	var pageIsRefresh = true;//定义页面是否需要刷新
	isRefresh(pageIsRefresh);
	$("*").css("width",document.body.clientWidth);
</script>
<script>
/*
window.onload=function(){
	//自适应rem初始化设置
	function fontSize(){
		if(document.documentElement.clientWidth<1280){ //initial-scale=0.5是缩小一倍后适应屏幕宽。
			document.documentElement.style.fontSize = 10*(document.documentElement.clientWidth/320)+'px';
		}else{ 
			document.documentElement.style.fontSize='40px';
		}
	}
	fontSize();
	window.onresize=function(){fontSize();};
}*/
</script>
</html>