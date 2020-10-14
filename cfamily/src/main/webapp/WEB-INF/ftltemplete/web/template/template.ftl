<!DOCTYPE html>
<html lang="en">
<#include "../shareshoppingmacro/shareshopping_common.ftl" />
<#assign weiXinUrl=b_method.upConfig("cfamily.hjyWeiXinUrl") >
<#assign wGSRegistUrl=b_method.upConfig("cfamily.hjyWGSRegistUrl") >
<#assign liveListApi=b_method.upConfig("cfamily.liveListUrl") > 
<#assign hjyWeiXinHttp=b_method.upConfig("cfamily.hjyWeiXinHttp") >
<#assign readWXClientMobileScript=b_method.upConfig("cfamily.readWXClientMobileUrl") >
<#assign mobile=RequestParameters["mobile"]! />
<#assign token=RequestParameters["token"]! />
<#assign templateService=b_method.upClass("com.cmall.familyhas.service.TemplateService") />
<#assign decodeResult=templateService.decodeMobileAndToken(mobile,token) />

<head>

    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta content="telephone=no" name="format-detection" />
    <meta content="email=no" name="format-detection" />
	<meta charset="UTF-8">
	
	<title>专题</title>
	<link rel="icon" type="image/png" href="../../resources/cfamily/zzz_js/favicon.png">
    <link href="${b_method.upConfig("familyhas.zt_host1")}cfamily/resources/css/template/style.css?v=3_2" rel="stylesheet">
    <link href="${b_method.upConfig("familyhas.zt_host1")}cfamily/resources/css/template/style_1.css?v=5" rel="stylesheet">
    <link href="${b_method.upConfig("familyhas.zt_host1")}cfamily/resources/css/template/swiper.css" rel="stylesheet">
    <#--2016.5.4-->
    <link rel="stylesheet" type="text/css" href="${b_method.upConfig("familyhas.zt_host1")}cfamily/resources/css/template/base.css?v=1" />
    <link rel="stylesheet" type="text/css" href="${b_method.upConfig("familyhas.zt_host2")}cfamily/resources/css/template/muban_01.css?v=2" />
    <#--2016.5.20
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=OAawGgYp4PDSMdtQHFXLosCoSyF9O2dQ"></script>
	-->
    <#--2016.6.6
    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=fc9f7d4f27125470af335e827456fe2e&plugin=AMap.Geocoder"></script>

	<#--<script src="https://pv.sohu.com/cityjson?ie=utf-8"></script>  <#--document.write(returnCitySN["cip"]+','+returnCitySN["cname"])  -->
    
    <script src="${b_method.upConfig("familyhas.zt_host2")}cfamily/resources/cfamily/zzz_js/template/jquery-1.11.1.min.js"></script>
    <#-- 获取用户信息 微信商城及pc获取用户手机号-->
	<script type="text/javascript" src="${readWXClientMobileScript}"></script>   
	
	<#-- 微信小程序JS -->
	<script src="https://res.wx.qq.com/open/js/jweixin-1.4.0.js"></script>
	<script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host2")}cfamily/resources/cfamily/js/jweixinHelper.js?v=20190314"></script>
	<script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host2")}cfamily/resources/yulan/js/g_app.js"></script>  
    <#-- js交互 -->
    <script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host2")}cfamily/resources/cfamily/js/template.js?v=20200628"></script>
    <script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host3")}cfamily/resources/cfamily/js/times.js?v=20191021"></script>
    <script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host3")}cfamily/resources/cfamily/js/template_type.js?v=20200529"></script>

    <script type="text/javascript" src="${ss_assign_resources}js/cfamily.js"></script>
    <script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host3")}cfamily/resources/sewise-player/player/sewise.player.min.js"></script>    
	<script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host3")}cfamily/resources/cfamily/zzz_js/ppth_todaysales_new_one/tost.js"></script> 
	<script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host4")}cfamily/resources/cfamily/zzz_js/template/demo1130_swipe.js"></script>
	<script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host4")}cfamily/resources/cfamily/zzz_js/template/iscroll.js"></script>	
	<script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host4")}cfamily/resources/cfamily/zzz_js/template/base64.js"></script>
	<script type="text/javascript" src="${b_method.upConfig("familyhas.zt_host2")}cfamily/resources/cfamily/js/template618.js?v=20200528"></script>
    <style>
    	#toastMessage{ top:50%; width:auto; position:fixed; z-index:9999;}
		#toastMessage span{ display:block;  overflow:hidden}
		#toastMessage .sbg{ background:#000; color:#fff;opacity:.9; z-index:1; width:100%; height:100%;position:absolute; left:0; top:0; border-radius:5px; -webkit-border-radius:5px; }
		#toastMessage .stxt{padding:.8em 1em; font-size:.15rem; line-height:.15rem; z-index:2; color:#fff; text-align:center; position:relative}
		.rebate ul li p .quan_542{ height:0.28rem;padding-left:0;margin-left:-0.03rem;overflow: hidden;}
		.rebate ul li p .quan_542 span{margin-top:0; line-height:0.12rem;display:inline-block; padding:0.02rem 0.05rem; font-size:0.12rem; color:#ff6c39; border-radius:2px; border:1px solid #ff6c39;float:left;margin-left:0.03rem;height:0.12rem;}
		.goods a p{padding-top:0.05rem}
		.goods a .quan_542_1{ height:0.2rem;padding:0;margin-left:-0.03rem;overflow: hidden;}
		.goods a .quan_542_1 span{margin-top:0; line-height:0.12rem;display:inline-block; padding:0.02rem 0.05rem; font-size:0.12rem; color:#ff6c39; border-radius:2px; border:1px solid #ff6c39;float:left;margin-left:0.03rem;height:0.12rem;}
		.goods a p font span{margin-top:-0.05rem}    
		
		
		
		.jbsm_550{ width:94%;  margin:auto; height:auto; overflow:hidden;}
		.sp_550{ margin-top:0.15rem; padding-bottom:0.1rem;height:auto; overflow:hidden; background-color:#fff; border-radius:8px 8px 0 0 }
		.sp_550 .img_550 img{ width:100%;}
		.sp_550 .spm_550{ margin:5px 3%; font-size:18px; color:#222; font-weight:bold; max-height:50px; line-height:25px; overflow:hidden}
		.sp_550 .spj_550{padding:0 3%; color:#ff6100; font-size:16px; font-weight:bold;}
		.sp_550 .spj_550 strong{color:#ff6100; font-size:22px;}
		.sp_550 .spj_550 span{color:#999999; font-size:14px; text-decoration:line-through; font-weight:100; margin-left:15px;}	
		
		.xfmb_550{ width:50px; height:50px;overflow:hidden;  position:fixed; right:4%; top:55px; z-index:9999;border-radius:25px;}
		.xfmb_550 img{ width:50px; height:50px;}
		.right_nav{ overflow:hidden;  position:fixed; left:2%; top:25px; z-index:9999;border-radius:25px;}
    </style>
    <style>
    	*{ margin:0; padding:0}
		ul li{ list-style:none}
		body, div, dl, dt, dd, ul, ol, li, h1, h2, h3, h4, h5, h6, pre, code, form, fieldset, legend, input, textarea, p, blockquote, th, td, ul,span {margin: 0;padding: 0; font-family:"微软雅黑"}
		.div_ng{ max-width:850px; min-width:320px; margin:auto;}
		.toutu{ position:relative; height:auto;}
		.toutu img{ width:100%;display: block;}
		.dhm_w{ padding:2% 0;}
		.dhm_w p{ text-align:center; line-height:25px; color:#fff; font-size:16px;}
		.ul_dhli{ padding:0 2.78%; margin-bottom:30px; height:auto; overflow:hidden}
		.ul_dhli li{ float:left; margin-top:8px; height:auto; overflow:hidden; position:relative; border-radius:5px; width:45.83%; margin-left:2.78%; background-color:#fff;}
		.ul_dhli li img{ width:100%;}
		.ul_dhli li .sp_m{ line-height: 20px; padding:0 3%; margin-top:2%; height:20px; overflow:hidden; text-overflow:ellipsis;white-space: nowrap;font-size:16px;}
		.ul_dhli li .no_g{ position:absolute; width:60%; top:11%; left:20%;margin-top:7px; display:none}
		.ul_dhli li .sp_j{ color:#ff6932;padding:0 3%; margin-top:2%;font-size:16px;}
		.ul_dhli li .sp_tj img{ width:80%; margin-left:10%; margin-top:2%; margin-bottom:4%;}
		.ul_dhli li .sp_tj{height:0.55rem}
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
$('html,body').animate({scrollTop: '0px'}, 800);
$('.noContent').css('width','746px');
</script>
-->
</head>
<body  id="bdy">
<input type="hidden" id="zt_618_pnm" value="${b_method.upConfig("cfamily.zt_618_pnm")}">
</body>
</html>   
<#-- 先初始化分享函数  -->
<script>
	jweixinHelper.init('${hjyWeiXinHttp}Ajax/API.aspx');	
</script>
<@ss_common_jscall "template.init('${weiXinUrl}','${wGSRegistUrl}','${liveListApi}','${hjyWeiXinHttp}','${decodeResult.mobile}','${decodeResult.token}');"/>
<#-- 获取屏幕宽度  -->
<script>
	try{
	    setTimeout(function(){
			$("body").css("width",document.body.clientWidth+"px");
	    },300);
	} catch(e){}
	window.onscroll = function () {
    if (document.documentElement.scrollTop + document.body.scrollTop > 400) {
        
        $("#top_542").show();
    }
    else {
        $("#top_542").hide();
    } 
}
</script>

<script type="text/javascript">
var loadImageTaskId = 0;
$(function() {
    $(window).scroll(function() {
    	// 清理上一次未执行的函数
       //clearTimeout(loadImageTaskId);
       	// 设置延迟100毫秒，防止重复执行
       //loadImageTaskId = setTimeout("loadImage()",100);
       loadImage(3);
       // 刷新618活动位置
       template618.refreshPosition();
    });
    
           
});
//延迟加载图片  
function loadImage(n) {  
	n = n || 1;  
	var winH = $(window).height(); //页面可视区域高度
    var scrollT = $(window).scrollTop(); //滚动条top
    $("li.type2").each(function(){
    	var $typeObj = $(this);
    	if(!$typeObj.attr('loaded')){ // 只对未加载的图片做处理
	     	var h = getH(this);
	     	var mh = $typeObj.height();
	     	// 只对当前区域显示的部分做图片加载
	     	// 以当前可视区域的上限位置和下限位置做判断，当前项顶部小于下限位置且底部大于上限位置
	    	if((h+mh) > scrollT && h < (winH*n + scrollT)){
	    		$(this).find("img.type2").filter('[src="../../resources/images/defaultImg/pc_list_defalt_main_img.png"]').each(function(){
	    			var src= $(this).attr("data-src");
	    			this.src = src;
	    			// 设置已加载的标识，避免重复加载
	    			$typeObj.attr('loaded',true);
	    		});
	    	}   	
    	}
    }); 
}    
//获得对象距离页面顶端的距离  
function getH(obj) {  
    var h = 0;  
    while (obj) {  
        h += obj.offsetTop;  
        obj = obj.offsetParent;  
    }  
    return h;  
}

window.addEventListener('pageshow', function (e) {
	template618.onPageShow(e)
})
</script>
