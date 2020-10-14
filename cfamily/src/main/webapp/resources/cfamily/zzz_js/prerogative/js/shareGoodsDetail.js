//打开惠家有客户端
var browser = '',ua = '';

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
			}
			
		}catch(e){}
		
		open1(openUrl);
		
		
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
	ua = navigator.userAgent.toLowerCase();
}
function up_urlparam(sKey, sUrl) {
	var sReturn = "";

	if (!sUrl) {
		sUrl = window.location.href;
		if (sUrl.indexOf('?') < 1) {
			sUrl = sUrl + "?";
		}
	}
	sUrl = decodeURIComponent(sUrl);
	var sUrlArr = sUrl.split('?');
	if(sUrlArr.length >= 3) {
		sReturn = up_urlparam_sk(sKey, sUrl);
		return sReturn;
	}
	var sParams = sUrlArr[1].split('&');

	for (var i = 0, j = sParams.length; i < j; i++) {

		var sKv = sParams[i].split("=");
		if (sKv[0] == sKey) {
			sReturn = sKv[1];
			break;
		}
	}

	return sReturn;

}
///*********************************************************************************************
function up_urlparam_sk(sKey, sUrl) {
	var sReturn = "";

	if (!sUrl) {
		sUrl = window.location.href;
		if (sUrl.indexOf('?') < 1) {
			sUrl = sUrl + "?";
		}
	}
	
	var urlArr = sUrl.split("?");
	var params = [];
	for(var i=0;i<urlArr.length;i++) {
		var paramArr = urlArr[i].split("&");
		for(var j=0;j<paramArr.length;j++) {
			params.push(paramArr[j]);
		}
	}
	for (var i = 0, j = params.length; i < j; i++) {

		var sKv = params[i].split("=");
		if(sKey == "link") {
			sReturn = sUrl.substring(sUrl.indexOf("link")+5);
			break;
		} else if (sKv[0] == sKey) {
			sReturn = sKv[1];
			break;
		}
	}

	return sReturn;

}
function repalce_urlparam_val(sKey,nValue,sUrl){
	
	var sReturn = "";
	if (!sUrl) {
		sUrl = window.location.href;
		if (sUrl.indexOf('?') < 1) {
			sUrl = sUrl + "?";
		}
	}
	//处理编码
	sUrl = upParam(sUrl);
	if(sUrl.indexOf(sKey) == -1){
		return (sUrl.indexOf("?") == -1) ? (sUrl+"?"+sKey+"="+nValue) : (sUrl+"&"+sKey+"="+nValue);
	}
	
	var sParams = sUrl.split('?')[1].split('&');
	
	for (var i = 0, j = sParams.length; i < j; i++) {

		var sKv = sParams[i].split("=");
		if (sKv[0] == sKey) {
			sReturn = sKv[1];
			break;
		}
	}
	var repalceStr = sKey+"="+sReturn;
	return (sUrl.substring(0,sUrl.indexOf(sKey)) +sKey+"="+nValue+sUrl.substring(sUrl.indexOf(repalceStr)+repalceStr.length));
}
function upParam(url) {
	
	var reg1=new RegExp("%2F","g");
	var reg2=new RegExp("%3A","g");
	var reg3=new RegExp("%3F","g");
	var reg4=new RegExp("%3D","g");
	url = url.replace(reg1, "/");
	url = url.replace(reg2, ":");
	url = url.replace(reg3, "?");
	url = url.replace(reg4, "=");
	return url;
}
//加密，解密url的&，？符号
//true为加密，false为解密
function url_param_mix(s_url,isMix){
	if(!s_url){
		return "";
	}
	var param = s_url;
	if(isMix){
		param = param.replace(/[\?]/g, "1M1");
		param = param.replace(/[\&]/g, "1M2");
		param = param.replace(/[\=]/g, "1M3");
	}else{
		param = param.replace(/(1M1)/g, "?");
		param = param.replace(/(1M2)/g, "&");
		param = param.replace(/(1M3)/g, "=");
	}
	return param;
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
    }, 3000)  
  
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
	
	open1(openUrl);
}
function open1(openUrl) {
	if(ua.indexOf("MicroMessenger")>0|| ua.match(/MicroMessenger/i)=="micromessenger") {
		window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850";
		
	} else
		if(browser.versions.android){
		//window.location = "huijiayou://huijiayou.com?"+openUrl+"#Intent;scheme=http;package=com.jiayou.qianheshengyun.app;end";

		//$(this).get(0).onclick = applink('http://www.ichsy.cn/apps/');
		window.location.href = "http://sj.qq.com/myapp/detail.htm?apkName=com.jiayou.qianheshengyun.app";

	} else if(browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
		//window.location = "huijiayou://huijiayou.com?"+openUrl+"#Intent;scheme=http;package=com.jiayou.qianheshengyun.app;end";
		/*window.location = "https://itunes.apple.com/cn/app/hui-jia-you/id641952456?mt=8";
		$(this).get(0).onclick = applink('https://appsto.re/cn/iR2qM.i');*/
		window.location.href = "https://itunes.apple.com/cn/app/hui-jia-you/id641952456?mt=8";
	} else {
		window.location.href= "http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850";
	}
}
function applink(backUrl){  
    return function(){  
        var clickedAt = +new Date;  
         setTimeout(function(){
             !window.document.webkitHidden && setTimeout(function(){ 
                   if (+new Date - clickedAt < 2000){  
                       window.location = backUrl;  
                   }  
             }, 500);       
         }, 500)   
    };  
} 