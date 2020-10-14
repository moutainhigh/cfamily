//app下载
var downloadapp = {
	init : function() {
		downloadapp.checkOsType();
		//var showRebateMsg = zapbase.up_urlparam("showRebateMsg");
		$("#newUserShowRebateId").show();
		/*if(showRebateMsg && showRebateMsg.length > 0 && showRebateMsg == '0') {
			$("#newUserShowRebateId").hide();
			$(".banner").attr("style","margin: 4rem 0 0;");
		} else {
			$("#newUserShowRebateId").show();
		}*/
	},
	//app下载，osType为空时会自动判定平台
	download : function(osType) {
		var toqqAppHref = "http://a.app.qq.com/o/simple.jsp?pkgname=com.jiayou.qianheshengyun.app&g_f=991850";
		var isAndroid = false;
		var isIOS = false;
		if(osType && osType.length > 0) {
			if(osType == 'android') {
				isAndroid = true;
			} else if(osType == 'ios') {
				isIOS = true;
			}
		} else {
			var u = navigator.userAgent, app = navigator.appVersion;
			isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1;
			isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
		}
		
		var ua = navigator.userAgent.toLowerCase();
		//alert(ua.math(/QQ/i)=="qq");
		try {
			if(ua.match(/MicroMessenger/i) == "micromessenger" || ua.match(/weibo/i) == "weibo" || (ua.indexOf('qqbrowser')>-1)) {
				document.location = toqqAppHref;
			} else if(true == isIOS) {
				document.location = 'https://itunes.apple.com/cn/app/jia-you-gou-wu/id641952456?mt=8';
			} else {
				document.location = 'http://www.ichsy.com/apps/Hui_Jia_You_ichsy.apk';
			}
		} catch(e) {
			capi.show_message("请稍等");
		}
		
	},
	//判定系统类型
	checkOsType : function() {
		var u = navigator.userAgent, app = navigator.appVersion;
		var ua = navigator.userAgent.toLowerCase();
		var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1;
		var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
		if(true == isiOS){
			$('#androidDownload').hide();
		}else if(true == isAndroid) {
			$('#iosDownload').hide();
		}
	}
}