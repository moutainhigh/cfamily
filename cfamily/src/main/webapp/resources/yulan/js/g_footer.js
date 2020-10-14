/// <reference path="g_header.js" />
/// <reference path="functions/g_Type.js" />
/// <reference path="functions/g_Const.js" />
/// <reference path="jquery-2.1.4.js" />

if (!IsDebug) {
    var _hmt = _hmt || [];
    (function () {
        //百度统计
        try {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?affbddf9bb0f531d6c0dab3e68ac89d0";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
            //百度竞价
            var hm1 = document.createElement("script");
            hm1.src = "//hm.baidu.com/hm.js?05634cc220f874c4eb61a399d90d296b";
            var s1 = document.getElementsByTagName("script")[0];
            s1.parentNode.insertBefore(hm1, s1);
        } catch (e) {

        }
    })();
    try {
        var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://"); document.write(unescape("%3Cspan id='cnzz_stat_icon_1256669490'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "w.cnzz.com/q_stat.php%3Fid%3D1256669490' type='text/javascript'%3E%3C/script%3E"));
    } catch (e) {

    }
}
$(document).ready(function () {
    var url = location.pathname + location.search;
    if (localStorage.getItem(g_const_localStorage.PagePathList) != null && localStorage.getItem(g_const_localStorage.PagePathList).length > 0) {
        g_const_PagePathList = localStorage.getItem(g_const_localStorage.PagePathList).split(',');
    }
    if (g_const_PagePathList[g_const_PagePathList.length - 1] == url && (!(g_const_PagePathList[g_const_PagePathList.length - 1] == "/index.html" || g_const_PagePathList[g_const_PagePathList.length - 1] == "/"))) {
        g_const_PagePathList.pop();
        localStorage[g_const_localStorage.PagePathList] = g_const_PagePathList;
    }

    if (!($(".ui-loader") == undefined)) {
        $(".ui-loader").hide();
    }

//判断是否IphoneX手机
    if (typeof (IsIphoneX) != "undefined") {
        if (IsIphoneX()&& (window.location.href.indexOf("InvitingFr") == -1)) {
            
			var csslink = document.createElement("link");
			csslink.type = "text/css";
			csslink.rel = "stylesheet";
			csslink.href = cdn_css+"/css/iPhonex.css";

			var length1 = document.getElementsByTagName("script").length;
			var cusScript1 = document.getElementsByTagName("script")[length1 - 1];
			cusScript1.parentNode.insertBefore(csslink, cusScript1);
			
			
			/*var l = document.createElement('link');
			l.setAttribute('rel','stylesheet');
			l.setAttribute('type','text/css');
			l.setAttribute('href',cdn_css+'/css/iPhonex.css');
			document.body.appendChild(l);
			*/
        }
    }
});


//如果来自与分享，则隐藏页面导航
try{
    if (GetQueryString("fromshare") == g_const_YesOrNo.YES.toString()) {
        $("header").css("display", "none");
    }
}
catch (e) {
    console.log("g_footer设定分享功能报错："+e);
}