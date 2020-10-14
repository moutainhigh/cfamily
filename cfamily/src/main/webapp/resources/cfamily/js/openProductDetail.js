//打开惠家有客户端
var browser='';
function openApp(sendTp){
	if(null == browser || "" == browser) {
		loadInit();
	}
	(function() {
		var openUrl = "";
		try{
			if(sendTp == 1) {//商品详情页，获取商品编号
				if(browser.versions.android) {
					openUrl = openUrl+"params={\"ichsy_key\":\"1\",\"ichsy_value\":\""+up_urlparam("pc", '')+"\"}";
				} else {
					openUrl = openUrl+"ichsy_key=1&ichsy_value="+up_urlparam("pc", '');
				}
			} else if(sendTp == 2) {//专题页 ，获取url
				if(browser.versions.android) {
					openUrl = openUrl+"params={\"ichsy_key\":\"2\",\"ichsy_value\":\""+up_urlparam("link", '')+"\"}";
				} else {
					openUrl = openUrl+"ichsy_key=2&ichsy_value="+up_urlparam("link", '');
				}
			} else if(sendTp == 11) {//优惠券管理页面
				if(browser.versions.android) {
					openUrl = openUrl+"params={\"ichsy_key\":\"3\",\"ichsy_value\":\"3\"}";
				} else {
					openUrl = openUrl+"ichsy_key=3&ichsy_value=3";
				}
			} else {
				window.location = "http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850";
			}
		
		}catch(e){}
		var ua = navigator.userAgent.toLowerCase();
		if(refresh == 1) {
			if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
				window.open("https://appsto.re/cn/iR2qM.i");
			} else {
				window.open("http://www.ichsy.cn/apps/"); 
			}
		} else {
			if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
				if(ua.indexOf("MicroMessenger")>0|| ua.match(/MicroMessenger/i)=="micromessenger") {
					window.open("http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850");
				} else if(ua.indexOf("qq")>0 || ua.indexOf("QQ")>0) {
//					$("#open_app_ifr").attr("src","https://appsto.re/cn/iR2qM.i");
					hasApp("https://appsto.re/cn/iR2qM.i");
				} else {
					hasApp("huijiayou://huijiayou.com?"+openUrl);
				}
			}else if(browser.versions.android){
				if(ua.indexOf("MicroMessenger")>0|| ua.match(/MicroMessenger/i)=="micromessenger") {
					window.open("http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850");
				} else if(ua.indexOf("qq")>0 || ua.indexOf("QQ")>0){
					hasApp("huijiayou://huijiayou.com?"+openUrl+"#Intent;scheme=http;package=com.jiayou.qianheshengyun.app;end");
				} else {
					hasApp("huijiayou://huijiayou.com?"+openUrl+"#Intent;scheme=http;package=com.jiayou.qianheshengyun.app;end");
				}
			} else {
				window.open("http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850"); 
			}
			window.setTimeout(function () { 
				if (window.location.href.indexOf('?') < 1) {
					window.location.href += '?mobile=1&refresh=1';
				}else{
					window.location.href += '&refresh=1';
				}
			}, 500);
		}
	})(); 
}
//尝试下载连接
var refresh = up_urlparam("refresh", '');
if(refresh == 1) {
	var val = $('#ichsy_openingApp').attr('val');
	openApp(val);
}

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
function up_urlparam(sKey, sUrl) {
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
		if (sKv[0] == sKey) {
			sReturn = sKv[1];
			break;
		}
	}

	return sReturn;

}
function getPrerogative(){
	var pmobile = up_urlparam("pm", '');
	document.location = 'prerogative.html?pmobile='+pmobile;
}
function isAbled_open(){
	var ua = navigator.userAgent.toLowerCase();
	if(ua.indexOf("Weibo")>0 || ua.indexOf("MicroMessenger")>0|| ua.match(/MicroMessenger/i)=="micromessenger" || ua.indexOf("qq")>0 || ua.indexOf("QQ")>0){
		return false;
	} else {
		return true;
	}
}
function hasApp(url) {  
    var timeout, t = 1000, hasApp = true;  
    curnt1 = setTimeout(function () {  
    	if (!hasApp) {
//    		clearTimeout(curnt2);
//        	window.location = "http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850";
        }  
        document.body.removeChild(ifr);  
    }, 3000);
  
    var t1 = Date.now();  
    var ifr = document.createElement("iframe");  
    ifr.setAttribute('src', url);  
    ifr.setAttribute('style', 'display:none');  
    document.body.appendChild(ifr);
    timeout = setTimeout(function () {  
         var t2 = Date.now();  
         if (!t1 || t2 - t1 < t + 100) {  
             hasApp = false;  
         }  
    }, t);  
    
}  
//打开商品详情页
function opengoodsDetail(product_code) {
	var openUrl = "";
	if(null == browser || "" == browser) {
		loadInit();
	}
	if(browser.versions.android) {
		openUrl = openUrl+"params={\"ichsy_key\":\"1\",\"ichsy_value\":\""+product_code+"\"}";
	} else {
		openUrl = openUrl+"ichsy_key=1&ichsy_value="+product_code;
	}
	
	openUrlFun(openUrl);
}
function openUrlFun(openUrl) { 
	
	var ua1 = navigator.userAgent.toLowerCase();
	if(refresh == 1) {
		if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
			window.open("https://appsto.re/cn/iR2qM.i");
		} else {
			window.open("http://www.ichsy.cn/apps/"); 
		}
	} else {
		if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
			if(ua1.indexOf("MicroMessenger")>0|| ua1.match(/MicroMessenger/i)=="micromessenger") {
				window.open("http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850");
			} else if(ua1.indexOf("qq")>0 || ua1.indexOf("QQ")>0) {
//				$("#open_app_ifr").attr("src","https://appsto.re/cn/iR2qM.i");
				hasApp("https://appsto.re/cn/iR2qM.i");
			} else {
				hasApp("huijiayou://huijiayou.com?"+openUrl);
			}
		}else if(browser.versions.android){
			if(ua1.indexOf("MicroMessenger")>0|| ua1.match(/MicroMessenger/i)=="micromessenger") {
				window.open("http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850");
			} else if(ua1.indexOf("qq")>0 || ua1.indexOf("QQ")>0){
				hasApp("huijiayou://huijiayou.com?"+openUrl+"#Intent;scheme=http;package=com.jiayou.qianheshengyun.app;end");
			} else {
				hasApp("huijiayou://huijiayou.com?"+openUrl+"#Intent;scheme=http;package=com.jiayou.qianheshengyun.app;end");
			}
		} else {
			window.open("http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850"); 
		}
		window.setTimeout(function () {
			if (window.location.href.indexOf('?') < 1) {
				window.open(window.location.href+'?mobile=1&refresh=1');
			}else{
				window.open(window.location.href+'&refresh=1');
			}
		}, 500);
	}
}