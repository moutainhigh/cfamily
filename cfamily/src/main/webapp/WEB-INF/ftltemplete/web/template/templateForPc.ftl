<!doctype html>
<html>
<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<#assign pcProductDetailUrl=b_method.upConfig("cfamily.hjyPcProductDetailUrl") >
<#assign wGSRegistUrl=b_method.upConfig("cfamily.hjyWGSRegistUrl") >
<#assign liveListApi=b_method.upConfig("cfamily.liveListUrl") >

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=9">
    <base target="_blank">
    <title>专题</title>
    <link rel="icon" type="image/png" href="../../resources/cfamily/zzz_js/favicon.png">
    <link href="../../resources/css/template/style_pc.css" rel="stylesheet">
    
    <script src="../../resources/cfamily/zzz_js/template/jquery-1.11.1.min.js"></script>
    
    <#-- 获取用户信息 微信商城及pc获取用户手机号-->
	<script type="text/javascript" src="http://test-wx.huijiayou.cn/GetPhone.aspx"></script> 
	
    <script type="text/javascript" src="${ss_assign_resources}js/cfamily.js"></script>
    <script type="text/javascript" src="../../resources/cfamily/js/templateForPc.js"></script>
	<script type="text/javascript" src="../../resources/cfamily/zzz_js/template/demo1130_swipe.js"></script>
	<script type="text/javascript" src="../../resources/cfamily/zzz_js/ppth_todaysales_new_one/tost.js"></script>
	
	
    <#-- 添加百度统计  -->
	<script>
		var _hmt = _hmt || [];
		(function() {
		  var hm = document.createElement("script");
		  hm.src = "//hm.baidu.com/hm.js?4b558c865d49b9bedd272692d183c227";
		  var s = document.getElementsByTagName("script")[0]; 
		  s.parentNode.insertBefore(hm, s);
		})();
		
		
		var tvTempleteHeight = "400px";//直播视频模板的高度设置(单位：px)
	</script>
</head>
<body id="bodyInfo">
	
<@ss_common_jscall "template.init('${pcProductDetailUrl}','${wGSRegistUrl}','${liveListApi}');"/>

