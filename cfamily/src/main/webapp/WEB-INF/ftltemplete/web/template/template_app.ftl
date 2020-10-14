<!DOCTYPE html>
<html lang="en">
<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<#assign weiXinUrl=b_method.upConfig("cfamily.hjyWeiXinUrl") >
<#assign wGSRegistUrl=b_method.upConfig("cfamily.hjyWGSRegistUrl") >
<#assign liveListApi=b_method.upConfig("cfamily.liveListUrl") >
<head>

    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
	<meta charset="UTF-8">
	
	<title>专题</title>
	<link rel="icon" type="image/png" href="../../resources/cfamily/zzz_js/favicon.png">
    <link href="../../resources/css/template/style.css" rel="stylesheet">
    <#--2016.5.4-->
    <link rel="stylesheet" type="text/css" href="../../resources/css/template/base.css" />
    <link rel="stylesheet" type="text/css" href="../../resources/css/template/muban_01.css" />
    <#--2016.5.20
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=OAawGgYp4PDSMdtQHFXLosCoSyF9O2dQ"></script>
	-->
    <#--2016.6.6
    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=fc9f7d4f27125470af335e827456fe2e&plugin=AMap.Geocoder"></script>
    -->
    <script src="http://pv.sohu.com/cityjson?ie=utf-8"></script>  <#--document.write(returnCitySN["cip"]+','+returnCitySN["cname"])  -->
    
    <script src="../../resources/cfamily/zzz_js/template/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="../../resources/cfamily/js/template.js"></script>
    <script type="text/javascript" src="${ss_assign_resources}js/cfamily.js"></script>
    <script type="text/javascript" src="../../resources/sewise-player/player/sewise.player.min.js"></script>    
	<script type="text/javascript" src="../../resources/cfamily/zzz_js/ppth_todaysales_new_one/tost.js"></script> 
	<script type="text/javascript" src="../../resources/cfamily/zzz_js/template/demo1130_swipe.js"></script>
    <style>
    	#toastMessage{ top:50%; width:auto; position:fixed; z-index:9999;}
		#toastMessage span{ display:block;  overflow:hidden}
		#toastMessage .sbg{ background:#000; color:#fff;opacity:.9; z-index:1; width:100%; height:100%;position:absolute; left:0; top:0; border-radius:5px; -webkit-border-radius:5px; }
		#toastMessage .stxt{padding:.8em 1em; font-size:.15rem; line-height:.15rem; z-index:2; color:#fff; text-align:center; position:relative}
		    	
    </style>
<#-- 添加百度统计  -->
<script>
	var _hmt = _hmt || [];
	(function() {
	  var hm = document.createElement("script");
	  hm.src = "//hm.baidu.com/hm.js?4b558c865d49b9bedd272692d183c227";
	  var s = document.getElementsByTagName("script")[0]; 
	  s.parentNode.insertBefore(hm, s);
	})();
	
	var tvTempleteHeight = "230px";//直播视频模板的高度设置(单位：px)
	
	
</script>

<#-- 自动加载高度 
<script type="text/javascript" language="javascript">
function iFrameHeight() {
var ifm= document.getElementById("mainweb");
var subWeb = document.frames ? document.frames["mainweb"].document :
ifm.contentDocument;
if(ifm != null && subWeb != null) {
ifm.height = subWeb.body.scrollHeight;
}
}
</script>
-->
</head>
<body  id="bdy">

<@ss_common_jscall "template.init('${weiXinUrl}','${wGSRegistUrl}','${liveListApi}');"/>


<#-- 获取屏幕宽度  -->
<script>
	try{
	    setTimeout(function(){
			$("body").css("width",parent.$("body").width()+"px");
	    },300);
	} catch(e){}
</script>


