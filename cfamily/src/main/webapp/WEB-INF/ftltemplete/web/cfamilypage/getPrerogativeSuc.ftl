<!doctype html>
<html class="no-js">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>领取返现特权</title>
  <meta name="description" content="">
  <meta name="keywords" content="">
  <meta name="viewport"
        content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp"/>
  <!-- Add to homescreen for Safari on iOS -->
<!-- 添加至主屏, 从主屏进入会隐藏地址栏和状态栏, 全屏(content="yes") -->
<meta name="apple-mobile-web-app-capable" content="yes">
<!-- 系统顶栏的颜色 -->
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<!-- 页面图标 -->
  <link rel="icon" type="image/png" href="../../resources/cfamily/zzz_js/favicon.png">
<!-- 页面主样式 -->
  <link rel="stylesheet" href="../../resources/cfamily/zzz_js/app.css">

</head>
<body>
<script src="../../resources/lib/jquery/jquery-last.min.js"></script>
<script src="../../resources/lib/easyui/jquery.easyui.min.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/amazeui.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/handlebars.min.js"></script>
<script src="../../resources/cfamily/zzz_js/assets/js/amazeui.widgets.helper.js"></script>

<script language="javascript" type="text/javascript"> 
	$(function() {
	
		$('#down_hjy_app').click(function(){
			if(null == browser || "" == browser) {
				loadInit();
			}
			//在微博，微信，qq中打开
			if(isAbled_open()) {
				//show_tc_ico();
				document.location = "http://www.ichsy.cn/apps/";
			} else {
				//在微博，微信，qq中打开
				if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) { 
					//document.location = "../../resources/cfamily/html/openClientApp.html";
					document.location = "http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850";
				} else if (browser.versions.android) { 
					document.location = "http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850";
				} else {
					document.location = "http://www.ichsy.cn/apps/";
				}
				//close_tc_ico();
			}
				
		})
		
	})
	var browser='';
	function loadInit() {
	
		browser = { 
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
		}
	
		
	}
	function isAbled_open(){
		var ua = navigator.userAgent.toLowerCase();
		if(ua.indexOf("Weibo")>0 || ua.indexOf("MicroMessenger")>0 || ua.match(/MicroMessenger/i)=="micromessenger" || ua.indexOf("qq")>0 || ua.indexOf("QQ")>0){
			return false;
		} else {
			return true;
		}
	}
</script>

<!-- 内容 [[-->
<div class="am-padding-top e-bgc1 am-padding-bottom">
<p class="am-text-center "><span class="e-tc am-text-xl ">恭喜您已成为特权用户！</span><br>去“惠家有”购物可得到返现哦！</p>
</div>

<!-- .am-u-sm-centered 始终居中 -->
<#--
<div class="am-container">
  <div class="am-u-sm-7 am-u-sm-centered am-margin-top">
  <span class="am-text-xl am-text-center am-block">惠家有介绍</span>
          <p class="am-margin-top am-text-center spic"><img src="../../resources/cfamily/zzz_js/logo.jpg"></p>
          <ul><li>时尚便捷的一站式家庭购物商城；</li>
          <li>家有购物集团严格质检，100%正品保证；</li>
          <li>支持货到付款，全场包邮，无忧退货；</li>
          <li>每日爆款特价限时购，优惠不只一点点。</li></ul>
          
<br><button id="down_hjy_app" type="button" class="am-btn am-btn-default am-btn-block" >下载“惠家有APP”</button>
</div>
</div>

-->


<div class="am-container">
  <div class="am-u-sm-9 am-u-sm-centered am-margin-top">
  <span class="am-text-lg am-text-center am-block">惠家有介绍</span>
  <div class="am-u-sm-5 am-u-sm-centered "><img src="../../resources/cfamily/zzz_js/logo.jpg"></div>
  </div>

<div class="am-u-sm-11 am-u-sm-centered am-margin-top-sm"><ul>
          <li>时尚便捷的一站式家庭购物商城；</li>
          <li>家有购物集团严格质检,100%正品保证；</li>
          <li>支持货到付款，全场包邮，无忧退货；</li>
          <li>每日爆款特价限时购，优惠不只一点点。</li></ul>
          </div>
<div class="am-u-sm-11 am-u-sm-centered am-margin-top"><button id="down_hjy_app" type="button" class="am-btn am-btn-default am-btn-block am-btn-lg">下载“惠家有APP”</button></div>
</div>
<!-- 内容 ]]-->



</body>
</html>
