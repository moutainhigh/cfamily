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
    <link href="../../resources/dlq/css/style.css" rel="stylesheet">
    <script src="../../resources/dlq/js/jquery-1.11.1.min.js"></script>
    <script src="../../resources/cfamily/js/lazyLoadImg.js"></script>
    <script src="../../resources/dlq/js/swipe.js"></script>
    <script type="text/javascript" src="../../resources/cfamily/zzz_js/ppth_todaysales_new_one/tost.js"></script>
    <script src="../../resources/cfamily/zzz_js/prerogative/js/shareGoodsDetail.js"></script>
    <script src="../../resources/dlq/dlq_page.js"></script>
    <script type="text/javascript">
    	dlq_page.init();
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
		var type = load();
		if(type == "ios") {
			return pageIsRefresh;
		} else if(type == "android"){
			try {
				window.share.isNeedPullRefresh(pageIsRefresh);
			} catch (e) {}
		}
        
	}}
	function load() {
			
		var thisOS = navigator.platform;
		
		var browser = { 
			versions: function() { 
				var u = navigator.userAgent, app = navigator.appVersion; 
				return {//移动终端浏览器版本信息  
					trident: u.indexOf('Trident') > -1, //IE内核 
					presto: u.indexOf('Presto') > -1, //opera内核 
					webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核 
					gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核 
					mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端 
					ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端 
					android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器 
					iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器 
					iPad: u.indexOf('iPad') > -1, //是否iPad 
					webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部 
				}; 
			}(), 
			language: (navigator.browserLanguage || navigator.language).toLowerCase() 
		};
	
		if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
			return "ios";
		} 
		else if (browser.versions.android) {
			return "android";
		}
	}
	var pageIsRefresh = true;//定义页面是否需要刷新
	isRefresh(pageIsRefresh);
	$("*").css("width",document.body.clientWidth);
</script>
</html>

